package com.qr.girish.qramazeon;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class MainActivity extends ActionBarActivity implements MyScanner.ResultHandler {

    private MyScanner mScannerView;
    String TAG = "fgt";

    String barcode = null;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new MyScanner(this);
        setContentView(mScannerView);                // Set the scanner view as the content
        setTitle("Scan Code");
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        barcode = rawResult.getContents();
        Log.v(TAG, rawResult.getContents()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
        //startActivity(new Intent(MainActivity.this, SwagListActivity.class));
        if(rawResult.getBarcodeFormat().getName().equals("QRCODE"))
        {
            try
            {
                int i = Integer.parseInt(rawResult.getContents());

                Intent in = new Intent(MainActivity.this, SwagListActivity.class);
                in.putExtra("shelf", "" + i);
                startActivity(in);
            }
            catch(NumberFormatException e)
            {

            }
        }
        else
        {
            DialogFragment newFragment = new FireMissilesDialogFragment();
            newFragment.show(getSupportFragmentManager(), "Confirmation");
        }
        // finish();
        //mScannerView.stopCamera();           // Stop camera on pause
        // mScannerView = new ZBarScannerView(this);

        // setContentView(mScannerView);

       // mScannerView.setResultHandler(this);
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
            case R.id.action_add:
                //startActivity(new Intent(MainActivity.this, MainActivity.class));
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
