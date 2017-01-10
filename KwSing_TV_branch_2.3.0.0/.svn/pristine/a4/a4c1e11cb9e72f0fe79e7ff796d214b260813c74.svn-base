/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.PagedData;
import cn.kuwo.sing.tv.bean.SearchPromptObject;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.logic.PagedDataHandler;
import cn.kuwo.sing.tv.utils.ASRDialog;
import cn.kuwo.sing.tv.utils.DensityUtils;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.view.adapter.DetailAdapter;
import cn.kuwo.sing.tv.view.adapter.HotKeywordListAdapter;
import cn.kuwo.sing.tv.view.adapter.SearchPromptAdapter;
import cn.kuwo.sing.tv.view.widget.LetterView;
import cn.kuwo.sing.tv.view.widget.LetterView.OnPressEnterListener;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.controller
 *
 * @Date 2013-4-8, 下午5:08:00, 2013
 *
 * @Author wangming
 *
 */
public class PinyinOrderController extends BaseController implements OnPressEnterListener {
	private static final String LOG_TAG = "PinyinOrderController";
	private TextView tvPinyinOrderSearchContent;
	private Button btPinyinOrderSeachBack;
	private Button btPinyinOrderSeachClear;
	private int mTotalPageNum;
	private int mCurrentPageNum = 1;
	private ListView lvPinyinOrder;
	private ListLogic mListLogic;
	private Button btPinyinOrderPreviousPage;
	private Button btPinyinOrderNextPage;
	private TextView tvPinyinOrderPagePrompt;
	private DetailAdapter mAdapter;
	private HotKeywordListAdapter mHotKeywordListAdapter;
	private SearchPromptAdapter mSearchPromptAdapter;
	private LetterView letterView;
	private TextView tvPinyinOrderInputContent;
	private TextView tvPinyinOrderSearchResultNum;
	private ProgressBar progressBar; 
	private TextView tvPinyinNoDataPrompt;
	private TextView tvPinyinOrderSearchPrompt;
	private RelativeLayout rlPinyinOrderResultPrompt;
	private Button btPinyinOrderDigital;
	private View digitalView;
	private Button btDigitalClose;
	private Button btDigitalViewOne;
	private Button btDigitalViewTwo;
	private Button btDigitalViewThree;
	private Button btDigitalViewFour;
	private Button btDigitalViewFive;
	private Button btDigitalViewSix;
	private Button btDigitalViewSeven;
	private Button btDigitalViewEight;
	private Button btDigitalViewNine;
	private Button btDigitalViewZero;
	private PopupWindow mDigitalPopupWindow;
	private RelativeLayout rlPinyinOrderPagerButton;
	private LinearLayout llPinyinOrderRequestFail;
	private Button btPinyinOrderRequestFail;
	private boolean loadFirstPage = true;
	private ListView lv_pinyin_order_search_prompt;
	private Button btPinyinOrderSeachGo;
	private ProgressBar pb_pinyin_order_search_prompt;
	private Button iv_pinyin_order_voice_controller;
	private EventBus mEventBus;
	private GridView gvPinyinOrderHotKeyword;
	private RelativeLayout rlPinyinOrderHotKeywordView;
	
	public PinyinOrderController(Activity activity) {
		super(activity);
		initData();
		initView();
		
	}
	
	private void initData() {
		mListLogic = new ListLogic();
		mEventBus = EventBus.getDefault();
		mEventBus.register(this);
	}
	
	@Override
	public void onDestroy() {
		mEventBus.unregister(this);
		super.onDestroy();
	}
	
	public void onEvent(Message msg) {
		switch (msg.what) {
		case Constants.MSG_VOICE_QUERY_SUCCESS:
//			mQueryKeyword = (String) msg.obj;
//			mLastQueryKeyword = (String) msg.obj;
			lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
			gvPinyinOrderHotKeyword.setVisibility(View.GONE);
			loadSearchResultListByGeneral(1,(String)msg.obj);
			break;
		default:
			break;
		}
	}

