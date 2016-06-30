package com.codepath.classconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.classconnect.R;
import com.codepath.classconnect.fragments.ClassActivityFragment;
import com.codepath.classconnect.fragments.ClassEventsFragment;
import com.codepath.classconnect.models.Klass;
import com.parse.GetCallback;

public class PTChatActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 123;
    private static final int RESULT_OK = 200;

    // Instance of the progress action-view
    MenuItem miActionProgressItem;
    String klassId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Intent i = getIntent();
        klassId= i.getStringExtra("klassId");

        final android.support.v7.app.ActionBar menu = getSupportActionBar();
        Klass.findByObjectId(klassId, new GetCallback<Klass>() {
            @Override
            public void done(Klass object, com.parse.ParseException e) {
                menu.setTitle(object.getName());
            }
        });


        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);

        ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);
        vpPager.setAdapter(new EventsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip pgSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        pgSlidingTabStrip.setViewPager(vpPager);

        // Attach the page change listener to tab strip and **not** the view pager inside the activity
        pgSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onTweetClick(View view) {
        //Intent i = new Intent(this,PostTweets.class);
        //startActivityForResult(i, REQUEST_CODE);
    }

    public class EventsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

        private String tabTitle[] = {"Activity", "Events"};
        private int tabIcons[] = {R.drawable.ic_add_message, R.drawable.ic_add_event};

        public EventsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ClassActivityFragment.newInstance(klassId);

            } else if (position == 1){
                return ClassEventsFragment.newInstance(klassId);
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
