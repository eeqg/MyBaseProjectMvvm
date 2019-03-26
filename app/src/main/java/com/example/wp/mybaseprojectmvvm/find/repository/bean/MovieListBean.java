package com.example.wp.mybaseprojectmvvm.find.repository.bean;

import com.example.wp.resource.basic.model.BasicBean;
import com.example.wp.resource.basic.model.StatusInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by wp on 2018/4/10.
 */

public class MovieListBean {
	public transient StatusInfo statusInfo = new StatusInfo();
	public String title;
	public int total;
	@SerializedName("subjects")
	public ArrayList<MovieItemBean> movieList;
}
