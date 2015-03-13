package ServerClientShared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wsv759 on 12/03/15.
 */
public abstract class FileFieldWithContent extends FieldWithContent {
    public FileFieldWithContent(FieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    protected void readObjectHelper(ObjectInputStream in) throws ClassNotFoundException, IOException {
        super.readObjectHelper(in);

        setContent((byte[]) in.readObject());
    }

    @Override
    protected void writeObjectHelper(ObjectOutputStream out) throws IOException {
        super.writeObjectHelper(out);

        //We know it's a byte array.
        out.writeObject(content);
    }

    @Override
    public boolean contentMatchesType(Serializable content)
    {
        return content instanceof byte[];
    }
}
