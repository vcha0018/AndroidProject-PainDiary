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

import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.apis.weather.RetrofitClient;
import com.monash.paindiary.apis.weather.RetrofitInterface;
import com.monash.paindiary.apis.weather.WeatherResponse;
import com.monash.paindiary.databinding.FragmentHomeBinding;
import com.monash.paindiary.workmanagers.WeatherFetchWork;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String API_KEY = "947c6e809cdbe01fcf3f6d54451666b3";
    private static final String latitude = "-37.9139";
    private static final String longitude = "145.1326";
    private static final String units = "metric";
    private static RetrofitInterface retrofitInterface;
    private static WeatherResponse weatherData = null;
    private static Date lastFetched;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        retrofitInterface = RetrofitClient.getRetrofitService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setWeatherData(false);

        return view;
    }

    private void setWeatherData(boolean forceRefresh) {
        if (weatherData == null
                || forceRefresh
                || TimeUnit.HOURS.convert(new Date().getTime() - lastFetched.getTime(), TimeUnit.MILLISECONDS) >= 1) {
            getWeatherData();
        } else {
            binding.textTemperature.setText(String.valueOf(weatherData.getMain().getTemp()));
            binding.textHumidity.setText(String.valueOf(weatherData.getMain().getHumidity()));
            binding.textPressure.setText(String.valueOf(weatherData.getMain().getPressure()));
        }
    }

    public void getWeatherData() {
        Call<WeatherResponse> weatherResponseCallAsync = retrofitInterface.weatherCall(latitude, longitude, units, API_KEY);
        weatherResponseCallAsync.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    weatherData = response.body();
                    lastFetched = new Date();
                    getActivity().runOnUiThread(() -> {
                        binding.textTemperature.setText(String.valueOf(weatherData.getMain().getTemp()));
                        binding.textHumidity.setText(String.valueOf(weatherData.getMain().getHumidity()));
                        binding.textPressure.setText(String.valueOf(weatherData.getMain().getPressure()));
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
