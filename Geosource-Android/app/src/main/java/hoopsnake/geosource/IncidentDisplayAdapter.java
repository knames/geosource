package hoopsnake.geosource;

/**
 * Created by Anam on 2015-02-09. Modified by William to fit with the app, and limit the use-cases:
 * only allows filling out an existing incident, not creating a new one.
 *
 * This adapts the array of fields that are to be displayed while a user is filling out the
 * form for an incident. It calls each field individually to ask how it should be displayed.
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

class IncidentDisplayAdapter extends ArrayAdapter<AppFieldWithContent> {

    private String logTag = MainActivity.APP_LOG_TAG;
    private final ArrayList<AppFieldWithContent> fieldList;
    private final Context context;

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

    /**
     * Return the view for the field at the current position in the ListView being adapted.
     * This is called automatically, whenever the underlying ListView needs a given view.
     * As such it should probably not be called manually, unless you are very sure of yourself.
     * @param position the position of the current field in the list.
     * @param convertView
     * @param parent the ListView that this is adapting.
     * @return the view for the current field.
     */
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
            prompt.setText("nothing here");

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