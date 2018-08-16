package com.android.bojan.bojanweather.view.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Create by bojan
 * on 2018/8/16
 */
public class BojanWeatherApplication extends Application {
    private static String sCacheDir;
    private static Context sContext;
    private static BojanWeatherApplication sBojanWeatherApplication;

    public static String getAppCacheDir() {
        return sCacheDir;
    }

    public static Context getContext() {
        return sContext;
    }

    public static BojanWeatherApplication getInstance() {
        return sBojanWeatherApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sBojanWeatherApplication = this;
        initCacheDir();
    }

    private void initCacheDir() {
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
