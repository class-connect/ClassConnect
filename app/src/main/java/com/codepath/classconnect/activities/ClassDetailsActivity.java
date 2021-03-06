package com.codepath.classconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.codepath.classconnect.R;
import com.codepath.classconnect.fragments.ClassDetailFragment;

public class ClassDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle("Class Details");
        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String klassId = intent.getStringExtra("klassId");



        if (savedInstanceState == null) {
            ClassDetailFragment userHeaderFragment = ClassDetailFragment.newInstance(klassId);
            // Display user fragment in this activity - dynamic way
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userHeaderFragment);
            ft.commit();
        }

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
