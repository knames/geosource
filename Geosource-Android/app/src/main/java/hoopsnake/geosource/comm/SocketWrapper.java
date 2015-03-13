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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
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

    private int portNum = 9001;
    //TODO change the IP address to come from a config file, or some other option.
    private String ipaddress = "104.236.112.44";

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Socket outSocket;

    private static final String logTag = "geosource comm";
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
                Log.i(logTag,"Connection Established");
            OutputStream outStream = outSocket.getOutputStream();
            InputStream inStream = outSocket.getInputStream();

            CipherOutputStream cipherOut = new CipherOutputStream(outStream, desCipher);
            GZIPOutputStream zipOut = new GZIPOutputStream(cipherOut);
            out = new ObjectOutputStream(zipOut);

            CipherInputStream cipherIn = new CipherInputStream(inStream, desCipher);
            GZIPInputStream zipIn = new GZIPInputStream(cipherIn);
            in = new ObjectInputStream(zipIn);

            Log.i(logTag, "Stream Created");
        }
        catch (InvalidKeyException IKe)
        {
            Log.e(logTag, "invalid encryption password");
        }
        catch (InvalidKeySpecException IKSe) {
            Log.e(logTag, "invalid key specification");
        }
        catch (NoSuchAlgorithmException NSAe) {
            Log.e(logTag, "invalid encryption algorithm");
        }
        catch (NoSuchPaddingException NSPe) {
            Log.e(logTag, "No Such Padding");
        }
    }
}