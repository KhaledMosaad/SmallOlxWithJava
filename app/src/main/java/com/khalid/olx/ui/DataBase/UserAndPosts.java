package com.khalid.olx.ui.DataBase;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.khalid.olx.ui.DataBase.Posts.Post;
import com.khalid.olx.ui.DataBase.users.User;

import java.util.List;

public class UserAndPosts {
    @Embedded public User user;
    @Relation(
            parentColumn = "email",
            entityColumn = "email"
    )
    public List<Post> Posts;
}
