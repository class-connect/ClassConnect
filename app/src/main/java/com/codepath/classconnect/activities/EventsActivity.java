package com.codepath.classconnect.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.classconnect.R;
import com.codepath.classconnect.fragments.ClassEventsFragment;

public class EventsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 123;
    private static final int RESULT_OK = 200;
    // Instance of the progress action-view
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle("Class Activity");
        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);

        ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip pgSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        pgSlidingTabStrip.setViewPager(vpPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            //tweets = new ArrayList<Tweet>();
            //aTweets = new TweetsArrayAdapter(TimelineActivity.this, tweets);
            //lvTweets.setAdapter(aTweets);
            populateTimeline(1);
        }*/
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        //miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        //ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish

        return super.onPrepareOptionsMenu(menu);
    }

    // onBackPressed is what is called when back is hit, call `overridePendingTransition`
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
           /* case R.id.miCompose:
                Intent i = new Intent(this,PostTweets.class);
                startActivityForResult(i, REQUEST_CODE);
                return true;*/
            case R.id.action_addEvent:
                Intent i = new Intent(this, NewEventActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onTweetClick(View view) {
        //Intent i = new Intent(this,PostTweets.class);
        //startActivityForResult(i, REQUEST_CODE);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

        private String tabTitle[] = {"Activity", "Events"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new Fragment();
            } else if (position == 1){
                return new ClassEventsFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
}
