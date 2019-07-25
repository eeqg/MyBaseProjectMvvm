package com.example.wp.resource.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import com.example.wp.resource.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import cn.shyman.library.refresh.RecyclerAdapter;

/**
 * Created by wp on 2018/4/11.
 */

public class NormalItemDecoration extends RecyclerAdapter.ItemDecoration {
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	private Paint dividerPaint;
	private int dividerSize;
	private int offset;
	
	private int orientation = HORIZONTAL;
	
	@Target(ElementType.PARAMETER)
	@IntDef({HORIZONTAL, VERTICAL})
	public @interface Orientation {
	}
	
	public NormalItemDecoration(Context context) {
		this(context, 0);
	}
	
	public NormalItemDecoration(Context context, int offset) {
		this(context, offset, HORIZONTAL);
	}
	
	public NormalItemDecoration(Context context, int offset, @Orientation int orientation) {
		this.offset = dp2px(context, offset);
		this.orientation = orientation;
		
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
		
		if (this.orientation == HORIZONTAL) {
			outRect.bottom = this.dividerSize;
		} else {
			outRect.right = this.dividerSize;
		}
	}
	
	@Override
	public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
		int childCount = parent.getChildCount();
		for (int index = 0; index < childCount; index++) {
			View view = parent.getChildAt(index);
			if (isSkipDraw(view, parent, state)) {
				continue;
			}
			
			int left = 0, top = 0, right = 0, bottom = 0;
			if (this.orientation == HORIZONTAL) {
				left = view.getLeft() + offset;
				top = view.getBottom();
				right = view.getRight() - offset;
				bottom = top + this.dividerSize;
			} else {
				left = view.getLeft();
				top = view.getBottom() + offset;
				right = view.getRight() + this.dividerSize;
				bottom = top - offset;
			}
			canvas.drawRect(left, top, right, bottom, this.dividerPaint);
		}
	}
	
	private int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}
}
