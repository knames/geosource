package hoopsnake.geosource.media;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hoopsnake.geosource.R;

/**
 * Created by wsv759 on 16/02/15.
 */
public class MediaManagement {

    public enum MediaType {
        IMAGE,
        VIDEO
    }

    /* Checks if external storage is available for read and write. */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /** Create a file Uri for saving an image or video. */
    public static Uri getOutputMediaFileUri(Context context, MediaType type){
        return Uri.fromFile(getOutputMediaFile(context, type));
    }

    /** Create a File for saving an image or video. This used to be static. */
    private static File getOutputMediaFile(Context context, MediaType type){

        // Check that the SDCard is mounted. If it is, initialize an external file.
        if (isExternalStorageWritable()) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "camera_test_app");

            Log.d(context.getString(R.string.app_name), mediaStorageDir.getAbsolutePath());
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(context.getString(R.string.app_name), "failed to create directory.");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            switch (type)
            {
                case IMAGE:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "IMG_" + timeStamp + ".jpg");
                    break;
                case VIDEO:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4");
                    break;
                default:
                    throw new InvalidParameterException("media file type is invalid.");
            }

            return mediaFile;
        }
        else
        {
            return null;
        }
    }
}
