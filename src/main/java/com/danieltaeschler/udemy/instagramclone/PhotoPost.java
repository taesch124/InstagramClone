package com.danieltaeschler.udemy.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Daniel Taeschler on 3/12/2018.
 */

public class PhotoPost {

    private String mUsername;
    private byte[] mImage;
    private String mCaption;

    public PhotoPost(String username, byte[] imageByteArray, String caption) {
        mUsername = username;
        mCaption = caption;
        mImage = imageByteArray;
    }

    public String getUsername() {return mUsername;}
    public byte[] getImage() {return mImage;}
    public String getCaption() {return mCaption;}

}
