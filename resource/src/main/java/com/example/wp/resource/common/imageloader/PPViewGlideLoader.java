package com.example.wp.resource.common.imageloader;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wp.resource.widget.preview.loader.PPViewImageLoader;

/**
 * Created by wp on 2019/4/17.
 */
public class PPViewGlideLoader implements PPViewImageLoader {
	@Override
	public void displayImage(ImageView imageView, String url) {
		Glide.with(imageView.getContext()).load(url).into(imageView);
	}
}
