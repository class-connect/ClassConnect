package com.codepath.classconnect.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.models.Klass;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by adeshpa on 6/25/16.
 */
public class ClassDetailFragment extends Fragment {
    private ImageView ivTeacherImage;
    private TextView tvKlassName;
    private TextView tvTeacherName;
    private TextView tvClassDescription;
    private TextView tvTeacherBio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_class_details, container, false);
        setUpViews(v);
        setFragmentFields();
        return v;
    }

    public static ClassDetailFragment newInstance(String klassId) {
        ClassDetailFragment fragment = new ClassDetailFragment();
        Bundle args = new Bundle();
        args.putString("klassId", klassId);
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpViews(View v) {
        ivTeacherImage = (ImageView) v.findViewById(R.id.ivTeacherImage);
        tvKlassName = (TextView) v.findViewById(R.id.tvKlassName);
        tvTeacherName = (TextView) v.findViewById(R.id.tvTeacherName);
        tvClassDescription = (TextView) v.findViewById((R.id.tvClassDescription));
        tvTeacherBio = (TextView) v.findViewById(R.id.tvTeacherBio);
    }

    private void setFragmentFields() {
        Bundle args = getArguments();
        String klassId = args.getString("klassId");
        Klass.findByObjectId(klassId, new GetCallback<Klass>() {
            @Override
            public void done(Klass klass, ParseException e) {
                Picasso.with(getActivity())
                        .load(klass.getProfileUrl())
                        .transform(new RoundedCornersTransformation(2, 2))
                        .error(R.drawable.progress_animation)
                        .placeholder(R.drawable.progress_animation)
                        .into(ivTeacherImage);
                tvKlassName.setText(klass.getName().toString());
                tvTeacherName.setText(klass.getTeacherName().toString());
                tvClassDescription.setText(klass.getDescription().toString());
                String bio = klass.getTeacher().getBio();
                if (bio != null) {
                    tvTeacherBio.setText(bio.toString());
                }
                else {
                    tvTeacherBio.setText("Bio not available");
                }
            }
        });

    }
}
