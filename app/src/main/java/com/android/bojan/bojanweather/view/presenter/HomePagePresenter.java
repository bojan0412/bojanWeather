package com.android.bojan.bojanweather.view.presenter;

import android.view.View;

import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;
import com.android.bojan.bojanweather.view.contract.HomePageContract;
import com.android.bojan.bojanweather.view.model.api.ApiClient;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.util.Util;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by bojan
 * on 2018/8/16
 */
public class HomePagePresenter implements HomePageContract.Presenter {
    private WeakReference<HomePageContract.View> mWeakView;
    private Disposable mDisposable;

    public HomePagePresenter(HomePageContract.View view) {
        mWeakView = new WeakReference<>(view);
        view.setPresenter(this);

    }

    @Override
    public void loadWeather(String cityId, boolean needToast) {
        if (Util.isNetworkConnected(BojanWeatherApplication.getContext())) {
            ApiClient.getInstance().fetchWeather(cityId).doOnSubscribe(consuermer -> {
                if (needToast) mWeakView.get().toastLoading();
            }).subscribe(new Observer<WeatherBean>() {
                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(WeatherBean weatherBean) {
                    mWeakView.get().showWeather(weatherBean);
                }

                @Override
                public void onError(Throwable e) {
                    if (needToast) {
                        mWeakView.get().toastError();
                    }
                }

                @Override
                public void onComplete() {
                    if (needToast) {
                        mWeakView.get().toastComplete();
                    }
                }
            });
        }

    }


    @Override
    public WeakReference<View> getView() {
        return mWeakView.get().provideView();
    }

    @Override
    public void onUnsubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
    }

    @Override
    public void onsubscribe(String city) {
        loadWeather(city, false);
    }
}
