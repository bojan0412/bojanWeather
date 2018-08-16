package com.android.bojan.bojanweather.view.contract;

import com.android.bojan.bojanweather.view.base.BasePresenter;
import com.android.bojan.bojanweather.view.base.BaseView;
import com.android.bojan.bojanweather.view.model.database.DrawerItemORM;

import java.util.List;

/**
 * Create by bojan
 * on 2018/8/16
 */
public interface DrawerContract {
    interface Presenter extends BasePresenter {
        void loadItem();
    }

    interface View extends BaseView<Presenter> {
        void showItem(List<DrawerItemORM> datas);
    }
}
