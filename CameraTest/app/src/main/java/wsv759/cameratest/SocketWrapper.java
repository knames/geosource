package wsv759.cameratest;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
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
 * @author Connor, with changes for Android compatibility by William.
 */
public class SocketWrapper {

    int portNum = 0;
    String ipaddress = "10.227.145.56";

    ObjectOutputStream out;
    ObjectInputStream in;

    Socket outSocket;

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

    public SocketWrapper(Context context) throws IOException
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
                Log.i(context.getString(R.string.app_name),"Connection Established");
            OutputStream outStream = outSocket.getOutputStream();
            InputStream inStream = outSocket.getInputStream();

            CipherOutputStream cipherOut = new CipherOutputStream(outStream, desCipher);
            GZIPOutputStream zipOut = new GZIPOutputStream(cipherOut);
            out = new ObjectOutputStream(zipOut);

            CipherInputStream cipherIn = new CipherInputStream(inStream, desCipher);
            GZIPInputStream zipIn = new GZIPInputStream(cipherIn);
            in = new ObjectInputStream(zipIn);

            Log.i(context.getString(R.string.app_name), "Stream Created");
        }
        catch (InvalidKeyException IKe)
        {
            Log.e(context.getString(R.string.app_name), "invalid encryption password");
        }
        catch (InvalidKeySpecException IKSe) {
            Log.e(context.getString(R.string.app_name), "invalid key specification");
        }
        catch (NoSuchAlgorithmException NSAe) {
            Log.e(context.getString(R.string.app_name), "invalid encryption algorithm");
        }
        catch (NoSuchPaddingException NSPe) {
            Log.e(context.getString(R.string.app_name), "No Such Padding");
        }
        finally
        {
            return;
        }
    }
}