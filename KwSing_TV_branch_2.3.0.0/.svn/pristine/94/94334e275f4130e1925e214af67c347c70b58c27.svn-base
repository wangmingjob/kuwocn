/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic;
import cn.kuwo.sing.tv.utils.DensityUtils;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.view.activity.PlayActivity;

import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 * 
 * @Date 2013-3-29, 下午5:43:32, 2013
 * 
 * @Author wangming
 * 
 */
public class OrderedMtvAdapter extends BaseAdapter {
	private Activity mActivity;
	private OrderSerializeLogic mOrderLogic;
	private List<Mtv> mOrderedMtvList;
	private int mSelectItem = -1;
	private int lastClickedButtonIndex = -1;
	private int mCurrentListSelectedItem = -1;
	
	private int mCurrentPageNum = 1;

	public OrderedMtvAdapter(Activity activity, List<Mtv> orderedMtvList) {
		mActivity = activity;
		mOrderLogic = OrderSerializeLogic.getInstance();
		mOrderedMtvList = orderedMtvList;
	}
	
	public void setOrderedMtvList(List<Mtv> orderedMtvList) {
		mOrderedMtvList = orderedMtvList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mOrderedMtvList.size();
	}

	@Override
	public Object getItem(int position) {
		return mOrderedMtvList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		final Mtv mtv = mOrderedMtvList.get(position);
		if (convertView == null) {
			view = View.inflate(mActivity, R.layout.ordered_mtv_item, null);	
			viewHolder = new ViewHolder();
			viewHolder.ll_ordered_mtv_item = (LinearLayout) view.findViewById(R.id.ll_ordered_mtv_item);
			viewHolder.tvOrderedMtvItemSerialIndex = (TextView) view.findViewById(R.id.tvOrderedMtvSerialIndex);
			viewHolder.tvOrderedMtvName = (TextView) view.findViewById(R.id.tvOrderedMtvName);
			viewHolder.tvOrderedMtvArtist = (TextView) view.findViewById(R.id.tvOrderedMtvArtist);
			viewHolder.btOrderedMtvTop = view.findViewById(R.id.btMtvTop);
			viewHolder.btOrderedMtvSing = view.findViewById(R.id.btMtvSing);
			viewHolder.btOrderedMtvDelete = view.findViewById(R.id.btMtvDelete);
			viewHolder.ivOrderedMtvPlayingLogo = (ImageView) view.findViewById(R.id.ivOrderedMtvPlayingLogo);

			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.tvOrderedMtvItemSerialIndex.setText(String.valueOf(position+1));
 		viewHolder.tvOrderedMtvName.setText(mtv.name);
 		viewHolder.tvOrderedMtvArtist.setText(mtv.artist);
 		viewHolder.tvOrderedMtvArtist.setVisibility(View.VISIBLE);
		viewHolder.btOrderedMtvTop.setTag(mtv);
		viewHolder.btOrderedMtvSing.setTag(mtv);
		viewHolder.btOrderedMtvDelete.setTag(mtv);
		
		viewHolder.btOrderedMtvSing.setActivated(mSelectItem == 0);
		viewHolder.btOrderedMtvTop.setActivated(mSelectItem == 1);
		viewHolder.btOrderedMtvDelete.setActivated(mSelectItem == 2);
		
		// 演唱
		viewHolder.btOrderedMtvSing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!AppContext.getNetworkSensor().hasAvailableNetwork())
					DialogUtils.toast("当前没有网络！", false);
				mCurrentListSelectedItem = position;
				lastClickedButtonIndex = 0;
				Mtv mtv = (Mtv) v.getTag();
				MobclickAgent.onEvent(mActivity, Constants.KS_UMENG_SING, "SING_FROM_ORDERED");
				toSingMtv(mtv);
				int index = (mCurrentPageNum-1) * (int)Constants.ORDERD_MTV_PAGESIZE + position;
				mOrderLogic.singMtv(mActivity.getApplication(), mtv, true, index);
			}
		});

		// 置顶
		viewHolder.btOrderedMtvTop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentListSelectedItem = position;
				int index = (mCurrentPageNum-1) * (int)Constants.ORDERD_MTV_PAGESIZE + position;
				lastClickedButtonIndex = 1;
				Mtv mtv = (Mtv) v.getTag();
				mOrderLogic.topMtv(mtv, index);
//				Message msg = new Message();
//				msg.what = Constants.MSG_NOTIFY_ORDERED_LISTVIEW;
//				EventBus.getDefault().post(msg);
			}
		});
		
		// 删除
		viewHolder.btOrderedMtvDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Mtv mtv = (Mtv) v.getTag();
				showDeleteDialog(mtv, position);
			}
		});
		int index = (mCurrentPageNum-1) * (int)Constants.ORDERD_MTV_PAGESIZE + position;
		if(MainApplication.isSingActivityAliving) {
			if (index == mOrderLogic.getCurrentMtvIndex()) {
				viewHolder.ivOrderedMtvPlayingLogo.setVisibility(View.VISIBLE);
			}else {
				viewHolder.ivOrderedMtvPlayingLogo.setVisibility(View.INVISIBLE);
			}
		}else {
			viewHolder.ivOrderedMtvPlayingLogo.setVisibility(View.INVISIBLE);
			mOrderLogic.setCurrentMtvIndex(-1);
		}
		
		if(index == mCurrentListSelectedItem) {
			if(lastClickedButtonIndex == 1) {
				viewHolder.btOrderedMtvTop.setActivated(true);
			}else if(lastClickedButtonIndex == 2) {
				viewHolder.btOrderedMtvDelete.setActivated(true);
			}else if(lastClickedButtonIndex == 0) {
				viewHolder.btOrderedMtvSing.setActivated(true);
			}
		}
		return view;
	}
	
	private void toSingMtv(Mtv mtv) {
		Intent singIntent = new Intent(mActivity, PlayActivity.class);
		singIntent.putExtra("mtv", mtv);
		mActivity.startActivity(singIntent);
	}

	private void showDeleteDialog(final Mtv mtv, final int position) {
		final AlertDialog dialog =  new AlertDialog.Builder(mActivity).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.custom_alert_dialog);
		TextView tvPlayControllerExitPrompt = (TextView) dialog.getWindow().findViewById(R.id.tvPlayControllerExitPrompt);
		tvPlayControllerExitPrompt.setText("您确定要删除么？");
		dialog.getWindow().findViewById(R.id.btPlayControllerExitOk).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				mCurrentListSelectedItem = position;
				int index = (mCurrentPageNum-1) * (int)Constants.ORDERD_MTV_PAGESIZE + position;
				lastClickedButtonIndex = 2;
				mOrderLogic.deleteMtv(mtv, index);
//				Message msg = new Message();
//				msg.what = Constants.MSG_NOTIFY_ORDERED_LISTVIEW;
//				EventBus.getDefault().post(msg);
			}
		});
		dialog.getWindow().findViewById(R.id.btPlayControllerExitCancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	public static class ViewHolder {
		public LinearLayout ll_ordered_mtv_item;
		public TextView tvOrderedMtvItemSerialIndex;
		public TextView tvOrderedMtvName;
		public TextView tvOrderedMtvArtist;
		public View btOrderedMtvTop;
		public View btOrderedMtvSing;
		public View btOrderedMtvDelete;
		public ImageView ivOrderedMtvPlayingLogo;
	}

	public void setCurrentPageNum(int nPageNum){
		mCurrentPageNum = nPageNum;
	}
	
	public int getCurrentPageNum(){
		return mCurrentPageNum;
	}
}
