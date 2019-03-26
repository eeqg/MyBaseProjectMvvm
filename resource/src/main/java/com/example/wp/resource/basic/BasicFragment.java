package com.example.wp.resource.basic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wp.resource.basic.model.DataListener;
import com.example.wp.resource.basic.model.StatusInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * Created by wp on 2019/3/21.
 */
public abstract class BasicFragment<B extends ViewDataBinding> extends Fragment
		implements BasicViewImp, DataListener {
	
	protected B dataBinding;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		dataBinding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
		return dataBinding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		init();
		initView();
	}
	
	@Override
	public void dataStart() {
	
	}
	
	@Override
	public void dataStop() {
	
	}
	
	@Override
	public void dataOther(StatusInfo statusInfo) {
	
	}
}
