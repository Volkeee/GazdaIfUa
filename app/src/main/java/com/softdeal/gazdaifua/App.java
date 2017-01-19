package com.softdeal.gazdaifua;

import android.app.Application;
import android.support.multidex.MultiDex;


/**
 * Created by Viktor on 19/01/2017.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }
}
