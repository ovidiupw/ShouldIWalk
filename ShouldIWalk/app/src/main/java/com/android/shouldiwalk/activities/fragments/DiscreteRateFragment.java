package com.android.shouldiwalk.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.exceptions.InvalidArgumentException;

public class DiscreteRateFragment extends ShouldIWalkFragment {
    public static final String INITIAL_SEEKBAR_VALUE = "InitialSeekbarValue";
    public static final String MAX_SEEKBAR_VALUE = "MaxSeekbarValue";
    public static final String SEEKBAR_TITLE = "SeekbarTitle";
    public static final String SEEKBAR_SUBTITLE = "SeekbarSubtitle";
    public static final String SEEKBAR_UNIT_DESCRIPTION = "SeekbarUnitDescription";

    private static final String CLASS_TAG = DiscreteRateFragment.class.getCanonicalName() + "-TAG";

    private int initialSeekBarValue;
    private int maxSeekBarValue;
    private String seekBarTitle;
    private String seekBarSubtitle;
    private String seekBarUnitDescription;

    private OnDiscreteRateSeekBarChangeListener onSeekBarChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initializeFragmentInstanceFromArguments();
        } else {
            initializeFragmentInstanceFromBundle(savedInstanceState);
        }

        View rootView = inflater.inflate(R.layout.discrete_rate_fragment, container, false);

        initializeSeekBarTitle(rootView);
        initializeSeekBarSubtitle(rootView);
        initializeSeekBar(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INITIAL_SEEKBAR_VALUE, this.initialSeekBarValue);
        outState.putInt(MAX_SEEKBAR_VALUE, this.maxSeekBarValue);
        outState.putString(SEEKBAR_TITLE, this.seekBarTitle);
        outState.putString(SEEKBAR_SUBTITLE, this.seekBarSubtitle);
        outState.putString(SEEKBAR_UNIT_DESCRIPTION, this.seekBarUnitDescription);
    }

    private void initializeFragmentInstanceFromArguments() {
        this.fragmentId = getArguments().getString(FRAGMENT_ID);
        if (this.fragmentId == null) {
            throw new InvalidArgumentException("The fragment id must be supplied as argument!");
        }

        this.initialSeekBarValue = getArguments().getInt(INITIAL_SEEKBAR_VALUE);
        if (this.initialSeekBarValue < 0) {
            throw new InvalidArgumentException("Initial seekbar value must be positive or 0!");
        }

        this.maxSeekBarValue = getArguments().getInt(MAX_SEEKBAR_VALUE);
        if (this.maxSeekBarValue < 0) {
            throw new InvalidArgumentException("Maximum seekbar value must be positive or 0!");
        }

        if (this.initialSeekBarValue > this.maxSeekBarValue) {
            throw new InvalidArgumentException("The initial seekbar value must not exceed the maximum seekbar value!");
        }

        this.seekBarTitle = getArguments().getString(SEEKBAR_TITLE);
        if (this.seekBarTitle == null) {
            throw new InvalidArgumentException("The seekbar title must be set!");
        }

        this.seekBarSubtitle = getArguments().getString(SEEKBAR_SUBTITLE);
        if (this.seekBarSubtitle == null) {
            throw new InvalidArgumentException("The seekbar subtitle must be set!");
        }

        this.seekBarUnitDescription = getArguments().getString(SEEKBAR_UNIT_DESCRIPTION);
        if (this.seekBarUnitDescription == null) {
            throw new InvalidArgumentException("The seekbar unit description must be set, but CAN be the empty string!");
        }
    }

    private void initializeFragmentInstanceFromBundle(Bundle savedInstanceState) {
        this.fragmentId = savedInstanceState.getString(FRAGMENT_ID);
        this.initialSeekBarValue = savedInstanceState.getInt(INITIAL_SEEKBAR_VALUE);
        this.maxSeekBarValue = savedInstanceState.getInt(MAX_SEEKBAR_VALUE);
        this.seekBarTitle = savedInstanceState.getString(SEEKBAR_TITLE);
        this.seekBarSubtitle = savedInstanceState.getString(SEEKBAR_SUBTITLE);
        this.seekBarUnitDescription = savedInstanceState.getString(SEEKBAR_UNIT_DESCRIPTION);
    }

    private void initializeSeekBarTitle(View parent) {
        TextView seekBarTitleTextView = (TextView) parent.findViewById(R.id.discreteSeekBarTitle);
        if (seekBarTitleTextView == null) {
            return;
        }
        seekBarTitleTextView.setText(seekBarTitle);
    }

    private void initializeSeekBarSubtitle(View parent) {
        TextView seekBarSubtitleTextView = (TextView) parent.findViewById(R.id.discreteSeekBarSubTitle);
        if (seekBarSubtitleTextView == null) {
            return;
        }
        seekBarSubtitleTextView.setText(seekBarSubtitle);
    }

    private void initializeSeekBar(final View parent) {
        SeekBar seekBar = (SeekBar) parent.findViewById(R.id.discreteSeekBar);
        if (seekBar == null) {
            return;
        }
        final TextView seekBarValueBox = (TextView) parent.findViewById(R.id.discreteSeekBarProgressBox);
        if (seekBarValueBox == null) {
            return;
        }
        final TextView seekBarUnitDescription = (TextView) parent.findViewById(R.id.discreteSeekBarProgressBoxDescription);
        if (seekBarUnitDescription == null) {
            return;
        }

        seekBar.setMax(this.maxSeekBarValue);
        seekBar.setProgress(this.initialSeekBarValue);
        seekBarValueBox.setText(String.valueOf(this.initialSeekBarValue));
        seekBarUnitDescription.setText(this.seekBarUnitDescription);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                initialSeekBarValue = progress;
                onSeekBarChangeListener.onSeekBarChange(fragmentId, progress);
                seekBarValueBox.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    public interface OnDiscreteRateSeekBarChangeListener {
        void onSeekBarChange(String sourceFragmentId, int seekBarPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onSeekBarChangeListener = (DiscreteRateFragment.OnDiscreteRateSeekBarChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDiscreteRateSeekBarChangeListener");
        }
    }
}
