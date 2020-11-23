package com.khalid.olx.ui;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
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
    private TextView noPost;
    private Toolbar mToolbar;
    private List<Post> postList;
    private final static int ADD_POST_REQUEST_CODE = 100;
    private String email;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        postsRV=findViewById(R.id.postsRV);
        floatingActionButton=findViewById(R.id.addpostbtn);
        noPost =findViewById(R.id.nopostid);
        postsRV.setLayoutManager(new LinearLayoutManager(this));


        mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("Home");
        mToolbar.inflateMenu(R.menu.menu_home_activity);
        setOnClickMenu();




       Intent getIntent=getIntent();
       email=getIntent.getStringExtra("email");
       password=getIntent.getStringExtra("password");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, AddPostActivity.class);
                startActivityForResult(intent,ADD_POST_REQUEST_CODE);
            }
        });
        GetPostAsyncTask getPostAsyncTask=new GetPostAsyncTask();
        getPostAsyncTask.execute();
    }

    private void setOnClickMenu() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId=(int) item.getItemId();
                if(itemId==R.id.search_button_home)
                {

                }
                else if(itemId==R.id.profile_icon)
                {
                    Intent intent=new Intent(HomeActivity.this,
                            UserProfileActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_POST_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                new GetPostAsyncTask().execute();
            }
        }
    }

    class GetPostAsyncTask extends AsyncTask<Void, List<Post> , List<Post>>
    {

        @Override
        protected List<Post> doInBackground(Void... voids) {

            postList=PostsDatabaseClint.getInstance(getApplicationContext())
                    .getUserManegerDataBase().postsDAO().getAllPost();
            return postList;
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);
            if(posts!=null&&posts.size()>0)
            {
                noPost.setVisibility(View.GONE);
                PostsAdabter postsAdabter=new PostsAdabter(HomeActivity.this,posts);
                postsRV.setAdapter(postsAdabter);
            }
            else
            {
                noPost.setVisibility(View.VISIBLE);
            }

        }
    }
}
