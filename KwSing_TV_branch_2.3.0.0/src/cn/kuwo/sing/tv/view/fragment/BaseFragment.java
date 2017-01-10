package cn.kuwo.sing.tv.view.fragment;

import java.util.ArrayList;

import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.controller.BaseController;
import de.greenrobot.event.EventBus;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;

public class BaseFragment extends Fragment {

	private ArrayList<BaseController> mControllers;

	public BaseFragment() {
		mControllers = new ArrayList<BaseController>();
	}

	public void loadController(BaseController ctrl) {
		mControllers.add(ctrl);
	}

	public void unloadController(BaseController ctrl) {
		mControllers.remove(ctrl);
	}

	/*
	 * 在这里加载Controller
	 */
	protected void onLoadController() {
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		onLoadController();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", "onCreate");
		for (BaseController observer : mControllers) {
			observer.onCreate(savedInstanceState);
		}
	}
	
	@Override
	public void onPause() {
		for (BaseController observer : mControllers) {
			observer.onPause();
		}
		super.onPause();
	}
	
	@Override
	public void onStart() {
		for (BaseController observer : mControllers) {
			observer.onStart();
		}
		super.onStart();
		
		MainApplication app = (MainApplication)getActivity().getApplication(); 
		app.init();
	}
	
	@Override
	public void onResume() {
		for (BaseController observer : mControllers) {
			observer.onResume();
		}
		super.onResume();
	}
	
	@Override
	public void onStop() {
		for (BaseController observer : mControllers) {
			observer.onStop();
		}
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		for (BaseController observer : mControllers) {
			observer.onDestroy();
		}
		super.onDestroy();
	}
}
