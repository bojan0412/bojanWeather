package com.android.bojan.bojanweather.view.model.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Create by bojan
 * on 2018/8/16
 */
public interface WeatherApiInterface {
    String HOST = "https://free-api.heweather.com/v5/";

    @GET("weather")
    Observable<WeatherBean> mWeatherAIP(@Query("city") String city, @Query("key") String key);
}
