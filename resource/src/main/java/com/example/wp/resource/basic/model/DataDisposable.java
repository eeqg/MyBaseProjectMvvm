package com.example.wp.resource.basic.model;

import com.example.wp.resource.basic.BasicApp;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public final class DataDisposable<T> implements Observer<T>, Disposable {
	private ModelLiveData<T> modelLiveData;
	private Disposable disposable;
	
	DataDisposable(ModelLiveData<T> modelLiveData) {
		this.modelLiveData = modelLiveData;
		if (modelLiveData == null) {
			throw new RuntimeException("modelLiveData can't be null!");
		}
	}
	
	@Override
	public void onSubscribe(Disposable disposable) {
		this.disposable = disposable;
		
		ModelLiveData.LiveDataWrapper liveDataWrapper = new ModelLiveData.LiveDataWrapper<T>()
				.dataStart();
		// noinspection unchecked
		this.modelLiveData.setValue(liveDataWrapper);
	}
	
	@Override
	public void onNext(T basicBean) {
		ModelLiveData.LiveDataWrapper liveDataWrapper;
		
		liveDataWrapper = new ModelLiveData.LiveDataWrapper<T>().dataStop();
		// noinspection unchecked
		this.modelLiveData.setValue(liveDataWrapper);
		
		liveDataWrapper = new ModelLiveData.LiveDataWrapper<T>()
				.dataSuccess(basicBean);
		// noinspection unchecked
		this.modelLiveData.setValue(liveDataWrapper);
	}
	
	@Override
	public void onError(Throwable throwable) {
		if (BasicApp.isDebug()) {
			throwable.printStackTrace();
		}
		
		ModelLiveData.LiveDataWrapper liveDataWrapper;
		
		liveDataWrapper = new ModelLiveData.LiveDataWrapper<T>().dataStop();
		// noinspection unchecked
		this.modelLiveData.setValue(liveDataWrapper);
		
		liveDataWrapper = new ModelLiveData.LiveDataWrapper<T>()
				.dataError(throwable);
		// noinspection unchecked
		this.modelLiveData.setValue(liveDataWrapper);
	}
	
	@Override
	public void onComplete() {
		// NO-OP
	}
	
	@Override
	public void dispose() {
		if (this.disposable != null) {
			this.disposable.dispose();
		}
	}
	
	@Override
	public boolean isDisposed() {
		return this.disposable != null && this.disposable.isDisposed();
	}
}
