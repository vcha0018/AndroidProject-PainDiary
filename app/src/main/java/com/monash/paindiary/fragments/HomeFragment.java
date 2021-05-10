package com.monash.paindiary.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.apis.weather.RetrofitClient;
import com.monash.paindiary.apis.weather.RetrofitInterface;
import com.monash.paindiary.apis.weather.WeatherResponse;
import com.monash.paindiary.databinding.FragmentHomeBinding;
import com.monash.paindiary.helper.WeatherInfo;
import com.monash.paindiary.workmanagers.WeatherFetchWork;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    //    private static Date lastFetched;
    private FragmentHomeBinding binding;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnCreateNewEntry.setOnClickListener(this::btnCreateNewEntryOnClicked);

        setWeatherData(false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    private void setWeatherData(boolean forceRefresh) {
        if (WeatherInfo.getInstance() == null
                || forceRefresh
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
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful()) {
                        WeatherInfo.setInstance(
                                response.body().getMain().getTemp(),
                                response.body().getMain().getHumidity(),
                                response.body().getMain().getPressure(),
                                true);
                        WeatherInfo.lastFetched = new Date();
                        getActivity().runOnUiThread(() -> {
                            binding.textTemperature.setText(String.valueOf(WeatherInfo.getTemperature()));
                            binding.textHumidity.setText(String.valueOf(WeatherInfo.getHumidity()));
                            binding.textPressure.setText(String.valueOf(WeatherInfo.getPressure()));
                        });
                    } else {
                        Log.i("ERROR", "Weather call failed");
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.textTemperature.setText(String.valueOf(WeatherInfo.getTemperature()));
            binding.textHumidity.setText(String.valueOf(WeatherInfo.getHumidity()));
            binding.textPressure.setText(String.valueOf(WeatherInfo.getPressure()));
        }
    }


}
