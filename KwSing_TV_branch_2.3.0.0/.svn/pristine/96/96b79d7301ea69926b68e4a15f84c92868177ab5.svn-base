package cn.kuwo.sing.tv.view.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.UserMtv;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic;
import cn.kuwo.sing.tv.socket.MessageCommons;
import cn.kuwo.sing.tv.socket.NioServerService;
import cn.kuwo.sing.tv.socket.NioServerService.NioServerBinder;
import cn.kuwo.sing.tv.utils.BitmapTools;
import cn.kuwo.sing.tv.utils.ChangeLog;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.utils.ExitDialog;
import cn.kuwo.sing.tv.utils.NetworkChangeReceiver;
import cn.kuwo.sing.tv.utils.QrCode4PhoneDialog;
import cn.kuwo.sing.tv.view.fragment.ItemListFragment;
import cn.kuwo.sing.tv.view.fragment.MoreFragment;
import cn.kuwo.sing.tv.view.fragment.MtvRecommendFragment;
import cn.kuwo.sing.tv.view.fragment.MtvCategoryFragment;
import cn.kuwo.sing.tv.view.fragment.OrderedMtvFragment;
import cn.kuwo.sing.tv.view.fragment.PinyinOrderFragment;
import cn.kuwo.sing.tv.view.fragment.SingerCategoryFragment;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ItemListActivity extends BaseFragmentActivity implements ItemListFragment.Callbacks {
	private static final String LOG_TAG = "ItemListActivity";
	private static final String ORDER_MTV = "OrderedMtv";
	private static final String PINYIN_ORDER = "PinyinOrder";
	private static final String MTV_CATEGOR = "MtvCategory";
	private static final String MTV_CATEGORY_ORDER = "MtvCategoryOrder";
	private static final String SINGER_CATEGORY = "SingerCategory";
	private static final String MORE = "More";

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private Bitmap mBackgroundBitmap;
	
	private HomeReceiver receiverHome = new HomeReceiver();
	private IntentFilter filterHome = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list_activity);
		
		FrameLayout container = (FrameLayout) findViewById(R.id.item_detail_container);
		if (container != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
			
			if (Constants.isUseBackground)
			{
				mBackgroundBitmap = BitmapTools.createBitmapByInputstream(this, R.drawable.item_list_right_bg_first, (int)(AppContext.SCREEN_WIDTH * (1660 *1.0f/1920)), AppContext.SCREEN_HIGHT);
				container.setBackgroundDrawable(new BitmapDrawable(mBackgroundBitmap));				
			}else {
				container.setBackgroundColor(this.getResources().getColor(R.color.item_list_right_color));
			}
			
			ItemListFragment itemListFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list);
			// Restore the previously serialized activated item position.
			itemListFragment.setActivateOnItemClick(2); //默认为2
		}
		// TODO: If exposing deep links into your app, handle intents here.
		registerReceiver(receiverHome, filterHome);
		
		
		//判断网络，无网络弹出提示框
		if(!AppContext.getNetworkSensor().hasAvailableNetwork()) {
			showNoNetworkDialog();
		}
//		ChangeLog changeLog = new ChangeLog(this, ChangeLog.DEFAULT_SMALL_CSS);
//		if(changeLog.isFirstRun()) {
//			AlertDialog dialog = changeLog.getLogDialog();
//			dialog.show();
//			dialog.getButton(DialogInterface.BUTTON_POSITIVE).requestFocus();
//		}
		boolean loadFirst = PreferencesManager.getBoolean("loadFirst", true);
		if(loadFirst) {
			QrCode4PhoneDialog qrCode4PhoneDialog = new QrCode4PhoneDialog(this);
			qrCode4PhoneDialog.show();
		}
		PreferencesManager.put("loadFirst", false).commit();
//		Intent intent = new Intent();
//		intent.setClassName(packageName, className);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		bindService(new Intent(this, NioServerService.class), conn, Context.BIND_AUTO_CREATE);
	}
	
	private ServiceConnection conn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        	NioServerBinder binder = (NioServerBinder)service;
            NioServerService bindService = binder.getService();
