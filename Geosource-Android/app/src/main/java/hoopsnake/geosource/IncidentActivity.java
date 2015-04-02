package hoopsnake.geosource;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import ServerClientShared.FieldWithContent;
import ServerClientShared.GeotagFieldWithContent;
import ServerClientShared.GeotagFieldWithoutContent;
import ServerClientShared.ImageFieldWithContent;
import ServerClientShared.ImageFieldWithoutContent;
import ServerClientShared.Incident;
import ServerClientShared.StringFieldWithContent;
import ServerClientShared.StringFieldWithoutContent;
import hoopsnake.geosource.comm.TaskSendIncident;
import hoopsnake.geosource.data.AppField;
import hoopsnake.geosource.data.AppIncident;
import hoopsnake.geosource.data.AppIncidentWithWrapper;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author wsv759
 * The activity that controls the app when the user is filling out a new incident.
 * This is called from MainActivity when the "add new incident" button is pressed.
 * This activity also launches the tasks to receive a new incident spec and send a new incident.
 */
public class IncidentActivity extends ActionBarActivity {
    private boolean clickable = true;
    private final ReentrantLock clickableLock = new ReentrantLock();

    private static final String LOG_TAG = "geosource";
    public static final String PARAM_STRING_CHANNEL_NAME = "channelName";
    public static final String PARAM_STRING_CHANNEL_OWNER = "channelOwner";
    public static final String PARAM_STRING_POSTER = "poster";

    public static final String SHAREDPREF_CUR_INCIDENT_EXISTS = "sharedpref_incident_exists";
    private static final String FILENAME_CUR_INCIDENT = "cur_incident_object";
    public static final String DIRNAME_INCIDENTS_YET_TO_SEND = "incidents_yet_to_send";

    /** The incident to be created and edited by the user on this screen. */
    private AppIncident incident;

    public void setCurFieldIdx(int curFieldIdx) {
        this.curFieldIdx = curFieldIdx;
    }

    /**
     * The position of the currently-selected field in the incidentFieldsDisplay.
     * This is recorded so that different activities/fragments can be called whenever a field is clicked,
     * and the corresponding field can be remembered upon their return.
     */
    private int curFieldIdx = NO_CUR_FIELD_SELECTED;
    public static final int NO_CUR_FIELD_SELECTED = -1;

    /**
     * The set of all request codes that are used by this activity when starting new activities or fragments.
     */
    public enum RequestCode {
        FIELD_ACTION_REQUEST_CODE
    }

