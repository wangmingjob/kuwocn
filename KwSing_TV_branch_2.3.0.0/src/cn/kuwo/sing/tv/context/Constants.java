package cn.kuwo.sing.tv.context;

import java.util.HashSet;
import java.util.Set;

import com.umeng.analytics.MobclickAgent;

import android.media.AudioFormat;
import android.view.KeyEvent;

public class Constants {
	
	public static final String TEST_ADDRESS = "";
	public static final String OFFICIAL_ADDRESS = "";
	public static final String MTV_ORI_ACCOMP = "mtv_ori_accomp";
	public static final String PLAYBACK_MODE = "playBackMode";
	public static final String HARDWARE_DECODE	= "hardware_decode";
	public static final int PLAYBACK_MODE_HARDWARE = 0;
	public static final int PLAYBACK_MODE_SOFTWARE = 1;
	public static final int PLAYBACK_MODE_NO_MIC = 2;
	public static final int BAR_LIST_BANGINFO_DEFAULT = 1;
	public static final int BAR_LIST_BANGINFO_CATEGORY = 2;
	public static final int HTTP_REQUEST_TIME_OUT = 8000;
	public static final int FROM_PINYIN = 1;	//通过拼音进入详情页
	public static final int FROM_HOTBAR = 2;	//通过榜单进入详情页 
	public static final int FROM_SINGER = 3;	//通过歌手进入详情页
	public static final int FROM_CATEGORY = 4;	//通过分类点歌进入详情页
	public static final int ORDERD_MTV_PAGESIZE = 8;	//已点歌曲每页显示的歌曲数
	
	public static final int SERVER_UDP_PORT = 8600;
	public static final String MULTICAST_IP = "233.0.0.1";
	public static final int MULTICAST_PORT = 8601;
	
	public static final int BUFFER_SIZE_READ = 1024;
	public static final int BUFFER_SIZE_WRITE = 10485760; //NIO ByteBuffer会报BufferOverflowException，http://www.blogjava.net/nokiaguy/archive/2009/10/09/297494.html
	
	public static boolean isUseBackground = true;
	public static boolean isUseAnimation = true;
	public static final int SERVER_PORT = 8384;
	public static final int SCREEN_WIDTH_PIXELS_1920 = 1920;
	public static final int SCREEN_DENSITY_DPI_240 = 240;
	public static final float XIAOMI_NEWTV_SCALE = 0.75f;
	
	/**
	 * 应用名称
	 */
	public static final String APP_NAME = "kwsing";
	public static final  float TARGET_HEAP_UTILIZATION = 0.75f;
	public final static int CWJ_HEAP_SIZE = 6* 1024* 1024 ;
	public static final int BITMAP_WIDTH = 320;
	public static final int BITMAP_HEIGHT = 320;
	
	/**
     * 屏幕亮度
     */
	public static final float DEFAULT_BRIGHTNESS = 0.8f;
	public static final int BRIGHTNESS_MODE_NIGHT = 1;
	public static final int BRIGHTNESS_MODE_DAY = 0;
    
	/**
	 * 程序运行所需的最小SD卡空间
	 */
	public static final int MIN_SD_SPACE = 50* 1024 * 1024;   // 50M

	// Preferences
	public static final String PREFERENCES_HAS_ACTIVATED = "has_activated";	// 是否激活
	public static final String LAST_SCREEN = "last_screen";	// 是否激活
	public static final String ORDER_RECORD_SONG_HAS_ACTIVATED = "order_record_song_activated";
	
	public static final String ACTION_REMOTE_CMD = "action_remote_cmd";
	
	/**
	 * 录音相关参数
	 */
	public static final int RECORDER_SAMPLE_RATE_44100 = 44100;//采样率
	public static final int RECORDER_SAMPLE_RATE_8000 = 8000;//采样率
	public static final int RECORDER_BIT_RATE = 48000; //录音aac文件比特率
	public static final int RECORDER_CHANNEL_COUNT = 2; // 通道数
	public static final int RECORDER_CHANNEL_CONFIG =  16;// AudioFormat.CHANNEL_STEREO=3.  AudioFormat.CHANNEL_MONO=2
	public static final int RECORDER_AUDIO_FORMAT  = 2;  //AudioFormat.ENCODING_PCM_16BIT;
	public static final int RECORDER_BPP = 16;	// 录音采样深度 bits per sample
	
