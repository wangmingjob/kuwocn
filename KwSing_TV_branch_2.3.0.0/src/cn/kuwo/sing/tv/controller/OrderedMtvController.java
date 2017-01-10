/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic;
import cn.kuwo.sing.tv.utils.DensityUtils;
import cn.kuwo.sing.tv.view.adapter.OrderedMtvAdapter;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.controller
 * 
 * @Date 2013-3-29, 下午5:36:49, 2013
 * 
 * @Author wangming
 * 
 */
public class OrderedMtvController extends BaseController {
	private ListView lvOrderedMtv;
	private OrderSerializeLogic mOrderLogic;
	private OrderedMtvAdapter mOrderedMtvAdapter;
	private TextView tvOrderedMtvNumber;
	private TextView tvSingNextMtvName; 
	private RelativeLayout rlOrderedMtvPrompt;
	private RelativeLayout rlOrderedMtvEmptyPrompt;
	private Button btOrderedMtvGoOrder;
	private Button btOrderedMtvDeleteRepeat;
	private RelativeLayout rlOrderedMtv;
	private ImageView ivOrderedMtvListDivide;
	private TextView tvOrderedMtvPrompt;
	
	private Button btOrderedPreviousPage;
	private Button btOrderedNextPage;
	private TextView tvOrderedlPagePrompt;
	private RelativeLayout rlOrderedPagerButton;
//	private int nCurrentPageNum = 1;
	private ListViewDecorator mListViewDecorator;
	private Button btOrderedMtvClearList;

	public OrderedMtvController(Activity activity) {
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
		default:
			break;
		}
	}
	
	private void showClearAllMtvDialog() {
		final AlertDialog dialog =  new AlertDialog.Builder(activity).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.custom_alert_dialog);
		TextView tvPlayControllerExitPrompt = (TextView) dialog.getWindow().findViewById(R.id.tvPlayControllerExitPrompt);
		tvPlayControllerExitPrompt.setText("您确定要清空列表么？");
		dialog.getWindow().findViewById(R.id.btPlayControllerExitOk).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(MainApplication.isSingActivityAliving) {
					Message msg = new Message();
					msg.what = Constants.MSG_CLOSE_PLAY_ACTIVITY_OR_PLAY_USER_ACTIVITY_WHEN_CLICK_HOME;
					EventBus.getDefault().post(msg);
				}
				
				mOrderLogic.clearAllMtv();
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_ORDERED_CLEAR_ALL, Constants.KS_UMENG_SUCCESS);
			}
		});
		dialog.getWindow().findViewById(R.id.btPlayControllerExitCancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_ORDERED_CLEAR_ALL, Constants.KS_UMENG_FAIL);
			}
		});
	}

	private void initData() {
		mOrderLogic = OrderSerializeLogic.getInstance();
	}
	
	private void initView() {
		btOrderedMtvClearList = (Button)activity.findViewById(R.id.btOrderedMtvClearList);
		btOrderedMtvClearList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showClearAllMtvDialog();
			}
		});
		rlOrderedPagerButton = (RelativeLayout)activity.findViewById(R.id.rlOrderedPagerButton);
		tvOrderedMtvPrompt = (TextView)activity.findViewById(R.id.tvOrderedMtvPrompt);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(DensityUtils.dip2px(activity, 50), DensityUtils.dip2px(activity, 5), 0, DensityUtils.dip2px(activity, 5));
		tvOrderedMtvPrompt.setLayoutParams(params);
		ivOrderedMtvListDivide = (ImageView) activity.findViewById(R.id.ivOrderedMtvListDivide);
		ivOrderedMtvListDivide.setVisibility(View.VISIBLE);
		rlOrderedMtv = (RelativeLayout) activity.findViewById(R.id.rlOrderedMtv);
		rlOrderedMtv.setPadding(DensityUtils.dip2px(activity, 0), DensityUtils.dip2px(activity, 0), DensityUtils.dip2px(activity, 0), DensityUtils.dip2px(activity, 0));
		
		btOrderedMtvDeleteRepeat = (Button) activity.findViewById(R.id.btOrderedMtvDeleteRepeat);
		btOrderedMtvDeleteRepeat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_DELETE_REPEAT, Constants.KS_UMENG_SUCCESS);
				OrderSerializeLogic.getInstance().deleteRepeatMtv();
				onDataChanged();
			}
		});
		
		View layout = activity.findViewById(R.id.flSingOrderedMtvList);
		lvOrderedMtv = (ListView) activity.findViewById(R.id.lvOrderedMtv);
