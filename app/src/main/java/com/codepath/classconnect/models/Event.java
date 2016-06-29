package com.codepath.classconnect.models;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ssunda1 on 6/20/16.
 */
@ParseClassName("Event")
public class Event extends ParseObject {

    public static final String KEY_KLASS = "klass";
    public static final String KEY_USER = "user";
    public static final String KEY_NAME = "name";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_LOCATION = "location";

    public Klass getKlass() {
        return (Klass) getParseObject(KEY_KLASS);
    }

    public void setKlass(Klass klass) {
        put(KEY_KLASS, klass);
    }

    public AppUser getUser() {
        return (AppUser) getParseObject(KEY_USER);
    }

    public void setUser(AppUser user) {
        put(KEY_USER, user);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public Date getStartTime() {
        return getDate(KEY_START_TIME);
    }

    public void setStartTime(Date startTime) {
        put(KEY_START_TIME, startTime);
    }

    public Date getEndTime() {
        return getDate(KEY_END_TIME);
    }

    public void setEndTime(Date endTime) {
        put(KEY_END_TIME, endTime);
    }

    public String getNotes() {
        return getString(KEY_NOTES);
    }

    public void setNotes(String notes) {
        put(KEY_NOTES, notes);
    }

    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location) {
        put(KEY_LOCATION, location);
    }

    public String getUserName() {
        AppUser user = getUser();
        return user != null ? user.getName() : null;
    }

    public String getProfileUrl() {
        AppUser user = getUser();
        return user != null ? user.getProfileUrl() : null;
    }

    public String getEventTime() {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, MMM dd, hh:mm a");
        Date startDate = getStartTime();
        String start = fmt.format(startDate);
        Date endDate = getEndTime();
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

    public static void findByObjectId(String objectId, GetCallback<Event> callback) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(KEY_KLASS);
        query.include(KEY_USER);
        query.getInBackground(objectId, callback);
    }

    public static void findAll(Klass klass, FindCallback<Event> callback) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.whereEqualTo(KEY_KLASS, klass);
        query.include(KEY_KLASS);
        query.include(KEY_USER);
        query.orderByAscending(KEY_START_TIME);
        query.findInBackground(callback);
    }

}
