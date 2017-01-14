package com.android.shouldiwalk.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.MeanOfTransportArrayAdapter;
import com.android.shouldiwalk.core.exceptions.InvalidArgumentException;
import com.android.shouldiwalk.core.model.MeanOfTransport;

import java.util.Arrays;
import java.util.List;

public class MeanOfTransportFragment extends ShouldIWalkFragment {

    public static final String INITIAL_MEAN_OF_TRANSPORT = "InitialMeanOfTransport";
    private static final String CLASS_TAG = MeanOfTransportFragment.class.getCanonicalName() + "-TAG";

    private OnMeanOfTransportChangeListener onMeanOfTransportChangeListener;
    private ListView meanOfTransportListView;

    private MeanOfTransport selectedMeanOfTransport;
    private int selectedListItemPosition;
    private MeanOfTransportArrayAdapter meanOfTransportArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mean_of_transport_fragment, container, false);

        if (savedInstanceState == null) {
            initializeFragmentInstanceFromArguments();
        } else {
            initializeFragmentInstanceFromBundle(savedInstanceState);
        }
        Log.i(CLASS_TAG, "Initial mean of transport for mean-of-transport fragment was " + selectedMeanOfTransport);

        List<MeanOfTransport> allMeansOfTransport = Arrays.asList(MeanOfTransport.values());

        this.meanOfTransportArrayAdapter = new MeanOfTransportArrayAdapter(
                this.getActivity(),
                R.layout.mean_of_transport_list_item,
                allMeansOfTransport
        );
        this.selectedListItemPosition = meanOfTransportArrayAdapter.getPosition(this.selectedMeanOfTransport);
        this.meanOfTransportArrayAdapter.setActiveItemPosition(this.selectedListItemPosition);

        Log.i(CLASS_TAG, "Selected list item position in onActivityCreated: " + this.selectedListItemPosition);

        this.meanOfTransportListView = (ListView) rootView.findViewById(R.id.meanOfTransportListView);
        this.meanOfTransportListView.setAdapter(this.meanOfTransportArrayAdapter);

        initializeMeanOfTransportListView();
        this.meanOfTransportListView.smoothScrollToPosition(this.selectedListItemPosition);

        return rootView;
    }

    private void initializeFragmentInstanceFromBundle(Bundle savedInstanceState) {
        this.fragmentId = savedInstanceState.getString(FRAGMENT_ID);
        this.selectedMeanOfTransport = (MeanOfTransport) savedInstanceState.getSerializable(INITIAL_MEAN_OF_TRANSPORT);
    }

    private void initializeFragmentInstanceFromArguments() {
        this.fragmentId = getArguments().getString(FRAGMENT_ID);
        if (this.fragmentId == null) {
            throw new InvalidArgumentException("The fragment id must be supplied as argument!");
        }
        this.selectedMeanOfTransport = MeanOfTransport.valueOf(getArguments().getString(INITIAL_MEAN_OF_TRANSPORT));
        if (this.selectedMeanOfTransport == null) {
            throw new InvalidArgumentException("The initial mean of transport must be supplied as argument!");
        }
    }

    private void initializeMeanOfTransportListView() {
        View footer = getActivity().getLayoutInflater().inflate(R.layout.list_view_scroll_footer, null);
        meanOfTransportListView.addFooterView(footer, null, false);

        meanOfTransportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                meanOfTransportArrayAdapter.setActiveItemPosition(position);
                meanOfTransportArrayAdapter.notifyDataSetChanged();

                selectedListItemPosition = position;
                selectedMeanOfTransport = (MeanOfTransport) parent.getItemAtPosition(position);
                onMeanOfTransportChangeListener.onMeanOfTransportChange(fragmentId, selectedMeanOfTransport);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(INITIAL_MEAN_OF_TRANSPORT, this.selectedMeanOfTransport);
        outState.putString(FRAGMENT_ID, this.fragmentId);
    }

    public interface OnMeanOfTransportChangeListener {
        void onMeanOfTransportChange(String sourceFragmentId, MeanOfTransport meanOfTransport);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onMeanOfTransportChangeListener = (OnMeanOfTransportChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMeanOfTransportChangeListener");
        }
    }
}
