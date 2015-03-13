package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 18/02/15.
 *
 * A single field within an incident.
 * Includes title, type, and a content field. The content field may be null.
 * Type and content are guaranteed to match.
 */
public abstract class FieldWithContent extends Field
{
    /**
     * The contents of the field, as entered in by the user. This must adhere to the type of this
     * Field.
     */
    Serializable content;

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    /**
     * Construct with non-null content.
     * @param title
     * @param type
     * @param isRequired
     * @param content
     */
    public FieldWithContent(String title, String type, boolean isRequired, Serializable content)
    {
        super(title, type, isRequired);

        setContent(content);
        if (content == null)
		    throw new RuntimeException("content is null.");

    }

    /**
     * Construct with null content, out of a field without content.
     * @param fieldWithoutContent the fieldWithoutContent to construct this out of.
     */
    FieldWithContent(FieldWithoutContent fieldWithoutContent)
    {
        super(fieldWithoutContent.getTitle(), fieldWithoutContent.getType(), fieldWithoutContent.isRequired());

        setContent(null);
    }

    /**
     * Construct with null content.
     * @param title
     * @param type
     * @param isRequired
     */
    public FieldWithContent(String title, String type, boolean isRequired)
    {
        super(title, type, isRequired);

        setContent(null);
    }

    /**
     * Sets content to the specified value, but only if newContent matches the current type.
     * newContent can be null.
     * @param newContent a Serializable object.
     */
    public void setContent(Serializable newContent)
    {
        if (type == null)
		    throw new RuntimeException("type is null.");

        if (contentMatchesType(newContent))
            content =  newContent;
        else
            throw new RuntimeException("field content is not of type " + type + ".");
    }

    public Serializable getContent() {
        return content;
    }

    /**
     * Does the given content object match the given field type? i.e. is it an admissible object
     * for the specified field type?
     *
     * NOTE: null content is always admissible.
     *
     * @param content the item to compare for suitability
     * @return true if the content and type match, false otherwise.
     */
    public abstract boolean contentMatchesType(Serializable content);
}
