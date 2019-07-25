package com.example.wp.mybaseprojectmvvm.common;

import android.content.Context;

import com.example.wp.resource.basic.BasicApp;

/**
 * Created by wp on 2019/3/22.
 */
public class App extends BasicApp {
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void logout(Context context) {
	
	}
	
	@Override
	public void requestLogin(Context context, int requestCode) {
	
	}
	
	@Override
	public void onBasicActivityResumed(Context context) {
	
	}
	
	@Override
	public void onBasicActivityPaused(Context context) {
	
	}
}
