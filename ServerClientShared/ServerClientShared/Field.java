package ServerClientShared;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wsv759 on 19/02/15.
 *
 * A single field within an incident. Does not include a content field by default.
 */
public abstract class Field implements Serializable {
    private String title;
    private String type;
    private boolean isRequired;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }


    public boolean isRequired() {
        return isRequired;
    }

    Field(String title, String type, boolean isRequired)
    {
        this.title = title;
        this.type = type;
        this.isRequired = isRequired;

        if (title == null)
		    throw new RuntimeException("title is null.");
        if (type == null)
		    throw new RuntimeException("type is null.");
    }

    void readObjectHelper(ObjectInputStream in) throws ClassNotFoundException, IOException {
        title = in.readUTF();
        type = in.readUTF();
        isRequired = in.readBoolean();
    }

    void writeObjectHelper(ObjectOutputStream out) throws IOException {
        out.writeUTF(title);
        out.writeUTF(type);
        out.writeBoolean(isRequired);
    }

    /** Serializable implementation. */
    void readObjectNoDataHelper() throws InvalidObjectException {
        throw new InvalidObjectException("Stream data required");
    }
}
