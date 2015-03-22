package hoopsnake.geosource.data;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import ServerClientShared.AudioFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;


/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type Audio.
 */
public class AppAudioField extends AbstractAppFieldWithFile {

    public AppAudioField(AudioFieldWithContent fieldToWrap, IncidentActivity activity) {
        super(fieldToWrap, activity);
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
        //TODO implement this.
        ImageView iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_view, null);

        return null;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {
        //TODO implement this.
    }
}
