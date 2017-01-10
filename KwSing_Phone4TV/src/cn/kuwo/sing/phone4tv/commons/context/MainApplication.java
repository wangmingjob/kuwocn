/**
 * Copyright (c) 2005, Kuwo.cn, Inc. All rights reserved.
 */
package cn.kuwo.sing.phone4tv.commons.context;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.util.Log;

import cn.kuwo.sing.phone4tv.commmons.log.LogSystem;
import cn.kuwo.sing.phone4tv.commons.file.FileSystem;
import cn.kuwo.sing.phone4tv.commons.file.PreferencesManager;

import com.devspark.appmsg.AppMsg;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MainApplication extends Application {
	private static final String LOG_TAG = "MainApplication";
	private static final String APP_NAME = "KwSing_Phone4TV";
	private boolean initFileSystemResult;
	public boolean m_bKeyRight = true;
	private static final boolean MODE_RELEASE = false;
	public static SocketChannel s_socketChannel;
	public static boolean isConnecting = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		AppContext.init(getApplicationContext()); // 初始化应用上下文
		initFileSystemResult = FileSystem.init(new DefaultFileDirectoryContext(APP_NAME)); // 初始化文件系统
		if (initFileSystemResult) {
			LogSystem.init(APP_NAME, Constants.LOG_LEVEL, Constants.IS_PERSISTENCE_LOG); // 初始化日志系统
			LogSystem.i(LOG_TAG, "初始化文件系统成功！");
			if(MODE_RELEASE) {
				AppCrashHandler crashHandler = AppCrashHandler.getInstance();
				crashHandler.init(getApplicationContext());
				Thread.currentThread().setUncaughtExceptionHandler(crashHandler);
			}
		} else {
			Log.e(LOG_TAG, "初始化文件系统失败！请检查SD卡，和user persission");
		}
		PreferencesManager.init(getApplicationContext()); // 初始化配置文件
		initImageLoader(getApplicationContext());
	}

	/**
	 * 初始化Universal ImageLoader
	 * 
	 * @param applicationContext
	 */
	private void initImageLoader(Context applicationContext) {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) applicationContext
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
															// limit
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		File cacheDir = null;
		if (!initFileSystemResult)
			cacheDir = StorageUtils.getOwnCacheDirectory(applicationContext,FileSystem.getDirectoryPath(DefaultFileDirectoryContext.FILE_DIRECTORY_TYPE_PICTURE));
		else
			cacheDir = StorageUtils.getCacheDirectory(applicationContext);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(applicationContext)
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// default
				.memoryCacheSize(2 * 1024 * 1024)
				.discCache(new UnlimitedDiscCache(cacheDir))
				// default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(applicationContext)) // default
				.tasksProcessingOrder(QueueProcessingType.LIFO) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs()
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
