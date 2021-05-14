package com.monash.paindiary.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.monash.paindiary.databinding.LayoutReminderDialogBinding;

import java.sql.Timestamp;
import java.util.Calendar;

public class ReminderDialogFragment extends DialogFragment {
    private LayoutReminderDialogBinding dialogBinding;
    private int WINDOW_HEIGHT;
    private int WINDOW_WIDTH;

    public interface OnDialogResult {
        void onTimeSet(int hour, int minute);
    }
    public OnDialogResult onDialogResult;

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WINDOW_HEIGHT = displayMetrics.heightPixels;
        WINDOW_WIDTH = displayMetrics.widthPixels;

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WINDOW_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WINDOW_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogBinding = LayoutReminderDialogBinding.inflate(inflater, container, false);
        View view = dialogBinding.getRoot();

        dialogBinding.btnSet.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, dialogBinding.timePicker.getHour());
            calendar.set(Calendar.MINUTE, dialogBinding.timePicker.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long millis = calendar.getTimeInMillis();
            onDialogResult.onTimeSet(dialogBinding.timePicker.getHour(), dialogBinding.timePicker.getMinute());
            getDialog().dismiss();
        });

        dialogBinding.btnCancel.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogBinding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onDialogResult = (OnDialogResult) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e("ReminderDialogFragment", "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
