package com.cy.suotouM.note;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class headchose_top_note {
    private Drawable image;
    private Bitmap bitmap;
    private boolean is_add=false;
    public  String path;
    public headchose_top_note(Drawable image)
    {
        this.image=image;
    }
    public headchose_top_note(String path,Bitmap image)
    {
        this.bitmap=image;
        this.path=path;
    }
    public headchose_top_note(boolean is_add)
    {
        if (is_add)
        {
            this.is_add=true;
        }

    }

    public String getPath() {
        return path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean getisAdd(){
        return is_add;
}
    public Drawable getImage() {
        return image;
    }
}
