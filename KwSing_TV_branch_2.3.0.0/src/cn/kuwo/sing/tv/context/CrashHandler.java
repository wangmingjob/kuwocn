/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.context;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import cn.kuwo.framework.dir.DirectoryContext;
import cn.kuwo.framework.dir.DirectoryManager;
import cn.kuwo.framework.log.KuwoLog;

import com.umeng.analytics.MobclickAgent;

/**
 * @Package cn.kuwo.sing.context
 * 
 * @Date 2013-3-4, 上午10:17:50, 2013
 * 
 * @Author wangming
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private static CrashHandler handler;
	private Context mContext;

	private CrashHandler() {

	}

	public static synchronized CrashHandler getInstance() {
		if (null == handler) {
			handler = new CrashHandler();
		}
		return handler;
	}

	public void init(Context context) {
		mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		KuwoLog.e(TAG, "程序Crash");
		if (Environment.getExternalStorageDirectory() != null) {
			String crashFileName = DirectoryManager.getFilePath(DirectoryContext.LOG, "crash.log");
			try {
				FileWriter fw = new FileWriter(crashFileName, true);
				fw.write(new Date() + "\n");
				StackTraceElement[] stackTrace = ex.getStackTrace();
				fw.write(ex.getMessage() + "\n");
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
		ex.printStackTrace();
		MobclickAgent.onError(mContext, KuwoLog.getLogger().getStackTraceString(ex));
		MobclickAgent.reportError(mContext,KuwoLog.getLogger().getStackTraceString(ex));
		MobclickAgent.onKillProcess(mContext);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
