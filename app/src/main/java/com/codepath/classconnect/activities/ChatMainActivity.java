package com.codepath.classconnect.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codepath.classconnect.R;
import com.codepath.classconnect.UserManager;
import com.codepath.classconnect.adapters.ChatListAdapter;
import com.codepath.classconnect.models.AppUser;
import com.codepath.classconnect.models.Klass;
import com.codepath.classconnect.models.Message;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by aramar1 on 6/2/16.
 */
public class ChatMainActivity extends AppCompatActivity {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    static final String TAG = ChatMainActivity.class.getSimpleName();
    EditText etText;
    Button btSend;

    ArrayList<Message> mMessages;
    ChatListAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;
    ImageButton cameraButton;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 500;
    static final int POLL_INTERVAL = 10000; // milliseconds
    Handler mHandler = new Handler();  // android.os.Handler
    Klass klassObject;
    Bitmap bmp;
    Intent i;
    Uri BmpFileName = null;
    AppUser appUser;
    Message message;
    String klassId;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        AppUser appUser = UserManager.getCurrentUser();
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle(R.string.add_message);
        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);
        i = getIntent();
        klassId = i.getStringExtra("klassId");
        // User login
        if (appUser!= null) { // start with existing user
            setupMessagePosting(appUser);
        } else { // If not logged in, login as a new anonymous user
            //login();
        }
        Klass.findByObjectId(klassId, new GetCallback<Klass>() {
            @Override
            public void done(Klass object, com.parse.ParseException e) {
                klassObject = object;

            }
        });


    }
    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting(final AppUser user) {
        mProgressDialog = new ProgressDialog(ChatMainActivity.this);
        mProgressDialog.setTitle("Message posting ");
        // Set your progress dialog Message
        mProgressDialog.setMessage("Posting Message , Please Wait!");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Show progress dialog

        // Find the text field and button
        etText = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatMainActivity.this, user.getUserId(), mMessages);
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
                ParseFile pFile = null ;
                message = new Message();
                message.setKlass(klassObject);
                message.setUser(user);
                message.setBody(data);
                // Ensure bmp has value
                if (bmp == null || BmpFileName == null) {
                    Log.d ("Error" , "Problem with image");
                    
                }else{
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    pFile = new ParseFile("Image.png", stream.toByteArray());
                    try
                    {
                        pFile.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException arg0) {
                                mProgressDialog.dismiss();
                            }
                        }, new ProgressCallback() {
                            @Override
                            public void done(Integer percentDone) {
                                Log.i("TAG", "Updating progress: " + percentDone);
                                mProgressDialog.show();
                                mProgressDialog.setProgress(percentDone);

                            }
                        });
                        message.setPhoto(pFile);

                    }
                    //catch (ParseException e)
                    catch (Exception e)
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
                        ChatMainActivity.this.finish();
                    }
                });

                etText.setText(null);
            }
        });
    }
    public void captureImage() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {

            File photoFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".jpg");
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
    // onBackPressed is what is called when back is hit, call `overridePendingTransition`
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

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
