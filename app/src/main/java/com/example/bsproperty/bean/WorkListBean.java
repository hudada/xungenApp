package com.example.bsproperty.bean;

import java.util.ArrayList;

public class WorkListBean extends BaseResponse {

	private ArrayList<WorkBean> data;

	public ArrayList<WorkBean> getData() {
		return data;
	}

	public void setData(ArrayList<WorkBean> data) {
		this.data = data;
	}
}
