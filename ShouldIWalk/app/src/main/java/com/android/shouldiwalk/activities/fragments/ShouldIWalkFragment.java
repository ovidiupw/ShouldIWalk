package com.android.shouldiwalk.activities.fragments;

import android.support.v4.app.Fragment;

public abstract class ShouldIWalkFragment extends Fragment {
    public static final String FRAGMENT_ID = "FragmentId";
    protected String fragmentId;

    public String getFragmentId() {
        return fragmentId;
    }
}
