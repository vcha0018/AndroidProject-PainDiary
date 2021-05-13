package com.monash.paindiary.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.apis.weather.RetrofitClient;
import com.monash.paindiary.apis.weather.WeatherResponse;
import com.monash.paindiary.databinding.FragmentPainDataEntryBinding;
import com.monash.paindiary.entity.PainRecord;
import com.monash.paindiary.enums.NavigationItem;
import com.monash.paindiary.helper.Converters;
import com.monash.paindiary.helper.UserInfo;
import com.monash.paindiary.helper.WeatherInfo;
import com.monash.paindiary.viewmodel.PainRecordViewModel;

import java.sql.Struct;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PainDataEntryFragment extends Fragment {
    private FragmentPainDataEntryBinding binding;
    private PainRecordViewModel viewModel;
    private int uid = -1;
    private long last_inserted_timestamp;

    public PainDataEntryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("INFO", "onCreateView");
        //Initialize vars
        binding = FragmentPainDataEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(getActivity()).get(PainRecordViewModel.class);
        binding.btnEdit.setEnabled(false);
        binding.textSliderIntensityValue.setText(String.valueOf((int) binding.sliderIntensityLevel.getValue()));

        ((AppActivity) requireActivity()).ManualSelectNavigationItem(NavigationItem.DataEntry);

        binding.btnSave.setOnClickListener(this::btnSaveOnClick);
        binding.btnEdit.setOnClickListener(this::btnEditOnClicked);
        binding.painAreaChipGroup.setOnCheckedChangeListener(this::painAreaChipGroupOnCheckedChange);
        binding.sliderIntensityLevel.addOnChangeListener(this::sliderIntensityLevelOnChanged);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && !getArguments().isEmpty()) {
            uid = getArguments().getInt("uid");
            if (uid >= 0) {
                fillUIControls(uid);
            }
        }
    }

    @Override
    public void onDestroyView() {
        Log.e("INFO", "onDestroyView");
        super.onDestroyView();
        binding = null;
    }

    private void fillUIControls(int uid) {
        CompletableFuture<PainRecord> painRecordCompletableFuture = viewModel.findRecordByID(uid);
        painRecordCompletableFuture.thenApply(painRecord -> {
            if (painRecord != null) {
                binding.sliderIntensityLevel.setValue(painRecord.getPainIntensityLevel());
                int painAreaId = getChipByName(painRecord.getPainArea());
                if (painAreaId > 0)
                    binding.painAreaChipGroup.check(painAreaId);
                int moodButtonId = getMoodButtonByName(painRecord.getMood());
                if (moodButtonId > 0) {
                    binding.btnMoodGroup.check(moodButtonId);
                    if (painRecord.getMood().toLowerCase().contains("good"))
                        binding.horizontalScrollButtonToggleGroup.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
                binding.editStepCount.setText(String.valueOf(painRecord.getStepCount()));
            }
            return painRecord;
        });
    }

    private void sliderIntensityLevelOnChanged(Slider slider, float value, boolean fromUser) {
        binding.textSliderIntensityValue.setText(String.valueOf((int) value));
    }

    private void painAreaChipGroupOnCheckedChange(@NonNull ChipGroup chipGroup, int checkedId) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setTextColor(getResources().getColor(
                    chip.isChecked() ? R.color.teal_900 : R.color.grey_900, null));
        }
    }

    private void setVisibilityOfUI(boolean isEnabled, @NonNull ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setEnabled(isEnabled);
            if (child instanceof ViewGroup) {
                setVisibilityOfUI(isEnabled, (ViewGroup) child);
            }
        }
        binding.btnEdit.setEnabled(!isEnabled);
        if (!isEnabled) {
            binding.editStepCount.setTextColor(Color.GRAY);
            for (int i = 0; i < binding.painAreaChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.painAreaChipGroup.getChildAt(i);
                chip.setTextColor(getResources().getColor(
                        chip.isChecked() ? R.color.teal_900 : R.color.grey_500, null));
            }
        } else {
            binding.editStepCount.setTextColor(Color.BLACK);
            for (int i = 0; i < binding.painAreaChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.painAreaChipGroup.getChildAt(i);
                chip.setTextColor(getResources().getColor(
                        chip.isChecked() ? R.color.teal_900 : R.color.grey_900, null));
            }
        }
    }

    private int getChipByName(@NonNull String chipName) {
        for (int i = 0; i < binding.painAreaChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.painAreaChipGroup.getChildAt(i);
            if (chip.getText().toString().toLowerCase().equals(chipName))
                return chip.getId();
        }
        return -1;
    }

    private int getMoodButtonByName(@NonNull String moodName) {
        switch (moodName.toLowerCase()) {
            case "very low":
                return binding.btnMoodVeryLow.getId();
            case "low":
                return binding.btnMoodLow.getId();
            case "average":
                return binding.btnMoodAverage.getId();
            case "good":
                return binding.btnMoodGood.getId();
            case "very good":
                return binding.btnMoodVeryGood.getId();
            default:
                return -1;
        }
    }

    // Save pain data entry with/without weather information.
    // If weather info is not up to date then it will make a call to weather api and update weather information first then save pain data entry.
    // If weather call fails then it saves pain data entry with default weather information.
    private void btnSaveOnClick(View view) {
        setVisibilityOfUI(false, binding.mainDataEntryLayout);
        int intensityLevel = Math.round(binding.sliderIntensityLevel.getValue());
        String painArea = ((Chip) binding.painAreaChipGroup.findViewById(binding.painAreaChipGroup.getCheckedChipId())).getText().toString();
        String mood = ((Button) binding.btnMoodGroup.findViewById(binding.btnMoodGroup.getCheckedButtonId())).getText().toString();
        int stepCount = Integer.parseInt(binding.editStepCount.getText().toString());
        if (WeatherInfo.getInstance() == null
                || TimeUnit.HOURS.convert(new Date().getTime() - WeatherInfo.lastFetched.getTime(), TimeUnit.MILLISECONDS) >= 1) {
            WeatherInfo.clearInstance();
            Call<WeatherResponse> weatherResponseCallAsync = RetrofitClient
                    .getRetrofitService().weatherCall(
                            WeatherInfo.latitude,
                            WeatherInfo.longitude,
                            WeatherInfo.units,
                            WeatherInfo.API_KEY);
            weatherResponseCallAsync.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        WeatherInfo.setInstance(
                                response.body().getMain().getTemp(),
                                response.body().getMain().getHumidity(),
                                response.body().getMain().getPressure(),
                                true);
                        WeatherInfo.lastFetched = new Date();
                        SaveEntry(intensityLevel, painArea, mood, stepCount, true);
                    } else {
                        Log.i("ERROR", "Weather call failed");
                        SaveEntry(intensityLevel, painArea, mood, stepCount, false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            SaveEntry(intensityLevel, painArea, mood, stepCount, true);
        }
    }

    private void SaveEntry(int intensityLevel, String painArea, String mood, int stepCount, boolean withWeather) {
        last_inserted_timestamp = Converters.dateToTimestamp(new Date());
        try {
            PainRecord newPainRecord = new PainRecord(
                    UserInfo.getUserEmail(),
                    last_inserted_timestamp,
                    intensityLevel,
                    painArea,
                    mood,
                    10000,
                    stepCount,
                    WeatherInfo.getTemperature(),
                    WeatherInfo.getHumidity(),
                    WeatherInfo.getPressure()
            );
            if (uid < 0)
                viewModel.insert(newPainRecord);
            else {
                newPainRecord.setUid(uid);
                viewModel.update(newPainRecord);
            }
            // TODO Check if we need runOnUiThread for Toast through https://edstem.org/courses/5305/discussion/468294
            getActivity().runOnUiThread(() -> {
                if (withWeather)
                    Toast.makeText(getContext(), "Pain entry saved successfully.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Pain entry saved without weather information.", Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            Log.i("EXCEPTION", e.getMessage());
        }
//        Navigation.findNavController(getView()).popBackStack();
    }

    private void btnEditOnClicked(View view) {
        setVisibilityOfUI(true, binding.mainDataEntryLayout);
        if (uid < 0) {
            CompletableFuture<PainRecord> painRecordCompletableFuture = viewModel.findRecordByTimestamp(last_inserted_timestamp);
            painRecordCompletableFuture.thenApply(painRecord -> {
                uid = painRecord.getUid();
                return painRecord;
            });
        }
    }

}
