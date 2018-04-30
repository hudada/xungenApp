package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.bean.WorkBean;
import com.example.bsproperty.bean.WorkListBean;
import com.example.bsproperty.fragment.UserFragment01;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkListActivity extends BaseActivity {

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
    private UserBean mUser;

    @Override
    protected void initView(Bundle savedInstanceState) {

        btnRight.setText("新增任务");
        btnRight.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(mContext, PointCheckListActivity.class);
                intent.putExtra("data", mData.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_work_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        OkHttpTools.sendGet(mContext, ApiManager.WORK_LIST)
                .addParams("uid", mUser.getId() + "")
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
        mUser = (UserBean) getIntent().getSerializableExtra("data");
        tvTitle.setText(mUser.getUserName() + "的任务");
    }


    @OnClick({R.id.btn_back,R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:
                Intent intent = new Intent(mContext, AddPointActivity.class);
                intent.putExtra("data", mUser);
                startActivity(intent);
                break;
        }
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
}
