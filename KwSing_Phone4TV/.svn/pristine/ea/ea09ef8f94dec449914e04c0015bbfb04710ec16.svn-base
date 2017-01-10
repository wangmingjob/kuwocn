/**
 * Copyright (c) 2013, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.fragment;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @Package cn.kuwo.sing.phone4tv.view.fragment
 *
 * @Date 2014-3-5, 上午11:11:56
 *
 * @Author wangming
 *
 */
public class BaseFragment extends SherlockFragment {
	// if change the screen orientation
	private int mTitleId;
	
	public void setTitleId(int titleId) {
		mTitleId = titleId;
	}
	
	public int getTitleId() {
		return mTitleId;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null) {
			mTitleId = savedInstanceState.getInt("titleId");
		}
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("titleId", mTitleId);
	}
}
