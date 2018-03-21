package com.danieltaeschler.udemy.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Daniel Taeschler on 3/12/2018.
 */

public class PhotoPost {

    private String mUsername;
    private Bitmap mImage;
    private String mCaption;

    public PhotoPost(String username, byte[] imageByteArray, String caption) {
        mUsername = username;
        mCaption = caption;
        mImage = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
    }

    public String getUsername() {return mUsername;}
    public Bitmap getImage() {return mImage;}
    public String getCaption() {return mCaption;}
}
