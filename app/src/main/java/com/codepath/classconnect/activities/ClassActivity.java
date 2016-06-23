package com.codepath.classconnect.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.UserManager;
import com.codepath.classconnect.adapters.ClassAdapter;
import com.codepath.classconnect.models.AppUser;
import com.codepath.classconnect.models.KlassRegistration;
import com.facebook.Profile;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ClassActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;

    private ListView lvKlasses;
    private ArrayList<KlassRegistration> klasses = new ArrayList<>();
    private ClassAdapter klassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.classToolbar);
        setSupportActionBar(toolbar);

        ActionBar menu = getSupportActionBar();
        menu.setLogo(R.drawable.ic_app);
        menu.setTitle(R.string.classes);
        menu.setDisplayUseLogoEnabled(true);

        ImageButton fab = (ImageButton) findViewById(R.id.action_addClass);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewClass();
            }
        });

        // Get reference to our ListView
        lvKlasses = (ListView) findViewById(R.id.lvKlasses);

        // Create arrayAdapter
        klassAdapter = new ClassAdapter(this, klasses);

        // Link adapter to our ListView
        if (lvKlasses != null) {
            lvKlasses.setAdapter(klassAdapter);
        }

        populateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addClass) {
            return true;
        }
        if (id == R.id.action_addStudent) {
            Intent i = new Intent(this, NewStudentActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewClass() {
        Intent i = new Intent(this, NewClassActivity.class);
        startActivityForResult(i, REQUEST_CODE);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            populateData();
        }
    }

    private void populateData() {
        final ProgressDialog dialog = new ProgressDialog(this);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

            @Override
            protected  Void doInBackground(Void... params) {
                fetchData();
                return null;
            }

            @Override
            protected void onPostExecute(Void args) {
                dialog.dismiss();
            }
        }.execute();

    }

    private void fetchData() {
        AppUser user = UserManager.getCurrentUser();
        if (user == null) {
            final Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                AppUser.findByUserId(profile.getId(), new FindCallback<AppUser>() {
                    @Override
                    public void done(List<AppUser> objects, ParseException e) {
                        if (e == null && objects != null && !objects.isEmpty()) {
                            KlassRegistration.findAll(objects.get(0), new FindCallback<KlassRegistration>() {
                                @Override
                                public void done(List<KlassRegistration> objects, ParseException e) {
                                    if (e == null) {
                                        klassAdapter.clear();
                                        klassAdapter.addAll(objects);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
        else {
            KlassRegistration.findAll(user, new FindCallback<KlassRegistration>() {
                @Override
                public void done(List<KlassRegistration> objects, ParseException e) {
                    if (e == null) {
                        klassAdapter.clear();
                        klassAdapter.addAll(objects);
                    }
                }
            });
        }
    }
}
