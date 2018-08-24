package com.android.bojan.bojanweather.view.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.contract.SelectCityContract;
import com.android.bojan.bojanweather.view.model.RecyclerDecor.GroupedDecoration;
import com.android.bojan.bojanweather.view.model.api.ApiClient;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.model.data.City;
import com.android.bojan.bojanweather.view.model.data.CityBean;
import com.android.bojan.bojanweather.view.model.data.Province;
import com.android.bojan.bojanweather.view.model.database.DrawerItemORM;
import com.android.bojan.bojanweather.view.util.LiteOrmUtil;
import com.android.bojan.bojanweather.view.view.adapter.CityListAdapter;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class SelectCityFragment extends Fragment implements SelectCityContract.View {

    private List<String> mData;
    private List<Province> mProvinces;
    private RecyclerView mCitiList;
    private List<CityBean> mCityBean;
    private CityListAdapter mCityAdapter;
    public static final int LEVEL_PROVINCE = 1;
    private int mCurrentLevel = LEVEL_PROVINCE;
    public static final int LEVEL_CITY = 2;
    private Disposable mDisposable;
    private Province mSelectedProvence;

    private SelectCityContract.Presenter mPresenter;

    public SelectCityFragment() {

        mData = new ArrayList<>();
        mCityBean = new ArrayList<>();
        mProvinces = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_select_city, container, false);
        mCitiList = rootView.findViewById(R.id.citylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        mCitiList.setLayoutManager(linearLayoutManager);
        mCitiList.addItemDecoration(new GroupedDecoration(rootView.getContext(), mCityBean));
        mCityAdapter = new CityListAdapter(mData);

        mCityAdapter.setOnClickListener(position -> {

            if (mCurrentLevel == LEVEL_PROVINCE) {

                mSelectedProvence = mProvinces.get(position);
                mPresenter.loadCities(mSelectedProvence.ProSort);
                mCitiList.smoothScrollToPosition(0);
                mCurrentLevel = LEVEL_CITY;

            } else if (mCurrentLevel == LEVEL_CITY) {
                if (LiteOrmUtil.getInstance().query(new QueryBuilder<>(DrawerItemORM.class).where("mCity=?", mData.get(position))).size() == 0) {
                    ApiClient.getInstance().fetchWeather(mData.get(position))
                            .subscribe(new Observer<WeatherBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    mDisposable = d;
                                }

                                @Override
                                public void onNext(WeatherBean weatherBean) {
                                    LiteOrmUtil.getInstance().save(new DrawerItemORM(mData.get(position), weatherBean.getHeWeather5().get(0).getNow().getTmp() + "℃", weatherBean.getHeWeather5().get(0).getNow().getCond().getCode()));
                                    Toast.makeText(getContext(), "已经成功添加" + mData.get(position), Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                } else {
                    Toast.makeText(getContext(), "您已经添加过" + mData.get(position) + "了，无需重复添加", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCitiList.setAdapter(mCityAdapter);
        mPresenter.loadProvinces();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


    @Override
    public void setPresenter(SelectCityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateCities(List<City> cities, List<CityBean> cityBeen) {
        if (mCityBean != null)
            mCityBean.clear();

        mCityBean.addAll(cityBeen);
        if (mData.size() != 0) {
            mData.clear();
        }

        for (int i = 0; i < cities.size(); i++) {
            mData.add(cities.get(i).CityName);
        }

        mCityAdapter.notifyDataSetChanged();

    }


    @Override
    public void updateProvinces(List<Province> provinces, List<CityBean> cityBeen) {

        if (mProvinces != null)
            mProvinces.clear();
        mProvinces.addAll(provinces);


        if (mCityBean != null) {
            mCityBean.clear();
        }
        mCityBean.addAll(cityBeen);


        if (mData.size() != 0)
            mData.clear();
        for (int i = 0; i < provinces.size(); i++) {
            mData.add(provinces.get(i).ProName);
        }

        mCityAdapter.notifyDataSetChanged();
    }

}
