package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by wsv759 on 07/03/15.
 */
public class StringField extends AbstractAppFieldWithContent {
    public StringField(FieldWithContent fieldToWrap) {
        super(fieldToWrap);
    }

    @Override
    public String getContentStringRepresentation() {
        if (wrappedField.content == null)
            return "";
        else
            return (String) wrappedField.content;
    }

    @Override
    public String getPromptStringForUi() {
        return "Click to enter text.";
    }

    @Override
    public boolean contentIsFilled() {
        return wrappedField.content != null;
    }

    @Override
    public View getContentViewRepresentation(Context context) {
        //TODO implement this.
        return null;
    }

    @Override
    public void onSelected(Activity activity, int requestCodeForIntent) {

    }

    @Override
    public void onResultFromSelection(Activity activity, int resultCode, Intent data) {
        //TODO implement this.
    }
}
