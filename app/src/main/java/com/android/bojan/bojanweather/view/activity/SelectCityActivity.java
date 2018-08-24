package com.android.bojan.bojanweather.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BaseActivity;
import com.android.bojan.bojanweather.view.contract.SelectCityContract;
import com.android.bojan.bojanweather.view.presenter.SelectCityPresenter;
import com.android.bojan.bojanweather.view.util.Util;
import com.android.bojan.bojanweather.view.view.fragment.SelectCityFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class SelectCityActivity extends BaseActivity{

    private SelectCityContract.Presenter presenter;
    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SelectCityFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        unbinder= ButterKnife.bind(this);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("请选择省份或城市");
        setSupportActionBar(toolbar);

        fragment = new SelectCityFragment();
        Util.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragment_container);
        presenter = new SelectCityPresenter(fragment);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }
}
