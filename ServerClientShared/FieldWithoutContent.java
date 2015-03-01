package hoopsnake.geosource.data;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wsv759 on 18/02/15.
 * A single field within an incident.
 * Includes title, type, and a content field. The content field may be null.
 */
public class FieldWithoutContent extends Field implements Serializable {

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public FieldWithoutContent(String title, FieldType type, boolean isRequired)
    {
       super(title, type, isRequired);
    }

    /** Serializable implementation. */
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        title = (String) in.readObject();
        type = (FieldType) in.readObject();
    }

    /** Serializable implementation. */
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(title);
        out.writeObject(type);
    }

    /** Serializable implementation. */
    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("Stream data required");
    }
}
