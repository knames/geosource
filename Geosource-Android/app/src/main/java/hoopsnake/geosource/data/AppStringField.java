package hoopsnake.geosource.data;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

import ServerClientShared.StringFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type String. This can be used for all basic text fields.
 */
public class AppStringField extends AbstractAppField{
    public AppStringField(StringFieldWithContent fieldToWrap, int fieldPosInList, IncidentActivity activity) {
        super(fieldToWrap, fieldPosInList, activity);
    }

    @Override
    public String getContentStringRepresentation() {
        //TODO remove the null case here. This is just for testing.
        if (wrappedField.getContent() == null)
            return "empty string here.";

        return (String) wrappedField.getContent();
    }

    /**
     *
     * @return a prompt string to be used by the UI when this class has no content. Guaranteed not null.
     */
    private String getPromptStringForUi() {
        return activity.getString(R.string.prompt_enter_text);
    }

    @Override
    public View getContentViewRepresentation(final int requestCodeForIntent) {
        //TODO both these lines seem to work for the same purpose. Which is better?
        EditText contentEditor = (EditText) activity.getLayoutInflater().inflate(R.layout.field_edit_text, null);

        String content = (String) wrappedField.getContent();
        //TODO check the length of the text.
        if (content != null)
            contentEditor.setText(content);
        else
            contentEditor.setHint(getPromptStringForUi());
        contentEditor.setClickable(true);

        contentEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (contentIsSuitable(text))
                    setContent(text);
                else
                    //Their new entry is not valid, but they have still eliminated their old entry. Thus it becomes null.
                    setContent(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return contentEditor;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {}

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
