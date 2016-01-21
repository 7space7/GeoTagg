package com.ua.viktor.geotagg;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by viktor on 19.01.16.
 */
public class GeoTagApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}