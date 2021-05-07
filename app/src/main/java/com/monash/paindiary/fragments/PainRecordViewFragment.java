package com.monash.paindiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.databinding.FragmentPainRecordviewBinding;
import com.monash.paindiary.enums.NavigationItem;

import java.util.Objects;

public class PainRecordViewFragment extends Fragment {
    private FragmentPainRecordviewBinding binding;

    public PainRecordViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPainRecordviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((AppActivity) requireActivity()).ManualSelectNavigationItem(NavigationItem.RecordView);
//        return super.onCreateView(inflater, container, savedInstanceState);

        binding.btnCreateNewEntry.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(
                    R.id.nav_pain_data_entry_fragment,
                    null,
                    new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_up)
                            .setExitAnim(R.anim.slide_down)
                            .build());
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
