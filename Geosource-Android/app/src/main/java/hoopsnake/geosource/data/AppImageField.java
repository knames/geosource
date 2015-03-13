package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ServerClientShared.ImageFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.media.MediaManagement;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type Image.
 */
public class AppImageField extends AbstractAppFieldWithContentAndFile {
    public AppImageField(ImageFieldWithContent fieldToWrap) {
        super(fieldToWrap);
    }

    @Override
    public boolean usesFilesOfType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    @Override
    public String getPromptStringForUi() {
        return "Click to take a picture";
    }

    @Override
    public View getContentViewRepresentation(final IncidentActivity activity, final int requestCodeForIntent) {
        ImageView iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_view, null);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activity.doIfClickable(new Runnable() {
                    @Override
                    public void run() {
                        //Make sure the activity knows which view was clicked.
                        activity.setCurFieldIdx((int) v.getTag());

                        Uri fileUriForNewImage = MediaManagement.getOutputImageFileUri();
                        if (fileUriForNewImage == null)
                        {
                            Toast.makeText(activity, "Cannot take picture; new image file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        MediaManagement.startCameraActivityForImage(activity, requestCodeForIntent, fileUriForNewImage);
                    }
                });
            }
        });

        return iv;
    }

    @Override
    public void onResultFromSelection(Activity activity, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Image captured and saved to fileUri specified in the Intent

            Toast.makeText(activity, "Image saved to:\n" + getContentFileUri(), Toast.LENGTH_LONG).show();

            //TODO display the image in its field! This means notifying the UI.
            //TODO set the content of this field appropriately. Probably in a background task?
            //setContent(new SerialBitmap(fileUri));


        } else if (resultCode == Activity.RESULT_CANCELED) {
            // User cancelled the image capture
        } else {
            // Image capture failed, advise user
            Toast.makeText(activity, "Failed to capture image.", Toast.LENGTH_LONG).show();
        }
    }
}
