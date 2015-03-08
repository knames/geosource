package hoopsnake.geosource.data;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by wsv759 on 07/03/15.
 */
public interface AppFieldWithContent {

    /**
     * @return a String representation of the content in this field.
     */
    public String getContentStringRepresentation();

    /**
     *
     * @return true if this is a required field, false otherwise.
     */
    public boolean isRequired();

    /**
     *
     * @return true if the content of this field has been set, either directly in the content object,
     * or as a file uri to be referenced.
     */
    public boolean contentIsFilled();

    /**
     * @param content an object that is intended to be serialized and sent back to the server,
     *          presumably as part of this field.
     * @precond content may be any implementation of Serializable, and could be null.
     * @postcond always returns a result.
     * @return True if the given content is suitable for this particular implementation of field,
     *      false otherwise.
     *      Also returns true if null.
     */
    public boolean contentIsSuitable(Serializable content);

    /**
     * The field should call the correct function on the UI passed in.
     * @param context The UI to be populated.
     */
    public void populateUi(Context context);
}
