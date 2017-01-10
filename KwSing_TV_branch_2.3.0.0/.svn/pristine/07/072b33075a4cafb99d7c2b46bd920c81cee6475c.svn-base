/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.kuwo.sing.tv.R;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 * 
 * @Date 2013-12-12, 下午4:43:32, 2013
 * 
 * @Author dongkun
 * 
 */
public class MicSettingAdapter extends BaseAdapter {
	private Context mContext;
	private int mmicSetting;
	private LinkedList<MicSetting> mMicSettingList;
	
	public MicSettingAdapter(Context context, int micSetting) {
		mContext = context;
		mmicSetting = micSetting;
		
		mMicSettingList = new LinkedList<MicSetting>();
		
		MicSetting ms1 = new MicSetting();
		ms1.MicRes = R.drawable.micsetting_mic_normal;
		ms1.MicInfo = "接头直径为6.5mm麦克风";
		ms1.MicDetailInfo = "KTV使用的麦克风";
		mMicSettingList.add(0,ms1);
		
		MicSetting ms2 = new MicSetting();
		ms2.MicRes = R.drawable.micsetting_usb_normal;
		ms2.MicInfo = "USB麦克风";
		ms2.MicDetailInfo = "进入演唱后生效，声音可能会有延时";
		mMicSettingList.add(1,ms2);
		
		MicSetting ms3 = new MicSetting();
		ms3.MicRes = R.drawable.micsetting_none_normal;
		ms3.MicInfo = "没有麦克风";
		ms3.MicDetailInfo = "随着MV干吼";
		mMicSettingList.add(2,ms3);
		
	}
	
	@Override
	public int getCount() {
		return mMicSettingList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMicSettingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		MicSetting ms = mMicSettingList.get(position);
		if (convertView == null) {
			view = View.inflate(mContext, R.layout.mic_setting_item, null);	
			viewHolder = new ViewHolder();
			viewHolder.ivMicSettingPic = (ImageView) view.findViewById(R.id.ivMicSettingItemPic);
			viewHolder.tvMicSettingInfo = (TextView) view.findViewById(R.id.tvMicSettingItemInfo);
			viewHolder.tvMicSettingDetailInfo = (TextView) view.findViewById(R.id.tvMicSettingItemDetailInfo);
			viewHolder.ivMicSettingChoose = (ImageView) view.findViewById(R.id.ivMicSettingItemChoose);
			
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		viewHolder.ivMicSettingPic.setImageResource(ms.MicRes);
		viewHolder.tvMicSettingInfo.setText(ms.MicInfo);
		viewHolder.tvMicSettingDetailInfo.setText(ms.MicDetailInfo);
		viewHolder.ivMicSettingChoose.setImageResource(R.drawable.micsetting_select_selected);
		
		if (mmicSetting == position)
			viewHolder.ivMicSettingChoose.setVisibility(View.VISIBLE);
		else
			viewHolder.ivMicSettingChoose.setVisibility(View.INVISIBLE);
		
		return view;
	}
	
	class ViewHolder{
		RelativeLayout rl_mic_setting_item;
		ImageView ivMicSettingPic;
		TextView tvMicSettingInfo;
		TextView tvMicSettingDetailInfo;
		ImageView ivMicSettingChoose;
	}
	
	class MicSetting{
		int	MicRes;
		String MicInfo;
		String MicDetailInfo;
	}
}
