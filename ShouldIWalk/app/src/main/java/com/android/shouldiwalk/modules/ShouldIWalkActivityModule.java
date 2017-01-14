package com.android.shouldiwalk.modules;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;

import dagger.Module;
import dagger.Provides;

@Module
public class ShouldIWalkActivityModule {

    @Provides
    public Handler provideHandler() {
        return new Handler();
    }

    @Provides
    public ConnectivityManager provideConnectivityManager(Application application) {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
