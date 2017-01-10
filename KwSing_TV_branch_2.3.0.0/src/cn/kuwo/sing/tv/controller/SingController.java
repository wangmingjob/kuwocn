/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;

import android.app.Activity;
import android.os.Message;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.SingLogic;
import cn.kuwo.sing.tv.logic.SingLogic.OnAudioRecordListener;
import cn.kuwo.sing.tv.utils.DialogUtils;
import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.controller
 *
 * @Date 2013-4-1, 下午5:31:49, 2013
 *  
 * @Author wangming
 * 
 * @Description 演唱模块 [麦克音量，伴奏音量]
 *
 */
public class SingController extends BaseController {
	private static final String TAG = "SingController";
	private SingLogic mSingLogic;
	private LyricController ctlLyric;

	public SingController(Activity activity, LyricController ctlLyric) {
		super(activity);
		
		this.ctlLyric = ctlLyric;
		initData();
		initView();
		processLogic();
		EventBus.getDefault().register(this);
	}
	
	public void onEvent(Message msg) {
		switch (msg.what) {
		case Constants.MSG_STOP_SING_CONTROLLER:
			exit();
			break;
		case Constants.MSG_RESTART_SING_CONTROLLER:
			onStart();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		KuwoLog.d("record", "SingController onDestroy");
		mSingLogic.exit();
	}
	
	private void initData() {
		mSingLogic = new SingLogic();
	}

	private void initView() {
	}
	
	private void processLogic() {
		mSingLogic.setOnAudioRecordListener(new OnAudioRecordListener() {
			
			@Override
			public void onDataRecord(byte[] data) {
				ctlLyric.ProcessWaveDate(data);
			}
		});
	}
	
	@Override
	public void onStart() {
		mSingLogic.start();
		super.onStart();
		KuwoLog.d(TAG, "onStart");
	}
	
	public void exit() {
		mSingLogic.exit();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		KuwoLog.d(TAG, "onStop");
	}
}
