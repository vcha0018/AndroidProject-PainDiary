package com.monash.paindiary.helper;

import com.monash.paindiary.apis.weather.RetrofitInterface;

import java.util.Date;

public class WeatherInfo {
    private static WeatherInfo INSTANCE;
    private float temperature = 0;
    private float humidity = 0;
    private float pressure = 0;
    private static RetrofitInterface retrofitInterface;

    public static final String API_KEY = "947c6e809cdbe01fcf3f6d54451666b3";
    public static final String latitude = "-37.9139";
    public static final String longitude = "145.1326";
    public static final String units = "metric";
    public static Date lastFetched;

    public static void setInstance(float temperature, float humidity, float pressure, boolean forceOverride) {
        if (INSTANCE == null || forceOverride)
            INSTANCE = new WeatherInfo(temperature, humidity, pressure);
    }

    public static void clearInstance() {
        INSTANCE = null;
    }

    public static WeatherInfo getInstance() {
        return INSTANCE;
    }

    private WeatherInfo(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public static float getTemperature() {
        if (INSTANCE == null)
            return 0;
        else
            return INSTANCE.temperature;
    }

    public static float getHumidity() {
        if (INSTANCE == null)
            return 0;
        else
            return INSTANCE.humidity;
    }

    public static float getPressure() {
        if (INSTANCE == null)
            return 0;
        else
            return INSTANCE.pressure;
    }
}
