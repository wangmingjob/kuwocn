package cn.kuwo.sing.tv.view.activity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.utils.ShortcutUtils;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.context.Config;
import cn.kuwo.sing.tv.logic.DataHandler;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.utils.BitmapTools;
import cn.kuwo.sing.tv.utils.ImageUtils;

import com.aispeech.common.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 入口Activity
 */
public class EntryActivity extends BaseActivity {
	private static final String LOG_TAG = "EntryActivity";
	private Activity mActivity;
	private Bitmap mBackgroundBitmap;
	
	
	/*
	 * 入口Activity负责启动场景的协调工作，
	 * 主要控制大场景的跳转
	 * 
	 * 逻辑如下：
	 * 如果应用没有启动过
	 * 初始化应用->验证是否初始化成功->初始化应用环境->场景切换
	 * 
	 * 启动过`	
	 * 直接进行场景切换
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KuwoLog.d(LOG_TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mActivity = this;

		ListLogic listLogic = new ListLogic();
		listLogic.getTopPic(mEntryBgDataHandler); //异步获取ImageObject
		
		ImageView welcomeView = new ImageView(this);
		//阻塞式，获取本地image
		FileInputStream fis = null;
		try {
			fis = mActivity.openFileInput("entry_image");
		} catch (FileNotFoundException e) {
			KuwoLog.d(LOG_TAG, "entry_image 文件不存在【第一次启动】，使用默认的图片");
		}
		BitmapFactory.Options options = BitmapTools.getBitmapOptions(AppContext.SCREEN_WIDTH, AppContext.SCREEN_HIGHT);
		mBackgroundBitmap = BitmapFactory.decodeStream(fis, null, options);
		if(mBackgroundBitmap == null) {
			mBackgroundBitmap = BitmapTools.createBitmapByInputstream(mActivity, R.drawable.entry_bg, AppContext.SCREEN_WIDTH, AppContext.SCREEN_HIGHT);
			welcomeView.setImageBitmap(mBackgroundBitmap);
		}else {
			welcomeView.setImageBitmap(mBackgroundBitmap);
		}
		welcomeView.setScaleType(ScaleType.FIT_XY);
		setContentView(welcomeView);
		
		if(!Build.BRAND.equals("XiaoMi")) {
			// 创建Shortcut
			if (!Config.getPersistence().hasCreatedShortcut) {
				ShortcutUtils shortcut = new ShortcutUtils(this, R.string.app_name, R.drawable.logo, EntryActivity.class.getName());
				shortcut.createShortcut();
				Config.getPersistence().hasCreatedShortcut = true;
				Config.savePersistence();
			}
		}
		
		
		if(PreferencesManager.getBoolean("isAppRunning")) {
			switchScene();
			return;
		}
		PreferencesManager.put("isAppRunning", true).commit();
		
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				switchScene();
			}
		}, 3000);
		
		//先删除语音控制的文件夹
		File path = Util.getAvaiableAppDataDirPerInternal(this, "audio");
		if (path.exists() && path.isDirectory())
			delete(path);
		
		MobclickAgent.onError(this);
		MobclickAgent.updateOnlineConfig(this);
	}
	
	private DataHandler<ImageObject> mEntryBgDataHandler = new DataHandler<ImageObject>() {

		@Override
		public void onSuccess(ImageObject data) {
			if(data != null) {
				KuwoLog.d(LOG_TAG, "entryBg imageObject.id="+data.id);
				KuwoLog.d(LOG_TAG, "entry imageObject.name="+data.name);
				KuwoLog.d(LOG_TAG, "entry imageObject.pic="+data.pic);
				fetchImageFromServer(data.pic);
			}
		}
		
		public void onFailure() {
			File file = new File(mActivity.getFilesDir(), "entry_image");
			if(file.exists())
				file.delete();
		}
	};
	
	private void fetchImageFromServer(final String picUrl) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bitmap = ImageUtils.getImageByHttpClient(picUrl);
				try {
					FileOutputStream fos = mActivity.openFileOutput("entry_image", Context.MODE_PRIVATE);
					fos.write(ImageUtils.bitmap2Bytes(bitmap));
					fos.close();
				} catch (FileNotFoundException e) {
					KuwoLog.printStackTrace(e);
				} catch (IOException e) {
					KuwoLog.printStackTrace(e);
				}
				
				/*try {
				ImageUtils.saveImage2SDCard(imagePath, ImageUtils.bitmap2Bytes(bitmap));
				} catch (IOException e) {
					KuwoLog.printStackTrace(e);
				}*/ 
			}
		}).start();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(!mBackgroundBitmap.isRecycled()) {
			mBackgroundBitmap.recycle();
			System.gc();
		}
	}

	/**
	 * 场景切换
	 */
	private void switchScene() {
		Intent intent = new Intent(this, ItemListActivity.class);
		startActivity(intent);
		finish();
	}
	
	public static void delete(File file) {  
	    if (file.isFile()) {  
	        file.delete();  
	        return;  
	    }  
		
       if(file.isDirectory()){  
           File[] childFiles = file.listFiles();  
            if (childFiles == null || childFiles.length == 0) {  
              file.delete();  
                return;  
           }  
    
            for (int i = 0; i < childFiles.length; i++) {  
                delete(childFiles[i]);  
            }  
        file.delete();  
       }  
    } 
}
