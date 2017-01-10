package cn.kuwo.sing.tv.controller;

import java.util.Timer;
import java.util.TimerTask;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.thread.LoopHandler;
import cn.kuwo.framework.utils.TimeUtils;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.logic.DataHandler;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic;
import cn.kuwo.sing.tv.logic.OrderSerializeLogic.OnCurrentMtvChangedListener;
import cn.kuwo.sing.tv.logic.SingLogic;
import cn.kuwo.sing.tv.socket.MessageCommons;
import cn.kuwo.sing.tv.utils.BitmapTools;
import cn.kuwo.sing.tv.utils.CommonAnimation;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.view.activity.ItemListActivity;
import cn.kuwo.sing.tv.view.fragment.PlayOrderedMtvFragment;
import cn.kuwo.sing.tv.view.widget.KonkaVideoView;

import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 
 * @Package cn.kuwo.sing.tv.controller
 *
 * @Date 2013-4-12, 下午2:52:18, 2013
 *
 * @Author likang 
 * 
 * @Description 播放模块[上一首，下一首，切歌，暂停，伴唱AudioTrack(1.原唱，2.伴唱)，重唱，已点，点歌台]
 *
 */
public class KonkaPlayController extends BaseController implements OnBufferingUpdateListener, OnPreparedListener, OnCompletionListener,
OnInfoListener, OnErrorListener,  OnCurrentMtvChangedListener {

	private static final String TAG = "PlayController";
	private TextView tvSingCurrentMtvName;
	private TextView tvSingNextMtvName;
	private ImageView ivSingMenuScoreMtv;
	private ImageView ivSingMenuSwitchMtv;
	private ImageView ivSingMenuPauseMtv;
	private ImageView ivSingMenuReplayMtv;
	private ImageView ivSingMenuOriginalMtv;
	private ImageView ivSingMenuOrderedMtv;
	private ImageView ivSingMenuMtvPlatform;
	private FrameLayout flSingOrderedMtvList;
	private SeekBar sbSingControllerSeekBar;
	private TextView tvSingControllerPlayedTime;
	private TextView tvSingControllerTotalTime;
	//private KwVideoView mVideoView;
	private KonkaVideoView mVideoView;
	private LoopHandler mLoopHandler = new LoopHandler();
	private PlayOrderedMtvFragment mOrderedMtvFragment;
	private FragmentTransaction mTransaction;
	private LinearLayout ll_sing_play_controller;
	private ViewGroup llSingScore;
	private ImageView ivSingMenuSlidebar;
	private static final int HIDE_KEYBOARD_MSG_WHAT = 0;
	private static final int HIDE_VOLUME_DIALOG_WHAT = 1;
	private int mFromX = -1;
	/** 播放控制栏滑块左右滑动动画时长*/
	private static final int SLIDEBAR_TRANSLATE_DURATION = 200; 
	/**  播放控制栏隐藏显示时长*/
	private static final int MENU_TRANSLATE_DURATION = 400;
	
	private static final int HIDE_MENU_DURATION = 8000; 
	private static final int HIDE_VOLUME_DIALOG_DURATION = 8000;
	private static final int HIDE_MENU_DURATION_FIRST = 15000; 
	private static final int SWITCH_DURATION  = 500;	//上次切歌到下次切歌的时间间隔
	
	private ImageView mCurrentSingMenu = null;
	private EventBus mEventBus;
	private boolean mHasFirstClick = false;
	private boolean mSingMenuClick = false;
	private boolean mOrderedMtvClick = false;
	//private Timer exitTimer = new Timer();
	private Timer singMenuTimer = new Timer();
	private Timer orderedMtvTimer = new Timer();
	private Timer switchPreSongTimer = new Timer();
	private Timer switchNextSongTimer = new Timer();
	private LinearLayout llSingActivityWaiting;
	private LinearLayout llSingPlayBufferFail;

	private float mAccompVolume;
	private float mMicrophoneVolume;
	private TextView tvSingSingleScore;
	private TextView tvSingTotalScore;
	private LinearLayout llSingVolumeController;
	private RelativeLayout rl_sing_menu_operate_prompt;
	private boolean hasFirstClickLeftButton = false;
	private boolean hasFirstClickRightButton = false;
	private SeekBar sb_sing_accomp_volume;
	private SeekBar sb_sing_microphone_volume;
	private SingLogic mSingLogic;
	private TextView tv_sing_accomp_volume_prompt;
	private TextView tv_sing_microphone_volume_prompt;
	private LinearLayout ll_sing_accomp_background;
	private LinearLayout ll_sing_microphone_background;
	private Mtv mCurrentMtv;
	private RelativeLayout rlSingCurrentMtvPrompt;
	private RelativeLayout rlSingNextMtvPrompt;
	private int mSlideBarWidth;
	private int mSlideBarHeight;
	private Bitmap mBitmapOriginal;
	private Bitmap mBitmapAccomp;
	private Bitmap mBitmapScore;
	private Bitmap mBitmapReplay;
	private Bitmap mBitmapPause;
	private Bitmap mBitmapPlay;
	private Bitmap mBitmapSwitch;
	private Bitmap mBitmapOrdered;
	private Bitmap mBitmapPlatform;
	
	private Bitmap mBitmapOriginalPressed;
	private Bitmap mBitmapAccompPressed;
	private Bitmap mBitmapScorePressed;
	private Bitmap mBitmapReplayPressed;
	private Bitmap mBitmapPausePressed;
	private Bitmap mBitmapPlayPressed;
	private Bitmap mBitmapSwitchPressed;
	private Bitmap mBitmapOrderedPressed;
	private Bitmap mBitmapPlatformPressed;
	
	private Bitmap mBitmapScoreDisable;
	private ScreenOffBroadcastReceiver mScreenOffReceiver;
	private ScreenOnBroadcastReceiver mScreenOnReceiver;
	
	private long lLastSwitchNextTime = 0;
	private long lLastSwitchPreTime = 0;
	private boolean mbSwitchAcc = false;
	private long mCurrentPosition = 0;
	
	boolean mSwitchedTrack = false;
	int mTrackNum = 2;
	ListLogic mlistLogic = new ListLogic();
//    SpeechEngine engine;
//    LocalWakeupParams params;
	
	public KonkaPlayController(Activity activity) {
		super(activity);
		mlistLogic.setContext(activity);
	}
	
	private Runnable mUpdateRunnable = new Runnable() {
		@Override
		public void run() {
			if(isPlaying() && AppContext.getNetworkSensor().hasAvailableNetwork()) {
				long pos = mVideoView.getCurrentPosition();
						
				mCurrentPosition = pos;	
				sbSingControllerSeekBar.setProgress((int)mCurrentPosition);
				if(mCurrentPosition <= mVideoView.getDuration())
					tvSingControllerPlayedTime.setText(TimeUtils.toString(mCurrentPosition));
				tvSingControllerTotalTime.setText(TimeUtils.toString(mVideoView.getDuration()));
			}
		}
	};
	private LinearLayout llSingMtvControllerBarBottom;
	private ImageView ivSingMenuAccompMtv;
	private ImageView ivSingMenuPlayMtv;
	private ImageView ivSingControllerLeft;
	private ImageView ivSingControllerRight;

	
	private class SwitchPreSongTask extends TimerTask {

		@Override
		public void run() {
			hasFirstClickLeftButton = false;
		}
	}
	
	private class SwitchNextSongTask extends TimerTask {

		@Override
		public void run() {
			hasFirstClickRightButton = false;
		}
	}
	
	private class ExitTimerTask extends TimerTask {

		@Override
		public void run() {
			 mHasFirstClick = false;
		}
	}
	
	private class SingMenuTimerTask extends TimerTask {

		@Override
		public void run() {
			mSingMenuClick = false;
		}
	}
	
	private class OrderedMtvTimerTask extends TimerTask {
		
		@Override
		public void run() {
			mOrderedMtvClick = false;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mEventBus = EventBus.getDefault();
		mEventBus.register(this);
		initData();
		initView();
		processLogic();
		//initSoundWakeUp();
	}
	
	
	@Override
	public void onStart() {
		Intent intent = activity.getIntent();
		open((Mtv) intent.getSerializableExtra("mtv"));
		super.onStart();
	}
		
	@Override
	public void onStop() {
		super.onStop();
		lgcOrder.setOnCurrentMtvChangedListener(null);
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mEventBus != null) 
			mEventBus.unregister(this);
		if(mScreenOffReceiver != null)
			activity.unregisterReceiver(mScreenOffReceiver);
		if(mScreenOnReceiver != null)
			activity.unregisterReceiver(mScreenOnReceiver);
		recycleBitmap();
		mlistLogic.delContext();
	}
	
	private void recycleBitmap() {
		if(mBitmapAccomp != null && !mBitmapAccomp.isRecycled()) {
			mBitmapAccomp.recycle();
			mBitmapAccomp = null;
		}
		if(mBitmapOrdered != null && !mBitmapOrdered.isRecycled()) { 
			mBitmapOrdered.recycle();
			mBitmapOrdered = null;
		}
		if(mBitmapOriginal != null && !mBitmapOriginal.isRecycled()) {
			mBitmapOriginal.recycle();
			mBitmapOrdered = null;
		}
		if(mBitmapPause != null && !mBitmapPause.isRecycled()) { 
			mBitmapPause.recycle();
			mBitmapPause = null;
		}
		if(mBitmapPlatform != null && !mBitmapPlatform.isRecycled()) { 
			mBitmapPlatform.recycle();
			mBitmapPlatform = null;
		}
		if(mBitmapPlay != null && !mBitmapPlay.isRecycled()) { 
			mBitmapPlay.recycle();
			mBitmapPlay = null;
		}
		if(mBitmapReplay != null && !mBitmapReplay.isRecycled()) {
			mBitmapReplay.recycle();
			mBitmapReplay = null;
		}
		if(mBitmapScore != null && !mBitmapScore.isRecycled()) { 
			mBitmapScore.recycle();
			mBitmapScore = null;
		}
		if(mBitmapSwitch != null && !mBitmapSwitch.isRecycled()) { 
			mBitmapSwitch.recycle();
			mBitmapSwitch = null;
		}
		
		if(mBitmapAccompPressed != null && !mBitmapAccompPressed.isRecycled()) {
			mBitmapAccompPressed.recycle();
			mBitmapAccompPressed = null;
		}
		if(mBitmapOrderedPressed != null && !mBitmapOrderedPressed.isRecycled()) { 
			mBitmapOrderedPressed.recycle();
			mBitmapOrderedPressed = null;
		}
		if(mBitmapOriginalPressed != null && !mBitmapOriginalPressed.isRecycled()) { 
			mBitmapOriginalPressed.recycle();
			mBitmapOriginalPressed = null;
		}
		if(mBitmapPausePressed != null && !mBitmapPausePressed.isRecycled()) { 
			mBitmapPausePressed.recycle();
			mBitmapPausePressed = null;
		}
		if(mBitmapPlatformPressed != null && !mBitmapPlatformPressed.isRecycled()) { 
			mBitmapPlatformPressed.recycle();
			mBitmapPlatformPressed = null;
		}
		if(mBitmapPlayPressed != null && !mBitmapPlayPressed.isRecycled()) { 
			mBitmapPlayPressed.recycle();
			mBitmapPlayPressed = null;
		}
		if(mBitmapReplayPressed != null && !mBitmapReplayPressed.isRecycled()) { 
			mBitmapReplayPressed.recycle();
			mBitmapReplayPressed = null;
		}
		if(mBitmapScorePressed != null && !mBitmapScorePressed.isRecycled()){ 
			mBitmapScorePressed.recycle();
			mBitmapScorePressed = null;
		}
		if(mBitmapSwitchPressed != null && !mBitmapSwitchPressed.isRecycled()) { 
			mBitmapSwitchPressed.recycle();
			mBitmapSwitchPressed = null;
		}
		
		if(mBitmapScoreDisable != null && !mBitmapScoreDisable.isRecycled()) {
			mBitmapScoreDisable.recycle();
			mBitmapScoreDisable = null;
		}
		System.gc();
	}
	
	public void onEvent(Message msg) {
		switch (msg.what) {
		case Constants.MSG_PLAY_CONTROLLER_CLOSE_ORDERED_MTV:
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			if(mCurrentSingMenu == null)
				updatePausePlay();
			else
				mVideoView.requestFocus();
			break;
		case Constants.MSG_VOLUME_CHANGED:
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			break;
		case Constants.MSG_CLOSE_PLAY_ACTIVITY_OR_PLAY_USER_ACTIVITY_WHEN_CLICK_HOME:
		case Constants.MSG_OPEN_USER_MTV_ACTIVITY_WHEN_PLAY_MTV:
			if(mVideoView != null) {
				MainApplication.isSingActivityAliving = false;
				mVideoView.pause();
				mVideoView.setVolume(0, 0);
				mVideoView.stopPlayback();
				activity.finish();
			}
			break;
		case Constants.MSG_NETWORK_UNVAILABLE:
			processNetworkUnavailable();
			break;
			//以下是手机发送给TV的消息
		case MessageCommons.CMD_TO_PLAY:
			displayToast("播放");
			if (!mVideoView.isPlaying()) {
				mVideoView.start();
			}
			updatePausePlay();
			sendMsgToService(MessageCommons.RESULT_TO_PAUSE);
			break;
		case MessageCommons.CMD_TO_PAUSE:
			displayToast("暂停");	
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
			}
			updatePausePlay();
			sendMsgToService(MessageCommons.RESULT_TO_PLAY);
			break;
		case MessageCommons.CMD_TO_PRE_MTV:
			displayToast("上一首"); 
			openPre();
			break;
		case MessageCommons.CMD_TO_NEXT_MTV:
			displayToast("下一首");
			openNext();
			break;
		case MessageCommons.CMD_TO_SWITCH_ORIGINAL:
			displayToast("原唱");
			switchAccomp();
			sendMsgToService(MessageCommons.RESULT_TO_SWITCH_ACCOMP);
			break;
		case MessageCommons.CMD_TO_SWITCH_ACCOMP:
			displayToast("伴唱");
			switchAccomp();
			sendMsgToService(MessageCommons.RESULT_TO_SWITCH_ORIGINAL);
			break;
		case MessageCommons.CMD_TO_SCORE:
			displayToast("打分");
			setScore();
			break;
		case MessageCommons.CMD_TO_REPLAY:
			displayToast("重唱");
			mVideoView.seekTo(0);
			break;
		case MessageCommons.CMD_ADD_MICPHONE_VOLUME:
			displayToast("麦克风音量+");
			//setMicVolume((int)mMicrophoneVolume*100+1);
			mMicrophoneVolume = mMicrophoneVolume >= 1.0f ? 1.0f : (mMicrophoneVolume + 0.01f);
			sb_sing_microphone_volume.setProgress((int) mMicrophoneVolume*100);
			break;
		case MessageCommons.CMD_DEC_MICPHONE_VOLUME:
			displayToast("麦克风音量-");
			//setMicVolume((int)mMicrophoneVolume*100-1);
			mMicrophoneVolume = mMicrophoneVolume <= 0.0f ? 0.0f : (mMicrophoneVolume - 0.01f);
			sb_sing_microphone_volume.setProgress((int) mMicrophoneVolume*100);
			break;
		case MessageCommons.CMD_ADD_ACCOMP_VOLUME:
			displayToast("伴唱音量+");
			//setMVVolume((int)mAccompVolume*100+1);
			mAccompVolume = mAccompVolume >= 1.0f ? 1.0f : (mAccompVolume + 0.01f);
			sb_sing_accomp_volume.setProgress((int) mAccompVolume*100);
			break;
		case MessageCommons.CMD_DEC_ACCOMP_VOLUME:
			displayToast("伴唱音量-");
			//setMVVolume((int)mAccompVolume*100-1);
			mAccompVolume = mAccompVolume <= 0.0f ? 0.0f : (mAccompVolume - 0.01f);
			sb_sing_accomp_volume.setProgress((int) mAccompVolume*100);
			break;
		default:
			break;
		}
	}
	
	private void processNetworkUnavailable() {
		mVideoView.pause();
		mVideoView.setVolume(0, 0);
		mVideoView.stopPlayback();
		showNetworkUnavailableDialog();
	}
	
	private void showNetworkUnavailableDialog() {
		try {
			final AlertDialog dialog = new AlertDialog.Builder(activity).create();
			dialog.show();
			dialog.getWindow().setContentView(R.layout.custom_alert_dialog);
			String exitPromptText = "网络中断，请检查您的网络连接";
			TextView tvPlayControllerExitPrompt = (TextView) dialog.getWindow().findViewById(R.id.tvPlayControllerExitPrompt);
			tvPlayControllerExitPrompt.setText(exitPromptText);
			Button btOk = (Button) dialog.getWindow().findViewById(R.id.btPlayControllerExitOk);
			btOk.setText("退出播放");
			btOk.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mVideoView.setVolume(0, 0);
					mVideoView.pause();
					mVideoView.stopPlayback();
					MainApplication.isSingActivityAliving = false;
					activity.finish();
				}
			});
			Button btCancel = (Button) dialog.getWindow().findViewById(R.id.btPlayControllerExitCancel);
			btCancel.setText("重新尝试");
			dialog.getWindow().findViewById(R.id.btPlayControllerExitCancel).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					open(mCurrentMtv);
				}
			});
		}catch(Exception e) {
			KuwoLog.e(TAG, "play mtv, show exit dialog error");
		}
	}
	
	private void initData() {
		// initial the player's accompaniment volume and microphone volume
		mSingLogic = new SingLogic();
		initCommonPlayerVolume();
		 
		mScreenOffReceiver = new ScreenOffBroadcastReceiver();
		mScreenOnReceiver = new ScreenOnBroadcastReceiver();
		IntentFilter screenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		IntentFilter screenOnFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		activity.registerReceiver(mScreenOffReceiver, screenOffFilter);
		activity.registerReceiver(mScreenOnReceiver, screenOnFilter);
	}
	
	private class ScreenOffBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(!TextUtils.isEmpty(action) && action.equals(Intent.ACTION_SCREEN_OFF)) {
				KuwoLog.d(TAG, "screen off");
				if(mVideoView != null && mVideoView.isPlaying()) {
					mVideoView.pause();
				}
			}
		}
		
	}
	
	private class ScreenOnBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(!TextUtils.isEmpty(action) && action.equals(Intent.ACTION_SCREEN_ON)) {
				KuwoLog.d(TAG, "screen on");
				if(mVideoView != null && !mVideoView.isPlaying()) {
					mVideoView.start();
				}
			}
		}
		
	}
	
	private void initView() {		
		ivSingControllerLeft = (ImageView)activity.findViewById(R.id.ivSingControllerLeft);
		ivSingControllerRight = (ImageView)activity.findViewById(R.id.ivSingControllerRight);
		ivSingMenuAccompMtv = (ImageView)activity.findViewById(R.id.ivSingMenuAccompMtv);
		ivSingMenuPlayMtv = (ImageView)activity.findViewById(R.id.ivSingMenuPlayMtv);
		ivSingMenuSlidebar = (ImageView) activity.findViewById(R.id.ivSingMenuSlidebar);
		llSingMtvControllerBarBottom = (LinearLayout)activity.findViewById(R.id.llSingMtvControllerBarBottom);
		
		rl_sing_menu_operate_prompt = (RelativeLayout) activity.findViewById(R.id.rl_sing_menu_operate_prompt);
		rl_sing_menu_operate_prompt.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Animation fadeOutAnimation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_out);
				rl_sing_menu_operate_prompt.startAnimation(fadeOutAnimation);
				rl_sing_menu_operate_prompt.setVisibility(View.GONE);
			}
		}, 8000); //8秒后操作提示消失

		tvSingSingleScore = (TextView) activity.findViewById(R.id.tvSingSingleScore);
		tvSingTotalScore = (TextView) activity.findViewById(R.id.tvSingTotalScore);
		llSingActivityWaiting = (LinearLayout) activity.findViewById(R.id.llSingActivityWaiting);
		llSingActivityWaiting.setVisibility(View.VISIBLE);
		
		
		mVideoView = (KonkaVideoView) activity.findViewById(R.id.konkasurface);
		
		sbSingControllerSeekBar = (SeekBar)activity.findViewById(R.id.sbSingControllerSeekBar);
		tvSingControllerPlayedTime = (TextView) activity.findViewById(R.id.tvSingControllerPlayedTime);
		tvSingControllerTotalTime = (TextView) activity.findViewById(R.id.tvSingControllerTotalTime);
		llSingScore = (ViewGroup)activity.findViewById(R.id.llSingScore);
		llSingScore.setVisibility(View.GONE);
		llSingPlayBufferFail = (LinearLayout) activity.findViewById(R.id.llSingPlayBufferFail);
		llSingPlayBufferFail.setVisibility(View.GONE);
		

		mVideoView.setVideoLayout(2, 0.0f);	//SysVideoView.VIDEO_LAYOUT_STRETCH=2
		
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnErrorListener(this);	
		
		mVideoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case 0: //鼠标左键
					if(!mSingMenuClick) {
						mSingMenuClick = true;
						singMenuTimer.schedule(new SingMenuTimerTask(), MENU_TRANSLATE_DURATION);
						toggleSingMenu(); //呼出菜单或者隐藏菜单
						sendHideMessageForSingMenu(HIDE_MENU_DURATION);
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
		
		llSingVolumeController = (LinearLayout) activity.findViewById(R.id.llSingVolumeController);
		llSingVolumeController.setVisibility(View.INVISIBLE);
		sb_sing_accomp_volume = (SeekBar) activity.findViewById(R.id.sb_sing_accomp_volume);
		sb_sing_accomp_volume.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		sb_sing_microphone_volume = (SeekBar) activity.findViewById(R.id.sb_sing_microphone_volume);
		sb_sing_microphone_volume.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		tv_sing_accomp_volume_prompt = (TextView) activity.findViewById(R.id.tv_sing_accomp_volume_prompt);
		tv_sing_microphone_volume_prompt = (TextView) activity.findViewById(R.id.tv_sing_microphone_volume_prompt);
		ll_sing_accomp_background = (LinearLayout) activity.findViewById(R.id.ll_sing_accomp_background);
		ll_sing_microphone_background = (LinearLayout) activity.findViewById(R.id.ll_sing_microphone_background);
		
		sb_sing_microphone_volume.setProgress((int)(mMicrophoneVolume*100));
		sb_sing_accomp_volume.setProgress((int)(mAccompVolume*100));
		
		ll_sing_play_controller = (LinearLayout) activity.findViewById(R.id.ll_sing_play_controller);
		rlSingCurrentMtvPrompt = (RelativeLayout) activity.findViewById(R.id.rlSingCurrentMtvPrompt);
		rlSingNextMtvPrompt = (RelativeLayout) activity.findViewById(R.id.rlSingNextMtvPrompt);
		tvSingCurrentMtvName = (TextView) activity.findViewById(R.id.tvSingCurrentMtvName);
		tvSingNextMtvName = (TextView) activity.findViewById(R.id.tvSingNextMtvName);
		
		ivSingMenuSwitchMtv = (ImageView) activity.findViewById(R.id.ivSingMenuSwitchMtv);
		ivSingMenuPauseMtv = (ImageView) activity.findViewById(R.id.ivSingMenuPauseMtv);
		ivSingMenuReplayMtv = (ImageView) activity.findViewById(R.id.ivSingMenuReplayMtv);
		ivSingMenuOriginalMtv = (ImageView) activity.findViewById(R.id.ivSingMenuOriginalMtv);
		ivSingMenuScoreMtv = (ImageView) activity.findViewById(R.id.ivSingMenuScoreMtv);
		ivSingMenuOrderedMtv = (ImageView) activity.findViewById(R.id.ivSingMenuOrderedMtv);
		ivSingMenuMtvPlatform = (ImageView) activity.findViewById(R.id.ivSingMenuMtvPlatform);
		flSingOrderedMtvList = (FrameLayout) activity.findViewById(R.id.flSingOrderedMtvList);

		ivSingMenuAccompMtv.setOnClickListener(mOnClickListener);
		ivSingMenuAccompMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuPlayMtv.setOnClickListener(mOnClickListener);
		ivSingMenuPlayMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuSwitchMtv.setOnClickListener(mOnClickListener);
		ivSingMenuSwitchMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuPauseMtv.setOnClickListener(mOnClickListener);
		ivSingMenuPauseMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuReplayMtv.setOnClickListener(mOnClickListener);
		ivSingMenuReplayMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuOriginalMtv.setOnClickListener(mOnClickListener);
		ivSingMenuOriginalMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuScoreMtv.setOnClickListener(mOnClickListener);
		ivSingMenuScoreMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuOrderedMtv.setOnClickListener(mOnClickListener);
		ivSingMenuOrderedMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		ivSingMenuMtvPlatform.setOnClickListener(mOnClickListener);
		ivSingMenuMtvPlatform.setOnFocusChangeListener(mOnFocusChangeListener);
		

		mOrderedMtvFragment = new PlayOrderedMtvFragment();
		mTransaction = ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
		mTransaction.replace(R.id.flSingOrderedMtvList, mOrderedMtvFragment).commit();

		
		flSingOrderedMtvList.setVisibility(View.INVISIBLE);
		ll_sing_play_controller.setVisibility(View.INVISIBLE);
		
		createBitmap();
		ViewTreeObserver vto = ivSingMenuSlidebar.getViewTreeObserver();  
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  

            @Override  

            public void onGlobalLayout() {  

            	ivSingMenuSlidebar.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
        		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        				(int) (AppContext.SCREEN_WIDTH*(50*1.0f/1920)), 
        				(int) (AppContext.SCREEN_HIGHT*(200*1.0f/1080)));
        		ivSingControllerLeft.setLayoutParams(params);
        		ivSingControllerRight.setLayoutParams(params);
        		mSlideBarWidth = (int) (AppContext.SCREEN_WIDTH*(260*1.0f/1920));
            	mSlideBarHeight = (int) (AppContext.SCREEN_HIGHT*(200*1.0f/1080));
            	
            	RelativeLayout.LayoutParams slideBarParams = new RelativeLayout.LayoutParams(mSlideBarWidth, mSlideBarHeight);
            	ivSingMenuSlidebar.setLayoutParams(slideBarParams);
            	
            	setMenuMeasure(ivSingMenuOriginalMtv, true);
            	setMenuMeasure(ivSingMenuAccompMtv, true);
            	setMenuMeasure(ivSingMenuReplayMtv, false);
            	setMenuMeasure(ivSingMenuScoreMtv, false);
            	setMenuMeasure(ivSingMenuPauseMtv, true);
            	setMenuMeasure(ivSingMenuPlayMtv, true);
            	setMenuMeasure(ivSingMenuSwitchMtv, false);
            	setMenuMeasure(ivSingMenuOrderedMtv, false);
            	setMenuMeasure(ivSingMenuMtvPlatform, false);
            	
            	updateMenuUI(true, ivSingMenuAccompMtv, mBitmapAccomp);
            	updateMenuUI(true, ivSingMenuOriginalMtv, mBitmapOriginal);
            	updateMenuUI(true, ivSingMenuReplayMtv, mBitmapReplay);
            	updateMenuUI(true, ivSingMenuScoreMtv, mBitmapScore);
            	updateMenuUI(true, ivSingMenuPauseMtv, mBitmapPause);
            	updateMenuUI(true, ivSingMenuPlayMtv, mBitmapPlay);
            	updateMenuUI(true, ivSingMenuSwitchMtv, mBitmapSwitch);
            	updateMenuUI(true, ivSingMenuOrderedMtv, mBitmapOrdered);
            	updateMenuUI(true, ivSingMenuMtvPlatform, mBitmapPlatform);
            	
            	updatePausePlay();
            }  

        });  
	}
	
	private void createBitmap() {
		//create bitmap
		mBitmapAccomp = BitmapTools.createBitmapByInputstream(activity, R.drawable.accomp_mtv, mSlideBarWidth, mSlideBarHeight);
		mBitmapOrdered = BitmapTools.createBitmapByInputstream(activity, R.drawable.sing_ordered_mtv, mSlideBarWidth, mSlideBarHeight);
		mBitmapOriginal = BitmapTools.createBitmapByInputstream(activity, R.drawable.original_normal, mSlideBarWidth, mSlideBarHeight);
		mBitmapPause = BitmapTools.createBitmapByInputstream(activity, R.drawable.pause_mtv, mSlideBarWidth, mSlideBarHeight);
		mBitmapPlatform = BitmapTools.createBitmapByInputstream(activity, R.drawable.sing_platform, mSlideBarWidth, mSlideBarHeight);
		mBitmapPlay = BitmapTools.createBitmapByInputstream(activity, R.drawable.play_mtv_normal, mSlideBarWidth, mSlideBarHeight);
		mBitmapReplay = BitmapTools.createBitmapByInputstream(activity, R.drawable.replay_mtv, mSlideBarWidth, mSlideBarHeight);
		mBitmapScore = BitmapTools.createBitmapByInputstream(activity, R.drawable.score_mtv, mSlideBarWidth, mSlideBarHeight);
		mBitmapSwitch = BitmapTools.createBitmapByInputstream(activity, R.drawable.switch_mtv, mSlideBarWidth, mSlideBarHeight);
		
		mBitmapAccompPressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.accomp_mtv_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapOrderedPressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.sing_ordered_mtv_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapOriginalPressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.original_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapPausePressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.pause_mtv_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapPlatformPressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.sing_platform_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapPlayPressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.play_mtv_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapReplayPressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.replay_mtv_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapScorePressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.score_mtv_pressed, mSlideBarWidth, mSlideBarHeight);
		mBitmapSwitchPressed = BitmapTools.createBitmapByInputstream(activity, R.drawable.switch_mtv_pressed, mSlideBarWidth, mSlideBarHeight);
		
		mBitmapScoreDisable = BitmapTools.createBitmapByInputstream(activity, R.drawable.score_mtv_disable, mSlideBarWidth, mSlideBarHeight);
	}
	
	private void setMenuMeasure(ImageView ivSingMenu, boolean bFrameLayoutParams) {
		if(bFrameLayoutParams) {
			FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(mSlideBarWidth, mSlideBarHeight);
			ivSingMenu.setLayoutParams(frameLayoutParams);
		}else {
			LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(mSlideBarWidth, mSlideBarHeight);
			ivSingMenu.setLayoutParams(linearLayoutParams);
		}
	}
	
	private OrderSerializeLogic lgcOrder = OrderSerializeLogic.getInstance();
	private void processLogic() {
		lgcOrder.setOnCurrentMtvChangedListener(this);
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(ll_sing_play_controller.getVisibility() == View.INVISIBLE) {
				showSingMenu();
				return;
			}
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			switch (v.getId()) { 
			case R.id.ivSingMenuSwitchMtv: //切歌
				long lCurTime = System.currentTimeMillis();
				if (lCurTime-lLastSwitchNextTime<=SWITCH_DURATION){
					lLastSwitchNextTime = lCurTime;
					return;				
				}
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_BUTTON_NEXT, Constants.KS_UMENG_SUCCESS);
				openNext();
				llSingActivityWaiting.setVisibility(View.VISIBLE);
				break;
			case R.id.ivSingMenuPauseMtv: //暂停
				clickPlayOrPause();
				break;
			case R.id.ivSingMenuPlayMtv:
				clickPlayOrPause();
				break;
			case R.id.ivSingMenuOriginalMtv: //原唱
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_BUTTON_SWITCH_ORI, Constants.KS_UMENG_SUCCESS);
				switchAccomp();
				break;
			case R.id.ivSingMenuAccompMtv: //伴唱
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_BUTTON_SWITCH_ACC, Constants.KS_UMENG_SUCCESS);
				switchAccomp();
				break;
			case R.id.ivSingMenuReplayMtv: //重唱
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_BUTTON_RESING, Constants.KS_UMENG_SUCCESS);
				mVideoView.seekTo(0);
				break;
			case R.id.ivSingMenuScoreMtv: //打分
						MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_BUTTON_SCORE, Constants.KS_UMENG_SUCCESS);
				setScore();
				break;
			case R.id.ivSingMenuOrderedMtv:  //已点
				if(!mOrderedMtvClick) {
					mOrderedMtvClick = true;
					orderedMtvTimer.schedule(new OrderedMtvTimerTask(), MENU_TRANSLATE_DURATION);
					if(flSingOrderedMtvList.getVisibility() == View.INVISIBLE) {
						MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_BUTTON_ORDERED, Constants.KS_UMENG_SUCCESS);
					}
					ll_sing_play_controller.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.player_bottom_out));
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							ll_sing_play_controller.setVisibility(View.INVISIBLE); 
						}
					}, MENU_TRANSLATE_DURATION);
					toggleOrderedMtvFragment();
				}
				break;
			case R.id.ivSingMenuMtvPlatform:  //点歌台
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_BUTTON_DIANGETAI, Constants.KS_UMENG_SUCCESS);
				showMtvPlatform();
				break;
			default:
				break;
			}
		}
	};
	
	private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			KuwoLog.d(TAG, "focusChange hasFocus="+hasFocus);
			int slidebarWidth = ivSingMenuSlidebar.getWidth();
			if(hasFocus) {
				int originalX = ivSingMenuSlidebar.getLeft();
				switch (v.getId()) {
				case R.id.ivSingMenuOriginalMtv: //原唱
					if(!mCurrentMtv.hasEcho) 
						DialogUtils.toast("该歌曲暂无伴唱资源", false, Gravity.CENTER);
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX, originalX, 0, 0);
					mFromX = originalX;
					mCurrentSingMenu = ivSingMenuOriginalMtv;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapOriginalPressed);
					break;
				case R.id.ivSingMenuAccompMtv: //伴唱
					if(!mCurrentMtv.hasEcho) 
						DialogUtils.toast("该歌曲暂无伴唱资源", false, Gravity.CENTER);
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX, originalX, 0, 0);
					mFromX = originalX;
					mCurrentSingMenu = ivSingMenuAccompMtv;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapAccompPressed);
					break;
				case R.id.ivSingMenuScoreMtv:  //打分
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX, originalX+slidebarWidth*1, 0, 0);
					mFromX = originalX+slidebarWidth*1;
					mCurrentSingMenu = ivSingMenuScoreMtv;
					
					if(!mCurrentMtv.hasKdatx) {
						DialogUtils.toast("该歌曲暂不支持打分", false, Gravity.CENTER);
						llSingScore.setVisibility(View.GONE);
						ivSingMenuScoreMtv.setEnabled(false);
						updateMenuUI(false, mCurrentSingMenu, mBitmapScoreDisable);
					}else {
						updateMenuUI(false, mCurrentSingMenu, mBitmapScorePressed);
					}
					break;
				case R.id.ivSingMenuReplayMtv: //重唱
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX, originalX+slidebarWidth*2, 0, 0);
					mFromX = originalX+slidebarWidth*2;
					mCurrentSingMenu = ivSingMenuReplayMtv;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapReplayPressed);
					break;
				case R.id.ivSingMenuPauseMtv: //暂停
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX,originalX+slidebarWidth*3, 0, 0);
					mFromX = originalX+slidebarWidth*3;
					mCurrentSingMenu = ivSingMenuPauseMtv;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapPausePressed);
					break;
				case R.id.ivSingMenuPlayMtv: //播放
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX,originalX+slidebarWidth*3, 0, 0);
					mFromX = originalX+slidebarWidth*3;
					mCurrentSingMenu = ivSingMenuPlayMtv;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapPlayPressed);
					break;
				case R.id.ivSingMenuSwitchMtv: //切歌
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX,originalX+slidebarWidth*4, 0, 0);
					mFromX = originalX+slidebarWidth*4;
					mCurrentSingMenu = ivSingMenuSwitchMtv;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapSwitchPressed);
					break;
				case R.id.ivSingMenuOrderedMtv:  //已点
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX, originalX+slidebarWidth*5, 0, 0);
					mFromX = originalX+slidebarWidth*5;
					mCurrentSingMenu = ivSingMenuOrderedMtv;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapOrderedPressed);
					break;
				case R.id.ivSingMenuMtvPlatform:  //点歌台
					CommonAnimation.translate(ivSingMenuSlidebar, SLIDEBAR_TRANSLATE_DURATION, true, mFromX, originalX+slidebarWidth*6, 0, 0);
					mFromX = originalX+slidebarWidth*6;
					mCurrentSingMenu = ivSingMenuMtvPlatform;
					
					updateMenuUI(false, mCurrentSingMenu, mBitmapPlatformPressed);
					break;
				default:
					break;
				}
			}else {
				//失去焦点时
				switch (v.getId()) {
				case R.id.ivSingMenuOriginalMtv: //原唱
					updateMenuUI(true, ivSingMenuOriginalMtv, mBitmapOriginal);
					break;
				case R.id.ivSingMenuAccompMtv: //伴唱
					updateMenuUI(true, ivSingMenuAccompMtv, mBitmapAccomp);
					break;
				case R.id.ivSingMenuScoreMtv:  //打分
					updateMenuUI(true, ivSingMenuScoreMtv, mBitmapScore);
					break;
				case R.id.ivSingMenuReplayMtv: //重唱
					updateMenuUI(true, ivSingMenuReplayMtv, mBitmapReplay);
					break;
				case R.id.ivSingMenuPauseMtv: //暂停
					updateMenuUI(true, ivSingMenuPauseMtv, mBitmapPause);
					break;
				case R.id.ivSingMenuPlayMtv:
					updateMenuUI(true, ivSingMenuPlayMtv, mBitmapPlay);
					break;
				case R.id.ivSingMenuSwitchMtv: //切歌
					updateMenuUI(true, ivSingMenuSwitchMtv, mBitmapSwitch);
					break;
				case R.id.ivSingMenuOrderedMtv:  //已点
					updateMenuUI(true, ivSingMenuOrderedMtv, mBitmapOrdered);
					break;
				case R.id.ivSingMenuMtvPlatform:  //点歌台
					updateMenuUI(true, ivSingMenuMtvPlatform, mBitmapPlatform);
					break;
				default:
					break;
				}
				
			}
		}
	};
	
	private void updateMenuUI(boolean changeStatusNow, final ImageView iv, final Bitmap bm) {
		if(changeStatusNow) {
			iv.setImageBitmap(bm);
		}else {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if(mCurrentSingMenu == iv)
						iv.setImageBitmap(bm);
				}
			}, SLIDEBAR_TRANSLATE_DURATION);
		}
	}
	
	private OnSeekBarChangeListener mOnSeekBarChangeListener = new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			switch (seekBar.getId()) {
			case R.id.sb_sing_accomp_volume:
				// 改变伴唱音量
				setMVVolume(progress);
				break;
			case R.id.sb_sing_microphone_volume:
				// 改变麦克风音量
				setMicVolume(progress);
				break;
			default:
				break;
			}
		}
	};
	
	private Handler mHideKeyboardHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HIDE_KEYBOARD_MSG_WHAT:
				if(activity.getCurrentFocus() == activity.findViewById(R.id.lvOrderedMtv)) 
					break;
