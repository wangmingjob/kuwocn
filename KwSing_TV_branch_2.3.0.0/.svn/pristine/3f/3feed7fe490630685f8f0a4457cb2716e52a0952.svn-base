package cn.kuwo.sing.tv.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.http.client.utils.URLEncodedUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import cn.kuwo.framework.cache.CacheManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.utils.IOUtils;
import cn.kuwo.framework.utils.TimeUtils;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.PagedData;
import cn.kuwo.sing.tv.bean.SearchPromptObject;
import cn.kuwo.sing.tv.bean.UpdateLog;
import cn.kuwo.sing.tv.bean.UserMtv;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.util.lyric.KdtxParser;
import cn.kuwo.sing.util.lyric.Lyric;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;


/**
 * 榜单逻辑
 * @author Administrator
 *
 */
public class ListLogic {
	
	private String TAG = "ListLogic";
	private File lyricFile;
	public static final int MTV_LIST_PAGE_SIZE = 9;	//热门推荐某一个标签里面每页显示的歌曲数
	public static final int MTV_LIST_BY_PINYIN_SEARCH = 7;
	public static final int SINGER_LIST_PAGE_SIZE = 15;
	private static final int MTV_LIST_BY_SINGER_PAGE_SIZE = 9;//歌手点歌,具体某一个歌手的每页显示的歌曲数
	public static final int USER_MTV_LIST_PAGE_SIZE = 9;	//热门作品每页显示的歌曲数
	
	//URL地址
//	private static final String HOT_KEYWORD_URL = "http://60.28.200.79/ks/tv/getTVData?type=hotkey"; //http://mboxspace.kuwo.cn
	private static final String HOT_KEYWORD_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData?type=hotkey"; 
	private static final String BAR_LIST_URL = "http://kbangserver.kuwo.cn/ksong.s";//榜单地址
//	private static final String CATEGORY_BAR_LIST_URL = "http://60.28.200.79/ks/tv/getTVData";
	private static final String CATEGORY_BAR_LIST_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData";
	private static final String MTV_LIST_URL = "http://kbangserver.kuwo.cn/ksong.s";//榜单歌曲地址
	private static final String MTV_LIST_BY_SONG_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData";//generic首字母地址
//	private static final String MTV_LIST_BY_SONG_URL = "http://60.28.200.79/ks/tv/getTVData";//generic首字母地址
	private static final String MTV_LIST_BY_SONG_PINYIN_URL = "http://mboxspace.kuwo.cn/ks/kwsing/s.list";//首字母拼音地址
	private static final String BAR_SINGER_LIST_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData";//歌手榜单
	private static final String SINGER_LIST_URL = "http://mboxspace.kuwo.cn/ks/kwsing/s.list";//歌手列表
	private static final String MTV_LIST_BY_SINGER_URL = "http://search.kuwo.cn/r.s";
	private static final String SONG_URL = "http://antiserver.kuwo.cn/anti.s";//获取作品播放地址
	private static final String TOP_PIC =  "http://mboxspace.kuwo.cn/ks/tv/getTVData";//获取作品播放地址
	private static final String LYRIC_URL = "http://wmvkdat.kuwo.cn/wmv.dat";//获取作品歌词
	private static final String LANG_BAR_SONG_LIST_URL = "http://mboxspace.kuwo.cn/ks/kwsing/s.list";
	private static final String URL_SEARCH_PROMPT = "http://mboxspace.kuwo.cn/ks/st/tips";
	private static final String URL_HOT_LIST_USER_MTV = "http://changba.kuwo.cn/kge/web/HotDataServer";
//	private static final String URL_HOT_LIST_USER_MTV = "http://60.28.200.79:8180/kge/web/HotDataServer";
	private static final String URL_USER_MTV_REQUEST = "http://changba.kuwo.cn/kge/st/getPlayUrl";
	//内部名
	private String HOT_KEYWORD_INNER_NAME = "HOT_KEYWORD";//热门搜索关键词
	private String BAR_LIST_INNER_NAME = "BAR_LIST";//榜单内部名
	private String CATEGORY_BAR_LIST_INNER_NAME = "CATEGORY_BAR_LIST";//分类榜单内部名
	private String MTV_LIST_INNER_NAME = "MTV_LIST";//榜单歌曲内部名
	private String MTV_LIST_BY_SONG_INNER_NAME = "MTV_LIST_BY_SONG";//首字母检索列表内部名
	private String MTV_LIST__PINYIN_BY_SONG_INNER_NAME = "MTV_LIST_PINYIN_BY_SONG";//首字母检索列表内部名
	private String SINGER_LIST_INNER_NAME = "SINGER_LIST";
	private String SINGER_LIST_BY_KEYWORD_INNER_NAME = "SINGER_LIST_BY_KEYWORD";
	private String MTV_LIST_BY_SINGER_INNER_NAME = "MTV_LIST_BY_SINGER";
	private String BAR_SINGER_LIST_INNER_NAME = "BAR_SINGER_LIST";//歌手榜单内部名
	private String LANG_BAR_LIST_INNER_NAME = "LANG_BAR_LIST";
	private String SEARCH_PROMPT_INNER_NAME = "SEARCH_PROMPT";
	private String USER_MTVS_INNER_NAME = "USER_MTVS_INNER_NAME";
	private String UPDATE_LOG_INFO = "UPDATE_LOG_INFO";
	//缓存时间
	private long HOT_KEYWORD_TIME = TimeUtils.DAY; //热门搜索关键词
	private long BAR_LIST_TIME = TimeUtils.DAY;//榜单内部名
	private long CATEGORY_BAR_LIST_TIME = TimeUtils.DAY;//榜单内部名
	private long MTV_LIST_TIME = TimeUtils.DAY;//榜单缓存有效时间
	private long MTV_LIST_BY_SONG_TIME = TimeUtils.DAY;//首字母检索缓存时间
	private long MTV_LIST_PINYIN_BY_SONG_TIME = TimeUtils.DAY;//首字母检索缓存时间
	private long SINGER_LIST_TIME = TimeUtils.DAY;
	private long SINGER_LIST_BY_KEYWORD_TIME = TimeUtils.DAY;
	private long MTV_LIST_BY_SINGER_TIME = TimeUtils.DAY;
	private long BAR_SINGER_LIST_TIME = TimeUtils.DAY;
	private long LANG_BAR_LIST_TIME = TimeUtils.DAY;
	private long SEARCH_PROMPT_TIME = TimeUtils.DAY;
	private long USER_MTVS_TIME = TimeUtils.DAY;
	private long UPDATE_LOG_INFO_TIME = TimeUtils.DAY;
	
