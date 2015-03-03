package FileSystem;

import ServerClientShared.FieldWithoutContent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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
}
