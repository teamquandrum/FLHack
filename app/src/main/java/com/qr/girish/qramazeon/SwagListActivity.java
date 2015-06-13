package com.qr.girish.qramazeon;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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


public class SwagListActivity extends ActionBarActivity {

    String title[];         //name
    String body[];          //mrp
    String askerid[];       //discount
    String qid[];           //barcode
    // List view
    String quizName="";
    String quizDate="";
    String objectId;
    int i=0;
    private ListView lv;
    private ProgressDialog pDialog;

    String names1[], dates1[];

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //setTitle("QQC");
        //getActionBar().setIcon(R.drawable.my_icon);
        Intent intent = getIntent();
        shelf = intent.getStringExtra("shelf");
        lv = (ListView) findViewById(R.id.list_view);

        String[] gg = {" "};
        adapter = new ArrayAdapter<String>(SwagListActivity.this, R.layout.list_item, R.id.firstLine, gg );
        lv.setAdapter(adapter);

        // MyAdapter adapter = new MyAdapter(AnswerActivity.this, generateData());

        TextView t = (TextView)findViewById(R.id.t);

        t.setText("");

        ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();

        param.add(new BasicNameValuePair("controller", "shelf"));
        param.add(new BasicNameValuePair("action", "getitems"));
        param.add(new BasicNameValuePair("shelfid", "1"));

        //param.add(new BasicNameValuePair("iemi", imei));
        //param.add(new BasicNameValuePair("name", "Girish"));
        //param.add(new BasicNameValuePair("model", "sup"));
        //param.add(new BasicNameValuePair("gcmregid", "sdg435c"));


        HttpPost hp = new HttpPost("http://qr.gear.host/index.php/manager");
        try {
            hp.setEntity(new UrlEncodedFormEntity(param));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PublishingAsyncTask pb = new PublishingAsyncTask(GET_USER_DETAILS, new DefaultHttpClient(), hp, new PublishingAsyncTask.CoolRunnable() {
            @Override
            public void run(String content) {
                Log.e("JSON", content);
                try {
                    JSONObject json = new JSONObject(content);
                    //JSONObject result = json.getJSONObject("result");
                    JSONArray questions = json.getJSONArray("result");

                    title = new String[questions.length()];
                    body = new String[questions.length()];

                    askerid = new String[questions.length()];
                    qid = new String[questions.length()];

                    for(int i=0; i<questions.length();i++)
                    {
                        JSONObject q = questions.getJSONObject(i);
                        title[i]=q.getString("name");
                        body[i]=q.getString("mrp");
                        askerid[i]=q.getString("damt");
                        qid[i]=q.getString("barcode");
                        System.out.println(title[i]);
                    }
                    System.out.println("sup?");
                    MyAdapter adapter = new MyAdapter(SwagListActivity.this, generateData());

                    // 2. Get ListView from activity_main.xml
                    ListView listView = (ListView) findViewById(R.id.list_view);

                    // 3. setListAdapter
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            //Toast.makeText(getApplicationContext(),
                            //  "Click ListItem Number " + position, Toast.LENGTH_LONG)
                            //  .show();
                            //--------------------onclick
          				/*
          				Intent i = new Intent(ListActivity.this, QuizActivity.class);
          				String keyIdentifer  = null;
          				i.putExtra("NAME", "faget");
          				startActivity(i);
          				*/
                            Intent i = new Intent(SwagListActivity.this, ProductActivity.class);
                            i.putExtra("name", title[position]);
                            i.putExtra("mrp", body[position]);
                            i.putExtra("discount", qid[position]);
                            i.putExtra("barcode", askerid[position]); //switch
                            startActivity(i);
                        }
                    });

                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void run() {

            }
        }, null);
        pb.execute();
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
                startActivity(new Intent(SwagListActivity.this, CartActivity.class));
                return true;
            case R.id.action_add:
                startActivity(new Intent(SwagListActivity.this, MainActivity.class));
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class InflateItems extends AsyncTask<String, String, String>{



        @Override
        protected void onPostExecute(String result) {
            //System.out.println(result);
            //String[] app = result.split(":");
            //System.out.println(app[1]);
            String[] app = {result , "adf ", "sdf"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SwagListActivity.this, R.layout.list_item, R.id.firstLine, app);
            lv.setAdapter(adapter);
            //adapter = new ArrayAdapter<String>(ListActivity.this, R.layout.list_item, R.id.firstLine, app);
            //lv.setAdapter(adapter);

            //adapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
            //adapter.clear();
            //adapter.addAll(app);
        }

        @Override
        protected void onPreExecute() {
            System.out.println("Chill Madi");
        }

        @Override
        protected String doInBackground(String... params) {


            return "";
        }

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
            if(askerid[i].equals("null"))
            {
                Resources res = getResources();
                drawable = res.getDrawable(R.drawable.newoffer);
                items.add(new Item(title[i],"MRP: Rs. " + body[i], drawable));
                System.out.println(title[i] + body[i]);
            }
            else
            {
                Resources res = getResources();
                drawable = res.getDrawable(R.drawable.offer);
                Log.e("mrp", Integer.parseInt(body[i])+"");
                Log.e("damt", Integer.parseInt(askerid[i])+"");

                double price = (Integer.parseInt(body[i]) - Integer.parseInt(askerid[i])*Integer.parseInt(body[i])/100);
                Log.e("fgt", "MRP: Rs. " + body[i] + ", Discount: Rs. " + price + "");
                items.add(new Item(title[i], "MRP: Rs. " + body[i] + ",   Discounted: Rs. " + price + "", drawable));
                System.out.println(title[i] + body[i]);
            }


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
}
