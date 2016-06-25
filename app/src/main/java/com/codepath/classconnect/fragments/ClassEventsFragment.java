package com.codepath.classconnect.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by adeshpa on 6/11/16.
 */
public class ClassEventsFragment extends ClassListFragment {

    @Override
    protected void customLoadMore(int page) {
        populateEvents(page);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize to show first page
        populateEvents(1);
    }

    // send api request to get timeline json
    // fill listview
    private void populateEvents(int sinceId) {

    }
}