	private void initView() {
		rlPinyinOrderHotKeywordView = (RelativeLayout)activity.findViewById(R.id.rlPinyinOrderHotKeywordView);
		gvPinyinOrderHotKeyword = (GridView)activity.findViewById(R.id.gvPinyinOrderHotKeyword);
		gvPinyinOrderHotKeyword.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String keyword = (String) mHotKeywordListAdapter.getItem(position);
				btPinyinOrderSeachClear.requestFocus();
				if(!TextUtils.isEmpty(keyword)){ 
					hideHotKeywordView();
					MobclickAgent.onEvent(activity, Constants.KS_UMENG_PINYIN_HOT_KEYWORD, Constants.KS_UMENG_SUCCESS);
					loadSearchResultListFirstByGeneral(keyword);
				}
			}
		});
		llPinyinOrderRequestFail = (LinearLayout) activity.findViewById(R.id.llPinyinOrderRequestFail);
		llPinyinOrderRequestFail.setVisibility(View.INVISIBLE);
		btPinyinOrderRequestFail = (Button) activity.findViewById(R.id.btPinyinOrderRequestFail);
		btPinyinOrderRequestFail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btPinyinOrderPreviousPage.requestFocus();
				btPinyinOrderNextPage.requestFocus();
				String keyword = tvPinyinOrderSearchContent.getText().toString();
				if(loadFirstPage)
					loadSearchResultListByGeneral(1, keyword);
				else
					loadSearchResultListByGeneral(mCurrentPageNum+1, keyword);
			}
		});
		rlPinyinOrderPagerButton = (RelativeLayout) activity.findViewById(R.id.rlPinyinOrderPagerButton);
		rlPinyinOrderPagerButton.setVisibility(View.INVISIBLE);
		
		digitalView = View.inflate(activity, R.layout.digital_view, null);
		btDigitalClose = (Button) digitalView.findViewById(R.id.btDigitalClose);
		btDigitalClose.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewOne = (Button) digitalView.findViewById(R.id.btDigitalViewOne);
		btDigitalViewOne.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewTwo = (Button) digitalView.findViewById(R.id.btDigitalViewTwo);
		btDigitalViewTwo.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewThree = (Button) digitalView.findViewById(R.id.btDigitalViewThree);
		btDigitalViewThree.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewFour = (Button) digitalView.findViewById(R.id.btDigitalViewFour);
		btDigitalViewFour.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewFive = (Button) digitalView.findViewById(R.id.btDigitalViewFive);
		btDigitalViewFive.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewSix = (Button) digitalView.findViewById(R.id.btDigitalViewSix);
		btDigitalViewSix.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewSeven = (Button) digitalView.findViewById(R.id.btDigitalViewSeven);
		btDigitalViewSeven.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewEight = (Button) digitalView.findViewById(R.id.btDigitalViewEight);
		btDigitalViewEight.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewNine = (Button) digitalView.findViewById(R.id.btDigitalViewNine);
		btDigitalViewNine.setOnClickListener(mOnDigitalClickListener);
		btDigitalViewZero = (Button) digitalView.findViewById(R.id.btDigitalViewZero);
		btDigitalViewZero.setOnClickListener(mOnDigitalClickListener);
		iv_pinyin_order_voice_controller = (Button) activity.findViewById(R.id.iv_pinyin_order_voice_controller);
		iv_pinyin_order_voice_controller.setOnClickListener(mOnClickListener);
		
		mDigitalPopupWindow = new PopupWindow(digitalView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mDigitalPopupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		mDigitalPopupWindow.setOutsideTouchable(true);
		mDigitalPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mDigitalPopupWindow.update();
		mDigitalPopupWindow.setTouchable(true);
		mDigitalPopupWindow.setFocusable(true);
		
		
		btPinyinOrderDigital = (Button) activity.findViewById(R.id.btPinyinOrderDigital);
		btPinyinOrderDigital.setOnClickListener(mOnClickListener);
		rlPinyinOrderResultPrompt = (RelativeLayout) activity.findViewById(R.id.rlPinyinOrderResultPrompt);
		rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE);
		tvPinyinOrderSearchPrompt = (TextView) activity.findViewById(R.id.tvPinyinOrderSearchPrompt);
		tvPinyinOrderSearchPrompt.setVisibility(View.VISIBLE);
		tvPinyinNoDataPrompt = (TextView) activity.findViewById(R.id.tvPinyinNoDataPrompt);
		tvPinyinNoDataPrompt.setVisibility(View.INVISIBLE);
		progressBar = (ProgressBar) activity.findViewById(R.id.progressBar_pinyin_order);
		progressBar.setVisibility(View.INVISIBLE);
		tvPinyinOrderInputContent = (TextView) activity.findViewById(R.id.tvPinyinOrderInputContent);
		tvPinyinOrderSearchResultNum = (TextView) activity.findViewById(R.id.tvPinyinOrderSearchResultNum);
		tvPinyinOrderSearchContent = (TextView) activity.findViewById(R.id.tvPinyinOrderSearchContent);
		btPinyinOrderSeachBack = (Button) activity.findViewById(R.id.btPinyinOrderSeachBack);
		btPinyinOrderSeachBack.setOnClickListener(mOnClickListener);
		btPinyinOrderSeachClear = (Button) activity.findViewById(R.id.btPinyinOrderSeachClear);
		btPinyinOrderSeachClear.setOnClickListener(mOnClickListener);
		
		//Pinyin order prompt
		pb_pinyin_order_search_prompt = (ProgressBar) activity.findViewById(R.id.pb_pinyin_order_search_prompt);
		pb_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
		lvPinyinOrder = (ListView) activity.findViewById(R.id.lvPinyinOrder);
		letterView = (LetterView) activity.findViewById(R.id.letterView);
		letterView.setOnPressEnterListener(this);
		btPinyinOrderPreviousPage = (Button) activity.findViewById(R.id.btPinyinOrderPreviousPage);
		btPinyinOrderPreviousPage.setOnClickListener(mOnClickListener);
		btPinyinOrderNextPage = (Button) activity.findViewById(R.id.btPinyinOrderNextPage);
		btPinyinOrderNextPage.setOnClickListener(mOnClickListener);
		btPinyinOrderSeachGo = (Button) activity.findViewById(R.id.btPinyinOrderSeachGo);
		btPinyinOrderSeachGo.setOnClickListener(mOnClickListener);
		
		tvPinyinOrderPagePrompt = (TextView) activity.findViewById(R.id.tvPinyinOrderPagePrompt);
		lv_pinyin_order_search_prompt = (ListView)activity.findViewById(R.id.lv_pinyin_order_search_prompt);
		lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
		lv_pinyin_order_search_prompt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchPromptObject searchPromptObj = (SearchPromptObject) mSearchPromptAdapter.getItem(position);
				if(searchPromptObj != null) {
					btPinyinOrderSeachGo.requestFocus();
					hideHotKeywordView();
					loadSearchResultListFirstByGeneral(searchPromptObj.name);
					MobclickAgent.onEvent(activity, Constants.KS_UMENG_PINYIN_SEARCH_SUGGEST, Constants.KS_UMENG_SUCCESS);
				}
			}
		});
		
		mAdapter = new DetailAdapter(activity, ListLogic.MTV_LIST_BY_PINYIN_SEARCH, Constants.FROM_PINYIN);
		mHotKeywordListAdapter = new HotKeywordListAdapter(activity);
		mSearchPromptAdapter = new SearchPromptAdapter(activity);
		lvPinyinOrder.setAdapter(mAdapter);
		lv_pinyin_order_search_prompt.setAdapter(mSearchPromptAdapter);
		
		int[] buttons = {R.id.btMtvSing, R.id.btMtvAdd};
		new ListViewDecorator(lvPinyinOrder, buttons);
		showNoContentInputView();
	}
	
	private void loadingAnimationSetting() {
		if(Constants.isUseAnimation) {
			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
			swingBottomInAnimationAdapter.setAbsListView(lvPinyinOrder);
			lvPinyinOrder.setAdapter(swingBottomInAnimationAdapter);
		}else {
			lvPinyinOrder.setAdapter(mAdapter);
		}
	}
	
	private void loadSearchResultListByGeneral(int pageNum, String keyword) {
		tvPinyinOrderSearchContent.setText(keyword);
		tvPinyinOrderInputContent.setText("“"+keyword+"”");
		KuwoLog.d(LOG_TAG, "search content="+keyword);
		if(TextUtils.isEmpty(keyword)) {
			mAdapter.clearMtvList();
			showNoContentInputView();
		}else {
			tvPinyinOrderSearchResultNum.setText("正在查询");
			progressBar.setVisibility(View.VISIBLE);
			//hide网络请求失败layout
			llPinyinOrderRequestFail.setVisibility(View.INVISIBLE);
			mListLogic.getMtvListBySong(keyword, pageNum, new PinyinOrderResultDataHandler());
			showProgressBar();
		}
	}
	
	private void showNoContentInputView() {
		showNoData();
//		tvPinyinNoDataPrompt.setText("请输入拼音进行查询！");
		tvPinyinNoDataPrompt.setVisibility(View.INVISIBLE);
		showHotKeywordView();
	}
	
	private void showNoData() {
		rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE); 
		rlPinyinOrderPagerButton.setVisibility(View.INVISIBLE);
		lvPinyinOrder.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		llPinyinOrderRequestFail.setVisibility(View.INVISIBLE);
		tvPinyinNoDataPrompt.setVisibility(View.VISIBLE);
		tvPinyinNoDataPrompt.setText("亲，木有搜索到任何视频:(");
	}
	
	private void showRequestFail() {
		rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE);
		lvPinyinOrder.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		llPinyinOrderRequestFail.setVisibility(View.VISIBLE);
		btPinyinOrderSeachGo.setNextFocusDownId(R.id.btPinyinOrderRequestFail);
		btPinyinOrderSeachBack.setNextFocusDownId(R.id.btPinyinOrderRequestFail);
		btPinyinOrderSeachClear.setNextFocusDownId(R.id.btPinyinOrderRequestFail);
		btPinyinOrderRequestFail.requestFocus();
	}
	
	private void showListView() {
		llPinyinOrderRequestFail.setVisibility(View.INVISIBLE);
		tvPinyinNoDataPrompt.setVisibility(View.INVISIBLE);
		rlPinyinOrderResultPrompt.setVisibility(View.VISIBLE);
		lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
		lvPinyinOrder.setVisibility(View.VISIBLE);
		btPinyinOrderSeachGo.setNextFocusDownId(R.id.lvPinyinOrder);
		btPinyinOrderSeachBack.setNextFocusDownId(R.id.lvPinyinOrder);
		btPinyinOrderSeachClear.setNextFocusDownId(R.id.lvPinyinOrder);
	}
	
	private void showProgressBar() {
		tvPinyinNoDataPrompt.setVisibility(View.INVISIBLE);
	}
	
	private class PinyinOrderResultDataHandler extends PagedDataHandler<Mtv> { 

		@Override
		public void onSuccess(PagedData<Mtv> data) {
			MobclickAgent.onEvent(activity, Constants.KS_UMENG_SEARCH_MUSIC, Constants.KS_UMENG_SUCCESS); 
			mTotalPageNum = 1;
			if (data.total<=ListLogic.MTV_LIST_BY_PINYIN_SEARCH)
			{
				mTotalPageNum = 1;
			}
			else
			{
				mTotalPageNum = (data.total%ListLogic.MTV_LIST_BY_PINYIN_SEARCH == 0) ? data.total/ListLogic.MTV_LIST_BY_PINYIN_SEARCH : (data.total/ListLogic.MTV_LIST_BY_PINYIN_SEARCH+1);
			}
			mCurrentPageNum = data.page;
			loadingAnimationSetting();
			mAdapter.setMtvList(data.data);
			mAdapter.setCurrentPageNum(mCurrentPageNum);
			rlPinyinOrderResultPrompt.setVisibility(View.VISIBLE);
			rlPinyinOrderPagerButton.setVisibility(View.VISIBLE);
			tvPinyinOrderSearchResultNum.setText(data.total+"个");
			tvPinyinOrderPagePrompt.setText(mCurrentPageNum+" / "+mTotalPageNum);
			progressBar.setVisibility(View.INVISIBLE);
			showListView();
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			tvPinyinOrderPagePrompt.setText("获取失败");
			progressBar.setVisibility(View.INVISIBLE);
			showRequestFail();
		}
		
		@Override
		public void onFailure() {
			MobclickAgent.onEvent(activity, Constants.KS_UMENG_SEARCH_MUSIC, Constants.KS_UMENG_FAIL);
			tvPinyinOrderSearchResultNum.setText("0个");
			progressBar.setVisibility(View.INVISIBLE);
			showNoData();
		}
		
		@Override
		public void onStart() {
			progressBar.setVisibility(View.VISIBLE);
			//hide网络请求失败layout
			llPinyinOrderRequestFail.setVisibility(View.INVISIBLE);
		}
		
		@Override
		public void onFinish() {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
	
	private void loadVoiceControlDialog() {
		if(lv_pinyin_order_search_prompt.getVisibility() == View.VISIBLE) {
			lv_pinyin_order_search_prompt.setVisibility(View.GONE);
		}
		ASRDialog dialog = new ASRDialog(activity);
		dialog.show();
	}
	
	private View.OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_pinyin_order_voice_controller:
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_VOICE_SEARCH, Constants.KS_UMENG_SUCCESS);
				loadVoiceControlDialog();
				break;
			case R.id.btPinyinOrderSeachGo: // process logic for search button 
				lv_pinyin_order_search_prompt.setVisibility(View.GONE);
				if(TextUtils.isEmpty(tvPinyinOrderSearchContent.getText().toString())) 
					return;
				hideHotKeywordView();
//				loadSearchResultByPinyin();
				String keyword = tvPinyinOrderSearchContent.getText().toString();
				loadSearchResultListFirstByGeneral(keyword);
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_PINYIN_SEARCH_DIRECTGO, Constants.KS_UMENG_SUCCESS);
				break;
			case R.id.btPinyinOrderSeachBack: // process logic for back button
				int length = tvPinyinOrderSearchContent.getText().length();
				if(length >= 1) {
					tvPinyinOrderSearchContent.setText(tvPinyinOrderSearchContent.getText().subSequence(0, length-1));
					if(length >= 2) {
						loadSearchPrompt("");
					}else {//if search content is less than or equals 1, we should hide the search prompt 
						lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE); 
						btPinyinOrderSeachClear.performClick();
					}
				}
				break;
			case R.id.btPinyinOrderSeachClear: // p rocess logic for clear button
				if(lv_pinyin_order_search_prompt.getVisibility() == View.VISIBLE)
					lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
				tvPinyinOrderSearchContent.setText("");
				showHotKeywordView();
				break;
			case R.id.btPinyinOrderPreviousPage: // process logic for previous page button
				loadFirstPage = false;
				if(lvPinyinOrder.getVisibility() == View.VISIBLE && mCurrentPageNum > 1) {
					loadSearchResultListByGeneral(mCurrentPageNum-1, tvPinyinOrderSearchContent.getText().toString());
						
				}
				break;
			case R.id.btPinyinOrderNextPage: // process logic for next page button
				if(mCurrentPageNum < mTotalPageNum) {
					loadFirstPage = false;
					loadSearchResultListByGeneral(mCurrentPageNum+1, tvPinyinOrderSearchContent.getText().toString());
				}
				break;
			case R.id.btPinyinOrderDigital: //process logic for digital button
				showDigitalView(activity.findViewById(R.id.btPinyinOrderDigital));
				DialogUtils.toast("点击'返回键'或者‘关闭按钮'关闭数字键盘", true);
				break;
			default:
				break;
			}
		}
	};
	
	private View.OnClickListener mOnDigitalClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btDigitalClose:
				mDigitalPopupWindow.dismiss();
				break;
			case R.id.btDigitalViewOne:
				loadSearchPrompt("1");
				break;
			case R.id.btDigitalViewTwo:
				loadSearchPrompt("2");
				break;
			case R.id.btDigitalViewThree:
				loadSearchPrompt("3");
				break;
			case R.id.btDigitalViewFour:
				loadSearchPrompt("4");
				break;
			case R.id.btDigitalViewFive:
				loadSearchPrompt("5");
				break;
			case R.id.btDigitalViewSix:
				loadSearchPrompt("6");
				break;
			case R.id.btDigitalViewSeven:
				loadSearchPrompt("7");
				break;
			case R.id.btDigitalViewEight:
				loadSearchPrompt("8");
				break;
			case R.id.btDigitalViewNine:
				loadSearchPrompt("9");
				break;
			case R.id.btDigitalViewZero:
				loadSearchPrompt("0");
				break;

			default:
				break;
			}
		}
	};
	
	private void showHotKeywordView() {
		//请求服务端热门搜索关键词列表
		if(AppContext.getNetworkSensor().hasAvailableNetwork()) {
			mListLogic.getHotKeywordList(new HotKeywordDataHandler());
		}else {
			showDefaultHotKeywordList();
		}
	}
	
	private void showDefaultHotKeywordList() {
		List<String> defaultHotKeywordList = new ArrayList<String>();
		defaultHotKeywordList.add("伤不起");
		defaultHotKeywordList.add("因为爱情");
		defaultHotKeywordList.add("我是歌手");
		defaultHotKeywordList.add("凤凰传奇");
		defaultHotKeywordList.add("我的歌声里");
		defaultHotKeywordList.add("红尘情歌");
		defaultHotKeywordList.add("凤凰传奇");
		defaultHotKeywordList.add("刘德华");
		defaultHotKeywordList.add("范玮琪");
		defaultHotKeywordList.add("蔡依林");
		mHotKeywordListAdapter.setHotKeywordList(defaultHotKeywordList);
		gvPinyinOrderHotKeyword.setAdapter(mHotKeywordListAdapter);
		lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
		lvPinyinOrder.setVisibility(View.INVISIBLE);
		llPinyinOrderRequestFail.setVisibility(View.INVISIBLE);
		tvPinyinNoDataPrompt.setVisibility(View.INVISIBLE);
		rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE);
		rlPinyinOrderPagerButton.setVisibility(View.INVISIBLE);
		rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE);
		rlPinyinOrderPagerButton.setVisibility(View.INVISIBLE);
		rlPinyinOrderHotKeywordView.setVisibility(View.VISIBLE);
		
		btPinyinOrderSeachGo.setNextFocusDownId(R.id.gvPinyinOrderHotKeyword);
		btPinyinOrderSeachBack.setNextFocusDownId(R.id.gvPinyinOrderHotKeyword);
		btPinyinOrderSeachClear.setNextFocusDownId(R.id.gvPinyinOrderHotKeyword);
	}
	
	private void hideHotKeywordView() {
		rlPinyinOrderHotKeywordView.setVisibility(View.INVISIBLE);
	}
	
	private class HotKeywordDataHandler extends PagedDataHandler<String> {

		@Override
		public void onSuccess(PagedData<String> data) {
			MobclickAgent.onEvent(activity, Constants.KS_UMENG_SEARCH_MUSIC_PROMPT, Constants.KS_UMENG_SUCCESS); 
			if(data != null && data.data.size() > 0) {
				//set adapter
				mHotKeywordListAdapter.setHotKeywordList(data.data);
				gvPinyinOrderHotKeyword.setAdapter(mHotKeywordListAdapter);
				llPinyinOrderRequestFail.setVisibility(View.INVISIBLE);
				tvPinyinNoDataPrompt.setVisibility(View.INVISIBLE);
				lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
				lvPinyinOrder.setVisibility(View.INVISIBLE);
				rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE);
				rlPinyinOrderPagerButton.setVisibility(View.INVISIBLE);
				rlPinyinOrderHotKeywordView.setVisibility(View.VISIBLE);
				
				btPinyinOrderSeachGo.setNextFocusDownId(R.id.gvPinyinOrderHotKeyword);
				btPinyinOrderSeachBack.setNextFocusDownId(R.id.gvPinyinOrderHotKeyword);
				btPinyinOrderSeachClear.setNextFocusDownId(R.id.gvPinyinOrderHotKeyword);
				
			}
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			KuwoLog.e(LOG_TAG, "content: "+content);
			showDefaultHotKeywordList();
		}
		
		@Override
		public void onFailure() {
			super.onFailure();
			KuwoLog.e(LOG_TAG, "onFailure");
			showDefaultHotKeywordList();
		}
	} 
	
	private void showDigitalView(View parent) {
		if(!mDigitalPopupWindow.isShowing()) {
			mDigitalPopupWindow.showAsDropDown(parent, -250, 5);
		}
	}

	/**
	 * A-Z字母响应
	 */
	@Override
	public void onPressEnter(String str) {
		loadSearchPrompt(str);
	}
	
	private void loadSearchPrompt(String keyword) {
		if(AppContext.getNetworkSensor().hasAvailableNetwork()) {
			String content = tvPinyinOrderSearchContent.getText().toString();
			content = content + keyword;
			tvPinyinOrderSearchContent.setText(content);
			tvPinyinNoDataPrompt.setVisibility(View.INVISIBLE);
			rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE);
			lvPinyinOrder.setVisibility(View.INVISIBLE);
			rlPinyinOrderPagerButton.setVisibility(View.INVISIBLE);
			if(!TextUtils.isEmpty(content)) 
				mListLogic.getSearchPromptList(content, new SearchPromptDataHandler()); 
		}else {
			DialogUtils.toast("网络连接失败，请检查网络后再试", true);
		}
	}
	
	private class SearchPromptDataHandler extends PagedDataHandler<SearchPromptObject> {

		@Override
		public void onSuccess(PagedData<SearchPromptObject> data) {
			MobclickAgent.onEvent(activity, Constants.KS_UMENG_SEARCH_MUSIC_PROMPT, Constants.KS_UMENG_SUCCESS); 
			if(data != null && data.data.size() > 0) {
				mSearchPromptAdapter.setSearchPromptList(data.data);
				hideHotKeywordView();
				lv_pinyin_order_search_prompt.setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public void onFailure() {
			super.onFailure();
			lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
			
		}
		
		@Override
		public void onStart() {
			super.onStart();
			showSearchPromptViewOnStart();
		}
		
		@Override
		public void onFinish() {
			super.onFinish();
			showSearchPrompViewOnFinish();
		}
	}
	
	private void showSearchPromptViewOnStart() {
		lv_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
		pb_pinyin_order_search_prompt.setVisibility(View.VISIBLE);
	}
	
	private void showSearchPrompViewOnFinish() {
		rlPinyinOrderResultPrompt.setVisibility(View.INVISIBLE);
		lvPinyinOrder.setVisibility(View.INVISIBLE);
		rlPinyinOrderPagerButton.setVisibility(View.INVISIBLE);
		pb_pinyin_order_search_prompt.setVisibility(View.INVISIBLE);
		btPinyinOrderSeachGo.setNextFocusDownId(R.id.lv_pinyin_order_search_prompt);
		btPinyinOrderSeachBack.setNextFocusDownId(R.id.lv_pinyin_order_search_prompt);
		btPinyinOrderSeachClear.setNextFocusDownId(R.id.lv_pinyin_order_search_prompt);
	}
	
	/**
	 * 使用通用搜索
	 */
	private void loadSearchResultListFirstByGeneral(String keyword) {
		if(progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
			DialogUtils.toast("上一次请求尚未结束，请等待:(", false);
			return;
		}
		loadFirstPage = true;
		loadSearchResultListByGeneral(1, keyword); //默认加载第一页
	}
}
