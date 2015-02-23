package hoopsnake.geosource;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import hoopsnake.geosource.data.FieldType;
import hoopsnake.geosource.data.FieldWithoutContent;
import hoopsnake.geosource.data.FieldWithContent;
import hoopsnake.geosource.data.Incident;
import hoopsnake.geosource.media.MediaManagement;

import static junit.framework.Assert.assertNotNull;


public class IncidentActivity extends ActionBarActivity {

    // The data to show
    ArrayList<FieldWithContent> fieldList = new ArrayList<FieldWithContent>();
    CustomAdapter aAdpt;

    /** The filepath to pass to the camera or video app, to which it will save a new media file. */
    private Uri fileUri;

    Incident incident;
    public static final String CHANNEL_NAME_PARAM_STRING = "channelName";
    String channelName;

    private enum RequestCode {
        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE,
        CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        assertNotNull(extras);

        channelName = extras.getString(CHANNEL_NAME_PARAM_STRING);
        assertNotNull(channelName);

        //Query the server for the spec!
        new TaskReceiveIncidentSpec(IncidentActivity.this).execute(channelName);

        // We get the ListView component from the layout
        ListView lv = (ListView) findViewById(R.id.listView);

        // This is a simple adapter that accepts as parameter
        // Context
        // Data list
        // The row layout that is used during the row creation
        // The keys used to retrieve the data
        // The View id used to show the data. The key number and the view id must match
        //aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fieledList);
        aAdpt = new CustomAdapter(fieldList, this);
        lv.setAdapter(aAdpt);

        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {

            }
        });

        // we register for the contextmenu
        registerForContextMenu(lv);
    }


    // We want to create a context Menu when the user long click on an item
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterViewCompat.AdapterContextMenuInfo aInfo = (AdapterViewCompat.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        FieldWithContent field = aAdpt.getItem(aInfo.position);

        switch(field.getType())
        {

            case IMAGE:
                // create Intent to take a picture and return control to the calling application
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (MediaManagement.isExternalStorageWritable()) {
                    fileUri = MediaManagement.getOutputMediaFileUri(IncidentActivity.this, MediaManagement.MediaType.IMAGE); // create a file to save the image
                    if (fileUri == null)
                    {
                        Toast.makeText(IncidentActivity.this, "New image file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                    // start the image capture Intent
                    startActivityForResult(imageIntent, RequestCode.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE.ordinal());
                }
                else
                {
                    //TODO Potentially save a file to internal storage, instead.
                }

                break;
            case STRING:
                throw new RuntimeException("Sorry, unimplemented.");
            case VIDEO:
                //create new Intent
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (MediaManagement.isExternalStorageWritable()) {
                    fileUri = MediaManagement.getOutputMediaFileUri(IncidentActivity.this, MediaManagement.MediaType.VIDEO);  // create a file to save the video
                    if (fileUri == null)
                    {
                        Toast.makeText(IncidentActivity.this, "New video file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
                    videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

                    // start the Video Capture Intent
                    startActivityForResult(videoIntent, RequestCode.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE.ordinal());
                }
                else
                {
                    //TODO Potentially save a file to internal storage, instead.
                }
                break;
            case SOUND:
                throw new RuntimeException("Sorry, unimplemented.");
        }

//        if (field.getTitle().equals("Picture"))
//        {
//            Intent intent = new Intent(IncidentActivity.this, CameraPage.class);
//            startActivity(intent);
//        }
//        else if (field.getTitle().equals("Audio"))
//        {
//            Intent intent = new Intent(IncidentActivity.this, AudioActivity.class);
//            startActivity(intent);
//        }
//        else if (field.getTitle().equals("Description"))
//        {
//            Intent intent = new Intent(IncidentActivity.this, DescriptionActivity.class);
//            startActivity(intent);
//        }
//
//        else
//        {
//            menu.setHeaderTitle("Options for " + field.getTitle());
//            menu.add(1, 1, 1, "Details");
//            menu.add(1, 2, 2, "Delete");
//
//        }
    }


    // This method is called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterViewCompat.AdapterContextMenuInfo aInfo = (AdapterViewCompat.AdapterContextMenuInfo) item.getMenuInfo();
        fieldList.remove(aInfo.position);
        aAdpt.notifyDataSetChanged();
        return true;
    }

    // Handle user click
    public void addField(View view) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.setTitle("Add a Field");
        d.setCancelable(true);

        final EditText edit = (EditText) d.findViewById(R.id.editTextPlanet);
        Button b = (Button) d.findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String fieldTitle = edit.getText().toString();

                //Sorry about the nested for loops, but my Android Studio was having issues with  recognizing the OR symbol, "||"
                if (!properField(fieldTitle))
                {
                    Toast.makeText(IncidentActivity.this,
                            "Sorry, that's not a valid field. Please try again.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    IncidentActivity.this.fieldList.add(new FieldWithContent(fieldTitle, FieldType.STRING, true, "placeholder"));
                    IncidentActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed
                    d.dismiss();
                }
            }
        });

        d.show();
    }

    boolean properField(String fieldTitle)
    {
        String picture = "Picture";
        String audio = "Audio";
        String description = "Description";
        String title = "Title";

        return (fieldTitle.equals(picture) || fieldTitle.equals(audio) || fieldTitle.equals(description) || fieldTitle.equals(title));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE.ordinal()) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                Toast.makeText(this, "Image saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();

                //TODO display the image in its field!

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(IncidentActivity.this, "Failed to capture image.", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == RequestCode.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE.ordinal()) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent

                Toast.makeText(this, "Video saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();

                //TODO display the video in its field!

//                new TaskSendImageOnSocket().execute(fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
                Toast.makeText(IncidentActivity.this, "Failed to capture video.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /** Try to submit the incident to the server!
     * //TODO this function is not connected to anything yet!
     * @param v the submit button.
     */
    private void onSubmitButtonClicked(View v)
    {
        if (incident.isCompletelyFilledIn())
            new TaskSendIncident(IncidentActivity.this).execute(incident);
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

            incident = new Incident(fieldsToBeFilled);

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
                    aAdpt.notifyDataSetChanged();
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
            String ipaddress = "10.227.145.56";
            int portNum = 80;

            ObjectOutputStream outStream; //wrapped stream to client

            ObjectInputStream inStream; //stream from client
            Socket outSocket;

            Incident incidentToSend = params[0];
            assertNotNull(incidentToSend);

            List<FieldWithContent> fieldsToSend = incidentToSend.getFieldWithContentList();
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

