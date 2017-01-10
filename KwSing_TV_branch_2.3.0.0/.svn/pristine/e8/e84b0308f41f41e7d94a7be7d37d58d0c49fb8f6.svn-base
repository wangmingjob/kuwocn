/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.UpdateLog;
import cn.kuwo.sing.tv.logic.DataHandler;
import cn.kuwo.sing.tv.logic.ListLogic;
/**
 * @Package cn.kuwo.sing.tv.utils
 * 
 * @Date Dec 2, 2013 5:29:31 PM
 *
 * @Author wangming
 *
 */
public class UpdateLogsDialog extends Dialog implements OnClickListener {
	private static final String LOG_TAG = "UpdateLogsDialog";
	private Context mContext;
	private ChangeLog mChangeLog;
	private ListLogic mListLogic;
	private WebView mWebView;
	private UpdateLogHandler ulh = new UpdateLogHandler();
	
	public UpdateLogsDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		String logCss = null;
		if(metrics.density == 2.0 && metrics.widthPixels == 1920) 
			logCss = ChangeLog.DEFAULT_CSS_EX_DENSITY_2;
		else 
			logCss = ChangeLog.DEFAULT_CSS_EX;
		mChangeLog = new ChangeLog(context, logCss);
		mListLogic = new ListLogic();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			mChangeLog.updateVersionInPreferences();
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
		setContentView(R.layout.dialog_update_logs);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
		
		//String fullLog = mChangeLog.getFullLog();
		mWebView = (WebView) findViewById(R.id.wvUpdateLog);
		if(AppContext.getNetworkSensor().hasAvailableNetwork()) {
			mListLogic.getUpdateLog(new UpdateLogHandler());
			try {
				Field f = View.class.getDeclaredField("mScrollCache");
				f.setAccessible(true);
				Object scrollabilityCache  = f.get(mWebView);
				f = f.getType().getDeclaredField("scrollBar");
				f.setAccessible(true); 
				Object scrollBarDrawable = f.get(scrollabilityCache);
				f = f.getType().getDeclaredField("mVerticalThumb");
				f.setAccessible(true); 
				Drawable drawable = (Drawable) f.get(scrollBarDrawable); 
				drawable = mContext.getResources().getDrawable(R.drawable.list_scrollbar);
				f.set(scrollBarDrawable, drawable);
			} catch (Exception e) {
				KuwoLog.e(LOG_TAG, e);
			}
		}else {
			String fullLog = mChangeLog.getNetworkErrorFormatLog("当前没有连接网络，无法检测!");
			mWebView.loadDataWithBaseURL(null, fullLog, "text/html", "UTF-8", null);
		}
		mWebView.setBackgroundColor(0); // transparent
        //webView.loadDataWithBaseURL(null, fullLog, "text/html", "UTF-8", null);
	}
	
	public boolean isFirstRunApplication() {
		return mChangeLog.isFirstRun();
	}

	@Override
	public void onClick(View v) {
		
	}
	
	private class UpdateLogHandler extends DataHandler<String> {

		@Override
		public void onSuccess(String jsonResult) {
			UpdateLog ul = new UpdateLog();
			try {
				JSONObject jsonObj = new JSONObject(jsonResult);
				String resultStr = jsonObj.getString("result");
				if(!TextUtils.isEmpty(resultStr) && resultStr.equals("ok")) {
					ul.version = jsonObj.getString("version");
					ul.info = jsonObj.getString("msg");
					ul.time = jsonObj.getString("time");
					String fullLog = mChangeLog.getFormatLog(ul.version, ul.time, ul.info);
					mWebView.loadDataWithBaseURL(null, fullLog, "text/html", "UTF-8", null);
				}else {
					ul = null;
				}
			} catch (JSONException e) {
				ul = null;
			}			
		}

	}
}
