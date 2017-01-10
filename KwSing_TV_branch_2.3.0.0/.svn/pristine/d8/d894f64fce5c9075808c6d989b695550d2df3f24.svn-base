package cn.kuwo.sing.tv.utils;

import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class HardwareDecodeDialog extends Dialog {
	private Context mContext;

	public HardwareDecodeDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hardware_decode);
		
		Switch switchHardwareDecode = (Switch) findViewById(R.id.llHardwareSwitch);
		switchHardwareDecode.setTextOff("已关闭");
		switchHardwareDecode.setTextOn("已打开");
		switchHardwareDecode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					PreferencesManager.put(Constants.HARDWARE_DECODE, 1).commit();
				}else{
					PreferencesManager.put(Constants.HARDWARE_DECODE, 0).commit();
				}
			}
		});
		int useHardwareDecode = PreferencesManager.getInt(Constants.HARDWARE_DECODE, 0);
		if (useHardwareDecode==1){
			switchHardwareDecode.setChecked(true);
		}else{
			switchHardwareDecode.setChecked(false);
		}
		switchHardwareDecode.requestFocus();
	}

}
