/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.kuwo.framework.context.AppContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @Package cn.kuwo.sing.tv.utils
 *
 * @Date 2013-5-10, 下午5:31:33, 2013
 *
 * @Author wangming
 *
 */
public class ImageUtils {
	
	/**
	 * Get image from server by HttpClient
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static Bitmap getImageByHttpClient(String imageUrl) {
		Bitmap bitmap = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(imageUrl);
			HttpResponse response = client.execute(getRequest);
			//Http response status code 200 OK
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				//response entity
				HttpEntity responseEntity = response.getEntity();
				InputStream is = responseEntity.getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				for(int len = 0; (len = is.read(buf)) != -1; ) {
					baos.write(buf, 0, len);
				}
				if(is != null) {
					is.close();
				}
				baos.flush();
				byte[] data = baos.toByteArray();
				BitmapFactory.Options opts = BitmapTools.getBitmapOptions(AppContext.SCREEN_WIDTH, AppContext.SCREEN_HIGHT);
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * Compress the bitmap to JPEG, and write to ByteArrayOutputStream.
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * Save the image to SD card.
	 * @param imagePath
	 * @param buf
	 * @throws IOException
	 */
	public static void saveImage2SDCard(String imagePath, byte[] buf) throws IOException {
		File file = new File(imagePath);
		if(file.exists()) {
			return;
		}else {
			File parentFile = file.getParentFile();
			if(!parentFile.exists()) {
				parentFile.mkdirs();
			}
			//you should make the directory first, and then make the file
			//not make the expanded name
			file.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(buf);
			fos.flush();
			fos.close();
		}
	}

}
