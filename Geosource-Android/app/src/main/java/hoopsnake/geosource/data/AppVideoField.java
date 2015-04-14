package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

import ServerClientShared.VideoFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.media.MediaManagement;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type Video.
 */
public class AppVideoField extends AbstractAppFieldWithFile{

    public AppVideoField(VideoFieldWithContent fieldToWrap, int fieldPosInList, IncidentActivity activity) {
        super(fieldToWrap, fieldPosInList, activity);
    }

    @Override
    public boolean usesFilesOfType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    @Override
    View getFilledContentViewRepresentation() {
        //TODO implement this.
        return null;
    }

    @Override
    View getEmptyContentViewRepresentation(final int requestCodeForIntent) {
        final IncidentActivity activity = getActivity();
        if (activity == null)
            return null;

        ImageView iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_button, null);
        iv.setImageResource(R.drawable.videocamera);

        assertNotNull(iv);
        activity.makeViewLaunchable(iv, new Runnable() {
            @Override
            public void run() {
                Uri fileUriForNewVideo = MediaManagement.getOutputVideoFileUri();
                if (fileUriForNewVideo == null) {
                    Toast.makeText(activity, "Cannot take video; new video file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                    return;
                }

                setContentFileUri(fileUriForNewVideo);

                MediaManagement.startCameraActivityForVideo(activity, requestCodeForIntent, fileUriForNewVideo);
            }
        });

        return iv;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Video captured and saved to fileUri specified in the Intent
            Uri contentFileUri = getContentFileUri();
            assertNotNull(contentFileUri);
            File vidFile = new File(contentFileUri.getPath());

            if(vidFile.exists()){
                String path = vidFile.getAbsolutePath();
                String msg = "Video saved to :\n" + path;
                Log.i(LOG_TAG, msg);

                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }

                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            } else {
                setContentFileUri(null);
                Log.e(LOG_TAG, "new mp4 file was not created.");

                Activity activity = getActivity();
                if (activity == null)
                    return;
                Toast.makeText(activity, activity.getString(R.string.failed_to_capture_video), Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            setContentFileUri(null);
        } else {
            // Video capture failed, advise user
            Activity activity = getActivity();
            if (activity == null)
                return;

            Toast.makeText(activity, activity.getString(R.string.failed_to_capture_video), Toast.LENGTH_LONG).show();
        }
    }

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    private void writeObject(ObjectOutputStream out) throws IOException
    {

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {

    }

    private void readObjectNoData() throws ObjectStreamException
    {

    }
}
