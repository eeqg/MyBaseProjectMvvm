package com.example.wp.resource.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by wp on 2019/5/8.
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {
	@Override
	public void transformPage(@NonNull View view, float position) {
		// float maxXScale = 0.5F;
		// float minXScale = 0.3F;
		// float maxYScale = 0.76F;
		// float minYScale = 0.6F;
		float maxXScale = 0.60F;
		float minXScale = 0.36F;
		float maxYScale = 0.86F;
		float minYScale = 0.76F;
		float translationOffset = 0.48F;
		
		// LogUtils.d("-----position = " + position);
		if (position == 0) {
			view.setScaleX(maxXScale);
			view.setScaleY(maxYScale);
			view.setTranslationX(0);
		} else if (position < 0) {//滑出的页
			view.setScaleX(maxXScale + (maxXScale - minXScale) * position);
			view.setScaleY(maxYScale + (maxYScale - minYScale) * position);
			view.setTranslationX(view.getWidth() * -position * translationOffset);
		} else if (position > 0) {//滑进的页
			view.setScaleX(maxXScale - (maxXScale - minXScale) * position);
			view.setScaleY(maxYScale - (maxYScale - minYScale) * position);
			view.setTranslationX(view.getWidth() * -position * translationOffset);
		}
		
		// //position小于等于1的时候，代表page已经位于中心item的最左边，
		// //此时设置为最小的缩放率以及最大的旋转度数
		// if (position <= -1){
		// 	page.setScaleX(MIN_SCALE);
		// 	page.setScaleY(MIN_SCALE);
		// 	imageTag.setAlpha((float) 1);
		// }//position从0变化到-1，page逐渐向左滑动
		// else if (position < 0){
		// 	imageTag.setAlpha(Math.abs(position));
		// 	page.setScaleX(scaleFactor);
		// 	page.setScaleY(scaleFactor);
		// }//position从0变化到1，page逐渐向右滑动
		// else if (position >=0 && position < 1){
		// 	imageTag.setAlpha(position);
		// 	page.setScaleX(scaleFactor);
		// 	page.setScaleY(scaleFactor);
		// }//position大于等于1的时候，代表page已经位于中心item的最右边
		// else if (position >= 1){
		// 	imageTag.setAlpha((float)1);
		// 	page.setScaleX(scaleFactor);
		// 	page.setScaleY(scaleFactor);
		// }
	}
}
