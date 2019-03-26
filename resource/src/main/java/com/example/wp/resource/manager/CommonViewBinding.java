package com.example.wp.resource.manager;

import android.graphics.drawable.Drawable;

import com.example.wp.resource.R;
import com.example.wp.resource.utils.LogUtils;
import com.example.wp.resource.widget.RatioImageView;

import androidx.databinding.BindingAdapter;

/**
 * Created by wp on 2019/3/26.
 */
public class CommonViewBinding {
	
	@BindingAdapter(value = {"imageUrl", "placeholder"}, requireAll = false)
	public static void setImageUrl(RatioImageView imageView, String url, Drawable placeHolder) {
		// GlideApp.with(imageView.getContext())
		// 		.load(url)
		// 		.into(imageView);
		LogUtils.d("-----" + url);
		imageView.setImageResource(R.mipmap.ic_loading);
	}
}