//				if(flSingOrderedMtvList.getVisibility() == View.VISIBLE)
//					hideOrderedMtv();
				if(ll_sing_play_controller.getVisibility() == View.VISIBLE) 
					hideSingMenu();
				break;
			default:
				break;
			}
		}
		
	};
	
	private Handler mHideVolumeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HIDE_VOLUME_DIALOG_WHAT:
				if(llSingVolumeController.getVisibility() == View.VISIBLE) 
					hideAccompAndMicphoneVolumeDialog();
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			if(flSingOrderedMtvList.getVisibility() == View.VISIBLE)
				hideOrderedMtv();
			if(llSingVolumeController.getVisibility() == View.VISIBLE)
				hideAccompAndMicphoneVolumeDialog();
			if(!mSingMenuClick) {
				mSingMenuClick = true;
				singMenuTimer.schedule(new SingMenuTimerTask(), MENU_TRANSLATE_DURATION);
				toggleSingMenu(); //呼出菜单或者隐藏菜单
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			if (flSingOrderedMtvList.getVisibility() == View.VISIBLE && !mOrderedMtvClick) {
				mOrderedMtvClick = true;
				orderedMtvTimer.schedule(new OrderedMtvTimerTask(), MENU_TRANSLATE_DURATION);
				hideOrderedMtv();
				showSingMenu();
				mCurrentSingMenu.requestFocus();
			}else if (ll_sing_play_controller.getVisibility() == View.VISIBLE && !mSingMenuClick) {
				mSingMenuClick = true;
				singMenuTimer.schedule(new SingMenuTimerTask(), MENU_TRANSLATE_DURATION);
				hideSingMenu();
			}else if(llSingVolumeController.getVisibility() == View.VISIBLE) {
				hideAccompAndMicphoneVolumeDialog();
			}else {
				showExitDialog(); //退出演唱
//					finishSingActivity(keyCode,event); //再按一次返回退出
//					activity.finish();
			}
			return true;
		//8秒内木有操作就隐藏menu	
		case KeyEvent.KEYCODE_DPAD_UP:
			if(llSingVolumeController.getVisibility() == View.VISIBLE ) {
				if(activity.getCurrentFocus() == sb_sing_accomp_volume) {
					ll_sing_microphone_background.setBackgroundResource(R.drawable.volume_background_shape);
					ll_sing_accomp_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
					sb_sing_microphone_volume.requestFocus();
				}else {
					ll_sing_microphone_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
					ll_sing_accomp_background.setBackgroundResource(R.drawable.volume_background_shape);
					sb_sing_accomp_volume.requestFocus();
				}
				sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				return true;
			}
			
			if(flSingOrderedMtvList.getVisibility() >= View.INVISIBLE 
			&& ll_sing_play_controller.getVisibility() >= View.INVISIBLE
			&& llSingVolumeController.getVisibility() >= View.INVISIBLE
			&& !hasFirstClickLeftButton
			&& !hasFirstClickRightButton) {
				showAccompVolumeDialog();
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_HOTKEY_VOLUME, Constants.KS_UMENG_SUCCESS);
				sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				return true;
			}
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(llSingVolumeController.getVisibility() == View.VISIBLE) {
				if(activity.getCurrentFocus() == sb_sing_microphone_volume) {
					ll_sing_microphone_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
					ll_sing_accomp_background.setBackgroundResource(R.drawable.volume_background_shape);
					sb_sing_accomp_volume.requestFocus();
				}else {
					ll_sing_accomp_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
					ll_sing_microphone_background.setBackgroundResource(R.drawable.volume_background_shape);
					sb_sing_microphone_volume.requestFocus();
				}
				sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				return true;
			}
			
			if(flSingOrderedMtvList.getVisibility() >= View.INVISIBLE 
			&& ll_sing_play_controller.getVisibility() >= View.INVISIBLE
			&& llSingVolumeController.getVisibility() >= View.INVISIBLE
			&& !hasFirstClickLeftButton
			&& !hasFirstClickRightButton) {
				showMicphoneVolumeDialog();
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_HOTKEY_VOLUME, Constants.KS_UMENG_SUCCESS);
				sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				return true;
			}
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(activity.getCurrentFocus() == ll_sing_microphone_background) {
				sb_sing_accomp_volume.requestFocus();
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				break;
			}
			if(flSingOrderedMtvList.getVisibility() == View.INVISIBLE 
			&& ll_sing_play_controller.getVisibility() == View.INVISIBLE 
			&& llSingVolumeController.getVisibility() == View.INVISIBLE) {
				if(hasFirstClickLeftButton) {
					//open pre song
					long lCurTime = System.currentTimeMillis();
					if (lCurTime-lLastSwitchPreTime<=SWITCH_DURATION){
						lLastSwitchPreTime = lCurTime;
						sendHideMessageForSingMenu(HIDE_MENU_DURATION);
						break;				
					}
					openPre();
					MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_HOTKEY_PRE, Constants.KS_UMENG_SUCCESS);
				}else {
					SpannableString spanString = new SpannableString("再按一次 左 方向键切至上一首");
					spanString.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					DialogUtils.toast(spanString, false);
					hasFirstClickLeftButton = true;
					switchPreSongTimer.schedule(new SwitchPreSongTask(), 2000);
				}
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				break;
			}
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:  
			if(activity.getCurrentFocus() == ll_sing_microphone_background) {
				sb_sing_accomp_volume.requestFocus();
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				break;
			}
			if(flSingOrderedMtvList.getVisibility() == View.INVISIBLE 
			&& ll_sing_play_controller.getVisibility() == View.INVISIBLE
			&& llSingVolumeController.getVisibility() == View.INVISIBLE) {
				if(hasFirstClickRightButton) {
					//open next song
					long lCurTime = System.currentTimeMillis();
					if (lCurTime-lLastSwitchNextTime<=SWITCH_DURATION){
						lLastSwitchNextTime = lCurTime;
						sendHideMessageForSingMenu(HIDE_MENU_DURATION);
						break;				
					}
					MobclickAgent.onEvent(activity, Constants.KS_UMENG_CONTROL_HOTKEY_NEXT, Constants.KS_UMENG_SUCCESS);
					openNext();
				}else {
					SpannableString spanString = new SpannableString("再按一次 右 方向键切至下一首");
					spanString.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					DialogUtils.toast(spanString, false);
					hasFirstClickRightButton = true;
					switchNextSongTimer.schedule(new SwitchNextSongTask(), 2000);
				}
				sendHideMessageForSingMenu(HIDE_MENU_DURATION);
				break;
			}
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if(llSingVolumeController.getVisibility() == View.VISIBLE)
				break;
			if(ll_sing_play_controller.getVisibility() == View.INVISIBLE) 
				showSingMenu();
			sendHideMessageForSingMenu(HIDE_MENU_DURATION);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showAccompVolumeDialog() {
		ll_sing_microphone_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
		ll_sing_accomp_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
		Animation fadeInAnimation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
		llSingVolumeController.startAnimation(fadeInAnimation);
		llSingVolumeController.setVisibility(View.VISIBLE);
		ll_sing_accomp_background.setBackgroundResource(R.drawable.volume_background_shape);
		sb_sing_accomp_volume.requestFocus();
	}
	
	private void showMicphoneVolumeDialog() {
		ll_sing_microphone_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
		ll_sing_accomp_background.setBackgroundColor(activity.getResources().getColor(color.transparent));
		Animation fadeInAnimation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
		llSingVolumeController.startAnimation(fadeInAnimation);
		llSingVolumeController.setVisibility(View.VISIBLE);
		ll_sing_microphone_background.setBackgroundResource(R.drawable.volume_background_shape);
		sb_sing_microphone_volume.requestFocus();
	}
	
	private void hideAccompAndMicphoneVolumeDialog() {
		Animation fadeOutAnimation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_out);
		llSingVolumeController.startAnimation(fadeOutAnimation);
		llSingVolumeController.setVisibility(View.INVISIBLE);
	}

	private void showSingMenu() {
		ll_sing_play_controller.setVisibility(View.VISIBLE);
		llSingMtvControllerBarBottom.setVisibility(View.VISIBLE);
		ll_sing_play_controller.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.player_bottom_in));
		
		if(mCurrentSingMenu != null) {
			mCurrentSingMenu.requestFocus();
		}else {
			if(mVideoView.isPlaying()) {
				ivSingMenuPlayMtv.setVisibility(View.INVISIBLE);
				ivSingMenuPauseMtv.setVisibility(View.VISIBLE);
				mCurrentSingMenu = ivSingMenuPauseMtv;
				ivSingMenuPauseMtv.requestFocus();
			}else {
				ivSingMenuPauseMtv.setVisibility(View.INVISIBLE);
				ivSingMenuPlayMtv.setVisibility(View.VISIBLE);
				mCurrentSingMenu = ivSingMenuPlayMtv;
				ivSingMenuPlayMtv.requestFocus();
			}
		}
	}
	
	private void hideSingMenu() {
		ll_sing_play_controller.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.player_bottom_out));
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				ll_sing_play_controller.setVisibility(View.INVISIBLE); 
				//回收bitmap
				mVideoView.requestFocus();
			}
		}, MENU_TRANSLATE_DURATION);
	}
	
	private void showOrderedMtv() {
		flSingOrderedMtvList.setVisibility(View.VISIBLE);
//		flSingOrderedMtvList.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_in));
		flSingOrderedMtvList.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.ordered_mtv_in));
		flSingOrderedMtvList.requestFocus();
		Message msg = new Message();
		msg.what = Constants.MSG_SHOW_ORDERED_MTV_IN_PLAY;
		EventBus.getDefault().post(msg);
	}
	
	private void hideOrderedMtv() {
//		flSingOrderedMtvList.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_out));
		flSingOrderedMtvList.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.ordered_mtv_out));
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				flSingOrderedMtvList.setVisibility(View.INVISIBLE);
			}
		}, MENU_TRANSLATE_DURATION);
	}
	
	/**
	 * 按两次‘返回键’退出演唱场景
	 */
	private void finishSingActivity(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			if(mHasFirstClick) {
				activity.finish();
			}else {
				DialogUtils.toast("再按一次'返回键'退出演唱", false);
				mHasFirstClick = true;
				//exitTimer.schedule(new ExitTimerTask(), 2000); //2秒后重置标记
			}
		}
	}
	
	private void sendHideMessageForSingMenu(int duration) {
		mHideKeyboardHandler.removeMessages(HIDE_KEYBOARD_MSG_WHAT);
		mHideKeyboardHandler.sendEmptyMessageDelayed(HIDE_KEYBOARD_MSG_WHAT, duration); //延迟8秒发送隐藏指令
	}
	
	private void sendHideVolumeDialogMessage(int duration) {
		mHideVolumeHandler.removeMessages(HIDE_VOLUME_DIALOG_WHAT);
		mHideVolumeHandler.sendEmptyMessageDelayed(HIDE_VOLUME_DIALOG_WHAT, duration);
	}
	
	/**
	 * 退出对话框
	 */
	private void showExitDialog() {
		try {
			final AlertDialog dialog = new AlertDialog.Builder(activity).create();
			dialog.show();
			dialog.getWindow().setContentView(R.layout.custom_alert_dialog);
			String exitPromptText = "您确定要退出演唱么？";
			TextView tvPlayControllerExitPrompt = (TextView) dialog.getWindow().findViewById(R.id.tvPlayControllerExitPrompt);
			tvPlayControllerExitPrompt.setText(exitPromptText);
			dialog.getWindow().findViewById(R.id.btPlayControllerExitOk).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mVideoView.setVolume(0, 0);
					mVideoView.pause();
					mVideoView.stopPlayback();
					MainApplication.isSingActivityAliving = false;
					activity.finish();
				}
			});
			dialog.getWindow().findViewById(R.id.btPlayControllerExitCancel).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}catch(Exception e) {
			KuwoLog.e(TAG, "play mtv, show exit dialog error");
		}
	}
	
	private void toggleSingMenu() {
		if(ll_sing_play_controller.getVisibility() == View.VISIBLE) {
			hideSingMenu();
			if(flSingOrderedMtvList.getVisibility() == View.VISIBLE ) {
				hideOrderedMtv();
			}
		}else {
			showSingMenu();
		}
	}
	
	private void toggleOrderedMtvFragment() {
		if(flSingOrderedMtvList.getVisibility() == View.INVISIBLE) {
			//如果隐藏，则显示
			showOrderedMtv();
		}else {
			//如果显示，则隐藏
			hideOrderedMtv();
		}
	}
	
	private void showMtvPlatform() {
		SharedPreferences sp = activity.getSharedPreferences("kuwoTV", Context.MODE_PRIVATE);
		boolean hasActivated = sp.getBoolean("hasActivatedSingActivity", false);
		if(!hasActivated) {
			//给出提示并且保存激活状态为true
			SpannableStringBuilder sb = new SpannableStringBuilder();
			sb.append("按  返回键 回到演唱哦");
			sb.setSpan(new ForegroundColorSpan(Color.YELLOW), 2, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			DialogUtils.toast(sb, true);
			Editor editor = sp.edit();
			editor.putBoolean("hasActivatedSingActivity", true);
			editor.commit();
		}

		if(flSingOrderedMtvList.getVisibility() == View.VISIBLE)
			hideOrderedMtv();
		if(ll_sing_play_controller.getVisibility() == View.VISIBLE)
			hideSingMenu();
		//post a message to event bus
		Message msg = new Message();
		msg.what = Constants.MSG_CLOSE_SECOND_PAGE;
		EventBus.getDefault().post(msg);
		Message stopSingControllerMsg = new Message();
		stopSingControllerMsg.what = Constants.MSG_STOP_SING_CONTROLLER;
		EventBus.getDefault().post(stopSingControllerMsg);
		Intent intent = new Intent(activity, ItemListActivity.class);
		activity.startActivity(intent);
	}
	
	public void open(Mtv mtv) {
		KuwoLog.d(TAG, "open mtv");
		if(!AppContext.getNetworkSensor().hasAvailableNetwork()) {
			processNetworkUnavailable();
			return;
		}

		mCurrentPosition = 0;
		mbSwitchAcc = false;
		mSwitchedTrack = false;
		llSingActivityWaiting.setVisibility(View.VISIBLE);
		if (mtv == null)
		{
			MobclickAgent.onEvent(activity, Constants.KS_UMENG_OPEN_MUSIC, Constants.KS_UMENG_FAIL);
			return;
		}
		MobclickAgent.onEvent(activity, Constants.KS_UMENG_OPEN_MUSIC, Constants.KS_UMENG_SUCCESS);
		
		//发送消息，告诉歌词控制器LyricController去获取歌词
		Message msg = new Message();
		msg.what = Constants.MSG_OPEN_SONG;
		msg.obj = mtv;
		EventBus.getDefault().post(msg);
		
		//视图控制
		llSingScore.setVisibility(View.GONE);
		rlSingCurrentMtvPrompt.setVisibility(View.VISIBLE);
		rlSingNextMtvPrompt.setVisibility(View.VISIBLE);
		sendHideMessageForSingMenu(HIDE_MENU_DURATION_FIRST); //无操作4秒后隐藏
		mCurrentMtv = mtv;
		tvSingCurrentMtvName.setText(mtv.name);
		OrderSerializeLogic orderSerializaLogic = OrderSerializeLogic.getInstance();
		Mtv next = orderSerializaLogic.peekMtv();
		if (next != null)
			tvSingNextMtvName.setText(next.name);
		tvSingSingleScore.setText("0");
		tvSingTotalScore.setText("0");
		llSingPlayBufferFail.setVisibility(View.GONE);
 		if(mtv.hasEcho){ //有伴唱资源[显示原唱按钮，播放伴唱]
 			if(PreferencesManager.getInt(Constants.MTV_ORI_ACCOMP, 2) == 2) {
 				ivSingMenuAccompMtv.setVisibility(View.INVISIBLE);
 	 			ivSingMenuOriginalMtv.setVisibility(View.VISIBLE);
 	 			ivSingMenuAccompMtv.setEnabled(true);
 	 			ivSingMenuOriginalMtv.setEnabled(true);
 	 			if(mCurrentSingMenu == ivSingMenuAccompMtv || mCurrentSingMenu == ivSingMenuOriginalMtv)
 	 				ivSingMenuOriginalMtv.requestFocus();
 			}else {
 				ivSingMenuOriginalMtv.setVisibility(View.INVISIBLE);
 	 			ivSingMenuAccompMtv.setVisibility(View.VISIBLE);
 	 			ivSingMenuOriginalMtv.setEnabled(true);
 	 			ivSingMenuAccompMtv.setEnabled(true);
 	 			if(mCurrentSingMenu == ivSingMenuAccompMtv || mCurrentSingMenu == ivSingMenuOriginalMtv)
 	 				ivSingMenuAccompMtv.requestFocus();
 			}
 			
 			sendMsgToService(MessageCommons.RESULT_TO_SWITCH_ORIGINAL);
		}else {
			ivSingMenuOriginalMtv.setVisibility(View.INVISIBLE);
 			ivSingMenuAccompMtv.setVisibility(View.VISIBLE);
 			ivSingMenuOriginalMtv.setEnabled(false);
 			ivSingMenuAccompMtv.setEnabled(false);
 			if(mCurrentSingMenu == ivSingMenuAccompMtv || mCurrentSingMenu == ivSingMenuOriginalMtv)
 				ivSingMenuAccompMtv.requestFocus();
 			sendMsgToService(MessageCommons.RESULT_TO_SWITCH_ACCOMP);
		}
		if(mtv.hasKdatx) { //有练唱图资源
			ivSingMenuScoreMtv.setEnabled(true);
			updateMenuUI(true, ivSingMenuScoreMtv,mBitmapScore);
		}else {
			llSingScore.setVisibility(View.GONE);
			ivSingMenuScoreMtv.setEnabled(false);
			updateMenuUI(true, ivSingMenuScoreMtv, mBitmapScoreDisable);
		}
		KuwoLog.d(TAG, "before pause");
		mVideoView.pause();
		KuwoLog.d(TAG, "after pause");
		mVideoView.setVolume(0, 0);
		KuwoLog.d(TAG, "after setVolume");
		mVideoView.stopPlayback();
		KuwoLog.d(TAG, "after stopPlayback");
		setVideoURI(mtv);
	}
	
	private void setVideoURI(Mtv mtv) {
		mlistLogic.getMtvUrl(mtv.rid, new DataHandler<String>() {

			@Override
			public void onSuccess(String data) {
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_GET_SONGURL, Constants.KS_UMENG_SUCCESS);
				KuwoLog.d(TAG, "video url="+data);
				mVideoView.setVideoURI(Uri.parse(data));
				KuwoLog.d(TAG, "onSuccess: " + data);
			}
			
			@Override
			public void onFailure() {
				super.onFailure();
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_GET_SONGURL, Constants.KS_UMENG_FAIL);
				openNext();
			}
		});
	}
	
	public synchronized void openPre() {
		try {
			OrderSerializeLogic.getInstance().preMtv();
		} catch (Exception e) {
			KuwoLog.printStackTrace(e);
		}
	}
	
	public synchronized void openNext() {
		try {
			OrderSerializeLogic.getInstance().nextMtv();
		} catch (Exception e) {
			KuwoLog.printStackTrace(e);
		}
	}
	
	private void clickPlayOrPause() {
		try {
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
//				DialogUtils.toast("暂停播放", false);
			} else {
				mVideoView.start();
//				DialogUtils.toast("继续播放", false);
			}
			updatePausePlay();
		} catch (Exception e) {
			KuwoLog.printStackTrace(e);
		}
	}
	
	private void switchAccomp() {
		mbSwitchAcc = true;
		//1：为原唱  2：为伴唱
		if (mVideoView.getAudioTrack() == 1){
					mVideoView.setAudioTrack(2);
			
			PreferencesManager.put(Constants.MTV_ORI_ACCOMP, 2).commit();
			ivSingMenuAccompMtv.setVisibility(View.INVISIBLE);
			ivSingMenuOriginalMtv.setVisibility(View.VISIBLE);
			ivSingMenuOriginalMtv.requestFocus();
			DialogUtils.toast("以后自动播放伴唱", false);
		}else {
			mVideoView.setAudioTrack(1);
			PreferencesManager.put(Constants.MTV_ORI_ACCOMP, 1).commit();
			ivSingMenuOriginalMtv.setVisibility(View.INVISIBLE);
			ivSingMenuAccompMtv.setVisibility(View.VISIBLE);
			ivSingMenuAccompMtv.requestFocus();
			DialogUtils.toast("以后自动播放原唱", false);
		}
		mVideoView.setVolume(mAccompVolume, mAccompVolume);
		//mVideoView.seekTo((int) currentPosition);
	}
	private void setScore(){
		if(mCurrentMtv.hasKdatx) {
			if (llSingScore.getVisibility() != View.VISIBLE) {
				llSingScore.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_in));
				llSingScore.setVisibility(View.VISIBLE);
			}else {
				llSingScore.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.fade_out));
				llSingScore.setVisibility(View.GONE);
			}
		}else {
			llSingScore.setVisibility(View.GONE);
			ivSingMenuScoreMtv.setEnabled(false);
			updateMenuUI(true, ivSingMenuScoreMtv, mBitmapScoreDisable);
		}
	}
	private void setMVVolume(int progress){
		mAccompVolume = progress*0.01f;
		SharedPreferences accompVolumeSp = activity.getSharedPreferences("kuwoTV", Context.MODE_PRIVATE);
		Editor accompVolumeEditor = accompVolumeSp.edit();
		accompVolumeEditor.putFloat("accompVolume", mAccompVolume);
		accompVolumeEditor.commit();
		mVideoView.setVolume(mAccompVolume, mAccompVolume);
		tv_sing_accomp_volume_prompt.setText(progress+"");
		sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
	}
	private void setMicVolume(int progress){
		mMicrophoneVolume = progress*0.010f;
		SharedPreferences micphoneVolumeSp = activity.getSharedPreferences("kuwoTV", Context.MODE_PRIVATE);
		Editor micphoneVolumeEditor = micphoneVolumeSp.edit();
		micphoneVolumeEditor.putFloat("microphoneVolume", mMicrophoneVolume);
		micphoneVolumeEditor.commit();
		mSingLogic.setVolume(activity, mMicrophoneVolume);
		tv_sing_microphone_volume_prompt.setText(progress+"");
		sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
	}
	
	private void initCommonPlayerVolume() {
		SharedPreferences sp = activity.getSharedPreferences("kuwoTV", Context.MODE_PRIVATE);
		mAccompVolume = sp.getFloat("accompVolume", 0.8f);
		mMicrophoneVolume = sp.getFloat("microphoneVolume", 0.8f);
		mSingLogic.setVolume(activity, mMicrophoneVolume);
		if (mVideoView != null)
			mVideoView.setVolume(mAccompVolume, mAccompVolume);
	}
	
	private void updatePausePlay() {
		if (mVideoView.isPlaying()) {
			ivSingMenuPlayMtv.setVisibility(View.INVISIBLE);
			ivSingMenuPauseMtv.setVisibility(View.VISIBLE);
			if(mCurrentSingMenu == ivSingMenuPlayMtv)
				ivSingMenuPauseMtv.requestFocus();
		}else {
			ivSingMenuPauseMtv.setVisibility(View.INVISIBLE);
			ivSingMenuPlayMtv.setVisibility(View.VISIBLE);
			if(mCurrentSingMenu == ivSingMenuPauseMtv)
				ivSingMenuPlayMtv.requestFocus();
		}
	}
	
