package com.example.bsproperty.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class RgVoiceActivity extends BaseActivity {

    @BindView(R.id.tv_v_pwd1)
    TextView tvVPwd1;
    @BindView(R.id.tv_v_pwd2)
    TextView tvVPwd2;
    @BindView(R.id.tv_v_pwd3)
    TextView tvVPwd3;
    @BindView(R.id.tv_v_pwd4)
    TextView tvVPwd4;
    @BindView(R.id.tv_v_pwd5)
    TextView tvVPwd5;
    private long id;
    private SpeakerVerifier mVerify;
    private String mNumPwd;
    private int sum = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mVerify = SpeakerVerifier.createVerifier(mContext, new InitListener() {

            @Override
            public void onInit(int errorCode) {
                if (ErrorCode.SUCCESS == errorCode) {
                    showToast("引擎初始化成功");
                } else {
                    showToast("引擎初始化失败，错误码：" + errorCode);
                }
            }
        });
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_rg_voice;
    }

    @Override
    protected void loadData() {
        id = getIntent().getLongExtra("id", 0);

        PermissionGen.with((Activity) mContext)
                .addRequestCode(521)
                .permissions(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).request();
    }

    @PermissionSuccess(requestCode = 521)
    private void ok() {
        mVerify.setParameter(SpeechConstant.MFV_SCENES, "ivp");
        mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + 3);
        mVerify.getPasswordList(new SpeechListener() {
            @Override
            public void onEvent(int i, Bundle bundle) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {


//                checkVoice();

                rgVoice(bytes);
            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }
        });
    }

    private void rgVoice(byte[] bytes) {
        StringBuffer numberString = new StringBuffer();
        String result = new String(bytes);
        mVerify.setParameter(SpeechConstant.ISV_SST, "train");
        mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + 3);

        try {
            JSONObject object = new JSONObject(result);
            JSONArray pwdArray = object.optJSONArray("num_pwd");
            numberString.append(pwdArray.get(0));
            tvVPwd1.setText(pwdArray.get(0).toString());
            for (int i = 1; i < pwdArray.length(); i++) {
                int j = i + 1;
                numberString.append("-" + pwdArray.get(i));
                int id = getResources().getIdentifier("tv_v_pwd" + j, "id", getPackageName());
                ((TextView) findViewById(id)).setText(pwdArray.get(i).toString());
            }
            mNumPwd = numberString.toString();
            mVerify.setParameter(SpeechConstant.ISV_PWD, mNumPwd);
            mVerify.setParameter(SpeechConstant.AUTH_ID, "voice" + id);
            mVerify.startListening(new VerifierListener() {
                @Override
                public void onVolumeChanged(int i, byte[] bytes) {
                }

                @Override
                public void onBeginOfSpeech() {
                }

                @Override
                public void onEndOfSpeech() {
                }

                @Override
                public void onResult(VerifierResult verifierResult) {
                    if (verifierResult.err == 0) {
                        switch (sum) {
                            case 0:
                                tvVPwd1.setTextColor(getResources().getColor(R.color.green));
                                break;
                            case 1:
                                tvVPwd2.setTextColor(getResources().getColor(R.color.green));
                                break;
                            case 2:
                                tvVPwd3.setTextColor(getResources().getColor(R.color.green));
                                break;
                            case 3:
                                tvVPwd4.setTextColor(getResources().getColor(R.color.green));
                                break;
                            case 4:
                                tvVPwd5.setTextColor(getResources().getColor(R.color.green));
                                OkHttpTools.sendPost(mContext, ApiManager.REGISTER)
                                        .addParams("id", id + "")
                                        .build()
                                        .execute(new BaseCallBack<UserObjBean>(mContext, UserObjBean.class) {
                                            @Override
                                            public void onResponse(UserObjBean userObjBean) {
                                                showToast("注册成功");
                                                finish();
                                            }
                                        });
                                break;
                        }
                        sum++;
                    } else {
                        switch (sum) {
                            case 0:
                                tvVPwd1.setTextColor(getResources().getColor(R.color.red));
                                break;
                            case 1:
                                tvVPwd2.setTextColor(getResources().getColor(R.color.red));
                                break;
                            case 2:
                                tvVPwd3.setTextColor(getResources().getColor(R.color.red));
                                break;
                            case 3:
                                tvVPwd4.setTextColor(getResources().getColor(R.color.red));
                                break;
                            case 4:
                                tvVPwd5.setTextColor(getResources().getColor(R.color.red));
                                break;
                        }
                        switch (verifierResult.err) {
                            case VerifierResult.MSS_ERROR_IVP_GENERAL:
                                showToast("内核异常");
                                break;
                            case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                                showToast("出现截幅");
                                break;
                            case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                                showToast("太多噪音");
                                break;
                            case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                                showToast("录音太短");
                                break;
                            case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                                showToast("验证不通过，您所读的文本不一致");
                                break;
                            case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                                showToast("音量太低");
                                break;
                            case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                                showToast("音频长达不到自由说的要求");
                                break;
                            default:
                                showToast("验证不通过,相似度仅为" + verifierResult.score + "%。");
                                break;
                        }
                    }

                }

                @Override
                public void onError(SpeechError speechError) {
                    Log.e("vhdd", "" + speechError.getErrorCode());
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @PermissionFail(requestCode = 521)
    private void showTip1() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-权限 中开启相关权限");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mVerify.cancel();
        mVerify.destroy();
    }
}
