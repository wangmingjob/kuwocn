package cn.kuwo.sing.tv.context;

import java.io.File;
import java.util.ArrayList;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Environment;
import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.dir.DirectoryManager;
import cn.kuwo.framework.log.BaseLogger;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.utils.DialogUtils;

public class MainApplication extends Application {
    private final static String TAG = "MainApplication";
    private ArrayList<Activity> mActivityList = new ArrayList<Activity>();
    public static boolean isSingActivityAliving = false;
    // 初始化文件系统是否失败
    public static boolean sInitFSFailed = true;
    private static final boolean RELEASE = false; 
    
	@Override
	public void onCreate() {
		super.onCreate();
		boolean externalStorageAvailable = checkExternalStorageState();
		KuwoLog.e(TAG, "外部存储状态 externalStorageAvailable="+externalStorageAvailable);
		
		if(RELEASE)
			initCrashHandler();
		init();
		initImageLoader(getApplicationContext(), externalStorageAvailable);
	}
	
	private void initCrashHandler() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		Thread.currentThread().setUncaughtExceptionHandler(crashHandler);
	}
	
	/**
	 * 检查SD卡是否可用
	 */
	private boolean checkExternalStorageState() {
		String state = Environment.getExternalStorageState();
		KuwoLog.e(TAG, "外部存储状态state="+state);
		boolean externalStorageAvailable = false;
		boolean externalStorageWriteable = false;
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	    externalStorageAvailable = externalStorageWriteable = true;
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	    externalStorageAvailable = true;
    	    externalStorageWriteable = false;
    	} else {
    	    // Something else is wrong. It may be one of many other states, but all we need
    	    //  to know is we can neither read nor write
    	    externalStorageAvailable = externalStorageWriteable = false;
    	}
		return externalStorageAvailable;
	}

	public static void initImageLoader(Context context, boolean externalStorageAvailable) {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory limit 
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = null;
		if(!sInitFSFailed)  
			cacheDir = StorageUtils.getOwnCacheDirectory(context, DirectoryManager.getDirPath(DirContext.PICTURE));
		else 
			cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // default
		        .memoryCacheSize(2 * 1024 * 1024)
		        .discCache(new UnlimitedDiscCache(cacheDir)) // default
		        .discCacheSize(50 * 1024 * 1024)
		        .discCacheFileCount(100)
		        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		        .imageDownloader(new BaseImageDownloader(context)) // default
		        .tasksProcessingOrder(QueueProcessingType.LIFO) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .enableLogging()
		        .build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * add the activity to ArrayList.
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		mActivityList.add(activity);
	}
	
	/**
	 * remove the activity from ArrayList.
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		mActivityList.remove(activity);
	}
	
	/**
	 * finish the activity.
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		activity.finish();
	}
	
	/**
	 * finish all activities.
	 */
	public void exit() {
		if(Build.BRAND.equals("Tcl") || Build.BRAND.equals("SkyWorth") || Build.BRAND.equalsIgnoreCase("changhong") || Build.BRAND.equals("Hisense")) { 
			try {
					TvManager.getInstance().setInputSource(EnumInputSource.E_INPUT_SOURCE_STORAGE, false, false, false);
				} catch (Exception e) {
					KuwoLog.e(TAG, "TvManager.getInstance().setInputSource 音频源设置失败");
			}
		}
		for(Activity activity : mActivityList) {
			activity.finish();
		}
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		KuwoLog.d(TAG, "onTerminate");
	}

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        KuwoLog.w(TAG, "onLowMemory");
        System.gc();
    }
    
    public void init() {
		// 初始化目录结构
		if (sInitFSFailed)
			sInitFSFailed = !DirectoryManager.init(new DirContext(Constants.APP_NAME));

		// 设置日志级别
		KuwoLog.init(Constants.APP_NAME);
		if(RELEASE) {
			KuwoLog.trace(true);
			KuwoLog.debug(false);
		}else{
			KuwoLog.trace(true);
			KuwoLog.debug(true);
			KuwoLog.getLogger().setLevel(BaseLogger.DEBUG);
		}
		

		// 初始化上下文
		AppContext.init(this);
		
		// 初始化PreferencesManager
		PreferencesManager.init(this);
		
		if ( sInitFSFailed ) {
			KuwoLog.e(TAG, "init file system failed");
		}
		
		if(PreferencesManager.getInt(Constants.PLAYBACK_MODE, Constants.PLAYBACK_MODE_HARDWARE) == Constants.PLAYBACK_MODE_HARDWARE)
		{
			if(Build.BRAND.equals("Tcl") || Build.BRAND.equals("SkyWorth") || Build.BRAND.equalsIgnoreCase("changhong") || Build.BRAND.equals("Hisense")) {
				try {
					TvManager.getInstance().setInputSource(EnumInputSource.E_INPUT_SOURCE_KTV, false, false, false);
				} catch (Exception e) {
					KuwoLog.e(TAG, "TvManager.getInstance().setInputSource 音频源输入设置失败");
				}
			}			
		}
    }
}
