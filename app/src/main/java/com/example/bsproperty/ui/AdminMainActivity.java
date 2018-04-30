package com.example.bsproperty.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.eventbus.LoginEvent;
import com.example.bsproperty.fragment.AdminFragment01;
import com.example.bsproperty.fragment.UserFragment01;
import com.example.bsproperty.fragment.UserFragment02;
import com.example.bsproperty.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class AdminMainActivity extends BaseActivity {


    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.tb_bottom)
    TabLayout tbBottom;

    private long backTime;
    private AdminFragment01 fragment01;
    private UserFragment02 fragment02;
    private ArrayList<Fragment> fragments;
    private MyFragmentPagerAdapter adapter;
    private String[] tabs = new String[]{
            "人员列表", "我的"
    };
    private int[] tabIcons = {
            R.drawable.ic_format_list_bulleted_grey_400_24dp,
            R.drawable.ic_person_grey_400_24dp
    };
    private int[] tabIconsPressed = {
            R.drawable.ic_format_list_bulleted_white_24dp,
            R.drawable.ic_person_white_24dp
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        PermissionGen.with((Activity) mContext)
                .addRequestCode(521)
                .permissions(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET
                ).request();

        EventBus.getDefault().register(this);
        MyApplication.getInstance().setUserBean(SpUtils.getUserBean(this));

        fragment01 = new AdminFragment01();
        fragment02 = new UserFragment02();
        fragments = new ArrayList<>();
        fragments.add(fragment01);
        fragments.add(fragment02);


        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        vpContent.setOffscreenPageLimit(2);
        vpContent.setAdapter(adapter);
        tbBottom.setTabMode(TabLayout.MODE_FIXED);
        tbBottom.setupWithViewPager(vpContent);

        for (int i = 0; i < fragments.size(); i++) {
            tbBottom.getTabAt(i).setCustomView(getTabView(i));
        }

        tbBottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_nav, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(tabs[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(tabIcons[position]);

        if (position == 0) {
            txt_title.setTextColor(Color.WHITE);
            img_title.setImageResource(tabIconsPressed[position]);
        } else {
            txt_title.setTextColor(getResources().getColor(R.color.tab_nav_grey));
            img_title.setImageResource(tabIcons[position]);
        }
        return view;
    }

    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setTextColor(Color.WHITE);
        vpContent.setCurrentItem(tbBottom.getSelectedTabPosition());
        img_title.setImageResource(tabIconsPressed[tbBottom.getSelectedTabPosition()]);
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.tab_nav_grey));
        img_title.setImageResource(tabIcons[tbBottom.getSelectedTabPosition()]);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int getRootViewId() {
        return R.layout.activity_user_main;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backTime < 2000) {
            super.onBackPressed();
        } else {
            showToast(this, "再按一次，退出程序");
            backTime = System.currentTimeMillis();
        }
        backTime = System.currentTimeMillis();
    }


    @PermissionSuccess(requestCode = 521)
    private void ok() {
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
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
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

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
