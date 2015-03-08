package hoopsnake.geosource.data;

import java.io.Serializable;

/**
 * Created by wsv759 on 07/03/15.
 */
public abstract class AbstractAppFieldWithContent implements AppFieldWithContent {

    /**
     * Underlying field object that contains the attributes to be acted upon by
     * the app. Its type should match the AppField that wraps it.
     */
    protected FieldWithContent wrappedField;

    public AbstractAppFieldWithContent(FieldWithContent fieldToWrap)
    {
        //TODO add checking.
        this.wrappedField = fieldToWrap;
    }

    public boolean isRequired()
    {
        return wrappedField.isRequired();
    }

    public boolean contentIsSuitable(Serializable content)
    {
        return FieldWithContent.contentMatchesType(content, wrappedField.getType());
    }
}
