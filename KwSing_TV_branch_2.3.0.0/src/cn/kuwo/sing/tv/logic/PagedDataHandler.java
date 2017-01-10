package cn.kuwo.sing.tv.logic;

import cn.kuwo.sing.tv.bean.PagedData;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class PagedDataHandler<T> extends AsyncHttpResponseHandler{
	
	public abstract void onSuccess(PagedData<T> data);
		
	
	public void onFailure( ){
		
	}
}
