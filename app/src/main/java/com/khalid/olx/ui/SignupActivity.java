package com.khalid.olx.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;

import com.khalid.olx.BuildConfig;
import com.khalid.olx.R;
import com.khalid.olx.ui.DataBase.PostsDatabaseClint;
import com.khalid.olx.ui.DataBase.users.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class SignupActivity extends AppCompatActivity {
    private ImageView mImageView;
    private EditText emailedit,passwordedit,confpasswordedit,phonerdit;
    private Button mSignupBtn;
    private static final int STORAGE_CODE=700;
    private static final int CAMERA_CODE=800;
    private static final int OPEN_GALLERY_CODE=900;
    private static final int TAKE_PHOTO_CODE=1000;
    private String mImagePath;
    private boolean mIsImageAdd =false;
    private User user;
    private File mPhotoFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        initialize();
        setListener();
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignup();
            }
        });
    }
    private void initialize()
    {
        mImageView =findViewById(R.id.addimgsingup);
        emailedit=findViewById(R.id.emaileditsignup);
        passwordedit=findViewById(R.id.passwordeditsignup);
        confpasswordedit=findViewById(R.id.confermpass);
        phonerdit=findViewById(R.id.phone);
        mSignupBtn =findViewById(R.id.signupbtnsignup);
    }
    private void setListener()
    {
        mImageView.setOnClickListener(new View.OnClickListener() {
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
        else if(!mIsImageAdd)
        {
            Toast.makeText(SignupActivity.this,"Please Set a photo",Toast.LENGTH_LONG).show();
        }
        else {
            user=new User();
            user.email=email;
            user.password=password;
            user.phone=phone;
            user.photoPath= mImagePath;

            new UserAsyncTask().execute();
        }
    }







    private void openMenuToChoose()
    {
        final CharSequence[] options={getString(R.string.open_camera),
                getString(R.string.open_Gallery),getString(R.string.cancel)};
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(SignupActivity.this);
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
                    try {
                        openCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alertDialog.show();
    }
    private void openGallery()
    {
        if(ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.
                READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED||ContextCompat.
                checkSelfPermission(SignupActivity.this,
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
    private void openCamera() throws IOException {
        if(ContextCompat.checkSelfPermission(SignupActivity.this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_CODE);
        }
        else
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                mPhotoFile = null;
                try {
                    mPhotoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    return;
                }
                // Continue only if the File was successfully created
                if (mPhotoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(SignupActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mImagePath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mImagePath);
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                mImageView.setImageBitmap(BitmapFactory.decodeStream(ims));
                mIsImageAdd=true;
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(SignupActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
        else if(requestCode== OPEN_GALLERY_CODE)
        {
            if(resultCode==RESULT_OK) {
                assert data != null;
                Uri img = data.getData();
                if (img != null) {
                    mImagePath = img.toString();
                    mImageView.setImageURI(img);
                    mIsImageAdd = true;
                }
            }
            else
            {
                Toast.makeText(this,"Please Select Photo",Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UserAsyncTask extends AsyncTask<Void,Void,Void>{


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


}
