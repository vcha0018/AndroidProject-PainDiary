package com.monash.paindiary.apis.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("coord")
    public Coord coord;
    public Sys sys;
    @SerializedName("weather")
    public List<Weather> weather;
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("rain")
    public Rain rain;
    @SerializedName("clouds")
    public Clouds clouds;
    public float dt;
    public int id;
    @SerializedName("name")
    public String name;
    public float cod;

    public Coord getCoord() {
        return coord;
    }

    public Sys getSys() {
        return sys;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Rain getRain() {
        return rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public float getDt() {
        return dt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getCod() {
        return cod;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public void setDt(float dt) {
        this.dt = dt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCod(float cod) {
        this.cod = cod;
    }
}

class Weather {
    @SerializedName("id")
    public int id;
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

class Clouds {
    @SerializedName("all")
    public float all;

    public float getAll() {
        return all;
    }

    public void setAll(float all) {
        this.all = all;
    }
}

class Rain {
    @SerializedName("3h")
    public float h3;

    public float getH3() {
        return h3;
    }

    public void setH3(float h3) {
        this.h3 = h3;
    }
}

class Wind {
    @SerializedName("speed")
    public float speed;
    @SerializedName("deg")
    public float deg;

    public float getSpeed() {
        return speed;
    }

    public float getDeg() {
        return deg;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDeg(float deg) {
        this.deg = deg;
    }
}

class Sys {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;

    public String getCountry() {
        return country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}

class Coord {
    @SerializedName("lon")
    public float lon;
    @SerializedName("lat")
    public float lat;

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }
}