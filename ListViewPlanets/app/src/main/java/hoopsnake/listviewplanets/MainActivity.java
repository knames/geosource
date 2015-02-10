package hoopsnake.listviewplanets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    // The data to show
    List<Field> fieldList = new ArrayList<Field>();
    CustomAdapter aAdpt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList();

        // We get the ListView component from the layout
        ListView lv = (ListView) findViewById(R.id.listView);


        // This is a simple adapter that accepts as parameter
        // Context
        // Data list
        // The row layout that is used during the row creation
        // The keys used to retrieve the data
        // The View id used to show the data. The key number and the view id must match
        //aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fieledList);
        aAdpt = new CustomAdapter(fieldList, this);
        lv.setAdapter(aAdpt);

        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {

            }
        });

        // we register for the contextmneu
        registerForContextMenu(lv);
    }


    // We want to create a context Menu when the user long click on an item
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        Field field = aAdpt.getItem(aInfo.position);

        if (field.getTitle().equals("Picture"))
        {
            Intent intent = new Intent(MainActivity.this, CameraPage.class);
            startActivity(intent);
        }
        else if (field.getTitle().equals("Audio"))
        {
            Intent intent = new Intent(MainActivity.this, AudioActivity.class);
            startActivity(intent);
        }
        else if (field.getTitle().equals("Description"))
        {
            Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
            startActivity(intent);
        }

        else
        {
            menu.setHeaderTitle("Options for " + field.getTitle());
            menu.add(1, 1, 1, "Details");
            menu.add(1, 2, 2, "Delete");

        }
    }


    // This method is called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        fieldList.remove(aInfo.position);
        aAdpt.notifyDataSetChanged();
        return true;
    }


    private void initList() {
        // We populate the planets

        fieldList.add(new Field("Title", "Title of the Post"));


    }


    // Handle user click
    public void addField(View view) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.setTitle("Add a Field");
        d.setCancelable(true);

        final EditText edit = (EditText) d.findViewById(R.id.editTextPlanet);
        Button b = (Button) d.findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String fieldTitle = edit.getText().toString();

                //Sorry about the nested for loops, but my Android Studio was having issues with  recognizing the OR symbol, "||"
                if (!properField(fieldTitle))
                {
                    Toast.makeText(MainActivity.this,
                                        "Sorry, that's not a valid field. Please try again.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MainActivity.this.fieldList.add(new Field(fieldTitle));
                    MainActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed
                    d.dismiss();
                }
            }
        });

        d.show();
    }

    boolean properField(String fieldTitle)
    {

        String picture = "Picture";
        String audio = "Audio";
        String description = "Description";
        String title = "Title";

        //Sorry about the nested for loops, but my Android Studio was having issues with  recognizing the OR symbol, "||"
        if ((!fieldTitle.equals(picture)))
        {
            if ((!fieldTitle.equals(audio)))
            {
                if ((!fieldTitle.equals(description)))
                {
                    if ((!fieldTitle.equals(title)))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
        else
        {
            return true;
        }

    }


}