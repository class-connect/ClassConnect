package com.codepath.classconnect.activities;

import android.content.Intent;
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
import com.codepath.classconnect.adapters.ClassAdapter;
import com.codepath.classconnect.models.Klass;

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

    }
}
