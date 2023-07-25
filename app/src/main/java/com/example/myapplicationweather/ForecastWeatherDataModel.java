package com.example.myapplicationweather;

public class ForecastWeatherDataModel {
    private String timestamp;
    private double temperature;
    private String icon;

    public ForecastWeatherDataModel(String timestamp, double temperature, String icon) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.icon = icon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }
}