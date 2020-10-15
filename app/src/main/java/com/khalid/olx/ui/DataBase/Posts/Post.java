package com.khalid.olx.ui.DataBase.Posts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    @PrimaryKey
    public String email;

    public String name;

    public double price;

    public String details;
}
