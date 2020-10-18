package com.khalid.olx.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.khalid.olx.ui.DataBase.PostsDatabaseClint;
import com.khalid.olx.ui.DataBase.users.User;

public class SignupActivity extends AppCompatActivity {
    private ImageView addimg;
    private EditText emailedit,passwordedit,confpasswordedit,phonerdit;
    private Button signup;
    private static final int STORAGE_CODE=700;
    private static final int CAMERA_CODE=800;
    private static final int OPEN_GALLERY_CODE=900;
    private static final int TAKE_PHOTO_CODE=1000;
    private String imgURI;
    private boolean isimgadd=false;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        initialize();
        setListener();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignup();
            }
        });
    }
    private void initialize()
    {
        addimg=findViewById(R.id.addimgsingup);
        emailedit=findViewById(R.id.emaileditsignup);
        passwordedit=findViewById(R.id.passwordeditsignup);
        confpasswordedit=findViewById(R.id.confermpass);
        phonerdit=findViewById(R.id.phone);
        signup=findViewById(R.id.signupbtnsignup);
    }
    private void fetchDatafromUser(){
    }
    private void setListener()
    {
        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuToChoose();
            }
        });

    }
    void setSignup(){
        final String email=emailedit.getText().toString();
        final String password=passwordedit.getText().toString();
        final String confpassword=confpasswordedit.getText().toString();
        final String phone=phonerdit.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            emailedit.setError("Please Enter Your Email");
        }
        else if(TextUtils.isEmpty(password))
        {
            passwordedit.setError("Please Enter Password");
        }
        else if(TextUtils.isEmpty(confpassword))
        {
            confpasswordedit.setError("Confirm Your Password");
        }
        else if(!password.equals(confpassword))
        {
            confpasswordedit.setError("The Password not equal each other");
        }
        else if(TextUtils.isEmpty(phone))
        {
            phonerdit.setError("Please Enter Your phone number");
        }
        else if(!isimgadd)
        {
            Toast.makeText(SignupActivity.this,"Please Set a photo",Toast.LENGTH_LONG).show();
        }
        else {
            user=new User();
            user.email=email;
            user.password=password;
            user.phone=phone;
            user.photoPath=imgURI;
            new UserAsyncTask().execute();
        }
    }

    class UserAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            PostsDatabaseClint.getInstance(getApplicationContext())
                    .getUserManegerDataBase().userDAO().insertUser(user);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent=new Intent(SignupActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void openMenuToChoose()
    {
        final CharSequence[] options={getString(R.string.open_camera),
                getString(R.string.open_Gallery),getString(R.string.cancel)};
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
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
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.
                READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED||ContextCompat.
                checkSelfPermission(this,
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
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=
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
                Bitmap img = (Bitmap) data.getExtras().get("data");
                if (img != null) {
                    addimg.setImageBitmap(img);
                    isimgadd = true;
                }
            }
            else {
                Toast.makeText(SignupActivity.this, "Please Choose photo",
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
                    addimg.setImageURI(img);
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
