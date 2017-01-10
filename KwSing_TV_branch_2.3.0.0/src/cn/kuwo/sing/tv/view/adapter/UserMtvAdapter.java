/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.UserMtv;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.view.activity.PlayUserMtvActivity;

import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 * 
 * @Date 2013年9月11日 上午10:59:32
 *
 * @author wangming
 *
 */
public class UserMtvAdapter extends BaseAdapter {
	private static final String TAG = "UserMtvAdapter";
	private List<UserMtv> mMtvtList;
	private Activity mActivity;
	private int mCurrentPageNum;
	private int mPageSize;
	private int mSelectItem = -1;
	
	public UserMtvAdapter(Activity activity, int pageSize) {
		mActivity = activity;
		mMtvtList = new ArrayList<UserMtv>();
		mPageSize = pageSize;
	}
	
	public void setMtvList(List<UserMtv> data) {
		mMtvtList = data;
		notifyDataSetChanged();
	}
	
	public void clearMtvList() {
		mMtvtList.clear();
		notifyDataSetChanged();
	}
	
	public void setCurrentPageNum(int pageNum) {
		mCurrentPageNum = pageNum;
	}

	@Override
	public int getCount() {
		return mMtvtList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMtvtList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		if(convertView == null) {
			view = View.inflate(mActivity, R.layout.user_mtv_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvUserMtvSerialIndex = (TextView) view.findViewById(R.id.tvUserMtvSerialIndex);
			viewHolder.tvUserMtvArtist = (TextView) view.findViewById(R.id.tvUserMtvArtist);
			viewHolder.tvUserMtvName = (TextView) view.findViewById(R.id.tvUserMtvName);
			viewHolder.ivUserMtvSing = view.findViewById(R.id.ivUserMtvSing);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		final UserMtv userMtv = (UserMtv) mMtvtList.get(position);
		viewHolder.tvUserMtvSerialIndex.setText(String.valueOf((mCurrentPageNum-1)*mPageSize+position+1));
		if(TextUtils.isEmpty(userMtv.title)) {
			viewHolder.tvUserMtvName.setText("未命名");
		}else {
			viewHolder.tvUserMtvName.setText(userMtv.title);
		}
		viewHolder.tvUserMtvArtist.setText(userMtv.uname);
		
		viewHolder.ivUserMtvSing.setActivated(mSelectItem == 0);
		
//		float textSize = mActivity.getResources().getDimension(R.dimen.item_text);
		float textSize = 24;

		viewHolder.tvUserMtvSerialIndex.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		viewHolder.tvUserMtvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		viewHolder.tvUserMtvArtist.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

		viewHolder.ivUserMtvSing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				KuwoLog.d(TAG, "click the play mtv button");
				if(!AppContext.getNetworkSensor().hasAvailableNetwork())
					DialogUtils.toast("当前没有网络！", false);
				MobclickAgent.onEvent(mActivity, Constants.KS_UMENG_PLAY, Constants.KS_UMENG_SUCCESS);
				if(MainApplication.isSingActivityAliving) {
					Message msg = new Message();
					msg.what = Constants.MSG_OPEN_USER_MTV_ACTIVITY_WHEN_PLAY_MTV;
					EventBus.getDefault().post(msg);
				}
				Intent singIntent = new Intent(mActivity, PlayUserMtvActivity.class);
				singIntent.putExtra("userMtv", userMtv);
				mActivity.startActivity(singIntent);
//				mActivity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
			}
		});
		
		viewHolder.ivUserMtvSing.setOnFocusChangeListener(mOnFocusChangeListener);
		return view;
	}
	
	private static class ViewHolder {
		TextView tvUserMtvSerialIndex;
		TextView tvUserMtvName;
		TextView tvUserMtvArtist;
		View ivUserMtvSing;
	}
	
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener(){

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			ViewGroup parent = (ViewGroup)v.getParent();
			parent.setSelected(parent.findFocus() != null);
		}
	};
}
