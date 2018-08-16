package com.android.bojan.bojanweather.view.contract;

import android.view.View;

import com.android.bojan.bojanweather.view.base.BasePresenter;
import com.android.bojan.bojanweather.view.base.BaseView;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;

import java.lang.ref.WeakReference;

/**
 * Create by bojan
 * on 2018/8/16
 */
public interface HomePageContract {
    interface View extends BaseView<Presenter> {
        void showWeather(WeatherBean weather);

        void toastLoading();

        void toastError();

        void toastComplete();

        WeakReference<android.view.View> provideView();
    }

    interface Presenter extends BasePresenter {
        void loadWeather(String cityId, boolean needToast);

        WeakReference<android.view.View> getView();
    }
}
