package com.codepath.classconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.models.Message;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvMessage) TextView tvMessage;
    @BindView(R.id.ivUploadedImage) ImageView ivUploadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String messageObjectId = intent.getStringExtra("messageObjectId");
            Message.findByObjectId(messageObjectId, new GetCallback<Message>() {
                @Override
                public void done(Message message, ParseException e) {
                    if (e == null) {
                        String photoUrl = message.getPhoto();
                        String messagebody = message.getBody();
                        String userName = message.getUserName();
                        tvUserName.setText(userName);
                        tvMessage.setText(messagebody);
                        Picasso.with(MessageDetailsActivity.this)
                                .load(photoUrl)
                                .error(R.drawable.progress_animation)
                                .placeholder(R.drawable.progress_animation)
                                .into(ivUploadedImage);

                    }
                }
            });

    }
}
