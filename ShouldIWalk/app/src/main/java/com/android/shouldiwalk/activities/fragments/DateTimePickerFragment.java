package com.android.shouldiwalk.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shouldiwalk.R;

import java.util.Date;

public class DateTimePickerFragment extends Fragment {

    public static final String FRAGMENT_ID = "FragmentId";
    public static final String INITIAL_DATE = "InitialDate";

    private String fragmentId;
    private Date initialDate;

    DateTimePickerFragment.OnDateChangeListener dateChangeListener;

    public interface OnDateChangeListener {
        void onDateChange(String sourceFragmentId, Date date);
        //TODO call this when updating date in current fragment
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dateChangeListener = (DateTimePickerFragment.OnDateChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.initialDate = (Date) getArguments().getSerializable(INITIAL_DATE);
        this.fragmentId = getArguments().getString(FRAGMENT_ID);

        View rootView = inflater.inflate(R.layout.datetime_picker_fragment, container, false);
        return rootView;
    }
}
