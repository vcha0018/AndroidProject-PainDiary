package com.monash.paindiary.workmanagers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.monash.paindiary.apis.weather.RetrofitClient;
import com.monash.paindiary.apis.weather.RetrofitInterface;
import com.monash.paindiary.apis.weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFetchWork extends Worker {
    private static final String API_KEY = "947c6e809cdbe01fcf3f6d54451666b3";
    private static final String latitude = "-37.9139";
    private static final String longitude = "145.1326";
    private static final String units = "metric";
    private static RetrofitInterface retrofitInterface;
    private static WeatherResponse weatherData = null;

    public WeatherFetchWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        fetchWeatherData();
        return Result.success();
    }

    public static WeatherResponse getWeatherResponse(boolean forceRefresh) {
        if (weatherData == null)
            fetchWeatherData();
        else if (forceRefresh) {

        }
        return weatherData;
    }

    private static void fetchWeatherData() {
        Call<WeatherResponse> weatherResponseCallAsync = RetrofitClient.getRetrofitService().weatherCall(latitude, longitude, units, API_KEY);
        weatherResponseCallAsync.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    weatherData = response.body();
                } else {
                    Log.i("ERROR", "Weather call failed");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.i("ERROR", t.getMessage());
            }
        });
    }
}
