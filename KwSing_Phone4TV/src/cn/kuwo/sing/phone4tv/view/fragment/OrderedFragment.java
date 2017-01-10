/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.fragment;

import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.bean.Mtv;
import cn.kuwo.sing.phone4tv.commons.context.Constants;
import cn.kuwo.sing.phone4tv.commons.context.MainApplication;
import cn.kuwo.sing.phone4tv.commons.socket.MessageCommons;
import cn.kuwo.sing.phone4tv.commons.socket.RequestMessage;
import cn.kuwo.sing.phone4tv.commons.socket.SocketManager;
import cn.kuwo.sing.phone4tv.view.adapter.OrderedAdapter;

import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

/**
 * @Package cn.kuwo.sing.phone4tv.view.fragment
 * 
 * @Date Mar 12, 2014 10:56:19 AM
 *
 * @Author wangming
 *
 */
public class OrderedFragment extends BaseProgressFragment {
	private static final String LOG_TAG = "OrderedFragment";
	private View mContentView;
	private PullToRefreshListView mPullRefreshListView;
	private OrderedAdapter mOrderedAdapter;
	private int mCurrentPageNum = 1;
	private int mTotalPage = 1;
	private TextView empty;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_detail, null);
		return inflater.inflate(R.layout.fragment_progress_empty, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(mContentView);
		setEmptyText(R.string.load_data_fail);
		initData();
		initView();
		obtainData();
	}

	private void initData() {
		getActivity().setTitle("已点歌曲");
		SocketManager.getInstance().addHander(OrderedFragment.class.getSimpleName(), mUpdateConversationHandler);
	}
	
	private void initView() {
		mPullRefreshListView = (PullToRefreshListView) getActivity().findViewById(R.id.lvFragmentDetail);
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				switch (refreshView.getCurrentMode()) {
				case PULL_FROM_START:
					toRefresh();
					break;
				default:
					break;
				}
			}
		});
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if(mTotalPage == mCurrentPageNum)
					AppMsg.makeText(getActivity(), "亲，没有更多了", AppMsg.STYLE_CONFIRM)
					.setLayoutGravity(Gravity.TOP)
					.show();
			}
		});
		

		
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = null;

		mOrderedAdapter = new OrderedAdapter(getActivity());
		swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mOrderedAdapter, Constants.LIST_ITEM_ANIMATION_DELAY, Constants.LIST_ITEM_ANIMATION_DURATION);
		
		swingBottomInAnimationAdapter.setAbsListView(actualListView);
		actualListView.setAdapter(swingBottomInAnimationAdapter);
		
		empty = (TextView)getActivity().findViewById(android.R.id.empty);
		empty.setOnClickListener(mOnClickListener);
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case android.R.id.empty:
				toRefresh();
				break;

			default:
				break;
			}
		}
	};
	
	private void obtainData() {
		setContentShown(false);
		loadMtvList(1);
	}
	
	private void toRefresh() {
		mOrderedAdapter.clearMtvList();
		loadMtvList(1);
	}
	
	private void loadMtvList(final int pageNum) {
		final SocketChannel socketChannel = MainApplication.s_socketChannel;
		if(socketChannel != null && socketChannel.isConnected()) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						RequestMessage requestMessage = SocketManager.getInstance().createRequestMessage(
								MessageCommons.CMD_ORDERED_MTV, 
								""+pageNum, 
								null,
								null,
								System.currentTimeMillis());
						SocketManager.getInstance().writeRequestMessage(socketChannel, requestMessage);
					} catch (SocketException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}else {
			MainApplication.isConnecting = false;
			AppMsg.makeText(getActivity(), "没有连接电视，请先扫描二维码!", AppMsg.STYLE_ALERT)
			.setLayoutGravity(Gravity.TOP)
			.show();
			//显示失败页面
			if(Build.VERSION.SDK_INT >= 14)
				setContentEmpty(true);
			setContentShown(false);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		SocketManager.getInstance().removeHandler(OrderedFragment.class.getSimpleName());
	}

	private Handler mUpdateConversationHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MessageCommons.CMD_ORDERED_MTV_WRITE:
				mPullRefreshListView.onRefreshComplete();
				mOrderedAdapter.hideWaitingDialog();
				
				List<Mtv> mtvList = (List<Mtv>) msg.obj;
				if(mtvList != null && mtvList.size() > 0) {
					if(Build.VERSION.SDK_INT >= 14)
						setContentEmpty(false);
					mOrderedAdapter.setOrderedData((List<Mtv>) msg.obj);
				}else { 
					setEmptyText("亲,列表为空");
					if(Build.VERSION.SDK_INT >= 14)
						setContentEmpty(true);
				}
				setContentShown(true);
				break;
			case MessageCommons.RESULT_ORDERED_MTV_TOP:
			case MessageCommons.RESULT_ORDERED_MTV_DELETE:
				loadMtvList(1);
				break;
			default:
				break;
			}
		}
    	
    };
}
