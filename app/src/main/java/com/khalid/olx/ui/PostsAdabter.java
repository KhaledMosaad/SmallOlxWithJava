package com.khalid.olx.ui;

import android.net.Uri;
import android.os.Parcel;
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

public class PostsAdabter extends RecyclerView.Adapter<PostsAdabter.PostViewHolder> {

    private final List<Post> postslist;
    public PostsAdabter(List<Post> postlist){
        this.postslist=postlist;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView=LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,
                null,false);
        return new PostViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder,final int position) {
        holder.price.setText((postslist.get(position).price+""));
        holder.postName.setText(postslist.get(position).name);
        Uri postimg;
        if ( postslist.get(position).postImg != null) {
             postimg = Uri.parse(Uri.decode(postslist.get(position).postImg));
            holder.postImage.setImageURI(postimg);
        }

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

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView postImage;
        TextView postName;
        TextView price;
        ConstraintLayout holePost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage= itemView.findViewById(R.id.postimage);
            postName= itemView.findViewById(R.id.postname);
            price= itemView.findViewById(R.id.demotext);
            holePost= itemView.findViewById(R.id.postitemcontainer);
        }
    }
}
