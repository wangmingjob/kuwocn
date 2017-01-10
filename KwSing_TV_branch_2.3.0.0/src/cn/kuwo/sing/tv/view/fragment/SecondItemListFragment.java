/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.view.activity.ItemListActivity;
import cn.kuwo.sing.tv.view.activity.SecondItemListActivity;
import cn.kuwo.sing.tv.view.adapter.SingerLetterViewAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;


/**
 * @Package cn.kuwo.sing.tv.view.fragment
 *
 * @Date 2013-3-27, 下午12:16:02, 2013
 *
 * @Author wangming
 *
 */
public class SecondItemListFragment extends BaseFragment {
	private DisplayImageOptions mOptions;
	private ImageObject mCurrentImageObject;
	private EventBus mEventBus;
	private TextView tvListItemActivatedNumber;
	private Intent mIntent;
	private ImageObject mParentImageObject;
	private TextView tvOtherName;
	private PopupWindow mDigitalPopupWindow;
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
	private Button btItemSecondBack;
	private GridView gvSingerLetterView;
	
	public SecondItemListFragment() {
		mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.resetViewBeforeLoading()
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888)  // default
		.displayer(new SimpleBitmapDisplayer())
		.build();
		
		mEventBus = EventBus.getDefault();
		mEventBus.register(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mEventBus.unregister(this);
	}
	
	public void onEvent(Message msg) {
		if(msg.what == Constants.MSG_SONG_COUNT) {
			int songCount = msg.arg1;
			tvListItemActivatedNumber.setText("共"+songCount+"首歌");
		}else if(msg.what == Constants.MSG_SINGER_COUNT) {
			int singerCount = msg.arg1;
			SecondItemListActivity.sSingerCount = msg.arg1;
			tvListItemActivatedNumber.setText("共"+singerCount+"个歌手");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_list_activated_second_fragment, container, false);
		gvSingerLetterView = (GridView)view.findViewById(R.id.gvSingerLetterView);
		btItemSecondBack = (Button)view.findViewById(R.id.btItemSecondBack);	
		btItemSecondBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showForwardPage();
			}
		});
		final Intent intent = getActivity().getIntent();
		mCurrentImageObject = (ImageObject) intent.getSerializableExtra("currentImageObject");
		boolean needSearch = intent.getBooleanExtra("needSearch", false);
		if(needSearch) {
			btItemSecondBack.setNextFocusDownId(gvSingerLetterView.getId());
			gvSingerLetterView.setVisibility(View.VISIBLE);
			final SingerLetterViewAdapter singerLetterViewAdapter = new SingerLetterViewAdapter(getActivity());
			gvSingerLetterView.setAdapter(singerLetterViewAdapter);
			gvSingerLetterView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(position == singerLetterViewAdapter.getCount()-1) {
						//clear
						if(tvOtherName.getText().toString().equals(mCurrentImageObject.name)) {
							DialogUtils.toast("请先输入关键字", false);					
						}
						turnBack();
					}else if(position == singerLetterViewAdapter.getCount()-2) {
						//back
						String singerName4Back = tvOtherName.getText().toString();
						if(singerName4Back.equals(mCurrentImageObject.name)) {
							DialogUtils.toast("请先输入关键字", false);
							return;
						}
						int length = singerName4Back.length();
						if(length > 1) {
							tvOtherName.setText(singerName4Back.substring(0, length-1).toString());
							sendRequestSingerList("");
						}
						if(length == 1)
							turnBack();
					}else if(position == singerLetterViewAdapter.getCount()-3) {
						//#
						DialogUtils.toast("点击'返回键'或者‘关闭按钮'关闭数字键盘", true);
						showDigitalView(gvSingerLetterView);
					}else {
						sendRequestSingerList((String)singerLetterViewAdapter.getItem(position));
					}
				}
			});
			
		}else {
			gvSingerLetterView.setVisibility(View.GONE);
			btItemSecondBack.setNextFocusDownId(btItemSecondBack.getId());
		}
		
		ImageView ivOther = (ImageView) view.findViewById(R.id.ivListItemActivatedOther);
		tvOtherName = (TextView) view.findViewById(R.id.tvListItemActivatedOtherName);
		tvListItemActivatedNumber = (TextView) view.findViewById(R.id.tvListItemActivatedNumber);
		ImageLoader.getInstance().displayImage(mCurrentImageObject.pic, ivOther, mOptions);
		tvOtherName.setText(mCurrentImageObject.name);
		
		digitalView = View.inflate(getActivity(), R.layout.digital_view, null);
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
		
		mDigitalPopupWindow = new PopupWindow(digitalView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mDigitalPopupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		mDigitalPopupWindow.setOutsideTouchable(true);
		mDigitalPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mDigitalPopupWindow.update();
		mDigitalPopupWindow.setTouchable(true);
		mDigitalPopupWindow.setFocusable(true);
		return view;
	}
	
	private void showForwardPage() {
		Intent backIntent = null;
		mIntent = getActivity().getIntent();
		if(!mIntent.getStringExtra("flag").equals("fromSinger") ) {
			backIntent = new Intent(getActivity(), ItemListActivity.class);
			startActivity(backIntent);
		}else {
			Message msg = new Message();
			msg.what = Constants.MSG_SINGER_COUNT;
			msg.arg1 = SecondItemListActivity.sSingerCount;
			EventBus.getDefault().post(msg);
		}
		getActivity().finish();
		getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	private void postMessage(int what, String keyword) {
		Message msg = new Message();
		msg.what = what;
		if(keyword != null)
			msg.obj = keyword;
		EventBus.getDefault().post(msg);
	}
	
	private void sendRequestSingerList(String keyword) {
		String content = tvOtherName.getText().toString();
		if(content.contains(mCurrentImageObject.name))
			content = content.substring(mCurrentImageObject.name.length());
		tvOtherName.setText(content + keyword);
		postMessage(Constants.MSG_SINGER_SEARCH_KEYWORD, tvOtherName.getText().toString());
	}
	
	private void turnBack() {
		tvOtherName.setText(mCurrentImageObject.name);
		postMessage(Constants.MSG_SINGER_SEARCH_CLEAR, null);
	}
	
	private void showDigitalView(View parent) {
		if(!mDigitalPopupWindow.isShowing()) {
			mDigitalPopupWindow.showAsDropDown(parent, -250, 2);
		}
	}
	
	private View.OnClickListener mOnDigitalClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btDigitalClose:
				mDigitalPopupWindow.dismiss();
				break;
			case R.id.btDigitalViewOne:
				sendRequestSingerList("1");
				break;
			case R.id.btDigitalViewTwo:
				sendRequestSingerList("2");
				break;
			case R.id.btDigitalViewThree:
				sendRequestSingerList("3");
				break;
			case R.id.btDigitalViewFour:
				sendRequestSingerList("4");
				break;
			case R.id.btDigitalViewFive:
				sendRequestSingerList("5");
				break;
			case R.id.btDigitalViewSix:
				sendRequestSingerList("6");
				break;
			case R.id.btDigitalViewSeven:
				sendRequestSingerList("7");
				break;
			case R.id.btDigitalViewEight:
				sendRequestSingerList("8");
				break;
			case R.id.btDigitalViewNine:
				sendRequestSingerList("9");
				break;
			case R.id.btDigitalViewZero:
				sendRequestSingerList("0");
				break;

			default:
				break;
			}
		}
	};
}