//		try {
//	         Field f = View.class.getDeclaredField("mScrollCache");
//	         f.setAccessible(true);
//	         Object scrollabilityCache  = f.get(lvOrderedMtv);
//	         f = f.getType().getDeclaredField("scrollBar");
//	         f.setAccessible(true); 
//	         Object scrollBarDrawable = f.get(scrollabilityCache);
//	         f = f.getType().getDeclaredField("mVerticalThumb");
//	         f.setAccessible(true); 
//	         Drawable drawable = (Drawable) f.get(scrollBarDrawable); 
//	         drawable = activity.getResources().getDrawable(R.drawable.list_scrollbar);
//	         f.set(scrollBarDrawable, drawable);
//	     } catch (Exception e) {
//	         KuwoLog.e("OrderedMtvController", e);
//	     }
		if(layout == null) {
			lvOrderedMtv.setNextFocusLeftId(R.id.rbOrderedMtv); //设置已点歌曲列表向左焦点
			btOrderedMtvDeleteRepeat.setVisibility(View.VISIBLE);
		}else {
			lvOrderedMtv.setNextFocusLeftId(R.id.ivSingMenuScoreMtv);
			lvOrderedMtv.setNextFocusRightId(R.id.ivSingMenuMtvPlatform);
			btOrderedMtvDeleteRepeat.setVisibility(View.INVISIBLE);
		}
		rlOrderedMtvPrompt = (RelativeLayout) activity.findViewById(R.id.rlOrderedMtvPrompt);
		rlOrderedMtvPrompt.setVisibility(View.VISIBLE);
		rlOrderedMtvEmptyPrompt = (RelativeLayout) activity.findViewById(R.id.rlOrderedMtvEmptyPrompt);
		rlOrderedMtvEmptyPrompt.setVisibility(View.INVISIBLE);
		btOrderedMtvGoOrder = (Button) activity.findViewById(R.id.btOrderedMtvGoOrder);
		btOrderedMtvGoOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳到热门推荐页面
				View view = activity.findViewById(R.id.rbMtvCategory);
				if(view != null) 
					view.requestFocus();
			}
		});

		tvOrderedMtvNumber = (TextView) activity.findViewById(R.id.tvOrderedMtvNumber);
		TextView tvOrderedMtvNumberEnd = (TextView) activity.findViewById(R.id.tvOrderedMtvNumberEnd);
		if(mOrderLogic.getOrderedMtvList().size() > 99) {
			tvOrderedMtvNumber.setText("99+");
		}else {
			tvOrderedMtvNumber.setText(mOrderLogic.getOrderedMtvList().size() + "");
		}
		tvSingNextMtvName = (TextView) activity.findViewById(R.id.tvSingNextMtvName);
		
		tvOrderedlPagePrompt = (TextView)activity.findViewById(R.id.tvOrderedPagePrompt);
		
		btOrderedPreviousPage = (Button) activity.findViewById(R.id.btOrderedPreviousPage);
		btOrderedPreviousPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int nPageNum = mOrderedMtvAdapter.getCurrentPageNum();
				if (nPageNum==1)
					return;
				loadingAnimationSetting();
				mOrderedMtvAdapter.setCurrentPageNum(--nPageNum);				
				onDataChanged();
			}
		});
		
		
		btOrderedNextPage = (Button) activity.findViewById(R.id.btOrderedNextPage);
		btOrderedNextPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int nPageNum = mOrderedMtvAdapter.getCurrentPageNum();
				if (nPageNum>=mOrderLogic.getTotalPageNum())
					return;
				loadingAnimationSetting();
				mOrderedMtvAdapter.setCurrentPageNum(++nPageNum);
				onDataChanged();
			}
		});
		
	}
	
	private void loadingAnimationSetting() {
		if(Constants.isUseAnimation) {
			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mOrderedMtvAdapter);
			swingBottomInAnimationAdapter.setAbsListView(lvOrderedMtv);
			lvOrderedMtv.setAdapter(swingBottomInAnimationAdapter);
		}else {
			lvOrderedMtv.setAdapter(mOrderedMtvAdapter);
		}
	}
	
	private void processLogic() {
		//mOrderedMtvAdapter = new OrderedMtvAdapter(activity, mHideArticle, mOrderLogic.getOrderedMtvList()); //一定要保证adatper数据源唯一，否则会notify无效,不可重新赋值给adatper引用
		mOrderedMtvAdapter = new OrderedMtvAdapter(activity, mOrderLogic.getOrderedMtvFixedSizeList(1)); //一定要保证adatper数据源唯一，否则会notify无效,不可重新赋值给adatper引用
		mOrderedMtvAdapter.setCurrentPageNum(1);
		loadingAnimationSetting();
		if(mOrderLogic.getOrderedMtvList().size() == 0) {
			showOrderedMtvEmptyView();
		}else {
			rlOrderedMtvPrompt.setVisibility(View.VISIBLE);
		}
		int[] buttons = {R.id.btMtvSing, R.id.btMtvTop, R.id.btMtvDelete};
		mListViewDecorator = new ListViewDecorator(lvOrderedMtv, buttons);
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

		if(OrderSerializeLogic.getInstance().getOrderedMtvList().size() > 99) 
			tvOrderedMtvNumber.setText("99+");
		else 
			tvOrderedMtvNumber.setText(OrderSerializeLogic.getInstance().getOrderedMtvList().size() + "");
		
		tvOrderedlPagePrompt.setText(mOrderedMtvAdapter.getCurrentPageNum()+" / "+mOrderLogic.getTotalPageNum());
		
	}
	
	private void showOrderedMtvEmptyView() {
		rlOrderedPagerButton.setVisibility(View.INVISIBLE);
		ivOrderedMtvListDivide.setVisibility(View.INVISIBLE);
		rlOrderedMtvPrompt.setVisibility(View.INVISIBLE);
		lvOrderedMtv.setVisibility(View.INVISIBLE);
		rlOrderedMtvEmptyPrompt.setVisibility(View.VISIBLE);
//		btOrderedMtvGoOrder.requestFocus();
	}
	
	private void showOrderedMtvListView() { 
		ivOrderedMtvListDivide.setVisibility(View.VISIBLE);
		rlOrderedMtvEmptyPrompt.setVisibility(View.INVISIBLE);
		rlOrderedPagerButton.setVisibility(View.VISIBLE);
		rlOrderedMtvPrompt.setVisibility(View.VISIBLE); 
		lvOrderedMtv.setVisibility(View.VISIBLE);
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
