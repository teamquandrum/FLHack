package com.qr.girish.qramazeon;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ProductActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_activty);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String mrp = intent.getStringExtra("mrp");
        String dname = intent.getStringExtra("dname");
        String damt = intent.getStringExtra("discount");


        TextView tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText(name);

        TextView tv2 = (TextView) findViewById(R.id.textView2);

        if(damt.equals("null"))
            tv2.setText("No discounts");
        else
            tv2.setText("MRP: Rs. " + mrp + "\n\n" + dname + "\nDiscounted Price: Rs. " + (Double.parseDouble(mrp)-Double.parseDouble(mrp)*Double.parseDouble(damt)/100));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_activty, menu);
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
}
