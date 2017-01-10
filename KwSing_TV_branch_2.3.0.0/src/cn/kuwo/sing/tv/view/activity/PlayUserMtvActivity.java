/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.thread.LoopHandler;
import cn.kuwo.framework.utils.TimeUtils;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.UserMtv;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.DataHandler;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.logic.LyricLogic;
import cn.kuwo.sing.tv.utils.BitmapTools;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.view.widget.SysVideoView;
import cn.kuwo.sing.util.lyric.Lyric;
import cn.kuwo.sing.util.lyric.Sentence;

import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.view.activity
 * 
 * @Date 2013-10-12, 下午7:29:41
 * 
 * @Author wangming
 * 
 */
public class PlayUserMtvActivity extends Activity implements
		OnBufferingUpdateListener, OnPreparedListener, OnCompletionListener,
		OnInfoListener, OnErrorListener , OnSeekCompleteListener{
	private static final String LOG_TAG = "PlayUserMtvActivity";
	private TextView tvPlayUserMtvName;
	private TextView tvPlayUserMtvArtist;
	private ImageView ivPlayUserMtvBackground;
	private LinearLayout llPlayUserMtvBufferFail;
	private ImageView ivPlayUserMtvPause;
	private LinearLayout llPlayUserMtvVolumeController;
	private SeekBar sbPlayUserMtvVolume;
	private SeekBar sbPlayUserMtvController;
	private TextView tvPlayUserMtvVolumeValue;
	private TextView tvPlayUserMtvLyric;
	private TextView tvPlayUserMtvControllerPlayedTime;
	private TextView tvPlayUserMtvControllerTotalTime;
	private UserMtv mUserMtv;
	private SysVideoView vvPlayUserMtv;
	private SharedPreferences mSharedPreferences;
	private float mAccompVolume;
	private Lyric mLyric;
	private LoopHandler mLoopHandler = new LoopHandler();
	private LyricLogic mLyricLogic;
	private LinearLayout llPlayUserMtvWaiting;
	private static final int SEEK_STEP_SIZE = 3000;
	private Bitmap mBackgroundBitmap;
	private static final int HIDE_VOLUME_DIALOG_DURATION = 8000;
	private static final int HIDE_VOLUME_DIALOG_MESSAGE_WHAT = 23;
	private boolean isReleaseLeftOrRightButton = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_user_mtv_activity);
		initData();
		initView();
		obtainData();
	}
	
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
	
	public void onEvent(Message msg) {
		switch (msg.what) {
		case Constants.MSG_CLOSE_PLAY_ACTIVITY_OR_PLAY_USER_ACTIVITY_WHEN_CLICK_HOME:
			if(vvPlayUserMtv != null) {
				vvPlayUserMtv.stopPlayback();
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	private void initData() {
		EventBus.getDefault().register(this);
		Intent intent = getIntent();
		mUserMtv = (UserMtv) intent.getSerializableExtra("userMtv");

		mSharedPreferences = getSharedPreferences("kuwoTV",
				Context.MODE_PRIVATE);
		mAccompVolume = mSharedPreferences.getFloat("accompVolume", 0.8f);

		mLyricLogic = new LyricLogic();
	}

	private void initView() {
		/** =====================VideoView=================== */
		vvPlayUserMtv = (SysVideoView) findViewById(R.id.playuser_syssurface);
		vvPlayUserMtv.setVolume(mAccompVolume, mAccompVolume);
		//vvPlayUserMtv.setBufferSize(1024 * 1024 * 2);
		vvPlayUserMtv.setVideoLayout(2, 0);
		vvPlayUserMtv.setOnBufferingUpdateListener(this);
		vvPlayUserMtv.setOnPreparedListener(this);
		vvPlayUserMtv.setOnCompletionListener(this);
		vvPlayUserMtv.setOnInfoListener(this);
		vvPlayUserMtv.setOnErrorListener(this);
		vvPlayUserMtv.setOnSeekCompleteListener(this);
		/** ======================================== */
		sbPlayUserMtvController = (SeekBar) findViewById(R.id.sbPlayUserMtvController);

		llPlayUserMtvWaiting = (LinearLayout) findViewById(R.id.llPlayUserMtvWaiting);
		llPlayUserMtvWaiting.setVisibility(View.GONE);

		tvPlayUserMtvName = (TextView) findViewById(R.id.tvPlayUserMtvName);
		tvPlayUserMtvName.setVisibility(View.VISIBLE);
		tvPlayUserMtvArtist = (TextView) findViewById(R.id.tvPlayUserMtvArtist);
		tvPlayUserMtvArtist.setVisibility(View.VISIBLE);

		ivPlayUserMtvBackground = (ImageView) findViewById(R.id.ivPlayUserMtvBackground);
		//内存还是会溢出，使用UIL的ImageLoader
		mBackgroundBitmap = BitmapTools.createBitmapByInputstream(
				this, R.drawable.user_mtv_background, AppContext.SCREEN_WIDTH,
				AppContext.SCREEN_HIGHT);
		ivPlayUserMtvBackground.setImageBitmap(mBackgroundBitmap);
//		DisplayImageOptions backgroundOptions = new DisplayImageOptions.Builder()
//			.cacheInMemory()
//			.cacheOnDisc()
//			.resetViewBeforeLoading()
//	        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//	        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//			.displayer(new SimpleBitmapDisplayer())
//			.build();
//		String backgroundUri = "drawable://"+R.drawable.user_mtv_background;
//		ImageLoader.getInstance().displayImage(backgroundUri, ivPlayUserMtvBackground, backgroundOptions);
		ivPlayUserMtvBackground.setScaleType(ScaleType.FIT_XY);
		ivPlayUserMtvBackground.setVisibility(View.VISIBLE);
		

		llPlayUserMtvBufferFail = (LinearLayout) findViewById(R.id.llPlayUserMtvBufferFail);
		llPlayUserMtvBufferFail.setVisibility(View.GONE);

		ivPlayUserMtvPause = (ImageView) findViewById(R.id.ivPlayUserMtvPause);
		ivPlayUserMtvPause.setVisibility(View.GONE);

		llPlayUserMtvVolumeController = (LinearLayout) findViewById(R.id.llPlayUserMtvVolumeController);
		llPlayUserMtvVolumeController.setVisibility(View.GONE);

		sbPlayUserMtvVolume = (SeekBar) findViewById(R.id.sbPlayUserMtvVolume);
		sbPlayUserMtvVolume.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

		tvPlayUserMtvVolumeValue = (TextView) findViewById(R.id.tvPlayUserMtvVolumeValue);

		tvPlayUserMtvLyric = (TextView) findViewById(R.id.tvPlayUserMtvLyric);
		tvPlayUserMtvLyric.setVisibility(View.VISIBLE);

		tvPlayUserMtvControllerPlayedTime = (TextView) findViewById(R.id.tvPlayUserMtvControllerPlayedTime);
		tvPlayUserMtvControllerTotalTime = (TextView) findViewById(R.id.tvPlayUserMtvControllerTotalTime);
	}

	private void obtainData() {
		if (mUserMtv == null) {
			KuwoLog.e(LOG_TAG, "play user mtv error: userMtv is null");
			return;
		}

		String title = mUserMtv.title;
		if (title.equals("")){
			title = "未命名";
		}
		tvPlayUserMtvName.setText("歌曲名："+title);
		
		String name = mUserMtv.uname;
		if (name.equals("")){
			tvPlayUserMtvArtist.setVisibility(View.INVISIBLE);
		}
		else{
			tvPlayUserMtvArtist.setText("网友："+name);
		}
		
		ListLogic listLogic = new ListLogic();
		listLogic.getUserMtvUrl(mUserMtv.id, mUserMtvUrlHandler);
		listLogic.getLyricSong(mUserMtv.id, mUserMtvLyricHandler);
	}

	private DataHandler mUserMtvLyricHandler = new DataHandler<Lyric>() {

		@Override
		public void onSuccess(Lyric lyric) {
			if (lyric == null)
				KuwoLog.e(LOG_TAG, "download the user mtv lyric error: lyric is null");
			mLyric = lyric;
			tvPlayUserMtvLyric.setVisibility(View.VISIBLE);
		}
		
		public void onFailure() {
			KuwoLog.e(LOG_TAG, "抱歉，没有获取到对应的歌词");
			tvPlayUserMtvLyric.setVisibility(View.GONE);
		};
	};

	private DataHandler mUserMtvUrlHandler = new DataHandler<String>() {

		@Override
		public void onSuccess(String data) {
			MobclickAgent.onEvent(PlayUserMtvActivity.this,
					Constants.KS_UMENG_GET_SONGURL, Constants.KS_UMENG_SUCCESS);
			KuwoLog.d(LOG_TAG, "user mtv url=" + data);
			vvPlayUserMtv.setVideoURI(Uri.parse(data));
		}

		@Override
		public void onFailure() {
			super.onFailure();
			MobclickAgent.onEvent(PlayUserMtvActivity.this, Constants.KS_UMENG_GET_SONGURL, Constants.KS_UMENG_FAIL);
		}
	};
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(llPlayUserMtvVolumeController.getVisibility() != View.VISIBLE) {
				long currentPosition = sbPlayUserMtvController.getProgress();
				if(currentPosition <= SEEK_STEP_SIZE) 
					vvPlayUserMtv.seekTo(0);
				else 
					vvPlayUserMtv.seekTo(currentPosition-SEEK_STEP_SIZE);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: 
			if( llPlayUserMtvVolumeController.getVisibility() != View.VISIBLE) {
				long currentPosition = sbPlayUserMtvController.getProgress();
				vvPlayUserMtv.seekTo(currentPosition+SEEK_STEP_SIZE);
			}
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int action = event.getAction();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (llPlayUserMtvVolumeController.getVisibility() == View.VISIBLE)
				hideUserMtvVolumeLayout();
			else
				showExitDialog();
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(llPlayUserMtvVolumeController.getVisibility() >= View.INVISIBLE)
				showUserMtvVolumeLayout();
			sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			isReleaseLeftOrRightButton = false;
			if(llPlayUserMtvVolumeController.getVisibility() != View.VISIBLE) {
				int currentPosition = sbPlayUserMtvController.getProgress();
				if(currentPosition <= SEEK_STEP_SIZE) 
					sbPlayUserMtvController.setProgress(0);
				else 
					sbPlayUserMtvController.setProgress(currentPosition-SEEK_STEP_SIZE);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: 
			isReleaseLeftOrRightButton = false;
			if( llPlayUserMtvVolumeController.getVisibility() != View.VISIBLE) {
				int currentPosition = sbPlayUserMtvController.getProgress();
				sbPlayUserMtvController.setProgress(currentPosition+SEEK_STEP_SIZE);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if(llPlayUserMtvWaiting.getVisibility() != View.VISIBLE && llPlayUserMtvVolumeController.getVisibility() != View.VISIBLE) {
				doPlayOrPause();
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	};
	
	private Handler mHideVolumeDialogHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HIDE_VOLUME_DIALOG_MESSAGE_WHAT:
				if(llPlayUserMtvVolumeController.getVisibility() == View.VISIBLE)
					hideUserMtvVolumeLayout();
				break;
			default:
				break;
			}
		}
		
	};
	
	private void sendHideVolumeDialogMessage(int duration) {
		mHideVolumeDialogHandler.removeMessages(HIDE_VOLUME_DIALOG_MESSAGE_WHAT);
		mHideVolumeDialogHandler.sendEmptyMessageDelayed(HIDE_VOLUME_DIALOG_MESSAGE_WHAT, duration);
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
			case R.id.sbPlayUserMtvVolume:
				mAccompVolume = progress*0.01f;
				Editor userMtvVolumeEditor = mSharedPreferences.edit();
				userMtvVolumeEditor.putFloat("accompVolume", mAccompVolume);
				userMtvVolumeEditor.commit();
				vvPlayUserMtv.setVolume(mAccompVolume, mAccompVolume);
				tvPlayUserMtvVolumeValue.setText(progress+"");
				sendHideVolumeDialogMessage(HIDE_VOLUME_DIALOG_DURATION);
				break;
			default:
				break;
			}
		}
	};

	private void showExitDialog() {
		try {
			final AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.show();
			dialog.getWindow().setContentView(R.layout.custom_alert_dialog);
			String exitPromptText = "您确定要退出播放么？";
			TextView tvPlayControllerExitPrompt = (TextView) dialog.getWindow().findViewById(R.id.tvPlayControllerExitPrompt);
			tvPlayControllerExitPrompt.setText(exitPromptText);
			dialog.getWindow().findViewById(R.id.btPlayControllerExitOk).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if(!mBackgroundBitmap.isRecycled()) {
							mBackgroundBitmap.recycle();
							System.gc();
						}
						finish();
					}
				});
			dialog.getWindow().findViewById(R.id.btPlayControllerExitCancel).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		}catch(Exception e) {
			KuwoLog.printStackTrace(e);
		}
	}
	
	private void showUserMtvVolumeLayout() {
		Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		llPlayUserMtvVolumeController.startAnimation(fadeInAnimation);
		llPlayUserMtvVolumeController.setVisibility(View.VISIBLE);
		sbPlayUserMtvVolume.requestFocus();
	}

	private void hideUserMtvVolumeLayout() {
		Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		llPlayUserMtvVolumeController.startAnimation(fadeOutAnimation);
		llPlayUserMtvVolumeController.setVisibility(View.GONE);
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		return false;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		KuwoLog.d(LOG_TAG, "onInfo, msg:" + what);
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			llPlayUserMtvWaiting.setVisibility(View.VISIBLE);
			if (vvPlayUserMtv.isPlaying()) {
				vvPlayUserMtv.pause();
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			llPlayUserMtvWaiting.setVisibility(View.GONE);
			vvPlayUserMtv.start();
			break;
		default:
			break;
		}
		return false;
	}

	private void doPlayOrPause() {
		if (vvPlayUserMtv.isPlaying())
			vvPlayUserMtv.pause();
		else
			vvPlayUserMtv.start();
		updatePlayOrPauseUIState();
	}

	private void updatePlayOrPauseUIState() {
		if (vvPlayUserMtv.isPlaying())
			ivPlayUserMtvPause.setVisibility(View.GONE);
		else
			ivPlayUserMtvPause.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		DialogUtils.toast("播放完毕", false);
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		sbPlayUserMtvController.setMax((int)vvPlayUserMtv.getDuration());
		// 准备播放时，开始进行进度回调
		mLoopHandler.start(mPlayProgressRunnable); // 开启进度回调线程,用来设置SeekBar的进度和歌词同步显示
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int progress) {
//		if (progress % 10 == 0)
//			KuwoLog.v(LOG_TAG, "user mtv playing progress: " + progress);
//		// sbPlayUserMtvController.setSecondaryProgress((int)(progress * mp.getDuration() / 100));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (vvPlayUserMtv != null)
			vvPlayUserMtv.setVideoLayout(2, 0);
		super.onConfigurationChanged(newConfig);
	}

	private Runnable mPlayProgressRunnable = new Runnable() {
		@Override
		public void run() {
			if(isReleaseLeftOrRightButton && vvPlayUserMtv.isPlaying()) {
				long currentPlayingPosition = vvPlayUserMtv.getCurrentPosition();
				if(Math.abs(currentPlayingPosition-sbPlayUserMtvController.getProgress()) > 9000)
					return;
				sbPlayUserMtvController.setProgress((int) currentPlayingPosition);
				tvPlayUserMtvControllerPlayedTime.setText(TimeUtils
						.toString(vvPlayUserMtv.getCurrentPosition()));
				tvPlayUserMtvControllerTotalTime.setText(TimeUtils
						.toString(vvPlayUserMtv.getDuration()));
				if (mLyric != null) {
					Sentence sentence = mLyricLogic.findSentence(
							currentPlayingPosition, mLyric,
							LyricLogic.SENTENCE_RESULT_NEXT);
					if (sentence != null)
						tvPlayUserMtvLyric.setText(sentence.getContent());
				}
			}
		}
	};

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		isReleaseLeftOrRightButton = true;
	}

}
