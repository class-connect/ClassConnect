package com.codepath.classconnect.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
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

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setTitle(R.string.new_event);
        menu.setLogo(R.drawable.ic_back);
        menu.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");


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
                    tvTime.setText(event.getEventTime());
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

    // onBackPressed is what is called when back is hit, call `overridePendingTransition`
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    // to handle activity transitions for Up navigation add it to the onOptionsItemSelected
    // as below
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
