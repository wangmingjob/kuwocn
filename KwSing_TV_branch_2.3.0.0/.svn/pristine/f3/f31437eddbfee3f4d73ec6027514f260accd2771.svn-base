/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.List;

import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.controller.SingerCategoryController.AnimateFirstDisplayListener;
import cn.kuwo.sing.tv.utils.DensityUtils;
import cn.kuwo.sing.tv.utils.DialogUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2013-3-26, 下午2:57:35, 2013
 *
 * @Author wangming
 *
 */
public class SingerCategoryAdapter extends BaseAdapter {
	private static final String LOG_TAG = "SingerCategoryAdapter";
	private List<ImageObject> mImageObjectList;
	private Activity mActivity;
	private DisplayImageOptions mOptions;
	private ImageLoadingListener mAnimateFirstListener;
	
	public SingerCategoryAdapter(Activity activity, List<ImageObject> imageObjectList, AnimateFirstDisplayListener animateFirstDisplayListener) {
		mActivity = activity;
		mImageObjectList = imageObjectList;
		mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.resetViewBeforeLoading()
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
		.displayer(new SimpleBitmapDisplayer())
		.build();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		if(convertView == null) {
			view = View.inflate(mActivity, R.layout.list_image_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivSingerCategoryItem = (ImageView) view.findViewById(R.id.ivListImageItem);
			viewHolder.tvSingerCategoryItemName = (TextView) view.findViewById(R.id.tvListImageItem);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		ImageObject imageObject = mImageObjectList.get(position);
		ImageLoader.getInstance().displayImage(imageObject.pic, viewHolder.ivSingerCategoryItem, mOptions, mAnimateFirstListener);
		viewHolder.tvSingerCategoryItemName.setText(imageObject.name);
		return view;
	}
	
	private static class ViewHolder {
		ImageView ivSingerCategoryItem;
		TextView tvSingerCategoryItemName;
	}

}
