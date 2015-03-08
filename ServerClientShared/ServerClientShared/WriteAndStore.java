package ServerClientShared;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



/**
 * Created by kts192 on 7/03/15.
 */
public class WriteAndStore  {

	public WriteAndStore(ArrayList<FieldWithoutContent> fwc){
		
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
	

	public static void main (String[] args) throws Exception{
		System.out.println("oy");
		FolderExists();

	}
}
