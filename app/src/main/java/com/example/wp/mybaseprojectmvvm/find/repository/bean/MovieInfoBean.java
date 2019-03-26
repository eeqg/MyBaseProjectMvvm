package com.example.wp.mybaseprojectmvvm.find.repository.bean;

import com.example.wp.resource.basic.model.BasicBean;
import com.example.wp.resource.basic.model.StatusInfo;

import java.util.List;

/**
 * Created by wp on 2018/11/15.
 */
public class MovieInfoBean {
	public transient StatusInfo statusInfo = new StatusInfo();
	public String title;
	public Images images;
	public String year;
	public List<String> tags;
	public List<String> genres;
	public RatingInfoBean rating;
	
	public String summary;
	
	public class Images {
		public String small;
		public String medium;
		public String large;
	}
	
	public class RatingInfoBean {
		public String average;
	}
	
	
}
