package hoopsnake.geosource.data;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

import ServerClientShared.Geotag;
import ServerClientShared.GeotagFieldWithContent;
import hoopsnake.geosource.AppGeotagWrapper;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;

/**
 * Created by wsv759 on 21/03/15.
 */
public class AppGeotagField extends AbstractAppField{

    private TextView tv;

    public AppGeotagField(GeotagFieldWithContent fieldToWrap, int fieldPosInList, IncidentActivity activity) {
        super(fieldToWrap, fieldPosInList, activity);
    }

    @Override
    public String getContentStringRepresentation() {
        Geotag g = (Geotag) wrappedField.getContent();
        if (g.exists())
            return "Geotag:\ntime in millis: " + g.getTimestamp() + "\nlongitude: " + g.getLongitude() + "\nlatitude: " + g.getLatitude();
        else
            return "Geotag not yet determined.";
    }

    public String getTimestampStringRepresentation(){
        Geotag g = (Geotag) wrappedField.getContent();
        return Long.toString(g.getTimestamp());
    }
    @Override
    public boolean contentIsFilled()
    {
        if (wrappedField.getContent() == null)
            Log.d(LOG_TAG, "geotagfield content is null.");
        else if (!((Geotag) wrappedField.getContent()).exists())
            Log.d(LOG_TAG, "geotagfield content does not exist.");

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

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {

    }

    private void readObjectNoData() throws ObjectStreamException
    {

    }
}
