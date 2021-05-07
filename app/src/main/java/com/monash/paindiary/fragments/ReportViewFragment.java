package com.monash.paindiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.databinding.FragmentReportViewBinding;
import com.monash.paindiary.enums.NavigationItem;

public class ReportViewFragment extends Fragment {
    private FragmentReportViewBinding binding;

    public ReportViewFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((AppActivity) requireActivity()).ManualSelectNavigationItem(NavigationItem.ReportView);
//        return super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
