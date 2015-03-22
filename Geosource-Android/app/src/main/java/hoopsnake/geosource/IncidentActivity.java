package hoopsnake.geosource;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.locks.ReentrantLock;

import ServerClientShared.Incident;
import hoopsnake.geosource.comm.TaskReceiveIncidentSpec;
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

    private final Geotag geotag = new Geotag();

    private static final String LOG_TAG = "geosource";
    public static final String PARAM_STRING_CHANNEL_NAME = "channelName";
    public static final String PARAM_STRING_CHANNEL_OWNER = "channelOwner";
    public static final String PARAM_STRING_POSTER = "poster";

    private static final String SHAREDPREF_INCIDENT = "sharedpref_incident";

    /** This holds the incident, and passes it to the incidentDisplay for display. */
    IncidentDisplayAdapter incidentAdapter;

    /** The LinearLayout that displays all the fields of the incident. */
    private LinearLayout incidentDisplay;

    /** The incident to be created and edited by the user on this screen. */
    private AppIncident incident;

    public void setCurFieldIdx(int curFieldIdx) {
        this.curFieldIdx = curFieldIdx;
    }

    /**
     * The position of the currently-selected field in the incidentDisplay.
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
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        if ((extras == null || extrasAreEmpty(extras)) && sharedPref.contains(SHAREDPREF_INCIDENT))
            initializeAppIncidentFromSharedPref(sharedPref);
        else if (extras != null)
            initializeAppIncidentFromServer(extras);
        else
            throw new RuntimeException("invalid onCreate scenario for IncidentActivity.");
    }

    private void initializeAppIncidentFromSharedPref(SharedPreferences sharedPref)
    {
        String jsonIncident = sharedPref.getString(SHAREDPREF_INCIDENT, null);
        Gson gson = new Gson();
        Incident curIncident = gson.fromJson(jsonIncident, Incident.class);
        assertNotNull(curIncident.getChannelName());
        assertNotNull(curIncident.getFieldList());
        assertNotNull(curIncident.getOwnerName());
        assertNotNull(curIncident.getPosterName());

        setIncident(new AppIncidentWithWrapper(curIncident, IncidentActivity.this));

        renderIncidentFromScratch();
    }

    private void initializeAppIncidentFromServer(Bundle extras)
    {
        geotag.update(IncidentActivity.this);

        String channelName = extras.getString(PARAM_STRING_CHANNEL_NAME);
        String channelOwner = extras.getString(PARAM_STRING_CHANNEL_OWNER);
        String poster = extras.getString(PARAM_STRING_POSTER);
        assertNotNull(channelName);
        assertNotNull(channelOwner);
        assertNotNull(poster);

        incidentDisplay = (LinearLayout) findViewById(R.id.incident_holder);

        new TaskReceiveIncidentSpec(IncidentActivity.this).execute(channelName, channelOwner, poster);
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
     * @precond the current incident, and all its fields, and the incidentDisplay, are not null.
     * @postcond each field's custom view is added to the linear layout, replacing all the old
     * views in the linear layout (if they existed).
     */
    public void renderIncidentFromScratch()
    {
        assertNotNull(incident);
        assertNotNull(incident.getFieldList());
        assertNotNull(incidentDisplay);

        //TODO grosssss... this accounts for refilling an incident from shared preferences, but man...
        if (!incident.getFieldList().get(Incident.POSITION_GEOTAG_FIELD).contentIsFilled())
            incident.setGeotag(geotag);

        incidentDisplay.removeAllViews();
        int i = 0;
        for (AppField field : incident.getFieldList())
        {
            assertNotNull(field);

            View v = field.getContentViewRepresentation(RequestCode.FIELD_ACTION_REQUEST_CODE.ordinal());
            assertNotNull(v);

            incidentDisplay.addView(v);
            //All views are given a tag that is equal to their position in the linear layout.
            v.setTag(i);
            i++;
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
     * @precond incident is not null.
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
    public void makeViewLaunchable(View v, final Runnable onClickLaunchable)
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
    protected void onDestroy() {
        super.onDestroy();

        //TODO this code doesn't work, because gson tries to serialize the content of every field in the incident. Yikes!
//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//        if (incident == null && sharedPref.contains(SHAREDPREF_INCIDENT))
//            editor.remove(SHAREDPREF_INCIDENT);
//        else
//        {
//            Gson gson = new Gson();
//            String json = gson.toJson(incident.toIncident());
//            editor.putString(SHAREDPREF_INCIDENT, json);
//        }
//        editor.commit();
    }
}

