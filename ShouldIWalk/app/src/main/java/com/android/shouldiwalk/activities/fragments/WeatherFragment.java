package com.android.shouldiwalk.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.exceptions.InvalidArgumentException;
import com.android.shouldiwalk.core.model.WeatherStatus;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class WeatherFragment extends ShouldIWalkFragment {
    public static final String INITIAL_TEMPERATURE = "InitialTemperature";
    public static final String INITIAL_WEATHER_STATUS = "InitialWeatherStatus";

    private static final String CLASS_TAG = WeatherFragment.class.getCanonicalName() + "-TAG";
    private static final int TEMPERATURE_RANGE = 140;

    private OnWeatherChangeListener onWeatherChangeListener;
    private int temperature;
    private WeatherStatus weatherStatus;
    private AlertDialog.Builder weatherStatusesAlertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initializeFragmentInstanceFromArguments();
        } else {
            initializeFragmentInstanceFromBundle(savedInstanceState);
        }

        View rootView = inflater.inflate(R.layout.weather_fragment, container, false);

        initializeTemperatureSeekBar(rootView);
        initializeWeatherStatusChooserDialog(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INITIAL_TEMPERATURE, this.temperature);
        outState.putSerializable(INITIAL_WEATHER_STATUS, this.weatherStatus);
        outState.putString(FRAGMENT_ID, this.fragmentId);
    }

    private void initializeFragmentInstanceFromArguments() {
        this.fragmentId = getArguments().getString(FRAGMENT_ID);
        if (this.fragmentId == null) {
            throw new InvalidArgumentException("The fragment id must be supplied as argument!");
        }
        this.temperature = getArguments().getInt(INITIAL_TEMPERATURE);
        if (this.temperature == 0) {
            throw new InvalidArgumentException("Possibly initial temperature not set!");
        }
        try {
            this.weatherStatus = WeatherStatus.valueOf(getArguments().getString(INITIAL_WEATHER_STATUS));
        } catch(IllegalArgumentException ignored) {
            throw new InvalidArgumentException("The initial weather status for the weather fragment was not set.");
        }
    }

    private void initializeFragmentInstanceFromBundle(Bundle savedInstanceState) {
        this.fragmentId = savedInstanceState.getString(FRAGMENT_ID);
        this.temperature = savedInstanceState.getInt(INITIAL_TEMPERATURE);
        this.weatherStatus = (WeatherStatus) savedInstanceState.getSerializable(INITIAL_WEATHER_STATUS);
    }


    private void initializeTemperatureSeekBar(final View parentView) {
        SeekBar seekBar = (SeekBar) parentView.findViewById(R.id.temperatureSeekbar);
        if (seekBar == null) {
            return;
        }
        final TextView temperatureView = (TextView) parentView.findViewById(R.id.temperatureInCelsiusTextView);
        if (temperatureView == null) {
            return;
        }

        temperatureView.setText(String.valueOf(temperature));

        seekBar.setMax(TEMPERATURE_RANGE);
        seekBar.setProgress(getSeekBarProgressFromTemperature(temperature));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                temperature = getTemperatureFromSeekBarProgress(i);
                onWeatherChangeListener.onTemperatureChange(fragmentId, temperature);
                temperatureView.setText(String.valueOf(temperature));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private int getSeekBarProgressFromTemperature(int temperature) {
        if (temperature < 0) {
            return -temperature;
        } else {
            return 80 + temperature;
        }
    }

    private int getTemperatureFromSeekBarProgress(int seekBarProgress) {
        if (seekBarProgress < 80) {
            // allow 80 degrees of negative temperatures from the total TEMPERATURE_RANGE
            return 0 - 80 + seekBarProgress;
        } else {
            // allow 60 degrees of positive temperatures from the total TEMPERATURE_RANGE
            return 60 - TEMPERATURE_RANGE + seekBarProgress;
        }
    }

    private void initializeWeatherStatusChooserDialog(View parent) {
        final TextView weatherStatusDisplay = (TextView) parent.findViewById(R.id.weatherConditionDisplayTextView);
        weatherStatusDisplay.setText(weatherStatus.toString());

        weatherStatusDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final List<WeatherStatus> allWeatherStatuses = Arrays.asList(WeatherStatus.values());
                final ArrayAdapter<WeatherStatus> weatherStatusesArrayAdapter = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_single_choice,
                        allWeatherStatuses
                );

                weatherStatusesAlertDialog = new AlertDialog.Builder(getActivity());
                weatherStatusesAlertDialog.setTitle(R.string.weatherStatusesAlertDialogTitle);
                weatherStatusesAlertDialog.setPositiveButton(getString(R.string.dismissButtonOkText), null);

                weatherStatusesAlertDialog.setSingleChoiceItems(
                        weatherStatusesArrayAdapter,
                        allWeatherStatuses.indexOf(weatherStatus),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectionIndex) {
                                weatherStatus = allWeatherStatuses.get(selectionIndex);
                                onWeatherChangeListener.onWeatherStatusChange(fragmentId, weatherStatus);

                                TextView textView = (TextView) view;
                                textView.setText(weatherStatus.toString());
                            }
                        }
                );

                weatherStatusesAlertDialog.show();
            }
        });
    }

    public interface OnWeatherChangeListener {
        void onTemperatureChange(String sourceFragmentId, int temperature);

        void onWeatherStatusChange(String sourceFragmentId, WeatherStatus weatherStatus);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onWeatherChangeListener = (WeatherFragment.OnWeatherChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnWeatherChangeListener");
        }
    }
}
