package com.android.bojan.bojanweather.view.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.activity.SelectCityActivity;
import com.android.bojan.bojanweather.view.base.BaseFragment;
import com.android.bojan.bojanweather.view.contract.DrawerContract;
import com.android.bojan.bojanweather.view.model.api.ApiClient;
import com.android.bojan.bojanweather.view.model.api.WeatherBean;
import com.android.bojan.bojanweather.view.model.database.DrawerItemORM;
import com.android.bojan.bojanweather.view.util.LiteOrmUtil;
import com.android.bojan.bojanweather.view.util.RxUtil;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class DrawerFragment extends BaseFragment implements DrawerContract.View {
    private DrawerContract.Presenter mPrsenter;
    private Unbinder unbinder;
    private OnDrawerItemClick onDrawerItemClick;
    private Disposable mDisposable;

    @Override
    protected void lazyLoad() {

    }

    public interface OnDrawerItemClick {
        void onDrawerItemClick(String city);
    }


    public DrawerFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @BindView(R.id.drawer_item)
    RecyclerView mDrawerRecy;

    @BindView(R.id.add_city_btn)
    Button add_btn;

    private DrawerItemAdapter mDraweritemAdapter;
    private List<DrawerItemORM> mDraweritemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        unbinder = ButterKnife.bind(this, view);


        mDrawerRecy.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mDraweritemList = new ArrayList<>();
        mDraweritemAdapter = new DrawerItemAdapter(mDraweritemList);
        mDraweritemAdapter.setOnHolderItemClickListener(new DrawerItemAdapter.OnHolderItemClickListener() {
            @Override
            public void onHolderItemClick(int position) {
                List<DrawerItemORM> item = LiteOrmUtil.getInstance().query(new QueryBuilder<>(DrawerItemORM.class).where("mCity=?", mDraweritemList.get(position).getCity()));
                ApiClient.getInstance().fetchWeather(mDraweritemList.get(position).getCity())
                        .compose(RxUtil.rxSchedulerHelper())
                        .subscribe(new Observer<WeatherBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable = d;
                            }

                            @Override
                            public void onNext(WeatherBean weatherBean) {
                                item.get(0).setCity(weatherBean.getHeWeather5().get(0).getBasic().getCity() + "市");
                                item.get(0).setTemp(weatherBean.getHeWeather5().get(0).getNow().getTmp() + "℃");
                                item.get(0).setCode(weatherBean.getHeWeather5().get(0).getNow().getCond().getCode());
                                LiteOrmUtil.getInstance().update(item.get(0));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                onDrawerItemClick.onDrawerItemClick(mDraweritemList.get(position).getCity());
            }
        });

        mDraweritemAdapter.setOnDeleteBtnClickListener(position -> {

            new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage("您确定将" + mDraweritemList.get(position).getCity() + "删除吗？")
                    .setPositiveButton("取消", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNegativeButton("确定", (dialog, which) -> {

                        LiteOrmUtil.getInstance().delete(new WhereBuilder(DrawerItemORM.class).where("mCity=?", mDraweritemList.get(position).getCity()));
                        mDraweritemList.remove(position);

                        mDraweritemAdapter.notifyDataSetChanged();
                    }).show();


        });


        mDrawerRecy.setAdapter(mDraweritemAdapter);


        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrawerItemClick) {
            onDrawerItemClick = (OnDrawerItemClick) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPrsenter.loadItem();
    }


    @Override
    public void setPresenter(DrawerContract.Presenter presenter) {
        mPrsenter = presenter;
    }


    @OnClick(R.id.add_city_btn)
    void onAddClick() {
        Intent intent = new Intent(getActivity(), SelectCityActivity.class);
        startActivity(intent);
    }


    @Override
    public void showItem(List<DrawerItemORM> data) {

        if (mDraweritemList != null) {
            mDraweritemList.clear();
        }

        mDraweritemList.addAll(data);
        mDraweritemAdapter.notifyDataSetChanged();
    }


    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPrsenter.onUnsubscribe();
        unbinder.unbind();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

    }
}
