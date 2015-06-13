package com.qr.girish.qramazeon;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class CartActivity extends ActionBarActivity {

    String title[];         //name
    String body[];          //mrp
    String askerid[];       //discount
    String qid[];           //barcode
    private ListView lv;
    ArrayAdapter<String> adapter;

    int gindex=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setTitle("Your Cart");
        //getActionBar().setIcon(R.drawable.my_icon);

        lv = (ListView) findViewById(R.id.list_view);

        String[] gg = {" "};
        adapter = new ArrayAdapter<String>(CartActivity.this, R.layout.list_item, R.id.firstLine, gg );
        lv.setAdapter(adapter);

        // MyAdapter adapter = new MyAdapter(AnswerActivity.this, generateData());

        TextView t = (TextView)findViewById(R.id.t);

        t.setText("");

        System.out.println("sup?");
        NewAdapter adapter = new NewAdapter(CartActivity.this, generateData());

        // 2. Get ListView from activity_main.xml
        ListView listView = (ListView) findViewById(R.id.list_view);

        // 3. setListAdapter
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                // TODO Auto-generated method stub
                gindex=index;
                DialogFragment newFragment = new FireMissilesDialogFragment();
                newFragment.show(getSupportFragmentManager(), "missiles");

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.swag_list_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search:
                //startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            case R.id.action_add:
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                return true;
            case R.id.action_removeall:
                new Cart().deleteAll();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<CartItem> generateData(){
        ArrayList<CartItem> items = new ArrayList<CartItem>();
        //items.add(new Item("Item 1","First Item on the list"));
        //items.add(new Item("Item 2","Second Item on the list"));
        //items.add(new Item("Item 3","Third Item on the list"));
        //Log.e("cart", new Cart().getCart().get(0).name);
        ArrayList<CartItem> a = new Cart().getCart();

        for(int i=0; i<a.size()/*names1.length*/; i++)
        {
            //items.add(new Item(names1[i], dates1[i]));

                items.add(new CartItem(a.get(i).name, a.get(i).price));


        }

        return items;
    }
    //what you see? looks sexy on my phone
    //but what about kutti screens?
    //check out the graphical view on the xml page
    //oh wait
    //you didn't modify the second thingy :P
    //do that also. twhahforgot lo what is second?
    //check out adithya krishna's photo in that xml's graphical
    //done
    //looks really awesome.
    //now what if i want to add a third one?
    //just alignbottom of parent?
    //shove whole thing in scrollview. Just copy paste the linearlayout for each image-text pair. one
    //thats it
    //wait, watch me copy paste
    //running
    //dude it looks beautiful!
    //acknowledge?
    //pls?
    //;_;

    public class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to remove this item from cart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            new Cart().removeFromCart(gindex);
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
