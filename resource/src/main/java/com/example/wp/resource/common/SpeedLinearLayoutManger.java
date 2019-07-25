package com.example.wp.resource.common;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wp on 2019/7/11.
 */
public class SpeedLinearLayoutManger extends LinearLayoutManager {
	public SpeedLinearLayoutManger(Context context) {
		super(context);
	}
	
	public SpeedLinearLayoutManger(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
	}
	
	public SpeedLinearLayoutManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
	
	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
		// LinearSmoothScroller linearSmoothScroller =
		// 		new LinearSmoothScroller(recyclerView.getContext());
		// linearSmoothScroller.setTargetPosition(position);
		// startSmoothScroll(linearSmoothScroller);
		
		RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
		smoothScroller.setTargetPosition(position);
		startSmoothScroll(smoothScroller);
	}
	
	private class CenterSmoothScroller extends LinearSmoothScroller {
		
		CenterSmoothScroller(Context context) {
			super(context);
		}
		
		@Nullable
		@Override
		public PointF computeScrollVectorForPosition(int targetPosition) {
			return SpeedLinearLayoutManger.this.computeScrollVectorForPosition(targetPosition);
		}
		
		@Override
		public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
			return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
		}
		
		/**
		 * 滑动速度 : 滑动1px需要的时间(ms)
		 */
		@Override
		protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
			return super.calculateSpeedPerPixel(displayMetrics);
		}
		
		/**
		 * 滑动总时间(milliseconds)
		 */
		@Override
		protected int calculateTimeForScrolling(int dx) {
			// LogUtils.d("-----dx = " + dx);
			int time = super.calculateTimeForScrolling(dx);
			// LogUtils.d("-----time = " + time);
			return time < 200 ? time : 200;
		}
		//
		// @Override
		// protected int getVerticalSnapPreference() {
		// 	return SNAP_TO_START;
		// }
	}
}
