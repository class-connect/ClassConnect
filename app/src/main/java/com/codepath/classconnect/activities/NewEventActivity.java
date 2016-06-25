package com.codepath.classconnect.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.classconnect.R;
import com.codepath.classconnect.UserManager;
import com.codepath.classconnect.fragments.DatePickerFragment;
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
    private static final int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.etEventName) EditText etEventName;
    @BindView(R.id.etStartDate) EditText etStartDate;
    @BindView(R.id.etStartTime) EditText etStartTime;
    @BindView(R.id.etEndDate) EditText etEndDate;
    @BindView(R.id.etEndTime) EditText etEndTime;
    @BindView(R.id.etNotes) EditText etNotes;
    @BindView(R.id.etLocation) EditText etLocation;

    private Klass klass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        ButterKnife.bind(this);

        // TODO: Get from intent
        final String klassId = "owPgAuBuQQ";

        Klass.findByObjectId(klassId, new GetCallback<Klass>() {
            @Override
            public void done(Klass object, com.parse.ParseException e) {
                klass = object;
            }
        });

        etStartDate.setText(null);
        etEndDate.setText(null);
    }

    public void saveEvent(View view) {
        Event event = new Event();
        event.setKlass(klass);
        event.setUser(UserManager.getCurrentUser());
        event.setName(etEventName.getText().toString());
        event.setNotes(etNotes.getText().toString());
        event.setLocation(etLocation.getText().toString());
        Date startTime = parse(etStartDate.getText().toString());
        if (startTime != null) {
            event.setStartTime(startTime);
        }
        Date endTime = parse(etEndDate.getText().toString());
        if (endTime != null) {
            event.setEndTime(endTime);
        }
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                finish();
            }
        });
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
        Date date = parse(value);
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
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = cal.getTime();
        text.setText(format(date));
    }

    private String format(Date date) {
        return date == null ? null : DATE_FORMAT.format(date);
    }

    private Date parse(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        try {
            return DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
