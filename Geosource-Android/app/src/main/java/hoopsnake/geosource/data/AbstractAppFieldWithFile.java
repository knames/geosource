package hoopsnake.geosource.data;

import android.net.Uri;
import android.view.View;

import ServerClientShared.FieldWithContent;
import hoopsnake.geosource.IncidentActivity;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Abstract implementation of AppField, intended to be extended by all content types
 * whose content is stored in a file on android (e.g. image, video, audio.)
 */
public abstract class AbstractAppFieldWithFile extends AbstractAppField {

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public AbstractAppFieldWithFile(FieldWithContent fieldToWrap, IncidentActivity activity)
    {
        super(fieldToWrap, activity);
    }

    /**
     * The Uri representing the file which will be converted into the Serializable content object
     * at some point. If this is non-null, the content field is assumed to be filled.
     *
     * If this is null, the content field may still be non-null!
     *
     * This should only ever be non-null if the content actually corresponds to a file
     * (i.e. Image, Video, or Audio content.)
     */
    private Uri contentFileUri;

    Uri getContentFileUri() {
        return contentFileUri;
    }

    @Override
    public boolean contentIsFilled()
    {
        return contentFileUri != null || wrappedField.getContent() != null;
    }

    @Override
    public String getContentStringRepresentation()
    {
        if (!contentIsFilled())
            return "this is an empty file";

        return contentFileUri.toString();
    }

    @Override
    View getContentViewRepresentation(final int requestCodeForIntent)
    {
        if (contentIsFilled())
            return getFilledContentViewRepresentation();
        else
            return getEmptyContentViewRepresentation(requestCodeForIntent);
    }

    /**
     *
     * @return a content view representation for if the content is filled.
     */
    abstract View getFilledContentViewRepresentation();

    /**
     *
     * @return a content view representation for if the content is empty.
     */
    abstract View getEmptyContentViewRepresentation(final int requestCodeForIntent);

    /**
     *
     * @param contentFileUri the Uri referring to a file on the device. This file should currently
     *                       store the content associated with this field.
     * @precond none. contentFileUri can be null.
     * @postcond sets the fileUri of this field to be contentFileUri.
     */
    public void setContentFileUri(Uri contentFileUri)
    {
        if (contentFileUri == null || usesFilesOfType(contentFileUri))
            this.contentFileUri = contentFileUri;
        else
            throw new RuntimeException(contentFileUri.toString() + "has incorrect file type.");
    }

    /**
     * @param contentFileUri the Uri representing the content file to check. Does it contain the correct
     *                       filetype for this field?
     * @precond contentFileUri is not null.
     * @postcond see return.
     * @return return true if this field uses this type of file for its content, or false otherwise.
     */
    abstract boolean usesFilesOfType(Uri contentFileUri);
}
