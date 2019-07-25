package com.example.wp.resource.widget.picturelayout;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.example.wp.resource.common.imageloader.GlideImageLoader;
import com.example.wp.resource.widget.PictureLayout;

/**
 * Created by wp on 2019/4/9.
 */
public class PictureLayoutImageLoader implements PictureLayout.ImageLoaderInterface<ImageView> {
    @Override
    public void displayImage(Context context, Uri pictureUri, ImageView imageView) {
        GlideImageLoader.getInstance().load(imageView, pictureUri.toString());
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView view = new ImageView(context);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return view;
    }
}
