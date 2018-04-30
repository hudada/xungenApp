package com.example.bsproperty.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.PointBean;
import com.example.bsproperty.bean.PointListBean;
import com.example.bsproperty.bean.WorkBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.view.VoiceDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PointListActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private WorkBean workBean;
    private MyAdapter adapter;
    private ArrayList<PointBean> mData;
    public LocationClient mLocationClient = null;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("巡更点列表");
        mData = new ArrayList<>();
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyAdapter(mContext, R.layout.item_points, mData);
        rvList.setAdapter(adapter);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_point_list;
    }

    @Override
    protected void loadData() {
        workBean = (WorkBean) getIntent().getSerializableExtra("data");
        loadWebData();
    }

    private void loadWebData() {
        OkHttpTools.sendGet(mContext, ApiManager.POINT_LIST)
                .addParams("wid", workBean.getId() + "")
                .build()
                .execute(new BaseCallBack<PointListBean>(mContext, PointListBean.class) {
                    @Override
                    public void onResponse(PointListBean pointListBean) {
                        mData.clear();
                        mData = pointListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    private class MyAdapter extends BaseAdapter<PointBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<PointBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, final PointBean pointBean, int position) {
            holder.setText(R.id.tv_name, pointBean.getName());
            TextView up = (TextView) holder.getView(R.id.tv_up);
            if (pointBean.getLat() > 0 && pointBean.getLng() > 0) {
                up.setTextColor(getResources().getColor(R.color.green));
                up.setText("已上报位置");
                holder.getView(R.id.tv_up).setOnClickListener(null);
            } else {
                up.setTextColor(getResources().getColor(R.color.white));
                up.setText("上报位置");

                holder.getView(R.id.tv_up).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLocationClient = new LocationClient(getApplicationContext());


                        LocationClientOption option = new LocationClientOption();

                        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

                        option.setCoorType("bd09ll");

                        option.setScanSpan(1000);

                        option.setOpenGps(true);

                        option.setLocationNotify(true);

                        option.setIgnoreKillProcess(false);

                        option.SetIgnoreCacheException(false);

                        option.setWifiCacheTimeOut(5 * 60 * 1000);

                        option.setEnableSimulateGps(false);

                        mLocationClient.setLocOption(option);


                        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                            @Override
                            public void onReceiveLocation(BDLocation location) {

                                final double latitude = location.getLatitude();    //获取纬度信息
                                final double longitude = location.getLongitude();    //获取经度信息
                                int errorCode = location.getLocType();
                                mLocationClient.stop();

                                VoiceDialog dialog = new VoiceDialog(mContext, new VoiceDialog.OnViewClickListener() {
                                    @Override
                                    public void onSuccess() {
                                        OkHttpTools.sendPost(mContext, ApiManager.POINT_CHANGE)
                                                .addParams("id", pointBean.getId() + "")
                                                .addParams("lat", latitude + "")
                                                .addParams("lng", longitude + "")
                                                .build()
                                                .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                                    @Override
                                                    public void onResponse(BaseResponse baseResponse) {
                                                        showToast("上报成功");
                                                        loadWebData();
                                                    }
                                                });
                                    }
                                });
                                dialog.show();

                            }
                        });

                        mLocationClient.start();
                    }
                });
            }
        }
    }
}
