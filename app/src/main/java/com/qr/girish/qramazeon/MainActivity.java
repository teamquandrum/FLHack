package com.qr.girish.qramazeon;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class MainActivity extends ActionBarActivity implements MyScanner.ResultHandler {

    private MyScanner mScannerView;
    String TAG = "fgt";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new MyScanner(this);
        setContentView(mScannerView);                // Set the scanner view as the content

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
        Log.v(TAG, rawResult.getContents()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
        /*if(rawResult.getBarcodeFormat().getName().equals("QRCODE"))
        {
            try
            {
                int i = Integer.parseInt(rawResult.getContents());
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
            catch(NumberFormatException e)
            {
            }
        }*/
        // finish();
        //mScannerView.stopCamera();           // Stop camera on pause
        // mScannerView = new ZBarScannerView(this);

        // setContentView(mScannerView);

       // mScannerView.setResultHandler(this);
    }
}
