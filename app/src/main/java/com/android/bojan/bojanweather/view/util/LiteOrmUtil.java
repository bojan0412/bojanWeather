package com.android.bojan.bojanweather.view.util;

import com.android.bojan.bojanweather.BuildConfig;
import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;
import com.android.bojan.bojanweather.view.model.database.DBConfig;
import com.litesuits.orm.LiteOrm;

/**
 * Create by bojan
 * on 2018/8/16
 */
public class LiteOrmUtil {

    private static LiteOrm litOrm;

    private LiteOrmUtil() {
        if (litOrm == null) {
            litOrm = LiteOrm.newSingleInstance(BojanWeatherApplication.getContext(), DBConfig.DB_NAME);
        }
        litOrm.setDebugged(BuildConfig.DEBUG);
    }


    private static class OrmHolder {
        private static final LiteOrmUtil mInstance = new LiteOrmUtil();
    }

    private static LiteOrmUtil getOrmHolder() {
        return OrmHolder.mInstance;
    }


    public static LiteOrm getInstance() {
        getOrmHolder();
        return litOrm;
    }

}
