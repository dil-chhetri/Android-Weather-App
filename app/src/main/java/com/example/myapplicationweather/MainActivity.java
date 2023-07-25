package com.example.myapplicationweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private LinearLayout linearLayout;

    private TextView weatherDesc;
    private TextView city;
    private TextView temp;

    private ImageView weatherIcon;
    private EditText cityEditText;
    private ImageView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        ArrayList<ForecastWeatherDataModel> forecastDataList = new ArrayList<>();


        adapter = new RecyclerViewAdapter(this, forecastDataList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        linearLayout = findViewById(R.id.linearlayoutsearch);
        weatherDesc = findViewById(R.id.weatherDesc);
        city = findViewById(R.id.cityTextView);
        temp = findViewById(R.id.temperatureTextView);
        weatherIcon = findViewById(R.id.weatherIconImageView);
        cityEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=kathmandu&appid=b31f986179635820b5d31e28a4cf9fc2";
        String forecastApiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=kathmandu&appid=b31f986179635820b5d31e28a4cf9fc2";
        fetchDataFromAPI(apiUrl);
        fetchDataFromForecastAPI(forecastApiUrl);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityEditText.getText().toString().trim();
                if (!cityName.isEmpty()) {
                    city.setText(capitalizeFirstLetter(cityName));
                    String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=b31f986179635820b5d31e28a4cf9fc2";
                    String forecastApiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=b31f986179635820b5d31e28a4cf9fc2";
                    fetchDataFromAPI(apiUrl);
                    fetchDataFromForecastAPI(forecastApiUrl);
                }
            }
        });


    }

    private void fetchDataFromAPI(String apiUrl) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonData = response;
                        jsonDecode(jsonData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("exception", error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }

    private void fetchDataFromForecastAPI(String forecastApiUrl) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, forecastApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonForecastData = response;
                        jsonForecastDecode(jsonForecastData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("exception", error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }

    public void jsonDecode(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String cityName = jsonObject.getString("name");
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            if (weatherArray.length() > 0) {
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String main = weatherObject.getString("main");
                String icon = weatherObject.getString("icon");
                Log.d("MainActivity", "Icon: " + icon);
                double temperature = jsonObject.getJSONObject("main").getDouble("temp");

                CurrentWeatherDataModel currentWeather = new CurrentWeatherDataModel(main, icon, cityName, temperature);
                updateCurrentWeatherUI(currentWeather);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public void jsonForecastDecode(String jsonForecastData) {
        ArrayList<ForecastWeatherDataModel> forecastDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonForecastData);

            JSONArray weatherForecastArray = jsonObject.getJSONArray("list");
            if (weatherForecastArray.length() > 0) {
                for (int i = 0; i < weatherForecastArray.length(); i++) {
                    JSONObject item = weatherForecastArray.getJSONObject(i);
                    String dtTxt = item.getString("dt_txt");
                    JSONArray weatherList = item.getJSONArray("weather");
                    if (weatherList.length() > 0) {
                        JSONObject weatherObject = weatherList.getJSONObject(0);
                        String icon = weatherObject.getString("icon");

                        double temperature = item.getJSONObject("main").getDouble("temp");

                        String time = formatTime(dtTxt);

                        forecastDataList.add(new ForecastWeatherDataModel(time, temperature, icon));
                    }
                }
                adapter.setPosts(forecastDataList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String formatTime(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(dateTime);
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }


    public void updateCurrentWeatherUI(CurrentWeatherDataModel weatherData) {
        weatherDesc.setText(weatherData.getMain());
        double temperatureCelsius = weatherData.getTemperature() - 273.15;
        String temperatureText = String.format("%.1f°C", temperatureCelsius);
        temp.setText(temperatureText);

        String iconUrl = "https://openweathermap.org/img/wn/" + weatherData.getIcon() + "@2x.png";
        Log.d("MainActivity", weatherData.getIcon());
        Picasso.get()
                .load(iconUrl)
                .into(weatherIcon);


    }

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
