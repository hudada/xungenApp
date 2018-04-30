package com.example.bsproperty.bean;

import java.util.ArrayList;

/**
 * Created by wdxc1 on 2018/1/28.
 */

public class UserListBean extends BaseResponse {
    private ArrayList<UserBean> data;

    public ArrayList<UserBean> getData() {
        return data;
    }

    public void setData(ArrayList<UserBean> data) {
        this.data = data;
    }
}
