/**
 * Copyright (c) 2013, wangmingjob.com, Inc. All rights reserved.
 */
package cn.kuwo.sing.phone4tv.commons.context;

import cn.kuwo.sing.phone4tv.commons.file.FileDirectory;
import cn.kuwo.sing.phone4tv.commons.file.FileDirectoryContext;

/**
 * 
 * @Package com.superdaddy.android.fragment
 *
 * @Date May 25, 2013,11:59:23 AM
 *
 * @Author wangming
 * 
 * @Description 默认的文件系统上下文
 *
 */
public class DefaultFileDirectoryContext extends FileDirectoryContext {
	public static String FILE_DIRECTORY_TYPE_CONFIG = "CONFIG";
	public static String FILE_DIRECTORY_VALUE_CONFIG = "config";
	public static String FILE_DIRECTORY_TYPE_PICTURE = "PICTURE";
	public static String FILE_DIRECTORY_VALUE_PICTURE = "picture";

	public DefaultFileDirectoryContext(String appName) {
		super(appName);
	}

	@Override
	protected void defineVirtualFileDirectory() {
		FileDirectory sdcardMain = getSDCardMainFileDirectory();
		FileDirectory sdcardHidden = getSDCardHiddenFileDirectory();
		addChildFileDirectory(sdcardMain, FILE_DIRECTORY_TYPE_CONFIG, FILE_DIRECTORY_VALUE_CONFIG); //先初始化一个file目录到非隐藏目录
		addChildFileDirectory(sdcardMain, FILE_DIRECTORY_TYPE_PICTURE, FILE_DIRECTORY_VALUE_PICTURE); //先初始化一个file目录到非隐藏目录
	}

}
