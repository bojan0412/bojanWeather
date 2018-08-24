package com.android.bojan.bojanweather.view.view.fragment;


import android.app.Notification;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;
import com.android.bojan.bojanweather.view.util.FileUtil;
import com.android.bojan.bojanweather.view.util.SharedPreferenceUtil;

import java.io.File;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener{

    private Preference mClearCache;
    CheckBoxPreference mNoticicatin;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mNoticicatin=(CheckBoxPreference)findPreference("noti_bar");
        if (SharedPreferenceUtil.getInstance().getNotificationModel() != Notification.FLAG_ONGOING_EVENT) {
            mNoticicatin.setChecked(false);
        }else {
            mNoticicatin.setChecked(true);
        }

        mNoticicatin.setOnPreferenceChangeListener(this);

        mClearCache=findPreference("clear_cache");
        mClearCache.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference){

        if(preference==mClearCache){
            if(FileUtil.delete(new File(BojanWeatherApplication.getAppCacheDir()+"/Cache"))){
                Toast.makeText(BojanWeatherApplication.getContext(),"缓存已清除",Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference,Object newValue){

        if(preference==mNoticicatin){
            SharedPreferenceUtil.getInstance().setNotificationModel((boolean) newValue ? Notification.FLAG_ONGOING_EVENT : Notification.FLAG_AUTO_CANCEL);
            Toast.makeText(BojanWeatherApplication.getContext(),"更改成功,重启应用后生效",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }


}
