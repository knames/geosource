package hoopsnake.geosource;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.concurrent.Semaphore;

import hoopsnake.geosource.media.MediaManagement;


public class MainActivity extends Activity {

    /** TODO ensure that only one button is ever clicked at a time. */
    private boolean clickable = true;

    Semaphore mutexClickable;
    /** The filepath to pass to the camera or video app, to which it will save a new media file. */
    private Uri fileUri;

    private enum RequestCode {
        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE,
        CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(getString(R.string.website_url));
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

    public void onImageButtonClicked(View v)
    {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (MediaManagement.isExternalStorageWritable()) {
            fileUri = MediaManagement.getOutputMediaFileUri(MainActivity.this, MediaManagement.MediaType.IMAGE); // create a file to save the image
            if (fileUri == null)
            {
                Toast.makeText(MainActivity.this, "New image file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                return;
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, RequestCode.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE.ordinal());
        }
        else
        {
            //TODO Potentially save a file to internal storage, instead.
        }
    }

    public void onVideoButtonClicked(View v)
    {
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (MediaManagement.isExternalStorageWritable()) {
            fileUri = MediaManagement.getOutputMediaFileUri(MainActivity.this, MediaManagement.MediaType.VIDEO);  // create a file to save the video
            if (fileUri == null)
            {
                Toast.makeText(MainActivity.this, "New video file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                return;
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

            // start the Video Capture Intent
            startActivityForResult(intent, RequestCode.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE.ordinal());
        }
        else
        {
            //TODO Potentially save a file to internal storage, instead.
        }
    }

    public void onAudioButtonClicked(View v)
    {
        Toast.makeText(MainActivity.this, "audio recording not yet implemented. ", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE.ordinal()) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                Toast.makeText(this, "Image saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();

                //TODO fill in the requisite incident fields by sending an intent.
                
                //TODO Send image to the geosource server.
//                new TaskSendImageOnSocket().execute(fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(MainActivity.this, "Failed to capture image.", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == RequestCode.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE.ordinal()) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent

                Toast.makeText(this, "Video saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();

                //TODO fill in the requisite incident fields by sending an intent.

                //TODO Send video to geosource server.
//                new TaskSendImageOnSocket().execute(fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
                Toast.makeText(MainActivity.this, "Failed to capture video.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
