package cn.kuwo.sing.tv.logic;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class DefaultAsyncHttpResponseHandler extends AsyncHttpResponseHandler{

	private static final String TAG = "DefaultAsyncHttpResponseHandler";
	private AsyncHttpResponseHandler mHandler;
	
	public DefaultAsyncHttpResponseHandler(AsyncHttpResponseHandler handler) {
		mHandler = handler;
	}

	@Override
	public void onFailure(Throwable t, String content) {
		mHandler.onFailure(t, content);
		super.onFailure(t, content);
	}
	
	@Override
	public void onStart() {
		mHandler.onStart();
		super.onStart();
	}
	
	@Override
	public void onFinish() {
		mHandler.onFinish();
		super.onFinish();
	}
	
	
}
