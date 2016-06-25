package com.codepath.classconnect.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by adeshpa on 6/11/16.
 */
public class ClassActivityFragment extends ClassListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize to show first page
        populateClassActivity(1);
    }

    @Override
    protected void customLoadMore(int page) {
        populateClassActivity(page);
    }

    // send api request to get timeline json
    // fill listview
    private void populateClassActivity(int sinceId) {

    }
}
