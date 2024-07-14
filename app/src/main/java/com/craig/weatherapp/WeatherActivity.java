package com.craig.weatherapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherActivity extends AppCompatActivity {
    private TextView cityWeather, temperatureWeather, condition, humidityWeather,
                     maxTempWeather, minTempWeather, pressureWeather, windWeather;
    private ImageView imageViewWeather;
    private EditText editTextCityName;
    private Button btnSearch;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        toolbar = findViewById(R.id.toolbarCity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cityWeather = findViewById(R.id.textViewCityWeather);
        temperatureWeather = findViewById(R.id.textViewTempWeather);
        condition = findViewById(R.id.textViewWeatherConditionWeather);
        humidityWeather = findViewById(R.id.textViewHumidityWeather);
        maxTempWeather = findViewById(R.id.textViewMaxTempWeather);
        minTempWeather = findViewById(R.id.textViewMinTempWeather);
        pressureWeather = findViewById(R.id.textViewPressureWeather);
        windWeather = findViewById(R.id.textViewWindSpeedWeather);
        imageViewWeather =findViewById(R.id.imageViewWeather);
        editTextCityName = findViewById(R.id.editTextCityName);
        btnSearch = findViewById(R.id.buttonSearch);

        btnSearch.setOnClickListener(view -> {
            String name = editTextCityName.getText().toString();
            getWeatherData(name);
            editTextCityName.setText(""); //clear the text field after the search
        });


    }
    public void getWeatherData(String name){
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);

        call.enqueue(new Callback<OpenWeatherMap>() { //allows us to retrieve the data asynchronously in the background.
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if(response.isSuccessful()){
                    cityWeather.setText(response.body().getName()+" , "+response.body().getSys().getCountry());
                    temperatureWeather.setText(response.body().getMain().getTemp()+" °F");  //getMain() is from the Main Class that contains the method to get the emp
                    condition.setText(response.body().getWeather().get(0).getDescription());
                    humidityWeather.setText(" : "+response.body().getMain().getHumidity()+"%");
                    maxTempWeather.setText(" : "+response.body().getMain().getTempMax()+" °F");
                    minTempWeather.setText(" : "+response.body().getMain().getTempMin()+" °F");
                    pressureWeather.setText(" : "+response.body().getMain().getPressure()+" hPa");
                    windWeather.setText(" : "+response.body().getWind().getSpeed()+" m/s");//getWind() is from the Main Class that contains the method to get the Wind

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/"+iconCode+"@2x.png")
                            .placeholder(R.drawable.weather_image)
                            .into(imageViewWeather);
                }
                else{
                    Toast.makeText(WeatherActivity.this, "CITY NOT FOUND, Please try again!!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable throwable) {

            }
        });
    }

}