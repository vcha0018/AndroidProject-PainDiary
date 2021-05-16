package com.monash.paindiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.adapter.RecyclerViewAdapter;
import com.monash.paindiary.databinding.FragmentPainRecordviewBinding;
import com.monash.paindiary.enums.NavigationItem;
import com.monash.paindiary.viewmodel.PainRecordViewModel;

public class PainRecordViewFragment extends Fragment {
    private FragmentPainRecordviewBinding binding;
    private RecyclerViewAdapter recyclerViewAdapter;

    public PainRecordViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPainRecordviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((AppActivity) requireActivity()).ManualSelectNavigationItem(NavigationItem.RecordView);

        PainRecordViewModel viewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        viewModel.getAllPainRecords().observe(getViewLifecycleOwner(), painRecords -> {
            recyclerViewAdapter = new RecyclerViewAdapter(painRecords);
            binding.recyclerViewPainRecords.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerViewPainRecords.setAdapter(recyclerViewAdapter);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
