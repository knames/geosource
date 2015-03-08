package FileSystem;

import ServerClientShared.FieldWithoutContent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Connor
 */
public class FileAccess {
    
    public ArrayList<FieldWithoutContent> getFormSpec(String filePath)
    {
        FileInputStream fileRead;
        ObjectInputStream in;
        try
        {
            fileRead = new FileInputStream(filePath); //file input
            in = new ObjectInputStream(fileRead); //stream to object
            return (ArrayList<FieldWithoutContent>)in.readObject();
        }
        catch (IOException IOe)
        {
            System.out.println("Reader initialization failed!");
            return null;
        }
        catch (ClassNotFoundException CNFe)
        {
            System.out.println("Parsing Specification failed");
            return null;
        }
    }
    
    public String savePicture(byte[] picture)
    {
        UUID fileUUID = UUID.nameUUIDFromBytes(picture);
        String fileName = fileUUID.toString();
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            fileOut.write(picture);
        }
        catch (IOException IOe)
        {
            System.out.println("Error attempting to write picture file");
        }
        return fileName;
    }
}
