package com.example.wp.resource.basic;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by wp on 2019/3/21.
 */
public abstract class BasicActivity<B extends ViewDataBinding> extends AppCompatActivity implements BasicViewImp {
	protected Context mContext;
	protected B dataBinding;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataBinding = DataBindingUtil.setContentView(this, getContentView());
		this.mContext = this;
		
		init();
		initView();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
