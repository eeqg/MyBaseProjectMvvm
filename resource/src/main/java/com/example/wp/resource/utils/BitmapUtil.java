package com.example.wp.resource.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

// import com.google.zxing.BarcodeFormat;
// import com.google.zxing.EncodeHintType;
// import com.google.zxing.WriterException;
// import com.google.zxing.common.BitMatrix;
// import com.google.zxing.qrcode.QRCodeWriter;
// import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by kaiwang on 2017/12/1.
 */

public class BitmapUtil {
	
	/**
	 * 保存base64图片到手机图库
	 *
	 * @param context
	 * @param str
	 */
	public static String saveImageFromBase64(Context context, String path, String str, boolean showToast) {
		try {
			byte[] bitmapArray = Base64.decode(str, Base64.DEFAULT);
			//Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			Bitmap bitmap = byteToBitmap(bitmapArray);
			if (bitmap != null) {
				String savePath = saveImageFromBitmap(path, bitmap, Bitmap.CompressFormat.PNG, null);
				
				bitmap.recycle();
				
				CommonUtil.sendImageChangeBroadcast(savePath);
				
				if (showToast) {
					// ToastUtil.showLongToast(context, R.string.toast_save_image);
				}
				
				return savePath;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 保存Drawable下的图片到本地
	 *
	 * @param context
	 * @param drawableId
	 */
	public static String saveImageFromDrawable(String path, Context context, int drawableId) {
		Resources resources = context.getResources();
		BitmapDrawable drawable = (BitmapDrawable) resources.getDrawable(drawableId);
		Bitmap bitmap = drawable.getBitmap();
		
		return saveImageFromBitmap(path, bitmap, Bitmap.CompressFormat.JPEG, null);
	}
	
	/**
	 * 保存bitmap到指定目录
	 *
	 * @param path
	 * @param bitmap
	 * @param format
	 * @return
	 */
	public static String saveImageFromBitmap(String path, Bitmap bitmap, Bitmap.CompressFormat format, String customName) {
		if (bitmap != null) {
			String suffix = ".jpg";
			if (format == Bitmap.CompressFormat.PNG) {
				suffix = ".png";
			}
			String imagePath = path + (TextUtils.isEmpty(customName) ? System.nanoTime() : customName) + suffix;
			try {
				OutputStream outputStream = new FileOutputStream(imagePath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
				outputStream.close();
				//CommonUtil.sendImageChangeBroadcast(imagePath);
				return imagePath;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 保存bitmap到指定目录
	 *
	 * @param path
	 * @param bitmap
	 * @param format
	 * @return
	 */
	public static String saveImageFromBitmap(String path, Bitmap bitmap, Bitmap.CompressFormat format, String customName, int quality) {
		if (bitmap != null) {
			String suffix = ".jpg";
			if (format == Bitmap.CompressFormat.PNG) {
				suffix = ".png";
			}
			String imagePath = path + (TextUtils.isEmpty(customName) ? System.nanoTime() : customName) + suffix;
			try {
				OutputStream outputStream = new FileOutputStream(imagePath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
				outputStream.close();
				CommonUtil.sendImageChangeBroadcast(imagePath);
				return imagePath;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 回收ImageView占用的图像内存;
	 *
	 * @param view
	 */
	public static void recycleImageView(View view) {
		if (view == null) return;
		if (view instanceof ImageView) {
			Drawable drawable = ((ImageView) view).getDrawable();
			if (drawable instanceof BitmapDrawable) {
				Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
				if (bmp != null && !bmp.isRecycled()) {
					((ImageView) view).setImageBitmap(null);
					bmp.recycle();
					bmp = null;
				}
			}
		}
	}
	
	public static Bitmap getCutBitmap(Bitmap bitmap, int width, int height) {
		// 计算缩放比例
		float scaleWidth = ((float) width) / bitmap.getWidth();
		float scaleHeight = ((float) height) / bitmap.getHeight();
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return bitmap;
	}
	
	/**
	 * Get bitmap from specified image path
	 *
	 * @param imgPath
	 * @param sampleSize
	 * @return
	 */
	public static Bitmap getBitmap(String imgPath, Integer sampleSize) {
		// Get bitmap through image path
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = false;
		newOpts.inPurgeable = true;
		newOpts.inInputShareable = true;
		newOpts.inSampleSize = sampleSize;
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeFile(imgPath, newOpts);
	}
	
	/**
	 * Store bitmap into specified image path
	 *
	 * @param bitmap
	 * @param outPath
	 * @throws FileNotFoundException
	 */
	public static void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
		FileOutputStream os = new FileOutputStream(outPath);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
	}
	
	/**
	 * Compress image by pixel, this will modify image width/height.
	 * Used to get thumbnail
	 *
	 * @param imgPath image path
	 * @param pixelW  target pixel of width
	 * @param pixelH  target pixel of height
	 * @return
	 */
	public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
		newOpts.inJustDecodeBounds = true;
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		// Get bitmap info, but notice that bitmap is null now
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 想要缩放的目标尺寸
		float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
		float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (hh == ww) {
			be = 2;
		} else if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		// 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		// 压缩好比例大小后再进行质量压缩
		//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
		return bitmap;
	}
	
	/**
	 * Compress image by size, this will modify image width/height.
	 * Used to get thumbnail
	 *
	 * @param image
	 * @param pixelW target pixel of width
	 * @param pixelH target pixel of height
	 * @return
	 */
	public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, os);
		if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			os.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
		float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		is = new ByteArrayInputStream(os.toByteArray());
		bitmap = BitmapFactory.decodeStream(is, null, newOpts);
		//压缩好比例大小后再进行质量压缩
		//	    return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
		return bitmap;
	}
	
	/**
	 * Compress by quality,  and generate image to the path specified
	 *
	 * @param image
	 * @param outPath
	 * @param maxSize target will be compressed to be smaller than this size.(kb)
	 * @throws IOException
	 */
	public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// scale
		int options = 100;
		// Store the bitmap into output stream(no compress)
		image.compress(Bitmap.CompressFormat.JPEG, options, os);
		// Compress by loop
		while (os.toByteArray().length / 1024 > maxSize) {
			// Clean up os
			os.reset();
			// interval 10
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, os);
		}
		
		// Generate compressed image file
		FileOutputStream fos = new FileOutputStream(outPath);
		fos.write(os.toByteArray());
		fos.flush();
		fos.close();
	}
	
	/**
	 * Compress by quality,  and generate image to the path specified
	 *
	 * @param imgPath
	 * @param outPath
	 * @param maxSize     target will be compressed to be smaller than this size.(kb)
	 * @param needsDelete Whether delete original file after compress
	 * @throws IOException
	 */
	public static void compressAndGenImage(String imgPath, String outPath, int maxSize, boolean needsDelete) throws IOException {
		compressAndGenImage(getBitmap(imgPath, 1), outPath, maxSize);
		
		// Delete original file
		if (needsDelete) {
			File file = new File(imgPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	/**
	 * Ratio and generate thumb to the path specified
	 *
	 * @param image
	 * @param outPath
	 * @param pixelW  target pixel of width
	 * @param pixelH  target pixel of height
	 * @throws FileNotFoundException
	 */
	public static void ratioAndGenThumb(Bitmap image, String outPath, float pixelW, float pixelH) throws FileNotFoundException {
		Bitmap bitmap = ratio(image, pixelW, pixelH);
		storeImage(bitmap, outPath);
	}
	
	/**
	 * Ratio and generate thumb to the path specified
	 *
	 * @param imgPath
	 * @param outPath
	 * @param pixelW      target pixel of width
	 * @param pixelH      target pixel of height
	 * @param needsDelete Whether delete original file after compress
	 * @throws FileNotFoundException
	 */
	public static void ratioAndGenThumb(String imgPath, String outPath, float pixelW, float pixelH, boolean needsDelete) throws FileNotFoundException {
		Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
		storeImage(bitmap, outPath);
		
		// Delete original file
		if (needsDelete) {
			File file = new File(imgPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	/**
	 * bitmap -> byte
	 *
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * byte to bitmap
	 *
	 * @param imgByte
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] imgByte) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		InputStream input = new ByteArrayInputStream(imgByte);
		SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
				input, null, options));
		Bitmap bitmap = (Bitmap) softRef.get();
		if (imgByte != null) {
			imgByte = null;
		}
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * view to bitmap
	 *
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
		
	}
	
	// /**
	//  * 生成二维码图片
	//  */
	// public static String getBitmapFromQCode(String content, int widthPix, int heightPix) {
	// 	String path = CommonUtil.getAppImageCachePath() + System.nanoTime() + ".jpg";
	// 	//配置参数
	// 	Map<EncodeHintType, Object> hints = new HashMap<>();
	// 	hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	// 	//容错级别
	// 	hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	// 	//设置空白边距的宽度
	// 	hints.put(EncodeHintType.MARGIN, 1); //default is 4
	//
	// 	// 图像数据转换，使用了矩阵转换
	// 	BitMatrix bitMatrix = null;
	// 	try {
	// 		bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
	// 				heightPix, hints);
	// 	} catch (WriterException e) {
	// 		e.printStackTrace();
	// 	}
	// 	int[] pixels = new int[widthPix * heightPix];
	// 	// 下面这里按照二维码的算法，逐个生成二维码的图片，
	// 	// 两个for循环是图片横列扫描的结果
	// 	for (int y = 0; y < heightPix; y++) {
	// 		for (int x = 0; x < widthPix; x++) {
	// 			if (bitMatrix.get(x, y)) {
	// 				pixels[y * widthPix + x] = 0xff000000;
	// 			} else {
	// 				pixels[y * widthPix + x] = 0xffffffff;
	// 			}
	// 		}
	// 	}
	//
	// 	// 生成二维码图片的格式，使用ARGB_8888
	// 	Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
	// 	bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
	//
	// 	try {
	// 		if (bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
	// 				new FileOutputStream(path))) {
	// 			return path;
	// 		}
	// 	} catch (FileNotFoundException e) {
	// 		e.printStackTrace();
	// 	}
	// 	return null;
	// }
	
	public static Bitmap compressBitmap(Bitmap bm, float scale) {
		LogUtils.i("douyao", "压缩前图片的大小" + (bm.getByteCount() / 1024F)
				+ "KB\n宽度为" + bm.getWidth() + "高度为" + bm.getHeight());
		
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, true);
		
		LogUtils.i("douyao", "压缩后图片的大小" + (bm.getByteCount() / 1024F)
				+ "KB\n宽度为" + bm.getWidth() + "高度为" + bm.getHeight());
		
		return bm;
	}
	
	public static Bitmap scaleBitmap(Bitmap bitmap, float w, float h) {
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		float x = 0, y = 0, scaleWidth = width, scaleHeight = height;
		Bitmap newbmp;
		//Log.e("gacmy","width:"+width+" height:"+height);
		if (w > h) {//比例宽度大于高度的情况
			float scale = w / h;
			float tempH = width / scale;
			if (height > tempH) {//
				x = 0;
				y = (height - tempH) / 2;
				scaleWidth = width;
				scaleHeight = tempH;
			} else {
				scaleWidth = height * scale;
				x = (width - scaleWidth) / 2;
				y = 0;
			}
		} else if (w < h) {//比例宽度小于高度的情况
			float scale = h / w;
			float tempW = height / scale;
			if (width > tempW) {
				y = 0;
				x = (width - tempW) / 2;
				scaleWidth = tempW;
				scaleHeight = height;
			} else {
				scaleHeight = width * scale;
				y = (height - scaleHeight) / 2;
				x = 0;
				scaleWidth = width;
			}
		} else {//比例宽高相等的情况
			if (width > height) {
				x = (width - height) / 2;
				y = 0;
				scaleHeight = height;
				scaleWidth = height;
			} else {
				y = (height - width) / 2;
				x = 0;
				scaleHeight = width;
				scaleWidth = width;
			}
		}
		try {
			newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y, (int) scaleWidth, (int) scaleHeight, null, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
			//bitmap.recycle();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newbmp;
	}
	
	
	public Bitmap scaleBitmap(Bitmap bitmap, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmapNew = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmapNew;
	}
	
	/**
	 * View截图
	 */
	public static Bitmap getViewBitmap(View view) {
		if (null == view) {
			return null;
		}
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		// Bitmap bitmap = view.getDrawingCache();
		
		return bitmap;
	}
	
	/**
	 * recycleview截图
	 *
	 * @param view
	 * @return
	 */
	public static Bitmap shotRecyclerView(RecyclerView view) {
		RecyclerView.Adapter adapter = view.getAdapter();
		Bitmap bigBitmap = null;
		if (adapter != null) {
			int size = adapter.getItemCount();
			int height = 0;
			Paint paint = new Paint();
			int iHeight = 0;
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			
			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory / 8;
			LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
			for (int i = 0; i < size; i++) {
				RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
				adapter.onBindViewHolder(holder, i);
				holder.itemView.measure(
						View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
						View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
				holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
						holder.itemView.getMeasuredHeight());
				holder.itemView.setDrawingCacheEnabled(true);
				holder.itemView.buildDrawingCache();
				Bitmap drawingCache = holder.itemView.getDrawingCache();
				if (drawingCache != null) {
					bitmaCache.put(String.valueOf(i), drawingCache);
				}
				height += holder.itemView.getMeasuredHeight();
			}
			
			// bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_4444);
			bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredWidth(), Bitmap.Config.ARGB_4444);
			
			Canvas bigCanvas = new Canvas(bigBitmap);
			Drawable lBackground = view.getBackground();
			if (lBackground instanceof ColorDrawable) {
				ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
				int lColor = lColorDrawable.getColor();
				bigCanvas.drawColor(lColor);
			}
			
			for (int i = 0; i < size; i++) {
				Bitmap bitmap = bitmaCache.get(String.valueOf(i));
				bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
				iHeight += bitmap.getHeight();
				bitmap.recycle();
			}
		}
		return bigBitmap;
	}
}
