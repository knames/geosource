package hoopsnake.geosource.data;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by wsv759 on 07/03/15.
 */
public interface AppFieldWithContent {
    public String getContentStringRepresentation();

    /**
     *
     * @return
     */
    public boolean typeCouldHaveUri();

    /**
     * @precond content may be any implementation of Serializable, and could be null.
     * Is?
     * @param content an object that is intended to be serialized and sent back to the server,
     *          presumably as part of this field.
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
