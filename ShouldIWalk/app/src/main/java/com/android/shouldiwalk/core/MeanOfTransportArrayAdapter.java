package com.android.shouldiwalk.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.model.MeanOfTransport;

import java.util.List;

public class MeanOfTransportArrayAdapter extends ArrayAdapter<MeanOfTransport> {
    private static final String CLASS_TAG = MeanOfTransportArrayAdapter.class.getCanonicalName() + "-TAG";

    private final int rootItemViewResourceId;
    private Integer activeItemPosition;

    public MeanOfTransportArrayAdapter(
            Context context, int rootItemViewResourceId, List<MeanOfTransport> allMeansOfTransport) {
        super(context, rootItemViewResourceId, allMeansOfTransport);
        this.rootItemViewResourceId = rootItemViewResourceId;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView meanOfTransportTitleView;
        View rootItemView = convertView;

        if (rootItemView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootItemView = inflater.inflate(rootItemViewResourceId, parent, false);
            meanOfTransportTitleView = (TextView) rootItemView.findViewById(R.id.meanOfTransportItemTitle);
            rootItemView.setTag(meanOfTransportTitleView);
        } else {
            meanOfTransportTitleView = (TextView) rootItemView.getTag();
        }

        RadioButton radioButton = (RadioButton) rootItemView.findViewById(R.id.meanOfTransportRadioButton);

        if (activeItemPosition != null && position == activeItemPosition) {
            rootItemView.setBackgroundColor(getContext().getColor(R.color.colorPrimary));
            meanOfTransportTitleView.setTextColor(getContext().getColor(R.color.listItemTitleColorLight));
            radioButton.setChecked(true);
        } else {
            rootItemView.setBackgroundColor(getContext().getColor(R.color.cardColor));
            meanOfTransportTitleView.setTextColor(getContext().getColor(R.color.listItemTitleColorDark));
            radioButton.setChecked(false);
        }

        MeanOfTransport meanOfTransport = getItem(position);
        if (meanOfTransport != null) {
            meanOfTransportTitleView.setText(meanOfTransport.toString());
        }

        return rootItemView;
    }

    @Override
    public int getCount() {
        return MeanOfTransport.values().length;
    }

    public int getActiveItemPosition() {
        return activeItemPosition;
    }

    public void setActiveItemPosition(int position) {
        this.activeItemPosition = position;
    }
}
