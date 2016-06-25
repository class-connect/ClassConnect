package com.codepath.classconnect.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by ssunda1 on 6/24/16.
 */
public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        int hourOfDay = arguments.getInt("hourOfDay");
        int minute = arguments.getInt("minute");

        if (hourOfDay == 0) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, hourOfDay, minute, false);
    }
}
