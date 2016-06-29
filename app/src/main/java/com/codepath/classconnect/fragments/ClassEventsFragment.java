package com.codepath.classconnect.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.classconnect.R;
import com.codepath.classconnect.activities.ChatMainActivity;
import com.codepath.classconnect.activities.NewEventActivity;
import com.codepath.classconnect.adapters.EventListAdapter;
import com.codepath.classconnect.adapters.MessageListAdapter;
import com.codepath.classconnect.listeners.EndlessRecylcerScrollerListener;
import com.codepath.classconnect.models.Event;
import com.codepath.classconnect.models.Klass;
import com.codepath.classconnect.models.Message;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adeshpa on 6/11/16.
 */
public class ClassEventsFragment extends ClassListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeContainer;
    ListView lvEvents;
    private EventListAdapter eventListAdapter ;
    private ArrayList<Event> events;

    private final int REQUEST_CODE=1002;

    @Override
    protected void customLoadMore(int page) {
        populateEvents(page);
    }

    public static ClassEventsFragment newInstance(String eventId) {
        ClassEventsFragment eventFragment = new ClassEventsFragment();
        Bundle args = new Bundle();
        args.putString("eventId", eventId);
        eventFragment.setArguments(args);
        return eventFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = new ArrayList<Event>();

        // Initialize to show first page
        populateEvents(1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        lvEvents= (ListView)view.findViewById(R.id.lvEvents);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        eventListAdapter = new EventListAdapter(getActivity(), events);
        lvEvents.setAdapter(eventListAdapter);
        ImageButton fab = (ImageButton)view.findViewById(R.id.fabEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewEvent();
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateEvents(0);
            }
        });

        return view;
    }

    // send api request to get timeline json
    // fill listview
    private void populateEvents(int sinceId) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        // Configure limit and sort order
        query.orderByAscending("createdAt");
        String KEY_EVENTS = getArguments().getString("eventId");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        Event.findByObjectId(KEY_EVENTS, new GetCallback<Event>() {
            @Override
            public void done(Event object, ParseException e) {
                if (e == null) {
                    if (object == null) {
                    }
                    else {
                        // there should be only one class
                        final Klass klass = object.getKlass();
                        Event.findAll(klass,new FindCallback<Event>() {
                            public void done(List<Event> eventObjs, ParseException e) {
                                if (e == null) {
                                    events.clear();
                                    events.addAll(eventObjs);
                                    eventListAdapter.notifyDataSetChanged();

                                } else {
                                    Log.e("message", "Error Loading Events" + e);
                                }
                            }
                        });
                    }
                }
                else {
                    Log.e("Events", "Error Loading Events" + e);
                }
            }
        });

    }

    @Override
    public void onRefresh() {

    }

    public void addNewEvent(){
        Intent i = new Intent(this.getActivity(), NewEventActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

}
