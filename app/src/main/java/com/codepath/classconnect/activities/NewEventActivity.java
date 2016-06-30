package com.codepath.classconnect.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codepath.classconnect.R;
import com.codepath.classconnect.UserManager;
import com.codepath.classconnect.fragments.DatePickerFragment;
import com.codepath.classconnect.fragments.TimePickerFragment;
import com.codepath.classconnect.models.Event;
import com.codepath.classconnect.models.Klass;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.GetCallback;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewEventActivity extends AppCompatActivity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a");

    private static final int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.etEventName) EditText etEventName;
    @BindView(R.id.etStartDate) EditText etStartDate;
    @BindView(R.id.etStartTime) EditText etStartTime;
    @BindView(R.id.etEndDate) EditText etEndDate;
    @BindView(R.id.etEndTime) EditText etEndTime;
    @BindView(R.id.etNotes) EditText etNotes;
    @BindView(R.id.etLocation) EditText etLocation;

    @BindView(R.id.startDateWrapper) TextInputLayout startDateWrapper;
    @BindView(R.id.endDateWrapper) TextInputLayout endDateWrapper;
    @BindView(R.id.startTimeWrapper) TextInputLayout startTimeWrapper;
    @BindView(R.id.endTimeWrapper) TextInputLayout endTimeWrapper;

    private Klass klass;
    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle(R.string.add_event);
        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String klassId = intent.getStringExtra("klassId");

        Klass.findByObjectId(klassId, new GetCallback<Klass>() {
            @Override
            public void done(Klass object, com.parse.ParseException e) {
                klass = object;
            }
        });

        if (startDate == null) {
            startDate = initDate();
        }

        if (endDate == null) {
            endDate = initDate();
        }
    }

    public void saveEvent(View view) {
        Event event = new Event();
        event.setKlass(klass);
        event.setUser(UserManager.getCurrentUser());
        event.setName(etEventName.getText().toString());
        event.setNotes(etNotes.getText().toString());
        event.setLocation(etLocation.getText().toString());
        if (isValid()) {
            if (!TextUtils.isEmpty(etStartDate.getText().toString())) {
                event.setStartTime(startDate);
            }
            if (!TextUtils.isEmpty(etEndDate.getText().toString())) {
                event.setEndTime(endDate);
            }
            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    finish();
                }
            });
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(etStartDate.getText().toString())) {
            startDateWrapper.setError("Enter start date");
            isValid = false;
        }
        if (TextUtils.isEmpty(etStartTime.getText().toString())) {
            startTimeWrapper.setError("Enter start time");
            isValid = false;
        }
        if (TextUtils.isEmpty(etEndDate.getText().toString())) {
            endDateWrapper.setError("Enter end date");
            isValid = false;
        }
        if (TextUtils.isEmpty(etEndTime.getText().toString())) {
            endTimeWrapper.setError("Enter end time");
            isValid = false;
        }

        if (startDate.after(endDate)) {
            startDateWrapper.setError("Start date has to be before end date");
            isValid = false;
        }

        if (isValid) {
            // clear all errors
            clearError(startDateWrapper);
            clearError(startTimeWrapper);
            clearError(endDateWrapper);
            clearError(endTimeWrapper);
        }

        return isValid;
    }

    private void clearError(TextInputLayout t) {
        t.setError(null);
        t.setErrorEnabled(false);
    }

    public void showPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to launch Google Place Picker", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (place != null) {
                    etLocation.setText(place.getAddress());
                }
            }
        }
    }

    public void showDatePickerDialog(View v) {
        String tag;
        String value;
        final EditText text;

        if (v == etStartDate) {
            text = etStartDate;
            tag = "startDate";
            value = etStartDate.getText().toString();
        }
        else {
            text = etEndDate;
            tag = "endDate";
            value = etEndDate.getText().toString();
        }

        Bundle args = new Bundle();
        Date date = parse(DATE_FORMAT, value);
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            args.putInt("year", cal.get(Calendar.YEAR));
            args.putInt("month", cal.get(Calendar.MONTH));
            args.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
        }

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setDate(text, year, monthOfYear, dayOfMonth);
            }
        });
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), tag);
    }

    private void setDate(EditText text, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        if (text == etStartDate) {
            cal.setTime(startDate);
        }
        else {
            cal.setTime(endDate);
        }
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = cal.getTime();
        text.setText(format(DATE_FORMAT, date));
        if (text == etStartDate) {
            startDate = date;
        }
        else {
            endDate = date;
        }
    }

    private String format(SimpleDateFormat fmt, Date date) {
        return date == null ? null : fmt.format(date);
    }

    private Date parse(SimpleDateFormat fmt, String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        try {
            return fmt.parse(value);
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void showTimePickerDialog(View v) {
        String tag;
        String value;
        final EditText text;

        if (v == etStartTime) {
            text = etStartTime;
            tag = "startTime";
            value = etStartTime.getText().toString();
        }
        else {
            text = etEndTime;
            tag = "endTime";
            value = etEndTime.getText().toString();
        }

        Bundle args = new Bundle();
        Date date = parse(TIME_FORMAT, value);
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            args.putInt("hourOfDay", cal.get(Calendar.HOUR_OF_DAY));
            args.putInt("minute", cal.get(Calendar.MINUTE));
        }

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTime(text, hourOfDay, minute);
            }
        });
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), tag);
    }

    private void setTime(EditText text, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        if (text == etStartTime) {
            cal.setTime(startDate);
        }
        else {
            cal.setTime(endDate);
        }
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        Date date = cal.getTime();
        text.setText(format(TIME_FORMAT, date));
        if (text == etStartTime) {
            startDate = date;
        }
        else {
            endDate = date;
        }
    }

    private Date initDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    // onBackPressed is what is called when back is hit, call `overridePendingTransition`
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // This refers to the Up navigation button in the action bar
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
