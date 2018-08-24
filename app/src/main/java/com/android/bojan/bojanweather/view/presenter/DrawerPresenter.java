package com.android.bojan.bojanweather.view.presenter;

import com.android.bojan.bojanweather.view.contract.DrawerContract;
import com.android.bojan.bojanweather.view.model.database.DrawerItemORM;
import com.android.bojan.bojanweather.view.util.LiteOrmUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by bojan
 * on 2018/8/16
 */
public class DrawerPresenter implements DrawerContract.Presenter {
    private DrawerContract.View mView;
    private List<DrawerItemORM> mDatas;

    public DrawerPresenter(DrawerContract.View view) {
        mView = view;
        mDatas = new ArrayList<>();
        mView.setPresenter(this);
    }

    @Override
    public void loadItem() {
        if (mDatas!= null){
            mDatas.clear();
        }
        mDatas= LiteOrmUtil.getInstance().query(DrawerItemORM.class);
        mView.showItem(mDatas);
    }

    @Override
    public void onUnsubscribe() {

    }

    @Override
    public void onsubscribe(String city) {

    }
}
