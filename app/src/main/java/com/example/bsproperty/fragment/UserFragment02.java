package com.example.bsproperty.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment02 extends BaseFragment {


    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.btn_out)
    Button btnOut;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void loadData() {
        tvName.setText(MyApplication.getInstance().getUserBean().getUserName());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("我的");
        btnBack.setVisibility(View.GONE);
    }


    @Override
    public int getRootViewId() {
        return R.layout.fragment_user02;
    }


    @OnClick(R.id.btn_out)
    public void onViewClicked() {
        if (SpUtils.cleanUserBean(mContext)) {
            System.exit(0);
        }
    }
}
