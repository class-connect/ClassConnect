package com.codepath.classconnect.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.activities.ChatMainActivity;
import com.codepath.classconnect.adapters.MessageListAdapter;
import com.codepath.classconnect.listeners.EndlessRecylcerScrollerListener;
import com.codepath.classconnect.models.Klass;
import com.codepath.classconnect.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adeshpa on 6/11/16.
 */
public class ClassActivityFragment extends ClassListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeContainer;
    ListView lvTimeline;
    private MessageListAdapter messageListAdapter ;
    private ArrayList<Message> messages;
    private long max_id_page = 0;
    private int page;
    boolean mFirstLoad;
    Handler mHandler = new Handler();  // android.os.Handler
    private final int REQUEST_CODE=1001;


    static final int MAX_CHAT_MESSAGES_TO_SHOW = 500;
    static final int POLL_INTERVAL = 10000; // milliseconds
    //inflation logic and
    //creation life cycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages = new ArrayList<Message>();
        //get the class object from the intent
        mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
        populateTimeline();
    }


    @Override
    protected void customLoadMore(int page) {
        populateClassActivity(page);
    }

    public static ClassActivityFragment newInstance(String klassId) {
        ClassActivityFragment userFragment = new ClassActivityFragment();
        Bundle args = new Bundle();
        args.putString("klassId", klassId);
        userFragment.setArguments(args);
        return userFragment;
    }

    // send api request to get timeline json
    // fill listview
    private void populateClassActivity(int sinceId) {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list,parent,false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        lvTimeline= (ListView)view.findViewById(R.id.lvTweets);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        messageListAdapter = new MessageListAdapter(getActivity(),messages);
        lvTimeline.setAdapter(messageListAdapter);
        ImageButton fab = (ImageButton)view.findViewById(R.id.fabMessage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPost();
            }
        });

        lvTimeline.setOnScrollListener(new EndlessRecylcerScrollerListener(){
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                System.out.println("739633557509918721 ON load more "+page +":::max_id_page"+max_id_page);
                populateTimeline();
                return true;
            }
            @Override
            public int getFooterViewType() {
                return 0;
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline();
            }
        });


        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //this is where we do our lookups
        //and initialization


    }

    @Override
    public void onRefresh() {
        populateTimeline();
    }

    //make an async request and list view
    private void populateTimeline() {
            // Construct query to execute
            ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
            // Configure limit and sort order
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            query.orderByAscending("createdAt");
            String KEY_KLASS = getArguments().getString("klassId");
            // Execute query to fetch all messages from Parse asynchronously
            // This is equivalent to a SELECT query with SQL
            Klass.findByCode(KEY_KLASS, new FindCallback<Klass>() {
                @Override
                public void done(List<Klass> objects, ParseException e) {
                    if (e == null) {
                        if (objects == null || objects.isEmpty()) {
                        }
                        else {
                            // there should be only one class
                            final Klass klass = objects.get(0);
                            System.out.println("klass object in class actvity Fragment"+klass.getProfileUrl()+"klass.getObjectID"+klass.getObjectId());
                            Message.findAll(klass,new FindCallback<Message>() {
                                public void done(List<Message> messageObjects, ParseException e) {
                                    if (e == null) {
                                        messages.clear();
                                        messages.addAll(messageObjects);
                                        messageListAdapter.notifyDataSetChanged(); // update adapter

                                    } else {
                                        Log.e("message", "Error Loading Messages" + e);
                                    }
                                }
                            });
                        }
                    }
                    else {
                        Log.e("message", "Error Loading Messages" + e);
                    }
                }
            });


    }
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            populateTimeline();
            mHandler.postDelayed(this, POLL_INTERVAL);
        }
    };
    public void addNewPost(){
            Intent i = new Intent(this.getActivity(), ChatMainActivity.class);
            startActivityForResult(i, REQUEST_CODE);
            // overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    }





