/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.activity;

import android.os.Bundle;
import android.widget.TextView;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.view.fragment.SearchFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * @Package cn.kuwo.sing.phone4tv.view.activity
 * 
 * @Date Mar 12, 2014 10:55:17 AM
 *
 * @Author wangming
 *
 */
public class SearchActivity extends SherlockFragmentActivity {

	private static final String LOG_TAG = "SearchActivity";
	private SearchFragment searchFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		searchFragment = new SearchFragment();
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.flFragmentSearch, new SearchFragment())
		.commit();
	}
	
	public void setResultInfo(String s){
		TextView tv = (TextView) findViewById(R.id.tvHotWord);
		tv.setText(s);
	}
}
