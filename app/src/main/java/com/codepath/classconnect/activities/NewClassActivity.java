package com.codepath.classconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.classconnect.R;
import com.codepath.classconnect.UserManager;
import com.codepath.classconnect.models.Klass;
import com.codepath.classconnect.models.KlassRegistration;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class NewClassActivity extends AppCompatActivity {

    private EditText etStudentName;
    private EditText etCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class);

        etStudentName = (EditText) findViewById(R.id.etStudentName);
        etCode = (EditText) findViewById(R.id.etCodeName);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle(R.string.add_class);
        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);
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

    public void addClass(View view) {
        final String student = etStudentName.getText().toString();
        String code = etCode.getText().toString();

        Klass.findByCode(code, new FindCallback<Klass>() {
            @Override
            public void done(List<Klass> objects, ParseException e) {
                if (e == null) {
                    if (objects == null || objects.isEmpty()) {
                        Toast.makeText(NewClassActivity.this, "Unable to find Class. Please retry.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // there should be only one class
                        final Klass klass = objects.get(0);
                        KlassRegistration.find(klass, UserManager.getCurrentUser(), new FindCallback<KlassRegistration>() {
                            @Override
                            public void done(List<KlassRegistration> objects, ParseException e) {
                                if (e == null) {
                                    if (objects == null || objects.isEmpty()) {
                                        KlassRegistration kr = new KlassRegistration();
                                        kr.setStudentName(student);
                                        kr.setKlass(klass);
                                        kr.setOwner(UserManager.getCurrentUser());
                                        kr.saveInBackground();

                                        Intent data = new Intent();
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(NewClassActivity.this, "Student is already registered.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(NewClassActivity.this, "Unable to find Class. Please retry.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
