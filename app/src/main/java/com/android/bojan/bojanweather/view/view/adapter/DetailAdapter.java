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
import com.android.bojan.bojanweather.view.model.database.DetailORM;

import java.util.List;

import butterknife.BindView;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class DetailAdapter extends BaseRecycleerVIewAdapter<DetailAdapter.ViewHolder> {

    private List<DetailORM> mDetailORM;

    public DetailAdapter(List<DetailORM> detailORM) {
        mDetailORM = detailORM;
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.detail_icon)
        ImageView detailIconView;

        @BindView(R.id.detail_txt)
        TextView detailTextView;

        @BindView(R.id.detail_key)
        TextView detailKey;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detail, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHoler, int i) {
        viewHoler.detailIconView.setImageResource(mDetailORM.get(i).getIconRes());
        viewHoler.detailKey.setText(mDetailORM.get(i).getKey());
        viewHoler.detailTextView.setText(mDetailORM.get(i).getValue());
    }


    @Override
    public int getItemCount() {
        return mDetailORM.size();
    }
}
