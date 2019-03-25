package com.example.wp.resource.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.example.wp.resource.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import cn.shyman.library.refresh.RecyclerAdapter;

/**
 * Created by wp on 2018/4/11.
 */

public class NormalItemDecoration extends RecyclerAdapter.ItemDecoration {
	private Paint dividerPaint;
	private int dividerSize;
	
	public NormalItemDecoration(Context context) {
		this.dividerPaint = new Paint();
		this.dividerPaint.setAntiAlias(true);
		this.dividerPaint.setColor(ContextCompat.getColor(context, R.color.colorThemeDivider));
		this.dividerSize = context.getResources().getDimensionPixelSize(R.dimen.dimenThemeDivider);
	}
	
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (isSkipDraw(view, parent, state)) {
			return;
		}
		
		outRect.bottom = this.dividerSize;
	}
	
	@Override
	public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
		int childCount = parent.getChildCount();
		for (int index = 0; index < childCount; index++) {
			View view = parent.getChildAt(index);
			if (isSkipDraw(view, parent, state)) {
				continue;
			}
			
			int left = view.getLeft();
			int top = view.getBottom();
			int right = view.getRight();
			int bottom = top + this.dividerSize;
			canvas.drawRect(left, top, right, bottom, this.dividerPaint);
		}
	}
}
