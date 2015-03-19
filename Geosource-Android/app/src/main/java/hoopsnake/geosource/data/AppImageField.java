package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import ServerClientShared.ImageFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.media.MediaManagement;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type Image.
 */
public class AppImageField extends AbstractAppFieldWithContentAndFile {
    private ImageView iv = null;

    public AppImageField(ImageFieldWithContent fieldToWrap, IncidentActivity activity) {
        super(fieldToWrap, activity);
    }

    @Override
    public boolean usesFilesOfType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    @Override
    public ImageView getContentViewRepresentation(final int requestCodeForIntent) {
        iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_view, null);
//        iv.setImageDrawable(super.getDrawableIconFromSVGResource(R.raw.camera_alt));
        iv.setImageResource(R.drawable.arrow_right); //TODO arrow_right is just a placeholder.
        iv.setClickable(true);

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

                        setContentFileUri(fileUriForNewImage);

                        MediaManagement.startCameraActivityForImage(activity, requestCodeForIntent, fileUriForNewImage);
                    }
                });
            }
        });

        return iv;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Image captured and saved to fileUri specified in the Intent
            Uri contentFileUri = getContentFileUri();
            assertNotNull(contentFileUri);
            File imgFile = new  File(contentFileUri.getPath());

            if(imgFile.exists()){
                String path = imgFile.getAbsolutePath();
                String msg = "Image saved to:\n" + path;
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                Log.i(LOG_TAG, msg);

                assertNotNull(iv);

                new TaskDisplayImageFromBitmap(iv).execute(path);
                //TODO call this in the sendIncident task instead.
//                new TaskSetContentBasedOnFileUri(this).execute();
            }
            else
            {
                Log.e(LOG_TAG, "new file was not created.");
                Toast.makeText(activity, activity.getString(R.string.failed_to_capture_image) , Toast.LENGTH_LONG).show();
            }


        } else if (resultCode == Activity.RESULT_CANCELED) {
            setContentFileUri(null);
        } else {
            // Image capture failed, advise user
            Toast.makeText(activity, activity.getString(R.string.failed_to_capture_image), Toast.LENGTH_LONG).show();
        }
    }

    private class TaskDisplayImageFromBitmap extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public TaskDisplayImageFromBitmap(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            assertNotNull(imageView);
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            String imgFilePath = params[0];
            assertNotNull(imgFilePath);
            Bitmap b = BitmapFactory.decodeFile(imgFilePath);
            Log.d(LOG_TAG, "decoded bitmap for " + imgFilePath);
            return b;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(LOG_TAG, "onPostExecute");
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    Log.d(LOG_TAG, "new image view should be set.");
                }
            }
        }
    }
}
