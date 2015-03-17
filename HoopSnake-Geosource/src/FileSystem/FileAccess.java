package FileSystem;

import ServerClientShared.FieldWithoutContent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Connor
 */
public class FileAccess {
    
    String specFolder = "media/spec/";
    
    public FileAccess() {}
    
    public ArrayList<FieldWithoutContent> getFormSpec(String fileName)
    {
        FileInputStream fileRead;
        ObjectInputStream in;
        String filePath = specFolder + fileName;
        try
        {
            fileRead = new FileInputStream(filePath); //file input
            in = new ObjectInputStream(fileRead); //stream to object
            return (ArrayList<FieldWithoutContent>)in.readObject();
        }
        catch (IOException IOe)
        {
            System.err.println("Reader initialization failed!");
            return null;
        }
        catch (ClassNotFoundException CNFe)
        {
            System.err.println("Parsing Specification failed");
            return null;
        }
    }
    
    public String saveField(Serializable fieldContent)
    {
        UUID fileUUID = UUID.randomUUID();
        String fileName = fileUUID.toString();
        try (ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(fileName))) {
            fileOut.writeObject(fieldContent);
        }
        catch (IOException IOe)
        {
            System.err.println("Error attempting to write field");
        }
        return fileName;
    }
}
