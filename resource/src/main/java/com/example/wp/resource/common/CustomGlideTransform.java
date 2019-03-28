package com.example.wp.resource.common;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.wp.resource.utils.LogUtils;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * Created by wp on 2019/3/27.
 */
public class CustomGlideTransform extends BitmapTransformation {
	private Paint boarderPaint;
	private final boolean circle;
	private final int radius;
	private final int stroke;
	
	public CustomGlideTransform(boolean circle, int radius, int stroke, int strokeColor) {
		this.circle = circle;
		this.radius = radius;
		this.stroke = stroke;
		LogUtils.d(String.format("------circle:%s-radius:%s-stroke:%s-strokeColor:%s-", circle, radius, stroke, strokeColor));
		
		if (strokeColor == 0) {
			strokeColor = Color.parseColor("#CECECE");
		}
		boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		boarderPaint.setColor(strokeColor);
		boarderPaint.setStyle(Paint.Style.STROKE);
		boarderPaint.setStrokeWidth(stroke);
	}
	
	@Override
	protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap source, int outWidth, int outHeight) {
		int width = source.getWidth();
		int height = source.getHeight();
		Bitmap resultBitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
		
		LogUtils.d(String.format("s.x=%s, s.y=%s, o.x=%s, o.y=%s", width, height, outWidth, outHeight));
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		if (circle) {
			//circle
			int size = Math.min(outWidth, outHeight);
			resultBitmap = pool.get(size, size, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(resultBitmap);
			int x = (outWidth - size) / 2;
			int y = (outHeight - size) / 2;
			Bitmap square = Bitmap.createBitmap(source, x, y, size, size);
			paint.setShader(new BitmapShader(square, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
			float r = Math.min(outWidth, outHeight) / 2f;
			canvas.drawCircle(r, r, r - stroke, paint);
			
			if (stroke > 0) {
				//绘制boarder
				canvas.drawCircle(r, r, r - stroke, boarderPaint);
			}
		} else if (radius > 0) {
			//round
			// resultBitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(resultBitmap);
			paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
			RectF rectF = new RectF(stroke, stroke, outWidth - stroke, outHeight - stroke);
			canvas.drawRoundRect(rectF, radius, radius, paint);
			
			if (stroke > 0) {
				//绘制boarder
				canvas.drawRoundRect(rectF, radius, radius, boarderPaint);
			}
		}
		
		return resultBitmap;
	}
	
	@Override
	public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
	
	}
}
