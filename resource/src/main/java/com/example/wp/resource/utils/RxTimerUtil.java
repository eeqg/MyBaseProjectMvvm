package com.example.wp.resource.utils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RxTimerUtil {
	
	// private static CompositeDisposable mCompositeDisposable = new CompositeDisposable();
	private static Disposable mDisposable;
	
	/**
	 * milliseconds毫秒后执行next操作
	 */
	public static void timer(long milliseconds, final IRxNext next) {
		Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Long>() {
					@Override
					public void onSubscribe(@NonNull Disposable disposable) {
						mDisposable = disposable;
						// mCompositeDisposable.add(disposable);
					}
					
					@Override
					public void onNext(@NonNull Long number) {
						if (next != null) {
							next.doNext(number);
						}
					}
					
					@Override
					public void onError(@NonNull Throwable e) {
						//取消订阅
						cancel();
					}
					
					@Override
					public void onComplete() {
						//取消订阅
						cancel();
					}
				});
	}
	
	
	/**
	 * 每隔milliseconds毫秒后执行next操作
	 */
	public static void interval(long milliseconds, final IRxNext next) {
		Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Long>() {
					Disposable d = null;
					
					@Override
					public void onSubscribe(@NonNull Disposable disposable) {
						d = disposable;
						// mCompositeDisposable.add(disposable);
					}
					
					@Override
					public void onNext(@NonNull Long number) {
						if (next != null) {
							next.doNext(number);
						}
					}
					
					@Override
					public void onError(@NonNull Throwable e) {
						d.dispose();
					}
					
					@Override
					public void onComplete() {
						d.dispose();
					}
				});
	}
	
	
	/**
	 * 取消订阅
	 */
	public static void cancel() {
		if (mDisposable != null && !mDisposable.isDisposed()) {
			mDisposable.dispose();
			mDisposable = null;
		}
		// mCompositeDisposable.clear();
	}
	
	public interface IRxNext {
		void doNext(long number);
	}
}
