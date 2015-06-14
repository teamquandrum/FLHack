package com.qr.girish.qramazeon;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter2 extends ArrayAdapter<Item> {

        private final Context context;
        private final ArrayList<Item> itemsArrayList;

        public MyAdapter2(Context context, ArrayList<Item> itemsArrayList) {
 
            super(context, R.layout.list_item, itemsArrayList);
 
            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.list_item, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.firstLine);
            TextView valueView = (TextView) rowView.findViewById(R.id.secondLine);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);


            valueView.setText(itemsArrayList.get(position).getDescription());
//            //tvMRP.setText(text, TextView.BufferType.SPANNABLE);
//            Spannable spannable = (Spannable) valueView.getText();
//            if(itemsArrayList.get(position).getDescription().indexOf(',')>=0)
//                spannable.setSpan(new StrikethroughSpan(), 9, itemsArrayList.get(position).getDescription().indexOf(','), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            //spannable.setSpan(new StrikethroughSpan(), 4, itemsArrayList.get(position).getDescription().indexOf(',')-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 4. Set the text for textView 
            labelView.setText(itemsArrayList.get(position).getTitle());
            //valueView.setText(itemsArrayList.get(position).getDescription());
            imageView.setImageDrawable(itemsArrayList.get(position).getImage());
 
            // 5. return rowView
            return rowView;
        }
}