//			if (!mSwitchedTrack && mUseSysVideoView)
//			android.media.MediaPlayer.TrackInfo[] ti =  mVideoView.getTrackInfo();
//			mTrackNum = ti.length;
			
			

	



	

	

	

	
	/** 是否需要自动恢复播放，用于自动暂停，恢复播放 */
    private boolean needResume;
    
    		
    
    		
    			
    
    
	private boolean isPlaying() {
		boolean result = false;
		try {
			result = mVideoView != null && mVideoView.isPlaying();
		} catch (IllegalStateException e) {
			KuwoLog.e(TAG, e.toString());
			result = false;
		}
		return result;
	}
	
	private void stopPlayer() {
		if (mVideoView != null)
			mVideoView.pause();
	}

	private void startPlayer() {
		if (mVideoView != null){
			mVideoView.start();
		}
	}

	@Override
	public void onCurrentMtvChanged(Mtv mtv) {
		open(mtv); 	
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		MobclickAgent.onEvent(activity, Constants.KS_UMENG_PLAY_MUSIC, Constants.KS_UMENG_FAIL);
		return false;
	}
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		KuwoLog.e(TAG, "what="+what+",extra="+extra);
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			KuwoLog.d(TAG, "onInfo, msg:BUFFERING_START");
			if (mbSwitchAcc)
				return true;
			llSingActivityWaiting.setVisibility(View.VISIBLE);
			if(isPlaying()){
				needResume = true;
				stopPlayer();
			}
			updatePausePlay();
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			KuwoLog.e(TAG, "onInfo, msg:BUFFERING_END");
			llSingActivityWaiting.setVisibility(View.GONE);			
			if (needResume)
			{
				mbSwitchAcc = false;
				needResume = false;
				startPlayer();
			}
			updatePausePlay();
			break;
		default:
			break;
		}
		return false;
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		openNext();
	}
	@Override
	public void onPrepared(MediaPlayer mp) {
		KuwoLog.d(TAG, "Vitamio onPrepared");
		mVideoView.setVolume(0, 0); //音量先设置为0
		if (mCurrentMtv != null && mCurrentMtv.hasEcho && PreferencesManager.getInt(Constants.MTV_ORI_ACCOMP, 2) == 2)  //如果有伴唱则播放伴唱
		KuwoLog.d(TAG, "after setAudioTrack");			
		initCommonPlayerVolume();
		sbSingControllerSeekBar.setMax((int)mVideoView.getDuration());
		mLoopHandler.start(mUpdateRunnable);	
		MobclickAgent.onEvent(activity, Constants.KS_UMENG_PLAY_MUSIC, Constants.KS_UMENG_SUCCESS);
	}
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
	}
	private void displayToast(String text){
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append("远程执行  ");
		sb.append(text);
		sb.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 5+text.length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		DialogUtils.toast(sb, true);
	}
	private void sendMsgToService(int cmd){
		try {				
			Message event = new Message();
			event.what = cmd; //告诉客户端显示Pause
			EventBus.getDefault().post(event);
		} catch (Exception e) {
			KuwoLog.printStackTrace(e);
		}
	}
}
