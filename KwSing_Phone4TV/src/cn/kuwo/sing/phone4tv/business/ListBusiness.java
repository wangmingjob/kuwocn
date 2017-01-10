/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.business;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import android.text.TextUtils;
import cn.kuwo.sing.phone4tv.bean.ImageObject;
import cn.kuwo.sing.phone4tv.bean.Mtv;
import cn.kuwo.sing.phone4tv.bean.PageData;
import cn.kuwo.sing.phone4tv.bean.SearchPromptObject;
import cn.kuwo.sing.phone4tv.bean.UserMtv;
import cn.kuwo.sing.phone4tv.commmons.log.LogSystem;
import cn.kuwo.sing.phone4tv.commons.context.AppContext;
import cn.kuwo.sing.phone4tv.commons.context.Constants;
import cn.kuwo.sing.phone4tv.commons.file.FileCacheManager;
import cn.kuwo.sing.phone4tv.commons.http.AsyncHttpClient;
import cn.kuwo.sing.phone4tv.commons.http.RequestParams;
import cn.kuwo.sing.phone4tv.commons.util.DataHandler;
import cn.kuwo.sing.phone4tv.commons.util.DefaultAsyncHttpResponseHandler;
import cn.kuwo.sing.phone4tv.commons.util.PageDataHandler;
import cn.kuwo.sing.phone4tv.commons.util.TimeUtils;


/**
 * @Package cn.kuwo.sing.phone4tv.business
 *
 * @Date 2014-2-25, 下午12:22:38
 *
 * @Author wangming
 *
 */
public class ListBusiness {
	private static final String LOG_TAG = "ListBusiness";
	
	//热门推荐
	private static final String MTV_CATEGORY_INNER_NAME = "MTV_CATEGORY";
	private static final long MTV_CATEGORY_CACHE_TIME = TimeUtils.DAY;
	private static final String MTV_CATEGORY_URL = "http://kbangserver.kuwo.cn/ksong.s"; 
	
	//分类点歌
	private static final String MTV_CATEGORY_ORDER_INNER_NAME = "MTV_CATEGORY_ORDER";
	private static final long MTV_CATEGORY_ORDER_CACHE_TIME = TimeUtils.DAY;
	private static final String MTV_CATEGORY_ORDER_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData";
	
	//歌手点歌
	private static final String SINGER_CATEGORY_INNER_NAME = "SINGER_CATEGORY";
	private static final long SINGER_CATEGORY_CACHE_TIME = TimeUtils.DAY;
	private static final String SINGER_CATEGORY_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData";
	
	//详细MTV list
	private static final String DETAIL_MTV_LIST_INNER_NAME = "DETAIL_MTV_LIST";
	private static final long DETAIL_MTV_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String DETAIL_MTV_LIST_URL = "http://kbangserver.kuwo.cn/ksong.s";
	
	//网友翻唱
	private static final String USER_DETAIL_MTV_LIST_INNER_NAME = "USER_DETAIL_MTV_LIST";
	private static final long USER_DETAIL_MTV_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String USER_DETAIL_MTV_LIST_URL = "http://changba.kuwo.cn/kge/web/HotDataServer";
	
	//各个语种
	private static final String LANGUAGE_DETAIL_MTV_LIST_INNER_NAME = "LANGUAGE_DETAIL_MTV_LIST";
	private static final long LANGUAGE_DETAIL_MTV_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String LANGUAGE_DETAIL_MTV_LIST_URL = "http://mboxspace.kuwo.cn/ks/kwsing/s.list";
	
	//各个语种
	private static final String SINGER_DETAIL_MTV_LIST_INNER_NAME = "SINGER_DETAIL_MTV_LIST";
	private static final long SINGER_DETAIL_MTV_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String SINGER_DETAIL_MTV_LIST_URL = "http://search.kuwo.cn/r.s";
	
	//歌手列表
	private static final String SINGER_LIST_INNER_NAME = "SINGER_LIST_INNER_NAME";
	private static final long SINGER_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String SINGER_LIST_URL = "http://mboxspace.kuwo.cn/ks/kwsing/s.list";
	
	//搜索列表
	private static final String SEARCH_LIST_INNER_NAME = "SEARCH_LIST_INNER_NAME";
	private static final long SEARCH_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String SEARCH_LIST_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData";
	
	//搜索列表
	private static final String SEARCH_SINGER_LIST_INNER_NAME = "SEARCH_SINGER_LIST_INNER_NAME";
	private static final long SEARCH_SINGER_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String SEARCH_SINGER_LIST_URL = "http://mboxspace.kuwo.cn/ks/kwsing/s.list";
	
