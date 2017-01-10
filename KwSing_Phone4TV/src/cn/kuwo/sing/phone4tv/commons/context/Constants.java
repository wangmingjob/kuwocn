/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.commons.context;


import cn.kuwo.sing.phone4tv.commmons.log.Logger;
import cn.kuwo.sing.phone4tv.commons.util.TimeUtils;


/**
 * 
 * @Package cn.kuwo.sing.context
 *
 * @Date 2013-7-18, 下午2:51:47
 *
 * @Author wangming
 *
 */
public abstract class Constants {
	public static final int LOG_LEVEL = Logger.LOG_LEVEL_DEBUG; //日志级别
	public static final boolean IS_PERSISTENCE_LOG = true; //是否持久化日志
	
	public static final String MULTICAST_IP = "233.0.0.1";
	public static final int MULTICAST_PORT = 8601;
	public static final String LAST_SERVER_IP_KEY = "lastServerIp";
	
	//menu
	public static final int MENU_GROUP_ID = 0;
	public static final int MENU_ITEM_ID_SEARCH = 0;
	public static final String MENU_ITEM_TITLE_SEARCH = "search";
	public static final int MENU_ITEM_ID_UPDATE = 1;
	public static final String MENU_ITEM_TITLE_UPDATE = "update";
	
	public static final String CACHE_DATA_HOT_SONGS = "hotSongs"; //热门作品数据
	public static final String CACHE_DATA_SUPER_SINGER = "superSinger"; //K歌达人数据
	public static final String CACHE_DATA_NEW_SONGS = "newSongs"; //最新作品数据
	public static final long CACHE_DATA_EXPIRED_MS_HOT_SONGS = TimeUtils.DAY; 
	public static final long CACHE_DATA_EXPIRED_MS_SUPER_SINGER = TimeUtils.DAY;
	public static final long CACHE_DATA_EXPIRED_MS_NEW_SONGS = TimeUtils.DAY;
	
	public static final long LIST_ITEM_ANIMATION_DELAY = 150;
	public static final long LIST_ITEM_ANIMATION_DURATION = 360;
	
	public static final int HTTP_REQUEST_TIME_OUT = 8000;
	
	public static final long PTR_SHOW_DELAY_TIME = 1200;
	
	public static final int BANGINFO_MTV_CATEGORY = 1;
	
	public static final String CLIENT_VERSION_PREFIX = "kwsing_android_";
	public static final String HTTP_REQUEST_URL_HOT_SONGS = "http://changba.kuwo.cn/kge/mobile/Plaza?t=hot&"; //后面有参数pn,rn,ts,version
	public static final String HTTP_REQUEST_URL_SUPER_SINGER = "http://changba.kuwo.cn/kge/mobile/Plaza?t=superstar&"; //后面有参数pn,rn,ts,version
	public static final String HTTP_REQUEST_URL_NEW_SONGS = "http://changba.kuwo.cn/kge/mobile/Plaza?t=new&"; //后面有参数pn,rn,ts,version
	
	public static final int PAGE_SIZE_DETAIL_MTV_LIST = 20;
	public static final int PAGE_SIZE_USER_DETAIL_MTV_LIST = 20;
	public static final int PAGE_SIZE_SINGER_LIST = 30;
	
	public static final int PAGE_FROM_PINYIN = 1;
	public static final int PAGE_FROM_MTV_CATEGORY = 2;
	public static final int PAGE_FROM_SINGER = 3;
	public static final int PAGE_FROM_MTV_CATEGORY_ORDER = 4;
	
	public static final int MTV_CATEGORY_TYPE_COMMON = 0; //通用
	public static final int MTV_CATEGORY_TYPE_LANGUAGE = 1; //语种
	public static final int MTV_CATEGORY_TYPE_USER = 2; //用户
	
	public static final String KS_UMENG_ADD = "KS_UMENG_ADD";
	public static final String KS_UMENG_SING = "KS_UMENG_SING";
	public static final String KS_UMENG_PLAY = "KS_UMENG_PLAY";
	public static final String KS_UMENG_DELETE_REPEAT = "KS_UMENG_DELETE_REPEAT";
	
}
