package com.khalid.olx.ui.DataBase.Posts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostsDAO {
    @Query("SELECT * FROM post")
    List<Post> getAllPost();

    @Insert
    void insertPost(Post post);

    @Delete
    void deletePost(Post post);

}
