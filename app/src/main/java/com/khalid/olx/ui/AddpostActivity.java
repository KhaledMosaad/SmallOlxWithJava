package com.khalid.olx.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import com.khalid.olx.ui.DataBase.Posts.Post;
import com.khalid.olx.ui.DataBase.PostsDatabaseClint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddpostActivity extends AppCompatActivity {

    private ImageView mImageView;
    private EditText name;
    private EditText price;
    private EditText details;
    private Button addbtn;
    private static final int STORAGE_CODE=700;
    private static final int CAMERA_CODE=800;
    private static final int OPEN_GALLERY_CODE=900;
    private static final int TAKE_PHOTO_CODE=1000;
    private String mImagePath;
    private boolean mIsImageAdd =false;
    private String email;
    private Post addpost;
    private String userPhone;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity);

        mImageView =findViewById(R.id.addimgpost);
        name=findViewById(R.id.nameaddpost);
        price=findViewById(R.id.priceaddpost);
        details=findViewById(R.id.detailsaddpost);
        addbtn=findViewById(R.id.addpostbtnaddpost);
        sharedPreferences=getSharedPreferences("infomation",MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();
        email=sharedPreferences.getString("Email","k");
        sharedEditor.apply();
        onClickListners();
    }
    private void onClickListners(){
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuToChoose();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPost();
            }
        });

    }
    void setPost(){
         String setname=name.getText().toString();
         String setprice=price.getText().toString();
         String setDetails=details.getText().toString();

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
        else if(!mIsImageAdd)
        {
            Toast.makeText(AddpostActivity.this,"Please Set a photo",Toast.LENGTH_LONG).show();
        }
        else {
           addpost=new Post();
           addpost.details=setDetails;
           addpost.name=setname;
           addpost.price=Double.parseDouble(setprice);
           addpost.email=email;
           addpost.postImg= mImagePath;
           new PostAsyncTask().execute();
        }
    }
    class PostAsyncTask extends AsyncTask<Void,Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            PostsDatabaseClint.getInstance(getApplicationContext()).
                    getUserManegerDataBase().postsDAO().insertPost(addpost);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(AddpostActivity.this,"Post add Successfully ",
                    Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
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
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,OPEN_GALLERY_CODE);
        }
    }
    private void openCamera() throws IOException {
        if(ContextCompat.checkSelfPermission(AddpostActivity.this,Manifest.permission.CAMERA)!=
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
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    return;
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(AddpostActivity.this,
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
                imageFileName,
                ".jpg",
                storageDir
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
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(AddpostActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
            if(mImageView!=null){
                mIsImageAdd=true;
            }
        }
        else if (requestCode == OPEN_GALLERY_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, 
                    null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mImagePath = cursor.getString(columnIndex);
            cursor.close();
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mImagePath));
            if(mImageView!=null){
                mIsImageAdd=true;
            }
        }
    }
}
