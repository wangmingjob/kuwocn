/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic;
import cn.kuwo.sing.tv.view.adapter.PlayOrderedMtvAdapter;
import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.controller
 * 
 * @Date 2013-3-29, 下午5:36:49, 2013
 * 
 * @Author wangming
 * 
 */
public class PlayOrderedMtvController extends BaseController {
	private ListView lvPlayOrderedMtv;
	private OrderSerializeLogic mOrderLogic;
	private PlayOrderedMtvAdapter mOrderedMtvAdapter;
	private TextView tvPlayOrderedMtvNumber;
	private TextView tvSingNextMtvName; 
	private RelativeLayout rlPlayOrderedMtvPrompt;
	private RelativeLayout rlPlayOrderedMtvEmptyPrompt;
	private Button btPlayOrderedMtvGoOrder;
	private RelativeLayout rlPlayOrderedMtv;
	private ImageView ivPlayOrderedMtvListDivide;
	private TextView tvPlayOrderedMtvPrompt;
	private ImageView ivPlayOrderedMtvClose;
	private Button btPlayOrderedPreviousPage;
	private Button btOrderedNextPage;
	private TextView tvPlayOrderedPagePrompt;

	public PlayOrderedMtvController(Activity activity) {
		super(activity);
		EventBus.getDefault().register(this);
		initData();
		initView();
		processLogic();
	}
	
	public void onEvent(Message msg) {
		switch (msg.what) {
		case Constants.MSG_ORDERED_MTV_DATA_CHANGED:
			onDataChanged();
			break;
		case Constants.MSG_SHOW_ORDERED_MTV_IN_PLAY:
			lvPlayOrderedMtv.requestFocus();
			break;
		default:
			break;
		}
	}

	private void initData() {
		mOrderLogic = OrderSerializeLogic.getInstance();
	}
	
