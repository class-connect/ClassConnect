package com.codepath.classconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.activities.ClassDetailsActivity;
import com.codepath.classconnect.activities.EventsActivity;
import com.codepath.classconnect.models.Klass;
import com.codepath.classconnect.models.KlassRegistration;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by adeshpa on 6/22/16.
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
        //ArrayAdapter<KlassRegistration> {
    private final static int FADE_DURATION = 1000; // in milliseconds
    Context m_context;
    // Store a member variable for the contacts
    private List<KlassRegistration> mClasses;
    private View contactView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        contactView = inflater.inflate(R.layout.item_klass, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        // Get the data item for this position
        KlassRegistration kr = mClasses.get(position);
        final Klass klass = kr.getKlass();

        // Set item views based on your views and data mode
        TextView tvKlassName = viewHolder.tvKlassName;
        if (tvKlassName != null) {
            tvKlassName.setText(klass.getName());
        }

        TextView tvStudentName = viewHolder.tvStudentName;
        if (tvStudentName != null) {
            tvStudentName.setText(kr.getStudentName());
        }

        TextView tvTeacherName = viewHolder.tvTeacherName;
        if (tvTeacherName != null) {
            tvTeacherName.setText(klass.getTeacherName());
        }

        TextView tvStartTime = viewHolder.tvStartTime;
        if (tvStartTime != null) {
            tvStartTime.setText(klass.getStartTime());
        }

        TextView tvEndTime = viewHolder.tvEndTime;
        if (tvEndTime != null) {
            tvEndTime.setText(klass.getEndTime());
        }

        TextView tvDaysOfWeek = viewHolder.tvDaysOfWeek;
        if (tvDaysOfWeek != null) {
            tvDaysOfWeek.setText(klass.getDaysOfWeek());
        }

        ImageView ivTeacherImage = viewHolder.teacherImage;
        if (ivTeacherImage != null) {
            Picasso.with(m_context)
                    .load(klass.getProfileUrl())
                    .transform(new RoundedCornersTransformation(2, 2))
                    .error(R.drawable.progress_animation)
                    .placeholder(R.drawable.progress_animation)
                    .into(ivTeacherImage);

            ivTeacherImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "Show Teachers Details here", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), ClassDetailsActivity.class);
                    intent.putExtra("klassId", klass.getObjectId());
                    v.getContext().startActivity(intent);
                    // Here you apply the animation when the view is bound
                    setAnimation(viewHolder.teacherImage, position);
                }

            });
        }

        // Set the view to fade in
        setFadeAnimation(viewHolder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(m_context, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mClasses.size();
    }

    public void clear() {
        mClasses.clear();
    }

    public void addAll(List<KlassRegistration> objects) {
        mClasses.addAll(objects);
    }

    // View lookup cache
    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        ImageView teacherImage;
        TextView tvKlassName;
        TextView tvStudentName;
        TextView tvTeacherName;
        TextView tvStartTime;
        TextView tvEndTime;
        TextView tvDaysOfWeek;

        public ViewHolder(final View itemView, ClassAdapter classAdapter) {
            super(itemView);

            teacherImage = (ImageView) itemView.findViewById(R.id.ivTeacherImage);
            tvKlassName = (TextView) itemView.findViewById(R.id.tvKlassName);
            tvStudentName = (TextView) itemView.findViewById(R.id.tvStudentName);
            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            tvStartTime = (TextView) itemView.findViewById(R.id.tvStartTime);
            tvEndTime = (TextView) itemView.findViewById(R.id.tvEndTime);
            tvDaysOfWeek = (TextView) itemView.findViewById(R.id.tvDaysOfWeek);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemView.getContext().startActivity(new Intent(itemView.getContext(), EventsActivity.class));
            //itemView.getContext().startActivity(new Intent(itemView.getContext(), ChatMainActivity.class));
            //Toast.makeText(itemView.getContext(), "Show Chat window here !!", Toast.LENGTH_SHORT).show();
        }
    }

    // Pass in the contact array into the constructor
    public ClassAdapter(Context context, List<KlassRegistration> classes) {
        mClasses = classes;
        m_context = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return m_context;
    }
}
