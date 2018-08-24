package com.android.bojan.bojanweather.view.contract;

import com.android.bojan.bojanweather.view.base.BasePresenter;
import com.android.bojan.bojanweather.view.base.BaseView;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.model.data.City;
import com.android.bojan.bojanweather.view.model.data.CityBean;
import com.android.bojan.bojanweather.view.model.data.Province;

import java.util.List;

/**
 * Create by bojan
 * on 2018/8/23
 */
public interface SelectCityContract {
    interface Presenter extends BasePresenter {
        void loadCities(int portNum);

        void loadProvinces();
    }

    interface View extends BaseView<Presenter> {
        void updateCities(List<City> cities, List<CityBean> cityBean);

        void updateProvinces(List<Province> provinces, List<CityBean> cityBean);
    }
}

