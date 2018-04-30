package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PointCheckListActivity extends BaseActivity {

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
        adapter = new MyAdapter(mContext, R.layout.item_check_points, mData);
        rvList.setAdapter(adapter);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("地图查看");
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


    @OnClick({R.id.btn_back, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:
                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra("data", mData);
                startActivity(intent);
                break;
        }

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
                up.setText("已上报位置");
                up.setTextColor(getResources().getColor(R.color.green));
            } else {
                up.setText("未上报");
                up.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }
}
