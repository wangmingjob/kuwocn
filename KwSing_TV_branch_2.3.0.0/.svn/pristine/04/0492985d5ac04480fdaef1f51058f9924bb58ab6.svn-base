/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.SearchPromptObject;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2013-8-8, 下午6:19:20
 *
 * @Author wangming
 *
 */
public class SearchPromptAdapter extends BaseAdapter {
	private Context mContext;
	private List<SearchPromptObject> mSearchPromptObjectList;
	
	public SearchPromptAdapter(Context context) {
		mContext = context;
		mSearchPromptObjectList = new ArrayList<SearchPromptObject>();
	}
	
	public void setSearchPromptList(List<SearchPromptObject> searchPromptObjectList) {
		mSearchPromptObjectList = searchPromptObjectList;
		notifyDataSetChanged();
	}
	
	public void clearSearchPromptList() {
		mSearchPromptObjectList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mSearchPromptObjectList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSearchPromptObjectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		if(convertView == null) {
			view = View.inflate(mContext, R.layout.search_prompt_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_search_prompt_name = (TextView) view.findViewById(R.id.tv_search_prompt_name);
			viewHolder.tv_search_prompt_hot = (TextView) view.findViewById(R.id.tv_search_prompt_hot);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		SearchPromptObject obj = mSearchPromptObjectList.get(position);
		viewHolder.tv_search_prompt_name.setText(obj.name);
		viewHolder.tv_search_prompt_hot.setText(obj.hot);
		return view;
	}
	
	private static class ViewHolder {
		TextView tv_search_prompt_name;
		TextView tv_search_prompt_hot;
	}
}
