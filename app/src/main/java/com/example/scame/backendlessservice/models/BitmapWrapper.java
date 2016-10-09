package com.example.scame.backendlessservice.models;


import android.graphics.Bitmap;

// to avoid android dependencies
public class BitmapWrapper {

    private Bitmap bitmap;

    public BitmapWrapper() { }

    public BitmapWrapper(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
