package com.khalid.olx.ui.DataBase;

import android.content.Context;
import androidx.room.Room;

public class PostsDatabaseClint {
    private UserManegerDataBase userManegerDataBase;
    private static PostsDatabaseClint postsDatabaseClint;
    private final static String DATABASE_NAME="posts";
    private Context context;



    private PostsDatabaseClint(Context context){
        this.context=context;
        userManegerDataBase= Room.databaseBuilder(context,
                UserManegerDataBase.class,DATABASE_NAME).
                fallbackToDestructiveMigration().build();
    }

    public static synchronized PostsDatabaseClint getInstance(Context context)
    {
        if(postsDatabaseClint==null)
        {
            postsDatabaseClint=new PostsDatabaseClint(context);
        }
        return postsDatabaseClint;
    }

    public UserManegerDataBase getUserManegerDataBase()
    {
        return userManegerDataBase;
    }

}
