package com.alenmutum21.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private tabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media Page");

        if (ParseUser.getCurrentUser() == null){
            startActivity(new Intent(SocialMediaActivity.this, MainActivity.class));
            finish();
        }

        initFields();



    }

    private void initFields() {
        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        viewPager = findViewById(R.id.viewPager);
        adapter = new tabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.logou:
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(SocialMediaActivity.this,MainActivity.class));
                finish();
                break;

            case R.id.quit:
                finish();
                break;
        }

        return true;
    }
}
