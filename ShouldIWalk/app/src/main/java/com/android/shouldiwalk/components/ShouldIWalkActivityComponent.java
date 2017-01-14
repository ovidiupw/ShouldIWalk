package com.android.shouldiwalk.components;

import com.android.shouldiwalk.activities.ShouldIWalkActivity;
import com.android.shouldiwalk.modules.AlertDialogModule;
import com.android.shouldiwalk.modules.ApplicationModule;
import com.android.shouldiwalk.modules.ShouldIWalkActivityModule;

import dagger.Component;

@Component(modules = {
        ApplicationModule.class,
        ShouldIWalkActivityModule.class,
        AlertDialogModule.class
})
public interface ShouldIWalkActivityComponent {
    void inject(ShouldIWalkActivity shouldIWalkActivity);
}
