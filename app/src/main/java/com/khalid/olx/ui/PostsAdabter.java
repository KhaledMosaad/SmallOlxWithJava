package com.khalid.olx.ui;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.khalid.olx.R;
import com.khalid.olx.ui.DataBase.Posts.Post;

import java.util.List;

public class PostsAdabter extends RecyclerView.Adapter<PostsAdabter.ViewHolder> {

    private List<Post> postslist;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View postView=inflater.inflate(R.layout.home_activity,null,false);
        ViewHolder viewHolder=new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.price.setText(postslist.get(position).details);
        holder.postName.setText(postslist.get(position).name);
        Uri postimg=Uri.parse(Uri.decode(postslist.get(position).postImg));
        holder.postImage.setImageURI(postimg);
        Uri userimg=Uri.parse(Uri.decode(postslist.get(position).userImg));
        holder.userImage.setImageURI(userimg);

        holder.holePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO after create details page
            }
        });
    }

    @Override
    public int getItemCount() {
        return postslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView postImage;
        ImageView userImage;
        TextView postName;
        TextView price;
        ConstraintLayout holePost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage=(ImageView) itemView.findViewById(R.id.postimage);
            userImage=(ImageView) itemView.findViewById(R.id.userimg);
            postName=(TextView) itemView.findViewById(R.id.postname);
            price=(TextView) itemView.findViewById(R.id.demotext);
            holePost=(ConstraintLayout) itemView.findViewById(R.id.postitem);
        }
    }
}
