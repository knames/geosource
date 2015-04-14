package hoopsnake.geosource.data;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

import hoopsnake.geosource.BackgroundRunnable;
import hoopsnake.geosource.R;
import hoopsnake.geosource.comm.RunnableSendIncident;

import static junit.framework.Assert.assertNotNull;

/**
 * The following @precond and @postcond apply to calling .run() on this class.
 * @precond before executing this task, construct it with an AbstractAppFieldWithFile.
 * That field must have a non-null file URI referring to an existent file.
 * @postcond the given field's content file is converted into a byte array, and its content is set to that byte array.
 */
public class RunnableSetContentBasedOnFileUri extends BackgroundRunnable<Boolean> {
    private RunnableSendIncident callingRunnable;
    private AbstractAppFieldWithFile fieldToSet;
    private static final String LOG_TAG = "geosource";
    private static final int BUFFER_SIZE = 8192;

    /**
     *
     * @param callingRunnable The task that is counting down based upon this.
     */
    public RunnableSetContentBasedOnFileUri(RunnableSendIncident callingRunnable, AbstractAppFieldWithFile fieldToSet)
    {
        super(new WeakReference<Activity>(fieldToSet.getActivity()));

        this.callingRunnable = callingRunnable;
        this.fieldToSet = fieldToSet;

        assertNotNull(fieldToSet);
        assertNotNull(callingRunnable);
    }

    protected Boolean doInBackground() {
        Uri contentFileUri = fieldToSet.getContentFileUri();
        assertNotNull(contentFileUri);
        File imageFile = new File(contentFileUri.getPath());

        FileInputStream fis;
        try {
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "uri " + contentFileUri + " not found.");
            return false;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[BUFFER_SIZE];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            }
        } catch (IOException ex) {
            Log.e(LOG_TAG, "failed to write file " + contentFileUri + " to byte array.");
            return false;
        }

        Log.i(LOG_TAG, "converted file " + contentFileUri + " to byte array.");

        byte[] fileInByteFormat = bos.toByteArray();
        assertNotNull(fileInByteFormat);
        fieldToSet.setContent(fileInByteFormat);

        //Alert the calling task that it is one step closer to being able to send this incident.
        CountDownLatch cdl = callingRunnable.getContentSerializationCountDownLatch();
        assertNotNull(cdl);
        cdl.countDown();

        return true;
    }

    protected void onPostExecute(Boolean setContentSucceeded, Activity activity)
    {
        if (!setContentSucceeded && fieldToSet != null)
            Toast.makeText(activity, activity.getString(R.string.failed_to_format_content), Toast.LENGTH_LONG).show();
    }
}
