package com.khalid.olx.ui;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khalid.olx.R;
import com.khalid.olx.ui.DataBase.Posts.Post;
import com.khalid.olx.ui.DataBase.PostsDatabaseClint;

import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private RecyclerView postsRV;
    private FloatingActionButton floatingActionButton;
    private TextView nopost;
    private List<Post> postList;
    private final static int ADD_POST_REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        postsRV=findViewById(R.id.postsRV);
        floatingActionButton=findViewById(R.id.addpostbtn);
        nopost=findViewById(R.id.nopostid);
        postsRV.setLayoutManager(new LinearLayoutManager(this));

       GetPostAsyncTask getPostAsyncTask=new GetPostAsyncTask();
       getPostAsyncTask.execute();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,AddpostActivity.class);
                startActivityForResult(intent,ADD_POST_REQUEST_CODE);
            }
        });
    }
    class GetPostAsyncTask extends AsyncTask<Void, List<Post> , List<Post>>
    {

        @Override
        protected List<Post> doInBackground(Void... voids) {
            return PostsDatabaseClint.getInstance(getApplicationContext())
                    .getUserManegerDataBase().postsDAO().getAllPost();
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);
            if(postList!=null&&postList.size()>0)
            {
                nopost.setVisibility(View.GONE);
                PostsAdabter postsAdabter=new PostsAdabter(postList);
                postsRV.setAdapter(postsAdabter);
            }
            else
            {
                nopost.setVisibility(View.VISIBLE);
            }

        }
    }
}
