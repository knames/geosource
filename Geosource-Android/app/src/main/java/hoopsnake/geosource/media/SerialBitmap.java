package hoopsnake.geosource.media;
//TODO not use this class anymore, in the same capacity. It should not form the content for a field! As we are using byte arrays instead.
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by wsv759 on 18/02/15.
 * A Serializable
 */
public class SerialBitmap implements Serializable {

    /** this stores the image. */
    public Bitmap bitmap;

    // TODO: Finish this constructor

    /**
     *
     * @param fileUri the Uri of the file to be stored in this SerialBitmap.
     */
    public SerialBitmap(Uri fileUri) {

        File imageFile = new File(fileUri.getPath());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("uri " + fileUri + " not found.");
        }

        bitmap = BitmapFactory.decodeStream(fis);
    }

    // Converts the Bitmap into a byte array for serialization
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte bitmapBytes[] = byteStream.toByteArray();
        out.write(bitmapBytes, 0, bitmapBytes.length);
    }

    // Deserializes a byte array representing the Bitmap and decodes it
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while((b = in.read()) != -1)
            byteStream.write(b);
        byte bitmapBytes[] = byteStream.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }
}
