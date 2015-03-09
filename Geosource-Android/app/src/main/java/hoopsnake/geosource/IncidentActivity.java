package hoopsnake.geosource;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import hoopsnake.geosource.comm.SocketResult;
import hoopsnake.geosource.comm.SocketWrapper;
import hoopsnake.geosource.data.AppFieldWithContent;
import hoopsnake.geosource.data.AppIncidentWithWrapper;
import hoopsnake.geosource.data.FieldType;
import hoopsnake.geosource.data.FieldWithContent;
import hoopsnake.geosource.data.FieldWithoutContent;
import hoopsnake.geosource.data.Incident;

import static junit.framework.Assert.assertNotNull;

/**
 * @author wsv759
 * The activity that controls the app when the user is filling out a new incident.
 * This is called from MainActivity when the "add new incident" button is pressed.
 * This activity also launches the tasks to receive a new incident spec and send a new incident.
 */
public class IncidentActivity extends ActionBarActivity {
    /** TODO ensure that only one button is ever clicked at a time. */
    private boolean clickable = true;

    /** This holds the incident, and passes it to the incidentDisplay for display. */
    IncidentDisplayAdapter incidentAdapter;

    /** The ListView that is actually visible to the user, displaying all the fields of the incident. */
    ListView incidentDisplay;

    /** The incident to be created and edited by the user on this screen. */
    AppIncidentWithWrapper incident;

    public static final String CHANNEL_NAME_PARAM_STRING = "channelName";

    /** The name of the channel to which to submit the new incident. */
    String channelName;

    /**
     * The position of the currently-selected field in the incidentDisplay.
     * This is recorded so that different activities/fragments can be called whenever a field is clicked,
     * and the corresponding field can be remembered upon their return.
     */
    int curFieldIdx;

    /**
     * The set of all request codes that are used by this activity when starting new activities or fragments.
     */
    private enum RequestCode {
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

        channelName = extras.getString(CHANNEL_NAME_PARAM_STRING);
        assertNotNull(channelName);

        //TODO Query the server for the spec!
        //new TaskReceiveIncidentSpec(IncidentActivity.this).execute(channelName);

        //TODO remove this mockedSpec eventually! It is just for testing.
        ArrayList<FieldWithoutContent> mockedSpec = new ArrayList<FieldWithoutContent>(3);
        mockedSpec.add(new FieldWithoutContent("Image", FieldType.IMAGE, true));
        mockedSpec.add(new FieldWithoutContent("Video", FieldType.VIDEO, false));
        mockedSpec.add(new FieldWithoutContent("Description",FieldType.STRING, true));

        incident = new AppIncidentWithWrapper(mockedSpec);
        // We get the ListView component from the layout
        incidentDisplay = (ListView) findViewById(R.id.listView);

        incidentAdapter = new IncidentDisplayAdapter(incident, IncidentActivity.this);
        incidentDisplay.setAdapter(incidentAdapter);

        incidentAdapter.notifyDataSetChanged();

        // React to user clicks on item
        incidentDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
                AppFieldWithContent field = incidentAdapter.getItem(position);

                curFieldIdx = position;

