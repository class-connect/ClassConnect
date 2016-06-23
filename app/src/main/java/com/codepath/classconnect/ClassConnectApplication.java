package com.codepath.classconnect;

import android.app.Application;

import com.codepath.classconnect.models.AppUser;
import com.codepath.classconnect.models.Klass;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by ssunda1 on 6/20/16.
 */
public class ClassConnectApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(AppUser.class);
        ParseObject.registerSubclass(Klass.class);

        Parse.enableLocalDatastore(getApplicationContext());

        // initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("classconnect")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://classconnect.herokuapp.com/parse/")
                .build());
    }
}
