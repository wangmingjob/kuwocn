package cn.kuwo.sing.phone4tv.view.activity;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.bean.UpdateLog;
import cn.kuwo.sing.phone4tv.business.ListBusiness;
import cn.kuwo.sing.phone4tv.commmons.log.LogSystem;
import cn.kuwo.sing.phone4tv.commons.context.AppContext;
import cn.kuwo.sing.phone4tv.commons.context.Constants;
import cn.kuwo.sing.phone4tv.commons.context.MainApplication;
import cn.kuwo.sing.phone4tv.commons.file.PreferencesManager;
import cn.kuwo.sing.phone4tv.commons.socket.MessageCommons;
import cn.kuwo.sing.phone4tv.commons.socket.RequestMessage;
import cn.kuwo.sing.phone4tv.commons.socket.SocketManager;
import cn.kuwo.sing.phone4tv.commons.util.CommonUtils;
import cn.kuwo.sing.phone4tv.commons.util.DataHandler;
import cn.kuwo.sing.phone4tv.commons.util.DensityUtils;
import cn.kuwo.sing.phone4tv.view.adapter.MainFragmentPagerAdapter;
import cn.kuwo.sing.phone4tv.view.widget.ChangeLog;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;
import com.devspark.appmsg.AppMsg;
import com.google.zxing.client.android.CaptureActivity;
import com.jazzyview.jazzyviewpager.JazzyViewPager;
import com.jazzyview.jazzyviewpager.JazzyViewPager.TransitionEffect;
import com.slidinglayer.SlidingLayer;

public class MainActivity extends SherlockFragmentActivity {
	private static final String LOG_TAG = "MainActivity";
	private static boolean hasBackButtonFirstClick = false;
	private PagerSlidingTabStrip mTabs;
	private JazzyViewPager mViewPager;
	private SlidingLayer mSlidingLayerMain;
	private Timer mExitTimer = new Timer();
	private LinearLayout llDisconnectedTvLayout;
	private LinearLayout llConnectedTvLayout;
	//private SocketManager mSocketManager;
	private ImageView btDisconnectedTvUpOrDown;
	private ImageView btConnectedTvUpOrDown;
	
	private ImageView btDisconnectedTvScanQrCode;
	private FrameLayout flConnectedTvOrderedMtv;
	private FrameLayout flConnectedTvToPreMtv;
	private FrameLayout flConnectedTvToPlay;
	private FrameLayout flConnectedTvToNextMtv;
	private FrameLayout flConnectedTvOrig;
	private FrameLayout flConnectedTvToScore;
	private FrameLayout flConnectedTvToReplay;
	private FrameLayout flConnectedTvMicphoneVolumeAdd;
	private FrameLayout flConnectedTvMicphoneVolumeDec;
	private FrameLayout flConnectedTvAccompVolumeAdd;
	private FrameLayout flConnectedTvAccompVolumeDec;
	private FrameLayout flConnectedTvToPause;
	private FrameLayout flConnectedTvAccomp;
	
	private ProgressBar pbConnectedTvSwitchOrigOrAccomp;
	private ProgressBar pbConnectedTvSwitchPlayOrPause;
	
	private AlertDialog historyDialog;
	private String mLastServerAddressInfo;
	
	private void showPlayOrPauseProgressBar() {
		flConnectedTvToPause.setVisibility(View.GONE);
		flConnectedTvToPlay.setVisibility(View.GONE);
		pbConnectedTvSwitchPlayOrPause.setVisibility(View.VISIBLE);
	}
	
