package wsv759.cameratest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

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
            Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
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
            Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
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
                        data.getData(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
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
                    Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
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
    public long getFractionFreeSpace()
    {
        File dataDir = Environment.getDataDirectory();
        return dataDir.getFreeSpace() / dataDir.getTotalSpace();
    }

    /*
     * Return the total free space available for internal storage by this app, in bytes.
     */
    public long getTotalFreeSpaceBytes()
    {
        return Environment.getDataDirectory().getTotalSpace();
    }
}
