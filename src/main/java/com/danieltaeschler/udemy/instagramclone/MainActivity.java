package com.danieltaeschler.udemy.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private RelativeLayout mBackgroundRelativeLayout;
    private ImageView mLogoImageView;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mActionButton;
    private TextView mSwitchActionTextView;
    private boolean signUpModeActive = true;

    public void performUserAction(View view) {
        final String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if(username.matches("") || password.matches("")) {
            Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (signUpModeActive) {
            ParseUser newUser = new ParseUser();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("USER", username + " signed up!");
                        Toast.makeText(MainActivity.this,username + " signed up!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(i);
                    } else {
                        Log.w("USER", "Sign up failed - " + e.toString());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {
                        Log.i("USER", username + " logged in!");
                        Toast.makeText(MainActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(i);
                    }  else {
                        Log.w("USER", "Error logging in - " + e.toString());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }});
            }
    }

    public void switchButtonAction(View view) {
        signUpModeActive = !signUpModeActive;
        if(signUpModeActive) {
            mActionButton.setText("Sign Up");
            mSwitchActionTextView.setText("Login");
        } else {
            mActionButton.setText("Login");
            mSwitchActionTextView.setText("Sign Up");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ParseUser.getCurrentUser() != null) {
            Intent i = new Intent(getApplicationContext(), UserListActivity.class);
            startActivity(i);
        }

        mBackgroundRelativeLayout = (RelativeLayout)findViewById(R.id.backgroundRelativeLayout);
        mLogoImageView = (ImageView)findViewById(R.id.logoImageView);
        mUsernameEditText = (EditText)findViewById(R.id.usernameEditText);
        mPasswordEditText = (EditText)findViewById(R.id.passwordEditText);
        mActionButton = (Button)findViewById(R.id.actionButton);
        mSwitchActionTextView = (TextView)findViewById(R.id.switchActionTextView);

        mBackgroundRelativeLayout.setOnClickListener(this);
        mLogoImageView.setOnClickListener(this);
        mPasswordEditText.setOnKeyListener(this);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            performUserAction(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backgroundRelativeLayout || v.getId() == R.id.logoImageView) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}
