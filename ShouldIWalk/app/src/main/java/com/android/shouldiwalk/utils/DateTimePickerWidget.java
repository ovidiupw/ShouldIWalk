package com.android.shouldiwalk.utils;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

public class DateTimePickerWidget extends SwitchDateTimeDialogFragment {
    public interface OnDestroyViewListener {
        void onDestroyView();
    }

    private OnDestroyViewListener onDestroyViewListener;

    public void setOnDestroyViewListener(OnDestroyViewListener onDestroyViewListener) {
        this.onDestroyViewListener = onDestroyViewListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            onDestroyViewListener.onDestroyView();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
