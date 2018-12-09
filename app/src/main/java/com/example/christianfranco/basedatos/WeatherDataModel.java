package com.example.christianfranco.basedatos;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataModel {

    // TODO: Declare the member variables here
    private String mTemperature;
    private int condition;
    private String mCity;

    // TODO: Create a WeatherDataModel from a JSON:
    public static WeatherDataModel fromJSON(JSONObject jsonObject){
        try {
            WeatherDataModel weatherdata = new WeatherDataModel();
            weatherdata.mCity = jsonObject.getString("name");
            weatherdata.condition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            //  weatherdata.mIconName = updateWeatherIcon(weatherdata.condition);
            double temp = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundValue = (int) Math.rint(temp);
            weatherdata.mTemperature = (""+roundValue);
            return weatherdata;
        }
        catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    // TODO: Create getter methods for temperature, city, and icon name:
    public String getTemperature() {
        return mTemperature +"°C";
    }

    public String getCity() {
        return mCity;
    }
}