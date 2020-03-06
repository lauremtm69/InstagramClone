package com.alenmutum21.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class SocialMediaActivity extends AppCompatActivity {
    private Button logout;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);


        if (ParseUser.getCurrentUser() == null){
            startActivity(new Intent(SocialMediaActivity.this, MainActivity.class));
            finish();
        }

        initFields();

        String welcomeText = welcome.getText().toString() + " " + ParseUser.getCurrentUser().getUsername();

        welcome.setText(welcomeText);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(SocialMediaActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initFields() {

        logout = findViewById(R.id.logout);
        welcome = findViewById(R.id.welcome);
    }
}
