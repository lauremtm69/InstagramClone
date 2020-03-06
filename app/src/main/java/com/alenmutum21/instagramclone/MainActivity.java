package com.alenmutum21.instagramclone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

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

        getSupportActionBar().setTitle("SignUp Page");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.gradbtn));

        initFields();
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    signUpUser();
                }

                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        loadingSignUp.setVisibility(View.VISIBLE);
        if (email.getText().toString().equals("") || username.getText().toString().equals("") || password.getText().toString().equals("")){

            FancyToast.makeText(MainActivity.this,"email, username, password is required ",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
            loadingSignUp.setVisibility(View.GONE);

        }else {

            ParseUser parseUser = new ParseUser();

            parseUser.setEmail(email.getText().toString());
            parseUser.setUsername(username.getText().toString());
            parseUser.setPassword(password.getText().toString());

            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        loadingSignUp.setVisibility(View.GONE);
                        FancyToast.makeText(MainActivity.this, "SignUp succesful for user " + ParseUser.getCurrentUser().getUsername(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();


                    } else {
                        loadingSignUp.setVisibility(View.GONE);
                        FancyToast.makeText(MainActivity.this, "error " + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                    }

                }
            });
        }
    }

    private void initFields() {
        alreadyLink = findViewById(R.id.alreadyLink);
        email = findViewById(R.id.emailSignUp);
        username = findViewById(R.id.usernameSignup);
        password = findViewById(R.id.pwdsignUp);

        signUp = findViewById(R.id.signUp);
        loadingSignUp = findViewById(R.id.loadingSignUp);
    }

    public void rootLayouttapped(View view){

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
