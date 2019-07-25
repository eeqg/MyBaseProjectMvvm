package com.example.wp.resource.basic.model;

import androidx.annotation.NonNull;

public abstract class DataObserver<T> {
	private final String TAG = DataObserver.class.getSimpleName();
	private DataListener dataListener;
	
	protected DataObserver(DataListener dataListener) {
		this.dataListener = dataListener;
	}
	
	protected void dataStart() {
		if (this.dataListener != null) {
			this.dataListener.dataStart();
		}
	}
	
	protected void dataStop() {
		if (this.dataListener != null) {
			this.dataListener.dataStop();
		}
	}
	
	final void dataSuccess(@NonNull T resultBean) {
		// Log.d(TAG, "-----dataSuccess()-----");
		if (resultBean instanceof BasicBean) {
			BasicBean basicBean = (BasicBean) resultBean;
			// Log.d(TAG, "-----dataSuccess()-----statusCode = " + basicBean.statusInfo.statusCode);
			if (basicBean.statusInfo.isSuccessful()) {
				dataResult(resultBean);
				dataStatus(basicBean.statusInfo);
			} else if (basicBean.statusInfo.isOther()) {
				if (this.dataListener != null) {
					this.dataListener.dataOther(basicBean.statusInfo);
				}
			} else {
				dataStatus(basicBean.statusInfo);
			}
		} else {
			dataResult(resultBean);
			dataStatus(new StatusInfo(StatusInfo.STATUS_SUCCESS));
		}
	}
	
	protected void dataError(Throwable throwable) {
		dataStatus(null);
	}
	
	protected abstract void dataResult(T basicBean);
	
	protected abstract void dataStatus(StatusInfo statusInfo);
}
