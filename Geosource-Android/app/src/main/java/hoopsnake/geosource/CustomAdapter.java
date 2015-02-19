package hoopsnake.geosource;

/**
 * Created by Anam on 2015-02-09.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hoopsnake.geosource.data.FullField;

import static junit.framework.Assert.assertNotNull;

public class CustomAdapter extends ArrayAdapter<FullField> {

    private List<FullField> fieldList;
    private Context context;



    public CustomAdapter(List<FullField> fieldList, Context ctx) {
        super(ctx, R.layout.img_row_layout, fieldList);
        this.fieldList = fieldList;
        this.context = ctx;
    }

    public int getCount() {
        return fieldList.size();
    }

    public FullField getItem(int position) {
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

        FullField f = fieldList.get(position);

        String fieldTitle = f.getTitle();
        assertNotNull(fieldTitle);
        holder.fieldTitleView.setText(fieldTitle);

        TextView prompt = holder.promptView;
        assertNotNull(prompt);

        if (f.getContent() == null) {
            switch (f.getType())
            {
                case IMAGE:
                    prompt.setText("Click to take a picture.");
                    break;
                case STRING:
                    prompt.setText("Click to fill in text.");
                    break;
                case VIDEO:
                    prompt.setText("Click to take a video.");
                    break;
                case SOUND:
                    prompt.setText("Click to record audio.");
                    break;
            }
        }
        else
        {
            switch (f.getType())
            {
                case IMAGE:
                    prompt.setText("There is a picture here, trust me.");
                    break;
                case STRING:
                    prompt.setText((String) f.getContent());
                    break;
                case VIDEO:
                    prompt.setText("There is a video here, trust me.");
                    break;
                case SOUND:
                    prompt.setText("There is an audio recording here, trust me.");
                    break;
            }
        }


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