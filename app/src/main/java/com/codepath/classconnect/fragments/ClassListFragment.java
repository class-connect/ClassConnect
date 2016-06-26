package com.codepath.classconnect.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.adapters.ChatListAdapter;
import com.codepath.classconnect.listeners.EndlessScrollListener;
import com.codepath.classconnect.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adeshpa on 6/11/16.
 */
public abstract class ClassListFragment extends Fragment {
    private ArrayList<Message> messages;
    private ChatListAdapter aChatList;
    protected ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;

    // Inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        setUpViews(v);
        return v;
    }

    private void setUpViews(View v) {
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aChatList);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount){
                customLoadMore(page);
                return true;
            }
        });

        swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aChatList.clear();
                customLoadMore(1);
                swipeContainer.setRefreshing(false);

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    // Creation life cycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messages = new ArrayList<Message>();
        //aTweets = new ChatListAdapter();
    }

    public void addAll(List<Message>tweets) {
        //aTweets.addAll(tweets);
    }

    protected abstract void customLoadMore(int page);

    public ListView getListView() {
        return lvTweets;
    }
}
