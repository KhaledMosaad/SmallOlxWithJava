package com.khalid.olx.ui.DataBase;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.khalid.olx.ui.DataBase.Posts.Post;
import com.khalid.olx.ui.DataBase.Posts.PostsDAO;
import com.khalid.olx.ui.DataBase.users.User;
import com.khalid.olx.ui.DataBase.users.UserDAO;

@Database(entities = {User.class, Post.class},version = 1)
public abstract class UserManegerDataBase extends RoomDatabase {
    public abstract UserDAO userDAO();

    public abstract PostsDAO postsDAO();
}
