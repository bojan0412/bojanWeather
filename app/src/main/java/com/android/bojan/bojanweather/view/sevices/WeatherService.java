package com.android.bojan.bojanweather.view.sevices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.activity.MainActivity;
import com.android.bojan.bojanweather.view.model.api.ApiClient;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.util.RxUtil;
import com.android.bojan.bojanweather.view.util.SharedPreferenceUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class WeatherService extends Service {

    private Notification notification;
    private NotificationManager manager;
    private Disposable mTaskDisposable;
    private Disposable mTimeDisposable;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Observable.interval(1, TimeUnit.HOURS)
                .compose(RxUtil.rxSchedulerHelper()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                mTimeDisposable = d;
            }

            @Override
            public void onNext(Long aLong) {
                getWeather();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        return START_REDELIVER_INTENT;

    }

    public void showNotificationbar(WeatherBean weatherBean) {

        Intent intent = new Intent(WeatherService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(WeatherService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(WeatherService.this);
        notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(weatherBean.getHeWeather5().get(0).getBasic().getCity())
                .setContentText(String.format("%s 当前温度: %s℃ ", weatherBean.getHeWeather5().get(0).getNow().getCond().getTxt(), weatherBean.getHeWeather5().get(0).getNow().getTmp()))
                .setSmallIcon(R.drawable.ic_sunny_black_24dp)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (SharedPreferenceUtil.getInstance().getNotificationModel() != Notification.FLAG_ONGOING_EVENT) {
            manager.cancel(1);
        } else {
            manager.notify(1, notification);
        }

    }


    private void getWeather() {

        ApiClient.getInstance().fetchWeather(SharedPreferenceUtil.getInstance().getCity())
                .subscribe(new Observer<WeatherBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        showNotificationbar(weatherBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {


        if (mTaskDisposable != null && !mTaskDisposable.isDisposed()) {
            mTaskDisposable.dispose();
        }

        if (mTaskDisposable != null && !mTaskDisposable.isDisposed()) {
            mTaskDisposable.dispose();
        }
    }

}