	private void initView() {
		tvPlayOrderedMtvPrompt = (TextView)activity.findViewById(R.id.tvPlayOrderedMtvPrompt);
		ivPlayOrderedMtvListDivide = (ImageView) activity.findViewById(R.id.ivPlayOrderedMtvListDivide);
		ivPlayOrderedMtvListDivide.setVisibility(View.VISIBLE);
		rlPlayOrderedMtv = (RelativeLayout) activity.findViewById(R.id.rlPlayOrderedMtv);
		ivPlayOrderedMtvClose = (ImageView) activity.findViewById(R.id.ivPlayOrderedMtvClose);
		ivPlayOrderedMtvClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final View layout = activity.findViewById(R.id.flSingOrderedMtvList);
				if(layout != null) {
					layout.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_out));
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							layout.setVisibility(View.INVISIBLE); 
						}
					}, 1400);
					layout.setVisibility(View.INVISIBLE);
					Message msg = new Message();
					msg.what = Constants.MSG_PLAY_CONTROLLER_CLOSE_ORDERED_MTV;
					EventBus.getDefault().post(msg);
				}
			}
		});
				
		View layout = activity.findViewById(R.id.flSingOrderedMtvList);
		lvPlayOrderedMtv = (ListView) activity.findViewById(R.id.lvPlayOrderedMtv);

		if(layout == null) {
			ivPlayOrderedMtvClose.setVisibility(View.INVISIBLE);
		}else {
			ivPlayOrderedMtvClose.setVisibility(View.VISIBLE);
		}
		rlPlayOrderedMtvPrompt = (RelativeLayout) activity.findViewById(R.id.rlPlayOrderedMtvPrompt);
		rlPlayOrderedMtvPrompt.setVisibility(View.VISIBLE);
		rlPlayOrderedMtvEmptyPrompt = (RelativeLayout) activity.findViewById(R.id.rlPlayOrderedMtvEmptyPrompt);
		rlPlayOrderedMtvEmptyPrompt.setVisibility(View.INVISIBLE);
		btPlayOrderedMtvGoOrder = (Button) activity.findViewById(R.id.btPlayOrderedMtvGoOrder);
		btPlayOrderedMtvGoOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳到热门推荐页面
				View view = activity.findViewById(R.id.rbMtvCategory);
				if(view != null) 
					view.requestFocus();
			}
		});

		tvPlayOrderedMtvNumber = (TextView) activity.findViewById(R.id.tvPlayOrderedMtvNumber);
		TextView tvOrderedMtvNumberEnd = (TextView) activity.findViewById(R.id.tvPlayOrderedMtvNumberEnd);
		if(mOrderLogic.getOrderedMtvList().size() > 99) {
			tvPlayOrderedMtvNumber.setText("99+");
		}else {
			tvPlayOrderedMtvNumber.setText(mOrderLogic.getOrderedMtvList().size() + "");
		}
		tvSingNextMtvName = (TextView) activity.findViewById(R.id.tvSingNextMtvName);
		
		tvPlayOrderedPagePrompt = (TextView)activity.findViewById(R.id.tvPlayOrderedPagePrompt);
		
		btPlayOrderedPreviousPage = (Button) activity.findViewById(R.id.btPlayOrderedPreviousPage);
		btPlayOrderedPreviousPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int nPageNum = mOrderedMtvAdapter.getCurrentPageNum();
				if (nPageNum==1)
					return;
				mOrderedMtvAdapter.setCurrentPageNum(--nPageNum);	
				onDataChanged();
			}
		});
		
		
		btOrderedNextPage = (Button) activity.findViewById(R.id.btPlayOrderedNextPage);
		btOrderedNextPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int nPageNum = mOrderedMtvAdapter.getCurrentPageNum();
				if (nPageNum>=mOrderLogic.getTotalPageNum())
					return;
				mOrderedMtvAdapter.setCurrentPageNum(++nPageNum);
				onDataChanged();
			}
		});
		
	}
	
	private void processLogic() {
		mOrderedMtvAdapter = new PlayOrderedMtvAdapter(activity, mOrderLogic.getOrderedMtvFixedSizeList(1)); //一定要保证adatper数据源唯一，否则会notify无效,不可重新赋值给adatper引用
		mOrderedMtvAdapter.setCurrentPageNum(1);
		lvPlayOrderedMtv.setAdapter(mOrderedMtvAdapter);
		if(mOrderLogic.getOrderedMtvList().size() == 0) {
			showOrderedMtvEmptyView();
		}else {
			rlPlayOrderedMtvPrompt.setVisibility(View.VISIBLE);
		}
		int[] buttons = {R.id.btPlayMtvSing, R.id.btPlayMtvTop, R.id.btPlayMtvDelete};
		new ListViewDecorator(lvPlayOrderedMtv, buttons);
		onDataChanged();
	}
	
	private void onDataChanged() {
		if (mOrderLogic.getOrderedMtvList().size() == 0) {
			showOrderedMtvEmptyView();
		}else {
			int pageCurNum = mOrderedMtvAdapter.getCurrentPageNum();
			int pageTotalNum = mOrderLogic.getTotalPageNum();
			if (pageCurNum>pageTotalNum)
				pageCurNum = pageTotalNum;
			mOrderedMtvAdapter.setCurrentPageNum(pageCurNum);
			mOrderedMtvAdapter.setOrderedMtvList(mOrderLogic.getOrderedMtvFixedSizeList(pageCurNum));
			showOrderedMtvListView();
		}
		if(mOrderLogic.getOrderedMtvList().size() > 99) 
			tvPlayOrderedMtvNumber.setText("99+");
		else 
			tvPlayOrderedMtvNumber.setText(mOrderLogic.getOrderedMtvList().size() + "");
		tvPlayOrderedPagePrompt.setText(mOrderedMtvAdapter.getCurrentPageNum()+" / "+mOrderLogic.getTotalPageNum());
		
		// 下一首
		if (tvSingNextMtvName != null) {
			Mtv next = OrderSerializeLogic.getInstance().peekMtv();
			if (next != null) {
				tvSingNextMtvName.setText(next.name);
			} else {
				tvSingNextMtvName.setText("无");
			}
		}
	}
	
	private void showOrderedMtvEmptyView() {
		ivPlayOrderedMtvListDivide.setVisibility(View.INVISIBLE);
		rlPlayOrderedMtvPrompt.setVisibility(View.INVISIBLE);
		lvPlayOrderedMtv.setVisibility(View.INVISIBLE);
		rlPlayOrderedMtvEmptyPrompt.setVisibility(View.VISIBLE);
	}
	
	private void showOrderedMtvListView() { 
		ivPlayOrderedMtvListDivide.setVisibility(View.VISIBLE);
		rlPlayOrderedMtvEmptyPrompt.setVisibility(View.INVISIBLE);
		rlPlayOrderedMtvPrompt.setVisibility(View.VISIBLE); 
		lvPlayOrderedMtv.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		onDataChanged();
	}
		
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
