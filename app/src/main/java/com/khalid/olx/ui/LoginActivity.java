package com.khalid.olx.ui;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.khalid.olx.R;
import com.khalid.olx.ui.DataBase.PostsDatabaseClint;
import com.khalid.olx.ui.DataBase.UserManegerDataBase;
import com.khalid.olx.ui.DataBase.users.User;

public class LoginActivity extends AppCompatActivity {
    private EditText emailedit,passwordedit;
    private Button login,signup;
    private Switch remembermesw;
    private TextView remembertext;

    private String email;
    private String pass;
    private SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initialize();

        SharedPreferences sharedPreferences = getSharedPreferences("users", MODE_PRIVATE);
        sharedEditor= sharedPreferences.edit();
        boolean isRememberMe=sharedPreferences.getBoolean("rememberMe",false);
        remembermesw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedEditor.putBoolean("rememberMe",isChecked);
                sharedEditor.apply();
            }
        });
        if(isRememberMe)
        {
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailedit.getText().toString();
                pass=passwordedit.getText().toString();
                logininitialize();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void logininitialize()
    {

        if(TextUtils.isEmpty(email))
        {
            emailedit.setError(getString(R.string.enter_your_email));
        }
        else if(TextUtils.isEmpty(pass))
        {
            emailedit.setError(null);
            passwordedit.setError(getString(R.string.enter_your_password));
        }
        else
        {
            emailedit.setError(null);
            passwordedit.setError(null);
            new FindUser().execute();
        }
    }
    private void initialize()
    {
        emailedit=findViewById(R.id.emailedit);
        passwordedit=findViewById(R.id.passwordedit);
        login=findViewById(R.id.loginbtn);
        signup=findViewById(R.id.signupbtn);
        remembermesw=findViewById(R.id.remembermeswitch);
        remembertext=findViewById(R.id.remembermetext);
    }
    class FindUser extends AsyncTask<Void,Void,User>{

        @Override
        protected User doInBackground(Void... voids) {
            return PostsDatabaseClint.getInstance(getApplicationContext()).
                    getUserManegerDataBase().
                    userDAO().
                    findUser(email,pass);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if(user==null)
            {
                AlertDialog.Builder alert=new AlertDialog.Builder(LoginActivity.this).
                        setTitle("Login Fail")
                        .setMessage("Email or password invalid").
                                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alert.show();
            }
            else
            {
                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("password",pass);
                SharedPreferences shared=getSharedPreferences("users",MODE_PRIVATE);
                SharedPreferences.Editor editor=shared.edit();
                editor.putString("email",email);
                editor.putString("password",pass);
                editor.apply();
                startActivity(intent);
                finish();
            }
        }
    }
}
