package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.io.Serializable;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Interface for Fields, to be accessed by the UI and control level of the android app.
 * Each implementation of field defines its own UI-level behaviour, as well as its basic
 * underlying Field behaviour.
 */
public interface AppFieldWithContent {

    /**
     *
     * @return the title of this field.
     */
    public String getTitle();
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
     * @return a String representation of the content in this field.
     */
    public String getContentStringRepresentation();

    /**
     *
     * @return a prompt string to be displayed to the ui, describing what occurs when the field's
     *  associated view is selected.
     */
    public String getPromptStringForUi();

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
     * @param context The UI to be populated.
     * @precond context is not null.
     * @postcond see return.
     * @return a View representing this field, to be displayed by the UI.
     */
    public View getContentViewRepresentation(Context context);

    //TODO uncomment this if it is needed.
//    /** Extract a vanilla field with content, for sending to the server. */
//    public FieldWithContent toFieldWithContent();
//

    /**
     * When this field is selected by the UI, execute the correct action.
     * @param activity the UI from which this field was selected.
     * @param requestCodeForIntent a request code this field may use if it uses the context to create an intent.
     * @precond context is not null.
     * @postcond the correct action is executed for the UI in question.
     */
    public void onSelected(Activity activity, int requestCodeForIntent);

    /**
     * When the result from this field being selected by the UI is returned, execute the correct action.
     * @param activity the UI which received the result.
     * @param resultCode the result, encoded as an Activity RESULT_* constant.
     * @param data any data that comes with the result.
     * @precond activity is not null, and resultCode consists of one of the predefined Activity.RESULT_* constants.
     *  data may be null.
     * @postcond the correct action is executed for the UI in question.
     */
    public void onResultFromSelection(Activity activity, int resultCode, Intent data);
}
