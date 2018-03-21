package com.danieltaeschler.udemy.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    private ListView mUserFeedListView;
    private ArrayList<PhotoPost> mUserPosts;
    private PhotoPostAdapter mPhotoPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        mUserFeedListView = (ListView)findViewById(R.id.userFeedListView);
        mUserPosts = new ArrayList<>();
        mPhotoPostAdapter = new PhotoPostAdapter(this, mUserPosts);
        mUserFeedListView.setAdapter(mPhotoPostAdapter);

        String username = getIntent().getStringExtra("username");
        ParseQuery<ParseObject> userFeedQuery = ParseQuery.getQuery("PhotoPost");
        userFeedQuery.whereEqualTo("username", username);
        userFeedQuery.orderByDescending("createdAt");
        userFeedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() > 0) {
                        for (final ParseObject photoPost : objects) {

                            photoPost.getParseFile("image").getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        mUserPosts.add(new PhotoPost(
                                                photoPost.getString("username"),
                                                data,
                                                photoPost.getString("caption")
                                        ));
                                        mPhotoPostAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(UserFeedActivity.this, "Could not find photo.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                } else {
                    Log.i("USER_FEED", e.toString());
                }
            }
        });

    }
}
