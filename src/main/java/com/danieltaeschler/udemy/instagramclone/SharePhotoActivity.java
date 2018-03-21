package com.danieltaeschler.udemy.instagramclone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SharePhotoActivity extends AppCompatActivity {

    private Bitmap mSelectedImage = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPhoto(null);
        }
    }

    public void getPhoto(View view) {

        if(Build.VERSION.SDK_INT > 22 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            startPhotoIntent();
        }
    }

    private void startPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void sharePhoto(View view) {
        final LinearLayout progressLinearLayout = (LinearLayout)findViewById(R.id.linlaHeaderProgress);

        TextView caption = (TextView)findViewById(R.id.captionEditText);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(mSelectedImage != null) {
            progressLinearLayout.setVisibility(View.VISIBLE);
            mSelectedImage.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] image = stream.toByteArray();

            ParseFile imageFile = new ParseFile("test.jpg", image);

            ParseObject photoPost = new ParseObject("PhotoPost");
            photoPost.put("username", ParseUser.getCurrentUser().getUsername());
            photoPost.put("image", imageFile);
            photoPost.put("caption", caption.getText().toString());
            photoPost.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Toast.makeText(SharePhotoActivity.this, "Image shared", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SharePhotoActivity.this, "Upload failed, try again later", Toast.LENGTH_SHORT).show();
                        progressLinearLayout.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            Toast.makeText(this, "Please add an image.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                mSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView imageView = (ImageView)findViewById(R.id.userPhotoImageView);
                imageView.setImageBitmap(mSelectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
