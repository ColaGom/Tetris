package com.bluewave.tetris.common;

import android.app.Application;

/**
 * Created by Developer on 2016-08-29.
 */
public class GlobalApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
    }

    private void init()
    {
        UserPref.init(this);
    }
}
