/**
 * Copyright (c) 2013, BigBeard Team, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.commmons.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * @File com.bigbeard.commons.log
 *
 * @Date Jul 10, 2013, 4:29:25 PM
 * 
 * @Author Ming Wang
 * 
 * @Version V1.0.0
 * 
 * @Description TODO: to describe this class
 */
public class Logger {
	public static final int LOG_LEVEL_VERBOSE = 5;
	public static final int LOG_LEVEL_DEBUG = 4;
	public static final int LOG_LEVEL_INFO = 3;
	public static final int LOG_LEVEL_WARN = 2;
	public static final int LOG_LEVEL_ERROR = 1;
	public static final int LOG_LEVEL_NONE = 0;
	public static final int LOG_ERROR = -1;

	private int mLogLevel = LOG_LEVEL_VERBOSE; // 默认打印所有
	private String mLogFilePath;
	private boolean mIsPersistenceLog;
	
	private static Logger instance = null;
	
	/**
	 * Make the constructor to private
	 */
	private Logger(String logFilePath) {
		if(TextUtils.isEmpty(logFilePath)) 
			throw new IllegalArgumentException("invalid parameter logFilePath.");
		mLogFilePath = logFilePath;
	}
	
	/**
	 * Use the single pattern to create the Logger object
	 * 
	 * @param logFilePath 
	 * @param logLevel 
	 * @param isPersistenceLog 
	 * @return
	 */
	public static Logger getInstance(String logFilePath) {
		if(instance == null)
			instance = new Logger(logFilePath);
		return instance;
	}
	
	/**
	 * Set the log level
	 * 
	 * @param logLevel
	 */
	public void setLogLevel(int logLevel) {
		if(logLevel > 5 || logLevel < 0) 
			throw new IllegalArgumentException("invalid parameter logLevel, please use Logger's static level constant.");
		mLogLevel = logLevel;
	}
	
	/**
	 * Whether need persistence the log information to SDCard
	 * 
	 * @param isPersistenceLog
	 */
	public void isPersistenceLog(boolean isPersistenceLog) {
		mIsPersistenceLog = isPersistenceLog;
	}
	
	/**
	 * Get Log information
	 * 
	 * @return
	 */
	private String getLogTag() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements == null) 
			return null;
		for (StackTraceElement element : elements) {
			if (element.isNativeMethod()) 
				continue;
			if (element.getClassName().equals(Thread.class.getName())) 
				continue;
			return "[" + Thread.currentThread().getName() + "("
					+ Thread.currentThread().getId() + "): "
					+ element.getFileName() + ":" +element.getMethodName()+":"+ element.getLineNumber()
					+ "]";
		}

		return null;
	}
	
	private final Object mLogLock = new Object();
	
	/**
	 * Persistence the log information to the SDCard
	 * 
	 * @param logLevelStr
	 * @param logTag
	 * @param msg
	 * @return 0: success, -1: fail
	 */
	private int persistenceLog(String logLevelStr, String logTag, String msg) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String time = dateFormat.format(new Date());
    	StringBuilder sb = new StringBuilder();
    	sb.append(time);
    	sb.append("\t");
    	sb.append(logLevelStr);
    	sb.append("\t");
    	sb.append(logTag);
    	sb.append("\t");
    	sb.append(msg);
    	sb.append("\n");
    	FileWriter writer = null;
    	
    	synchronized (mLogLock) {    		
			try {
				File file = new File(mLogFilePath);
				if (!file.exists()) {
					file.createNewFile();
				}
				writer = new  FileWriter(file, true);
				writer.write(sb.toString());
			} catch (FileNotFoundException e) {
				return -1;
			} catch (IOException e) {
				return -1;
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						return -1;
					}
				}				
			}    		
		}
    	return 0;	
	}
	
	/**
	 * Get the Stack trace information
	 * 
	 * @param tr
	 * @return
	 */
	public String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
	
	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public int v(String logTag, String msg) {
		if(mIsPersistenceLog)
			persistenceLog("verbose", logTag, msg);
		if (mLogLevel >= LOG_LEVEL_VERBOSE)
			return Log.v(logTag, msg);
		else
			return LOG_ERROR;
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public int d(String logTag, String msg) {
		if(mIsPersistenceLog)
			persistenceLog("debug", logTag, msg);
		if (mLogLevel >= LOG_LEVEL_DEBUG)
			return Log.d(logTag, msg);
		else
			return LOG_ERROR;
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public int i(String logTag, String msg) {
		if(mIsPersistenceLog)
			persistenceLog("info", logTag, msg);
		if (mLogLevel >= LOG_LEVEL_INFO)
			return Log.i(logTag, msg);
		else
			return LOG_ERROR;
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public int w(String logTag, String msg) {
		if(mIsPersistenceLog)
			persistenceLog("warn", logTag, msg);
		if (mLogLevel >= LOG_LEVEL_WARN)
			return Log.w(logTag, msg);
		else
			return LOG_ERROR;
	}

	/**
	 * 
	 * @param logTag
	 * @param msg
	 * @return
	 */
	public int e(String logTag, String msg) {
		if(mIsPersistenceLog)
			persistenceLog("error", logTag, msg);
		if (mLogLevel >= LOG_LEVEL_ERROR)
			return Log.e(logTag, msg);
		else
			return LOG_ERROR;
	}
}
