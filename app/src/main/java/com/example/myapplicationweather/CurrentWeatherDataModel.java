package com.example.myapplicationweather;

public class CurrentWeatherDataModel {
    private String main;
    private String icon;
    private String cityName;
    private double temperature;

    public CurrentWeatherDataModel(String main, String icon, String cityName ,double temperature) {
        this.main = main;
        this.icon = icon;
        this.cityName = cityName;
        this.temperature = temperature;
    }

    public String getMain() {
        return main;
    }

    public String getIcon() {
        return icon;
    }

    public String getCityName(){
        return cityName;
    }



    public double getTemperature() {
        return temperature;
    }
}
