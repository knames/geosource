package hoopsnake.geosource;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 27/03/15.
 */
public class FileIO {
    private static final String LOG_TAG = "geosource FileIO";

    /**
     * @precond none of the params are null.
     * @param context the context from which to search for the given file.
     * @param fileName the relative fileName (absolute path will be provided by context.)
     * @param object a serializable object.
     * @return true if the object was written successfully, false otherwise.
     */
    public static boolean writeObjectToFile(Context context, Serializable object, String fileName) {

        assertNotNull(object);
        ObjectOutputStream objectOut = null;
        try {

            FileOutputStream fileOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.flush();
            fileOut.getFD().sync();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @precond none of the params are null.
     * @param context the context from which to search for the given file.
     * @param fileName the relative fileName (absolute path will be provided by context.)
     * @return the object from the given file, or null if no such object exists.
     */
    public static Serializable readObjectFromFile(Context context, String fileName) {

        ObjectInputStream objectIn = null;
        Serializable object = null;
        try {

            FileInputStream fileIn = context.getApplicationContext().openFileInput(fileName);
            objectIn = new ObjectInputStream(fileIn);
            object = (Serializable) objectIn.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "file " + fileName + " not found.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return object;
    }

    /**
     * @precond none of the params are null.
     * @param fileName the absolute fileName
     * @return the object from the given file, or null if no such object exists.
     */
    public static Serializable readObjectFromFileNoContext(String fileName) {

        ObjectInputStream objectIn = null;
        Serializable object = null;
        try {

            FileInputStream fileIn = new FileInputStream(fileName);
            objectIn = new ObjectInputStream(fileIn);
            object = (Serializable) objectIn.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "file " + fileName + " not found.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return object;
    }

    /**
     * @precond none of the params are null.
     * @param fileName the relative fileName (absolute path will be provided by context.)
     * @param object a serializable object.
     * @return true if the object was written successfully, false otherwise.
     */
    public static boolean writeObjectToFileNoContext(Serializable object, String fileName) {

        assertNotNull(object);
        ObjectOutputStream objectOut = null;
        try {

            FileOutputStream fileOut = new FileOutputStream(fileName);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.flush();
            fileOut.getFD().sync();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private static boolean readObjectFromFile()
}
