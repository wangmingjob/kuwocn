/**
 * Copyright (c) 2013, Kuwo.cn, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.bean.ImageObject;
import cn.kuwo.sing.phone4tv.commons.context.Constants;
import cn.kuwo.sing.phone4tv.commons.util.CommonUtils;
import cn.kuwo.sing.phone4tv.view.adapter.SingerLetterViewAdapter;
import cn.kuwo.sing.phone4tv.view.fragment.SingerFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.devspark.appmsg.AppMsg;
import com.slidinglayer.SlidingLayer;

/**
 * @Package cn.kuwo.sing.phone4tv.view.activity
 * 
 * @Date Mar 7, 2014 11:27:02 AM
 *
 * @Author wangming
 * 
 * @Parameters ImageObject imageObject;
 *
 */
public class SingerActivity extends SherlockFragmentActivity {
	private static final String LOG_TAG = "SingerActivity";
	private SlidingLayer slidingLayerSinger;
	private EditText etSingerSearchContent;
	private Button btSingerSearchBackspace;
	private GridView gvSingerLetterView;
	private SingerFragment mSingerFragment;
	private ImageObject mCurrentImageObject;
	private Button btSingerLetterViewBackspace;
	private Button btSingerLetterViewClear;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//点击两次返回键退出应用
		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if(slidingLayerSinger.isOpened()) {
				slidingLayerSinger.closeLayer(true);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Constants.MENU_GROUP_ID, Constants.MENU_ITEM_ID_SEARCH, Menu.NONE,Constants.MENU_ITEM_TITLE_SEARCH)
		.setIcon(R.drawable.bt_search_selector)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); 
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Constants.MENU_ITEM_ID_SEARCH:
		// switch the app theme
			if (slidingLayerSinger.isOpened())
				slidingLayerSinger.closeLayer(true);
			else
				slidingLayerSinger.openLayer(true);
			break;
		case android.R.id.home:
			finish();
			overridePendingTransition(android.R.anim.fade_in, R.anim.push_right_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_singer);
		
		CommonUtils.setActionBarColor(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mSingerFragment = new SingerFragment();
		ft.replace(R.id.flSinger, mSingerFragment);
		ft.commit();
		
		Intent data = getIntent();
		mCurrentImageObject = (ImageObject) data.getSerializableExtra("imageObject");
		
		slidingLayerSinger = (SlidingLayer)findViewById(R.id.slidingLayerSinger);
		etSingerSearchContent = (EditText)findViewById(R.id.etSingerSearchContent);
		etSingerSearchContent.setEnabled(false);
		gvSingerLetterView = (GridView)findViewById(R.id.gvSingerLetterView);
		btSingerLetterViewBackspace = (Button)findViewById(R.id.btSingerLetterViewBackspace);
		btSingerLetterViewBackspace.setOnClickListener(mOnClickListener);
		btSingerLetterViewClear = (Button)findViewById(R.id.btSingerLetterViewClear);
		btSingerLetterViewClear.setOnClickListener(mOnClickListener);
		final SingerLetterViewAdapter singerLetterViewAdapter = new SingerLetterViewAdapter(this);
		gvSingerLetterView.setAdapter(singerLetterViewAdapter);
		gvSingerLetterView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sendRequestSingerList((String)singerLetterViewAdapter.getItem(position));
			}
		});
	}
	
	private View.OnClickListener mOnClickListener =  new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btSingerLetterViewBackspace:
				String searchContent = etSingerSearchContent.getText().toString();
				if(!TextUtils.isEmpty(searchContent)) {
					if(searchContent.length() > 1) {
						String resultContent = searchContent.substring(0, searchContent.length()-1);
						etSingerSearchContent.setText(resultContent);
					}else {
						// == 1
						etSingerSearchContent.setText("");
						turnBack();
					}
				}else {
					AppMsg.makeText(SingerActivity.this, "请先输入歌手名", AppMsg.STYLE_INFO)
					.setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
					.setLayoutGravity(Gravity.TOP)
					.show();
				}
				turnBack();
				break;
			case R.id.btSingerLetterViewClear:
				etSingerSearchContent.setText("");
				turnBack();
				break;

			default:
				break;
			}
			
		}
	};
	
	private void turnBack() {
		if(mSingerFragment != null)
			mSingerFragment.turnBack();
	}
	
	private void sendRequestSingerList(String keyword) {
		String content = etSingerSearchContent.getText().toString();
		etSingerSearchContent.setText(content + keyword);
		if(mSingerFragment != null)
			mSingerFragment.loadSingerListBySearch(etSingerSearchContent.getText().toString());
	}
}
