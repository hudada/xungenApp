package com.example.bsproperty.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.ui.BaseActivity;
import com.example.bsproperty.utils.DenstityUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

/**
 * Created by yezi on 2018/1/27.
 */

public class VoiceDialog extends Dialog {

    private OnViewClickListener onViewClickListener;
    private SpeakerVerifier mVerify;
    private Context mContext;

    private TextView tvVPwd1;

    public VoiceDialog(Context context, final OnViewClickListener onViewClickListener) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.dialog_voice);
        mContext = context;
        mVerify = SpeakerVerifier.createVerifier(mContext, new InitListener() {

            @Override
            public void onInit(int errorCode) {
            }
        });

        this.onViewClickListener = onViewClickListener;

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DenstityUtils.screenWidth((Activity) context) - DenstityUtils.dp2px(context, 80);
        window.setAttributes(params);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        initView();
    }

    public interface OnViewClickListener {

        void onSuccess();
    }

    public void initView() {
        tvVPwd1 = (TextView) findViewById(R.id.tv_v_pwd1);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        checkVoice();
    }

    private void checkVoice() {
        mVerify.setParameter(SpeechConstant.ISV_SST, "verify");
        mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + 3);

        String pwd = mVerify.generatePassword(8);
        tvVPwd1.setText(pwd);
        mVerify.setParameter(SpeechConstant.ISV_PWD, pwd);
        mVerify.setParameter(SpeechConstant.AUTH_ID, "voice"+MyApplication.getInstance().getUserBean().getId());
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
                    dismiss();
                    onViewClickListener.onSuccess();
                } else {
                    switch (verifierResult.err) {
                        case VerifierResult.MSS_ERROR_IVP_GENERAL:
                            Log.e("vhdd", "内核异常");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                            Log.e("vhdd", "出现截幅");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                            Log.e("vhdd", "太多噪音");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                            Log.e("vhdd", "录音太短");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                            Log.e("vhdd", "验证不通过，您所读的文本不一致");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                            Log.e("vhdd", "音量太低");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                            Log.e("vhdd", "音频长达不到自由说的要求");
                            break;
                        default:
                            Log.e("vhdd", "验证不通过,相似度仅为" + verifierResult.score + "%。");
                            break;
                    }

                }
            }

            @Override
            public void onError(SpeechError speechError) {
                switch (speechError.getErrorCode()) {
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
                        ((BaseActivity) mContext).showToast("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                        ((BaseActivity) mContext).showToast("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                        ((BaseActivity) mContext).showToast("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                        ((BaseActivity) mContext).showToast("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                        ((BaseActivity) mContext).showToast("验证不通过，您所读的文本不一致");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                        ((BaseActivity) mContext).showToast("音量太低");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                        ((BaseActivity) mContext).showToast("音频长达不到自由说的要求");
                        break;
                }
                checkVoice();
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mVerify.cancel();
        mVerify.destroy();
    }
}
