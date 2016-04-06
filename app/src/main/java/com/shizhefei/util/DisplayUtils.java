package com.shizhefei.util;

import java.util.Locale;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayUtils {

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isTvbox(Context context) {
		return getDeviceType(context) == 3;
	}

	private static int deviceType = -1;

	public static int getDeviceType(Context context) {
		if (deviceType == -1) {
			if (context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
				// Check if android.hardware.touchscreen feature is available.
				deviceType = 1;
			} else if (context.getPackageManager().hasSystemFeature("android.hardware.touchscreen")) {
				deviceType = 2;
			} else {
				deviceType = 3;
			}
		}

		return deviceType;
	}

	/**
	 * 根据dip值转化成px值
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dipToPix(Context context, int dip) {
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
		return size;
	}

	public static float spToPx(Context context, float sp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
	}

	public static int getDimenValue(Context context, int dimenId) {
		return (int) context.getResources().getDimension(dimenId);
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		// DisplayMetrics dm = new DisplayMetrics();
		// activity.getResources().getDisplayMetrics();
		// activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return context.getResources().getDisplayMetrics();
	}

	/**
	 * 判断设备是否是模拟器
	 * 
	 * @return
	 */
	public static boolean isEmulator() {
		return "sdk".equals(Build.PRODUCT) || "google_sdk".equals(Build.PRODUCT) || "generic".equals(Build.BRAND.toLowerCase(Locale.getDefault()));
	}

	/**
	 * 得到设备id
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		return android_id;
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void copyToClipboard(Context context, String text) {
		// if()
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(text);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
		}
	}
}
