/**
 * Copyright (c) 2013, BigBeard Team, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.commmons.log;

import cn.kuwo.sing.phone4tv.commons.file.FileDirectoryContext;
import cn.kuwo.sing.phone4tv.commons.file.FileSystem;


/**
 * 
 * @File com.bigbeard.commons.log
 *
 * @Date Jul 10, 2013, 4:29:38 PM
 * 
 * @Author Ming Wang
 * 
 * @Version V1.0.0
 * 
 * @Description TODO: to describe this class
 */
public class LogSystem {
	private static Logger sLogger = null;
	
	/**
	 * You must initial the LogSystem by Calling this method
	 * 
	 * @param appName
	 */
	public static void init(String appName, int logLevel, boolean isPersistenceLog) {
		String logFilePath = FileSystem.getFilePath(FileDirectoryContext.FILE_DIRECTORY_TYPE_SDCARD_LOG, appName+".log");
		if(sLogger == null)
			sLogger = Logger.getInstance(logFilePath);
		sLogger.setLogLevel(logLevel);
		sLogger.isPersistenceLog(isPersistenceLog);
	}
	
	/**
	 * Get the stack track information
	 * 
	 * @param tr
	 * @return 
	 */
	public static String getStackTraceString(Throwable tr) {
		if(sLogger == null)
			return null;
		return sLogger.getStackTraceString(tr);
	}
	
	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public static int v(String logTag, String msg) {
		if(sLogger == null)
			return Logger.LOG_ERROR;
		return sLogger.v(logTag, msg);
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public static int d(String logTag, String msg) {
		if(sLogger == null)
			return Logger.LOG_ERROR;
		return sLogger.d(logTag, msg);
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public static int i(String logTag, String msg) {
		if(sLogger == null)
			return Logger.LOG_ERROR;
		return sLogger.i(logTag, msg);
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public static int w(String logTag, String msg) {
		if(sLogger == null)
			return Logger.LOG_ERROR;
		return sLogger.w(logTag, msg);
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public static int e(String logTag, String msg) {
		if(sLogger == null)
			return Logger.LOG_ERROR;
		return sLogger.e(logTag, msg);
	}
	
	public static int printStackTrace(String logTag, Exception e) {
		if (sLogger== null) 
			return Logger.LOG_ERROR;
		else
			return sLogger.e(logTag, sLogger.getStackTraceString(e));
	}
}
