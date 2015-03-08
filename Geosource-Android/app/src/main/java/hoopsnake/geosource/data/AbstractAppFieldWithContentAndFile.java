package hoopsnake.geosource.data;

import android.net.Uri;

/**
 * Created by wsv759 on 07/03/15.
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

    public boolean contentIsFilled()
    {
        if (contentFileUri != null)
            return true;

        return wrappedField.content != null;
    }

    public String getContentStringRepresentation()
    {
        if (!contentIsFilled())
            return "";

        return contentFileUri.toString();
    }

    public void setContentFileUri(Uri contentFileUri)
    {
        if (contentFileUri == null)
            throw new RuntimeException("null Uri.");

        if (isCorrectFileType(contentFileUri))
            this.contentFileUri = contentFileUri;
        else
            throw new RuntimeException(contentFileUri.toString() + "has incorrect file type.");
    }

    /**
     * @precond contentFileUri is not null.
     * @param contentFileUri
     * @return
     */
    public abstract boolean isCorrectFileType(Uri contentFileUri);
}
