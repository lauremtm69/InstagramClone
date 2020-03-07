package com.alenmutum21.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener{
    private Button btnShare;
    private ImageView imageShare;
    private EditText descShare;
    private Bitmap recievedImage;
    private ProgressBar loading;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imageShare = view.findViewById(R.id.imageShare);
        descShare = view.findViewById(R.id.descShare);
        btnShare = view.findViewById(R.id.btnShare);
        loading = view.findViewById(R.id.loading);


        imageShare.setOnClickListener(SharePictureTab.this);
        btnShare.setOnClickListener(SharePictureTab.this);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageShare:
                if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },1000);
                }else {
                    getChosenImage();
                }
                break;

            case R.id.btnShare:
                loading.setVisibility(View.VISIBLE);
                if (recievedImage != null){
                    if (descShare.getText().toString().equals("")){
                        FancyToast.makeText(getContext(),"specify a description of the image first",Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    }else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        recievedImage.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png",bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture",parseFile);
                        parseObject.put("image_desc",descShare.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    FancyToast.makeText(getContext(),"Image updated succesfully",Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                    loading.setVisibility(View.GONE);
                                }else {
                                    FancyToast.makeText(getContext(),"error "+ e.getMessage(),Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                    loading.setVisibility(View.GONE);
                                }
                            }
                        });

                    }

                }else {
                    FancyToast.makeText(getContext(),"You must select an Image",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }

                break;
        }

    }

    private void getChosenImage() {
        //FancyToast.makeText(getContext(),"Now we can acces the images",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK){
            try {
                Uri imageUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(imageUri,filePathColumn,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                recievedImage = BitmapFactory.decodeFile(picturePath);
                imageShare.setImageBitmap(recievedImage);
            }catch (Exception e){
                Toast.makeText(getContext(), "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
