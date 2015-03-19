package hoopsnake.geosource.comm;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author Connor, with changes for Android compatibility by William.
 *
 * This is a wrapper that creates a new socket that connects to a server, then initializes an
 * ObjectOutputStream and an ObjectInputStream with encryption and compression.
 */
public class SocketWrapper {

    private static final int COMPRESSION_BLOCK_SIZE = 1024;
    private int portNum = 9001;
    //TODO change the IP address to come from a config file, or some other option.
    private String ipaddress = "www.okenso.com";

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Socket outSocket;

    private static final String LOG_TAG = "geosource comm";
    // Password must be at least 8 characters
    private static final String password = "hiedlbrand";

    public Socket getOutSocket() {
        return outSocket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public SocketWrapper() throws IOException
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

            outSocket = new Socket(InetAddress.getByName(ipaddress), portNum);
            if (outSocket.isConnected())
                Log.i(LOG_TAG,"Connection Established");

            OutputStream outStream = outSocket.getOutputStream();
            InputStream inStream = outSocket.getInputStream();
//            CipherOutputStream cipherOut = new CipherOutputStream(outStream, desCipher);
//            CipherInputStream cipherIn = new CipherInputStream(inStream, desCipher);
//            CompressedBlockOutputStream zipOut = new CompressedBlockOutputStream(cipherOut, COMPRESSION_BLOCK_SIZE);
            out = new ObjectOutputStream(outStream);
            out.flush();

//            CompressedBlockInputStream zipIn = new CompressedBlockInputStream(cipherIn);
            in = new ObjectInputStream(inStream);

            Log.i(LOG_TAG, "Stream Created");
        }
        catch (InvalidKeyException IKe)
        {
            Log.e(LOG_TAG, "invalid encryption password");
        }
        catch (InvalidKeySpecException IKSe) {
            Log.e(LOG_TAG, "invalid key specification");
        }
        catch (NoSuchAlgorithmException NSAe) {
            Log.e(LOG_TAG, "invalid encryption algorithm");
        }
        catch (NoSuchPaddingException NSPe) {
            Log.e(LOG_TAG, "No Such Padding");
        }
    }
}