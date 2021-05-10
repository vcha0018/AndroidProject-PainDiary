package com.monash.paindiary.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.adapter.RecyclerViewAdapter;
import com.monash.paindiary.databinding.FragmentPainRecordviewBinding;
import com.monash.paindiary.entity.PainRecord;
import com.monash.paindiary.enums.NavigationItem;
import com.monash.paindiary.viewmodel.PainRecordViewModel;

import java.util.List;
import java.util.Objects;

public class PainRecordViewFragment extends Fragment {
    private FragmentPainRecordviewBinding binding;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    public PainRecordViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPainRecordviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((AppActivity) requireActivity()).ManualSelectNavigationItem(NavigationItem.RecordView);
//        return super.onCreateView(inflater, container, savedInstanceState);

        binding.btnCreateNewEntry.setOnClickListener(this::btnCreateNewEntryOnClicked);
        //MaterialCardView materialCardView = getActivity().findViewById(R.id.pain_record_card_view);
        //materialCardView.setOnClickListener(this::materialCardViewOnClicked);

        PainRecordViewModel viewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        viewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                recyclerViewAdapter = new RecyclerViewAdapter(painRecords);
                binding.recyclerViewPainRecords.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerViewPainRecords.setAdapter(recyclerViewAdapter);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void materialCardViewOnClicked(View view) {
        int uid = Integer.parseInt(((TextView) view.findViewById(R.id.text_hidden_record_id)).getText().toString());
        Bundle bundle = new Bundle();
        bundle.putInt("uid", uid);
        Navigation.findNavController(getView()).navigate(
                R.id.nav_pain_data_entry_fragment,
                bundle,
                new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_up)
                        .setExitAnim(R.anim.fade_out)
                        .setPopExitAnim(R.anim.slide_down)
                        .build()
        );
    }

    private void btnCreateNewEntryOnClicked(View view) {
        Navigation.findNavController(view).navigate(
                R.id.nav_pain_data_entry_fragment,
                null,
                new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_up)
                        .setExitAnim(R.anim.fade_out)
                        .setPopExitAnim(R.anim.slide_down)
                        .build());
    }
}
