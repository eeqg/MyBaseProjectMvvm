package com.example.wp.resource.common.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.wp.resource.R;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class GlideImageLoader implements ImageLoader {
	private static GlideImageLoader INSTANCE;
	
	private GlideImageLoader() {
	}
	
	public static GlideImageLoader getInstance() {
		if (INSTANCE == null) {
			synchronized (GlideImageLoader.class) {
				if (INSTANCE == null) {
					INSTANCE = new GlideImageLoader();
				}
			}
		}
		return INSTANCE;
	}
	
	@Override
	public void load(@NonNull ImageView imageView, String imageUrl) {
		load(imageView, imageUrl, R.mipmap.ic_placeholder);
	}
	
	@Override
	public void load(@NonNull ImageView imageView, String imageUrl, @DrawableRes int defaultImage) {
		if (defaultImage == 0) {
			defaultImage = R.mipmap.ic_placeholder;
		}
		RequestOptions options = new RequestOptions()
				.centerCrop()
				.placeholder(defaultImage)
				.error(defaultImage);
		loadReal(imageView, imageUrl, options);
	}
	
	public void loadBlur(ImageView imageView, String imageUrl) {
		loadBlur(imageView, imageUrl, 25, 1);
	}
	
	@Override
	public void loadBlur(@NonNull ImageView imageView, String imageUrl, int radius, int sampling) {
		RequestOptions requestOptions = bitmapTransform(new BlurTransformation(radius, sampling))
				.placeholder(R.mipmap.ic_placeholder)
				.error(R.mipmap.ic_placeholder);
		loadReal(imageView, imageUrl, requestOptions);
	}
	
	@Override
	public void loadCircle(@NonNull ImageView imageView, String imageUrl) {
		RequestOptions requestOptions = new RequestOptions()
				.placeholder(R.mipmap.ic_placeholder)
				.error(R.mipmap.ic_placeholder)
				.transform(new CircleCrop());
		loadReal(imageView, imageUrl, requestOptions);
	}
	
	@Override
	public void loadRound(@NonNull ImageView imageView, String imageUrl, int radius) {
		RequestOptions options = bitmapTransform(new RoundedCorners(radius))
				.placeholder(R.mipmap.ic_placeholder)
				.error(R.mipmap.ic_placeholder);
		loadReal(imageView, imageUrl, options);
	}
	
	public void loadTopRounded(ImageView imageView, String imageUrl, int radius) {
		loadRounded(imageView, imageUrl, radius, 0, RoundedCornersTransformation.CornerType.TOP);
	}
	
	public void loadBottomRounded(ImageView imageView, String imageUrl, int radius) {
		loadRounded(imageView, imageUrl, radius, 0, RoundedCornersTransformation.CornerType.BOTTOM);
	}
	
	public void loadRounded(ImageView imageView, String imageUrl, int radius, int margin,
	                        RoundedCornersTransformation.CornerType cornerType) {
		RequestOptions options = bitmapTransform(new RoundedCornersTransformation(radius, margin, cornerType))
				.placeholder(R.mipmap.ic_placeholder)
				.error(R.mipmap.ic_placeholder);
		loadReal(imageView, imageUrl, options);
	}
	
	@Override
	public void load(@NonNull ImageView imageView, String imageUrl, Transformation<Bitmap> transformation) {
		RequestOptions options = bitmapTransform(transformation)
				.placeholder(R.mipmap.ic_placeholder)
				.error(R.mipmap.ic_placeholder);
		loadReal(imageView, imageUrl, options);
	}
	
	private void loadReal(@NonNull ImageView imageView, String imageUrl, RequestOptions options) {
		if (imageUrl == null) {
			return;
		}
		Glide.with(imageView.getContext())
				.load(imageUrl)
				.apply(options)
				.into(imageView);
	}
}