package ServerClientShared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

/**
 * Created by wsv759 on 12/03/15.
 */
public class ImageFieldWithoutContent extends FieldWithoutContent {
    public ImageFieldWithoutContent(String title, boolean isRequired) {
        super(title, "image", isRequired);
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        super.writeObjectHelper(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        super.readObjectHelper(in);
    }

    private void readObjectNoData() throws ObjectStreamException
    {
        super.readObjectNoDataHelper();
    }
}