	private void showOrigOrAccompProgressBar() {
		flConnectedTvAccomp.setVisibility(View.GONE);
		flConnectedTvOrig.setVisibility(View.GONE);
		pbConnectedTvSwitchOrigOrAccomp.setVisibility(View.VISIBLE);
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btDisconnectedTvScanQrCode:
				//打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
				break;
			case R.id.flConnectedTvOrderedMtv:
				performCommand(MessageCommons.CMD_ORDERED_MTV);
				break;
			case R.id.flConnectedTvToPreMtv:
				performCommand(MessageCommons.CMD_TO_PRE_MTV);
				break;
			case R.id.flConnectedTvToPlay:
				performCommand(MessageCommons.CMD_TO_PLAY);
				break;
			case R.id.flConnectedTvToPause:
				performCommand(MessageCommons.CMD_TO_PAUSE);
				break;
			case R.id.flConnectedTvToNextMtv:
				performCommand(MessageCommons.CMD_TO_NEXT_MTV);
				break;
			case R.id.flConnectedTvOrig:
				performCommand(MessageCommons.CMD_TO_SWITCH_ORIGINAL);
				break;
			case R.id.flConnectedTvAccomp:
				performCommand(MessageCommons.CMD_TO_SWITCH_ACCOMP);
				break;
			case R.id.flConnectedTvToScore:
				performCommand(MessageCommons.CMD_TO_SCORE);
				break;
			case R.id.flConnectedTvToReplay:
				performCommand(MessageCommons.CMD_TO_REPLAY);
				break;
			case R.id.flConnectedTvMicphoneVolumeAdd:
				performCommand(MessageCommons.CMD_ADD_MICPHONE_VOLUME);
				break;
			case R.id.flConnectedTvMicphoneVolumeDec:
				performCommand(MessageCommons.CMD_DEC_MICPHONE_VOLUME);
				break;
			case R.id.flConnectedTvAccompVolumeAdd:
				performCommand(MessageCommons.CMD_ADD_ACCOMP_VOLUME);
				break;
			case R.id.flConnectedTvAccompVolumeDec:
				performCommand(MessageCommons.CMD_DEC_ACCOMP_VOLUME);
				break;
			default:
				break;
			}
		}
	};
	
	
	private void performCommand(final int cmd) {
		final SocketChannel socketChannel = MainApplication.s_socketChannel;
		if(socketChannel != null && socketChannel.isConnected()) {
			switch (cmd) {
			case MessageCommons.CMD_ORDERED_MTV:
				Intent orderedIntent = new Intent(MainActivity.this, OrderedActivity.class);
				startActivity(orderedIntent);
				return;
			case MessageCommons.CMD_TO_PLAY:
				showPlayOrPauseProgressBar();
				break;
			case MessageCommons.CMD_TO_PAUSE:
				showPlayOrPauseProgressBar();
				break;
			case MessageCommons.CMD_TO_SWITCH_ORIGINAL:
				showOrigOrAccompProgressBar();
				break;
			case MessageCommons.CMD_TO_SWITCH_ACCOMP:
				showOrigOrAccompProgressBar();
				break;
			default:
				break;
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						RequestMessage requestMessage = SocketManager.getInstance().createRequestMessage(
								cmd, 
								null, 
								null,
								null,
								System.currentTimeMillis());
						SocketManager.getInstance().writeRequestMessage(socketChannel, requestMessage);
					} catch (SocketException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}else {
			refreshConnectSlidingLayer();
			AppMsg.makeText(MainActivity.this, "没有连接电视，请先扫描二维码!", AppMsg.STYLE_ALERT)
			.setLayoutGravity(Gravity.TOP)
			.show();
		}
	}
	
    private class ExitTimerTask extends TimerTask {

		@Override
		public void run() {
			hasBackButtonFirstClick = false;
		}
	}
    
    private Handler mUpdateConversationHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//connect成功，要么是通过扫描二维码，要么是从History中自动连接,都会调用preProcessConnect..方法
			case MessageCommons.RESULT_CONNECT_SUCCESS: 
				mSlidingLayerMain.setOpenOnTapEnabled(true);
				if(connectDialog != null && connectDialog.isShowing())
					connectDialog.dismiss();
				AppMsg.makeText(MainActivity.this, "连接成功", AppMsg.STYLE_INFO)
				.setLayoutGravity(Gravity.TOP)
				.show();
				if(!TextUtils.isEmpty(mLastServerAddressInfo))
					PreferencesManager.put(Constants.LAST_SERVER_IP_KEY, mLastServerAddressInfo);
				refreshConnectSlidingLayer();
				break;
			case MessageCommons.RESULT_CONNECT_FAIL:
				mSlidingLayerMain.setOpenOnTapEnabled(true);
				if(connectDialog != null && connectDialog.isShowing())
					connectDialog.dismiss();
				reloadHistoryDialog();
				AppMsg.makeText(MainActivity.this, "连接失败，请尝试二维码连接", AppMsg.STYLE_ALERT)
				.setLayoutGravity(Gravity.TOP)
				.show();
				break;
			case MessageCommons.RESULT_CONNECT_TIME_OUT:
				mSlidingLayerMain.setOpenOnTapEnabled(true);
				if(connectDialog != null && connectDialog.isShowing())
					connectDialog.dismiss();
				reloadHistoryDialog();
				AppMsg.makeText(MainActivity.this, "连接超时", AppMsg.STYLE_ALERT)
				.setLayoutGravity(Gravity.TOP)
				.show();
				break;
			case MessageCommons.RESULT_IS_NOT_PLAYING:
				AppMsg.makeText(MainActivity.this, "TV没有处于播放状态，请先播放", AppMsg.STYLE_ALERT)
				.setLayoutGravity(Gravity.TOP)
				.show();
				showPauseView();
				showOrigView();
				break;
			case MessageCommons.RESULT_SERVER_SHUTDOWN:
				mSlidingLayerMain.setOpenOnTapEnabled(true);
				AppMsg.makeText(MainActivity.this, "TV端K歌已经关闭", AppMsg.STYLE_ALERT)
				.setLayoutGravity(Gravity.TOP)
				.show();
				refreshConnectSlidingLayer();
				break;
			case MessageCommons.RESULT_TO_SWITCH_ORIGINAL:
				AppMsg.makeText(MainActivity.this, "播放伴唱", AppMsg.STYLE_INFO)
				.setLayoutGravity(Gravity.TOP)
				.show();
				showOrigView();
				break;
			case MessageCommons.RESULT_TO_SWITCH_ACCOMP:
				AppMsg.makeText(MainActivity.this, "播放原唱", AppMsg.STYLE_INFO)
				.setLayoutGravity(Gravity.TOP)
				.show();
				showAccompView();
				break;
			case MessageCommons.RESULT_TO_PLAY:
				AppMsg.makeText(MainActivity.this, "开始播放", AppMsg.STYLE_INFO)
				.setLayoutGravity(Gravity.TOP)
				.show();
				showPlayView();
				break;
			case MessageCommons.RESULT_TO_PAUSE:
				AppMsg.makeText(MainActivity.this, "暂停播放", AppMsg.STYLE_INFO)
				.setLayoutGravity(Gravity.TOP)
				.show();
				showPauseView();
				break;
			default:
				break;
			}
		}
    	
    };
	private TextView tvAutoConnectServer;
    
    private void showPlayView() {
    	pbConnectedTvSwitchPlayOrPause.setVisibility(View.GONE);
    	flConnectedTvToPause.setVisibility(View.GONE);
    	flConnectedTvToPlay.setVisibility(View.VISIBLE);
    }
    
    private void showPauseView() {
    	pbConnectedTvSwitchPlayOrPause.setVisibility(View.GONE);
    	flConnectedTvToPlay.setVisibility(View.GONE);
    	flConnectedTvToPause.setVisibility(View.VISIBLE);
    }
    
    private void showAccompView() {
    	pbConnectedTvSwitchOrigOrAccomp.setVisibility(View.GONE);
    	flConnectedTvOrig.setVisibility(View.GONE);
    	flConnectedTvAccomp.setVisibility(View.VISIBLE);
    }
    
    private void showOrigView() {
    	pbConnectedTvSwitchOrigOrAccomp.setVisibility(View.GONE);
    	flConnectedTvAccomp.setVisibility(View.GONE);
    	flConnectedTvOrig.setVisibility(View.VISIBLE);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//add menu
//		menu.add(Constants.MENU_GROUP_ID, Constants.MENU_ITEM_ID_SEARCH, Menu.NONE,Constants.MENU_ITEM_TITLE_SEARCH)
//		.setIcon(R.drawable.bt_search_selector)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); 
//		
//		menu.add(Constants.MENU_GROUP_ID, Constants.MENU_ITEM_ID_UPDATE, Menu.NONE,Constants.MENU_ITEM_TITLE_UPDATE)
//		.setIcon(android.R.drawable.ic_menu_search)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); 
		
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(LOG_TAG, "onCreateOptionsMenu");
		switch (item.getItemId()) {
		case R.id.menu_main_search:
			startActivity(new Intent(MainActivity.this, SearchActivity.class));
			break;
		case R.id.menu_main_update:
			if(AppContext.getNetworkSensor().hasAvailableNetwork()) {
				ListBusiness listBusiness = new ListBusiness();
				listBusiness.getUpdateLog(new UpdateLogHandler());
			}else {
				AppMsg.makeText(MainActivity.this, "当前没有连接网络，无法检测!", AppMsg.STYLE_ALERT)
				.setLayoutGravity(Gravity.TOP)
				.show();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class UpdateLogHandler extends DataHandler<String> {

		@Override
		public void onSuccess(String jsonResult) {
			UpdateLog ul = new UpdateLog();
			try {
				JSONObject jsonObj = new JSONObject(jsonResult);
				String resultStr = jsonObj.getString("result");
				if(!TextUtils.isEmpty(resultStr) && resultStr.equals("ok")) {
					ul.version = jsonObj.getString("version");
					ul.info = jsonObj.getString("msg");
					ul.time = jsonObj.getString("time");
					final String link = jsonObj.getString("link");
					final ChangeLog changeLog = new ChangeLog(MainActivity.this, ChangeLog.DEFAULT_SMALL_CSS);
					String fullLog = changeLog.getFormatLog(ul.version, ul.time, ul.info);
					AlertDialog dialog = changeLog.getLogDialog4Update(fullLog);
					dialog.setButton(DialogInterface.BUTTON_POSITIVE, "下次再说", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							changeLog.updateVersionInPreferences();
						}
					});
					dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "下载", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							   Uri  uri = Uri.parse(link);
							   Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
							   startActivity(intent);
						}
					});
					dialog.show();
				}
			} catch (JSONException e) {
				LogSystem.e(LOG_TAG, e.toString());
			}			
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			processAddressAndConnectServer(scanResult);
		}
	}
	
	private void processAddressAndConnectServer(String serverAddressInfo) {
		if(serverAddressInfo.contains(":")) {
			//连接服务端ip
			String[] ipAndPort = serverAddressInfo.split(":");
			mLastServerAddressInfo = serverAddressInfo;
			performConnectServer(ipAndPort[0], ipAndPort[1]);
		}else {
			mSlidingLayerMain.setOpenOnTapEnabled(true);
			AppMsg.makeText(MainActivity.this, "无效的电视二维码，请勿扫描其它二维码", AppMsg.STYLE_ALERT)
			.setLayoutGravity(Gravity.TOP)
			.show();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initView();
//		loadChangeLog();
	}
	
	private void loadChangeLog() {
		ChangeLog log = new ChangeLog(this, ChangeLog.DEFAULT_SMALL_CSS);
		if(log.isFirstRun()) {
			AlertDialog dialog = log.getLogDialog();
			dialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SocketManager.getInstance().removeHandler(MainActivity.class.getSimpleName());
		SocketManager.getInstance().closeChannel();
	}
	
	private void initView() {
		CommonUtils.setActionBarColor(this);
		Resources rs = getResources();
		//Tab
		mTabs = (PagerSlidingTabStrip)findViewById(R.id.pagerSlidingTabStrip);
		mTabs.setShouldExpand(true);
//		final int tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rs.getDimension(R.dimen.tab_font_size),rs.getDisplayMetrics());
		final int tabTextSize = DensityUtils.dip2px(this, 14.6f);
		mTabs.setTextSize(tabTextSize);
		mTabs.setIndicatorHeight(6);
		mTabs.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		mTabs.setIndicatorColorResource(R.color.background_slidetab);
		
		//ViewPager
		mViewPager = (JazzyViewPager)findViewById(R.id.viewPager);
		final int viewPagerMarginPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rs.getDimension(R.dimen.viewpager_margin), rs.getDisplayMetrics());
		mViewPager.setPageMargin(viewPagerMarginPixels);
		String[] titles = getResources().getStringArray(R.array.main_fragment_titles);
		final MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), titles, mViewPager);
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setTransitionEffect(TransitionEffect.Tablet);
		mViewPager.setAdapter(pagerAdapter);
		mTabs.setViewPager(mViewPager);
		
		llDisconnectedTvLayout = (LinearLayout)findViewById(R.id.llDisconnectedTvLayout);
		llConnectedTvLayout = (LinearLayout)findViewById(R.id.llConnectedTvLayout);
		
		btDisconnectedTvUpOrDown = (ImageView) findViewById(R.id.btDisconnectedTvUpOrDown);
		btConnectedTvUpOrDown = (ImageView)findViewById(R.id.btConnectedTvUpOrDown);
		
		//disconnected layout
        btDisconnectedTvScanQrCode = (ImageView)findViewById(R.id.btDisconnectedTvScanQrCode);
        btDisconnectedTvScanQrCode.setOnClickListener(mOnClickListener);
        
        //connected layout
        flConnectedTvOrderedMtv = (FrameLayout)findViewById(R.id.flConnectedTvOrderedMtv);
        flConnectedTvToPreMtv = (FrameLayout) findViewById(R.id.flConnectedTvToPreMtv);
		flConnectedTvToPlay = (FrameLayout) findViewById(R.id.flConnectedTvToPlay);
		flConnectedTvToPause = (FrameLayout)findViewById(R.id.flConnectedTvToPause);
		flConnectedTvToNextMtv = (FrameLayout) findViewById(R.id.flConnectedTvToNextMtv);
		flConnectedTvOrig = (FrameLayout)findViewById(R.id.flConnectedTvOrig);
		flConnectedTvAccomp = (FrameLayout)findViewById(R.id.flConnectedTvAccomp);
		flConnectedTvToScore = (FrameLayout)findViewById(R.id.flConnectedTvToScore);
		flConnectedTvToReplay = (FrameLayout)findViewById(R.id.flConnectedTvToReplay);
		flConnectedTvMicphoneVolumeAdd = (FrameLayout)findViewById(R.id.flConnectedTvMicphoneVolumeAdd);
		flConnectedTvMicphoneVolumeDec = (FrameLayout)findViewById(R.id.flConnectedTvMicphoneVolumeDec);
		flConnectedTvAccompVolumeAdd = (FrameLayout)findViewById(R.id.flConnectedTvAccompVolumeAdd);
		flConnectedTvAccompVolumeDec = (FrameLayout)findViewById(R.id.flConnectedTvAccompVolumeDec);
		
		pbConnectedTvSwitchPlayOrPause = (ProgressBar)findViewById(R.id.pbConnectedTvSwitchPlayOrPause);
		pbConnectedTvSwitchOrigOrAccomp = (ProgressBar)findViewById(R.id.pbConnectedTvSwitchOrigOrAccomp);
		
		
		flConnectedTvOrderedMtv.setOnClickListener(mOnClickListener);
		flConnectedTvToPreMtv.setOnClickListener(mOnClickListener);
		flConnectedTvToPlay.setOnClickListener(mOnClickListener);
		flConnectedTvToPause.setOnClickListener(mOnClickListener);
		flConnectedTvToNextMtv.setOnClickListener(mOnClickListener);
		flConnectedTvOrig.setOnClickListener(mOnClickListener);
		flConnectedTvAccomp.setOnClickListener(mOnClickListener);
		flConnectedTvToScore.setOnClickListener(mOnClickListener);
		flConnectedTvToReplay.setOnClickListener(mOnClickListener);
		flConnectedTvMicphoneVolumeAdd.setOnClickListener(mOnClickListener);
		flConnectedTvMicphoneVolumeDec.setOnClickListener(mOnClickListener);
		flConnectedTvAccompVolumeAdd.setOnClickListener(mOnClickListener);
		flConnectedTvAccompVolumeDec.setOnClickListener(mOnClickListener);
		
		mSlidingLayerMain = (SlidingLayer) findViewById(R.id.slidingLayerMain);
		mSlidingLayerMain.setOnInteractListener(mOnInteractListener);
		
		tvAutoConnectServer = (TextView)findViewById(R.id.tvAutoConnectServer);
		tvAutoConnectServer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(llDisconnectedTvLayout.getVisibility() == View.VISIBLE) {
					mSlidingLayerMain.setOpenOnTapEnabled(false);
					toProcessAddressAndAutoConnectServer();
				}
			}
		});
		
		refreshConnectSlidingLayer();
	}
	
	private void initData() {
		SocketManager.getInstance().addHander(MainActivity.class.getSimpleName(), mUpdateConversationHandler);
	}
	
	private void refreshConnectSlidingLayer() {
		final SocketChannel socketChannel = MainApplication.s_socketChannel;
		if(socketChannel != null && socketChannel.isConnected()) 
			showConnectedTvLayout();
		else
			showDisconnectedTvLayout();
	}
	
	private void showConnectedTvLayout() {
		llDisconnectedTvLayout.setVisibility(View.GONE);
		llConnectedTvLayout.setVisibility(View.VISIBLE);
	}
	
	private void showDisconnectedTvLayout() {
		llConnectedTvLayout.setVisibility(View.GONE);
		llDisconnectedTvLayout.setVisibility(View.VISIBLE);
//		toConnectTvServer();
		toProcessAddressAndAutoConnectServer();
	}
	
	/**
	 * 根据历史记录，尝试自动连接
	 */
	private void toProcessAddressAndAutoConnectServer() {
		String lastServerIpInfo = PreferencesManager.getString(Constants.LAST_SERVER_IP_KEY, null);
		if(!TextUtils.isEmpty(lastServerIpInfo)) {
			processAddressAndConnectServer(lastServerIpInfo);
		}
	}
	
	/**
	 * 暂时去掉MulticastSocket的组播自动连接功能
	 */
	private void toConnectTvServer() {
		new Thread(new Runnable() {
			
			private InetAddress groupAddr;
			private MulticastSocket multiSocket;

			@Override
			public void run() {
				//receive mutlcast and connect server
				try {
					multiSocket = new MulticastSocket(Constants.MULTICAST_PORT);
					groupAddr = InetAddress.getByName(Constants.MULTICAST_IP);
					multiSocket.joinGroup(groupAddr);
					
					 byte[] rev = new byte[512];  
					 DatagramPacket  packet = new DatagramPacket(rev, rev.length);  
					 multiSocket.receive(packet);  
				     String scanResult = new String(packet.getData()).trim();  //不加trim，则会打印出512个byte，后面是乱码  
				     AppMsg.makeText(MainActivity.this, "侦测到Tv的信息："+scanResult, AppMsg.STYLE_INFO)
							.setLayoutGravity(Gravity.TOP)
							.show();
				     
				     if(scanResult.contains(":")) {
							//连接服务端ip
							String[] ipAndPort = scanResult.split(":");
							performConnectServer(ipAndPort[0], ipAndPort[1]);
						}else {
							AppMsg.makeText(MainActivity.this, "无效的电视二维码", AppMsg.STYLE_ALERT)
							.setLayoutGravity(Gravity.TOP)
							.show();
						}
				} catch (IOException e) {
					e.printStackTrace();
					try {
						multiSocket.leaveGroup(groupAddr);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
//				final SocketChannel socketChannel = MainApplication.s_socketChannel;
//				if(socketChannel != null && socketChannel.isConnected()) {
//					showConnectedTvLayout();
//				}else {
//					reloadHistoryItems();
//				}
				
			}
		}).start();
	}
	
	/**
	 * 如果连接失败，就显示历史记录
	 */
	private void reloadHistoryDialog() {
		AlertDialog.Builder historyDialogBuilder = new AlertDialog.Builder(this);
		historyDialogBuilder.setTitle("连接失败");
		View view = View.inflate(this, R.layout.server_address_history_layout, null);
		historyDialogBuilder.setView(view);
		TextView tvLastServerAddressInfo = (TextView) view.findViewById(R.id.tvLastServerAddressInfo);
		final String lastServerAddressInfo = PreferencesManager.getString(Constants.LAST_SERVER_IP_KEY, "记录为空");
		tvLastServerAddressInfo.setText(lastServerAddressInfo);
		tvLastServerAddressInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(historyDialog != null && historyDialog.isShowing())
					historyDialog.dismiss();
				processAddressAndConnectServer(lastServerAddressInfo);
			}
		});
		historyDialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(historyDialog != null && historyDialog.isShowing())
					historyDialog.dismiss();
			}
		});
		historyDialog = historyDialogBuilder.create();
		historyDialog.show();//show()一定要在setContentView之前
	  }
	
	private SlidingLayer.OnInteractListener mOnInteractListener = new SlidingLayer.OnInteractListener() {
		
		@Override
		public void onOpened() {
			final SocketChannel socketChannel = MainApplication.s_socketChannel;
			if(socketChannel != null && socketChannel.isConnected())
				btConnectedTvUpOrDown.setBackgroundResource(R.drawable.flag_down_selector);
			else 
				btDisconnectedTvUpOrDown.setBackgroundResource(R.drawable.flag_down_selector);
		}
		
		@Override
		public void onOpen() {
		}
		
		@Override
		public void onClosed() {
			final SocketChannel socketChannel = MainApplication.s_socketChannel;
			if(socketChannel != null && socketChannel.isConnected())
				btConnectedTvUpOrDown.setBackgroundResource(R.drawable.flag_up_selector);
			else 
				btDisconnectedTvUpOrDown.setBackgroundResource(R.drawable.flag_up_selector);
		}
		
		@Override
		public void onClose() {
			
		}
	};
	private ProgressDialog connectDialog;
	
	private void performConnectServer(String serverIp, String serverPort) {
		String localIp = CommonUtils.getWifiIp(this);
		String localIpPrefix = "";
		String tvIpPrefix = "";
		if(serverIp.contains(".") && localIp.contains(".")) {
			localIpPrefix = localIp.substring(0, localIp.lastIndexOf("."));
			tvIpPrefix = serverIp.substring(0, serverIp.lastIndexOf("."));
		}else {
			mSlidingLayerMain.setOpenOnTapEnabled(true);
			AppMsg.makeText(MainActivity.this, "IP地址不正确！", AppMsg.STYLE_ALERT)
			.setLayoutGravity(Gravity.TOP).show();
			return;
		}

		if(localIpPrefix.equals(tvIpPrefix)) {
			connectDialog = new ProgressDialog(this);
			connectDialog.setTitle("正在连接...");
			connectDialog.show();
			//点击 “连接” 进行服务端的连接操作
			startSocketConnectThread(serverIp, Integer.parseInt(serverPort));
		}else {
			mSlidingLayerMain.setOpenOnTapEnabled(true);
			AppMsg.makeText(MainActivity.this, "不在同一网段内!", AppMsg.STYLE_ALERT)
			.setLayoutGravity(Gravity.TOP)
			.show();
			if(connectDialog != null && connectDialog.isShowing())
				connectDialog.dismiss();
			reloadHistoryDialog();
		}
	}
	
	private void startSocketConnectThread(String serverIp, int serverPort) {
		SocketManager.getInstance().startSocketConnectThread(serverIp, serverPort);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//点击两次返回键退出应用
		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if(mSlidingLayerMain.isOpened()) {
				mSlidingLayerMain.closeLayer(true);
			}else {
				if(hasBackButtonFirstClick) {
					finish();
				}else { 
					AppMsg.makeText(this, R.string.click_again_to_exit, AppMsg.STYLE_INFO)
					.setLayoutGravity(Gravity.TOP)
					.show();
					hasBackButtonFirstClick = true;
					mExitTimer.schedule(new ExitTimerTask(), 2000); //2秒后重置标记
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}