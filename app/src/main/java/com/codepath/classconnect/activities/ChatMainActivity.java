package com.codepath.classconnect.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.classconnect.R;
import com.codepath.classconnect.adapters.ChatListAdapter;
import com.codepath.classconnect.models.Klass;
import com.codepath.classconnect.models.Message;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by aramar1 on 6/2/16.
 */
public class ChatMainActivity extends AppCompatActivity {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";

    static final String TAG = ChatMainActivity.class.getSimpleName();
    EditText etText;
    Button btSend;
    ListView lvChat;
    ArrayList<Message> mMessages;
    ChatListAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;
    ImageButton cameraButton;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 500;
    static final int POLL_INTERVAL = 100; // milliseconds
    Handler mHandler = new Handler();  // android.os.Handler
    Klass klassObject;
    Bitmap bmp;
    Intent i;
    Uri BmpFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

        //TBD
        //get the class object from the intent
        mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

    }
    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }
    void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Find the text field and button
        etText = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);

        mMessages = new ArrayList<>();
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatListAdapter(ChatMainActivity.this, userId, mMessages);
        lvChat.setAdapter(mAdapter);
        cameraButton=(ImageButton)findViewById(R.id.ibCamera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etText.getText().toString();
                ParseObject message = ParseObject.create("Message");
                ParseFile pFile = null ;

                String KEY_KLASS = "KjCD4El19U";//remove this once intergratedw ith the class


                message.put(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
                message.put(BODY_KEY, data);

                // Ensure bmp has value
                if (bmp == null || BmpFileName == null) {
                    Log.d ("Error" , "Problem with image");
                    Toast.makeText(ChatMainActivity.this, "No image..",
                            Toast.LENGTH_SHORT).show();

                }else{
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    pFile = new ParseFile("Image.png", stream.toByteArray());
                    try
                    {
                        pFile.saveInBackground();
                        Toast.makeText(ChatMainActivity.this, "Image Saved file",
                                Toast.LENGTH_SHORT).show();
                        message.put("photo", pFile);
                        message.save();
                        Toast.makeText(ChatMainActivity.this, "Image Saved",
                                Toast.LENGTH_SHORT).show();

                    }
                    catch (ParseException e)
                    {
                        // TODO Auto-generated catch block
                        Toast.makeText(ChatMainActivity.this, "Error in saving image",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }


                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ChatMainActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etText.setText(null);
            }
        });
    }

    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            mHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    public void captureImage() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {

            File photoFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".jpg");
            System.out.println("Photo file path:::: "+photoFile.getAbsolutePath());
            try {
                if (photoFile.exists() == false) {
                    photoFile.getParentFile().mkdirs();
                    photoFile.createNewFile();
                }
            } catch (IOException e) {
                Log.d("DocumentActivity", "Could not create file.", e);
            }
            Log.d("DocumentActivity", photoFile.getAbsolutePath());
                BmpFileName = Uri.fromFile(photoFile);
            i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, BmpFileName);
            startActivityForResult(i, 0);


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                bmp = MediaStore.Images.Media.getBitmap( this.getContentResolver(), BmpFileName);
            } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



}
