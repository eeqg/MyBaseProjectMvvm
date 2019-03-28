package com.example.wp.resource.manager;

import com.example.wp.resource.common.CustomGlideTransform;
import com.example.wp.resource.common.imageloader.GlideImageLoader;
import com.example.wp.resource.widget.RatioImageView;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

/**
 * Created by wp on 2019/3/26.
 */
public class CommonViewBinding {
	
	@BindingAdapter(value = {"imageUrl", "placeholder", "circle", "radius", "stroke", "strokeColor"}, requireAll = false)
	public static void loadImage(RatioImageView imageView, String url, @DrawableRes int defaultImage,
	                             boolean circle, int radius, int stroke, int strokeColor) {
		GlideImageLoader.getInstance().load(imageView, url, new CustomGlideTransform(circle, radius, stroke, strokeColor));
		// GlideImageLoader.getInstance().load(imageView, url, defaultImage);
	}
}
