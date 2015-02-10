package Communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;

/**
 *
 * @author Connor
 */
public class Packet implements Serializable{
    
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;
    
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        
    }
    
     private void writeObject(ObjectOutputStream out) throws IOException
     {
        
     }
    
    
}
