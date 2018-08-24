package com.android.bojan.bojanweather.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BaseActivity;
import com.android.bojan.bojanweather.view.view.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class SettingActivity  extends BaseActivity{

    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }



        SettingFragment fragment = new SettingFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_setting, fragment).commit();


    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
