package com.zhirova.alina.presentation.application;


import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.util.Log;

import java.util.concurrent.TimeUnit;


public class CityApplication extends Application implements LifecycleObserver {

    private final String TAG = CityApplication.class.getSimpleName();
    public static boolean firstLoading = true;


    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Log.e(TAG, "ERROR!");
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onEnterForeground() {
        firstLoading = true;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onEnterBackground() {
    }


}
