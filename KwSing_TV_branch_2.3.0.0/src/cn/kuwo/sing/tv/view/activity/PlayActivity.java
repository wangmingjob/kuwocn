/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Decoders;
import cn.kuwo.sing.tv.controller.BaseController;
import cn.kuwo.sing.tv.controller.KonkaPlayController;
import cn.kuwo.sing.tv.controller.LyricController;
import cn.kuwo.sing.tv.controller.SingController;
import cn.kuwo.sing.tv.controller.SysPlayController;
import cn.kuwo.sing.tv.controller.VitamioPlayController;

import com.umeng.analytics.MobclickAgent;

/**
 * @Package cn.kuwo.sing.tv.view.activity
 *
 * @Date 2013-4-1, 下午5:04:23, 2013
 *
 * @Author wangming  
 * 
 */
public class PlayActivity extends BaseFragmentActivity {
	private static final String LOG_TAG = "PlayActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//api 版本小于或等于15使用vitamio解码器,15以后使用系统的解码器，主要原因是15前的解码器不支持原伴唱切换
		if (Decoders.isUseKonkaDecoder()){
			setContentView(R.layout.play_activity_konka);
		}
		else if (Decoders.isUseSysDecoder()){
			setContentView(R.layout.play_activity_sys);
		}else{
			setContentView(R.layout.play_activity_vitamio);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onLoadController() {
		super.onLoadController();
		if (Decoders.isUseVitamioDecoder()){
			if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
				return;
		}
		
		if(Build.HOST.equalsIgnoreCase("letv")) { //乐视K客盒子的初始化 ;初始化卡拉OK设备，参数“1”为打开卡拉OK外接设备，“0”为关闭设备
			AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	        audioManager.setParameters("karaoke_dev_init=1;");
	        String initStatus = audioManager.getParameters("karaoke_dev_init=");
	        if(initStatus.equals("karaoke_dev_init=1")) {
	            Log.d(LOG_TAG, "kke dev init successful");
	        } else {
	            Log.d(LOG_TAG, "kke dev init error");
	        }
		}
		LyricController ctlLyric = new LyricController(this);
		SingController singController = new SingController(this, ctlLyric);
		loadController(ctlLyric);
		loadController(singController);
		BaseController playController = null;
		
		if (Decoders.isUseKonkaDecoder()){
			playController = new KonkaPlayController(this);
		}else if (Decoders.isUseSysDecoder()){
			playController = new SysPlayController(this);
		}else{
			playController = new VitamioPlayController(this);
		}
		loadController(playController);
	}
	
	@Override
	protected void onDestroy() {
		if(Build.HOST.equalsIgnoreCase("letv")) {
			AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			audioManager.setParameters("karaoke_dev_init=0;");
		}
		super.onDestroy();
	}
}