    /**
     * Initialize the display, and its underlying incident. There are only two legitimate cases for initializing the incident:
     *  1. There are no extras, but an incident is stored in the shared preferences.
     *      In this case we are resuming filling out an incident from before.
     *  2. There are extras, and no incident is stored in the shared preferences.
     *      In this case we send off for the incident spec, based on the channel name and channel owner provided by the extras.
     * @param savedInstanceState automatically handled by android.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);
        assertNull(incident);

        Bundle extras = getIntent().getExtras();

        if (extras == null || extrasAreEmpty(extras)) {
            //If we are expecting to retrieve the incident from a pre-existing state, but we can't, finish the activity.
            if (!initializeAppIncidentFromPreexistingState()) {
                saveIncidentState();
                leaveDueToLostIncident();
                return;
            }
        }
        else
            initializeAppIncidentFromServer(extras);
    }

    private boolean initializeAppIncidentFromPreexistingState()
    {
        if (!retrieveIncidentState())
            return false;

        assertNotNull(incident);
        assertNotNull(incident.toIncident());
        assertNotNull(incident.getFieldList());
        assertNotNull(incident.getChannelName());
        assertNotNull(incident.getChannelOwner());
        assertNotNull(incident.getIncidentAuthor());

        renderIncidentFromScratch(true);

        return true;
    }

    private void initializeAppIncidentFromServer(Bundle extras)
    {
        String channelName = extras.getString(PARAM_STRING_CHANNEL_NAME);
        String channelOwner = extras.getString(PARAM_STRING_CHANNEL_OWNER);
        String poster = extras.getString(PARAM_STRING_POSTER);
        assertNotNull(channelName);
        assertNotNull(channelOwner);
        assertNotNull(poster);

//        new TaskReceiveIncidentSpec(IncidentActivity.this).execute(channelName, channelOwner, poster);
        //TODO uncomment the above code once spec can be pulled properly, then remove up to "renderIncidentFromScratch()"
        ArrayList<FieldWithContent> l = new ArrayList<>();
        l.add(new StringFieldWithContent(new StringFieldWithoutContent("StringTitle", true)));
        l.add(new GeotagFieldWithContent(new GeotagFieldWithoutContent("GeotagTitle", true)));
        l.add(new ImageFieldWithContent(new ImageFieldWithoutContent("ImageTitle", true)));
        // etc.

        incident = new AppIncidentWithWrapper(new Incident(l, channelName, channelOwner, poster), IncidentActivity.this);
        renderIncidentFromScratch(true);
    }

    private boolean extrasAreEmpty(Bundle extras)
    {
        assertNotNull(extras);
        return !extras.containsKey(PARAM_STRING_CHANNEL_NAME);
    }

    public void setIncident(AppIncident incident) {
        this.incident = incident;
    }

    /**
     * @precond the current incident, and all its fields, and the incidentFieldsDisplay, are not null.
     * @postcond each field's custom view is added to the linear layout, replacing all the old
     * views in the linear layout (if they existed).
     *
     * IMPORTANT: All views are given a tag that is equal to their position in the linear layout.
     * They must preserve this tag, as IncidentActivity relies upon it.
     */
    public void renderIncidentFromScratch(boolean isFirstTime)
    {
        assertNotNull(incident);
        assertNotNull(incident.getFieldList());

        if (isFirstTime) {
            LinearLayout incidentMetadataDisplay = (LinearLayout) findViewById(R.id.incident_metadata_holder);
            assertNotNull(incidentMetadataDisplay);
            //Fill in the author, channel, and channel owner on the UI.
            TextView authorView = (TextView) incidentMetadataDisplay.getChildAt(1);
            authorView.setText(authorView.getText() + " " + incident.getIncidentAuthor());
            TextView channelNameView = (TextView) incidentMetadataDisplay.getChildAt(2);
            channelNameView.setText(channelNameView.getText() + " " + incident.getChannelName());
            TextView channelOwnerView = (TextView) incidentMetadataDisplay.getChildAt(3);
            channelOwnerView.setText(channelOwnerView.getText() + " " + incident.getChannelOwner());
        }

        LinearLayout incidentFieldsDisplay = (LinearLayout) findViewById(R.id.incident_fields_holder);
        assertNotNull(incidentFieldsDisplay);

        //TODO grosssss... this accounts for refilling an incident from shared preferences, but man... at least it could be put in a separate function.
        if (!incident.getFieldList().get(Incident.POSITION_GEOTAG_FIELD).contentIsFilled()) {
            AppGeotagWrapper geotagWrapper = new AppGeotagWrapper();
            incident.setGeotag(geotagWrapper);
            geotagWrapper.update(this);
        }

        if (!isFirstTime)
            incidentFieldsDisplay.removeAllViews();

        //Add the views for each field.
        for (AppField field : incident.getFieldList())
        {
            assertNotNull(field);

            Log.d(LOG_TAG, "content: " + field.getContentStringRepresentation());
            View v = field.getFieldViewRepresentation(RequestCode.FIELD_ACTION_REQUEST_CODE.ordinal());
            assertNotNull(v);

            incidentFieldsDisplay.addView(v);
        }
    }

