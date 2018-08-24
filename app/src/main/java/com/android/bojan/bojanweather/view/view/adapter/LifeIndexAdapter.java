package com.android.bojan.bojanweather.view.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BaseRecycleerVIewAdapter;
import com.android.bojan.bojanweather.view.base.BaseViewHolder;
import com.android.bojan.bojanweather.view.model.database.LifeIndexORM;

import java.util.List;

import butterknife.BindView;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class LifeIndexAdapter extends BaseRecycleerVIewAdapter<LifeIndexAdapter.ViewHolder> {

    private List<LifeIndexORM> mData;

    public LifeIndexAdapter(List<LifeIndexORM> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lifeindex, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.icon.setImageResource(mData.get(position).getIconRes());
        holder.cond_brif.setText(mData.get(position).getCon_brif());
        holder.cond_txt.setText(mData.get(position).getCon_txt());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.cond_brif)
        TextView cond_brif;

        @BindView(R.id.cond_txt)
        TextView cond_txt;

        @BindView(R.id.icon)
        ImageView icon;


        ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
