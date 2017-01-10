/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.utils.HardwareDecodeDialog;
import cn.kuwo.sing.tv.utils.MicrophoneHelpDialog;
import cn.kuwo.sing.tv.utils.MicrophoneSettingsDialog;
import cn.kuwo.sing.tv.utils.QrCode4PhoneDialog;
import cn.kuwo.sing.tv.utils.UpdateLogsDialog;
import cn.kuwo.sing.tv.utils.UserFeedbackDialog;

/**
 * @Package cn.kuwo.sing.tv.controller
 * 
 * @Date Dec 2, 2013 11:06:26 AM
 *
 * @Author wangming
 *
 */
public class MoreController extends BaseController {
	public MoreController(Activity activity) {
		super(activity);
		initView();
	}

	private void initView() {
		Button btMicrophoneHelp = (Button) activity.findViewById(R.id.btMicrophoneHelp);
		Button btUserFeedback = (Button)activity.findViewById(R.id.btUserFeedback);
		Button btUpdateLogs = (Button)activity.findViewById(R.id.btUpdateLogs);
		Button btQrCode4Phone = (Button) activity.findViewById(R.id.btQrCode4Phone);
		Button btHardwareDecode = (Button) activity.findViewById(R.id.btHardwareDecode);
		
		btMicrophoneHelp.setOnClickListener(mOnClickListener);
		btUserFeedback.setOnClickListener(mOnClickListener);
		btUpdateLogs.setOnClickListener(mOnClickListener);
		btQrCode4Phone.setOnClickListener(mOnClickListener);
		btHardwareDecode.setOnClickListener(mOnClickListener);
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btMicrophoneHelp:
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_MIC_HELP, Constants.KS_UMENG_SUCCESS);
				MicrophoneHelpDialog mMicrophoneHelpDialog = new MicrophoneHelpDialog(activity);
				mMicrophoneHelpDialog.show();
				break;
			case R.id.btUserFeedback:
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_USER_FEEDBACK, Constants.KS_UMENG_SUCCESS);
				UserFeedbackDialog mUserFeedbackDialog = new UserFeedbackDialog(activity);
				mUserFeedbackDialog.show();
				break;
			case R.id.btUpdateLogs:
				UpdateLogsDialog mUpdateLogsDialog = new UpdateLogsDialog(activity);
				mUpdateLogsDialog.show();
				break;
			case R.id.btQrCode4Phone:
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_QRCODE4PHONE, Constants.KS_UMENG_SUCCESS);
				QrCode4PhoneDialog qrCode4PhoneDialog = new QrCode4PhoneDialog(activity);
				qrCode4PhoneDialog.show();
				break;
			case R.id.btHardwareDecode:
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_HARDWAREDECODE, Constants.KS_UMENG_SUCCESS);
				HardwareDecodeDialog hardwareDecodeDialog = new HardwareDecodeDialog(activity);
				hardwareDecodeDialog.show();
				break;
			default:
				break;
			}
			
		}
	};

}
