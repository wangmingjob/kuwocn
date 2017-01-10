/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.bean.ImageObject;
import cn.kuwo.sing.phone4tv.commons.util.CommonUtils.AnimateFirstDisplayListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2013-3-25, 下午2:01:37, 2013
 *
 * @Author wangming
 *
 */
public class ImageObjectAdapter extends BaseAdapter {
	private static final String LOG_TAG = "MtvCategoryAdapter";
	private Activity mActivity;
	private DisplayImageOptions mOptions;
	private ImageLoadingListener mAnimateFirstListener;
	private int mCurrentPageNum;
	private List<ImageObject> mImageObjectList = new ArrayList<ImageObject>();
	
	public ImageObjectAdapter(Activity activity, AnimateFirstDisplayListener animateFirstDisplayListener) {
		mActivity = activity;
		mAnimateFirstListener = animateFirstDisplayListener;
		mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.resetViewBeforeLoading()
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
		.displayer(new SimpleBitmapDisplayer())
		.build();
	}
	
	public void setImageObjectData(int currentPageNum, List<ImageObject> imageObjectList) {
		mCurrentPageNum = currentPageNum;
		mImageObjectList.addAll(imageObjectList);
		notifyDataSetChanged();
	}
	
	public void clearImageObjectList() {
		mImageObjectList.clear();
	}

	@Override
	public int getCount() {
		return mImageObjectList.size();
	}

	@Override
	public Object getItem(int position) {
		return mImageObjectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		if(convertView == null) {
			view = View.inflate(mActivity, R.layout.item_list_image, null);
			viewHolder = new ViewHolder();
			viewHolder.ivMtvCategoryItem = (ImageView) view.findViewById(R.id.ivListImageItem);
			viewHolder.tvMtvCategoryItemName = (TextView) view.findViewById(R.id.tvListImageItem);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		ImageObject imageObject = mImageObjectList.get(position);
		ImageLoader.getInstance().displayImage(imageObject.pic, viewHolder.ivMtvCategoryItem, mOptions, mAnimateFirstListener);
		viewHolder.tvMtvCategoryItemName.setText(imageObject.name);
		return view;
	}
	
	private static class ViewHolder {
		ImageView ivMtvCategoryItem;
		TextView tvMtvCategoryItemName;
	}

}
