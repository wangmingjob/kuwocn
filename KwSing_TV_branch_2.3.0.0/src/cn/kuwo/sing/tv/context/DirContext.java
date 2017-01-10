package cn.kuwo.sing.tv.context;

import cn.kuwo.framework.dir.Directory;
import cn.kuwo.framework.dir.DirectoryContext;
import cn.kuwo.framework.dir.DirectoryManager;

public class DirContext extends DirectoryContext{

	public DirContext(String appName) {
		super(appName);
	}

	public static String CONFIG = "CONFIG";
	public static String MUSIC = "MUSIC";
	public static String ACCOMPANIMENT = "ACCOMPANIMENT";
	public static String RECORD = "RECORD";
	public static String LYRICS = "LYRICS";
	public static String DOWNLOAD = "DOWNLOAD";
	public static String CODEC = "CODEC";
	public static String PICTURE = "PICTURE";
	public static String MTV_PICTURE = "MTV_PICTURE";	// 
	public static String MY_PICTURE = "MY_PICTURE"; 	// 自己录制歌曲的背景图片

	@Override
	protected void initDirectories() {
		Directory sdcardMain = getSdcardMain();
		Directory sdcardHidden = getSdcardHidden();
		
		AddChild(sdcardMain, CONFIG, "config");
		AddChild(sdcardMain, MUSIC, "music");
		AddChild(sdcardMain, ACCOMPANIMENT, "accompaniment");
		AddChild(sdcardMain, LYRICS, "lyrics");
		AddChild(sdcardMain, DOWNLOAD, "download");
		AddChild(sdcardMain, CODEC, "codec");
		
		AddChild(sdcardHidden, RECORD, "record");
		AddChild(sdcardHidden, MTV_PICTURE, "mtv_picture");
		AddChild(sdcardHidden, PICTURE, "picture");
		AddChild(sdcardHidden, MY_PICTURE, "my_picture");
	}
}
