/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.umeng.analytics.MobclickAgent;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.view.adapter.MicSettingAdapter;
import de.greenrobot.event.EventBus;
/**
 * @Package cn.kuwo.sing.tv.utils
 * 
 * @Date Dec 2, 2013 5:29:31 PM
 *
 * @Author wangming
 *
 */
public class MicrophoneSettingsDialog extends Dialog {
	private static final String LOG_TAG = MicrophoneSettingsDialog.class.getSimpleName();
	private Context mContext;
	private MicSettingAdapter mSettingAdapter;
	private ListView mMicSettingList;
	private int micSetting = 0;


	public MicrophoneSettingsDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_microphone_settings);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
		
		mMicSettingList = (ListView)findViewById(R.id.lvMicSetting);
		micSetting = PreferencesManager.getInt(Constants.PLAYBACK_MODE, Constants.PLAYBACK_MODE_HARDWARE);
		mSettingAdapter = new MicSettingAdapter(mContext, micSetting);
		
		mMicSettingList.setAdapter(mSettingAdapter);
		mMicSettingList.setOnItemClickListener(mOnItemClickListener );
		mMicSettingList.setOnItemSelectedListener(mOnItemSelectedListerner);
		
	};	
	
	private OnItemSelectedListener mOnItemSelectedListerner = new OnItemSelectedListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			int position = arg2;
			
			ImageView iv1 = (ImageView)arg0.getChildAt(0).findViewById(R.id.ivMicSettingItemPic);
			iv1.setImageResource(R.drawable.micsetting_mic_normal);
			
			ImageView iv2 = (ImageView)arg0.getChildAt(1).findViewById(R.id.ivMicSettingItemPic);
			iv2.setImageResource(R.drawable.micsetting_usb_normal);
			
			ImageView iv3 = (ImageView)arg0.getChildAt(2).findViewById(R.id.ivMicSettingItemPic);
			iv3.setImageResource(R.drawable.micsetting_none_normal);
			
			TextView tv1 = (TextView)arg0.getChildAt(0).findViewById(R.id.tvMicSettingItemInfo);
			tv1.setTextColor(mContext.getResources().getColor(R.color.more_title_color));			
			TextView tv11 = (TextView)arg0.getChildAt(0).findViewById(R.id.tvMicSettingItemDetailInfo);
			tv11.setTextColor(mContext.getResources().getColor(R.color.more_title_color));			

			TextView tv2 = (TextView)arg0.getChildAt(1).findViewById(R.id.tvMicSettingItemInfo);
			tv2.setTextColor(mContext.getResources().getColor(R.color.more_title_color));
			TextView tv22 = (TextView)arg0.getChildAt(1).findViewById(R.id.tvMicSettingItemDetailInfo);
			tv22.setTextColor(mContext.getResources().getColor(R.color.more_title_color));
			
			TextView tv3 = (TextView)arg0.getChildAt(2).findViewById(R.id.tvMicSettingItemInfo);
			tv3.setTextColor(mContext.getResources().getColor(R.color.more_title_color));
			TextView tv33 = (TextView)arg0.getChildAt(2).findViewById(R.id.tvMicSettingItemDetailInfo);
			tv33.setTextColor(mContext.getResources().getColor(R.color.more_title_color));
			
			ImageView ivItemPic = (ImageView)arg0.getChildAt(position).findViewById(R.id.ivMicSettingItemPic);
			if (position == 0)
			{				
				ivItemPic.setImageResource(R.drawable.micsetting_mic_selected);
			}
			else if (position == 1)
			{	
				ivItemPic.setImageResource(R.drawable.micsetting_usb_selected);
			}
			else
			{
				ivItemPic.setImageResource(R.drawable.micsetting_none_selected);
			}
			
			TextView tvItemInfo = (TextView)arg0.getChildAt(position).findViewById(R.id.tvMicSettingItemInfo);
			TextView tvItemDetailInfo = (TextView)arg0.getChildAt(position).findViewById(R.id.tvMicSettingItemDetailInfo);
			tvItemInfo.setTextColor(Color.WHITE);
			tvItemDetailInfo.setTextColor(Color.WHITE);
			
			
			ImageView ivItemChoose = (ImageView)arg0.getChildAt(micSetting).findViewById(R.id.ivMicSettingItemChoose);
			if (position == micSetting)
			{
				ivItemChoose.setImageResource(R.drawable.micsetting_select_selected);
			}
			else
			{
				ivItemChoose.setImageResource(R.drawable.micsetting_select_normal);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			for (int i=0; i<parent.getChildCount(); i++)
			{
				if (i==position)
				{
					ImageView iv = (ImageView)parent.getChildAt(i).findViewById(R.id.ivMicSettingItemChoose);
					if (iv != null)
					{
						iv.setImageResource(R.drawable.micsetting_select_selected);
						iv.setVisibility(View.VISIBLE);
					}
				}
				else
				{
					ImageView iv = (ImageView)parent.getChildAt(i).findViewById(R.id.ivMicSettingItemChoose);
					if (iv != null)
					{
						iv.setVisibility(View.INVISIBLE);
					}		
				}
			}
			
			
			if (position==0)
			{
				micSetting = 0;
				MobclickAgent.onEvent(mContext, Constants.KS_UMENG_MIC_SETTING, Constants.KS_UMENG_MIC_SETTING_MIC);
				PreferencesManager.put(Constants.PLAYBACK_MODE, Constants.PLAYBACK_MODE_HARDWARE).commit();
				if(Build.BRAND.equals("Tcl") || Build.BRAND.equals("SkyWorth") || Build.BRAND.equalsIgnoreCase("changhong") || Build.BRAND.equals("Hisense")) {
					try {
						TvManager.getInstance().setInputSource(EnumInputSource.E_INPUT_SOURCE_KTV, false, false, false);
					} catch (Exception e) {
						
					}
				}
			}
			else if (position==1)
			{
				micSetting = 1;
				MobclickAgent.onEvent(mContext, Constants.KS_UMENG_MIC_SETTING, Constants.KS_UMENG_MIC_SETTING_USB);
				PreferencesManager.put(Constants.PLAYBACK_MODE, Constants.PLAYBACK_MODE_SOFTWARE).commit();
				if(Build.BRAND.equals("Tcl") || Build.BRAND.equals("SkyWorth") || Build.BRAND.equalsIgnoreCase("changhong") || Build.BRAND.equals("Hisense")) {
					try {
						TvManager.getInstance().setInputSource(EnumInputSource.E_INPUT_SOURCE_STORAGE, false, false, false);
					} catch (Exception e) {
						
					}
				}
			}
			else
			{
				micSetting = 2;
				MobclickAgent.onEvent(mContext, Constants.KS_UMENG_MIC_SETTING, Constants.KS_UMENG_MIC_SETTING_NONE);
				PreferencesManager.put(Constants.PLAYBACK_MODE, Constants.PLAYBACK_MODE_NO_MIC).commit();
			}
			
		}
	};
	
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
}
