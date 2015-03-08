package hoopsnake.geosource;

/**
 * Created by Anam on 2015-02-09. Modified by William. This adapts the array of fields
 * (or prompts to create fields) that are to be displayed while a user is filling out the
 * form for an incident.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hoopsnake.geosource.data.AppFieldWithContent;
import hoopsnake.geosource.data.AppIncident;

import static junit.framework.Assert.assertNotNull;

public class IncidentDisplayAdapter extends ArrayAdapter<AppFieldWithContent> {

    private String logTag = MainActivity.APP_LOG_TAG;
    private ArrayList<AppFieldWithContent> fieldList;
    private Context context;

    public IncidentDisplayAdapter(AppIncident incident, Context ctx) {
        super(ctx, R.layout.img_row_layout, incident.getFieldList());
        this.fieldList = incident.getFieldList();
        this.context = ctx;
    }

    public int getCount() {
        return fieldList.size();
    }

    public AppFieldWithContent getItem(int position) {
        return fieldList.get(position);
    }

    public long getItemId(int position) {
        return fieldList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        FieldHolder holder = new FieldHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.img_row_layout, null);
            // Now we can fill the layout with the right values
            TextView tv = (TextView) v.findViewById(R.id.name);
            TextView distView = (TextView) v.findViewById(R.id.dist);

            holder.fieldTitleView = tv;
            holder.promptView = distView;

            v.setTag(holder);
        }
        else
            holder = (FieldHolder) v.getTag();

        AppFieldWithContent f = fieldList.get(position);

        String fieldTitle = f.getTitle();
        assertNotNull(fieldTitle);
        holder.fieldTitleView.setText(fieldTitle);

        TextView prompt = holder.promptView;
        assertNotNull(prompt);

        if (f.contentIsFilled())
            prompt.setText(f.getContentStringRepresentation());
        else
            prompt.setText(f.getPromptStringForUi());

        return v;
    }

	/* *********************************
	 * We use the holder pattern
	 * It makes the view faster and avoid finding the component
	 * **********************************/

    private static class FieldHolder {
        public TextView fieldTitleView;
        public TextView promptView;
    }


}