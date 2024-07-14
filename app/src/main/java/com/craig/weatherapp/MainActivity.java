package com.craig.weatherapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.location.LocationListenerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView cityName, temperature, weatherCondition, humidity, maxTemp, minTemp, pressure, wind;
    private ImageView imageView;
    private FloatingActionButton fab;

    LocationManager locationManager;
    LocationListener locationListener;

    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        cityName = findViewById(R.id.textViewCityName);
        temperature = findViewById(R.id.textViewTemp);
        weatherCondition = findViewById(R.id.textWeatherCondition);
        humidity = findViewById(R.id.textViewHumidity);
        maxTemp = findViewById(R.id.textViewMaxTemp);
        minTemp = findViewById(R.id.textViewMinTemp);
        pressure = findViewById(R.id.textViewPressure);
        wind = findViewById(R.id.textViewWind);
        imageView = findViewById(R.id.imageView);

        fab.setOnClickListener(view -> {
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));

        });
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //gets the user's location
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                Log.e ("lat :" , String.valueOf(lat));
                Log.e ("lon :" , String.valueOf(lon));
                getWeatherData(lat,lon);
            }
        };
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){ //needs to be added for permission: android.Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},  1); //needs to be added for permission: android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && permissions.length >0
                && ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }
    public void getWeatherData(double lat, double lon){
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(lat, lon);

        call.enqueue(new Callback<OpenWeatherMap>() { //allows us to retrieve the data asynchronously in the background.
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                cityName.setText(response.body().getName()+" , "+response.body().getSys().getCountry());
                temperature.setText(response.body().getMain().getTemp()+" °F");  //getMain() is from the Main Class that contains the method to get the emp
                weatherCondition.setText(response.body().getWeather().get(0).getDescription());
                humidity.setText(" : "+response.body().getMain().getHumidity()+"%");
                maxTemp.setText(" : "+response.body().getMain().getTempMax()+" °F");
                minTemp.setText(" : "+response.body().getMain().getTempMin()+" °F");
                pressure.setText(" : "+response.body().getMain().getPressure()+" hPa");
                wind.setText(" : "+response.body().getWind().getSpeed()+" m/s");//getWind() is from the Main Class that contains the method to get the Wind

                String iconCode = response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/"+iconCode+"@2x.png")
                        .placeholder(R.drawable.weather_image)
                        .into(imageView);
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "FAILED TO GET LOCATION", Toast.LENGTH_LONG).show();
            }
        });
    }
}