package com.example.wp.resource.basic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wp on 2019/3/21.
 */
public class BasicActivity extends AppCompatActivity {
	protected Context mContext;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
	}
}
