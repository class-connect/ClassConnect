package com.codepath.classconnect.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by ssunda1 on 6/21/16.
 */
@ParseClassName("KlassRegistration")
public class KlassRegistration extends ParseObject {

    public static String KEY_KLASS = "klass";
    public static String KEY_OWNER = "owner";
    public static String KEY_TEACHER_NAME = "teacherName";
    public static String KEY_STUDENT_NAME = "studentName";

    public Klass getKlass() {
        return (Klass) getParseObject(KEY_KLASS);
    }

    public void setKlass(Klass klass) {
        put(KEY_KLASS, klass);
    }

    public AppUser getOwner() {
        return (AppUser) getParseObject(KEY_OWNER);
    }

    public void setOwner(AppUser owner) {
        put(KEY_OWNER, owner);
    }

    public String getTeacherName() {
        return getString(KEY_TEACHER_NAME);
    }

    public void setTeacherName(String name) {
        put(KEY_TEACHER_NAME, name);
    }

    public String getStudentName() {
        return getString(KEY_STUDENT_NAME);
    }

    public void setStudentName(String name) {
        put(KEY_STUDENT_NAME, name);
    }

    public static void find(Klass klass, AppUser owner, FindCallback<KlassRegistration> callback) {
        ParseQuery<KlassRegistration> query = ParseQuery.getQuery(KlassRegistration.class);
        query.whereEqualTo(KEY_KLASS, klass);
        query.whereEqualTo(KEY_OWNER, owner);
        query.include(KEY_KLASS);
        query.findInBackground(callback);
    }

    public static void findAll(AppUser owner, FindCallback<KlassRegistration> callback) {
        ParseQuery<KlassRegistration> query = ParseQuery.getQuery(KlassRegistration.class);
        query.whereEqualTo(KEY_OWNER, owner);
        query.include(KEY_KLASS);
        query.include("klass.teacher");
        query.orderByAscending("createdAt");
        query.findInBackground(callback);
    }
}
