/**
 * Copyright (c) 2013, BigBeard Team, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.commons.context;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import cn.kuwo.sing.phone4tv.commmons.log.LogSystem;
import cn.kuwo.sing.phone4tv.commons.file.FileDirectoryContext;
import cn.kuwo.sing.phone4tv.commons.file.FileSystem;

import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @File com.bigbeard.commons.context
 *
 * @Date Jul 10, 2013, 4:27:44 PM
 * 
 * @Author Ming Wang
 * 
 * @Version V1.0.0
 * 
 * @Description TODO: to describe this class
 */
public class AppCrashHandler implements UncaughtExceptionHandler {
	private static final String LOG_TAG = "AppCrashHandler";
	private static AppCrashHandler instance;
	private Context mContext;

	/**
	 * Make the constructor to private
	 */
	private AppCrashHandler() {
	}

	/**
	 * Use the single pattern to create the AppCrashHandler object
	 * 
	 * @return AppCrashHandler
	 */
	public static synchronized AppCrashHandler getInstance() {
		if (null == instance) {
			instance = new AppCrashHandler();
		}
		return instance;
	}

	/**
	 * Initial the AppCrashHandler with the Context object
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		LogSystem.e(LOG_TAG, "The application is crashing...");
		if (Environment.getExternalStorageDirectory() != null) {
			String crashFileName = FileSystem.getFilePath(FileDirectoryContext.FILE_DIRECTORY_TYPE_SDCARD_LOG, "crash.log");
			try {
				FileWriter fw = new FileWriter(crashFileName, true);
				fw.write(new Date() + "\n");
				StackTraceElement[] stackTrace = throwable.getStackTrace();
				fw.write(throwable.getMessage() + "\n");
				for (int i = 0; i < stackTrace.length; i++) {
					fw.write("file:" + stackTrace[i].getFileName() + " class:"
							+ stackTrace[i].getClassName() + " method:"
							+ stackTrace[i].getMethodName() + " line:"
							+ stackTrace[i].getLineNumber() + "\n");
				}
				fw.write("\n");
				fw.close();
			} catch (IOException e) {
				Log.e("crash handler", "load file failed...", e.getCause());
			}
		}
		throwable.printStackTrace();
		//Use the UMENG library to report the application error information to server
		MobclickAgent.onError(mContext, LogSystem.getStackTraceString(throwable));
		MobclickAgent.reportError(mContext,LogSystem.getStackTraceString(throwable));
		MobclickAgent.onKillProcess(mContext);
		//Kill the application process with the Process.killProcess API
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
