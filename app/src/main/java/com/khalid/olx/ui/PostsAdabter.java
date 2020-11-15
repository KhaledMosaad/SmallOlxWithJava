package com.khalid.olx.ui;

import android.content.Context;
import android.content.Intent;
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

public class PostsAdabter extends RecyclerView.Adapter<PostsAdabter.PostViewHolder> {

    private final List<Post> mPostList;
    private Context mContext;
    public PostsAdabter(Context context,List<Post> postList){

        mContext=context;
        this.mPostList =postList;
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
        holder.price.setText((mPostList.get(position).price+""));
        holder.postName.setText(mPostList.get(position).name);
        Uri postimg;
        if ( mPostList.get(position).postImg != null) {
             postimg = Uri.parse(Uri.decode(mPostList.get(position).postImg));
            holder.postImage.setImageURI(postimg);
        }

        holder.holePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,PostDetailsActivity.class);
                intent.putExtra(PostDetailsActivity.GET_POST_IMAGE, mPostList.
                        get(position).postImg);
                intent.putExtra(PostDetailsActivity.GET_POST_NAME,mPostList.get(position).name);
                intent.putExtra(PostDetailsActivity.GET_POST_PRICE,mPostList.get(position).price);
                intent.putExtra(PostDetailsActivity.GET_POST_DETAILS,mPostList.get(position).details);
                intent.putExtra(PostDetailsActivity.GET_POST_PHONE,mPostList.get(position).phone);

               mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
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
