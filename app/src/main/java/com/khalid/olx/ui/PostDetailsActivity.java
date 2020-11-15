package com.khalid.olx.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.khalid.olx.R;
import com.khalid.olx.ui.DataBase.Posts.Post;
import com.khalid.olx.ui.DataBase.PostsDatabaseClint;

public class PostDetailsActivity extends AppCompatActivity {
    public static final String GET_POST_PHONE = "com.khalid.olx.ui.GET_POST_PHONE";
    public static final String GET_POST_DETAILS  = "com.khalid.olx.ui.GET_POST_DETAILS";
    public static final String GET_POST_NAME ="com.khalid.olx.ui.GET_POST_NAME";
    public static final String GET_POST_PRICE  = "com.khalid.olx.ui.GET_POST_PRICE";
    public static final String GET_POST_IMAGE = "com.khalid.olx.ui.GET_POST_IMAGE";
    private Button mCallSellerButton;
    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mDetailsTextView;
    private ImageView mPostImageView;
    private String mPhone,mName,mDetails,mImagePath;
    private double mPrice;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details_activity);
        mCallSellerButton=findViewById(R.id.callsaler_button_postdetails);
        mNameTextView=findViewById(R.id.name_postdatials);
        mPriceTextView=findViewById(R.id.price_postdetails);
        mDetailsTextView=findViewById(R.id.details_postdetails);
        mPostImageView=findViewById(R.id.postimage_postdetails);
        Intent intent=getIntent();
        mImagePath=intent.getStringExtra(GET_POST_IMAGE);
        mDetails=intent.getStringExtra(GET_POST_DETAILS);
        mName=intent.getStringExtra(GET_POST_NAME);
        mPhone=intent.getStringExtra(GET_POST_PHONE);
        mPrice=intent.getDoubleExtra(GET_POST_PRICE,-1);
        mNameTextView.setText("Name: "+mName);
        Uri image=Uri.parse(Uri.decode(mImagePath));
        mPostImageView.setImageURI(image);
        mPriceTextView.setText("Price: "+mPrice);
        mDetailsTextView.setText("Details: "+mDetails);

        mCallSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mPhone));
                startActivity(intent);
            }
        });
    }


}
