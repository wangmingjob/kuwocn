/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.UserMtv;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.context.Config;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.utils.DialogUtils;
import de.greenrobot.event.EventBus;


/**
 * @Package cn.kuwo.sing.tv.logic
 *
 * @Date 2013-4-16, 上午11:14:01, 2013
 *
 * @Author wangming
 *
 */
public class OrderSerializeLogic {
	private static final String LOG_TAG = "OrderSerializeLogic";
	private static OrderSerializeLogic instance;
	private static LinkedList<Mtv> sOrders;
	//private LinkedList<Mtv> sCurrentPageMtvList;
	private int mCurrentMtvIndex = -1;  //从0开始
	private OnCurrentMtvChangedListener mOnCurrentMtvChangedListener;
	
	private OrderSerializeLogic() {
	    if(Config.getPersistence().orderLinkedList == null) {
	    	KuwoLog.d(LOG_TAG, "sOrders is null");
	    	Config.getPersistence().orderLinkedList = new LinkedList<Mtv>();
	    }
	    sOrders = Config.getPersistence().orderLinkedList;
	    
	    calcCurrentPageMtv(1);	//初始化第一页数据
	}
	
	public static OrderSerializeLogic getInstance() {
		if(instance == null) {
			instance = new OrderSerializeLogic();
		}
		return instance;
	}
	
	private void sendDataChangeMessage() {
		Message msg = new Message();
		msg.what = Constants.MSG_ORDERED_MTV_DATA_CHANGED;
		EventBus.getDefault().post(msg);
	}
	
	protected void onDataChanged() {
		persistanceOrderList();
		sendDataChangeMessage();
	}
	
	public void setOnCurrentMtvChangedListener(OnCurrentMtvChangedListener listener) {
		mOnCurrentMtvChangedListener = listener;
	}
	
	public interface OnCurrentMtvChangedListener {
		/**
		 * You should play the mtv.
		 */
		void onCurrentMtvChanged(Mtv mtv);
	}
	
	public int getCurrentMtvIndex() {
		return mCurrentMtvIndex;
	}
	
	public int getTotalPageNum(){
		return (sOrders.size()%Constants.ORDERD_MTV_PAGESIZE == 0) ? sOrders.size()/Constants.ORDERD_MTV_PAGESIZE : (sOrders.size()/Constants.ORDERD_MTV_PAGESIZE+1);
	}
	
	/**
	 * 从数据库中获取已点歌曲列表
	 * @return
	 */
	public List<Mtv> getOrderedMtvList() {
		return sOrders;
	}
	
	public List<Mtv> getOrderedMtvFixedSizeList(int Page){		
		return calcCurrentPageMtv(Page);
	}
	
	private void persistanceOrderList() {
		Config.savePersistence();
	}
	
	public void clearAllMtv() {
		sOrders.clear();
		onDataChanged();
	}
	
	/**
	 * 内部插入排序
	 * @param index
	 * @param mtv
	 */
	private void insertMtv(int index, Mtv mtv) {
		sOrders.add(index, mtv);
		onDataChanged();
	}
	