	Context mContext;
	AsyncHttpClient mAhcMtvUrl;
	RequestParams mParams;
	
	public void getHotKeywordList(final PagedDataHandler<String> handler) {
		boolean flag = false;
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(HOT_KEYWORD_INNER_NAME,1);
		if(CacheManager.checkCache(memCacheKey, HOT_KEYWORD_TIME)) { //先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, HOT_KEYWORD_TIME);
				KuwoLog.d(TAG,"getHotKeywordList : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getHotKeywordList : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<String> data = listParser.parseHotKeywordList(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		}
		
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(Constants.HTTP_REQUEST_TIME_OUT);
			client.get(HOT_KEYWORD_URL, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					ListParser listParser = new ListParser();
					PagedData<String> data = listParser.parseHotKeywordList(content);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, content);
					} else {
						KuwoLog.w(TAG,"getHotKeywordList : parseHotKeywordList by content is error : " + content);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 获取榜单列表
	 * @param page
	 * @param handler
	 */
	public void getBarList(final int banginfo, final PagedDataHandler<ImageObject> handler){
		RequestParams params = new RequestParams();
		params.put("banginfo", String.valueOf(banginfo));
		boolean flag = false;
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(BAR_LIST_INNER_NAME+banginfo,1);
		if(CacheManager.checkCache(memCacheKey, BAR_LIST_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, BAR_LIST_TIME);
				KuwoLog.d(TAG,"getBarList : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getBarList : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<ImageObject> data = listParser.parseBarList(banginfo, jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(BAR_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<ImageObject> data = listParser.parseBarList(banginfo, arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						KuwoLog.w(TAG,"getBarList : parseMtvList by ar0 is error : " + arg0);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 获取榜单列表
	 * @param page
	 * @param handler
	 */
	public void getCategoryBarList(final PagedDataHandler<ImageObject> handler){
		RequestParams params = new RequestParams();
		params.put("type", "categorybar");
		boolean flag = false;
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(CATEGORY_BAR_LIST_INNER_NAME, 1);
		if(CacheManager.checkCache(memCacheKey, CATEGORY_BAR_LIST_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, CATEGORY_BAR_LIST_TIME);
				KuwoLog.d(TAG,"getBarList : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getBarList : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<ImageObject> data = listParser.parseCategoryBarList(jsonMess);
				if(data != null){
					if (data.data.size() == 0){
						AsyncHttpClient client = new AsyncHttpClient();
						client.get(CATEGORY_BAR_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
							@Override
							public void onSuccess(String arg0) {
								KuwoLog.d(TAG, arg0);
								ListParser listParser = new ListParser();
								PagedData<ImageObject> data = listParser.parseCategoryBarList(arg0);
								if(data != null){
									if (data.data.size() == 0){
										handler.onFailure();
									}
									handler.onSuccess(data);
									CacheManager.cacheString(memCacheKey, arg0);
								} else {
									KuwoLog.w(TAG,"getBarList : parseMtvList by ar0 is error : " + arg0);
									handler.onFailure();
								}
							}
						});
					}
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(CATEGORY_BAR_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<ImageObject> data = listParser.parseCategoryBarList(arg0);
					if(data != null){
						if (data.data.size() == 0){
							handler.onFailure();
						}
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						KuwoLog.w(TAG,"getBarList : parseMtvList by ar0 is error : " + arg0);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 获取榜单歌曲列表
	 * @param id 榜单id
	 * @param page 页数
	 * @param handler
	 */
	public void getMtvList(String id, int page, final PagedDataHandler<Mtv> handler) {
		RequestParams params = new RequestParams();
		params.put("bangid", id);
		params.put("stage", "0");
		params.put("pn", String.valueOf(page));
		params.put("rn", String.valueOf(MTV_LIST_PAGE_SIZE));
		params.put("r", String.valueOf(System.currentTimeMillis()));
		boolean flag = false;
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(MTV_LIST_INNER_NAME, page,id);
		if(CacheManager.checkCache(memCacheKey, MTV_LIST_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, MTV_LIST_TIME);
				KuwoLog.d(TAG,"getMtvList : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getMtvList : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<Mtv> data = listParser.parseMtvList(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(MTV_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					ListParser listParser = new ListParser();
					PagedData<Mtv> data = listParser.parseMtvList(content);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, content);
					} else {
						KuwoLog.w(TAG,"getMtvList : parseMtvList by ar0 is error : " + content);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	AsyncHttpClient getMtvListBySongClient;
	/**
	 * 获取语种歌曲列表
	 * @param id 榜单id
	 * @param page 页数
	 * @param handler
	 */
	public void getMtvListByLang(String id, int page, final PagedDataHandler<Mtv> handler) {
		//初始化参数.
		page = page - 1;
		RequestParams params = new RequestParams();
		params.put("category", id);
		params.put("stype", "songlist");
		params.put("order", "hot");
		params.put("pn", String.valueOf(page));
		params.put("rn", String.valueOf(MTV_LIST_PAGE_SIZE));
		boolean flag = false;
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(LANG_BAR_LIST_INNER_NAME, page,URLEncoder.encode(id));
		if(CacheManager.checkCache(memCacheKey, LANG_BAR_LIST_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, LANG_BAR_LIST_TIME);
				KuwoLog.d(TAG,"getBarSongList : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getBarSongList : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<Mtv> data = listParser.parseLangList(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			KuwoLog.d(TAG, "getMtvListByLang params="+params.toString());
			client.get(LANG_BAR_SONG_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<Mtv> data = listParser.parseLangList(arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						KuwoLog.w(TAG,"getMtvListByLang : parseMtvList by ar0 is error : " + arg0);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 根据首字母检索获取歌曲列表
	 * @param page
	 * @param handler
	 */
	public void getMtvListBySong(String keyword,int page, final PagedDataHandler<Mtv> handler) {
		//初始化参数.
		RequestParams params = new RequestParams();
		params.put("type", "search");
		params.put("key", keyword);
		params.put("pn", String.valueOf(page));
		params.put("rn", String.valueOf(MTV_LIST_BY_PINYIN_SEARCH));
		boolean flag = false;
		final String memCacheKey = genMemInnerName(MTV_LIST_BY_SONG_INNER_NAME, page,keyword);
		String jsonMess = "";
		
		if(CacheManager.checkCache(memCacheKey, MTV_LIST_BY_SONG_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, MTV_LIST_BY_SONG_TIME);
				KuwoLog.d(TAG,"getMtvListBySong : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getMtvListBySong : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<Mtv> data = listParser.parseMtvListBySong(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			if (getMtvListBySongClient == null) 
				getMtvListBySongClient = new AsyncHttpClient();
			else
				getMtvListBySongClient.cancelRequests(AppContext.context, true);
			
//			ImageLoader.getInstance().denyNetworkDownloads(true);
			getMtvListBySongClient.get(MTV_LIST_BY_SONG_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<Mtv> data = listParser.parseMtvListBySong(arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 根据首字母检索获取歌曲列表
	 * @param page
	 * @param handler
	 */
	public void getMtvListPinyinBySong(String prefix,int page, final PagedDataHandler<Mtv> handler) {
		//初始化参数.
		RequestParams params = new RequestParams();
		params.put("stype", "songlist");
		params.put("category", "all");
		params.put("prefix", prefix);
		params.put("order","dict");
		params.put("pn", String.valueOf(page-1));
		params.put("rn", String.valueOf(MTV_LIST_BY_PINYIN_SEARCH));
		params.put("hasecho","1");
		boolean flag = false;
		final String memCacheKey = genMemInnerName(MTV_LIST__PINYIN_BY_SONG_INNER_NAME, page,prefix);
		String jsonMess = "";
		
		if(CacheManager.checkCache(memCacheKey, MTV_LIST_PINYIN_BY_SONG_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, MTV_LIST_BY_SONG_TIME);
				KuwoLog.d(TAG,"getMtvListPinyinBySong : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getMtvListPinyinBySong : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<Mtv> data = listParser.parseMtvListPinyinBySong(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			if (getMtvListBySongClient == null) 
				getMtvListBySongClient = new AsyncHttpClient();
			else
				getMtvListBySongClient.cancelRequests(AppContext.context, true);
			
//			ImageLoader.getInstance().denyNetworkDownloads(true);
			getMtvListBySongClient.get(MTV_LIST_BY_SONG_PINYIN_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<Mtv> data = listParser.parseMtvListPinyinBySong(arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 根据歌手类型获取歌手列表
	 * @param page
	 * @param handler
	 */
	public void getSingerList(String id,int page, final PagedDataHandler<ImageObject> handler) {
		//初始化参数.
		page = page - 1;
		RequestParams params = new RequestParams();
		params.put("stype","artistlist");
		params.put("category", id);
		params.put("order", "hot");
		params.put("pn", String.valueOf(page));
		params.put("rn", String.valueOf(SINGER_LIST_PAGE_SIZE));
		
		boolean flag = false;
		final String memCacheKey = genMemInnerName(SINGER_LIST_INNER_NAME, page,id);
		String jsonMess = "";
		if(CacheManager.checkCache(memCacheKey, SINGER_LIST_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, SINGER_LIST_TIME);
				KuwoLog.d(TAG,"getSingerList : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getSingerList : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<ImageObject> data = listParser.parseSingerList(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(SINGER_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<ImageObject> data = listParser.parseSingerList(arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getSingerListByKeyword(String category, String keyword, int pageNum, final PagedDataHandler<ImageObject> handler) {
		//初始化参数.
		pageNum = pageNum - 1;
		RequestParams params = new RequestParams();
		params.put("stype","artistlist");
		params.put("category", category);
		params.put("prefix", keyword);
		params.put("order", "dict");
		params.put("pn", String.valueOf(pageNum));
		params.put("rn", String.valueOf(SINGER_LIST_PAGE_SIZE));
		params.put("r", System.currentTimeMillis()+"");
		
		boolean flag = false;
		final String memCacheKey = genMemInnerName(SINGER_LIST_BY_KEYWORD_INNER_NAME, pageNum,category+"_"+keyword);
		String jsonMess = "";
		if(CacheManager.checkCache(memCacheKey, SINGER_LIST_BY_KEYWORD_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, SINGER_LIST_BY_KEYWORD_TIME);
				KuwoLog.d(TAG,"getSingerListByKeyword : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getSingerListByKeyword : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<ImageObject> data = listParser.parseSingerList(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(SINGER_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<ImageObject> data = listParser.parseSingerList(arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 根据歌手获取歌手列表
	 * @param page
	 * @param handler
	 */
	public void getMtvListBySinger(String singer,int page, final PagedDataHandler<Mtv> handler) {
		//初始化参数.
		page = page - 1;
		RequestParams params = new RequestParams();
		params.put("client","ksong");
		params.put("artist",singer);
		params.put("pn", String.valueOf(page));
		params.put("rn", String.valueOf(MTV_LIST_BY_SINGER_PAGE_SIZE));
		params.put("ft","music");
		params.put("newsearch","1");
		params.put("cluster","0");
		params.put("itemset","ksong");
		params.put("strategy","2012");
		params.put("hasmkv","1");
		
		boolean flag = false;
		final String memCacheKey = genMemInnerName(MTV_LIST_BY_SINGER_INNER_NAME, page,singer);
		String jsonMess = "";
		if(CacheManager.checkCache(memCacheKey, MTV_LIST_BY_SINGER_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, MTV_LIST_BY_SINGER_TIME);
				KuwoLog.d(TAG,"getMtvListBySinger : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getMtvListBySinger : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<Mtv> data = listParser.parseMtvListBySinger(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(AppContext.context, MTV_LIST_BY_SINGER_URL, params, "GBK", new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<Mtv> data = listParser.parseMtvListBySinger(arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 获取歌手列表
	 * @param page
	 * @param handler
	 */
	public void getSingerBarList(final PagedDataHandler<ImageObject> handler) {
		RequestParams params = new RequestParams();
		params.put("type", "singerbar");
		boolean flag = false;
		final String memCacheKey = genMemInnerName(BAR_SINGER_LIST_INNER_NAME, 1);
		String jsonMess = "";
		if(CacheManager.checkCache(memCacheKey, BAR_SINGER_LIST_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, BAR_SINGER_LIST_TIME);
				KuwoLog.d(TAG,"getBarSingerList : from cache : " + jsonMess);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getBarSingerList : loadString error . inner_name : " + memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PagedData<ImageObject> data = listParser.parseSingerBarList(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(AppContext.context, BAR_SINGER_LIST_URL, params, "GBK", new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					KuwoLog.d(TAG, arg0);
					ListParser listParser = new ListParser();
					PagedData<ImageObject> data = listParser.parseSingerBarList(arg0);
					if(data != null){
						handler.onSuccess(data);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	/**
	 * 获取作品播放地址.
	 * @param musicId
	 * @param handler
	 */
	public void getMtvUrl(String musicId,final DataHandler<String> handler){
		if (mParams == null){
			mParams = new RequestParams();
		}
		mParams.put("type","convert_url");
		mParams.put("format","mkv");
		mParams.put("coop", "android");//TODO: AppContext.DEVICE_ID);
		mParams.put("rid","MUSIC_" + musicId);
//		AsyncHttpClient client = new AsyncHttpClient();
//		client.get(SONG_URL, params, new DefaultAsyncHttpResponseHandler(handler)
		if (mAhcMtvUrl==null)
			mAhcMtvUrl = new AsyncHttpClient();
		mAhcMtvUrl.cancelRequests(mContext, true);
		mAhcMtvUrl.get(mContext, SONG_URL, mParams, new DefaultAsyncHttpResponseHandler(handler){
			@Override
			public void onSuccess(String content) {
				if(content != null){
					ListParser listParser = new ListParser();
					String url = listParser.parseMtvUrl(content);
					if(url != null && url.length() > 0){
						handler.onSuccess(url);
					} else {
						handler.onFailure();
					}
				}
			}
		});
	}
	
	public void getUserMtvUrl(String id, final DataHandler<String> handler) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(URL_USER_MTV_REQUEST, params, new DefaultAsyncHttpResponseHandler(handler) {
			
			@Override
			public void onSuccess(String content) {
				if(content != null) {
					ListParser parser = new ListParser();
					String url = parser.parseUserMtvUrl(content);
					if(url != null && url.length() > 0)
						handler.onSuccess(url);
					else 
						handler.onFailure();
				}
			}
		});
	}
	/**
	 * 获取作品歌词文件.
	 * @param musicId MUSIC_1019396
	 * @param handler
	 * @throws IOException 
	 */
	public void getLyricSong(String rid,final DataHandler<Lyric> handler){
//		lyricFile = DirectoryManager.getFile(DirContext.LYRICS, rid);
		lyricFile = new File(Environment.getDataDirectory()+"/lyrics/"+rid);
		String url = LYRIC_URL + "?id=" + rid + "&type=kdatx";
		if(lyricFile != null && lyricFile.length() > 0){
			FileInputStream fis;
			try {
				fis = new FileInputStream (lyricFile);
				byte[] bytes = IOUtils.readLeftBytes(fis);
				if(bytes != null){
					KdtxParser kdtxParser = new KdtxParser();
					Lyric lyric = kdtxParser.parserLyrics(null,bytes);
					handler.onSuccess(lyric);//获取成功
				}
				fis.close();
				
			} catch (FileNotFoundException e) {
				handler.onFailure(e,"FileNotFoundException : get rid=" + rid + " lyric error.");
				KuwoLog.e(TAG, e.getMessage());
			} catch (IOException e){
				handler.onFailure(e, "IOException : get rid=" + rid + " lyric error.");
				KuwoLog.e(TAG, e.getMessage());
			} finally {
				fis = null;
				lyricFile = null;
			}
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, new BinaryHttpResponseHandler() {
			    @Override
			    public void onSuccess(byte[] fileData) {
			    	MobclickAgent.onEvent(AppContext.context, Constants.KS_UMENG_DOWN_LYRIC, Constants.KS_UMENG_SUCCESS);
			    	if(fileData != null && fileData.length > 0){
			    		try {
			    			KdtxParser kdtxParser = new KdtxParser();
							Lyric lyric = kdtxParser.parserLyrics(null,fileData);
				    		handler.onSuccess(lyric);
//			    			FileOutputStream out =new FileOutputStream(lyricFile.getAbsoluteFile(), false);
//							out.write(fileData);
						} catch (FileNotFoundException e) {
							KuwoLog.printStackTrace(e);
						} catch (IOException e) {
							KuwoLog.printStackTrace(e);
						} finally {
							KuwoLog.d(TAG, "lyricFile.length : " + lyricFile.length());
							lyricFile = null;
						}
			    	} else {
			    		handler.onFailure();
			    	}
			    }
			    @Override
			    public void onFailure(Throwable error) {
			    	MobclickAgent.onEvent(AppContext.context, Constants.KS_UMENG_DOWN_LYRIC, Constants.KS_UMENG_FAIL);	    	
			    	handler.onFailure();
			    	super.onFailure(error);
			    	
			    }
			    
			    @Override
			    public void onFailure(Throwable error, String content) {
			    	//MobclickAgent.onEvent(AppContext.context, Constants.KS_UMENG_DOWN_LYRIC, Constants.KS_UMENG_FAIL);
			    	//handler.onFailure();
			    	super.onFailure(error, content);
			    }
			});

		}
	}
	/**
	 * 获取开机背景图.
	 * @param handler
	 * @return return ImageObject id:status name:status pic:url
	 */
	public void getTopPic(final DataHandler<ImageObject> handler){
		RequestParams params = new RequestParams();
		params.put("type","topPic");
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(TOP_PIC, params, new DefaultAsyncHttpResponseHandler(handler){
			@Override
			public void onSuccess(String content) {
				if(content != null){
					ListParser listParser = new ListParser();
					ImageObject imageObject = listParser.parseTopPic(content);
					if(imageObject != null && imageObject.pic != null){
						handler.onSuccess(imageObject);
					} else {
						handler.onFailure();
					}
				} else {
					handler.onFailure();
				}
			}
			
			@Override
			public void onFailure(Throwable t, String content) {
			}
		});
	}
	
	/**
	 *  搜索精简化请求写法
	 * 
	 *  http://mboxspace.kuwo.cn/ks/st/tips?key=xia&_=1375948196434
	 * @param keyword
	 * @param handler
	 */
	public void getSearchPromptList(String keyword, final PagedDataHandler<SearchPromptObject> handler ) {
		RequestParams params = new RequestParams();
		params.put("key", keyword);
		params.put("_", UUID.randomUUID().toString());
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(SEARCH_PROMPT_INNER_NAME, 1); //cache the search prompt from 1 
		if(CacheManager.checkCache(memCacheKey, SEARCH_PROMPT_TIME)) {
			try {
				jsonMess = CacheManager.loadString(memCacheKey, SEARCH_PROMPT_TIME);
			} catch (IOException e) {
				jsonMess = null;
				KuwoLog.e(TAG, "getSearchPromptList : loading cache content from local file fail, inner_name="+memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonMess != null && jsonMess.length() > 0) {
				ListParser parser = new ListParser();
				PagedData<SearchPromptObject> pagedData = parser.parseSearchPromptContent(jsonMess);
				handler.onSuccess(pagedData);
			}
		}else {
			new AsyncHttpClient().get(URL_SEARCH_PROMPT, params, new DefaultAsyncHttpResponseHandler(handler) {
				
				@Override
				public void onSuccess(String content) {
					if(!TextUtils.isEmpty(content)) {
						ListParser parser = new ListParser();
						PagedData<SearchPromptObject> pagedData = parser.parseSearchPromptContent(content);
						if(pagedData.data != null && pagedData.data.size() > 0) 
							handler.onSuccess(pagedData);
						else
							handler.onFailure();
					}else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param handler
	 */
	public void getUserMtvList(int pageNum, final PagedDataHandler<UserMtv> handler) {
		RequestParams params = new RequestParams();
		params.put("dataType", "rmzp");
		params.put("returntype", "json");
		params.put("pn", String.valueOf(pageNum));
		params.put("ps", String.valueOf(USER_MTV_LIST_PAGE_SIZE));
		String jsonContent = null;
		final String memCacheKey = genMemInnerName(USER_MTVS_INNER_NAME, pageNum);
		if(CacheManager.checkCache(memCacheKey, USER_MTVS_TIME)) {
			try {
				jsonContent = CacheManager.loadString(memCacheKey, USER_MTVS_TIME);
			} catch (IOException e) {
				jsonContent = null;
				KuwoLog.e(TAG, "getUserMtvList : loading cache content from local file fail, inner_name="+memCacheKey);
				KuwoLog.printStackTrace(e);
			}
			if(jsonContent != null && jsonContent.length() > 0) {
				ListParser parser = new ListParser();
				PagedData<UserMtv> pagedData = parser.parseUserMtvList(jsonContent);
				handler.onSuccess(pagedData);
			}
		}else {
			new AsyncHttpClient().get(URL_HOT_LIST_USER_MTV, params, new DefaultAsyncHttpResponseHandler(handler) {
				
				@Override
				public void onSuccess(String content) {
					if(!TextUtils.isEmpty(content)) {
						ListParser parser = new ListParser();
						PagedData<UserMtv> pagedData = parser.parseUserMtvList(content);
						handler.onSuccess(pagedData);
					}else {
						handler.onFailure();
					}
				}
			});
		}
	}
	/**
	 * 获取缓存关键字
	 * @param innerName 各功能内部名
	 * @param page 页数
	 * @param type 类型（可省略）
	 * @return
	 */
	public String genMemInnerName(String innerName,int page,String type){
		if(innerName == null || innerName.length() <=0){
			return "";
		}
		if(type == null || type.equals("")){
			return innerName + "_" + page;
		} else {
			return innerName + "_" + type + "_" + page;
		}
	}
	/**
	 * 获取缓存关键字
	 * @param innerName 各功能内部名
	 * @param page 类型（可省略）
	 * @return
	 */
	public String genMemInnerName(String innerName,int page){
		return genMemInnerName(innerName, page, null);
	}
	
	
	/**
	 * 获取更新日志
	 */
	public void getUpdateLog(final DataHandler<String> handler) {
		RequestParams params = new RequestParams();
		params.put("type", "version");
		boolean flag = false;
		final String memCacheKey = genMemInnerName(UPDATE_LOG_INFO, 1);
		String jsonMess = "";
		if(CacheManager.checkCache(memCacheKey, UPDATE_LOG_INFO_TIME)){//先从内存中获得.
			try {
				jsonMess = CacheManager.loadString(memCacheKey, BAR_SINGER_LIST_TIME);

			} catch (IOException e) {
				jsonMess = null;
			}
			if(jsonMess != null && jsonMess.length() > 0){
				handler.onSuccess(jsonMess);
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(AppContext.context, BAR_SINGER_LIST_URL, params, "GBK", new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String arg0) {
					if(arg0 != null){
						handler.onSuccess(arg0);
						CacheManager.cacheString(memCacheKey, arg0);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}

	public void setContext(Context context) {
		mContext = context;
	}
	
	public void delContext(){
		if (mContext != null && mAhcMtvUrl != null){
			mAhcMtvUrl.cancelRequests(mContext, true);
		}
	}
	
}
