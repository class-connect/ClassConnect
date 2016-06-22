package com.codepath.classconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.adapters.ClassAdapter;
import com.codepath.classconnect.models.Klass;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {
    ListView lvKlasses;
    ArrayList<Klass> klasses = new ArrayList<Klass>();
    ClassAdapter klassAdapter;

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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
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
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    private void populateData() {
        // Add item to adapter
        Klass k = new Klass();
        k.setName("Music Class");
        k.setTeacherName("Ms. Johnson");

        String dtStart = "2016-6-23T09:30:00Z";
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(dtStart);
            k.setStartTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dtEnd = "2016-6-23T10:30:00Z";
        try {
            Date date = format.parse(dtEnd);
            k.setEndTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        k.setDaysOfWeek("Monday-Friday");
        k.setProfileUrl("https://randomuser.me/api/portraits/thumb/women/30.jpg");
        klassAdapter.add(k);

        Klass k1 = new Klass();
        k1.setName("Math Class");
        k1.setTeacherName("Mr. Smith");
        dtStart = "2016-6-24T10:00:00Z";
        try {
            Date date = format.parse(dtStart);
            k1.setStartTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dtEnd = "2016-6-23T12:30:00Z";
        try {
            Date date = format.parse(dtEnd);
            k1.setEndTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        k1.setDaysOfWeek("Thursday");
        k1.setProfileUrl("https://randomuser.me/api/portraits/thumb/men/20.jpg");
        klassAdapter.add(k1);

    }
}
