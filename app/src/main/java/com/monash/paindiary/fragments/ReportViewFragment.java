package com.monash.paindiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.adapter.ReportCollectionPagerAdapter;
import com.monash.paindiary.databinding.FragmentReportViewBinding;
import com.monash.paindiary.entity.PainRecord;
import com.monash.paindiary.enums.NavigationItem;
import com.monash.paindiary.helper.UserInfo;
import com.monash.paindiary.viewmodel.PainRecordViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ReportViewFragment extends Fragment {
    private FragmentReportViewBinding binding;

    public ReportViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((AppActivity) requireActivity()).ManualSelectNavigationItem(NavigationItem.ReportView);

        binding.viewPager2.setAdapter(new ReportCollectionPagerAdapter(this));
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.tab1);
                    break;
                case 1:
                    tab.setText(R.string.tab2);
                    break;
                case 2:
                    tab.setText(R.string.tab3);
                    break;
            }
        }).attach();

//        BuildDummyData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void BuildDummyData() {
        PainRecordViewModel viewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        viewModel.deleteAll();
        Random random = new Random();
        Date currentDate = new Date();
        String[] painAreaArray = new String[]{"back", "neck", "head", "knees", "hips", "abdomen", "elbows", "shoulders", "shins", "jaw", "facial"};
        String[] moodArray = new String[]{"very low", "low", "average", "good", "very good"};
        Float[] temps = new Float[]{11.0f, 18.3f, 30.4f, 22.1f, 9f, 20f, 15.5f};

        for (int i = 0; i < 10; i++) {
            PainRecord newPainRecord = new PainRecord(
                    UserInfo.getUserEmail(),
                    new Date(currentDate.getTime() - (i * 24 * 60 * 60 * 1000)).getTime(),
                    i,
                    painAreaArray[random.nextInt(painAreaArray.length)],
                    i == 5 ? moodArray[2] : i > 5 ? moodArray[random.nextInt(2)] : moodArray[random.nextInt(2) + 3],
                    10000,
                    i == 5 ? 8000 : i > 6 ? random.nextInt(5000) + 1000 : random.nextInt(5000) + 5000,
                    temps[random.nextInt(temps.length)],
                    random.nextInt(100),
                    random.nextInt(400) + 700
            );
            viewModel.insert(newPainRecord);
        }
    }
}
