package com.khalid.olx.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.khalid.olx.R;
import com.khalid.olx.ui.DataBase.users.User;

import java.util.Objects;

public class AddpostActivity extends AppCompatActivity {

    private ImageView addimage;
    private EditText name;
    private EditText price;
    private EditText details;
    private Button addbtn;
    private static final int STORAGE_CODE=700;
    private static final int CAMERA_CODE=800;
    private static final int OPEN_GALLERY_CODE=900;
    private static final int TAKE_PHOTO_CODE=1000;
    private String imgURI;
    private boolean isimgadd=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity);

        addimage=findViewById(R.id.addimgpost);
        name=findViewById(R.id.nameaddpost);
        price=findViewById(R.id.priceaddpost);
        details=findViewById(R.id.detailsaddpost);
        addbtn=findViewById(R.id.addpostbtn);
        setPost();
        onClickListners();
    }
    private void onClickListners(){
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuToChoose();
            }
        });

    }
    void setPost(){
        final String setname=name.getText().toString();
        final String setprice=price.getText().toString();
        final String setDetails=details.getText().toString();

        if(TextUtils.isEmpty(setname))
        {
            name.setError("Please Enter Name");
        }
        else if(TextUtils.isEmpty(setprice))
        {
            price.setError("Please Enter Price");
        }
        else if(TextUtils.isEmpty(setDetails))
        {
            details.setError("Details can't be empty");
        }
        else if(!isimgadd)
        {
            Toast.makeText(AddpostActivity.this,"Please Set a photo",Toast.LENGTH_LONG).show();
        }
        else {
            //TODO add post into list
        }
    }
    private void openMenuToChoose()
    {
        final CharSequence[] options={getString(R.string.open_camera),
                getString(R.string.open_Gallery),getString(R.string.cancel)};
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(AddpostActivity.this);
        alertDialog.setTitle(getString(R.string.option_to_select));
        alertDialog.setItems(options,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(options[which].equals(getString(R.string.open_Gallery)))
                {
                    openGallery();
                }
                else if(options[which].equals(getString(R.string.open_camera)))
                {
                    openCamera();
                }
            }
        });
        alertDialog.show();
    }
    private void openGallery()
    {
        if(ContextCompat.checkSelfPermission(AddpostActivity.this, Manifest.permission.
                READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED||ContextCompat.
                checkSelfPermission(AddpostActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_CODE);
        }
        else
        {
            Intent openGalleryIntent=new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent,OPEN_GALLERY_CODE);
        }
    }
    private void openCamera()
    {
        if(ContextCompat.checkSelfPermission(AddpostActivity.this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_CODE);
        }
        else
        {
            Intent openCameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(openCameraIntent,TAKE_PHOTO_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TAKE_PHOTO_CODE)
        {
            if(resultCode==RESULT_OK) {
                assert data != null;
                Bitmap img = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                if (img != null) {
                    addimage.setImageBitmap(img);
                    isimgadd = true;
                }
            }
            else {
                Toast.makeText(AddpostActivity.this, "Please Choose photo",
                        Toast.LENGTH_LONG).show();

            }
        }
        else if(requestCode== OPEN_GALLERY_CODE)
        {
            if(resultCode==RESULT_OK) {
                assert data != null;
                Uri img = data.getData();
                if (img != null) {
                    imgURI = img.toString();
                    addimage.setImageURI(img);
                    isimgadd = true;
                }
            }
            else
            {
                Toast.makeText(this,"Please Select Photo",Toast.LENGTH_LONG).show();
            }
        }
    }
}
