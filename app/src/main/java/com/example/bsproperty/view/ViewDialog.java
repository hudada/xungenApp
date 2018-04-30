package com.example.bsproperty.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.utils.DenstityUtils;

/**
 * Created by yezi on 2018/1/27.
 */

public abstract class ViewDialog extends Dialog {

    public ViewDialog(Context context,int layoutId) {
        super(context, R.style.MyDialog);
        setContentView(layoutId);

        initView();

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DenstityUtils.screenWidth((Activity) context) - DenstityUtils.dp2px(context, 100);
        window.setAttributes(params);
    }

    protected abstract void initView();
}
