package com.example.wp.resource.basic.model;

import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wp on 2019/3/23.
 */
public class BasicViewModel extends ViewModel {
	private final String TAG = getClass().getSimpleName();
	private CompositeDisposable mCompositeDisposable;
	
	protected void registerDisposable(Disposable d) {
		if (this.mCompositeDisposable == null || this.mCompositeDisposable.isDisposed()) {
			this.mCompositeDisposable = new CompositeDisposable();
		}
		this.mCompositeDisposable.add(d);
	}
	
	@Override
	protected void onCleared() {
		super.onCleared();
		if (this.mCompositeDisposable != null) {
			this.mCompositeDisposable.dispose();
		}
	}
}
