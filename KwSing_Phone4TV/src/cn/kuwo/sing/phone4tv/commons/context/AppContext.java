/**
 * Copyright (c) 2013, BigBeard Team, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.commons.context;

import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * 
 * @File com.bigbeard.commons.context
 *
 * @Date Jul 10, 2013, 4:27:24 PM
 * 
 * @Author Ming Wang
 * 
 * @Version V1.0.0
 * 
 * @Description TODO: to describe this class
 */
public class AppContext {
	private static final String LOG_TAG = "AppContext";
	private static boolean sInited = false;
	private static NetworkSensor sNetworkSensor;
	public static Context context;
	
	/**
	 *  The unique device ID, for example, 
	 *  the IMEI for GSM and the MEID or ESN for CDMA phones
	 */
	public static String DEVICE_ID;

	/**
	 * The application's version code
	 */
	public static int VERSION_CODE;

	/**
	 * The application's version name
	 */
	public static String VERSION_NAME;

	/**
	 * The screen width of the android device
	 */
	public static int SCREEN_WIDTH;

	/**
	 * The screen height of the android device
	 */
	public static int SCREEN_HEIGHT;

	/**
	 * The source of install
	 */
	public static String INSTALL_SOURCE;

	public synchronized static boolean init(Context context) {
		if (sInited)
			return true;

		AppContext.context = context;

		Log.i(LOG_TAG, "MODEL: " + Build.MODEL);
		Log.i(LOG_TAG, "BOARD: " + Build.BOARD);
		Log.i(LOG_TAG, "BRAND: " + Build.BRAND);
		Log.i(LOG_TAG, "DEVICE: " + Build.DEVICE);
		Log.i(LOG_TAG, "PRODUCT: " + Build.PRODUCT);
		Log.i(LOG_TAG, "DISPLAY: " + Build.DISPLAY);
		Log.i(LOG_TAG, "HOST: " + Build.HOST);
		Log.i(LOG_TAG, "ID: " + Build.ID);
		Log.i(LOG_TAG, "USER: " + Build.USER);

		try {
			String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			if (deviceId == null) {
				WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = wifi.getConnectionInfo();
				deviceId = info.getMacAddress(); // if device id is null, use the mac address.
			}
			if (deviceId == null)
				deviceId = "000000";  //if device id and mac address is null, use the string value "000000"
			DEVICE_ID = deviceId;
			Log.i(LOG_TAG, "DEVICE_ID:" + DEVICE_ID);

			// Get the version code and version code from the PackageInfo object
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			VERSION_CODE = pi.versionCode;
			VERSION_NAME = pi.versionName;
			Log.i(LOG_TAG, "install VERSION_CODE:" + VERSION_CODE);

			// Get the screen width and height from Display object
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			SCREEN_WIDTH = display.getWidth(); //deprecated Use getSize(Point) instead.
			SCREEN_HEIGHT = display.getHeight(); //deprecated Use getSize(Point) instead.
			Log.i(LOG_TAG, String.format("Screen width: %s  height: %s", SCREEN_WIDTH, SCREEN_HEIGHT));

			//Initial the network environment
			sNetworkSensor = new NetworkSensor(context);

		} catch (Exception e) {
			return false;
		}

		sInited = true;
		return true;
	}

	public static NetworkSensor getNetworkSensor() {
		return sNetworkSensor;
	}

	public static boolean isChinese(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		return locale.toString().equals("zh_CN");
	}

	public static boolean isScreenPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == 1;
	}
}
