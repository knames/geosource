package FileSystem;

import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Connor
 */
public class FileAccess {

    public FileAccess() throws URISyntaxException {

        try {
            FolderExists();
        }
        catch (Exception e)
        {
            System.out.println("Failed to check/create folders!");
            e.printStackTrace();
        }
    }

    /**
     * Checks if the media/spec folder exists..
     * 
     * @return true if there, false otherwise
     * @throws Exception
     */
    private void FolderExists() {
        String[] folderNames = { "media", "media/spec", "media/fieldContent" };

        for (int i = 0; i < folderNames.length; i++) {
            Path folder = Paths.get(folderNames[i]);
            if (Files.notExists(folder, LinkOption.NOFOLLOW_LINKS)) {
                File dir = new File(folderNames[i]);
                dir.mkdir();
                System.out.println(folderNames[i]
                                    + " has been created.");
            } else {
                System.out.println(folderNames[i] + " exists!");
            }
        }
    }

    public ArrayList<FieldWithoutContent> getFormSpec(String fileName) {
        FileInputStream fileRead;
        ObjectInputStream in;
        try {
            fileRead = new FileInputStream("media/spec/" + fileName); // file input
            in = new ObjectInputStream(fileRead); // stream to object
            ArrayList<FieldWithoutContent> returnList = (ArrayList<FieldWithoutContent>)in.readObject();
            in.close();
            return returnList;
        } catch (IOException IOe) {
            System.out.println("Reader initialization failed!");
            return null;
        } catch (ClassNotFoundException CNFe) {
            System.out.println("Parsing Specification failed");
            return null;
        }
    }
    
    public void saveFormSpec(ArrayList<FieldWithoutContent> fields, String owner, int identifier)
    {
        try (FileOutputStream fout = new FileOutputStream("media/spec/" + owner + "." + identifier); ObjectOutputStream oos = new ObjectOutputStream(fout))
        {
            oos.writeObject(fields); //save spec file
            oos.flush();
            oos.close();
        }
        catch (IOException IOe)
        {
            System.out.println("Error writing form specification file");
        }
    }

    public String saveField(FieldWithContent fieldContent) {
        UUID fileUUID = UUID.randomUUID();
        String fileName = fileUUID.toString();
        try
        {
            fieldContent.write("media/fieldContent/"+fileName);
        }
        catch (IOException IOe)
        {
            System.out.println("Saving field to file system failed" + IOe.getMessage());
        }
        return fileName;
    }
}
