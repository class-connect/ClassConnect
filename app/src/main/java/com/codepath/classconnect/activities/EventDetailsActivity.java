package com.codepath.classconnect.activities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.codepath.classconnect.R;
import com.codepath.classconnect.models.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvNotes) TextView tvNotes;
    @BindView(R.id.tvLocation) TextView tvLocation;
    @BindView(R.id.flMapContainer) FrameLayout flMapContainer;

    private LatLng coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.bind(this);

        // TODO: Get from intent
        String eventId = "TebPDG6jCH";

        Event.findByObjectId(eventId, new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if (e == null) {
                    coordinates = getLocationFromAddress(getApplicationContext(), event.getLocation());
                    if (coordinates != null) {
                        flMapContainer.setVisibility(View.VISIBLE);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(EventDetailsActivity.this);
                    }
                    else {
                        flMapContainer.setVisibility(View.GONE);
                    }
                    tvName.setText(event.getName());
                    tvTime.setText(formatTime(event));
                    tvNotes.setText(event.getNotes());
                    tvLocation.setText(event.getLocation());
                    if (TextUtils.isEmpty(event.getNotes())) {
                        tvNotes.setVisibility(View.GONE);
                    }
                    else {
                        tvNotes.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private String formatTime(Event event) {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, MMM dd, hh:mm a");
        Date startDate = event.getStartTime();
        String start = fmt.format(startDate);
        Date endDate = event.getEndTime();
        String end;
        if (isSameDay(startDate, endDate)) {
            fmt = new SimpleDateFormat("hh:mm a");
            end = fmt.format(endDate);
        }
        else {
            end = fmt.format(endDate);
        }
        return String.format("%s to %s", start, end);
    }

    private boolean isSameDay(Date start, Date end) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        return (startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) && startCal.get(Calendar.DAY_OF_YEAR) == endCal.get(Calendar.DAY_OF_YEAR));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14.0f));
        map.addMarker(new MarkerOptions().position(coordinates).title("Event Location"));
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        try {
            List<Address> address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            return new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
