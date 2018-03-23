package com.danieltaeschler.udemy.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private ListView mUserListView;
    private ArrayAdapter mArrayAdapter;

    private void ViewFeed() {

        ArrayList<String> users = new ArrayList<>();
        for (int j = mArrayAdapter.getCount() - 1; j >= 0; j--) {
            if (mUserListView.isItemChecked(j)) {
                Log.d("USER",mArrayAdapter.getItem(j).toString());
                users.add(mArrayAdapter.getItem(j).toString());
            }
        }

        Intent intent = new Intent(getApplicationContext(), UserFeedActivity.class);
        intent.putExtra("usernames", users);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle(ParseUser.getCurrentUser().getUsername());
        final ArrayList<String> users = new ArrayList<>();
        mUserListView = (ListView)findViewById(R.id.userListView);
        mArrayAdapter = new ArrayAdapter(this, R.layout.list_item_user, users);

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        userParseQuery.orderByAscending("username");
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null || objects.size() > 0) {
                    for(ParseUser user : objects) {
                        users.add(user.getUsername());
                    }
                    mUserListView.setAdapter(mArrayAdapter);
                } else {
                    Log.w("USER_LIST", "No users found from query!");
                }
            }
        });

        final AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.user_feed_context, menu);
                mode.setTitle("Selected users");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_view_feed:
                        //Start activity with multiple users
                        ViewFeed();
                        mode.finish();
                        return true;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        };

        mUserListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mUserListView.setMultiChoiceModeListener(multiChoiceModeListener);
        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUserListView.setItemChecked(position, true);
                Log.d("VIEW", view.toString());
                view.setActivated(true);
                startActionMode(multiChoiceModeListener);
            }
        });

        //Code to select one user feed at a time
        /*
        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), UserFeedActivity.class);
                i.putExtra("username", users.get(position));
                startActivity(i);
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.sharePhotoMenu:
                Intent intent = new Intent(this, SharePhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.logoutMenuItem:
                ParseUser.logOut();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            default:
                return false;
        }
        return true;
    }
}