	/**
	 * 内部置顶，不用写入数据库
	 * @param mtv
	 */
	public int topMtv(Mtv mtv, int position) {
		if(mtv == null)
			return -1;
		if(mCurrentMtvIndex == -1 && position == 0) { //在未播放的场景下，置顶第一首歌
			DialogUtils.toast("当前歌曲，无需置顶", false);
			return -1;
		}
		if(mCurrentMtvIndex >= 0 && mCurrentMtvIndex < sOrders.size() && mCurrentMtvIndex == position) { //在未播放的场景下，置顶当前歌
			DialogUtils.toast("当前歌曲，无需置顶", false);
			return -1;
		}
		if(mCurrentMtvIndex >= 0 && mCurrentMtvIndex < sOrders.size()-1 && (mCurrentMtvIndex+1) == position) {
			DialogUtils.toast("已经处于下一首，无需置顶", false);
			return -1;
		}
		if (sOrders.size() == 0 || sOrders.size() < position +1 || !sOrders.get(position).rid.equals(mtv.rid)){
			DialogUtils.toast("列表已经过期", false);
			return -1;
		}
		
		deleteMtvWithoutEvent(position);
		insertMtv(mCurrentMtvIndex+1, mtv);
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append("成功置顶 ").append(mtv.name);
		sb.setSpan(new ForegroundColorSpan(Color.YELLOW), 4, 4+mtv.name.length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		DialogUtils.toast(sb, false);
		return 0;
	}
	
	/**
	 * 外部插入，要持久化
	 * @param mtv
	 */
	public void addMtv(Mtv mtv) { 
		sOrders.addLast(mtv);
		onDataChanged();
	}
	
	/**
	 * 已点列表删除，要持久化
	 * @param mtv
	 */
	public int deleteMtv(Mtv mtv, int position) {
		if(mtv == null)
			return -1;
		if(mCurrentMtvIndex >= 0 && mCurrentMtvIndex < sOrders.size() && mCurrentMtvIndex == position && MainApplication.isSingActivityAliving) {
			DialogUtils.toast("当前歌曲正在播放，请勿删除！", false);
			return -1;
		}
		
		if (sOrders.size() == 0 || sOrders.size() < position +1 || !sOrders.get(position).rid.equals(mtv.rid)){
			DialogUtils.toast("列表已经过期", false);
			return -1;
		}
		
		//deleteMtvWithoutEvent(mtv);
		deleteMtvWithoutEvent(position);
		onDataChanged();
		return 0;
	}
	
	private void deleteMtvWithoutEvent(int position)
	{
		if(position == -1 || position > (sOrders.size()-1))
			return;
		if (position < mCurrentMtvIndex)
			mCurrentMtvIndex--;
		sOrders.remove(position);
	}
	
	/**
	 * 外部演唱一首歌,要持久化
	 * 
	 * @param mtv
	 */
	public int singMtv(Application application, Mtv mtv, boolean fromOrderedMtvList, int position) {
		int currentMtvIndex = -1;
		if(fromOrderedMtvList) {
			//如果已点歌曲列表的歌曲为空，获取index大于歌曲数，或者当前点歌的内容与mtv本身的内容不符合，则返回
			if (sOrders.size()==0 || sOrders.size()<position+1 || !sOrders.get(position).rid.equals(mtv.rid)){
				DialogUtils.toast("列表已经过期", false);
				return -1;
			}			
			currentMtvIndex = position;
		}else {   
			currentMtvIndex = sOrders.size();
			sOrders.add(currentMtvIndex, mtv);
		}
		MainApplication app = (MainApplication) application;
		if(MainApplication.isSingActivityAliving) {
			Message stopSingControllerMsg = new Message();
			stopSingControllerMsg.what = Constants.MSG_RESTART_SING_CONTROLLER;
			EventBus.getDefault().post(stopSingControllerMsg);
		}
		MainApplication.isSingActivityAliving = true;
		setCurrentMtvIndex(currentMtvIndex);
		onDataChanged();
		return 0;
	} 
	
	public void setCurrentMtvIndex(int value) {
		mCurrentMtvIndex = value;
		if(value == -1 || value > (sOrders.size()-1))
			return;
		if (mOnCurrentMtvChangedListener != null) {
			Mtv mtv = sOrders.get(value);
			mOnCurrentMtvChangedListener.onCurrentMtvChanged(mtv);
		}
		
	}
	
	/**
	 * 切换到上一首歌
	 * @return
	 */
	public Mtv preMtv() {
		int index = mCurrentMtvIndex-1 == -1 ? sOrders.size()-1 : mCurrentMtvIndex-1;
		setCurrentMtvIndex(index);
		sendDataChangeMessage();
		return sOrders.get(index);
	}
	
	/**
	 * 切换到下一个MTV
	 * @return 下一个MTV
	 */
	public Mtv nextMtv() {
		int index = mCurrentMtvIndex+1 == sOrders.size() ? 0 : mCurrentMtvIndex+1;
		setCurrentMtvIndex(index);
		sendDataChangeMessage();
		return sOrders.get(index);
	}
	
	/**
	 * 获取下一个MTV，但不切换。
	 * @return
	 */
	public Mtv peekMtv() {
		int index = mCurrentMtvIndex+1 == sOrders.size() ? 0 : mCurrentMtvIndex+1;
		if (index >= sOrders.size())
			return null;
		return sOrders.get(index);
	}
	
	/*
	 * 删除重复的歌曲
	 * 
	 */
	public void deleteRepeatMtv()
	{
		if (sOrders.size() == 0)
			return;
		
		HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
		for (int i=0; i<sOrders.size(); i++)
		{
			Mtv tempMtv = sOrders.get(i);
			ArrayList<Integer> list  = (ArrayList<Integer>) map.get(tempMtv.rid);
			if (list==null)
			{
				list = new ArrayList<Integer>();
			}
			list.add(i);
			map.put(tempMtv.rid, list);
		}
		
		ArrayList<Integer> delList = new ArrayList<Integer>();
		Iterator<Entry<String, ArrayList<Integer>>> iterator=map.entrySet().iterator();    
		while(iterator.hasNext()){          
			Map.Entry<String, ArrayList<Integer>> entry= iterator.next();    
			ArrayList<Integer> list  = (ArrayList<Integer>) entry.getValue();
			if (list!=null)
			{
				boolean bCurIdxInList = false;//当前播放歌曲idx是否在这个列表里面
				for (int i=0; i<list.size(); i++)
				{
					int curIdx = (Integer) list.get(i);
					if (curIdx==mCurrentMtvIndex)
					{
						bCurIdxInList = true;
						break;
					}
				}	
				
				if (bCurIdxInList)
				{
					for (int i=0; i<list.size(); i++)
					{
						int curIdx = (Integer) list.get(i);
						if (curIdx==mCurrentMtvIndex)
							continue;
						delList.add((Integer) list.get(i));
					}					
				}
				else
				{
					for (int i=0; i<list.size(); i++)
					{
						if (i==0)
							continue;
						else
							delList.add((Integer) list.get(i));
					}
				}

			}
		}
		
		if (delList.size() == 0)
			return;
		
		Collections.sort(delList);		
		for (int i=0; i<delList.size(); i++)
		{
			int index = (Integer) delList.get(i)-i;//每删除一个mtv，链表中每个元素的index都需要减1
			if (index<mCurrentMtvIndex)
				mCurrentMtvIndex--;
			sOrders.remove(index);		
		}
		
		DialogUtils.toast("删除成功");
		
		onDataChanged();
	}
	
	/*
	*
	*根据当前页码计算当前应该显示的mtv list数据
	*
	*/
	private List<Mtv> calcCurrentPageMtv(int Page)
	{
		LinkedList<Mtv> sCurrentPageMtvList = new LinkedList<Mtv>();		
		if (sOrders == null)
			return null;
		
		sCurrentPageMtvList.clear();
		int index = (Page-1)*Constants.ORDERD_MTV_PAGESIZE;
		for(int i=index; i<sOrders.size() && i<index+Constants.ORDERD_MTV_PAGESIZE; i++)
		{
			sCurrentPageMtvList.add(sOrders.get(i));
		}
		
		return sCurrentPageMtvList;
	}
}