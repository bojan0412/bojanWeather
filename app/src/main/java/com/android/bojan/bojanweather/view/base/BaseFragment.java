package com.android.bojan.bojanweather.view.base;

import android.support.v4.app.Fragment;

/**
 * Create by bojan
 * on 2018/8/24
 */
public abstract class BaseFragment extends Fragment {
    protected boolean mIsVisible;

    protected abstract void lazyLoad();

    protected void onInvisible() {

    }

    protected void onVisible() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }
}
