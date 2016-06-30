package com.codepath.classconnect.models;

import android.text.format.DateUtils;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by ssunda1 on 6/20/16.
 */
@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String KEY_KLASS = "klass";
    public static final String KEY_USER = "user";
    public static final String KEY_BODY = "body";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_PHOTO = "photo";


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

    public String getBody() {
        return getString(KEY_BODY);
    }

    public void setBody(String body) {
        put(KEY_BODY, body);
    }

    public String getPhoto() {
        ParseFile file = (ParseFile) get(KEY_PHOTO);
        if (file != null) {
            return file.getUrl();
        }
        else {
            return null;
        }
    }

    public void setPhoto(ParseFile pFile) {
        put("photo", pFile);

    }

    public String getUserName() {
        AppUser user = getUser();
        return user != null ? user.getName() : null;
    }

    public String getProfileUrl() {
        AppUser user = getUser();
        return user != null ? user.getProfileUrl() : null;
    }

    public String getRelativeTime() {
        Date date = getCreatedAt();
        long now = System.currentTimeMillis();
        String relativeDate = DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.SECOND_IN_MILLIS).toString();
        return formatRelativeDate(relativeDate);
    }

    private String formatRelativeDate(String date) {
        String[] split = date.split(" ");
        if (split.length == 3) {
            String unit = split[1];
            if (unit.startsWith("second") || unit.startsWith("minute") || unit.startsWith("hour") || unit.startsWith("day")) {
                return split[0] + unit.charAt(0);
            }
        }
        return date;
    }

    public static void findByObjectId(String objectId, GetCallback<Message> callback) {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.include(KEY_KLASS);
        query.include(KEY_USER);
        query.getInBackground(objectId, callback);
    }

    public static void findAll(Klass klass, FindCallback<Message> callback) {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo(KEY_KLASS, klass);
        query.include(KEY_KLASS);
        query.include(KEY_USER);
        query.orderByDescending("createdAt");
        query.findInBackground(callback);
    }


}
