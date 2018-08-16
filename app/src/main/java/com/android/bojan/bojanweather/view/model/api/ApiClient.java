package com.android.bojan.bojanweather.view.model.api;

import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;
import com.android.bojan.bojanweather.view.model.database.DBConfig;
import com.android.bojan.bojanweather.view.util.RxUtil;
import com.android.bojan.bojanweather.view.util.Util;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by bojan
 * on 2018/8/16
 */
public class ApiClient {
    private static WeatherApiInterface sWeatherApiInterface = null;
    private static retrofit2.Retrofit sRetrofit = null;
    private static OkHttpClient sOkHttpClient = null;
    private static final long CACHE_SIZE = 1024 * 1024 * 50;

    private ApiClient() {
        init();
    }

    public static WeatherApiInterface getWeatherApiInterface() {
        return sWeatherApiInterface;
    }

    public static ApiClient getInstance() {
        return RetrofitHolder.INSTANCE;
    }

    private static class RetrofitHolder {
        private static final ApiClient INSTANCE = new ApiClient();
    }

    private void init() {
        initOkHttp();
        initRetrofit();
        sWeatherApiInterface = sRetrofit.create(WeatherApiInterface.class);
    }

    private static void initRetrofit() {
        sRetrofit = new retrofit2.Retrofit.Builder().baseUrl(WeatherApiInterface.HOST).client(sOkHttpClient).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheFile = new File(BojanWeatherApplication.getAppCacheDir(), "/Cache");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!Util.isNetworkConnected(BojanWeatherApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            Response.Builder newBuilder = response.newBuilder();
            if (Util.isNetworkConnected(BojanWeatherApplication.getContext())) {
                int maxAge = 0;
                newBuilder.header("Cache-Control", "public,max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                newBuilder.header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale);
            }
            return newBuilder.build();
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        sOkHttpClient = builder.build();
    }

    public Observable<WeatherBean> fetchWeather(String city) {
        return sWeatherApiInterface.mWeatherAIP(city, DBConfig.API_KEY).flatMap(weatherAPI -> {
            String status = weatherAPI.getHeWeather5().get(0).getStatus();
            if ("no more requests".equals(status)) {
                return Observable.error(new RuntimeException("No api"));
            } else if ("unknown city".equals(status)) {
                return Observable.error(new RuntimeException(String.format("%s is not in the api list", city)));
            }
            return Observable.just(weatherAPI);
        }).map(weatherAPI -> {
            return weatherAPI;
        }).compose(RxUtil.rxSchedulerHelper());
    }
}
