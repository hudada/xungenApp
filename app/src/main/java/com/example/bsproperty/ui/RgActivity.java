package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RgActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_0)
    Button btn0;

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_rg;
    }

    @Override
    protected void loadData() {

    }


    @OnClick(R.id.btn_0)
    public void onViewClicked() {
        final String user = etUser.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
            showToast("请输入完整信息");
            return;
        }
        OkHttpTools.sendPost(mContext,ApiManager.USER_CHECK)
                .addParams("name",user)
                .addParams("pwd", pwd)
                .addParams("role", 0 + "")
                .build()
                .execute(new BaseCallBack<UserObjBean>(mContext,UserObjBean.class) {
                    @Override
                    public void onResponse(UserObjBean userObjBean) {
                        Intent intent = new Intent(mContext, RgVoiceActivity.class);
                        intent.putExtra("id", userObjBean.getData().getId());
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
