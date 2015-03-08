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

    /** The position of the currently-selected field in the incidentDisplay.
     * This is recorded for when the Camera or Video activity returns. */
    int curFieldIdx;

    private enum RequestCode {
        FIELD_ACTION_REQUEST_CODE
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RequestCode requestCodeVal = RequestCode.values()[requestCode];
        switch(requestCodeVal)
        {
            case FIELD_ACTION_REQUEST_CODE:
                AppFieldWithContent curField = incident.getFieldList().get(curFieldIdx);
                curField.onResultFromSelection(IncidentActivity.this, resultCode, data);
                break;
            default:
                throw new RuntimeException("invalid request code " + requestCode + ".");
        }
    }

    /** Try to submit the incident to the server!
     * //TODO this function is not connected to anything yet!
     * @param v the submit button.
     */
    public void onSubmitButtonClicked(View v)
    {
        if (incident.isCompletelyFilledIn()) {
            //TODO actually call this task.
            //new TaskSendIncident(IncidentActivity.this).execute(incident);

            this.finish();
        }
        else
            Toast.makeText(IncidentActivity.this, "incident has not been completely filled in!",Toast.LENGTH_LONG).show();
    }

    /**
     * Created by wsv759 on 19/02/15.
     */
    private class TaskReceiveIncidentSpec extends AsyncTask<String, Void, SocketResult> {
        private Context context;

        String logTag = MainActivity.APP_LOG_TAG;

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
                Log.e(logTag, "Shit got no connection, son!");
                e.printStackTrace();

                return SocketResult.FAILED_CONNECTION; //end program if connection failed
            }


            ArrayList<FieldWithoutContent> fieldsToBeFilled;
            try
            {
                //TODO identify with the server whether I am asking for an incident spec or sending an incident.
                Log.i(logTag, "Attempting to send incident.");
                outStream.writeObject(channelName);

                Log.i(logTag, "Retrieving reply...");
                //TODO this is a bit presumptuous on my part.
                fieldsToBeFilled = (ArrayList<FieldWithoutContent>) inStream.readObject();

                Log.i(logTag, "Connection Closing");
                inStream.close();
                outStream.close();
                outSocket.close();
                Log.i(logTag, "Connection Closed");
            }
            catch (IOException e)
            {
                Log.e(logTag,"Unknown Error");
                e.printStackTrace();

                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                Log.e(logTag, "incoming class not found.");
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
                    Log.e(logTag, "Could not download incident spec for channel " + channelName + ". Unknown error.");
                    break;
                case FAILED_CONNECTION:
                    Toast.makeText(context, "Could not download incident spec for channel " + channelName + ". Connection failed.", Toast.LENGTH_LONG).show();
                    Log.e(logTag, "Could not download incident spec for channel " + channelName + ". Connection failed.");
                    break;
                case CLASS_NOT_FOUND:
                    Toast.makeText(context, "Could not download incident spec for channel " + channelName + ". Server response object class not found.", Toast.LENGTH_LONG).show();
                    Log.e(logTag, "Could not download incident spec for channel " + channelName + ". Server response object class not found.");
                    break;
                case SUCCESS:
                    Log.i(logTag, "incident spec for channel " + channelName + "downloaded successfully.");

                    //TODO notify the ui of the new incident. We probably need more than this.
                    incidentAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Created by wsv759 on 19/02/15.
     */
    private class TaskSendIncident extends AsyncTask<Incident, Void, SocketResult> {

        private Context context;
        String logTag = MainActivity.APP_LOG_TAG;

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
                Log.e(logTag,"Shit got no connection, son!");
                e.printStackTrace();

                return SocketResult.FAILED_CONNECTION; //end program if connection failed
            }


            try
            {
                //TODO identify with the server whether I am asking for an incident spec or sending an incident.
                Log.i(logTag, "Attempting to send incident.");
                outStream.writeObject(fieldsToSend);

                Log.i(logTag, "Retrieving reply...");
                //TODO this is a bit presumptuous on my part.
                String reply = (String) inStream.readObject();

                Log.i(logTag, "Connection Closing");
                inStream.close();
                outStream.close();
                outSocket.close();
                Log.i(logTag, "Connection Closed");
            }
            catch (IOException e)
            {
                Log.e(logTag,"Unknown Error");
                e.printStackTrace();

                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                Log.e(logTag, "incoming class not found.");
                e.printStackTrace();

                return SocketResult.CLASS_NOT_FOUND;

            }

            return SocketResult.SUCCESS;
        }

        protected void onPostExecute(SocketResult result) {
            switch (result) {
                case UNKNOWN_ERROR:
                    Toast.makeText(context, "Incident upload failed. Unknown error.", Toast.LENGTH_LONG).show();
                    Log.e(logTag, "Incident upload failed. Unknown error.");
                    break;
                case FAILED_CONNECTION:
                    Toast.makeText(context, "Incident upload failed. Connection failed.", Toast.LENGTH_LONG).show();
                    Log.e(logTag, "Incident upload failed. Connection failed.");
                    break;
                case CLASS_NOT_FOUND:
                    Toast.makeText(context, "Incident upload failed. Server response object class not found.", Toast.LENGTH_LONG).show();
                    Log.e(logTag, "Incident upload failed. Server response object class not found.");
                    break;
                case SUCCESS:
                    Toast.makeText(context, "New incident uploaded successfully.", Toast.LENGTH_LONG).show();
                    Log.i(logTag,"New incident uploaded successfully.");
            }
        }
    }
}

