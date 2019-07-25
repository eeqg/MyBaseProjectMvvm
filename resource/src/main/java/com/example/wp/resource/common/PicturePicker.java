package com.example.wp.resource.common;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.example.wp.resource.common.imageloader.MatisseGlideEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

/**
 * Created by wp on 2019/4/10.
 */
public class PicturePicker {
	
	public static void pickSingle(Activity activity, int requestCode) {
		pickMulti(activity, requestCode, 1);
	}
	
	public static void pickCrop(Activity activity, int requestCode, float xRatio, float yRatio) {
	
	}
	
	public static void pickMulti(Activity activity, int requestCode, int count) {
		Matisse.from(activity)
				.choose(MimeType.allOf())
				.countable(true)
				.capture(true) //是否提供拍照功能
				.captureStrategy(new CaptureStrategy(true, "com.pl.handbag.fileprovider"))//存储到哪里
				.maxSelectable(count)
				// .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
				// .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
				.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
				.thumbnailScale(0.85f)
				.imageEngine(new MatisseGlideEngine())
				.forResult(requestCode);
	}
	
	public static void pickMulti(Fragment fragment, int requestCode, int count) {
		Matisse.from(fragment)
				.choose(MimeType.allOf())
				.countable(true)
				.capture(true) //是否提供拍照功能
				.captureStrategy(new CaptureStrategy(true, "com.example.wp.rusiling.fileprovider"))//存储到哪里
				.maxSelectable(count)
				// .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
				// .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
				.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
				.thumbnailScale(0.85f)
				.imageEngine(new MatisseGlideEngine())
				.forResult(requestCode);
	}
	
	public static Uri pickCamera(Activity activity, int requestCode) {
		Uri uri;
		File outputImage = new File(activity.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = FileProvider.getUriForFile(activity,
					activity.getApplicationContext().getPackageName() + ".file.provider", outputImage);
		} else {
			uri = Uri.fromFile(outputImage);
		}
		//启动相机程序
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(intent, requestCode);
		
		return uri;
	}
}
