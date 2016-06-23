package com.codepath.classconnect.models;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by ssunda1 on 6/21/16.
 */
@ParseClassName("AppUser")
public class AppUser extends ParseObject {

    public static String KEY_ID = "userId";
    public static String KEY_NAME = "name";
    public static String KEY_PROFILE_URL = "profileUrl";
    public static String KEY_STUDENTS = "students";

    public String getUserId() {
        return getString(KEY_ID);
    }

    public void setUserId(String userId) {
        put(KEY_ID, userId);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getProfileUrl() {
        return getString(KEY_PROFILE_URL);
    }

    public void setProfileUrl(String profileUrl) {
        put(KEY_PROFILE_URL, profileUrl);
    }

    public void addStudent(Student student) {
        add(KEY_STUDENTS, student);
    }

    public List<Student> getStudents() {
        return getList(KEY_STUDENTS);
    }

    public static void findByObjectId(String objectId, GetCallback<AppUser> callback) {
        ParseQuery<AppUser> query = ParseQuery.getQuery(AppUser.class);
        query.include(KEY_STUDENTS);
        query.getInBackground(objectId, callback);
    }

    public static void findByUserId(String userId, FindCallback<AppUser> callback) {
        ParseQuery<AppUser> query = ParseQuery.getQuery(AppUser.class);
        query.whereEqualTo(KEY_ID, userId);
        query.include(KEY_STUDENTS);
        query.findInBackground(callback);
    }
}
