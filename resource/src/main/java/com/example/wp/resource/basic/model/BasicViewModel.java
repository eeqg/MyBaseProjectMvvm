package com.example.wp.resource.basic.model;

import android.util.Log;

import androidx.lifecycle.ViewModel;

/**
 * Created by wp on 2019/3/23.
 */
public class BasicViewModel extends ViewModel {
	private final String TAG  = getClass().getSimpleName();
	
	
	@Override
	protected void onCleared() {
		super.onCleared();
		Log.d(TAG, "-----onCleared()-----");
	}
}
