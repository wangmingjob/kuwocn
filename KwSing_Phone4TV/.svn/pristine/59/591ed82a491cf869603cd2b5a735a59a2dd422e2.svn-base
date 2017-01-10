/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.view.fragment.BaseProgressFragment;
import cn.kuwo.sing.phone4tv.view.fragment.MtvRecommendFragment;
import cn.kuwo.sing.phone4tv.view.fragment.MtvCategoryFragment;
import cn.kuwo.sing.phone4tv.view.fragment.SingerCategoryFragment;

import com.jazzyview.jazzyviewpager.JazzyViewPager;

/**
 * @Package cn.kuwo.sing.phone4tv.view.adapter
 *
 * @Date 2014-3-5, 下午5:17:42
 *
 * @Author wangming
 *
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
	private String[] mTitles = null;
	private JazzyViewPager mViewPager;

	public MainFragmentPagerAdapter(FragmentManager fm, String[] titles, JazzyViewPager viewPager) {
		super(fm);
		mTitles = titles;
		mViewPager = viewPager;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Object obj = super.instantiateItem(container, position);
		mViewPager.setObjectForPosition(obj, position);
		return obj;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}
	
	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public Fragment getItem(int position) {
		return createFragment(position);
	}
	
	private Fragment createFragment(int position) {
		BaseProgressFragment fragment = null;
		switch (position) {
		case 0:
			fragment = new MtvRecommendFragment(); //热门推荐
			fragment.setTitleId(R.string.fragment_title_mtvcategory);
			break;
		case 1:
			fragment = new MtvCategoryFragment(); // 分类点歌
			fragment.setTitleId(R.string.fragment_title_mtvcategoryorder);
			break;
		case 2: 
			fragment = new SingerCategoryFragment(); //歌手点歌
			fragment.setTitleId(R.string.fragment_title_singercategory);
			break;
		default:
			break;
		}
		return fragment;
	}
}
