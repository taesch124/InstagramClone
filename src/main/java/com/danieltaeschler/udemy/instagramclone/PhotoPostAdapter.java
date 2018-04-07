package com.danieltaeschler.udemy.instagramclone;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Daniel Taeschler on 3/12/2018.
 */

public class PhotoPostAdapter extends ArrayAdapter<PhotoPost> {

    public PhotoPostAdapter(@NonNull Context context, ArrayList<PhotoPost> posts) {
        super(context, 0, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        PhotoPost post = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_photo_post, null);
        }

        TextView username = (TextView)convertView.findViewById(R.id.usernameTextView);
        TextView caption = (TextView)convertView.findViewById(R.id.captionTextView);
        ImageView image = (ImageView)convertView.findViewById(R.id.userFeedImageView);

        username.setText(post.getUsername());
        Glide.with(getContext()).load(post.getImage()).into(image);
        //image.setImageBitmap(post.getImage());
        caption.setText(post.getCaption());

        return convertView;
    }
}
