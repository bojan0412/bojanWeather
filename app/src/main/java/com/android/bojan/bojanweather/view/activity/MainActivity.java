package com.android.bojan.bojanweather.view.activity;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BaseActivity;
import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.presenter.DrawerPresenter;
import com.android.bojan.bojanweather.view.presenter.HomePagePresenter;
import com.android.bojan.bojanweather.view.sevices.WeatherService;
import com.android.bojan.bojanweather.view.util.SharedPreferenceUtil;
import com.android.bojan.bojanweather.view.util.Util;
import com.android.bojan.bojanweather.view.view.fragment.DrawerFragment;
import com.android.bojan.bojanweather.view.view.fragment.HomePageFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainActivity extends BaseActivity implements HomePageFragment.OnFragmentInteractionListener, DrawerFragment.OnDrawerItemClick {

    private Unbinder unbinder;

    @BindView(R.id.update_time)
    TextView update_time;

    @BindView(R.id.temp)
    TextView temp;

    @BindView(R.id.condition)
    TextView condition;

    @BindView(R.id.city)
    TextView city;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    private AMapLocationClientOption mOtion;
    private AMapLocationClient mClient;
    DrawerPresenter drawerpresenter;

    HomePagePresenter pagePresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        initToolbar();
        initDrawer();
        initRefreshLayout();
        initFragment();
        chePermission();
    }


    @Override
    public void onDestroy() {

        //release resource
        super.onDestroy();
        if (mClient != null)
            mClient.onDestroy();
        mClient = null;
        mOtion = null;
        pagePresenter.onUnsubscribe();
        pagePresenter = null;
        unbinder.unbind();

    }


    @Override
    public void updateHeader(WeatherBean weather) {

        update_time.setText("更新时间" + weather.getHeWeather5().get(0).getBasic().getUpdate().getLoc());
        city.setText(weather.getHeWeather5().get(0).getBasic().getCity());
        temp.setText(weather.getHeWeather5().get(0).getNow().getTmp() + "℃");
        condition.setText(weather.getHeWeather5().get(0).getNow().getCond().getTxt());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.setting) {

            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.about) {

        }

        return super.onOptionsItemSelected(item);
    }


    public void getLocation() {

        //AMap location config
        mClient = new AMapLocationClient(BojanWeatherApplication.getContext());
        mOtion = new AMapLocationClientOption();
        mOtion.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mOtion.setInterval(60000 * 60 * 24);
        mClient.setLocationOption(mOtion);
        mClient.setLocationListener(aMapLocation -> {

            pagePresenter.loadWeather(aMapLocation.getCity(), true);
            SharedPreferenceUtil.getInstance().setCity(aMapLocation.getCity());

            if (SharedPreferenceUtil.getInstance().getNotificationModel() == Notification.FLAG_ONGOING_EVENT) {
                //start the service when location changed
                Intent intent = new Intent(this, WeatherService.class);
                startService(intent);
            } else {
                Intent intent = new Intent(this, WeatherService.class);
                stopService(intent);
            }

        });
        mClient.startLocation();
        Log.d("getlocaton", "called");
    }


    @Override
    public void onDrawerItemClick(String city) {

        pagePresenter.loadWeather(city, true);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(refreshlayout -> {
         pagePresenter.loadWeather(SharedPreferenceUtil.getInstance().getCity(), false);

            refreshlayout.finishRefresh();
        });
    }

    private void chePermission() {

        //need to check permission above android 6.0
        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(granted -> {
            if (granted) {
                getLocation();
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(BojanWeatherApplication.getContext(), "未获得定位权限，加载默认城市", Toast.LENGTH_LONG).show();
                }
                pagePresenter.loadWeather("beijing", true);
            }
        });
    }

    private void initFragment() {

        HomePageFragment homePageFragment = HomePageFragment.newInstance();
        Util.addFragmentToActivity(getSupportFragmentManager(), homePageFragment, R.id.fragment_container);

        DrawerFragment drawerFragment = DrawerFragment.newInstance();
        Util.addFragmentToActivity(getSupportFragmentManager(), drawerFragment, R.id.fragment_container_drawer);

        pagePresenter = new HomePagePresenter(homePageFragment);
        drawerpresenter = new DrawerPresenter(drawerFragment);


    }
}
