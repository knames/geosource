package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

import ServerClientShared.VideoFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.media.MediaManagement;

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
        ImageView iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_button, null);
        iv.setImageResource(R.drawable.videocamera);

        activity.makeViewLaunchable(iv, new Runnable() {
            @Override
            public void run() {
                Uri fileUriForNewVideo = MediaManagement.getOutputVideoFileUri();
                if (fileUriForNewVideo == null) {
                    Toast.makeText(activity, "Cannot take video; new image file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                    return;
                }

                MediaManagement.startCameraActivityForVideo(activity, requestCodeForIntent, fileUriForNewVideo);
            }
        });

        return iv;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Video captured and saved to fileUri specified in the Intent
            Toast.makeText(activity, "Video saved to:\n" + getContentFileUri(), Toast.LENGTH_LONG).show();

            //TODO display the video in its field! This means notifying the UI.
            //TODO set the content of this field appropriately. Probably in a background task?


        } else if (resultCode == Activity.RESULT_CANCELED) {
            // User cancelled the video capture
        } else {
            // Video capture failed, advise user
            Toast.makeText(activity, "Failed to capture video.", Toast.LENGTH_LONG).show();
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
