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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
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
public class AppImageField extends AbstractAppFieldWithFile{
    private ImageView iv = null;

    public AppImageField(ImageFieldWithContent fieldToWrap, int fieldPosInList, IncidentActivity activity) {
        super(fieldToWrap, fieldPosInList, activity);
    }

    @Override
    public boolean usesFilesOfType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    @Override
    View getFilledContentViewRepresentation() {
        Activity activity = getActivity();
        if (activity == null)
            return null;

        iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_button, null);
        iv.setClickable(false);
        new TaskDisplayImageFromBitmap(iv).execute(getContentFileUri().getPath());

        return iv;
    }

    @Override
    View getEmptyContentViewRepresentation(final int requestCodeForIntent) {
        final IncidentActivity activity = getActivity();
        if (activity == null)
            return null;

        iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_button, null);
        iv.setImageResource(R.drawable.camera);

        assertNotNull(iv);
        activity.makeViewLaunchable(iv, new Runnable() {
            @Override
            public void run() {
                Uri fileUriForNewImage = MediaManagement.getOutputImageFileUri();
                if (fileUriForNewImage == null) {
                    Toast.makeText(activity, "Cannot take picture; new image file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                    return;
                }

                setContentFileUri(fileUriForNewImage);

                MediaManagement.startCameraActivityForImage(activity, requestCodeForIntent, fileUriForNewImage);
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
            File imgFile = new File(contentFileUri.getPath());

            if(imgFile.exists()){
                String path = imgFile.getAbsolutePath();
                String msg = "Image saved to:\n" + path;
                Log.i(LOG_TAG, msg);

                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }

                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

                assertNotNull(iv);

                new TaskDisplayImageFromBitmap(iv).execute(path);
            }
            else
            {
                setContentFileUri(null);
                Log.e(LOG_TAG, "new jpg file was not created.");

                Activity activity = getActivity();
                if (activity == null)
                    return;
                Toast.makeText(activity, activity.getString(R.string.failed_to_capture_image) , Toast.LENGTH_LONG).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            setContentFileUri(null);
        } else {
            // Image capture failed, advise user
            Activity activity = getActivity();
            if (activity == null)
                return;

            Toast.makeText(activity, activity.getString(R.string.failed_to_capture_image), Toast.LENGTH_LONG).show();
        }
    }


    private class TaskDisplayImageFromBitmap extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        private static final int IMAGE_SCALED_HEIGHT = 384;
        private static final int IMAGE_MAX_SCALED_WIDTH = 512;
        /**
         *
         * @param imageView the view that will display the resulting decoded bitmap.
         */
        public TaskDisplayImageFromBitmap(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            assertNotNull(imageView);
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Decode image in background.
         * @param params a single String containing the img filepath to decode.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            String imgFilePath = params[0];
            assertNotNull(imgFilePath);
            Bitmap b = BitmapFactory.decodeFile(imgFilePath);
            Log.v(LOG_TAG, "decoded bitmap for " + imgFilePath);

            //Scaling.
            //TODO this could probably be replaced with the built-in thumbnail functionality, but I think it just works.

            int unscaledHeight = b.getHeight();
            int unscaledWidth = b.getWidth();

            Log.d(LOG_TAG, "unscaledHeight: " + unscaledHeight);
            Log.d(LOG_TAG, "unscaledWidth: " + unscaledWidth);

            //if the image is already smaller than the scaled dimensions, just return it.
            if (unscaledHeight <= IMAGE_SCALED_HEIGHT && unscaledWidth <= IMAGE_MAX_SCALED_WIDTH)
                return b;

            //Default to the given scaled height. If the scaled width is then too wide,
            //set the scaling to match the maximum scaled width instead (height will be less than originally desired.)
            int scaledHeight = IMAGE_SCALED_HEIGHT;
            double scalingFactor = scaledHeight / (double) unscaledHeight;
            int scaledWidth = (int) (0.5 + unscaledWidth * scalingFactor); //+0.5 rounds to nearest int rather than down.

            if (scaledWidth > IMAGE_MAX_SCALED_WIDTH)
            {
                scaledWidth = IMAGE_MAX_SCALED_WIDTH;
                scalingFactor = scaledWidth / (double) unscaledWidth;
                scaledHeight = (int) (0.5 + unscaledHeight * scalingFactor); //+0.5 rounds to nearest int rather than down.
            }

            Log.d(LOG_TAG, "scaledHeight: " + scaledHeight);
            Log.d(LOG_TAG, "scaledWidth: " + scaledWidth);
            return Bitmap.createScaledBitmap(b, scaledWidth, scaledHeight, true);
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
