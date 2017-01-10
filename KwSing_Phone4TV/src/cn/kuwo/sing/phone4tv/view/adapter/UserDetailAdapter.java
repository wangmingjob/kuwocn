/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.adapter;

import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.bean.UserMtv;
import cn.kuwo.sing.phone4tv.commons.context.Constants;
import cn.kuwo.sing.phone4tv.commons.context.MainApplication;
import cn.kuwo.sing.phone4tv.commons.socket.MessageCommons;
import cn.kuwo.sing.phone4tv.commons.socket.RequestMessage;
import cn.kuwo.sing.phone4tv.commons.socket.SocketManager;

import com.devspark.appmsg.AppMsg;

/**
 * @Package cn.kuwo.sing.phone4tv.view.adapter
 * 
 * @Date Mar 6, 2014 6:10:25 PM
 *
 * @Author wangming
 *
 */
public class UserDetailAdapter extends BaseAdapter {
	private static final String LOG_TAG = "DetailAdapter";
	private Activity mActivity;
	private int mCurrentPageNum;
	private List<UserMtv> mUserMtvList = new ArrayList<UserMtv>();
	private int mPageSize = Constants.PAGE_SIZE_USER_DETAIL_MTV_LIST;
	//private SocketManager mSocketManager;
	
	public UserDetailAdapter(Activity activity) {
		mActivity = activity;
		//mSocketManager = new SocketManager(null);
	}
	
	public void setMtvData(int currentPageNum, List<UserMtv> mtvList) {
		mCurrentPageNum = currentPageNum;
		mUserMtvList.addAll(mtvList);
		notifyDataSetChanged();
	}
	
	public void clearMtvList() {
		mUserMtvList.clear();
	}

	@Override
	public int getCount() {
		return mUserMtvList.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserMtvList.get(position);
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
			view = View.inflate(mActivity, R.layout.item_user_detail_list, null);
			viewHolder = new ViewHolder();
			viewHolder.tvDetailUserMtvArtist = (TextView) view.findViewById(R.id.tvDetailUserMtvArtist);
			viewHolder.tvDetailUserMtvName = (TextView) view.findViewById(R.id.tvDetailUserMtvName);
			viewHolder.btDetailUserMtvPlay = view.findViewById(R.id.btDetailUserMtvPlay);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		final UserMtv mtv = mUserMtvList.get(position);
		viewHolder.tvDetailUserMtvName.setText(mtv.title);
		viewHolder.tvDetailUserMtvArtist.setText(mtv.uname);
		
		viewHolder.btDetailUserMtvPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				performCommand(MessageCommons.CMD_PLAY, mtv);
			}
		});
		return view;
	}
	
	private void performCommand(final int cmd, final UserMtv userData) {
		final SocketChannel socketChannel = MainApplication.s_socketChannel;
		if(socketChannel != null && socketChannel.isConnected()) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						RequestMessage requestMessage = SocketManager.getInstance().createRequestMessage(
								cmd, 
								null, 
								null,
								userData,
								System.currentTimeMillis());
						SocketManager.getInstance().writeRequestMessage(socketChannel, requestMessage);
					} catch (SocketException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}else {
			AppMsg.makeText(mActivity, "没有连接电视，请先扫描二维码!", AppMsg.STYLE_ALERT)
			.setLayoutGravity(Gravity.TOP)
			.show();
		}
	}
	
	private static class ViewHolder {
		TextView tvDetailUserMtvSerialIndex;
		TextView tvDetailUserMtvArtist;
		TextView tvDetailUserMtvName;
		View btDetailUserMtvPlay;
	}

}
