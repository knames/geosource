package Communication;

import Control.Controller;
import ServerClientShared.Commands.IOCommand;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.Incident;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * @author Connor
 */
public class CommSocket implements Runnable{
    
    public static int portNum = 0;
    private static Controller controller = null;
    private final int SocketNum; //identification number
    
    private final static int compressionBlockSize = 1024; //indicates the size in bytes of the blocks that are sent over the stream

    protected static ServerSocket serverSocket = null;
    private Socket clientSocket;
    
    // Password must be at least 8 characters
    private static final String password = "hiedlbrand";
    
    public CommSocket(Controller parentControl, int SocketNumber)
    {
        if (serverSocket == null)
        {
            try
            {
                serverSocket = new ServerSocket(portNum); //global socket bind
            }
            catch (IOException IOe)
            {
                System.out.println("Binding server Socket Failed");
            }
        }
        
        if (controller == null)
        {
            controller = parentControl; //controller to query for data/files
        }
        
        SocketNum = SocketNumber;
    }
    
    /**
     * run the socket, which wait for incoming connections and deals with them
     */
    @Override
    public void run()
    {
        try
        {
            // Bind client and create streams
            clientSocket = serverSocket.accept();

            System.out.println("Client Connected");

            OutputStream outStream = clientSocket.getOutputStream();
            InputStream inStream = clientSocket.getInputStream();
            int typeCommand = inStream.read();
            switch (typeCommand)
            {
                case 1: //Android
                {
                    androidRun(inStream, outStream);
                    break;
                }
                case 2: //Website
                {
                    websiteRun(inStream, outStream);
                    break;
                }
            }
        }
        catch (IOException IOe)
        {
            throw new RuntimeException("Binding ordinary socket failed");
        }
        
        controller.socketComplete(SocketNum);
    }
    
    /**
     * this socket is dealing with an android app
     * @param inStream an input stream, already created, to use in communication
     * @param outStream an already created output stream to use in communication
     */
    public void androidRun(InputStream inStream, OutputStream outStream)
    {
        try
        {
            // Create Key
            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            // Create Cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //CipherOutputStream cipherOut = new CipherOutputStream(outStream, desCipher);
            //CipherInputStream cipherIn = new CipherInputStream(inStream, desCipher);
            //CompressedBlockOutputStream zipOut = new CompressedBlockOutputStream(cipherOut, compressionBlockSize);
            //CompressedBlockInputStream zipIn = new CompressedBlockInputStream(cipherIn);
            
            ObjectOutputStream out = new ObjectOutputStream(outStream);
            ObjectInputStream in = new ObjectInputStream(inStream);
            out.flush();
            
            //Read command
            IOCommand command = (IOCommand)in.readObject();
            
            //Read/Send appropriate data
            switch (command)
            {
                case GET_FORM:
                {
                    String channelName = in.readUTF();
                    String ownerName = in.readUTF();
                    ArrayList<FieldWithoutContent> formList = controller.getForm(channelName, ownerName);
                    out.writeObject(formList);
                    out.flush();
                    break;
                }
                case SEND_INCIDENT:
                {
                    Incident newIncident = (Incident)in.readObject();
                    controller.dealWith(newIncident);
                    break;
                }
                case PING:
                {
                    out.writeObject(IOCommand.PING);
                    out.flush();
                    break;
                }
            }
            in.close();
            out.close();
            clientSocket.close();
        }
        catch (IOException IOe)
        {
            System.out.println("IO Exception: ");
            IOe.printStackTrace();
        }
        catch (InvalidKeyException IKe)
        {
            System.out.println("invalid encryption password");
        }
        catch (InvalidKeySpecException IKSe) {
            System.out.println("invalid key specification");
        }
        catch (NoSuchAlgorithmException NSAe) {
            System.out.println("invalid encryption algorithm");
        }
        catch (NoSuchPaddingException NSPe) {
            System.out.println("No Such Padding");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * this socket is dealing with the website
     * @param inStream an already created input stream to use in communication
     * @param outStream an already created output stream to use in communication
     */
    public void websiteRun(InputStream inStream, OutputStream outStream)
    {
        OutputStreamWriter out = new OutputStreamWriter(outStream);
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        
        try
        {
            String command = in.readLine();
            
            switch (command)
            {
                case "CREATE_CHANNEL":
                {
                    break;
                }
            }
        }
        catch (IOException IOe)
        {
            System.out.println("Communication error, aborting website communication");
        }
    }
}
