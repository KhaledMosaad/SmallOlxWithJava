package com.khalid.olx.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khalid.olx.R;

public class HomeActivity extends AppCompatActivity {


    private RecyclerView postsRV;
    private FloatingActionButton floatingActionButton;
    private TextView nopost;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        postsRV=findViewById(R.id.postsRV);
        floatingActionButton=findViewById(R.id.addpostbtn);
        nopost=findViewById(R.id.nopostid);

        postsRV.setLayoutManager(new LinearLayoutManager(this));

    }
}
