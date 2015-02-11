package wsv759.cameratest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;


public class MainActivity extends ActionBarActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPictureButtonClicked(View v)
    {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (isExternalStorageWritable()) {
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
            if (fileUri == null)
                return;

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        }
        else
        {
            //TODO Potentially save a file to internal storage, instead.
        }
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void onVideoButtonClicked(View v)
    {
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (isExternalStorageWritable()) {
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
            if (fileUri == null)
                return;

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

            // start the Video Capture Intent
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
        else
        {
            //TODO Potentially save a file to internal storage, instead.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                Toast.makeText(this, "Image saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();

                //Send image to the geosource server.
                new TaskSendImageOnSocket().execute(fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(MainActivity.this, "Failed to capture image.", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Uri fileUri = data.getData();
                Toast.makeText(this, "Video saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();

                new TaskSendImageOnSocket().execute(fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
                Toast.makeText(MainActivity.this, "Failed to capture video.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /** Create a file Uri for saving an image or video. This used to be static. */
    private Uri getOutputMediaFileUri(int type){
        File outputMediaFile = getOutputMediaFile(type);
        if (outputMediaFile == null)
        {
            Toast.makeText(MainActivity.this, "New file could not be created on external storage device.", Toast.LENGTH_LONG).show();
            return null;
        }

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video. This used to be static. */
    private File getOutputMediaFile(int type){

        // Check that the SDCard is mounted. If it is, initialize an external file.
        if (this.isExternalStorageWritable()) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "camera_test_app");

            Log.d(getString(R.string.app_name), mediaStorageDir.getAbsolutePath());
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(getString(R.string.app_name), "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }
        else
        {
            return null;
        }
    }

    enum SocketResult {SUCCESS, FAILED_CONNECTION, CLASS_NOT_FOUND, UNKNOWN_ERROR};

    private class TaskSendImageOnSocket extends AsyncTask<Uri, Void, SocketResult>
    {
        protected SocketResult doInBackground(Uri... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            String ipaddress = "10.227.145.56";
            int portNum = 80;

            OutputStream out; //wrapped stream to client

            ObjectInputStream in; //stream from client
            Socket outSocket;

            try //create socket
            {
                outSocket = new Socket(InetAddress.getByName(ipaddress), portNum);
                if (outSocket.isConnected())
                    Log.i(getString(R.string.app_name),"Connection Established");
                out = new ObjectOutputStream(outSocket.getOutputStream());
                in = new ObjectInputStream(outSocket.getInputStream());
                Log.i(getString(R.string.app_name), "Stream Created");
            }
            catch(IOException e)
            {
                Log.e(getString(R.string.app_name),"Shit got no connection, son!");
                e.printStackTrace();

                return SocketResult.FAILED_CONNECTION; //end program if connection failed
            }

            File imageFile = new File(params[0].getPath());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imageFile);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageByteArray = stream.toByteArray();

            LinkedList receiveList;
            try
            {
                Log.i(getString(R.string.app_name), "Attempting receive");
                receiveList = (LinkedList) in.readObject();

                String logMessage = "";
                ListIterator<Integer> iter = receiveList.listIterator();
                while (iter.hasNext())
                {
                    logMessage += iter.next().toString() + ", ";
                }
                Log.d(getString(R.string.app_name),logMessage);

                Log.i(getString(R.string.app_name), "Attempting to send image " + params[0].getPath());
                byte[] size = ByteBuffer.allocate(4).putInt(stream.size()).array();
                out.write(size);
                out.write(imageByteArray);

                Log.i(getString(R.string.app_name), "Connection Closing");
                in.close();
                out.close();
                outSocket.close();
                Log.i(getString(R.string.app_name), "Connection Closed");
            }
            catch (IOException e)
            {
                Log.e(getString(R.string.app_name),"Unknown Error");
                e.printStackTrace();

                return SocketResult.UNKNOWN_ERROR;
            } catch (ClassNotFoundException e) {
                Log.e(getString(R.string.app_name),"incoming class not found.");
                e.printStackTrace();

                return SocketResult.CLASS_NOT_FOUND;
            }

            return SocketResult.SUCCESS;
        }

        protected void onPostExecute(SocketResult result) {
            switch (result) {
                case UNKNOWN_ERROR:
                    Toast.makeText(MainActivity.this, "File upload failed. Unknown error.", Toast.LENGTH_SHORT).show();
                    break;
                case FAILED_CONNECTION:
                    Toast.makeText(MainActivity.this, "File upload failed. Connection failed.", Toast.LENGTH_SHORT).show();
                    break;
                case CLASS_NOT_FOUND:
                    Toast.makeText(MainActivity.this, "File upload failed. Server response object class not found.", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    Toast.makeText(MainActivity.this, "New file uploaded.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /* Checks if external storage is available for read and write. */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /*
     * We may want the below code to warn the user about internal storage usage, if we are forced to
     * use it.
     */

    /*
     * Return the fraction of free space available for internal storage by this app.
     */
    public double getFractionFreeSpace()
    {
        File dataDir = Environment.getDataDirectory();
        return dataDir.getFreeSpace() / (double) dataDir.getTotalSpace();
    }

    /*
     * Return the total free space available for internal storage by this app, in bytes.
     */
    public long getTotalFreeSpaceBytes()
    {
        return Environment.getDataDirectory().getTotalSpace();
    }
}
