package com.android.bojan.bojanweather.view.util;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by bojan
 * on 2018/8/16
 */
public class RxUtil {
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return tObservable->tObservable.subscribeOn(Schedulers.io()).unsubscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