                field.onSelected(IncidentActivity.this, RequestCode.FIELD_ACTION_REQUEST_CODE.ordinal());
            }
        });
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
                AppFieldWithContent curField = incident.getFieldList().get(curFieldIdx);
                curField.onResultFromSelection(IncidentActivity.this, resultCode, data);
                break;
            default:
                throw new RuntimeException("invalid request code " + requestCode + ".");
        }
    }

    /**
     * Try to submit the incident to the server.
     * //TODO this function is not connected to anything yet!
     * @param v the submit button.
     * @precond incident is not null.
     * @postcond the new incident is submitted to the server. The new incident may not end up going through.
     */
    public void onSubmitButtonClicked(View v)
    {
        if (incident.isCompletelyFilledIn()) {
            //TODO actually call this task.
            //new TaskSendIncident(IncidentActivity.this).execute(incident);
        }
        else
            Toast.makeText(IncidentActivity.this, "incident has not been completely filled in!",Toast.LENGTH_LONG).show();
    }

    /**
     * Created by wsv759 on 19/02/15.
     * Task to receive a new incident spec from the server, detailing what the fields are that need to be filled out.
     */
    private class TaskReceiveIncidentSpec extends AsyncTask<String, Void, SocketResult> {
        private Context context;

        public static final String LOG_TAG = "geosource comm";

        public TaskReceiveIncidentSpec(Context context)
        {
            this.context = context;
            assertNotNull(context);
        }

        protected SocketResult doInBackground(String... params) {
            //TODO make this the actual ip address.
            String ipaddress = "10.227.145.56";
            int portNum = 80;

            ObjectOutputStream outStream; //wrapped stream to client

            ObjectInputStream inStream; //stream from client
            Socket outSocket;

            String channelName = params[0];
            assertNotNull(channelName);

            try //create socket
            {
                SocketWrapper socketWrapper = new SocketWrapper();
                outSocket = socketWrapper.getOutSocket();
                outStream = socketWrapper.getOut();
                inStream = socketWrapper.getIn();
            }
            catch(IOException e)
            {
                Log.e(LOG_TAG, "Shit got no connection, son!");
                e.printStackTrace();

                return SocketResult.FAILED_CONNECTION; //end program if connection failed
            }


            ArrayList<FieldWithoutContent> fieldsToBeFilled;
            try
            {
                //TODO identify with the server whether I am asking for an incident spec or sending an incident.
                Log.i(LOG_TAG, "Attempting to send incident.");
                outStream.writeObject(channelName);

                Log.i(LOG_TAG, "Retrieving reply...");
                //TODO this is a bit presumptuous on my part.
                fieldsToBeFilled = (ArrayList<FieldWithoutContent>) inStream.readObject();

                Log.i(LOG_TAG, "Connection Closing");
                inStream.close();
                outStream.close();
                outSocket.close();
                Log.i(LOG_TAG, "Connection Closed");
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG,"Unknown Error");
                e.printStackTrace();

                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                Log.e(LOG_TAG, "incoming class not found.");
                e.printStackTrace();

                return SocketResult.CLASS_NOT_FOUND;

            }

            incident = new AppIncidentWithWrapper(fieldsToBeFilled);

            incidentAdapter = new IncidentDisplayAdapter(incident, IncidentActivity.this);
            incidentDisplay.setAdapter(incidentAdapter);

            return SocketResult.SUCCESS;
        }

        protected void onPostExecute(SocketResult result) {
            switch (result) {
                case UNKNOWN_ERROR:
                    Toast.makeText(context, "Could not download incident spec for channel " + channelName + ". Unknown error.", Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Could not download incident spec for channel " + channelName + ". Unknown error.");
                    break;
                case FAILED_CONNECTION:
                    Toast.makeText(context, "Could not download incident spec for channel " + channelName + ". Connection failed.", Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Could not download incident spec for channel " + channelName + ". Connection failed.");
                    break;
                case CLASS_NOT_FOUND:
                    Toast.makeText(context, "Could not download incident spec for channel " + channelName + ". Server response object class not found.", Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Could not download incident spec for channel " + channelName + ". Server response object class not found.");
                    break;
                case SUCCESS:
                    Log.i(LOG_TAG, "incident spec for channel " + channelName + "downloaded successfully.");

                    //TODO notify the ui of the new incident. We probably need more than this.
                    incidentAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Created by wsv759 on 19/02/15.
     * Task to send a new completed incident to the server.
     */
    private class TaskSendIncident extends AsyncTask<Incident, Void, SocketResult> {

        private Context context;
        public static final String LOG_TAG = "geosource comm";

        public TaskSendIncident(Context context)
        {
            this.context = context;
            assertNotNull(context);
        }

        protected SocketResult doInBackground(Incident... params) {
            //TODO make this the actual ip address.
            String ipaddress = "10.227.145.56";
            int portNum = 80;

            ObjectOutputStream outStream; //wrapped stream to client

            ObjectInputStream inStream; //stream from client
            Socket outSocket;

            Incident incidentToSend = params[0];
            assertNotNull(incidentToSend);

            List<FieldWithContent> fieldsToSend = incidentToSend.getFieldList();
            assertNotNull(fieldsToSend);

            try //create socket
            {
                SocketWrapper socketWrapper = new SocketWrapper();
                outSocket = socketWrapper.getOutSocket();
                outStream = socketWrapper.getOut();
                inStream = socketWrapper.getIn();
            }
            catch(IOException e)
            {
                Log.e(LOG_TAG,"Shit got no connection, son!");
                e.printStackTrace();

                return SocketResult.FAILED_CONNECTION; //end program if connection failed
            }


            try
            {
                //TODO identify with the server whether I am asking for an incident spec or sending an incident.
                Log.i(LOG_TAG, "Attempting to send incident.");
                outStream.writeObject(fieldsToSend);

                Log.i(LOG_TAG, "Retrieving reply...");
                //TODO this is a bit presumptuous on my part.
                String reply = (String) inStream.readObject();

                Log.i(LOG_TAG, "Connection Closing");
                inStream.close();
                outStream.close();
                outSocket.close();
                Log.i(LOG_TAG, "Connection Closed");
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG,"Unknown Error");
                e.printStackTrace();

                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                Log.e(LOG_TAG, "incoming class not found.");
                e.printStackTrace();

                return SocketResult.CLASS_NOT_FOUND;

            }

            return SocketResult.SUCCESS;
        }

        protected void onPostExecute(SocketResult result) {
            switch (result) {
                case UNKNOWN_ERROR:
                    Toast.makeText(context, "Incident upload failed. Unknown error.", Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Incident upload failed. Unknown error.");
                    break;
                case FAILED_CONNECTION:
                    Toast.makeText(context, "Incident upload failed. Connection failed.", Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Incident upload failed. Connection failed.");
                    break;
                case CLASS_NOT_FOUND:
                    Toast.makeText(context, "Incident upload failed. Server response object class not found.", Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Incident upload failed. Server response object class not found.");
                    break;
                case SUCCESS:
                    Toast.makeText(context, "New incident uploaded successfully.", Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG,"New incident uploaded successfully.");

                    //Return the main page. The incident has been submitted, and our work is done.
                    IncidentActivity.this.finish();
            }
        }
    }
}

