package com.alenmutum21.instagramclone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    private TextView alreadyLink;
    private EditText email,password,username;
    private Button signUp;
    private ProgressBar loadingSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        getSupportActionBar().setTitle("Sign up Screen");

        initFields();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingSignUp.setVisibility(View.VISIBLE);
                signUpUser();
            }
        });

        alreadyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });


    }

    private void signUpUser() {
        ParseUser parseUser = new ParseUser();

        parseUser.setEmail(email.getText().toString());
        parseUser.setUsername(username.getText().toString());
        parseUser.setPassword(password.getText().toString());

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    loadingSignUp.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();

                }else {
                    loadingSignUp.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initFields() {
        alreadyLink = findViewById(R.id.alreadyLink);
        email = findViewById(R.id.emailSignUp);
        username = findViewById(R.id.usernameSignup);
        password = findViewById(R.id.pwdsignUp);

        signUp = findViewById(R.id.signUp);
        loadingSignUp = findViewById(R.id.loadingSignUp);
    }
}
