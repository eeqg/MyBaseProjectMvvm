package com.example.wp.resource.basic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wp.resource.R;
import com.example.wp.resource.basic.model.DataListener;
import com.example.wp.resource.basic.model.StatusInfo;
import com.example.wp.resource.common.LoadingDialog;

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
	private LoadingDialog loadingDialog;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		dataBinding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
		return dataBinding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		loadingDialog = new LoadingDialog(getContext());
		
		init();
		initView();
	}
	
	protected void subscribe() {
	}
	
	protected void promptMessage(int resId) {
		promptMessage(getString(resId));
	}
	
	protected void promptMessage(String msg) {
		BasicApp.toast(msg);
	}
	
	public void promptMessage(StatusInfo statusInfo) {
		if (statusInfo == null) {
			promptMessage(R.string.network_request_error);
		} else {
			promptMessage(statusInfo.statusMessage);
		}
	}
	
	public void promptFailure(StatusInfo statusInfo) {
		if (statusInfo == null) {
			promptMessage(R.string.network_request_error);
		} else if (!statusInfo.isSuccessful()) {
			promptMessage(statusInfo.statusMessage);
		}
	}
	
	@Override
	public void dataStart() {
		// showLoading();
	}
	
	@Override
	public void dataStop() {
		// hideLoading();
	}
	
	@Override
	public void dataOther(StatusInfo statusInfo) {
		promptMessage(statusInfo);
		BasicApp.INSTANCE.requestLogin(getContext(), BasicConst.REQUEST_CODE_LOGIN);
	}
	
	protected void showLoading() {
		if (!loadingDialog.isShowing()) {
			loadingDialog.show();
		}
	}
	
	protected void hideLoading() {
		if (loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}
}
