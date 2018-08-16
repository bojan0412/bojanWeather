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
    private static LiteOrm sLiteOrm;

    private LiteOrmUtil() {
        if (sLiteOrm == null) {
            sLiteOrm = LiteOrm.newSingleInstance(BojanWeatherApplication.getContext(), DBConfig.DB_NAME);
        }
        sLiteOrm.setDebugged(BuildConfig.DEBUG);
    }

    public static LiteOrm getInstance() {
        new Holder();
        return sLiteOrm;
    }

    private static class Holder {
        private static LiteOrmUtil liteOrmUtil = new LiteOrmUtil();

    }
}
