package com.codepath.classconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle(R.string.new_message);
        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);

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
                    if (photoUrl != null) {
                        Picasso.with(MessageDetailsActivity.this)
                                .load(photoUrl)
                                .error(R.drawable.progress_animation)
                                .placeholder(R.drawable.progress_animation)
                                .into(ivUploadedImage);
                    }
                }
                }
            });

    }

    // onBackPressed is what is called when back is hit, call `overridePendingTransition`
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    // to handle activity transitions for Up navigation add it to the onOptionsItemSelected
    // as below
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // This refers to the Up navigation button in the action bar
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
