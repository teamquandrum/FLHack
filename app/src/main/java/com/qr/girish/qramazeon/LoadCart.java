package com.qr.girish.qramazeon;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class LoadCart extends ActionBarActivity {

    String title[];         //name
    String body[];
    // List view
    int i=0;
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;

    String products[]=null;

    String shelf;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;


    final static String GET_USER_DETAILS="getuserdetails";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_cart);
        setTitle("Load a cart!");
        String path = Environment.getExternalStorageDirectory().toString() + File.separator + "Saved";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
            File file[] = f.listFiles();
        Log.d("Files", "Size: " + file.length);
            if (file.length > 0) {
                title = new String[file.length];
                body = new String[file.length];
                for (int i = 0; i < file.length; i++) {
                    Log.d("Files", "FileName:" + file[i].getName());
                    title[i] = file[i].getName();
                    body[i] = "Click to load this cart";
                }


                lv = (ListView) findViewById(R.id.load_cart);

                String[] gg = {" "};
                adapter = new ArrayAdapter<String>(LoadCart.this, R.layout.list_item, R.id.firstLine, gg);
                lv.setAdapter(adapter);

                // MyAdapter adapter = new MyAdapter(AnswerActivity.this, generateData());
                TextView t = (TextView) findViewById(R.id.t);
                t.setText("");

                MyAdapter adapter = new MyAdapter(LoadCart.this, generateData());
                ListView listView = (ListView) findViewById(R.id.load_cart);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        new Cart().loadCart(title[position]);
                        finish();
                    }
                });

            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private ArrayList<Item> generateData(){
        ArrayList<Item> items = new ArrayList<Item>();
        //items.add(new Item("Item 1","First Item on the list"));
        //items.add(new Item("Item 2","Second Item on the list"));
        //items.add(new Item("Item 3","Third Item on the list"));

        for(int i=0; i<title.length/*names1.length*/; i++)
        {
            //items.add(new Item(names1[i], dates1[i]));
            Drawable drawable = null;
            if(false)
            {
                Random rand = new Random();
                int r = rand.nextInt(2);
                if(r == 1) {
                    Resources res = getResources();
                    drawable = res.getDrawable(R.drawable.newoffer);
                }
                else
                {
                    Resources res = getResources();
                    drawable = res.getDrawable(R.drawable.ic_launcher);
                }
                items.add(new Item(title[i],"MRP: Rs. " + body[i], drawable));
                System.out.println(title[i] + body[i]);
            }
            else
            {
                Resources res = getResources();
                drawable = res.getDrawable(R.drawable.ic_launcher);
                items.add(new Item(title[i], body[i], drawable));
                System.out.println(title[i] + body[i]);
            }


        }

        return items;
    }
}
