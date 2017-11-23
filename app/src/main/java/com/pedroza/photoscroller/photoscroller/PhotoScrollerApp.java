package com.pedroza.photoscroller.photoscroller;

import android.app.Activity;
import android.app.Application;

import com.pedroza.photoscroller.photoscroller.di.AppComponent;
import com.pedroza.photoscroller.photoscroller.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;



public class PhotoScrollerApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> mDispatchingActivityInjector;

    private AppComponent mAppComponent;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mDispatchingActivityInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getOrCreateApplicationComponent();
    }

    public AppComponent getOrCreateApplicationComponent() {

        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .application(this)
                    .build();
            mAppComponent.inject(this);
        }
        return mAppComponent;
    }
}
