package ServerClientShared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Created by wsv759 on 12/03/15.
 */
public class StringFieldWithContent extends FieldWithContent implements Serializable {

    public StringFieldWithContent(FieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content) {
        return content instanceof String;
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        super.readObjectHelper(in);

        setContent(in.readUTF());
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        super.writeObjectHelper(out);

        out.writeUTF((String) content);
    }

    private void readObjectNoData() throws ObjectStreamException
    {
        super.readObjectNoDataHelper();
    }
}