	//友盟统计
	//操作结果等
	public static final String KS_UMENG_SEARCH_MUSIC = "KS_UMENG_SEARCH_MUSIC";
	public static final String KS_UMENG_PLAY_MUSIC  = "KS_UMENG_PLAY_MUSIC";
	public static final String KS_UMENG_OPEN_MUSIC = "KS_UMENG_OPEN_MUSIC";
	public static final String KS_UMENG_GET_SONGURL = "KS_UMENG_GET_SONGURL";
	public static final String KS_UMENG_DOWN_SONGLIST = "KS_UMENG_DOWN_SONGLIST";
	public static final String KS_UMENG_DOWN_MUSIC  = "KS_UMENG_DOWN_MUSIC";
	public static final String KS_UMENG_DOWN_LYRIC = "KS_UMENG_DOWN_LYRIC";
	public static final String KS_UMENG_SEARCH_MUSIC_PROMPT = "KS_UMENG_SEARCH_MUSIC_PROMPT";
	
	//页面访问
	public static final String KS_UMENG_HOTBAR_DETAIL = "KS_UMENG_HOTBAR_DETAIL";
	public static final String KS_UMENG_HOTBAR_DETAIL_POSITION = "KS_UMENG_HOTBAR_DETAIL_POSITION";
	public static final String KS_UMENG_CATEGORY_DETAIL = "KS_UMENG_CATEGORY_DETAIL";
	public static final String KS_UMENG_CATEGORY_DETAIL_POSITION = "KS_UMENG_CATEGORY_DETAIL_POSITION";
	public static final String KS_UMENG_SINGER_DETAIL = "KS_UMENG_SINGER_DETAIL";
	public static final String KS_UMENG_USER_FEEDBACK = "KS_UMENG_USER_FEEDBACK";
	public static final String KS_UMENG_QRCODE4PHONE = "KS_UMENG_QRCODE4PHONE";
	public static final String KS_UMENG_HARDWAREDECODE = "KS_UMENG_HARDWAREDECODE";
	public static final String KS_UMENG_MIC_HELP = "KS_UMENG_MIC_HELP";
	public static final String KS_UMENG_MIC_HELP_RESULT = "KS_UMENG_MIC_HELP_RESULT";
	public static final String KS_UMENG_MIC_SETTING = "KS_UMENG_MIC_SETTING";
	
	//搜歌
	public static final String KS_UMENG_VOICE_SEARCH = "KS_UMENG_VOICE_SEARCH";
	public static final String KS_UMENG_VOICE_REC = "KS_UMENG_VOICE_REC";
	public static final String KS_UMENG_PINYIN_SEARCH_DIRECTGO = "KS_UMENG_PINYIN_SEARCH_DIRECTGO";
	public static final String KS_UMENG_PINYIN_SEARCH_SUGGEST = "KS_UMENG_PINYIN_SEARCH_SUGGEST";
	public static final String KS_UMENG_PINYIN_HOT_KEYWORD = "KS_UMENG_PINYIN_HOT_KEYWORD";
	public static final String KS_UMENG_SINGER_FILTER = "KS_UMENG_SINGER_FILTER";
	public static final String KS_UMENG_ORDERED_CLEAR_ALL = "KS_UMENG_ORDERED_CLEAR_ALL";
	
	//添加与播放
	public static final String KS_UMENG_ADD = "KS_UMENG_ADD";
	public static final String KS_UMENG_SING = "KS_UMENG_SING";
	public static final String KS_UMENG_PLAY = "KS_UMENG_PLAY";
	public static final String KS_UMENG_DELETE_REPEAT = "KS_UMENG_DELETE_REPEAT";
	
