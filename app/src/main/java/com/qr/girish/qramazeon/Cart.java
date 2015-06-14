package com.qr.girish.qramazeon;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Aman on 6/13/2015.
 */
public class Cart {

    public static Cart cart;

    private ArrayList<CartItem> myCart;

    public Cart() {
        cart = this;
        myCart = new ArrayList<CartItem>();
        File oldcart = new File(Environment.getExternalStorageDirectory() + File.separator + "oldcart.tmp");
        if (oldcart.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(oldcart);
                ObjectInputStream ois = new ObjectInputStream(fis);
                myCart = (ArrayList<CartItem>) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void addToCart(int pid, String name, int quantity, Double price) {
        myCart.add(new CartItem(pid, name, quantity, price));
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "oldcart.tmp");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myCart);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFromCart(int index) {
        /*int i;
        for(i=0; i<myCart.size();++i) {
            if(myCart.get(i).pid == pid)
                break;
        }*/
        myCart.remove(index);
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "oldcart.tmp");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myCart);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        myCart.clear();
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "oldcart.tmp");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myCart);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CartItem> getCart() {
        return myCart;
    }

    public int getBillTime() {
        return (int) (myCart.size() * 0.5);
    }

    public int getAmount() {
        int sum = 0;
        for(int i=0; i<myCart.size();++i)
            sum+=myCart.get(i).price;
        return sum;
    }

    public void saveCart(String name) {
        try {
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Saved");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (success) {
                Log.e("Directory exists", "Chill");
            } else {
                folder.mkdir();
            }
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Saved" + File.separator + name + ".tmp");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myCart);
            oos.close();
            Log.e("Saved Cart", "saveCart");
        } catch (Exception e) {
            Log.e("Exception saving file", "saveCart");
            e.printStackTrace();
        }
    }

    public void loadCart(String name) {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Saved");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.e("Directory exists", "Chill");
        } else {
            folder.mkdir();
        }
        myCart = new ArrayList<CartItem>();
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Saved" + File.separator + name);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            myCart = (ArrayList<CartItem>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "oldcart.tmp");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myCart);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
