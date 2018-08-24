package com.android.bojan.bojanweather.view.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Create by bojan
 * on 2018/8/24
 */

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<String> mCities;
    private OnCityItemClickListener mOnCityClickListener;

    public CityListAdapter(List<String>list){
        mCities=list;

    }


    @Override
    public int getItemCount(){
        return mCities.size();
    }



    @Override
    public void onBindViewHolder(ViewHolder holder,int pos){
        holder.mCity.setText(mCities.get(pos));
        holder.mItem.setOnClickListener(v -> {
            mOnCityClickListener.onItemClick(pos);
        });
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city_name,parent,false);

        return new ViewHolder(itemView);
    }


    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.item_city)
        LinearLayout mItem;

        @BindView(R.id.city)
        TextView mCity;
        public ViewHolder(View itemView){
            super(itemView);

        }
    }



    public interface OnCityItemClickListener{
        void onItemClick(int position);
    }

    public void setOnClickListener(OnCityItemClickListener listener){
        mOnCityClickListener=listener;
    }




}