	//控制
	public static final String KS_UMENG_CONTROL_BUTTON_NEXT = "KS_UMENG_CONTROL_BUTTON_NEXT";
	public static final String KS_UMENG_CONTROL_HOTKEY_NEXT = "KS_UMENG_CONTROL_HOTKEY_NEXT";
	public static final String KS_UMENG_CONTROL_HOTKEY_PRE = "KS_UMENG_CONTROL_HOTKEY_NEXT";
	public static final String KS_UMENG_CONTROL_VOICE_NEXT = "KS_UMENG_CONTROL_VOICE_NEXT";
	public static final String KS_UMENG_CONTROL_BUTTON_RESING = "KS_UMENG_CONTROL_BUTTON_RESING";
	public static final String KS_UMENG_CONTROL_VOICE_RESING = "KS_UMENG_CONTROL_VOICE_RESING";
	public static final String KS_UMENG_CONTROL_BUTTON_SWITCH_ACC = "KS_UMENG_CONTROL_BUTTON_SWITCH_ACC";
	public static final String KS_UMENG_CONTROL_VOICE_SWITCH_ACC = "KS_UMENG_CONTROL_VOICE_SWITCH_ACC";
	public static final String KS_UMENG_CONTROL_BUTTON_SWITCH_ORI = "KS_UMENG_CONTROL_BUTTON_SWITCH_ORI";
	public static final String KS_UMENG_CONTROL_VOICE_SWITCH_ORI = "KS_UMENG_CONTROL_VOICE_SWITCH_ORI";
	public static final String KS_UMENG_CONTROL_BUTTON_SCORE = "KS_UMENG_CONTROL_BUTTON_SCORE";
	public static final String KS_UMENG_CONTROL_VOICE_SCORE = "KS_UMENG_CONTROL_VOICE_SCORE";
	public static final String KS_UMENG_CONTROL_BUTTON_ORDERED = "KS_UMENG_CONTROL_BUTTON_ORDERED";
	public static final String KS_UMENG_CONTROL_VOICE_ORDERED = "KS_UMENG_CONTROL_VOICE_ORDERED";
	public static final String KS_UMENG_CONTROL_BUTTON_DIANGETAI = "KS_UMENG_CONTROL_BUTTON_DIANGETAI";
	public static final String KS_UMENG_CONTROL_VOICE_DIANGETAI = "KS_UMENG_CONTROL_VOICE_DIANGETAI";
	public static final String KS_UMENG_CONTROL_HOTKEY_VOLUME = "KS_UMENG_CONTROL_HOTKEY_VOLUME";
	
	
	public static final String KS_UMENG_SUCCESS = "1";
	public static final String KS_UMENG_FAIL = "0";
	
	public static final String KS_UMENG_MIC_SETTING_MIC = "1";
	public static final String KS_UMENG_MIC_SETTING_USB = "2";
	public static final String KS_UMENG_MIC_SETTING_NONE = "3";
	
	private static final int MSG_BASE_VALUE = 1000;
	public static final int MSG_SONG_COUNT = MSG_BASE_VALUE + 1;
	public static final int MSG_SINGER_COUNT = MSG_BASE_VALUE + 2;
	public static final int MSG_PLAY_CONSTROLL_CLOSE_VOLUME_WINDOW = MSG_BASE_VALUE + 3;
	public static final int MSG_PLAY_CONTROLLER_CLOSE_ORDERED_MTV = MSG_BASE_VALUE + 4;
	public static final int MSG_CONNECT_CLIENT_SUCCESS = MSG_BASE_VALUE + 5;
	public static final int MSG_VOLUME_CHANGED = MSG_BASE_VALUE + 6;
	public static final int MSG_VOICE_QUERY_SUCCESS = MSG_BASE_VALUE + 7;
	public static final int MSG_APP_EXIT = MSG_BASE_VALUE + 8;
	public static final int MSG_CLOSE_SECOND_PAGE = MSG_BASE_VALUE + 9;
	public static final int MSG_STOP_SING_CONTROLLER = MSG_BASE_VALUE + 10;
	public static final int MSG_RESTART_SING_CONTROLLER = MSG_BASE_VALUE + 11;
	public static final int MSG_OPEN_SONG = MSG_BASE_VALUE + 12;
	public static final int MSG_OPEN_USER_MTV_ACTIVITY_WHEN_PLAY_MTV = MSG_BASE_VALUE + 13;
	public static final int MSG_CLOSE_PLAY_ACTIVITY_OR_PLAY_USER_ACTIVITY_WHEN_CLICK_HOME = MSG_BASE_VALUE + 14;
	public static final int MSG_SECOND_PAGE_ON_KEYDOWN_PRE_PAGE = MSG_BASE_VALUE + 15;
	public static final int MSG_SECOND_PAGE_ON_KEYDOWN_NEXT_PAGE = MSG_BASE_VALUE + 16;
	public static final int MSG_DETAIL_FRAGMENT_LIST_ROWID = MSG_BASE_VALUE + 17;
	public static final int MSG_SINGER_SEARCH_KEYWORD = MSG_BASE_VALUE + 18;
	public static final int MSG_SINGER_SEARCH_CLEAR = MSG_BASE_VALUE + 19;
	public static final int MSG_ORDERED_MTV_DATA_CHANGED = MSG_BASE_VALUE + 20;
	public static final int MSG_SHOW_ORDERED_MTV_IN_PLAY = MSG_BASE_VALUE + 21;
	public static final int MSG_NETWORK_UNVAILABLE = MSG_BASE_VALUE + 22;
}
