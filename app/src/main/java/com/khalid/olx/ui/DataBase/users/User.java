package com.khalid.olx.ui.DataBase.users;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.khalid.olx.ui.DataBase.Posts.Post;

import java.util.List;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    public String email;

    public String password;

    public String phone;

    public String photoPath;

    public List<Post> userPosts;
}
