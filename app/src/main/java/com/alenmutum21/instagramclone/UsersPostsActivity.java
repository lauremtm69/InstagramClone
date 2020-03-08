package com.alenmutum21.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPostsActivity extends AppCompatActivity {

    private ProgressBar loadingPosts;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        loadingPosts = findViewById(R.id.loadingPosts);
        linearLayout = findViewById(R.id.linearLayout);

        final String recievedUsername = getIntent().getExtras().get("username").toString();


        setTitle(recievedUsername + "'s Posts");

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username",recievedUsername);
        parseQuery.orderByDescending("createdAt");

        loadingPosts.setVisibility(View.VISIBLE);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.size() > 0 && e == null){

                    for (final ParseObject post : objects){

                        final TextView postDesc = new TextView(UsersPostsActivity.this);
                        if (post.get("image_desc") == null){
                            postDesc.setVisibility(View.GONE);
                        }else {
                            postDesc.setText(post.get("image_desc") + "");
                        }

                        ParseFile postImg = (ParseFile) post.get("picture");
                        postImg.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null){

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(UsersPostsActivity.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500);
                                    params .setMargins(5,5,5,5);
                                    postImageView.setLayoutParams(params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    paramsText.setMargins(5,5,5,5);
                                    postDesc.setLayoutParams(paramsText);
                                    postDesc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    postDesc.setBackgroundResource(R.drawable.gradsocial);
                                    postDesc.setTextColor(Color.WHITE);
                                    postDesc.setTextSize(28f);

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDesc);

                                }
                                loadingPosts.setVisibility(View.GONE);
                            }
                        });
                    }

                }else {
                    FancyToast.makeText(UsersPostsActivity.this,recievedUsername + " has no posts yet", Toast.LENGTH_LONG,FancyToast.INFO,false).show();
                    finish();
                }
            }
        });
    }
}
