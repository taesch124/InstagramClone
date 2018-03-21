package com.danieltaeschler.udemy.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), UserFeedActivity.class);
                i.putExtra("username", users.get(position));
                startActivity(i);
            }
        });
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
