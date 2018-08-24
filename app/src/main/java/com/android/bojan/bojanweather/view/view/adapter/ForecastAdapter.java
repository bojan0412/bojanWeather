package com.android.bojan.bojanweather.view.view.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BaseRecycleerVIewAdapter;
import com.android.bojan.bojanweather.view.base.BaseViewHolder;
import com.android.bojan.bojanweather.view.model.database.ForecastORM;

import java.util.List;

import butterknife.BindView;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class ForecastAdapter extends BaseRecycleerVIewAdapter<ForecastAdapter.ViewHolder> {

    private List<ForecastORM> mForecastORMList;

    public ForecastAdapter(List<ForecastORM> forecastORMList) {
        mForecastORMList = forecastORMList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_daily_forecast, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.temMinTextView.setText(mForecastORMList.get(i).getTempminText());
        viewHolder.temMaxTextView.setText(mForecastORMList.get(i).getTempmaxText());
        viewHolder.weekTextView.setText(mForecastORMList.get(i).getWeekText());
        viewHolder.dateTextView.setText(mForecastORMList.get(i).getDateText());
        viewHolder.weatherTextView.setText(mForecastORMList.get(i).getWeatherText());
        int code = Integer.parseInt(mForecastORMList.get(i).getCode());
        if (code == 100) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_icon_sunny);
        } else if (code > 100 && code <= 213) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_cloudy);
        } else if (code > 213 && code <= 301) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_icon_rain);
        } else if (code > 301 && code <= 304) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_icon_thunder_rain);
        } else if (code > 304 && code <= 313) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_icon_heavy_rain);
        } else if (code > 313) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_icon_snow);
        }
    }

    @Override
    public int getItemCount() {
        return mForecastORMList.size();
    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.weekday)
        TextView weekTextView;

        @BindView(R.id.date)
        TextView dateTextView;

        @BindView(R.id.icon)
        ImageView iconImageView;

        @BindView(R.id.condition)
        TextView weatherTextView;

        @BindView(R.id.max_temp)
        TextView temMaxTextView;

        @BindView(R.id.min_temp)
        TextView temMinTextView;

        ViewHolder(View itemView) {

            super(itemView);

        }
    }
}
