package com.example.wp.resource.basic.model;

import com.google.gson.annotations.SerializedName;

public class ArrayBean extends BasicBean {
	@SerializedName(value = "totalCount",alternate = "count")
	public int totalCount;
}
