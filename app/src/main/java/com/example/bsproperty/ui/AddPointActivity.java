package com.example.bsproperty.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.DenstityUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPointActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.ibtn_sub)
    ImageButton ibtnSub;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.ibtn_add)
    ImageButton ibtnAdd;
    @BindView(R.id.ll_point_list)
    LinearLayout llPointList;
    @BindView(R.id.et_work_name)
    EditText etWorkName;

    private int sum;
    private LayoutInflater inflater;
    private ArrayList<EditText> mPoint;
    private UserBean mUser;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("新的任务");
        btnRight.setText("发布");
        btnRight.setVisibility(View.VISIBLE);

        inflater = LayoutInflater.from(mContext);
        mPoint = new ArrayList<>();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_add_point;
    }

    @Override
    protected void loadData() {
        mUser = (UserBean) getIntent().getSerializableExtra("data");
        sum = 1;
        initPoint();
    }

    private void initPoint() {
        llPointList.removeAllViews();
        mPoint.clear();
        for (int i = 0; i < sum; i++) {
            View view = inflater.inflate(R.layout.item_point, null, false);
            EditText editText = (EditText) view.findViewById(R.id.et_name);
            mPoint.add(editText);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DenstityUtils.dp2px(mContext, 50));
            llPointList.addView(view, params);
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_right, R.id.ibtn_sub, R.id.ibtn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:
                String workName = etWorkName.getText().toString().trim();
                if (TextUtils.isEmpty(workName)) {
                    showToast(etWorkName.getHint().toString().trim());
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                for (EditText editText : mPoint) {
                    String name = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        showToast(editText.getHint().toString().trim());
                        return;
                    }
                    buffer.append(name);
                    buffer.append(",");
                }
                String name = buffer.substring(0, buffer.length() - 1);
                OkHttpTools.sendPost(mContext, ApiManager.POINT_ADD)
                        .addParams("pname", name)
                        .addParams("uid", mUser.getId() + "")
                        .addParams("wname", workName)
                        .build()
                        .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                            @Override
                            public void onResponse(BaseResponse baseResponse) {
                                showToast("发布成功");
                                finish();
                            }
                        });
                break;
            case R.id.ibtn_sub:
                if (sum == 1) {
                    showToast("至少需要一个巡更点");
                    return;
                }
                sum--;
                initPoint();
                tvSum.setText(sum + "");
                break;
            case R.id.ibtn_add:
                sum++;
                initPoint();
                tvSum.setText(sum + "");
                break;
        }
    }
}
