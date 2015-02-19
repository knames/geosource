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
public class EmptyField implements Serializable {
    String title;
    FieldTypeManagement.FieldType type;

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public EmptyField(String title, FieldTypeManagement.FieldType type)
    {
        this.title = title;
        this.type = type;

        assertNotNull(title);
        assertNotNull(type);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        title = (String) in.readObject();
        type = (FieldTypeManagement.FieldType) in.readObject();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(title);
        out.writeObject(type);
    }

    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("Stream data required");
    }
}
