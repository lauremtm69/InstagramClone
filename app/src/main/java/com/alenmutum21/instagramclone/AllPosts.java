package com.alenmutum21.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AllPosts extends Fragment {
    private String recievedUsername;
    private ProgressBar loadingAll;
    private LinearLayout allTimeline;

    public AllPosts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_posts, container, false);

        recievedUsername = ParseUser.getCurrentUser().getUsername();
        loadingAll = view.findViewById(R.id.loadingAll);
        allTimeline = view.findViewById(R.id.allLinearlayout);



        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.orderByDescending("createdAt");

        loadingAll.setVisibility(View.VISIBLE);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.size() > 0 && e == null){

                    for (final ParseObject post : objects){

                        final TextView postDesc = new TextView(getContext());
                        if (post.get("image_desc") == null){
                            postDesc.setText("");
                        }else {
                            postDesc.setText(post.get("image_desc") + "");
                        }
                        final TextView username = new TextView(getContext());
                        username.setText(post.get("username").toString() + ":");


                        ParseFile postImg = (ParseFile) post.get("picture");
                        postImg.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null){

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(getContext());
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500);
                                    params .setMargins(5,0,5,0);
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

                                    LinearLayout.LayoutParams paramsUser = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    paramsUser.setMargins(5,10,5,0);
                                    username.setLayoutParams(paramsUser);
                                    username.setBackgroundResource(R.drawable.gradinputs);
                                    username.setTextColor(Color.GRAY);
                                    username.setTextSize(20f);

                                    allTimeline.addView(username);
                                    allTimeline.addView(postImageView);
                                    allTimeline.addView(postDesc);

                                }
                                loadingAll.setVisibility(View.GONE);
                            }
                        });
                    }

                }else {
                    FancyToast.makeText(getContext(), "you has no posts yet", Toast.LENGTH_LONG,FancyToast.INFO,false).show();

                }
            }
        });


        return view;
    }
}
