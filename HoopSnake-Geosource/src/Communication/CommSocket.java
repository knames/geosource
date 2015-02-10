package Communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Connor
 */
public class CommSocket {
    
    int portNum = 0;

    ObjectOutputStream out;
    ObjectInputStream in;

    ServerSocket serverSocket;
    Socket clientSocket;
    
    // Password must be at least 8 characters
    private static final String password = "hiedlbrand";
    
    public CommSocket()
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
            
            serverSocket = new ServerSocket(portNum);
            System.out.println("Server Bound");
            clientSocket = serverSocket.accept();
            OutputStream outStream = clientSocket.getOutputStream();
            InputStream inStream = clientSocket.getInputStream();
            System.out.println("Client Connected");
            
            CipherOutputStream cipherOut = new CipherOutputStream(outStream, desCipher);
            GZIPOutputStream zipOut = new GZIPOutputStream(cipherOut);
            out = new ObjectOutputStream(zipOut);
            
            CipherInputStream cipherIn = new CipherInputStream(inStream, desCipher);
            GZIPInputStream zipIn = new GZIPInputStream(cipherIn);
            in = new ObjectInputStream(zipIn);
        }
        catch (IOException IOe)
        {
            System.out.println("Binding server failed!");
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
        finally
        {
            return;
        }
    }
}
