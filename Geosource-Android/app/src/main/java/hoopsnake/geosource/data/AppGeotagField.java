package hoopsnake.geosource.data;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import ServerClientShared.Geotag;
import ServerClientShared.GeotagFieldWithContent;
import hoopsnake.geosource.AppGeotagWrapper;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;

/**
 * Created by wsv759 on 21/03/15.
 */
public class AppGeotagField extends AbstractAppField implements Serializable {

    private TextView tv;

    public AppGeotagField(GeotagFieldWithContent fieldToWrap, IncidentActivity activity) {
        super(fieldToWrap, activity);
    }

    @Override
    public String getContentStringRepresentation() {
        return wrappedField.getContent().toString();
    }

    @Override
    public boolean contentIsFilled()
    {
        return (wrappedField.getContent() != null && ((Geotag) wrappedField.getContent()).exists());
    }

    @Override
    public View getContentViewRepresentation(int requestCodeForIntent) {
        tv = (TextView) activity.getLayoutInflater().inflate(R.layout.field_geotag_view, null);
        //This is for the faded out effect. (rather than setText().)
        tv.setHint(getContentStringRepresentation());
        return tv;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {}

    public void onContentUpdated()
    {
        if (tv != null)
            tv.setHint(getContentStringRepresentation());
    }

    public void registerForGeotag(AppGeotagWrapper geotagWrapper)
    {
        geotagWrapper.setRegisteredField(this);
        setContent(geotagWrapper.toGeotag());
    }

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        super.writeObjectHelper(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        super.readObjectHelper(in);
    }

    private void readObjectNoData() throws ObjectStreamException
    {
        super.readObjectNoDataHelper();
    }
}
