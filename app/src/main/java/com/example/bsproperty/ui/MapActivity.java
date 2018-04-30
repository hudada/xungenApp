package com.example.bsproperty.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.PointBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;


    private MapView bmapView;

    private BaiduMap mBaiduMap;

    private ArrayList<PointBean> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("巡更点地图");
        bmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_map;
    }

    @Override
    protected void loadData() {

        mData = (ArrayList<PointBean>) getIntent().getSerializableExtra("data");

        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        List<OverlayOptions> textOptions = new ArrayList<OverlayOptions>();
        LatLng cenpt = null;
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher);

        for (PointBean mDatum : mData) {
            if (mDatum.getLng() > 0 && mDatum.getLat() > 0) {
                LatLng point = new LatLng(mDatum.getLat(), mDatum.getLng());
                if (cenpt == null) {
                    cenpt = point;
                }

                OverlayOptions textOption = new TextOptions()
                        .bgColor(0xAAFFFF00)
                        .fontSize(24)
                        .fontColor(0xFFFF00FF)
                        .text(mDatum.getName())
                        .rotate(0)
                        .position(point);


                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                options.add(option);

                textOptions.add(textOption);
            }
        }
        mBaiduMap.addOverlays(options);
        mBaiduMap.addOverlays(textOptions);

        if (cenpt != null) {
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(20)
                    .build();

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @Override
    protected void onDestroy() {
        bmapView.onDestroy();
        super.onDestroy();
    }
}
