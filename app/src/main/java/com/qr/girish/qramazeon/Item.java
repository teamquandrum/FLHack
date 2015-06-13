package com.qr.girish.qramazeon;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Item {
	 
    private String title;
    private String description;
    private Drawable icon;
 
    public Item(String title, String description, Drawable i) {
        super();
        this.title = title;
        this.description = description;
        this.icon = i;
    }
    // getters and setters...   
    public void setTitle(String t)
    {
    	title = t;
    }
    public void setDescription(String t)
    {
    	description = t;
    }
    public void setImage(Drawable i)
    {
        icon = i;
    }
    public String getTitle()
    {
    	return title;
    }
    public String getDescription()
    {
    	return description;
    }
    public Drawable getImage()
    {
        return icon;
    }
    public String toString()
    {
		return this.title;
    	
    }
}