package com.android.shouldiwalk.core;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.components.DaggerShouldIWalkActivityComponent;
import com.android.shouldiwalk.components.ShouldIWalkActivityComponent;
import com.android.shouldiwalk.modules.AlertDialogModule;
import com.android.shouldiwalk.modules.ApplicationModule;
import com.android.shouldiwalk.modules.ShouldIWalkActivityModule;

public class ShouldIWalkApplication extends Application {
    private ShouldIWalkActivityComponent mockShouldIWalkActivityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mockShouldIWalkActivityComponent = createComponent();
    }

    public ShouldIWalkActivityComponent getShouldIWalkActivityComponent(Activity activity) {
        ApplicationModule applicationModule = new ApplicationModule(this);

        ShouldIWalkActivityModule shouldIWalkActivityModule = new ShouldIWalkActivityModule();
        AlertDialogModule alertDialogModule = new AlertDialogModule(
                activity,
                R.string.noNetworkConnectionDialogTitle,
                R.string.noNetworkConnectionDialogMessage);

        return DaggerShouldIWalkActivityComponent
                .builder()
                .applicationModule(applicationModule)
                .shouldIWalkActivityModule(shouldIWalkActivityModule)
                .alertDialogModule(alertDialogModule)
                .build();
    }

    protected ShouldIWalkActivityComponent createComponent() {
        return getShouldIWalkActivityComponent(null);
    }

    public ShouldIWalkActivityComponent getMockShouldIWalkActivityComponent() {
        return mockShouldIWalkActivityComponent;
    }

    @VisibleForTesting
    public void setMockShouldIWalkActivityComponent(ShouldIWalkActivityComponent component) {
        this.mockShouldIWalkActivityComponent = component;
    }
}
