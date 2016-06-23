package com.codepath.classconnect.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by ssunda1 on 6/20/16.
 */
@ParseClassName("Klass")
public class Klass extends ParseObject {

    public static String KEY_NAME = "name";
    public static String KEY_START_TIME = "startTime";
    public static String KEY_END_TIME = "endTime";
    public static String KEY_DAYS_OF_WEEK = "daysOfWeek";
    public static String KEY_DESCRIPTION = "description";
    public static String KEY_TEACHER = "teacher";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getStartTime() {
        return getString(KEY_START_TIME);
    }

    public void setStartTime(String startTime) {
        put(KEY_START_TIME, startTime);
    }

    public String getEndTime() {
        return getString(KEY_END_TIME);
    }

    public void setEndTime(String endTime) {
        put(KEY_END_TIME, endTime);
    }

    public String getDaysOfWeek() {
        return getString(KEY_DAYS_OF_WEEK);
    }

    public void setDaysOfWeek(String daysOfWeek) {
        put(KEY_DAYS_OF_WEEK, daysOfWeek);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public AppUser getTeacher() {
        return (AppUser) getParseObject(KEY_TEACHER);
    }

    public void setTeacher(AppUser teacher) {
        put(KEY_TEACHER, teacher);
    }

    public String getTeacherName() {
        AppUser teacher = getTeacher();
        return teacher != null ? teacher.getName() : null;
    }

    public String getProfileUrl() {
        AppUser teacher = getTeacher();
        return teacher != null ? teacher.getProfileUrl() : null;
    }

    public static void find(String teacher, String code, FindCallback<Klass> callback) {
        ParseQuery<Klass> query = ParseQuery.getQuery(Klass.class);
        query.whereEqualTo("objectId", code);
        //query.whereEqualTo("teacher.name", teacher);
        query.include(KEY_TEACHER);
        query.findInBackground(callback);
    }

}
