package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.WorkBean;
import com.example.bsproperty.bean.WorkListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.PointListActivity;
import com.example.bsproperty.ui.WorkListActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment01 extends BaseFragment {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    private ArrayList<WorkBean> mData;
    private MyAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        loadWebData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mContext != null) {
            loadWebData();
        }
    }

    private void loadWebData() {
        OkHttpTools.sendGet(mContext, ApiManager.WORK_LIST)
                .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                .build()
                .execute(new BaseCallBack<WorkListBean>(mContext, WorkListBean.class) {
                    @Override
                    public void onResponse(WorkListBean workListBean) {
                        mData.clear();
                        mData = workListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @Override
    protected void loadData() {

    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("任务");
        btnBack.setVisibility(View.GONE);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        adapter = new MyAdapter(mContext, R.layout.item_voice, mData);
        rvList.setAdapter(adapter);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(mContext, PointListActivity.class);
                intent.putExtra("data", mData.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_user01;
    }


    private class MyAdapter extends BaseAdapter<WorkBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<WorkBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, WorkBean workBean, int position) {
            holder.setText(R.id.tv_name, workBean.getName());
            holder.setText(R.id.tv_time, "发布时间：" + MyApplication.format.format(workBean.getTime()));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
