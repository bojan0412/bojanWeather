package com.android.bojan.bojanweather.view.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.bojan.bojanweather.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        initToolbar();
        initDrawer();
        initRefreshLayout();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
    private void initDrawer(){
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initRefreshLayout(){
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            pagePresenter.loadWeather(SharedPreferenceUtil.getInstance().getCity(),false);
            refreshlayout.finishRefresh();
        });
    }
}
