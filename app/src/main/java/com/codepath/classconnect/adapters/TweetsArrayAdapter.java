package com.codepath.classconnect.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    private static class ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvRelativeTime;
        public TextView tvScreenName;
        public TextView tvFavoriteCount;
        public ImageView ivFavoriteCount;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    // setup custom template
    // viewHolder pattern - optimization

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the tweet
        Tweet tweet = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        /*ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.tvRelativeTime = (TextView)convertView.findViewById(R.id.tvRelativeTime);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvFavoriteCount = (TextView)convertView.findViewById(R.id.tvFavoriteCount);
            viewHolder.ivFavoriteCount = (ImageView)convertView.findViewById(R.id.ivFavoriteCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate data into the template view using the data object
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        Glide.with(getContext())
                .load(Uri.parse(tweet.getUser().getProfileImageUrl()))
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.ivProfileImage);
        viewHolder.tvRelativeTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.tvScreenName.setText(tweet.getUser().getScreenName());
        viewHolder.tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));

        */
        // return the view to be inserted into list
        return convertView;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            relativeDate = relativeDate.replace("ago","");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
