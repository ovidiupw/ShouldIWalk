package com.android.shouldiwalk.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.utils.DateTimePickerWidget;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickerFragment extends ShouldIWalkFragment {

    public static final String DATETIME_PICKER_DIALOG_TAG = "DateTimePicker";
    public static final String INITIAL_DATE = "InitialDate";
    public static final String DATE_PICKER_CARD_TITLE = "DatePickerCardTitle";

    private static final String CLASS_TAG = DateTimePickerFragment.class.getCanonicalName() + "-TAG";
    private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT
            = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());

    private Date currentDate;
    private String datePickerCardTitle;
    private boolean isDatePickerOpen;

    DateTimePickerFragment.OnDateChangeListener dateChangeListener;
    private View rootView;
    private DateTimePickerWidget datePickerWidget;

    public interface OnDateChangeListener {
        void onDateChange(String sourceFragmentId, Date date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dateChangeListener = (DateTimePickerFragment.OnDateChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDateChangeListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isDatePickerOpen) {
            datePickerWidget.dismiss();
            isDatePickerOpen = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentId = getArguments().getString(FRAGMENT_ID);
        this.datePickerCardTitle = getArguments().getString(DATE_PICKER_CARD_TITLE);
        this.currentDate = (Date) getArguments().getSerializable(INITIAL_DATE);

        rootView = inflater.inflate(R.layout.datetime_picker_fragment, container, false);
        initializeTextViews();
        datePickerWidget = buildDateTimePickerFragment(rootView);

        wireDatePickerButtonToDatePickerWidget(rootView, datePickerWidget);
        wireTimePickerButtonToTimePickerWidget(rootView, datePickerWidget);

        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void initializeTextViews() {
        TextView dateTextView = (TextView) rootView.findViewById(R.id.datePickerDateTextView);
        dateTextView.setText(DATE_FORMAT.format(this.currentDate));

        TextView timeTextView = (TextView) rootView.findViewById(R.id.timePickerDateTextView);
        timeTextView.setText(TIME_FORMAT.format(this.currentDate));

        TextView datePickerTitle = (TextView) rootView.findViewById(R.id.datePickerTitle);
        datePickerTitle.setText(this.datePickerCardTitle);
    }

    private void wireDatePickerButtonToDatePickerWidget(
            final View rootView, final SwitchDateTimeDialogFragment dateTimePickerFragment) {

        Button datePickerButton = (Button) rootView.findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDatePickerOpen) {
                    dateTimePickerFragment.startAtCalendarView();
                    dateTimePickerFragment.show(getFragmentManager(), DATETIME_PICKER_DIALOG_TAG);
                    isDatePickerOpen = true;
                }
            }
        });
    }

    private void wireTimePickerButtonToTimePickerWidget(
            View rootView, final SwitchDateTimeDialogFragment dateTimePickerFragment) {

        Button timePickerButton = (Button) rootView.findViewById(R.id.timePickerButton);
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDatePickerOpen) {
                    dateTimePickerFragment.startAtTimeView();
                    dateTimePickerFragment.show(getFragmentManager(), DATETIME_PICKER_DIALOG_TAG);
                    isDatePickerOpen = true;
                }
            }
        });
    }

    @NonNull
    private DateTimePickerWidget buildDateTimePickerFragment(final View rootView) {
        final DateTimePickerWidget dateTimeFragment = new DateTimePickerWidget();
        Bundle args = new Bundle();
        args.putString("LABEL", getString(R.string.label_datetime_dialog));
        args.putString("POSITIVE_BUTTON", getString(R.string.positive_button_datetime_picker));
        args.putString("NEGATIVE_BUTTON", getString(R.string.negative_button_datetime_picker));
        dateTimeFragment.setArguments(args);

        dateTimeFragment.set24HoursMode(true);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.currentDate);

        dateTimeFragment.setDefaultHourOfDay(calendar.get(Calendar.HOUR_OF_DAY));
        dateTimeFragment.setDefaultMinute(calendar.get(Calendar.MINUTE));
        dateTimeFragment.setDefaultDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateTimeFragment.setDefaultMonth(calendar.get(Calendar.MONTH));
        dateTimeFragment.setDefaultYear(calendar.get(Calendar.YEAR));

        final DateTimePickerFragment that = this;
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {

            @Override
            public void onPositiveButtonClick(Date date) {
                TextView dateTextView = (TextView) rootView.findViewById(R.id.datePickerDateTextView);
                dateTextView.setText(DATE_FORMAT.format(date));

                TextView timeTextView = (TextView) rootView.findViewById(R.id.timePickerDateTextView);
                timeTextView.setText(TIME_FORMAT.format(date));

                dateChangeListener.onDateChange(that.fragmentId, date);
                that.currentDate = date;
                isDatePickerOpen = false;

                getArguments().putSerializable(INITIAL_DATE, date);
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                isDatePickerOpen = false;
            }
        });

        dateTimeFragment.setOnDestroyViewListener(new DateTimePickerWidget.OnDestroyViewListener() {
            @Override
            public void onDestroyView() {
                isDatePickerOpen = false;
            }
        });


        return dateTimeFragment;
    }
}
