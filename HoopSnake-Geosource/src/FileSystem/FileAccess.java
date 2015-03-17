package FileSystem;

import Control.Controller;
import ServerClientShared.FieldWithoutContent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
    
    String homeDirectory;
    
    public FileAccess() throws URISyntaxException
    {
        homeDirectory = Controller.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
    	try {
			FolderExists();
		} catch (Exception e) {
			System.out.println("Failed to check/create folders!");
			e.printStackTrace();
		}
//        System.out.println("Home Directory is " + homeDirectory);
//        System.out.println("would find specs at " + homeDirectory + "media/spec/okenso.6");
    }

    
    
    /** Checks if the media/spec folder exists..
     * @return true if there, false otherwise
     * @throws Exception */
    private static boolean FolderExists() throws Exception{
    	String[] folderNames = {"media","media/spec", "media/audio","media/video", "media/image", "media/string"};
    	
    	for (int i = 0; i < folderNames.length; i++) {
    		Path folder = Paths.get(folderNames[i]);
    		if (Files.notExists(folder, LinkOption.NOFOLLOW_LINKS)){
    			File dir = new File(folderNames[i]);
    			dir.mkdir();
    			System.out.println(folderNames[i] + " has been created.");
    		} else {
    			System.out.println(folderNames[i] + " exists!");
    		}
    	}
    	
    	
        Path folder = Paths.get("media/spec");
        if (Files.notExists(folder, LinkOption.NOFOLLOW_LINKS)){
            throw new Exception("The media/spec folder does not exist");
        } else {
            return true;
        }
    }
    
    public ArrayList<FieldWithoutContent> getFormSpec(String fileName)
    {
        FileInputStream fileRead;
        ObjectInputStream in;
        String filePath = homeDirectory + fileName;
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
