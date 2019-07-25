package com.example.wp.resource.utils;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.example.wp.resource.basic.BasicApp;
import com.example.wp.resource.basic.BasicConst;
import com.example.wp.resource.common.FixedSpeedScroller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by wp on 2019/5/11.
 */
public class CommonUtil {
	
	/**
	 * textView 添加中划线
	 */
	public static void lineAction(TextView textView) {
		textView.getPaint().setAntiAlias(true);//抗锯齿
		// textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
		// textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
		textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 设置中划线并加清晰
	}
	
	/**
	 * json字符串转Map
	 */
	public static Map<String, Object> jsonToMap(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			@SuppressWarnings("unchecked")
			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = (String) keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void setViewPagerScroll(ViewPager viewPager, Interpolator interpolator, int duration) {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), interpolator);
			scroller.setmDuration(duration);
			mScroller.set(viewPager, scroller);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断设备是否安装淘宝app
	 *
	 * @return
	 */
	public static boolean isPkgInstalledTb() {
		return isAppInstall("com.taobao.taobao");
	}
	
	/**
	 * 判断设备是否安装微信
	 *
	 * @return
	 */
	public static boolean isPkgInstalledWX() {
		return isAppInstall("com.tencent.mm");
	}
	
	/**
	 * 判断是否安装凭多多
	 *
	 * @return
	 */
	public static boolean isPkgInstalledPDD() {
		return isAppInstall("com.xunmeng.pinduoduo");
	}
	
	/**
	 * 判断设备是否安装QQ
	 *
	 * @return
	 */
	public static boolean isPkgInstalledQQ() {
		return isAppInstall("com.tencent.mobileqq");
	}
	
