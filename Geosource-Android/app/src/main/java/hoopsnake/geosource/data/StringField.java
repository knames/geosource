package hoopsnake.geosource.data;

import android.content.Context;

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
    public boolean contentIsFilled() {
        return wrappedField.content != null;
    }

    @Override
    public void populateUi(Context context) {
        //TODO implement this.
    }
}
