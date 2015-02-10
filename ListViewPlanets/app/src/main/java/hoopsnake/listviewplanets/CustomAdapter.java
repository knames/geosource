package hoopsnake.listviewplanets;

/**
 * Created by Anam on 2015-02-09.
 */

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Field> {

    private List<Field> fieldList;
    private Context context;



    public CustomAdapter(List<Field> fieldList, Context ctx) {
        super(ctx, R.layout.img_row_layout, fieldList);
        this.fieldList = fieldList;
        this.context = ctx;
    }

    public int getCount() {
        return fieldList.size();
    }

    public Field getItem(int position) {
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

        Field f = fieldList.get(position);
        holder.fieldTitleView.setText(f.getTitle());
        if (f.getDescription() == null) {
            holder.promptView.setText("Click to fill in");
        }
        else
        {
            holder.promptView.setText(f.getDescription());
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