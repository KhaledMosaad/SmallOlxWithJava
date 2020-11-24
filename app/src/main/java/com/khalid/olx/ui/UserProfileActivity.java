package com.khalid.olx.ui;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.khalid.olx.R;
import com.khalid.olx.ui.DataBase.PostsDatabaseClint;
import com.khalid.olx.ui.DataBase.UserManegerDataBase;
import com.khalid.olx.ui.DataBase.users.User;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView emailText;
    private TextView phoneText;
    private Button signOutButton;
    private String mEmail;
    private String mPassword;
    private SharedPreferences.Editor sharedEditor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        profileImage=findViewById(R.id.profile_image);
        emailText=findViewById(R.id.email_profile);
        phoneText=findViewById(R.id.phone_profile);
        signOutButton=findViewById(R.id.signoutbtn);
        SharedPreferences sharedPreferences = getSharedPreferences("users", MODE_PRIVATE);
        sharedEditor= sharedPreferences.edit();
        boolean isRememberMe=sharedPreferences.getBoolean("rememberMe",false);
        mEmail=sharedPreferences.getString("email","k");
        mPassword=sharedPreferences.getString("password","p");
        new AsyncUser().execute();
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfileActivity.this,
                        LoginActivity.class);
                sharedEditor.putBoolean("rememberMe",false);
                sharedEditor.commit(); // commit() with main thread , apply() with another thread
                startActivity(intent);
                finish();
            }
        });
    }
    class AsyncUser extends AsyncTask<Void,Void, User>
    {

        @Override
        protected User doInBackground(Void... voids) {
            return PostsDatabaseClint.getInstance(getApplicationContext()).
                    getUserManegerDataBase().userDAO().
                    findUser(mEmail,mPassword);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Uri image=Uri.parse(Uri.decode(user.photoPath));
            profileImage.setImageURI(image);
            emailText.setText(user.email);
            phoneText.setText(user.phone);
        }
    }
}
