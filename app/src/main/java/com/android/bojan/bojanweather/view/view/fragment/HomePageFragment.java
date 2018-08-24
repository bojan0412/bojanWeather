package com.android.bojan.bojanweather.view.view.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.activity.MainActivity;
import com.android.bojan.bojanweather.view.base.BaseFragment;
import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;
import com.android.bojan.bojanweather.view.contract.HomePageContract;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.model.database.DetailORM;
import com.android.bojan.bojanweather.view.model.database.ForecastORM;
import com.android.bojan.bojanweather.view.model.database.LifeIndexORM;
import com.android.bojan.bojanweather.view.util.SharedPreferenceUtil;
import com.android.bojan.bojanweather.view.util.Util;
import com.android.bojan.bojanweather.view.view.adapter.DetailAdapter;
import com.android.bojan.bojanweather.view.view.adapter.ForecastAdapter;
import com.android.bojan.bojanweather.view.view.adapter.LifeIndexAdapter;
import com.androidadvance.topsnackbar.TSnackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class HomePageFragment extends BaseFragment implements HomePageContract.View {

    private Unbinder mUnbinder;
    @BindView(R.id.detail_recyclerview)
    RecyclerView detailRecy;

    @BindView(R.id.forecast_recyclerview)
    RecyclerView forecastRecy;

    @BindView(R.id.lifeindex_recyclerview)
    RecyclerView lifeRecy;
    private DetailAdapter detailAdapter;
    private ForecastAdapter forecastAdapter;
    private LifeIndexAdapter lifeIndexAdapter;


    private List<DetailORM> detailORMs;
    private List<ForecastORM> forecastORMs;
    private List<LifeIndexORM> lifeIndexOrms;
    private HomePageContract.Presenter presenter;

    private OnFragmentInteractionListener onFragmentInteractionListener;

    private TSnackbar tSnackbar;

    public interface OnFragmentInteractionListener {
        void updateHeader(WeatherBean weather);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        detailRecy.setNestedScrollingEnabled(false);
        detailRecy.setLayoutManager(new GridLayoutManager(BojanWeatherApplication.getContext(), 3));
        detailORMs = new ArrayList<>();
        detailAdapter = new DetailAdapter(detailORMs);
        detailRecy.setAdapter(detailAdapter);

        forecastRecy.setNestedScrollingEnabled(false);
        forecastRecy.setLayoutManager(new LinearLayoutManager(BojanWeatherApplication.getContext()));
        forecastORMs = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(forecastORMs);
        forecastRecy.setAdapter(forecastAdapter);

        lifeRecy.setNestedScrollingEnabled(false);
        lifeRecy.setLayoutManager(new LinearLayoutManager(BojanWeatherApplication.getContext()));
        lifeIndexOrms = new ArrayList<>();
        lifeIndexAdapter = new LifeIndexAdapter(lifeIndexOrms);
        lifeRecy.setAdapter(lifeIndexAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else
            throw new RuntimeException(context.toString() + "must implement onFragmentInteractionListener");
    }

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    private List<DetailORM> createDetail(WeatherBean weather) {
        List<DetailORM> details = new ArrayList<>();
        details.add(new DetailORM(R.drawable.ic_temp, "体感温度", weather.getHeWeather5().get(0).getNow().getFl() + "℃"));
        details.add(new DetailORM(R.drawable.ic_shidu, "相对湿度", weather.getHeWeather5().get(0).getNow().getHum() + "%"));
        details.add(new DetailORM(R.drawable.ic_pres, "气压", weather.getHeWeather5().get(0).getNow().getPres() + "Pa"));
        details.add(new DetailORM(R.drawable.ic_rain, "降水量", weather.getHeWeather5().get(0).getNow().getPcpn() + "mm"));
        details.add(new DetailORM(R.drawable.ic_wind, "风速", weather.getHeWeather5().get(0).getNow().getWind().getSpd() + "km/h"));
        details.add(new DetailORM(R.drawable.ic_vis, "能见度", weather.getHeWeather5().get(0).getNow().getVis() + "km"));
        return details;
    }

    private List<ForecastORM> createForecasts(WeatherBean weather) {
        List<ForecastORM> forecasts = new ArrayList<>();
        for (int i = 0; i < weather.getHeWeather5().get(0).getDaily_forecast().size(); i++) {

            if (!Util.isToday(weather.getHeWeather5().get(0).getDaily_forecast().get(i).getDate()))

                forecasts.add(new ForecastORM(Util.getWeekByDateStr(weather.getHeWeather5().get(0).getDaily_forecast().get(i).getDate()), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getDate(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getCond().getTxt_d(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getTmp().getMax(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getTmp().getMin(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getCond().getCode_d()));

            else {

                forecasts.add(new ForecastORM("今天", weather.getHeWeather5().get(0).getDaily_forecast().get(i).getDate(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getCond().getTxt_d(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getTmp().getMax(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getTmp().getMin(), weather.getHeWeather5().get(0).getDaily_forecast().get(i).getCond().getCode_d()));

            }
        }
        return forecasts;
    }

    private List<LifeIndexORM> createLifeIndex(WeatherBean weather) {
        List<LifeIndexORM> lifeIndex = new ArrayList<>();
        lifeIndex.add(new LifeIndexORM(weather.getHeWeather5().get(0).getSuggestion().getCw().getBrf(), weather.getHeWeather5().get(0).getSuggestion().getCw().getTxt(), R.drawable.ic_car));
        lifeIndex.add(new LifeIndexORM(weather.getHeWeather5().get(0).getSuggestion().getSport().getBrf(), weather.getHeWeather5().get(0).getSuggestion().getSport().getTxt(), R.drawable.ic_sport));
        lifeIndex.add(new LifeIndexORM(weather.getHeWeather5().get(0).getSuggestion().getDrsg().getBrf(), weather.getHeWeather5().get(0).getSuggestion().getDrsg().getTxt(), R.drawable.ic_cloth));
        lifeIndex.add(new LifeIndexORM(weather.getHeWeather5().get(0).getSuggestion().getUv().getBrf(), weather.getHeWeather5().get(0).getSuggestion().getUv().getTxt(), R.drawable.ic_uv));

        return lifeIndex;
    }

    @Override
    public void showWeather(WeatherBean weather) {


        detailORMs.clear();
        detailORMs.addAll(createDetail(weather));
        detailAdapter.notifyDataSetChanged();


        forecastORMs.clear();
        forecastORMs.addAll(createForecasts(weather));
        forecastAdapter.notifyDataSetChanged();

        lifeIndexOrms.clear();
        lifeIndexOrms.addAll(createLifeIndex(weather));
        lifeIndexAdapter.notifyDataSetChanged();


        showNotificationbar(weather);
        onFragmentInteractionListener.updateHeader(weather);
    }

    @Override
    public void toastLoading() {
        TSnackbar.make(getView(), "(＝^ω^＝)正在加载天气，请稍候...", TSnackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void toastError() {
        TSnackbar.make(getView(), "（╯＾╰）无法加载当前城市的天气，请重试...", TSnackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void toastComplete() {
        tSnackbar = TSnackbar.make(getView(), "加载完毕~(@^_^@)~", TSnackbar.LENGTH_LONG)
                .setAction("取消", v -> {
                    tSnackbar.dismiss();
                });
        tSnackbar.show();
    }

    public void showNotificationbar(WeatherBean weather) {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getActivity());
        Notification notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(weather.getHeWeather5().get(0).getBasic().getCity())
                .setContentText(String.format("%s 当前温度: %s℃ ", weather.getHeWeather5().get(0).getNow().getCond().getTxt(), weather.getHeWeather5().get(0).getNow().getTmp()))
                .setSmallIcon(R.drawable.ic_sunny_black_24dp)
                .build();
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        if (SharedPreferenceUtil.getInstance().getNotificationModel() != Notification.FLAG_ONGOING_EVENT) {

            manager.cancel(1);
        } else {
            manager.notify(1, notification);

        }

    }

    @Override
    public WeakReference<View> provideView() {
        return new WeakReference<>(getView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (presenter != null)
            presenter.onUnsubscribe();
        //释放资源
        tSnackbar = null;

    }

    @Override
    public void setPresenter(HomePageContract.Presenter presenter) {

        this.presenter = presenter;
    }
}

