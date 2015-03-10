package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type Audio.
 */
public class AudioField extends AbstractAppFieldWithContentAndFile{

    public AudioField(FieldWithContent fieldToWrap) {
        super(fieldToWrap);
        assertEquals(fieldToWrap.getType(), FieldType.AUDIO);
    }

    @Override
    public boolean usesFilesOfType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    @Override
    public String getPromptStringForUi() {
        return "Click to record audio.";
    }

    @Override
    public View getContentViewRepresentation(Context context) {
        //TODO implement this.
        return null;
    }

    @Override
    public void onSelected(Activity activity, int requestCodeForIntent) {
       //TODO implement this
    }

    @Override
    public void onResultFromSelection(Activity activity, int resultCode, Intent data) {
        //TODO implement this.
    }
}