//            bindService.MyMethod();
        }
    };
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	private void showNoNetworkDialog() {
		try {
			final AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.show();
			dialog.getWindow().setContentView(R.layout.custom_alert_dialog);
			String exitPromptText = "您的TV当前没连网络";
			TextView tvPlayControllerExitPrompt = (TextView) dialog.getWindow().findViewById(R.id.tvPlayControllerExitPrompt);
			tvPlayControllerExitPrompt.setText(exitPromptText);
			dialog.getWindow().findViewById(R.id.btPlayControllerExitOk).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.getWindow().findViewById(R.id.btPlayControllerExitCancel).setVisibility(View.GONE);
		}catch(Exception e) {
			KuwoLog.e(LOG_TAG, "play mtv, show exit dialog error");
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(!MainApplication.isSingActivityAliving) {
				try {
					ExitDialog dlgExit = new ExitDialog(this);
					dlgExit.show();
				}
				catch(Exception e) {
					KuwoLog.printStackTrace(e);
				}
			}else {
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				startActivity(new Intent(ItemListActivity.this, PlayActivity.class));
				Message stopSingControllerMsg = new Message();
				stopSingControllerMsg.what = Constants.MSG_RESTART_SING_CONTROLLER;
				EventBus.getDefault().post(stopSingControllerMsg);
				return true;
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) { 
		ImageLoader.getInstance().clearMemoryCache();
		int itemId = Integer.parseInt(id);
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment orderedMtvFragment = fm.findFragmentByTag(ORDER_MTV);
			Fragment pinyinOrderFragment = fm.findFragmentByTag(PINYIN_ORDER);
			Fragment mtvCategoryFragment = fm.findFragmentByTag(MTV_CATEGOR);
			Fragment mtvCategoryOrderFragment = fm.findFragmentByTag(MTV_CATEGORY_ORDER);
			Fragment singerCategoryFragment = fm.findFragmentByTag(SINGER_CATEGORY);
			Fragment moreFragment = fm.findFragmentByTag(MORE);
			
			if(orderedMtvFragment !=  null)
				ft.hide(orderedMtvFragment);
			if(pinyinOrderFragment != null)
				ft.hide(pinyinOrderFragment);
			if(mtvCategoryFragment != null)
				ft.hide(mtvCategoryFragment);
			if(mtvCategoryOrderFragment != null)
				ft.hide(mtvCategoryOrderFragment);
			if(singerCategoryFragment != null)
				ft.hide(singerCategoryFragment);
			if(moreFragment != null)
				ft.hide(moreFragment);
			
			switch (itemId) {
			case 0:
				if(orderedMtvFragment !=  null) {
					ft.show(orderedMtvFragment);
				}else {
					OrderedMtvFragment orderedFragment = new OrderedMtvFragment();
					ft.add(R.id.item_detail_container, orderedFragment, ORDER_MTV);
				}
				break;
			case 1:
				if(pinyinOrderFragment != null) 
					ft.show(pinyinOrderFragment);
				else 
					ft.add(R.id.item_detail_container, new PinyinOrderFragment(), PINYIN_ORDER);
				break;
			case 2:
				if(mtvCategoryFragment != null) 
					ft.show(mtvCategoryFragment);
				else 
					ft.add(R.id.item_detail_container, new MtvRecommendFragment(), MTV_CATEGOR);
				break;
			case 3:
				if(mtvCategoryOrderFragment != null) 
					ft.show(mtvCategoryOrderFragment);
				else 
					ft.add(R.id.item_detail_container, new MtvCategoryFragment(), MTV_CATEGORY_ORDER);
				break;
			case 4:
				if(singerCategoryFragment != null) 
					ft.show(singerCategoryFragment);
				else 
					ft.add(R.id.item_detail_container, new SingerCategoryFragment(), SINGER_CATEGORY);
				break;
			case 5:
				if(moreFragment != null)
					ft.show(moreFragment);
				else 
					ft.add(R.id.item_detail_container, new MoreFragment(), MORE);
				break;
			default:
				break;
			}
			ft.commit();
		} 
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		NetworkChangeReceiver.isOpenNetworkToast = true;
	}
	
	@Override
	protected void onDestroy() {
		if(Build.HOST.equalsIgnoreCase("letv")) {
			AudioManager audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setParameters("karaoke_dev_init=0;");
		}
		unregisterReceiver(receiverHome);
		if (Constants.isUseBackground)
		{
			if (mBackgroundBitmap != null && !mBackgroundBitmap.isRecycled())
			{
				mBackgroundBitmap.recycle();
				mBackgroundBitmap = null;
				System.gc();
			}	
		}
		unbindService(conn);
		super.onDestroy();
	}
	
	public class HomeReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
			{
				String reason = intent.getStringExtra("reason");
				if(!TextUtils.isEmpty(reason) && reason.equals("homekey")) {
					Message msg = new Message();
					msg.what = Constants.MSG_CLOSE_PLAY_ACTIVITY_OR_PLAY_USER_ACTIVITY_WHEN_CLICK_HOME;
					EventBus.getDefault().post(msg);
				}
			}
		}
	};
	
	protected void onEvent(Message msg) {
		switch (msg.what) {
		case MessageCommons.RESULT_CONNECT_SUCCESS: //本地处理
			Object[] objArray_success = (Object[]) msg.obj;
			String clientAddress = (String)objArray_success[1];
			SpannableStringBuilder sb = new SpannableStringBuilder();
			sb.append("成功连接  ").append(clientAddress.substring(1));
			sb.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 5+clientAddress.substring(1).length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			DialogUtils.toast(sb, false);
			break;
		case MessageCommons.CMD_ADD: //本地处理
			Object[] objArray_add = (Object[]) msg.obj;
			cmdAdd((Mtv)objArray_add[1]);  
			break;
		case MessageCommons.CMD_PLAY:
			Object[] objArray_play = (Object[]) msg.obj;
			Object dataObj = objArray_play[1];
			Object userDataObj = objArray_play[2];
			if(dataObj != null) {
				performCmdPlay((Mtv)dataObj, -1);
			}else if(userDataObj != null) {
				performCmdPlayUser((UserMtv)userDataObj);
			}
			break;
		case MessageCommons.CMD_ORDERED_MTV:
			Object[] objectArray_ordered = (Object[]) msg.obj;
			//String  pageNumStr = (String) objectArray_ordered[3];
			//List<Mtv> orderedMtvList = OrderSerializeLogic.getInstance().getOrderedMtvFixedSizeList(Integer.valueOf(pageNumStr));
			List<Mtv> orderedMtvList = OrderSerializeLogic.getInstance().getOrderedMtvList();
			Message writeOrderedMsg = new Message();
			writeOrderedMsg.what = MessageCommons.CMD_ORDERED_MTV_WRITE;
			Object[] objArray_ordered = new Object[]{objectArray_ordered[0], orderedMtvList};
			writeOrderedMsg.obj = objArray_ordered;
			EventBus.getDefault().post(writeOrderedMsg);
			break;
		case MessageCommons.CMD_ORDERED_PLAY:
			Object[] objArray_orderedPlay = (Object[]) msg.obj;
			Object mtvPlayObj = objArray_orderedPlay[1];
			String indexPlayStr = (String) objArray_orderedPlay[3];
			if(mtvPlayObj != null) {
				performCmdPlay((Mtv)mtvPlayObj, Integer.valueOf(indexPlayStr));
				
			}
			break;
		case MessageCommons.CMD_ORDERED_TOP:
			Object[] objArray_orderedTop = (Object[]) msg.obj;
			Object mtvTopObj = objArray_orderedTop[1];
			String indexTopStr = (String) objArray_orderedTop[3];
			if(mtvTopObj != null) {
				boolean result = performCmdTop((Mtv)mtvTopObj, Integer.valueOf(indexTopStr));
				KuwoLog.d(LOG_TAG, "远程置顶成功");
				
				Message responseMsg = new Message();
				responseMsg.what = MessageCommons.RESULT_ORDERED_MTV_TOP;
				responseMsg.obj = new Object[]{objArray_orderedTop[0]};
				EventBus.getDefault().post(responseMsg);
			}
			break;
		case MessageCommons.CMD_ORDERED_DELETE:
			Object[] objArray_orderedDelete = (Object[]) msg.obj;
			Object mtvDeleteObj = objArray_orderedDelete[1];
			String indexDeleteStr = (String) objArray_orderedDelete[3];
			if(mtvDeleteObj != null) {
				boolean result = performCmdDelete((Mtv)mtvDeleteObj, Integer.valueOf(indexDeleteStr));
				KuwoLog.d(LOG_TAG, "远程删除成功");
				
				Message responseMsg = new Message();
				responseMsg.what = MessageCommons.RESULT_ORDERED_MTV_DELETE;
				responseMsg.obj = new Object[]{objArray_orderedDelete[0]};
				EventBus.getDefault().post(responseMsg);
			}
			break;
		default:
			break;
		}
		super.onEvent(msg);
	}
	
	private boolean cmdAdd(Mtv mtv) {
		if(mtv == null)
			return false;
		OrderSerializeLogic.getInstance().addMtv(mtv);
		
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append("远程添加  ").append(mtv.name).append(" 到已点列表");
		sb.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 5+mtv.name.length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		DialogUtils.toast(sb, false);
		return true;
	}
	
	/*
	 * index=-1表示来自手机端点歌台，index>0表示来自手机端已点歌曲列表
	 */
	private boolean performCmdPlay(Mtv mtv, int index) {
		if(mtv == null)
			return false;
		if(!AppContext.getNetworkSensor().hasAvailableNetwork()) {
			DialogUtils.toast("当前没有网络！", false);
			return false;
		}
		Intent singIntent = new Intent(this, PlayActivity.class);
		singIntent.putExtra("mtv", mtv);
		startActivity(singIntent);
		
		if (index < 0){
			OrderSerializeLogic.getInstance().singMtv(this.getApplication(), mtv, false, -1);
		}else{
			int result = OrderSerializeLogic.getInstance().singMtv(this.getApplication(), mtv, true, index);
			if (result == -1){
				return false;
			}
		}	
		
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append("远程播放  ").append(mtv.name);
		sb.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 5+mtv.name.length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		DialogUtils.toast(sb, false);
		return true;
	}
	
	private boolean performCmdPlayUser(UserMtv userMtv) {
		if(!AppContext.getNetworkSensor().hasAvailableNetwork()) {
			DialogUtils.toast("当前没有网络！", false);
			return false;
		}
		if(MainApplication.isSingActivityAliving) {
			Message msg = new Message();
			msg.what = Constants.MSG_OPEN_USER_MTV_ACTIVITY_WHEN_PLAY_MTV;
			EventBus.getDefault().post(msg);
		}
		Intent singIntent = new Intent(this, PlayUserMtvActivity.class);
		singIntent.putExtra("userMtv", userMtv);
		startActivity(singIntent);
		return true;
	}
	
	private boolean performCmdTop(Mtv mtv, int index){
		if (mtv == null){
			return false;
		}
		
		if(!AppContext.getNetworkSensor().hasAvailableNetwork()) {
			DialogUtils.toast("当前没有网络！", false);
			return false;
		}
		
		OrderSerializeLogic.getInstance().topMtv(mtv, index);
		
		return true;
	}
	
	private boolean performCmdDelete(Mtv mtv, int index){
		if (mtv == null){
			return false;
		}
		
		if(!AppContext.getNetworkSensor().hasAvailableNetwork()) {
			DialogUtils.toast("当前没有网络！", false);
			return false;
		}
		
		OrderSerializeLogic.getInstance().deleteMtv(mtv, index);
		
		return true;
	}
}
