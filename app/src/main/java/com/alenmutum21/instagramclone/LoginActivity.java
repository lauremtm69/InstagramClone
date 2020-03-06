package com.alenmutum21.instagramclone;

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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {
    private TextView noAccount;
    private Button login;
    private EditText emailLogin,pwdLogin;
    private ProgressBar loginprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initfield();

        pwdLogin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    loginUser();
                }

                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });



        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void loginUser() {
        loginprogress.setVisibility(View.VISIBLE);
        if (emailLogin.getText().toString().equals("") || pwdLogin.getText().toString().equals("")){

            FancyToast.makeText(LoginActivity.this,"email and password field cannot be empty",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
            loginprogress.setVisibility(View.GONE);

        }else {

            ParseUser.logInInBackground(emailLogin.getText().toString(), pwdLogin.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && e == null) {
                        FancyToast.makeText(LoginActivity.this, "login succesful for user " + ParseUser.getCurrentUser().getUsername(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        sendUserToSocialMediaActivity();
                        finish();
                        loginprogress.setVisibility(View.GONE);
                    } else {
                        FancyToast.makeText(LoginActivity.this, "error " + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        loginprogress.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void initfield() {
        getSupportActionBar().setTitle("Login page");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.gradbtn));
        noAccount = findViewById(R.id.noAccount);
        login = findViewById(R.id.login);
        emailLogin = findViewById(R.id.emailLogin);
        pwdLogin = findViewById(R.id.pwdLogin);
        loginprogress = findViewById(R.id.loginProgress);

    }
    public void rootTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void sendUserToSocialMediaActivity(){
        Intent SocialMediaActivityIntent = new Intent(LoginActivity.this,SocialMediaActivity.class);
        startActivity(SocialMediaActivityIntent);
    }
}
