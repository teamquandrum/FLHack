package com.qr.girish.qramazeon;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import me.dm7.barcodescanner.zbar.Result;


public class MainActivity extends ActionBarActivity implements SensorEventListener, MyScanner.ResultHandler {

    FrameLayout rootLayout;
    Timer timer;
    int degree = 0;
    private SensorManager mSensorManager;
    static Context context;
    String qr = "A";
    String toqr = "";
    boolean nav = false;

    private MyScanner mScannerView;
    String TAG = "fgt";
    private AutoCompleteTextView actv;
    String barcode = null;
    String itemArray[];

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mScannerView = new MyScanner(this);
        setContentView(mScannerView);                // Set the scanner view as the content
        setTitle("Scan Code");
        rootLayout = (FrameLayout) findViewById(android.R.id.content);
        context = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);


}

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        barcode = rawResult.getContents();
        Log.v(TAG, rawResult.getContents()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
        //startActivity(new Intent(MainActivity.this, SwagListActivity.class));
        if (rawResult.getBarcodeFormat().getName().equals("QRCODE")) {
            try {
                int i = Integer.parseInt(rawResult.getContents());

                Intent in = new Intent(MainActivity.this, SwagListActivity.class);
                in.putExtra("shelf", "" + i);
                startActivity(in);
            } catch (NumberFormatException e) {
                //This is a ground QR Code
                qr = rawResult.getContents().toString();
                Log.e("Last known orientation",String.valueOf(degree));
                rootLayout.removeView(findViewById(R.id.qr_ground));
                
                View.inflate(this, R.layout.ground_qr, rootLayout);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        closePopup();
                    }
                }, 5000);
            }
        } else {
            DialogFragment newFragment = new FireMissilesDialogFragment();
            newFragment.show(getSupportFragmentManager(), "Confirmation");
        }
        // finish();
        //mScannerView.stopCamera();           // Stop camera on pause
        // mScannerView = new ZBarScannerView(this);

        // setContentView(mScannerView);

        // mScannerView.setResultHandler(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int degree2 = Math.round(event.values[0]);
        if(degree2!=degree) {
            degree = degree2;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Cry
    }

    public void callEmployee(View view) {
        Toast.makeText(this,"An employee will help you at "+qr+" soon.",Toast.LENGTH_SHORT).show();
    }

    public void startNavigation(View view) {

        nav = true;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("controller","floor");
        params.add("action","getpathto");
        params.add("start",qr.toLowerCase().charAt(0)-'a'+"");
        params.add("keyword",actv.getText().toString());
        client.get("http://qr.gear.host/index.php/manager",params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String s = new String(response);
                    JSONObject obj = new JSONObject(s);
                    JSONObject result = obj.getJSONObject("result");
                    for (Iterator<String> i = result.keys(); i.hasNext(); ) {
                        int p = Integer.parseInt(i.next());
                        int dir = result.getInt(p + "");
                        String direction = "";
                        switch (dir) {
                            case 1:
                                direction = "FORWARD";
                                break;
                            case 2:
                                direction = "RIGHT";
                                break;
                            case 3:
                                direction = "BACKWARD";
                                break;
                            case 4:
                                direction = "LEFT";
                                break;
                        }
                        if (p == qr.toLowerCase().charAt(0) - 'a') Toast.makeText(MainActivity.this, direction, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

public class FireMissilesDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to add this item to cart?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!


                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.add("controller", "item");
                        params.add("action", "getitem");
                        params.add("barcode", barcode);
                        client.get("http://qr.gear.host/index.php/manager", params, new AsyncHttpResponseHandler() {

                            @Override
                            public void onStart() {
                                // called before request is started
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                // called when response HTTP status is "200 OK"
                                Log.e("JSON", new String(response));
                                try {
                                    JSONObject json = new JSONObject(new String(response));
                                    JSONObject result = json.getJSONObject("result");

                                    double price;
                                    double mrp = Double.parseDouble(result.getString("mrp"));
                                    System.out.println(result.getString("damt"));
                                    if (result.getString("damt").equals("null"))
                                        price = mrp;
                                    else {
                                        double discount = Double.parseDouble(result.getString("damt"));
                                        price = mrp - mrp * discount / 100;
                                    }
                                    new Cart().addToCart(Integer.parseInt(result.getString("itemid")), result.getString("name"), 1, price);
                                    System.out.println(new Cart().getCart().get(0).name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                        //new Cart().addToCart()
                        //http://qr.gear.host/index.php/manager
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
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            case R.id.action_time:
                startActivity(new Intent(MainActivity.this, TimeActivity.class));
                return true;
            case R.id.action_add:
                //startActivity(new Intent(MainActivity.this, MainActivity.class));
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void closePopup(View view) {
        timer.cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootLayout.removeView(findViewById(R.id.qr_ground));


            }
        });
    }

    public void closePopup() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootLayout.removeView(findViewById(R.id.qr_ground));


            }
        });

    }

    public void navigate(View view) {
        timer.cancel();
        closePopup();
        View.inflate(this, R.layout.navigate_layout, rootLayout);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("controller","floor");
        params.add("action","getitems");
        client.get("http://qr.gear.host/index.php/manager",params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                JSONObject result = null;
                try {
                    result = new JSONObject(new String(response));
                    Log.e("result",new String(response));
                JSONArray items = result.getJSONArray("result");
                itemArray = new String[items.length()];
                    Log.e("result",items.length()+"");
                for(int i=0; i<items.length();++i)
                    itemArray[i] = items.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,itemArray);
                actv.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void closeNavigatePopup(View view) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootLayout.removeView(findViewById(R.id.nav_lay));


            }
        });
    }


}