    /**
     * When an activity launched from here returns, respond accordingly.
     * @param requestCode the request code with which the activity was launched.
     * @param resultCode the result of the activity.
     * @param data any extra data associated with the result. This could be null.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RequestCode requestCodeVal = RequestCode.values()[requestCode];
        switch(requestCodeVal)
        {
            case FIELD_ACTION_REQUEST_CODE:
                /**
                 * Delegate the response to the field whose selection launched the returning activity in the first place.
                 */
                assertFalse(curFieldIdx == NO_CUR_FIELD_SELECTED);
                AppField curField = incident.getFieldList().get(curFieldIdx);
                curField.onResultFromSelection(resultCode, data);
                break;
            default:
                throw new RuntimeException("invalid request code " + requestCode + ".");
        }
    }

    /**
     * Try to submit the incident to the server.
     * @param v the submit button.
     * @precond none.
     * @postcond the new incident is submitted to the server. The new incident may not end up going through.
     */
    public void onSubmitButtonClicked(View v)
    {
        if (incident != null && incident.isCompletelyFilledIn()) {
            //TODO uncomment this when actually using it.
            Toast.makeText(IncidentActivity.this, "Attempting to format and send your incident to server.", Toast.LENGTH_LONG).show();

            new TaskSendIncident(IncidentActivity.this).execute(incident);

            setIncident(null);
            setResult(RESULT_OK);
            finish();
        }
        else
            Toast.makeText(IncidentActivity.this, "incident has not been completely filled in!",Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @param v the cancel button.
     * @precond none.
     * @postcond Cancel: stop creating the current incident, and discard it forever.
     * (The media files created during the process will still be stored.)
     */
    public void onCancelButtonClicked(View v)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cancelling Incident Creation")
                .setMessage("Are you sure? This incident will be discarded forever. (Any media files you created will be preserved.)")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setIncident(null);
                        IncidentActivity.this.finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Test-and-set: atomically check if this activity can be clicked, and if it can, say that no other
     * thread can click it.
     * @return true if the activity is not busy and can be clicked, false otherwise.
     */
    private boolean canLaunch() {
        clickableLock.lock();
        if (clickable) {
            clickable = false;
            clickableLock.unlock();
            return true;
        }

        clickableLock.unlock();
        return false;
    }

    /**
     * When a thread is done using this activity, allow other threads to use it again.
     */
    private void doneLaunching() {
        clickableLock.lock();
        assertFalse(clickable);
        clickable = true;
        clickableLock.unlock();
    }

    /**
     *
     * @param taskThatLaunches a task that needs to be run by some other class, and uses this activity's code to launch an activity or fragment.
     * @precond taskThatLaunches is not null.
     * @postcond taskThatLaunches is run if this activity is not busy; otherwise it does not run.
     */
    private void doIfLaunchable(Runnable taskThatLaunches)
    {
        if (canLaunch())
        {
            taskThatLaunches.run();

            doneLaunching();
        }
    }

    /**
     *
     * @param v the view that needs to launch some activity or fragment on click.
     * @param onClickLaunchable a piece of runnable code that could launch an activity or fragment from this IncidentActivity.
     */
    public void makeViewLaunchable(final View v, final Runnable onClickLaunchable)
    {
        v.setClickable(true);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                doIfLaunchable(new Runnable() {
                    @Override
                    public void run() {
                        //Make sure the activity knows which view was clicked.
                        setCurFieldIdx((int) v.getTag());

                        onClickLaunchable.run();
                    }
                });
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        saveIncidentState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO save things like image bitmaps that have been created so they don't have to be regenerated each time. Basically, save incident state properly.
        if (incident == null && !retrieveIncidentState()) {
            leaveDueToLostIncident();
        }

        renderIncidentFromScratch(false);
    }

    /**
     * Incident state not guaranteed to be saved during execution of this function. Calling method is response for that.
     * (It is done automatically onPause() so there is no need to double-tap.)
     */
    private void leaveDueToLostIncident()
    {
        String msg = getString(R.string.incident_lost_media_saved);
        Log.e(LOG_TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Save the current incident state, if the incident has not yet been submitted.
     * This serializes the whole incident into a file, so that it can
     * be deserialized the next time the incident activity is launched.
     * TODO ensure the precond actually holds.
     * @precond as long as incident != null, no file content fields are filled. Thus there will be no
     * attempt to serialize a massive file content object!
     * @postcond the incident is serialized, so that it can be reopened later. Or if the incident is null,
     * no serialization occurs and a new incident will be created next time.
     *
     * NOTE: the sharedPreferences key SHAREDPREF_CUR_INCIDENT_EXISTS is updated accordingly. Other activities
     * should ask for that key to see whether a cur incident exists.
     */
    private void saveIncidentState()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_sharedpref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (incident == null) {
            deleteFile(FILENAME_CUR_INCIDENT);
            editor.putBoolean(SHAREDPREF_CUR_INCIDENT_EXISTS, false);
            editor.commit();
            return;
        }

        boolean fileWasWritten = FileIO.writeObjectToFile(IncidentActivity.this, (AppIncidentWithWrapper) incident, FILENAME_CUR_INCIDENT);
        if (fileWasWritten)
            editor.putBoolean(SHAREDPREF_CUR_INCIDENT_EXISTS, true);
        else
        {
            editor.putBoolean(SHAREDPREF_CUR_INCIDENT_EXISTS, false);
            String msg = getString(R.string.incident_lost_media_saved);
            Log.e(LOG_TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }

        editor.commit();
    }

    /**
     * @precond incident == null. Otherwise this method should not be called as you already have an incident!
     * @postcond
     * Retrieve the current incident state, by deserializing it from the file saved to by {@link #saveIncidentState()}.
     * The incident field is populated with the pre-existing one.
     * If there is no current incident to restore, this method returns false, and incident remains null.
     */
    private boolean retrieveIncidentState()
    {
        assertNull(incident);

        if (curIncidentExistsInFileSystem(this)) {
            incident = (AppIncident) FileIO.readObjectFromFile(this, FILENAME_CUR_INCIDENT);

            //If we couldn't retrieve the incident, we have to return to the main screen. :(
            if (incident == null)
                return false;

            for (AppField field : incident.getFieldList())
                field.setActivity(this);

            return true;
        }

        return false;
    }

    /**
     * @precond none.
     * @return true if there is a current incident serialized in the file system. False otherwise.
     */
    public static boolean curIncidentExistsInFileSystem(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_sharedpref_file_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(SHAREDPREF_CUR_INCIDENT_EXISTS, false);
    }
}

