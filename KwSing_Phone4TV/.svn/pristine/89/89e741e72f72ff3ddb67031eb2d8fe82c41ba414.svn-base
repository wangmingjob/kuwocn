/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.commons.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.commons.context.Constants;
import cn.kuwo.sing.phone4tv.commons.file.PreferencesManager;
import cn.kuwo.sing.phone4tv.view.activity.MainActivity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.devspark.appmsg.AppMsg;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * @Package cn.kuwo.sing.phone4tv.util
 *
 * @Date 2014-2-25, 下午12:59:04
 *
 * @Author wangming
 *
 */
public class CommonUtils {
	
//	public static void toAutoConnectServerByHistory(Activity activity) {
//		String lastServerIpInfo = PreferencesManager.getString(Constants.LAST_SERVER_IP_KEY, null);
//		if(!TextUtils.isEmpty(lastServerIpInfo)) {
//			if(lastServerIpInfo.contains(":")) {
//				//连接服务端ip
//				String[] ipAndPort = lastServerIpInfo.split(":");
//				performConnectServer(ipAndPort[0], ipAndPort[1]);
//			}else {
//				AppMsg.makeText(activity, "无效的电视二维码，请勿扫描其它二维码", AppMsg.STYLE_ALERT)
//				.setLayoutGravity(Gravity.TOP)
//				.show();
//			}
//		}
//	}
	
//	private static void performConnectServer(Activity activity, String serverIp, String serverPort) {
//		String localIp = CommonUtils.getWifiIp(activity);
//		String localIpPrefix = "";
//		String tvIpPrefix = "";
//		if(serverIp.contains(".") && localIp.contains(".")) {
//			localIpPrefix = localIp.substring(0, localIp.lastIndexOf("."));
//			tvIpPrefix = serverIp.substring(0, serverIp.lastIndexOf("."));
//		}else {
//			AppMsg.makeText(activity, "IP地址不正确！", AppMsg.STYLE_ALERT)
//			.setLayoutGravity(Gravity.TOP).show();
//			return;
//		}
//		if(localIpPrefix.equals(tvIpPrefix)) {
//			connectDialog = new ProgressDialog(activity);
//			connectDialog.setTitle("正在连接...");
//			connectDialog.show();
//			//点击 “连接” 进行服务端的连接操作
//			startSocketConnectThread(serverIp, Integer.parseInt(serverPort));
//		}else {
//			AppMsg.makeText(activity, "不在同一网段内!", AppMsg.STYLE_ALERT)
//			.setLayoutGravity(Gravity.TOP)
//			.show();
//			if(connectDialog != null && connectDialog.isShowing())
//				connectDialog.dismiss();
//		}
//	}

	public static String getWifiIp(Activity activity) {
		WifiManager wifimanage=(WifiManager)activity.getSystemService(Context.WIFI_SERVICE);//获取WifiManager  
		//检查wifi是否开启  
		if(!wifimanage.isWifiEnabled())  {  
			wifimanage.setWifiEnabled(true);  
		}  
		  
		WifiInfo wifiinfo= wifimanage.getConnectionInfo();  
		return intToIp(wifiinfo.getIpAddress());  
	}
	
	//将获取的int转为真正的ip地址
	private static String intToIp(int i)  {
		return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) +"."+((i >> 24 ) & 0xFF);
	}  
	
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				} else {
					imageView.setImageBitmap(loadedImage);
				}
			}
		}
	}
	
	public static void setActionBarColor(SherlockFragmentActivity activity) {
		Resources rs = activity.getResources();
		Drawable colorDrawable = new ColorDrawable(rs.getColor(R.color.background_actionBar));
		Drawable bottomDrawable = rs.getDrawable(R.drawable.actionbar_bottom);
		LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable,bottomDrawable });
		TransitionDrawable td = new TransitionDrawable(new Drawable[] {ld, ld });
		td.startTransition(200);
		activity.getSupportActionBar().setBackgroundDrawable(td);
		if(Build.VERSION.SDK_INT >= 14) {
			int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");  
			TextView title = (TextView) activity.findViewById(titleId); 
			title.setTextColor(rs.getColor(android.R.color.white));
		}
	}
}
