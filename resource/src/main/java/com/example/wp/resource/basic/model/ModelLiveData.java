package com.example.wp.resource.basic.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public final class ModelLiveData<T> extends LiveData<ModelLiveData.LiveDataWrapper<T>>
		implements Observer<ModelLiveData.LiveDataWrapper<T>> {
	private DataObserver<T> dataObserver;
	
	public void observe(@NonNull LifecycleOwner lifecycleOwner,
	                    @NonNull DataObserver<T> dataObserver) {
		if (this.dataObserver != null) {
			throw new RuntimeException("only can observe one!");
		}
		this.dataObserver = dataObserver;
		super.observe(lifecycleOwner, this);
	}
	
	public void observeForever(@NonNull DataObserver<T> dataObserver) {
		if (this.dataObserver != null) {
			throw new RuntimeException("only can observe one!");
		}
		this.dataObserver = dataObserver;
		super.observeForever(this);
	}
	
	@Override
	public void postValue(LiveDataWrapper<T> value) {
		super.postValue(value);
	}
	
	@Override
	public void setValue(LiveDataWrapper<T> value) {
		super.setValue(value);
	}
	
	@Override
	public void onChanged(@Nullable ModelLiveData.LiveDataWrapper<T> liveDataWrapper) {
		if (liveDataWrapper == null) {
			this.dataObserver.dataError(null);
		} else if (liveDataWrapper.isDataStart()) {
			this.dataObserver.dataStart();
		} else if (liveDataWrapper.isDataStop()) {
			this.dataObserver.dataStop();
		} else if (liveDataWrapper.isDataSuccess()) {
			this.dataObserver.dataSuccess(liveDataWrapper.basicBean);
		} else if (liveDataWrapper.isDataError()) {
			this.dataObserver.dataError(liveDataWrapper.throwable);
		}
	}
	
	public final DataDisposable<T> dispose() {
		return new DataDisposable<>(this);
	}
	
	static class LiveDataWrapper<T> {
		private static final int DATA_START = 1;
		private static final int DATA_STOP = 2;
		private static final int DATA_SUCCESS = 3;
		private static final int DATA_ERROR = 4;
		
		private int taskStatus;
		private T basicBean;
		private Throwable throwable;
		
		LiveDataWrapper<T> dataStart() {
			this.taskStatus = DATA_START;
			return this;
		}
		
		boolean isDataStart() {
			return this.taskStatus == DATA_START;
		}
		
		LiveDataWrapper<T> dataStop() {
			this.taskStatus = DATA_STOP;
			return this;
		}
		
		boolean isDataStop() {
			return this.taskStatus == DATA_STOP;
		}
		
		LiveDataWrapper<T> dataSuccess(T basicBean) {
			this.taskStatus = DATA_SUCCESS;
			this.basicBean = basicBean;
			return this;
		}
		
		boolean isDataSuccess() {
			return this.taskStatus == DATA_SUCCESS;
		}
		
		LiveDataWrapper<T> dataError(Throwable throwable) {
			this.taskStatus = DATA_ERROR;
			this.throwable = throwable;
			return this;
		}
		
		boolean isDataError() {
			return this.taskStatus == DATA_ERROR;
		}
	}
}
