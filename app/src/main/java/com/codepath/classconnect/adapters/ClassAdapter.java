package com.codepath.classconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.models.Klass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by adeshpa on 6/22/16.
 */
public class ClassAdapter extends ArrayAdapter<Klass> {
    Context m_context;

    // View lookup cache
    private static class ViewHolder {
        ImageView teacherImage;
        TextView tvKlassName;
        TextView tvTeacherName;
        TextView tvStartTime;
        TextView tvEndTime;
        TextView tvDaysOfWeek;
    }

    public ClassAdapter(Context context, ArrayList<Klass> movies) {
        super(context, R.layout.item_klass, movies);
        m_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Klass klass = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_klass, parent, false);

            // Find image view
            viewHolder.teacherImage = (ImageView) convertView.findViewById(R.id.ivTeacherImage);

            viewHolder.tvKlassName = (TextView) convertView.findViewById(R.id.tvKlassName);
            viewHolder.tvTeacherName = (TextView) convertView.findViewById(R.id.tvTeacherName);

            if (viewHolder.teacherImage != null) {
                Picasso.with(m_context).load(klass.getProfileUrl()).error(R.drawable.progress_animation)
                        .transform(new RoundedCornersTransformation(2, 2))
                        .placeholder(R.drawable.progress_animation).into(viewHolder.teacherImage);
            }

            viewHolder.tvStartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
            viewHolder.tvEndTime = (TextView) convertView.findViewById(R.id.tvEndTime);
            viewHolder.tvDaysOfWeek = (TextView) convertView.findViewById(R.id.tvDaysOfWeek);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tvKlassName.setText(klass.getName());
        viewHolder.tvTeacherName.setText(klass.getTeacherName());
        viewHolder.teacherImage = (ImageView) convertView.findViewById(R.id.ivTeacherImage);
        if (viewHolder.teacherImage != null) {
            Picasso.with(m_context)
                    .load(klass.getProfileUrl())
                    .transform(new RoundedCornersTransformation(2, 2))
                    .error(R.drawable.progress_animation)
                    .placeholder(R.drawable.progress_animation)
                    .into(viewHolder.teacherImage);
        }
        Date startTime = klass.getStartTime();
        viewHolder.tvStartTime.setText(startTime.getHours() + ":" + startTime.getMinutes());
        viewHolder.tvEndTime.setText(klass.getEndTime().getHours() + ":" + klass.getEndTime().getMinutes());
        viewHolder.tvDaysOfWeek.setText(klass.getDaysOfWeek());
        // Return the completed view to render on screen
        return convertView;
    }
}
