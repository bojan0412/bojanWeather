package com.android.bojan.bojanweather.view.presenter;

import com.android.bojan.bojanweather.view.contract.SelectCityContract;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.model.comparator.PinyinComparator;
import com.android.bojan.bojanweather.view.model.data.City;
import com.android.bojan.bojanweather.view.model.data.CityBean;
import com.android.bojan.bojanweather.view.model.data.Province;
import com.android.bojan.bojanweather.view.model.database.DBManager;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by bojan
 * on 2018/8/23
 */
public class SelectCityPresenter implements SelectCityContract.Presenter {

    private DBManager database;
    private SelectCityContract.View cityListView;
    private List<CityBean> mCitiybeans;
    private List<Province> mProvinces;
    private List<City> mCities;
    private int currentLevel;

    public SelectCityPresenter(SelectCityContract.View view) {


        mCitiybeans = new ArrayList<>();
        mProvinces = new ArrayList<>();
        database = new DBManager();
        mCities = new ArrayList<>();
        cityListView = view;
        cityListView.setPresenter(this);

    }


    @Override
    public void onUnsubscribe() {

        database.closeDatabase();

    }

    @Override
    public void onsubscribe(String city) {

    }

    @Override
    public void loadCities(int port) {
        queryCities(port);
    }

    private void queryProvinces() {

        if (mCitiybeans != null)
            mCitiybeans.clear();

        if (mProvinces != null) {
            mProvinces.clear();
        }
        mProvinces.addAll(WeatherDB.loadProvinces(database.getDatabase()));
        Collections.sort(mProvinces, new PinyinComparator());

        for (int i = 0; i < mProvinces.size(); i++) {

            String s = Pinyin.toPinyin(mProvinces.get(i).ProName, "").substring(0, 1).toUpperCase();
            mCitiybeans.add(new CityBean(s, mProvinces.get(i).ProName));

        }

        cityListView.updateProvinces(mProvinces, mCitiybeans);


    }


    @Override
    public void loadProvinces() {
        queryProvinces();
    }


    public void queryCities(int portnum) {

        if (mCities != null)
            mCities.clear();

        if (mCitiybeans != null)
            mCitiybeans.clear();

        mCities.addAll(WeatherDB.loadCities(database.getDatabase(), portnum));
        Collections.sort(mCities, new PinyinComparator());

        for (int i = 0; i < mCities.size(); i++) {

            String s = Pinyin.toPinyin(mCities.get(i).CityName, "").substring(0, 1).toUpperCase();
            mCitiybeans.add(new CityBean(s, mCities.get(i).CityName));

        }

        cityListView.updateCities(mCities, mCitiybeans);

    }

}
