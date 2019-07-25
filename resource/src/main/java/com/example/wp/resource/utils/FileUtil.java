package com.example.wp.resource.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wp on 2019/4/26.
 */
public class FileUtil {
	
	public static String geFileFromAssets(Context context, String fileName) {
		if (context == null || TextUtils.isEmpty(fileName)) {
			return null;
		}
		
		StringBuilder s = new StringBuilder("");
		try {
			InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			return s.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String geFileFromRaw(Context context, int resId) {
		if (context == null) {
			return null;
		}
		
		StringBuilder s = new StringBuilder();
		try {
			InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			return s.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
