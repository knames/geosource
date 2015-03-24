package hoopsnake.geosource.data;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import ServerClientShared.GeotagFieldWithContent;
import hoopsnake.geosource.Geotag;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;

/**
 * Created by wsv759 on 21/03/15.
 */
public class AppGeotagField extends AbstractAppField {

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
    public boolean contentIsSuitable(Serializable content)
    {
        return content instanceof Geotag;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {}

    public void onContentUpdated()
    {
        if (tv != null)
            tv.setHint(getContentStringRepresentation());
    }

    @Override
    public void setContent(Serializable content)
    {
        super.setContent(content);

        ((Geotag) content).setRegisteredField(this);
    }
}
