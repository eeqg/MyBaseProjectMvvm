package com.example.wp.resource.basic.model;

import androidx.annotation.NonNull;

public abstract class DataObserver<T extends BasicBean> {
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
	
	final void dataSuccess(@NonNull T basicBean) {
		if (basicBean.statusInfo.isSuccessful()) {
			dataResult(basicBean);
			dataStatus(basicBean.statusInfo);
		} else if (basicBean.statusInfo.isOther()) {
			if (this.dataListener != null) {
				this.dataListener.dataOther(basicBean.statusInfo);
			}
		} else {
			dataStatus(basicBean.statusInfo);
		}
	}
	
	protected void dataError(Throwable throwable) {
		dataStatus(null);
	}
	
	protected abstract void dataResult(T basicBean);
	
	protected abstract void dataStatus(StatusInfo statusInfo);
}
