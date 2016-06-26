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
import com.codepath.classconnect.models.AppUser;
import com.codepath.classconnect.models.Klass;
import com.squareup.picasso.Picasso;

/**
 * Created by adeshpa on 6/25/16.
 */
public class ClassDetailFragment extends Fragment{
    private ImageView ivTeacherImage;
    private TextView tvKlassName;
    private TextView tvTeacherName;
    private TextView tvClassDescription;
    private TextView numStudents;
    private Klass klass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        klass = getClassDetails();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_class_details, container, false);
        setUpViews(v);

        setFragmentFields();
        return v;
    }

    public static ClassDetailFragment newInstance() {
        ClassDetailFragment classDetailFragment = new ClassDetailFragment();
        return classDetailFragment;
    }

    public Klass getClassDetails(){
        Klass k = new Klass();
        AppUser user = new AppUser();
        user.setProfileUrl("https://randomuser.me/api/portraits/men/74.jpg");
        user.setName("George Clooney");
        user.setUserId("12344");
        k.setTeacher(user);
        k.setName("Drama Class");
        k.setDescription("In this class, students will continue their study of the basic concepts and begin to refine their presentational skills.  Students will use various creative drama techniques to build ensemble, stimulate imagination, movement, and role-play with an emphasis on believability and sensory awareness.  Students will use observation and emotional memory to reveal thoughts and feelings and to build believable characters and situations.  Students will learn and use drama and theatre vocabulary in class discussions and the activities will address the promotion and reinforcement of students' literacy skills.  A history of theatre timeline will be presented with a focus on the Eastern Theatre, and specifically the Noh and Kabuki Theatres, to establish historical perspective and relevance.  Students will exhibit and reinforce their skills through individual and group presentations, performances, and script and journal writing.");
        return k;
    }

    private void setUpViews(View v) {
        ivTeacherImage = (ImageView)v.findViewById(R.id.ivTeacherImage);
        tvKlassName = (TextView)v.findViewById(R.id.tvKlassName);
        tvTeacherName = (TextView)v.findViewById(R.id.tvTeacherName);
        tvClassDescription = (TextView)v.findViewById((R.id.tvClassDescription));
        numStudents=(TextView)v.findViewById(R.id.tvNumStudents);
    }

    private void setFragmentFields() {
        Picasso.with(getActivity()).load(klass.getProfileUrl()).into(ivTeacherImage);
        tvKlassName.setText(klass.getName().toString());
        tvTeacherName.setText(klass.getTeacherName().toString());
        tvClassDescription.setText(klass.getDescription().toString());
        numStudents.setText("25");
    }
}
