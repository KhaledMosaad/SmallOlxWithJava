package com.khalid.olx.ui.DataBase.Posts;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    @NonNull
    public String email;


    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public double price;

    public String phone;

    public String details;

    public String postImg;

    public String userImg;
}
