package com.codepath.classconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.activities.EventDetailsActivity;
import com.codepath.classconnect.models.Event;

import java.util.ArrayList;

/**
 * Created by adeshpa on 6/29/16.
 */
public class EventListAdapter  extends ArrayAdapter<Event> {
    // View lookup cache
    private static class ViewHolder  {
        public TextView tvEventName;
        public TextView tvEventNote;
        public TextView tvEventLocation;
    }

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Event event = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item, parent, false);
            viewHolder.tvEventName = (TextView)convertView.findViewById(R.id.tvEventName);
            viewHolder.tvEventNote = (TextView)convertView.findViewById(R.id.tvEventNotes);
            viewHolder.tvEventLocation = (TextView)convertView.findViewById(R.id.tvLocation);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Populate data into the template view using the data object
        viewHolder.tvEventName.setText(event.getName());
        viewHolder.tvEventNote.setText(event.getNotes());
        viewHolder.tvEventLocation.setText(event.getLocation());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventDetailsActivity.class);
                intent.putExtra("eventId", event.getObjectId());
                v.getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
