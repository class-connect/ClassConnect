package com.codepath.classconnect.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ssunda1 on 6/22/16.
 */
@ParseClassName("Student")
public class Student extends ParseObject {

    public static String KEY_FIRST_NAME = "firstName";
    public static String KEY_LAST_NAME = "lastName";

    public String getFirstName() {
        return getString(KEY_FIRST_NAME);
    }

    public void setFirstName(String name) {
        put(KEY_FIRST_NAME, name);
    }

    public String getLastName() {
        return getString(KEY_LAST_NAME);
    }

    public void setLastName(String name) {
        put(KEY_LAST_NAME, name);
    }

}