	//搜索提示列表
	private static final String SEARCH_PROMPT_LIST_INNER_NAME = "SEARCH_PROMPT_LIST_INNER_NAME";
	private static final long SEARCH_PROMPT_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String SEARCH_PROMPT_LIST_URL = "http://mboxspace.kuwo.cn/ks/st/tips";
	
	//搜索提示列表
	private static final String SEARCH_HOTKEYWORD_LIST_INNER_NAME = "SEARCH_HOTKEYWORD_LIST_INNER_NAME";
	private static final long SEARCH_HOTKEYWORD_LIST_CACHE_TIME = TimeUtils.DAY;
	private static final String SEARCH_HOTKEYWORD_LIST_URL = "http://mboxspace.kuwo.cn/ks/tv/getTVData?type=hotkey";
	
	//更新日志
	private static final String UPDATE_LOG_INFO_INNER_NAME = "UPDATE_LOG_INFO_INNER_NAME";
	private static final long UPDATE_LOG_INFO_CACHE_TIME = TimeUtils.DAY;
	private static final String UPDATE_LOG_INFO_URL = "http://60.28.200.79/ks/tv/getTVData?type=apk"; //Test url
	
	/**
	 * 获取缓存关键字
	 * @param innerName 各功能内部名
	 * @param page 页数
	 * @param type 类型（可省略）
	 * @return
	 */
	private String genMemInnerName(String innerName,String type, int page){
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
	private String genMemInnerName(String innerName,int page){
		return genMemInnerName(innerName, null, page);
	}
	
	/**
	 * 
	 * @param banginfo
	 * @param handler
	 */
	public void getMtvCategoryPageData(final int banginfo, final PageDataHandler<ImageObject> handler){
		RequestParams params = new RequestParams();
		params.put("banginfo", String.valueOf(banginfo));
		String json = "";
		final String memCacheKey = genMemInnerName(MTV_CATEGORY_INNER_NAME, String.valueOf(banginfo), 1);
		if(FileCacheManager.checkCache(memCacheKey, MTV_CATEGORY_CACHE_TIME)){ //先从内存中获得.
			handler.onStart();
			json = FileCacheManager.loadString(memCacheKey, MTV_CATEGORY_CACHE_TIME);
			handler.onFinish();
			if(json != null && json.length() > 0){
				ListParser listParser = new ListParser();
				PageData<ImageObject> data = listParser.parseMtvCategoryContent(banginfo, json);
				if(data != null)
					handler.onSuccess(data);
				else 
					handler.onFailure();
			}
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(MTV_CATEGORY_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<ImageObject> data = listParser.parseMtvCategoryContent(banginfo, content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 获取分类点歌
	 * @param page
	 * @param handler
	 */
	public void getMtvCategoryOrderPageData(final PageDataHandler<ImageObject> handler){
		RequestParams params = new RequestParams();
		params.put("type", "categorybar");
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(MTV_CATEGORY_ORDER_INNER_NAME, 1);
		if(FileCacheManager.checkCache(memCacheKey, MTV_CATEGORY_ORDER_CACHE_TIME)){//先从内存中获得.
			handler.onStart();
			jsonMess = FileCacheManager.loadString(memCacheKey, MTV_CATEGORY_ORDER_CACHE_TIME);
			handler.onFinish();
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<ImageObject> data = listParser.parseMtvCategoryOrderContent(jsonMess);
				if(data != null)
					handler.onSuccess(data);
				else 
					handler.onFailure();
			}
		} else { 
			//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(MTV_CATEGORY_ORDER_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<ImageObject> data = listParser.parseMtvCategoryOrderContent(content);
					if(data != null){
						if (data.data.size() == 0){
							handler.onFailure();
						}
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
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
	public void getSingerCategoryPageData(final PageDataHandler<ImageObject> handler) {
		RequestParams params = new RequestParams();
		params.put("type", "singerbar");
		boolean flag = false;
		final String memCacheKey = genMemInnerName(SINGER_CATEGORY_INNER_NAME, 1);
		String jsonMess = "";
		if(FileCacheManager.checkCache(memCacheKey, SINGER_CATEGORY_CACHE_TIME)){//先从内存中获得.
			handler.onStart();
			jsonMess = FileCacheManager.loadString(memCacheKey, SINGER_CATEGORY_CACHE_TIME);
			handler.onFinish();
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<ImageObject> data = listParser.parseSingerCategoryContent(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(AppContext.context, SINGER_CATEGORY_URL, params, "GBK", new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<ImageObject> data = listParser.parseSingerCategoryContent(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getMtvPageData4Common(String id, int pageNum, final PageDataHandler<Mtv> handler) {
		RequestParams params = new RequestParams();
		params.put("bangid", id);
		params.put("stage", "0");
		params.put("pn", String.valueOf(pageNum));
		params.put("rn", String.valueOf(Constants.PAGE_SIZE_DETAIL_MTV_LIST));
		params.put("r", String.valueOf(System.currentTimeMillis()));
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(DETAIL_MTV_LIST_INNER_NAME, id, pageNum);
		if(FileCacheManager.checkCache(memCacheKey, DETAIL_MTV_LIST_CACHE_TIME)){//先从内存中获得.
			handler.onStart();
			jsonMess = FileCacheManager.loadString(memCacheKey, DETAIL_MTV_LIST_CACHE_TIME);
			LogSystem.d(LOG_TAG,"getMtvPageData4Common : from cache : " + jsonMess);
			handler.onFinish();
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<Mtv> data = listParser.parseMtvListContent4Common(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
			}
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(DETAIL_MTV_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					ListParser listParser = new ListParser();
					PageData<Mtv> data = listParser.parseMtvListContent4Common(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						LogSystem.e(LOG_TAG,"getMtvPageData4Common : parseMtvList by ar0 is error : " + content);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getMtvPageData4User(int pageNum, final PageDataHandler<UserMtv> handler) {
		RequestParams params = new RequestParams();
		params.put("dataType", "rmzp");
		params.put("returntype", "json");
		params.put("pn", String.valueOf(pageNum));
		params.put("ps", String.valueOf(Constants.PAGE_SIZE_USER_DETAIL_MTV_LIST));
		String jsonContent = null;
		final String memCacheKey = genMemInnerName(USER_DETAIL_MTV_LIST_INNER_NAME, pageNum);
		if(FileCacheManager.checkCache(memCacheKey, USER_DETAIL_MTV_LIST_CACHE_TIME)) {
			handler.onStart();
			jsonContent = FileCacheManager.loadString(memCacheKey, USER_DETAIL_MTV_LIST_CACHE_TIME);
			handler.onFinish();
			if(jsonContent != null && jsonContent.length() > 0) {
				ListParser parser = new ListParser();
				PageData<UserMtv> pagedData = parser.parseMtvListContent4User(jsonContent);
				handler.onSuccess(pagedData);
			}
		}else {
			new AsyncHttpClient().get(USER_DETAIL_MTV_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler) {
				
				@Override
				public void onSuccess(String content) {
					if(!TextUtils.isEmpty(content)) {
						ListParser parser = new ListParser();
						PageData<UserMtv> pagedData = parser.parseMtvListContent4User(content);
						if(pagedData != null) {
							FileCacheManager.cacheString(memCacheKey, content);
							handler.onSuccess(pagedData);
						}
					}else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getMtvPageData4Language(String id, int pageNum, final PageDataHandler<Mtv> handler) {
		//初始化参数.
		pageNum = pageNum - 1;
		RequestParams params = new RequestParams();
		params.put("category", id);
		params.put("stype", "songlist");
		params.put("order", "hot");
		params.put("pn", String.valueOf(pageNum));
		params.put("rn", String.valueOf(Constants.PAGE_SIZE_DETAIL_MTV_LIST));
		boolean flag = false;
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(LANGUAGE_DETAIL_MTV_LIST_INNER_NAME, URLEncoder.encode(id), pageNum);
		if(FileCacheManager.checkCache(memCacheKey, LANGUAGE_DETAIL_MTV_LIST_CACHE_TIME)){//先从内存中获得.
			handler.onStart();
			jsonMess = FileCacheManager.loadString(memCacheKey, LANGUAGE_DETAIL_MTV_LIST_CACHE_TIME);
			handler.onFinish();
			LogSystem.d(LOG_TAG,"getMtvPageData4Language : from cache : " + jsonMess);
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<Mtv> data = listParser.parseMtvListContent4Language(jsonMess);
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
			LogSystem.d(LOG_TAG, "getMtvListByLang params="+params.toString());
			client.get(LANGUAGE_DETAIL_MTV_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<Mtv> data = listParser.parseMtvListContent4Language(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						LogSystem.e(LOG_TAG,"getMtvPageData4Language : parseMtvList by ar0 is error : " + content);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getMtvPageData4Singer(String singer,int pageNum, final PageDataHandler<Mtv> handler) {
		//初始化参数.
		pageNum = pageNum - 1;
		RequestParams params = new RequestParams();
		params.put("client","ksong");
		params.put("artist",singer);
		params.put("pn", String.valueOf(pageNum));
		params.put("rn", String.valueOf(Constants.PAGE_SIZE_DETAIL_MTV_LIST));
		params.put("ft","music");
		params.put("newsearch","1");
		params.put("cluster","0");
		params.put("itemset","ksong");
		params.put("strategy","2012");
		params.put("hasmkv","1");
		
		boolean flag = false;
		final String memCacheKey = genMemInnerName(SINGER_DETAIL_MTV_LIST_INNER_NAME, singer, pageNum);
		String jsonMess = "";
		if(FileCacheManager.checkCache(memCacheKey, SINGER_DETAIL_MTV_LIST_CACHE_TIME)){//先从内存中获得.
			handler.onStart();
			jsonMess = FileCacheManager.loadString(memCacheKey, SINGER_DETAIL_MTV_LIST_CACHE_TIME);
			handler.onFinish();
			LogSystem.d(LOG_TAG,"getMtvPageData4Singer : from cache : " + jsonMess);
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<Mtv> data = listParser.parseMtvListContent4Singer(jsonMess);
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
			client.get(AppContext.context, SINGER_DETAIL_MTV_LIST_URL, params, "GBK", new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<Mtv> data = listParser.parseMtvListContent4Singer(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getSingerPageData(String id,int pageNum, final PageDataHandler<ImageObject> handler) {
		//初始化参数.
		pageNum = pageNum - 1;
		RequestParams params = new RequestParams();
		params.put("stype","artistlist");
		params.put("category", id);
		params.put("order", "hot");
		params.put("pn", String.valueOf(pageNum));
		params.put("rn", String.valueOf(Constants.PAGE_SIZE_SINGER_LIST));
		
		final String memCacheKey = genMemInnerName(SINGER_LIST_INNER_NAME, id, pageNum);
		String jsonMess = "";
		if(FileCacheManager.checkCache(memCacheKey, SINGER_LIST_CACHE_TIME)){//先从内存中获得.
			handler.onStart();
			jsonMess = FileCacheManager.loadString(memCacheKey, SINGER_LIST_CACHE_TIME);
			handler.onFinish();
			LogSystem.d(LOG_TAG,"getSingerPageData : from cache : " + jsonMess);
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<ImageObject> data = listParser.parseSingerListContent(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
			}
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(SINGER_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<ImageObject> data = listParser.parseSingerListContent(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	/**
	 * 根据首字母检索获取歌曲列表
	 * @param pageNum
	 * @param handler
	 */
	public void getMtvListPageDataBySearch(String keyword,int pageNum, final PageDataHandler<Mtv> handler) {
		//初始化参数.
		RequestParams params = new RequestParams();
		params.put("type", "search");
		params.put("key", keyword);
		params.put("pn", String.valueOf(pageNum));
		params.put("rn", String.valueOf(Constants.PAGE_SIZE_DETAIL_MTV_LIST));
		final String memCacheKey = genMemInnerName(SEARCH_LIST_INNER_NAME, keyword, pageNum);
		String jsonMess = "";
		
		if(FileCacheManager.checkCache(memCacheKey, SEARCH_LIST_CACHE_TIME)){//先从内存中获得.
			jsonMess = FileCacheManager.loadString(memCacheKey, SEARCH_LIST_CACHE_TIME);
			LogSystem.d(LOG_TAG,"getMtvListPageDataBySearch : from cache : " + jsonMess);
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<Mtv> data = listParser.parseMtvListContentBySearch(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
			}
		} else {
			new AsyncHttpClient().get(SEARCH_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<Mtv> data = listParser.parseMtvListContentBySearch(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getSingerListPageDataByKeyword(String category, String keyword, int pageNum, final PageDataHandler<ImageObject> handler) {
		//初始化参数.
		pageNum = pageNum - 1;
		RequestParams params = new RequestParams();
		params.put("stype","artistlist");
		params.put("category", category);
		params.put("prefix", keyword);
		params.put("order", "dict");
		params.put("pn", String.valueOf(pageNum));
		params.put("rn", String.valueOf(Constants.PAGE_SIZE_SINGER_LIST));
		params.put("r", System.currentTimeMillis()+"");
		
		boolean flag = false;
		final String memCacheKey = genMemInnerName(SEARCH_SINGER_LIST_INNER_NAME, category+"_"+keyword, pageNum);
		String jsonMess = "";
		if(FileCacheManager.checkCache(memCacheKey, SEARCH_SINGER_LIST_CACHE_TIME)){//先从内存中获得.
			jsonMess = FileCacheManager.loadString(memCacheKey, SEARCH_SINGER_LIST_CACHE_TIME);
			LogSystem.d(LOG_TAG,"getSingerListPageDataByKeyword : from cache : " + jsonMess);
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<ImageObject> data = listParser.parseSingerListContentBySearch(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
				flag = true;
			}
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(SEARCH_SINGER_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					LogSystem.d(LOG_TAG, content);
					ListParser listParser = new ListParser();
					PageData<ImageObject> data = listParser.parseSingerListContentBySearch(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getHotKeywordListPageData(final PageDataHandler<String> handler) {
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(SEARCH_HOTKEYWORD_LIST_INNER_NAME,1);
		if(FileCacheManager.checkCache(memCacheKey, SEARCH_HOTKEYWORD_LIST_CACHE_TIME)) { //先从内存中获得.
			jsonMess = FileCacheManager.loadString(memCacheKey, SEARCH_HOTKEYWORD_LIST_CACHE_TIME);
			LogSystem.d(LOG_TAG,"getHotKeywordListPageData : from cache : " + jsonMess);
			if(jsonMess != null && jsonMess.length() > 0){
				ListParser listParser = new ListParser();
				PageData<String> data = listParser.parseHotKeywordContent(jsonMess);
				if(data != null){
					handler.onSuccess(data);
				} else {
					handler.onFailure();
				}
			}
		}else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(Constants.HTTP_REQUEST_TIME_OUT);
			client.get(SEARCH_HOTKEYWORD_LIST_URL, new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					ListParser listParser = new ListParser();
					PageData<String> data = listParser.parseHotKeywordContent(content);
					if(data != null){
						handler.onSuccess(data);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						LogSystem.e(LOG_TAG,"getHotKeywordListPageData : parseHotKeywordList by content is error : " + content);
						handler.onFailure();
					}
				}
			});
		}
	}
	
	public void getSearchSuggestListPageData(String keyword, final PageDataHandler<SearchPromptObject> handler ) {
		RequestParams params = new RequestParams();
		params.put("key", keyword);
		params.put("_", UUID.randomUUID().toString());
		String jsonMess = "";
		final String memCacheKey = genMemInnerName(SEARCH_PROMPT_LIST_INNER_NAME, 1); //cache the search prompt from 1 
		if(FileCacheManager.checkCache(memCacheKey, SEARCH_PROMPT_LIST_CACHE_TIME)) {
			jsonMess = FileCacheManager.loadString(memCacheKey, SEARCH_PROMPT_LIST_CACHE_TIME);
			if(jsonMess != null && jsonMess.length() > 0) {
				ListParser parser = new ListParser();
				PageData<SearchPromptObject> pagedData = parser.parseSearchPromptContent(jsonMess);
				handler.onSuccess(pagedData);
			}
		}else {
			new AsyncHttpClient().get(SEARCH_PROMPT_LIST_URL, params, new DefaultAsyncHttpResponseHandler(handler) {
				
				@Override
				public void onSuccess(String content) {
					if(!TextUtils.isEmpty(content)) {
						ListParser parser = new ListParser();
						PageData<SearchPromptObject> pagedData = parser.parseSearchPromptContent(content);
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
	 * 获取更新日志
	 */
	public void getUpdateLog(final DataHandler<String> handler) {
		RequestParams params = new RequestParams();
		params.put("type", "version");
		boolean flag = false;
		final String memCacheKey = genMemInnerName(UPDATE_LOG_INFO_INNER_NAME, 1);
		String jsonMess = "";
		if(FileCacheManager.checkCache(memCacheKey, UPDATE_LOG_INFO_CACHE_TIME)){//先从内存中获得.
			jsonMess = FileCacheManager.loadString(memCacheKey, UPDATE_LOG_INFO_CACHE_TIME);
			if(jsonMess != null && jsonMess.length() > 0){
				handler.onSuccess(jsonMess);
				flag = true;
			}
		} 
		if(!flag){//缓存中没有获取缓存获取失败，从网络请求.
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(AppContext.context, UPDATE_LOG_INFO_URL, params, "GBK", new DefaultAsyncHttpResponseHandler(handler){
				@Override
				public void onSuccess(String content) {
					if(content != null){
						handler.onSuccess(content);
						FileCacheManager.cacheString(memCacheKey, content);
					} else {
						handler.onFailure();
					}
				}
			});
		}
	}
	
}