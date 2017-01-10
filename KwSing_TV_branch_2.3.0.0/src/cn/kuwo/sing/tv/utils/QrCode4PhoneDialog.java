/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;

import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

/**
 * @Package cn.kuwo.sing.tv.utils
 * 
 * @Date Dec 2, 2013 5:29:31 PM
 *
 * @Author wangming
 *
 */
public class QrCode4PhoneDialog extends Dialog implements OnClickListener {
	private Context mContext;
	private ImageView ivQrCode4Phone;
	private ImageView ivDownloadLink;
	

	public QrCode4PhoneDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			dismiss();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_qrcode_phone);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
		
		ivQrCode4Phone = (ImageView)findViewById(R.id.ivQrCode4Phone);
		ivDownloadLink = (ImageView)findViewById(R.id.ivDownloadQr);
		//tvLocalIp = (TextView)findViewById(R.id.tvLocalIp);
		
		String linkString = "http://media.cdn.kuwo.cn/resource/m1/webkge/2014/14/201404091530.apk"; 
		try {
			int widthAndHeight = DensityUtils.dip2px(mContext, 420);
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(linkString, widthAndHeight);
			ivDownloadLink.setImageBitmap(qrCodeBitmap);
		} catch (WriterException e) {
			DialogUtils.toast("二维码生成失败！");
		}
		
		String contentString = getLocalIpAddress()+":"+Constants.SERVER_PORT;
		try {
			int widthAndHeight = DensityUtils.dip2px(mContext, 420);
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, widthAndHeight);
			ivQrCode4Phone.setImageBitmap(qrCodeBitmap);
		} catch (WriterException e) {
			DialogUtils.toast("二维码生成失败！");
		}

	}
	
	private String getLocalIpAddress() {  
		try {  
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                     InetAddress inetAddress = enumIpAddr.nextElement();  
                     if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {  
                    	 return inetAddress.getHostAddress().toString();  
                     }  
                }  
             }  
	     } catch (SocketException ex) {  
	         KuwoLog.e("WifiPreference IpAddress", ex.toString());  
	     }  
          return null;  
    }   
	
	private String getLocalMacAddress() {     
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);     
        WifiInfo info = wifi.getConnectionInfo();     
        return info.getMacAddress();     
    }     

	@Override
	public void onClick(View v) {
		
	}
}