	private static boolean isAppInstall(String packageName) {
		android.content.pm.ApplicationInfo info = null;
		try {
			info = BasicApp.INSTANCE.getPackageManager().getApplicationInfo(packageName, 0);
			return info != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 打开微信
	 */
	
	public static void openWx(Context context) {
		if (isPkgInstalledWX()) {
			openApplicatio(context, "com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
		}
	}
	
	/**
	 * 打开外部应用
	 */
	private static void openApplicatio(Context context, String pkg, String cls) {
		Intent intent = new Intent();
		ComponentName cmp = new ComponentName(pkg, cls);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setComponent(cmp);
		context.startActivity(intent);
		context = null;
	}
	
	/**
	 * 获取手机相册路径
	 *
	 * @return
	 */
	public static String getCameraPath() {
		String cameraPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/DCIM/Camera/";
		File file = new File(cameraPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return cameraPath;
	}
	
	public static String getAppCachePath() {
		String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/Android/data/com.douyao.android/cache/";
		File file = new File(cachePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return cachePath;
	}
	
	public static String getAppImageCachePath() {
		String cachePath = getAppCachePath() + "image/";
		File file = new File(cachePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return cachePath;
	}
	
	public static String getApkCachePath() {
		String cachePath = getAppCachePath() + "apk/";
		File file = new File(cachePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return cachePath;
	}
	
	/**
	 * 更新系统图库
	 *
	 * @param imgPath
	 */
	public static void sendImageChangeBroadcast(String imgPath) {
		if (TextUtils.isEmpty(imgPath)) return;
		File file = new File(imgPath);
		if (file.exists() && file.isFile()) {
			MediaScannerConnection.scanFile(BasicApp.INSTANCE, new String[]{file.getAbsolutePath()}, null, null);
		}
	}
	
	/**
	 * 获取系统相册路径
	 */
	public static String getServicePhonePath() {
		String cachePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
		return cachePath;
	}
	
	/**
	 * URLDecoder 解码
	 *
	 * @return String
	 * @author lifq
	 * @date 2015-3-17 下午04:09:51
	 */
	public static String getURLDecoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 防止空指针异常
	 *
	 * @param text
	 * @return
	 */
	public static String cleanEmpty(String text) {
		return TextUtils.isEmpty(text) ? "" : text;
	}
	
	
	/**
	 * 获取剪切板的内容。
	 */
	public static String getPrimaryClip(Context context) {
		ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		if (manager == null) {
			return "";
		}
		ClipData data = manager.getPrimaryClip();
		if (data == null || data.getItemCount() <= 0) {
			return "";
		}
		ClipData.Item item = data.getItemAt(0);
		return item != null ? item.getText().toString() : "";
	}
	
	/**
	 * 复制文字
	 *
	 * @param context
	 * @param text
	 */
	public static void copy(Context context, String text, boolean toast) {
		if (TextUtils.isEmpty(text)) return;
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setPrimaryClip(ClipData.newPlainText("text", text));
		if (toast) {
			BasicApp.toast("复制成功");
		}
		saveCopyText(text);
		// AccountUtil.getInstance().saveCopyText(text);
	}
	
	public static void saveCopyText(String res) {
		File cacheDir = new File(Environment.getExternalStorageDirectory(), BasicConst.CACHE_DIRECTORY_NAME);
		cacheDir.mkdirs();
		if (!cacheDir.canWrite()) {
			cacheDir = BasicApp.INSTANCE.getExternalCacheDir();
			if (cacheDir == null || !cacheDir.canWrite()) {
				cacheDir = BasicApp.INSTANCE.getFilesDir();
			}
		}
		File loginFilePath = new File(cacheDir, "appcopytext");
		FileUtils.saveObject(loginFilePath.getPath(), res);
	}
	
	public static void copy(Context context, String text) {
		copy(context, text, null);
	}
	
	public static void copy(Context context, String text, String toast) {
		if (TextUtils.isEmpty(text)) return;
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
		cmb.setPrimaryClip(ClipData.newPlainText("text", text));
		// if (!TextUtils.isEmpty(toast)) {
		// 	ToastUtil.showToast(context, toast);
		// }
		// AccountUtil.getInstance().saveCopyText(text);
	}
	
	public static String getCDN(String imageUrl, int cdnWidth, int cdnHeight) {
		return getCDN(imageUrl, cdnWidth, cdnHeight, true);
	}
	
	public static String getCDN(String imageUrl, int cdnWidth, int cdnHeight, boolean list) {
		if (list && (cdnHeight > 300 || cdnWidth > 300)) {
			cdnHeight = 300;
			cdnWidth = 300;
		}
		if (!TextUtils.isEmpty(imageUrl) && (imageUrl.contains("tbcdn") || imageUrl.contains("alicdn") || imageUrl.contains("taobaocdn"))) {
			if (isAddCDN(imageUrl)) {
				return imageUrl;
			}
			imageUrl = imageUrl + "_" + cdnWidth + "x" + cdnHeight + "q70";
		}
		return imageUrl;
	}
	
	/**
	 * 判断url里是否已经拼接了cdn截图信息了（_200x200）
	 *
	 * @param url
	 * @return
	 */
	public static boolean isAddCDN(String url) {
		String lastStr = url.substring(url.length() - 10, url.length());
		return lastStr.contains("_") || lastStr.contains("x");
	}
	
	/**
	 * 获取设备ID
	 */
	public static String getDeviceId() {
		String uniqueId = null;
		// 先判断有没有权限
		if (ActivityCompat.checkSelfPermission(BasicApp.INSTANCE, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			uniqueId = Settings.System.getString(BasicApp.INSTANCE.getContentResolver(), Settings.System.ANDROID_ID);
		} else {
			// uniqueId = ((TelephonyManager) BasicApp.INSTANCE.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		}
		
		// LogUtils.d("1-----uniqueId = " + uniqueId);
		if (!TextUtils.isEmpty(uniqueId)) { // 如果能正确得到id
			return uniqueId;
		} else {
			// 先从本地取
			uniqueId = AppPreferences.getString(BasicApp.INSTANCE, "device_id");
			// LogUtils.d("2-----uniqueId = " + uniqueId);
			if (!TextUtils.isEmpty(uniqueId)) {
				return uniqueId;
			} else {
				// 如果本地也没有id，生成一个UUID，并存在本地
				String id = UUID.randomUUID().toString();
				if (TextUtils.isEmpty(id)) {
					id = String.valueOf(System.currentTimeMillis());
				}
				// LogUtils.d("3-----id = " + id);
				AppPreferences.putString(BasicApp.INSTANCE, "device_id", id);
				return id;
			}
		}
	}
	
	public static boolean isNotificationEnabled(Context context) {
		boolean isOpened = false;
		try {
			isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOpened;
	}
	
	/**
	 * 打开通知设置
	 */
	public static void launchNotificationSettings(Context context) {
		try {
			Intent intent = new Intent();
			if (Build.VERSION.SDK_INT >= 26) {
				// android 8.0引导
				intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
				intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
			} else if (Build.VERSION.SDK_INT >= 21) {
				// android 5.0-7.0
				intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
				intent.putExtra("app_package", context.getPackageName());
				intent.putExtra("app_uid", context.getApplicationInfo().uid);
			} else {
				// 其他
				intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.setData(Uri.fromParts("package", context.getPackageName(), null));
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Intent intent = new Intent();
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts("package", context.getPackageName(), null);
			intent.setData(uri);
			context.startActivity(intent);
		}
	}
}
