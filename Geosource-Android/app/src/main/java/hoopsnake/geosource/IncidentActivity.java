package hoopsnake.geosource;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import ServerClientShared.Commands.IOCommand;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.Incident;
import hoopsnake.geosource.comm.SocketResult;
import hoopsnake.geosource.comm.SocketWrapper;
import hoopsnake.geosource.data.AppFieldWithContent;
import hoopsnake.geosource.data.AppIncident;
import hoopsnake.geosource.data.AppIncidentWithWrapper;

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
        //TODO uncomment this for real app.
        new TaskReceiveIncidentSpec(IncidentActivity.this).execute(channelName, channelOwner);

        //TODO remove this mockedSpec eventually! It is just for testing.
//        ArrayList<FieldWithoutContent> mockedSpec = new ArrayList<FieldWithoutContent>(3);
//        mockedSpec.add(new StringFieldWithoutContent("Title", true));
//        mockedSpec.add(new ImageFieldWithoutContent("Image", true));
//        mockedSpec.add(new StringFieldWithoutContent("Description", false));
//
//        incident = new AppIncidentWithWrapper(mockedSpec, channelName, channelOwner, IncidentActivity.this);
//        renderIncident();
    }

    /**
     * @precond the current incident, and all its fields, and the incidentDisplay, are not null.
     * @postcond each field's custom view is added to the linear layout, replacing all the old
     * views in the linear layout (if they existed).
     */
    private void renderIncident()
    {
        assertNotNull(incident);
        assertNotNull(incident.getFieldList());
        assertNotNull(incidentDisplay);

        incidentDisplay.removeAllViews();
        int i = 0;
        for (AppFieldWithContent field : incident.getFieldList())
        {
            //TODO change this from a test case to a regular case.
            assertNotNull(field);
            TextView tv = new TextView(IncidentActivity.this);
            tv.setText(field.getContentStringRepresentation());
//            View v = field.getContentViewRepresentation(RequestCode.FIELD_ACTION_REQUEST_CODE.ordinal());
//            assertNotNull(v);

            incidentDisplay.addView(tv);
            //All views are given a tag that is equal to their position in the linear layout.
            tv.setTag(i);
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

    /**
     * Created by wsv759 on 19/02/15.
     * Task to receive a new incident spec from the server, detailing what the fields are that need to be filled out.
     */
    private class TaskReceiveIncidentSpec extends AsyncTask<String, Void, SocketResult> {
        private final Context context;

        String channelName, channelOwner;
        public static final String LOG_TAG = "geosource comm";

        public TaskReceiveIncidentSpec(Context context)
        {
            this.context = context;
            assertNotNull(context);
        }

        protected SocketResult doInBackground(String... params) {
            ObjectOutputStream outStream; //wrapped stream to client

            ObjectInputStream inStream; //stream from client
            Socket outSocket;

            channelName = params[0];
            channelOwner = params[1];
            assertNotNull(channelName);
            assertNotNull(channelOwner);

            try //create socket
            {
                SocketWrapper socketWrapper = new SocketWrapper();
                outSocket = socketWrapper.getOutSocket();
                outStream = socketWrapper.getOut();
                inStream = socketWrapper.getIn();
            }
            catch(IOException e)
            {
                e.printStackTrace();

                return SocketResult.FAILED_CONNECTION;
            }


            ArrayList<FieldWithoutContent> fieldsToBeFilled;
            try
            {
                //TODO identify with the server whether I am asking for an incident spec or sending an incident.
                Log.i(LOG_TAG, "Attempting to send incident.");
                outStream.writeObject(IOCommand.GET_FORM);
                outStream.writeUTF(channelName);
                outStream.writeUTF(channelOwner);

                Log.i(LOG_TAG, "Retrieving reply...");
                fieldsToBeFilled = (ArrayList<FieldWithoutContent>) inStream.readObject();

                inStream.close();
                outStream.close();
                outSocket.close();
                Log.i(LOG_TAG, "Connection Closed");
            }
            catch (IOException e)
            {
                e.printStackTrace();

                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

                return SocketResult.CLASS_NOT_FOUND;

            }

            //TODO some of this code should maybe go on the ui thread.
            incident = new AppIncidentWithWrapper(fieldsToBeFilled, channelName, channelOwner, IncidentActivity.this);

            return SocketResult.SUCCESS;
        }

        protected void onPostExecute(SocketResult result) {
            makeToastAndLogOnSocketResult(
                    getString(R.string.downloaded_incident_spec_for_channel) + channelName + ".",
                    getString(R.string.failed_to_download_incident_spec_for_channel)  + channelName + ".",
                    result,
                    context,
                    LOG_TAG);

            if (result.equals(SocketResult.SUCCESS)) {
                renderIncident();
            }
        }
    }

    /**
     * Created by wsv759 on 19/02/15.
     * Task to send a new completed incident to the server.
     */
    private class TaskSendIncident extends AsyncTask<Incident, Void, SocketResult> {

        private final Context context;
        public static final String LOG_TAG = "geosource comm";

        public TaskSendIncident(Context context)
        {
            this.context = context;
            assertNotNull(context);
        }

        protected SocketResult doInBackground(Incident... params) {
            ObjectOutputStream outStream; //wrapped stream to client

            ObjectInputStream inStream; //stream from client
            Socket outSocket;

            Incident incidentToSend = params[0];
            assertNotNull(incidentToSend);

            try //create socket
            {
                SocketWrapper socketWrapper = new SocketWrapper();
                outSocket = socketWrapper.getOutSocket();
                outStream = socketWrapper.getOut();
                inStream = socketWrapper.getIn();
            }
            catch(IOException e)
            {
                e.printStackTrace();

                return SocketResult.FAILED_CONNECTION;
            }


            try
            {
                //TODO identify with the server whether I am asking for an incident spec or sending an incident.
                Log.i(LOG_TAG, "Attempting to send incident.");
                outStream.writeObject(IOCommand.SEND_INCIDENT);
                outStream.writeObject(incidentToSend);

                //TODO is a reply really not necessary?

                inStream.close();
                outStream.close();
                outSocket.close();
                Log.i(LOG_TAG, "Connection Closed");
            }
            catch (IOException e)
            {
                e.printStackTrace();

                return SocketResult.UNKNOWN_ERROR;
            }
            //TODO confirm this is not necessary (it is associated with receiving a reply, that currently isn't happening).
//            catch (ClassNotFoundException e) {
//                Log.e(LOG_TAG, "incoming class not found.");
//                e.printStackTrace();
//
//                return SocketResult.CLASS_NOT_FOUND;
//
//            }

            return SocketResult.SUCCESS;
        }

        protected void onPostExecute(SocketResult result) {
            makeToastAndLogOnSocketResult(getString(R.string.uploaded_incident),
                    getString(R.string.failed_to_upload_incident),
                    result,
                    context,
                    LOG_TAG);

            if (result.equals(SocketResult.SUCCESS))
                IncidentActivity.this.finish();
        }
    }

    /**
     * Helper function. Allows the two socket tasks to react similarly to various results.
     * @param onSuccess the string to toast and log on success.
     * @param onFailure the string to toast and log on failure.
     * @param result the socket result for this task.
     * @param context the context upon which to toast and log.
     * @param logTag the log tag to use when logging.
     */
    private void makeToastAndLogOnSocketResult(String onSuccess, String onFailure, SocketResult result, Context context, String logTag)
    {
        String message;
        switch (result) {
            case UNKNOWN_ERROR:
                message = onFailure + " " + getString(R.string.unknown_error);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Log.e(logTag, message);
                break;
            case FAILED_CONNECTION:
                message = onFailure + " " + getString(R.string.connection_failed);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Log.e(logTag, message);
                break;
            case CLASS_NOT_FOUND:
                Toast.makeText(context, onFailure + " " + getString(R.string.incomprehensible_server_response), Toast.LENGTH_LONG).show();
                Log.e(logTag, onFailure + "Server response object class not found.");
                break;
            case SUCCESS:
                Toast.makeText(context, onSuccess, Toast.LENGTH_LONG).show();
                Log.i(logTag,onSuccess);
            default:
                throw new RuntimeException("invalid result.");
        }
    }
}

