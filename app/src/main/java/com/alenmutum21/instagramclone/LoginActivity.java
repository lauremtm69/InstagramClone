package com.alenmutum21.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginprogress.setVisibility(View.VISIBLE);
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
        ParseUser.logInInBackground(emailLogin.getText().toString(), pwdLogin.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null){
                    Toast.makeText(LoginActivity.this, "login Succesfull", Toast.LENGTH_SHORT).show();
                    loginprogress.setVisibility(View.GONE);
                }else {
                    Toast.makeText(LoginActivity.this, "error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    loginprogress.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initfield() {
        getSupportActionBar().setTitle("Login page");
        noAccount = findViewById(R.id.noAccount);
        login = findViewById(R.id.login);
        emailLogin = findViewById(R.id.emailLogin);
        pwdLogin = findViewById(R.id.pwdLogin);
        loginprogress = findViewById(R.id.loginProgress);

    }
}
