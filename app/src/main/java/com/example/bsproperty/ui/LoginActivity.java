package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_0)
    Button btn0;
    @BindView(R.id.btn_1)
    Button btn1;
    @BindView(R.id.rg_list)
    RadioGroup rgList;

    private int role;

    @Override
    protected void initView(Bundle savedInstanceState) {
        rgList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                role = Integer.parseInt(findViewById(checkedId).getTag().toString());
                if (role == 0) {
                    btn1.setVisibility(View.VISIBLE);
                } else {
                    btn1.setVisibility(View.GONE);
                }
            }
        });
        ((RadioButton) rgList.getChildAt(0)).setChecked(true);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void loadData() {
    }


    @OnClick({R.id.btn_0, R.id.btn_1})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_0:
                String user = etUser.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
                    showToast("请输入完整信息");
                    return;
                }

                OkHttpTools.sendPost(mContext, ApiManager.LOGIN)
                        .addParams("name", user)
                        .addParams("pwd", pwd)
                        .addParams("role", role + "")
                        .build()
                        .execute(new BaseCallBack<UserObjBean>(mContext, UserObjBean.class) {
                            @Override
                            public void onResponse(UserObjBean userObjBean) {
                                SpUtils.setUserBean(mContext, userObjBean.getData());
                                MyApplication.getInstance().setUserBean(userObjBean.getData());
                                if (role == 0){
                                    startActivity(new Intent(mContext, UserMainActivity.class));
                                }else {
                                    startActivity(new Intent(mContext, AdminMainActivity.class));
                                }
                                finish();
                            }
                        });
                break;
            case R.id.btn_1:
                jumpAct(RgActivity.class);
                break;
        }
    }
}
