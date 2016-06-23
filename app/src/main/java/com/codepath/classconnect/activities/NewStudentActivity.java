package com.codepath.classconnect.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.codepath.classconnect.R;
import com.codepath.classconnect.UserManager;
import com.codepath.classconnect.models.AppUser;
import com.codepath.classconnect.models.Student;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class NewStudentActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle(R.string.add_student);
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

    public void saveStudent(View view) {
        Student student = new Student();
        student.setFirstName(etFirstName.getText().toString());
        student.setLastName(etLastName.getText().toString());
        AppUser parent = UserManager.getCurrentUser();
        parent.addStudent(student);
        parent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                UserManager.refreshCurrentUser();
            }
        });
        finish();
    }
}
