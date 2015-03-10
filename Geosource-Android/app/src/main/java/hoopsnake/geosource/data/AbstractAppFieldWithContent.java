package hoopsnake.geosource.data;

import java.io.Serializable;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Abstract implementation of AppFieldWithContent, wrapping an underlying vanilla Java FieldWithContent
 * object. (the object that is actually sent to the server.)
 *
 * This class is constructed with a FieldWithContent reference as a parameter. That reference is
 * guaranteed to stay up to date with the modifications to this class's wrapped field. So other
 * objects can make use of that reference.
 *
 * All implementations of AppFieldWithContent should extend this.
 */
public abstract class AbstractAppFieldWithContent implements AppFieldWithContent {

    /**
     * Underlying field object that contains the attributes to be acted upon by
     * the app. Its type should match the AppField that wraps it.
     */
    protected FieldWithContent wrappedField;

    /**
     * Construct a new AppFieldWithContent, wrapping (not copying) a FieldWithContent.
     * Thus the reference to that FieldWithContent is guaranteed to remain up to date.
     * @param fieldToWrap a FieldWithContent that will be wrapped (not copied).
     * @precond fieldToWrap is not null, and is of the correct type.
     * @postcond a new AppFieldWithContent is created with fieldToWrap as an underlying field.
     */
    public AbstractAppFieldWithContent(FieldWithContent fieldToWrap)
    {
        assertNotNull(fieldToWrap);
        this.wrappedField = fieldToWrap;
    }

    @Override
    public String getTitle()
    {
        return wrappedField.getTitle();
    }

    @Override
    public boolean isRequired()
    {
        return wrappedField.isRequired();
    }

    @Override
    public boolean contentIsSuitable(Serializable content)
    {
        return FieldWithContent.contentMatchesType(content, wrappedField.getType());
    }
}