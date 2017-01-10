/**
 * Copyright (c) 2013, Kuwo.cn, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.activity;

import android.os.Bundle;
import android.util.Log;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.commons.util.CommonUtils;
import cn.kuwo.sing.phone4tv.view.fragment.DetailFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * @Package cn.kuwo.sing.phone4tv.view.activity
 * 
 * @Date Mar 6, 2014 5:19:09 PM
 *
 * @Author wangming
 * 
 * @Parameters int pageFrom;</br>
 * 			ImageObject imageObject;
 *
 */
public class DetailActivity extends SherlockFragmentActivity {
	private static final String LOG_TAG = "DetailActivity";
	private static final int MENU_GROUP_ID = 0;
	private static final int MENU_ITEM_ID_SEARCH = 0;
	private static final String MENU_ITEM_TITLE_SEARCH = "search";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(LOG_TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
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
		setContentView(R.layout.activity_detail);
		
		CommonUtils.setActionBarColor(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	    
	    getSupportFragmentManager().beginTransaction()
	    .replace(R.id.flDetailContent, new DetailFragment())
	    .commit();
	}
}
