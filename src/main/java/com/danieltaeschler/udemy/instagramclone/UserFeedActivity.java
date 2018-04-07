package com.danieltaeschler.udemy.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
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
    private final int imagesToLoad = 5;
    private int imagesLoaded = 0;

    private ListView mUserFeedListView;
    private ArrayList<String> usernames;
    private ArrayList<PhotoPost> mUserPosts;
    private PhotoPostAdapter mPhotoPostAdapter;

    private void loadImages() {
        Log.d("LOADED", Integer.toString(imagesLoaded));
        ParseQuery<ParseObject> userFeedQuery = ParseQuery.getQuery("PhotoPost");
        userFeedQuery.whereContainedIn("username", usernames);
        userFeedQuery.orderByDescending("createdAt");
        userFeedQuery.setSkip(imagesLoaded);
        userFeedQuery.setLimit(imagesToLoad);
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
        imagesLoaded += imagesToLoad;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        mUserFeedListView = (ListView)findViewById(R.id.userFeedListView);
        mUserPosts = new ArrayList<>();
        mPhotoPostAdapter = new PhotoPostAdapter(this, mUserPosts);
        mUserFeedListView.setAdapter(mPhotoPostAdapter);

        usernames = (ArrayList<String>) getIntent().getSerializableExtra("usernames");
        loadImages();

        mUserFeedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean scrolling;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE) {
                    scrolling = false;
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int position = firstVisibleItem+visibleItemCount;
                int limit = totalItemCount;

                // Check if bottom has been reached
                if (position >= limit && totalItemCount > 0 && scrolling) {
                    if (this != null ) {
                        scrolling = false;
                        loadImages();
                    }
                }
            }
        });
    }


}
