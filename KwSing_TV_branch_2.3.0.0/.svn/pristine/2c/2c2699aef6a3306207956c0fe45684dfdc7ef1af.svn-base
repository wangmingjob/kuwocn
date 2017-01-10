/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.view.activity.PlayActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2013-3-27, 下午5:13:26, 2013
 *
 * @Author wangming
 *
 */
public class DetailAdapter extends BaseAdapter {
	private static final String TAG = "DetailAdapter";
	private List<Mtv> mMtvtList;
	private Activity mActivity;
	private OrderSerializeLogic mOrderLogic;
	private int mCurrentPageNum;
	private int mPageSize;
	private int mSelectItem = -1;
	private int mFromPage = -1;
	
	public DetailAdapter(Activity activity, int pageSize, int fromPage) {
		mActivity = activity;
		mMtvtList = new ArrayList<Mtv>();
		mOrderLogic = OrderSerializeLogic.getInstance();
		mPageSize = pageSize;
		mFromPage = fromPage;
	}
	
	public void setMtvList(List<Mtv> data) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		if(convertView == null) {
			view = View.inflate(mActivity, R.layout.second_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvDetailMtvSerialIndex = (TextView) view.findViewById(R.id.tvDetailMtvSerialIndex);
			viewHolder.tvDetailMtvArtist = (TextView) view.findViewById(R.id.tvDetailMtvArtist);
			viewHolder.tvDetailMtvName = (TextView) view.findViewById(R.id.tvDetailMtvName);
			viewHolder.btDetailAddMtv = view.findViewById(R.id.btMtvAdd);
			viewHolder.btDetailPlayMtv = view.findViewById(R.id.btMtvSing);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		final Mtv mtv = mMtvtList.get(position);
		viewHolder.tvDetailMtvSerialIndex.setText(String.valueOf((mCurrentPageNum-1)*mPageSize+position+1));
		viewHolder.tvDetailMtvName.setText(mtv.name);
		viewHolder.tvDetailMtvArtist.setText(mtv.artist);
		
		viewHolder.btDetailPlayMtv.setActivated(mSelectItem == 0);
		viewHolder.btDetailAddMtv.setActivated(mSelectItem == 1);

		viewHolder.btDetailAddMtv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				KuwoLog.d(TAG, "click the add mtv button");
				mOrderLogic.addMtv(mtv);
				SpannableStringBuilder sb = new SpannableStringBuilder();
				sb.append("添加  ").append(mtv.name).append(" 到已点列表");
				sb.setSpan(new ForegroundColorSpan(Color.YELLOW), 3, 3+mtv.name.length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				DialogUtils.toast(sb, false);
				String s = "" ;
				if (mFromPage==1)
				{
					s = "ADD_FROM_PINYIN";
				}
				else if (mFromPage==2)
				{
					s = "ADD_FROM_HOTBAR";
				}
				else if (mFromPage==3)
				{
					s = "ADD_FROM_SINGER";
				}
				else if(mFromPage == 4)
				{
					s = "ADD_FROM_CATEGORY";
				}
				MobclickAgent.onEvent(mActivity, Constants.KS_UMENG_ADD, s);
			}
		});
		viewHolder.btDetailPlayMtv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				KuwoLog.d(TAG, "click the play mtv button");
				if(!AppContext.getNetworkSensor().hasAvailableNetwork()) {
					DialogUtils.toast("当前没有网络！", false);
					return;
				}
				Intent singIntent = new Intent(mActivity, PlayActivity.class);
				singIntent.putExtra("mtv", mtv);
				mActivity.startActivity(singIntent);
				mOrderLogic.singMtv(mActivity.getApplication(), mtv, false, position);
				String s = "" ;
				if (mFromPage==1)
				{
					s = "SING_FROM_PINYIN";
				}
				else if (mFromPage==2)
				{
					s = "SING_FROM_HOTBAR";
				}
				else if (mFromPage==3)
				{
					s = "SING_FROM_SINGER";
				}
				else if(mFromPage == 4) 
				{
					s = "SING_FROM_CATEGORY";
				}
				
				MobclickAgent.onEvent(mActivity, Constants.KS_UMENG_SING, s);
			}
		});
		
		viewHolder.btDetailPlayMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		viewHolder.btDetailAddMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		
		return view;
	}
	
	private static class ViewHolder {
		TextView tvDetailMtvSerialIndex;
		TextView tvDetailMtvName;
		TextView tvDetailMtvArtist;
		View btDetailAddMtv;
		View btDetailPlayMtv;
	}
	
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener(){

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			ViewGroup parent = (ViewGroup)v.getParent();
			parent.setSelected(parent.findFocus() != null);
		}
	};

}
