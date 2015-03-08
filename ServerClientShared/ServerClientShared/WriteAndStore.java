package ServerClientShared;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



/**
 * Created by kts192 on 7/03/15.
 * this class writes a FieldWithoutContent to file, naming it in the following
 * format: <username>.specNumber ex: okenso.1
 */
public class WriteAndStore implements Serializable  {
	
	/** takes an arrayed FieldWithoutContent list and writes the object to file. Names it 
	 * after the username and specnumber
	 * @param fwc the arrayedlist of a FieldWithoutContent class
	 * @param chName the name of the channel
	 * @param username the owner of the channel
	 * @param ispublic whether the channel is private or public */
	//public WriteAndStore(String fwc, String chName, String username, boolean ispublic){ //string version for testing
	public WriteAndStore(ArrayList<FieldWithoutContent> fwc, String chName, String username, boolean ispublic){
		   try{
			   	dbInsertSpec spec = new dbInsertSpec(chName, username, ispublic);
			   	if (spec.getSpecNum() != -1){ // if it fails, dont write anything.
					FileOutputStream fout = new FileOutputStream("media/spec/" + username + "." + spec.getSpecNum());
					ObjectOutputStream oos = new ObjectOutputStream(fout);   
					oos.writeObject(fwc);
					oos.close();
					System.out.println("Done");
			   	}
		 
			   }catch(Exception ex){
				   ex.printStackTrace();
			   }
	}
	
	/** Checks if the media/spec folder exists..
	 * @return true if there, false otherwise
	 * @throws Exception */
	private static boolean FolderExists() throws Exception{
		Path folder = Paths.get("media/spec");
		if (Files.notExists(folder, LinkOption.NOFOLLOW_LINKS)){
			throw new Exception("The media/spec folder does not exist");
		} else {
			return true;
		}
	}
	
	/** Tests the functionality of this class */
	public static void main (String[] args) throws Exception{
		System.out.println("oy");
		FolderExists();
		/*
		dbInsertSpec spec = new dbInsertSpec("pothozlezz", "frank", true);
		System.out.println(spec.getSpecNum());
		*/
		//WriteAndStore test = new WriteAndStore("yes", "newname", "okenso", true); // this tests the string version.

	}
}
