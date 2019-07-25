package com.example.wp.resource.basic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.wp.resource.R;
import com.example.wp.resource.basic.model.DataListener;
import com.example.wp.resource.basic.model.StatusInfo;
import com.example.wp.resource.common.LoadingDialog;
import com.example.wp.resource.utils.StatusBarUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by wp on 2019/3/21.
 */
public abstract class BasicActivity<B extends ViewDataBinding> extends AppCompatActivity
		implements BasicViewImp, DataListener {
	protected Context mContext;
	protected B dataBinding;
	private boolean isLightMode;
	
	private LoadingDialog loadingDialog;
	private AlertDialog mAlertDialog;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
			fixOrientation();
		}
		super.onCreate(savedInstanceState);
		dataBinding = DataBindingUtil.setContentView(this, getContentView());
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.mContext = this;
		
		loadingDialog = new LoadingDialog(this);
		
		init();
		initView();
		subscribe();
	}
	
	protected void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}
	
	protected void setLightMode() {
		isLightMode = true;
		StatusBarUtil.setDarkMode(this);
		StatusBarUtil.setColor(this, getResources().getColor(R.color.colorWhite), 0);
	}
	
	protected void setDarkMode() {
		isLightMode = false;
		StatusBarUtil.setLightMode(this);
		StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
	}
	
	protected void showAlertDialog(@Nullable String title, @Nullable String message,
	                               @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
	                               @NonNull String positiveText,
	                               @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
	                               @NonNull String negativeText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
		builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
		mAlertDialog = builder.show();
	}
	
	protected void subscribe() {
	}
	
	@Override
	public void dataStart() {
		// showLoading();
	}
	
	@Override
	public void dataStop() {
		hideLoading();
	}
	
	@Override
	public void dataOther(StatusInfo statusInfo) {
		// promptMessage(statusInfo);
		promptMessage("请先登录");
		BasicApp.INSTANCE.requestLogin(this, BasicConst.REQUEST_CODE_LOGIN);
		finish();
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
	
	protected ToolbarAction createBack() {
		if (isLightMode) return ToolbarAction.createBack(this, R.mipmap.ic_back_black);
		return ToolbarAction.createBack(this, R.mipmap.ic_back_white);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		BasicApp.INSTANCE.onBasicActivityResumed(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		BasicApp.INSTANCE.onBasicActivityPaused(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void setRequestedOrientation(int requestedOrientation) {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
			return;
		}
		super.setRequestedOrientation(requestedOrientation);
	}
	
	private boolean isTranslucentOrFloating() {
		boolean isTranslucentOrFloating = false;
		try {
			int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable")
					.getField("Window").get(null);
			final TypedArray ta = obtainStyledAttributes(styleableRes);
			Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
			m.setAccessible(true);
			isTranslucentOrFloating = (boolean) m.invoke(null, ta);
			m.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isTranslucentOrFloating;
	}
	
	private void fixOrientation() {
		try {
			Field field = Activity.class.getDeclaredField("mActivityInfo");
			field.setAccessible(true);
			ActivityInfo o = (ActivityInfo) field.get(this);
			o.screenOrientation = -1;
			field.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
