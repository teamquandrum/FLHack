package com.qr.girish.qramazeon;



import java.io.Serializable;

/**
 * Created by Aman on 6/13/2015.
 */
public class CartItem implements Serializable {

    public int pid;
    public int quantity;
    public String name;
    public double price;

    public CartItem(int pid, String name, int quantity, double price) {
        this.quantity = quantity;
        this.pid = pid;
        this.name = name;
        this. price = price;
    }
    public CartItem(String name, double price)
    {
        this.name = name;
        this.price = price;
    }

    public String getTitle()
    {
        return name;
    }
    public double getDescription()
    {
        return price;
    }
}
