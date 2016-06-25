package com.codepath.classconnect;

import android.app.Application;

import com.codepath.classconnect.models.AppUser;
import com.codepath.classconnect.models.Event;
import com.codepath.classconnect.models.Klass;
import com.codepath.classconnect.models.KlassRegistration;
import com.codepath.classconnect.models.Message;
import com.codepath.classconnect.models.Student;
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
        ParseObject.registerSubclass(Student.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(KlassRegistration.class);

        Parse.enableLocalDatastore(getApplicationContext());

        // initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("classconnect")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://classconnect.herokuapp.com/parse/")
                .build());
    }
}
