package com.example.wp.resource.basic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * Created by wp on 2019/3/21.
 */
public abstract class BasicFragment<B extends ViewDataBinding> extends Fragment implements BasicViewImp {
	
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
}
