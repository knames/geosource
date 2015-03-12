package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import ServerClientShared.FieldWithContent;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type String. This can be used for all basic text fields.
 */
public class StringField extends AbstractAppFieldWithContent {
    //TODO hardcoded until we get params implemented.
    public int maxLength = 140;

    public StringField(FieldWithContent fieldToWrap) {
        super(fieldToWrap);
    }

    @Override
    public String getContentStringRepresentation() {
        if (wrappedField.getContent() == null)
            return "";
        else
            return (String) wrappedField.getContent();
    }

    @Override
    public String getPromptStringForUi() {
        return "Click to enter text.";
    }

    @Override
    public boolean contentIsFilled() {
        return wrappedField.getContent() != null;
    }

    @Override
    public View getContentViewRepresentation(final IncidentActivity activity, final int requestCodeForIntent) {
        EditText contentEditor = (EditText) activity.findViewById(R.id.field_edit_text);
        String content = (String) wrappedField.getContent();
        //TODO check the length of the text.
        if (content != null)
            contentEditor.setText(content);
        contentEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (contentIsSuitable(text))
                    setContent(text);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return contentEditor;
    }

    @Override
    public void onResultFromSelection(Activity activity, int resultCode, Intent data) {
        //TODO implement this.
    }
}
