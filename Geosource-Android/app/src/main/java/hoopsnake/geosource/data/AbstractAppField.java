package hoopsnake.geosource.data;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ServerClientShared.FieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Abstract implementation of AppField, wrapping an underlying vanilla Java FieldWithContent
 * object. (the object that is actually sent to the server.)
 *
 * This class is constructed with a FieldWithContent reference as a parameter. That reference is
 * guaranteed to stay up to date with the modifications to this class's wrapped field. So other
 * objects can make use of that reference.
 *
 * All implementations of AppField should extend this.
 */
public abstract class AbstractAppField implements AppField {

    /**
     * The weight of each content view, so that it takes up the appropriate amount of screen compared with the field title.
     */
    private static final float CONTENT_VIEW_WEIGHT = 0.8f;

    private static final int POSITION_VIEW_FIELD_TITLE = 0;
    /**
     * Underlying field object that contains the attributes to be acted upon by
     * the app. Its type should match the AppField that wraps it.
     */
    final FieldWithContent wrappedField;

    public IncidentActivity getActivity() {
        return activity;
    }

    /**
     * The activity that will be displaying this field on the UI.
     */
    final IncidentActivity activity;

    String LOG_TAG = "geosource ui";

    /**
     * Construct a new AppField, wrapping (not copying) a FieldWithContent.
     * Thus the reference to that FieldWithContent is guaranteed to remain up to date.
     * @param fieldToWrap a FieldWithContent that will be wrapped (not copied).
     * @param activity
     * @precond fieldToWrap is not null, and is of the correct type.
     * @postcond a new AppField is created with fieldToWrap as an underlying field.
     */
    public AbstractAppField(FieldWithContent fieldToWrap, IncidentActivity activity)
    {
        assertNotNull(fieldToWrap);
        assertNotNull(activity);
        this.wrappedField = fieldToWrap;
        this.activity = activity;
    }

    /**
     * Naive implementation, to be overridden by most extensions of this class.
     */
    @Override
    public boolean contentIsFilled()
    {
        return wrappedField.getContent() != null;
    }

    @Override
    public String getTitle()
    {
        return wrappedField.getTitle();
    }

    @Override
    public boolean isRequired()
    {
        return wrappedField.isRequired();
    }

    @Override
    public boolean contentIsSuitable(Serializable content)
    {
        return wrappedField.contentMatchesType(content);
    }

    @Override
    public void setContent(Serializable content)
    {
        assertTrue(contentIsSuitable(content));

        wrappedField.setContent(content);
    }

    @Override
    public View getFieldViewRepresentation(final int requestCodeForIntent)
    {
        LinearLayout fieldView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.field_view, null);
        TextView titleView = (TextView) fieldView.getChildAt(POSITION_VIEW_FIELD_TITLE);

        String fieldLabel = getTitle() + ":";
        if (isRequired())
            fieldLabel += "*";

        titleView.setText(fieldLabel);
        View contentView = getContentViewRepresentation(requestCodeForIntent);
        //Make sure the content view knows which field in the list it corresponds to as well.
        contentView.setTag(fieldView.getTag());

        fieldView.addView(contentView);
        return fieldView;
    }

    /**
     * @param requestCodeForIntent a request code by which the returned view will be able to launch new activities.
     * @return the view representing the field's content itself, to be placed to the left of the textview
     * that represents the title for this field.
     */
    abstract View getContentViewRepresentation(final int requestCodeForIntent);


    /** Serializable implementation. */
    void readObjectNoDataHelper() throws InvalidObjectException {
        wrappedField.readObjectNoDataHelper();
    }

    void readObjectHelper(ObjectInputStream in) throws ClassNotFoundException, IOException {
        wrappedField.readObjectHelper(in);
    }

    void writeObjectHelper(ObjectOutputStream out) throws IOException {
        wrappedField.writeObjectHelper(out);
    }
}
