package hoopsnake.geosource.data;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import hoopsnake.geosource.R;

import static junit.framework.Assert.assertNotNull;

/**
 * The following @precond and @postcond apply to calling .execute() on this class.
 * @precond before executing this task, construct it with an AbstractAppFieldWithContentAndFile.
 * That field must have a non-null file URI referring to an existent file.
 * @postcond the given field's content file is converted into a byte array, and its content is set to that byte array.
 */
public class TaskSetContentBasedOnFileUri extends AsyncTask<Void, Void, Boolean>
{
    private AbstractAppFieldWithContentAndFile fieldToSet;

    private static final String LOG_TAG = "geosource data";
    /**
     *
     * @param fieldToSet the field whose content needs to be set based upon its fileUri.
     */
    public TaskSetContentBasedOnFileUri(AbstractAppFieldWithContentAndFile fieldToSet)
    {
        this.fieldToSet = fieldToSet;
        assertNotNull(fieldToSet);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Uri contentFileUri = fieldToSet.getContentFileUri();
        assertNotNull(contentFileUri);
        File imageFile = new File(contentFileUri.getPath());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "uri " + params[0] + " not found.");
            return false;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            }
        } catch (IOException ex) {
            Log.e(LOG_TAG, "failed to write file " + params[0] + " to byte array.");
            return false;
        }

        Log.i(LOG_TAG, "converted file " + params[0] + " to byte array.");

        byte[] fileInByteFormat = bos.toByteArray();
        assertNotNull(fileInByteFormat);
        fieldToSet.setContent(fileInByteFormat);
        //Alert the IncidentActivity that it is one step closer to being able to send this incident.
        fieldToSet.getActivity().getContentCountDownLatch().countDown();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean setContentSucceeded)
    {
        if (!setContentSucceeded && fieldToSet != null) {
            Activity activity = fieldToSet.getActivity();
            Toast.makeText(activity, activity.getString(R.string.failed_to_format_content), Toast.LENGTH_LONG).show();
        }
    }
}