package com.monash.paindiary.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "PAINRECORD")
public class PainRecord {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "user_email")
    @NonNull
    public String userEmail;

    @ColumnInfo(name = "timestamp")
    public double datetime;

    @ColumnInfo(name = "pain_intensity")
    public int painIntensityLevel;

    @ColumnInfo(name = "pain_area")
    @NonNull
    public String painArea;

    @ColumnInfo(name = "mood")
    @NonNull
    public String mood;

    @ColumnInfo(name = "step_count")
    public long stepCount;

    @ColumnInfo(name = "day_temperature")
    public float temperature;

    @ColumnInfo(name = "day_humidity")
    public float humidity;

    @ColumnInfo(name = "day_pressure")
    public float pressure;

    public PainRecord(@NonNull String userEmail, double datetime, int painIntensityLevel,
                      @NonNull String painArea, @NonNull String mood, long stepCount,
                      float temperature, float humidity, float pressure) {
        this.userEmail = userEmail;
        this.datetime = datetime;
        this.painIntensityLevel = painIntensityLevel;
        this.painArea = painArea;
        this.mood = mood;
        this.stepCount = stepCount;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }
}
