package cn.kuwo.sing.tv.context;

import cn.kuwo.framework.config.PreferencesManager;
import android.os.Build;

public class Decoders {
	static int mSDKVersion=0; 
	public static int getSystemSDKVersion() { 
		try { 
			mSDKVersion = Integer.valueOf(android.os.Build.VERSION.SDK); 
		} 
		catch (NumberFormatException e){
			
		} 
		return mSDKVersion; 
	}
	
	public static boolean isUseSysDecoder(){
		int nHardwareDecode = PreferencesManager.getInt(Constants.HARDWARE_DECODE, 0);
		return getSystemSDKVersion()>15 && nHardwareDecode==1;		
	}
	
	public static boolean isUseKonkaDecoder(){
		if (Build.BRAND.equals("Konka"))
			return true;
		return false;
	}
	
	public static boolean isUseVitamioDecoder(){
		if (isUseKonkaDecoder()){
			return false;
		}
		int nHardwareDecode = PreferencesManager.getInt(Constants.HARDWARE_DECODE, 0);
		if (nHardwareDecode==1){
			return false;
		}
		return true;
	}
}
