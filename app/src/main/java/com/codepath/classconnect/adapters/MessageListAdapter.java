package com.codepath.classconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.activities.ChatMainActivity;
import com.codepath.classconnect.models.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
/**
 * Created by aramar1 on 6/28/16.
 */
public class MessageListAdapter extends ArrayAdapter<Message> {
        // View lookup cache
        private static class ViewHolder  {
            public ImageView imageViewIcon;
            public TextView tvUsernName;
            public TextView body;
            public ImageView ivCover;
        }
        // Define listener member variable
        private static OnItemClickListener listener;
        // Define the listener interface
        public interface OnItemClickListener {
            void onItemClick(View itemView, int position);
        }
        // Define the method that allows the parent activity or fragment to define the listener
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public MessageListAdapter(Context context, ArrayList<Message> messages) {
            super(context, android.R.layout.simple_list_item_1, messages);
        }
        //overide advanced adapter

        // into a relevant row within an AdapterView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Message message = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.message_content, parent, false);
                viewHolder.ivCover = (ImageView)convertView.findViewById(R.id.ivUserCover);
                viewHolder.tvUsernName = (TextView)convertView.findViewById(R.id.tvUsernName);
                viewHolder.body = (TextView)convertView.findViewById(R.id.tvBody);
                viewHolder.imageViewIcon = (ImageView)convertView.findViewById(R.id.imageViewIcon);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //Populate data into the template view using the data object
            viewHolder.tvUsernName.setText(message.getUser().getName());
            viewHolder.body.setText(message.getBody());
            if(message.getPhoto()!=null && !message.getPhoto().equals(""))
            {
                viewHolder.imageViewIcon.setVisibility(View.VISIBLE);

                Picasso.with(getContext()).load(R.drawable.ic_launcher_attm).into(viewHolder.imageViewIcon);
            }
            else {
                viewHolder.imageViewIcon.setVisibility(View.GONE);
            }

            Picasso.with(getContext()).load(message.getUser().getProfileUrl()).
                    transform(new RoundedCornersTransformation(5,5)).into(viewHolder.ivCover);
            viewHolder.ivCover.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent myIntent = new Intent(v.getContext(), ChatMainActivity.class);
                    System.out.println();

                    myIntent.putExtra("klassObjectId",message.getKlass().getObjectId());
                    v.getContext().startActivity(myIntent);
                }
            });
            //Picasso.with(getContext()).load(book.getPosterUrl()).into(viewHolder.ivCover);
            // Return the completed view to render on screen
            return convertView;
        }
    }



