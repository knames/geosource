package hoopsnake.geosource;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.concurrent.locks.ReentrantLock;

import hoopsnake.geosource.comm.TaskReceiveIncidentSpec;
import hoopsnake.geosource.data.AppFieldWithContent;
import hoopsnake.geosource.data.AppIncident;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * @author wsv759
 * The activity that controls the app when the user is filling out a new incident.
 * This is called from MainActivity when the "add new incident" button is pressed.
 * This activity also launches the tasks to receive a new incident spec and send a new incident.
 */
public class IncidentActivity extends ActionBarActivity {
    private boolean clickable = true;
    private final ReentrantLock clickableLock = new ReentrantLock();

    public static final String PARAM_STRING_CHANNEL_NAME = "channelName";
    public static final String PARAM_STRING_CHANNEL_OWNER = "channelOwner";

    /** This holds the incident, and passes it to the incidentDisplay for display. */
    IncidentDisplayAdapter incidentAdapter;

    /** The LinearLayout that displays all the fields of the incident. */
    private LinearLayout incidentDisplay;

    public void setIncident(AppIncident incident) {
        this.incident = incident;
    }

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
     * Initialize the display and the adapted, and send off for the incident spec, based on the channel name
     * provided by the MainActivity that called this.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        Bundle extras = getIntent().getExtras();
        assertNotNull(extras);

        String channelName = extras.getString(PARAM_STRING_CHANNEL_NAME);
        String channelOwner = extras.getString(PARAM_STRING_CHANNEL_OWNER);
        assertNotNull(channelName);
        assertNotNull(channelOwner);

        incidentDisplay = (LinearLayout) findViewById(R.id.incident_holder);

        new TaskReceiveIncidentSpec(IncidentActivity.this).execute(channelName, channelOwner);
    }

    /**
     * @precond the current incident, and all its fields, and the incidentDisplay, are not null.
     * @postcond each field's custom view is added to the linear layout, replacing all the old
     * views in the linear layout (if they existed).
     */
    public void renderIncident()
    {
        assertNotNull(incident);
        assertNotNull(incident.getFieldList());
        assertNotNull(incidentDisplay);

        incidentDisplay.removeAllViews();
        int i = 0;
        for (AppFieldWithContent field : incident.getFieldList())
        {
            //TODO remove the commented out test code.
            assertNotNull(field);
//            TextView tv = new TextView(IncidentActivity.this);
//            tv.setText(field.getContentStringRepresentation());
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
                AppFieldWithContent curField = incident.getFieldList().get(curFieldIdx);
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
            Toast.makeText(IncidentActivity.this, "success! Incident submitted.", Toast.LENGTH_LONG).show();
//            new TaskSendIncident(IncidentActivity.this).execute(incident.toIncident());
        }
        else
            Toast.makeText(IncidentActivity.this, "incident has not been completely filled in!",Toast.LENGTH_LONG).show();
    }


    /**
     * Test-and-set: atomically check if this activity can be clicked, and if it can, say that no other
     * thread can click it.
     * @return true if the activity is not busy and can be clicked, false otherwise.
     */
    private boolean canBeClicked() {
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
    private void doneClicking() {
        clickableLock.lock();
        assertFalse(clickable);
        clickable = true;
        clickableLock.unlock();
    }

    /**
     *
     * @param taskUsingActivity a task that needs to be run by some other class, using this activity's code.
     * @precond taskUsingActivity is not null.
     * @postcond taskUsingActivity is run if this activity is not busy; otherwise it does not run.
     */
    public void doIfClickable(Runnable taskUsingActivity)
    {
        if (canBeClicked())
        {
            taskUsingActivity.run();

            doneClicking();
        }
    }
}

