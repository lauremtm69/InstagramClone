package com.alenmutum21.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private tabAdapter adapter;
    private ProgressBar loadingRotat;

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
        loadingRotat = (ProgressBar) findViewById(R.id.loadingRotat);


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

            case R.id.postImageItem:

                if (Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(SocialMediaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 3000);
                }else {
                    captureImage();
                }
                break;
        }

        return true;
    }

    private void captureImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,4000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 3000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4000 && resultCode == RESULT_OK && data != null){
            loadingRotat.setVisibility(View.VISIBLE);
            try {
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();


                ParseFile parseFile = new ParseFile("img.png",bytes);
                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture",parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(SocialMediaActivity.this,"Image updated succesfully",Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                            loadingRotat.setVisibility(View.GONE);
                        }else {
                            FancyToast.makeText(SocialMediaActivity.this,"error "+ e.getMessage(),Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                            loadingRotat.setVisibility(View.GONE);
                        }
                    }
                });


            }catch (Exception e){
                Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
