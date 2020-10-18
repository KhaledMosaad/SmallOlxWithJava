package com.khalid.olx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.khalid.olx.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailedit,passwordedit;
    private Button login,signup;
    private Switch remembermesw;
    private TextView remembertext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initialize();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        String email=emailedit.getText().toString();
        String pass=passwordedit.getText().toString();
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
            //TODO go to home page

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
}
