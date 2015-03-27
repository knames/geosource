package ServerClientShared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Created by wsv759 on 21/03/15.
 */
public class GeotagFieldWithContent extends FieldWithContent implements Serializable {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public GeotagFieldWithContent(GeotagFieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content) {
        return content instanceof Geotag;
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
