package com.craig.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather?appid=198d6ad2b20c00f4bece6567db467ff0&units=Imperial")
    Call<OpenWeatherMap> getWeatherWithLocation(@Query("lat") double lat, @Query("lon") double lon);

    @GET("weather?appid=198d6ad2b20c00f4bece6567db467ff0&units=Imperial")
    Call<OpenWeatherMap> getWeatherWithCityName(@Query("q") String name);
}
