package ServerClientShared;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wsv759 on 18/02/15.
 *
 * A single field within an incident.
 * Includes title, type, and a content field. The content field may be null.
 * Type and content are guaranteed to match.
 */
public class FieldWithContent extends Field implements Serializable
{
    /**
     * The contents of the field, as entered in by the user. This must adhere to the type of this
     * Field.
     */
    protected Serializable content;

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    /**
     * Construct with non-null content.
     * @param title
     * @param type
     * @param isRequired
     * @param content
     */
    public FieldWithContent(String title, FieldType type, boolean isRequired, Serializable content)
    {
        super(title, type, isRequired);

        setContent(content);
        if (content == null)
		    throw new RuntimeException(content + " is null.");

    }

    /**
     * Construct with null content.
     * @param title
     * @param type
     * @param isRequired
     */
    public FieldWithContent(String title, FieldType type, boolean isRequired)
    {
        super(title, type, isRequired);

        setContent(null);
    }


    /** Serializable implementation. */
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        title = (String) in.readObject();
        type = (FieldType) in.readObject();

        switch (type)
        {
            case IMAGE:
                setContent((byte[]) in.readObject());
                break;
            case STRING:  
                setContent((String) in.readObject());
                break;
            case VIDEO:
                //TODO implement this.
                throw new UnsupportedOperationException("Not supported yet.");
                //break; PUT THIS BACK AFTER IMPLEMENTATION
            case AUDIO:
                //TODO implement this.
                throw new UnsupportedOperationException("Not supported yet.");
                //break; PUT THIS BACK AFTER IMPLEMENTATION
            default:
                throw new RuntimeException("invalid type");
        }
    }

    /** Serializable implementation. */
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(title);
        out.writeObject(type);
        out.writeObject(content);
    }

    /** Serializable implementation. */
    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("Stream data required");
    }

    /**
     * Sets content to the specified value, but only if newContent matches the current type.
     * newContent can be null.
     * @param newContent a Serializable object.
     */
    public void setContent(Serializable newContent)
    {
        if (type == null)
		    throw new RuntimeException(type + " is null.");

        if (contentMatchesType(newContent, type))
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
     * @param content a Serializable content object. This is probably the 'content' field from a
     *                FieldWithContent object.
     * @param type  An instance of FieldType. This is probably the 'type' field from a FieldWithContent object.
     * @return true if the content and type match, false otherwise.
     */
    public static boolean contentMatchesType(Serializable content, FieldType type)
    {
        if (type == null)
		    throw new RuntimeException(type + " is null.");

        if (content == null)
            return true;

        switch (type)
        {
            case IMAGE:
                return (content instanceof byte[]);
            case VIDEO:
                //TODO implement this.
                throw new RuntimeException("Video object type not yet implemented, sorry!");
            case STRING:
                return (content instanceof String);
            case AUDIO:
                //TODO implement this.
                throw new RuntimeException("Sound object type not yet implemented, sorry!");
            default:
                throw new RuntimeException("field type is invalid.");
        }
    }
}
