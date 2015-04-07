package hoopsnake.geosource.data;

import android.content.Intent;
import android.view.View;

import java.io.Serializable;

import hoopsnake.geosource.IncidentActivity;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Interface for Fields, to be accessed by the UI and control level of the android app.
 * Each implementation of field defines its own UI-level behaviour, as well as its basic
 * underlying Field behaviour.
 *
 * GUARANTEE: Any View produced by an appField that may launch a new activity or fragment has its tag (view.getTag())
 * set to equal the view's position in the field list.
 */
public interface AppField extends Serializable {

    /**
     * Set the activity pointer to which this AppField is bound. (So that it can render on the UI properly).
     * @param activity the activity which will be displaying this AppField.
     */
    public void setActivity(IncidentActivity activity);

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

//    /**
//     *
//     * @precond this field's view has already been initialized in the UI, so whatever view it uses is not null.
//     * @postcond Set the position of this field in the fieldList, so that it knows its own position.
//     * @param position the numbered position in the fieldList for this Field.
//     */
//    public void setPositionInFieldList(int position);

    /**
     *
     * @param newContent the new content for this field.
     * @precond newContent is suitable for this field. null newContent is always accepted.
     * @postcond this field's content is set to newContent.
     */
    public void setContent(Serializable newContent);

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
     * @param requestCodeForIntent a request code by which the returned view will be able to launch new activities.
     * @precond context is not null.
     * @postcond see return.
     * @return a View representing this field, to be displayed by the UI. This view should be up-to-date
     * with the field's content status: if content is not null, return a view showing the filled version.
     */
    public View getFieldViewRepresentation(final int requestCodeForIntent);

    /**
     * When the result from this field being selected by the UI is returned, execute the correct action.
     * @param resultCode the result, encoded as an Activity RESULT_* constant.
     * @param data any data that comes with the result.
     * @precond activity is not null, and resultCode consists of one of the predefined Activity.RESULT_* constants.
     *  data may be null.
     * @postcond the correct action is executed for the UI in question.
     */
    public void onResultFromSelection(int resultCode, Intent data);
}
