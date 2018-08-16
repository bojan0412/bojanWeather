package com.android.bojan.bojanweather.view.presenter;

import android.view.View;

import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;
import com.android.bojan.bojanweather.view.contract.HomePageContract;
import com.android.bojan.bojanweather.view.model.api.ApiClient;
import com.android.bojan.bojanweather.view.util.Util;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;

/**
 * Create by bojan
 * on 2018/8/16
 */
public class HomePagePresenter implements HomePageContract.Presenter {
    private WeakReference<HomePageContract.View> mWeakView;
    private Subscription mSubscription;

    public HomePagePresenter(HomePageContract.View view) {
        mWeakView = new WeakReference<>(view);
        view.setPresenter(this);
    }

    @Override
    public void loadWeather(String cityId, boolean needToast) {
        if (Util.isNetworkConnected(BojanWeatherApplication.getContext())) {
            mSubscription = ApiClient.getInstance().fetchWeather(cityId)
        }

    }


    @Override
    public WeakReference<View> getView() {
        return null;
    }

    @Override
    public void onUnsubscribe() {

    }
}
