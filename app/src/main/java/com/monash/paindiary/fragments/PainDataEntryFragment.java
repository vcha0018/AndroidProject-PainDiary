package com.monash.paindiary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.databinding.FragmentPainDataEntryBinding;
import com.monash.paindiary.entity.PainRecord;
import com.monash.paindiary.enums.NavigationItem;
import com.monash.paindiary.helper.Converters;
import com.monash.paindiary.viewmodel.PainRecordViewModel;

import java.util.Date;

public class PainDataEntryFragment extends Fragment {
    private FragmentPainDataEntryBinding binding;

    public PainDataEntryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPainDataEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((AppActivity) requireActivity()).ManualSelectNavigationItem(NavigationItem.DataEntry);
//        return super.onCreateView(inflater, container, savedInstanceState);

        PainRecordViewModel viewModel = new ViewModelProvider(getActivity()).get(PainRecordViewModel.class);
        binding.btnSave.setOnClickListener(v -> {
            PainRecord newPainRecord = new PainRecord(
                    "vivek",
                    Converters.dateToTimestamp(new Date()),
                    Math.round(binding.sliderIntensityLevel.getValue()),
                    ((Chip) binding.painAreaChipGroup.findViewById(binding.painAreaChipGroup.getCheckedChipId())).getText().toString(),
                    ((Button) binding.btnMoodGroup.findViewById(binding.btnMoodGroup.getCheckedButtonId())).getText().toString(),
                    Integer.parseInt(binding.editStepCount.getText().toString()),
                    0,
                    0,
                    0
            );
            viewModel.insert(newPainRecord);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
