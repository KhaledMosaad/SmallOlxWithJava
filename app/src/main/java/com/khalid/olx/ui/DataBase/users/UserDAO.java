package com.khalid.olx.ui.DataBase.users;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.khalid.olx.ui.DataBase.Posts.Post;
import com.khalid.olx.ui.DataBase.UserAndPosts;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE email=:email AND password=:password")
    User findUser(String email,String password);


    @Transaction
    @Query("SELECT * FROM users")
    List<UserAndPosts> getUserPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);


}
