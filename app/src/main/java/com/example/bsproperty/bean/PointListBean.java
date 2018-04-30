package com.example.bsproperty.bean;

import java.util.ArrayList;

public class PointListBean extends BaseResponse {

	private ArrayList<PointBean> data;

	public ArrayList<PointBean> getData() {
		return data;
	}

	public void setData(ArrayList<PointBean> data) {
		this.data = data;
	}
}
