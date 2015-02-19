package hoopsnake.geosource.data;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 18/02/15.
 */
public class Field extends EmptyField implements Serializable
{
    /**
     * The contents of the field, as entered in by the user. This must adhere to the type of the
     * field.
     */
    Serializable content;

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public Field(String title, FieldTypeManagement.FieldType type, Serializable content)
    {
        super(title, type);

        this.content = content;
        assertNotNull(content);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        title = (String) in.readObject();
        type = (FieldTypeManagement.FieldType) in.readObject();
        content = setContent((Serializable) in.readObject());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(title);
        out.writeObject(type);
        out.writeObject(content);
    }

    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("Stream data required");
    }

    Serializable setContent(Serializable newContent) throws InvalidObjectException
    {
        assertNotNull(type);
        assertNotNull(newContent);

        //TODO implement the check here.
        if (FieldTypeManagement.contentMatchesFieldType(newContent, type))
            return newContent;
        else
            throw new InvalidObjectException("field content is not of type " + type + ".");
    }
}
