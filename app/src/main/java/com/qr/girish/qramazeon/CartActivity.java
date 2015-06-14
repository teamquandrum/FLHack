package com.qr.girish.qramazeon;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CartActivity extends ActionBarActivity {

    String title[];         //name
    String body[];          //mrp
    String askerid[];       //discount
    String qid[];           //barcode
    private ListView lv;
    private String saveName = "";
    ArrayAdapter<String> adapter;

    int gindex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Your Cart");
        //getActionBar().setIcon(R.drawable.my_icon);

        lv = (ListView) findViewById(R.id.list_view);

        String[] gg = {" "};
        adapter = new ArrayAdapter<String>(CartActivity.this, R.layout.list_item, R.id.firstLine, gg);
        lv.setAdapter(adapter);

        // MyAdapter adapter = new MyAdapter(AnswerActivity.this, generateData());

        TextView t = (TextView) findViewById(R.id.t);

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
                gindex = index;
                DialogFragment newFragment = new FireMissilesDialogFragment();
                newFragment.show(getSupportFragmentManager(), "missiles");

                return true;
            }
        });
    }

    public void pay(View view)
    {
        double price = new Cart().getAmount();
        Parcels.wallet -= price;
    }

    public void finalize(View view)
    {
        int time = new Cart().getBillTime();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("controller", "time");
        params.add("action", "addtime");
        params.add("time", time+"");
        client.get("http://qr.gear.host/index.php/manager", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.e("JSON", new String(response));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                e.printStackTrace();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void done(View view)
    {
        int time = new Cart().getBillTime();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("controller", "time");
        params.add("action", "reducetime");
        params.add("time", time+"");
        client.get("http://qr.gear.host/index.php/manager", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.e("JSON", new String(response));
                new Cart().deleteAll();
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                e.printStackTrace();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
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
            case R.id.action_wallet:
                startActivity(new Intent(CartActivity.this, WalletActivity.class));
                return true;
            case R.id.action_time:
                startActivity(new Intent(CartActivity.this, TimeActivity.class));
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

    private ArrayList<CartItem> generateData() {
        ArrayList<CartItem> items = new ArrayList<CartItem>();
        //items.add(new Item("Item 1","First Item on the list"));
        //items.add(new Item("Item 2","Second Item on the list"));
        //items.add(new Item("Item 3","Third Item on the list"));
        //Log.e("cart", new Cart().getCart().get(0).name);
        ArrayList<CartItem> a = new Cart().getCart();

        for (int i = 0; i < a.size()/*names1.length*/; i++) {
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

    public void clear(View view) {
        new Cart().deleteAll();
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    public void saveCart(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveName = input.getText().toString();
                new Cart().saveCart(saveName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    public void loadCart(View view) {
        Intent i = new Intent (this, LoadCart.class);
        startActivity(i);
        finish();
    }

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
