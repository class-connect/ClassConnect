package com.codepath.classconnect;

import android.app.Application;

import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by ssunda1 on 6/20/16.
 */
public class ClassConnectApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        ParseObject.registerSubclass(Klass.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(AppUser.class);
        */

        // initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("classconnect")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://classconnect.herokuapp.com/parse/").build());
    }
}
