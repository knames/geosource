package hoopsnake.geosource.data;

import android.net.Uri;

import ServerClientShared.FieldWithContent;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Abstract implementation of AppFieldWithContent, intended to be extended by all content types
 * whose content is stored in a file on android (e.g. image, video, audio.)
 */
public abstract class AbstractAppFieldWithContentAndFile extends AbstractAppFieldWithContent {

    public AbstractAppFieldWithContentAndFile(FieldWithContent fieldToWrap)
    {
        super(fieldToWrap);
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
    protected Uri contentFileUri;

    public Uri getContentFileUri() {
        return contentFileUri;
    }

    @Override
    public boolean contentIsFilled()
    {
        if (contentFileUri != null)
            return true;

        return wrappedField.getContent() != null;
    }

    @Override
    public String getContentStringRepresentation()
    {
        if (!contentIsFilled())
            return "this is an empty file";

        return contentFileUri.toString();
    }

    /**
     *
     * @param contentFileUri the Uri referring to a file on the device. This file should currently
     *                       store the content associated with this field.
     * @precond the contentFileUri is not null.
     * @postcond sets the fileUri of this field to be contentFileUri.
     */
    public void setContentFileUri(Uri contentFileUri)
    {
        assertNotNull(contentFileUri);

        if (usesFilesOfType(contentFileUri))
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
    public abstract boolean usesFilesOfType(Uri contentFileUri);
}
