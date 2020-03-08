package com.alenmutum21.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class TImelineActivity extends AppCompatActivity {
    private String recievedUsername;
    private ProgressBar loadingTimeline;
    private LinearLayout linTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_imeline);

        recievedUsername = ParseUser.getCurrentUser().getUsername();
        setTitle(recievedUsername + "'s Posts");
        loadingTimeline = findViewById(R.id.loadingTimeline);
        linTimeline = findViewById(R.id.linTimeline);



        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username",recievedUsername);
        parseQuery.orderByDescending("createdAt");

        loadingTimeline.setVisibility(View.VISIBLE);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.size() > 0 && e == null){

                    for (final ParseObject post : objects){

                        final TextView postDesc = new TextView(TImelineActivity.this);
                        if (post.get("image_desc") == null){
                            postDesc.setText("");
                        }else {
                            postDesc.setText(post.get("image_desc") + "");
                        }

                        ParseFile postImg = (ParseFile) post.get("picture");
                        postImg.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null){

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(TImelineActivity.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500);
                                    params .setMargins(5,10,5,0);
                                    postImageView.setLayoutParams(params);
                                    postImageView.setBackgroundResource(R.drawable.gradinputs);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setPadding(10,10,10,10);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    paramsText.setMargins(5,0,5,50);
                                    postDesc.setLayoutParams(paramsText);
                                    postDesc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    postDesc.setBackgroundResource(R.drawable.gradinputs);
                                    postDesc.setTextColor(Color.BLACK);
                                    postDesc.setTextSize(20f);

                                    linTimeline.addView(postImageView);
                                    linTimeline.addView(postDesc);

                                }
                                loadingTimeline.setVisibility(View.GONE);
                            }
                        });
                    }

                }else {
                    FancyToast.makeText(TImelineActivity.this, "you has no posts yet", Toast.LENGTH_LONG,FancyToast.INFO,false).show();
                    finish();
                }
            }
        });
    }
}
