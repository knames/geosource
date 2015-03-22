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
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public StringFieldWithContent(StringFieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content) {
        return content instanceof String;
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        super.readObjectHelper(in);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        super.writeObjectHelper(out);
    }

    private void readObjectNoData() throws ObjectStreamException
    {
        super.readObjectNoDataHelper();
    }
}
