/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.commons.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import cn.kuwo.sing.phone4tv.bean.Mtv;
import cn.kuwo.sing.phone4tv.bean.UserMtv;
import cn.kuwo.sing.phone4tv.commons.context.MainApplication;


/**
 * @Package cn.kuwo.sing.phone4tv.business
 * 
 * @Date Mar 19, 2014 10:13:12 AM
 *
 * @Author wangming
 *
 */
public class SocketManager {
	private static final String LOG_TAG = "SocketBusiness";
	private static NioHandlerImpl mNioHandler;
	public static SocketManager socketManager;
	private SocketConnectTask mSocketConnectTask;
	
	/**
	 * 如果不需要更新 客户端提示信息，就传null
	 * 
	 * @param updateConversationHandler
	 */	
	public SocketManager() {
		mNioHandler = new NioHandlerImpl();
	}
	
	public static SocketManager getInstance(){
		if (socketManager == null){
			socketManager = new SocketManager();
		}
		return socketManager;
	}
		
	public static void addHander(String className,Handler updateConversationHandler){
		mNioHandler.addHandler(className, updateConversationHandler);
	}
	
	public static void removeHandler(String className){
		mNioHandler.removeHandler(className);
	}
	
	public RequestMessage createRequestMessage(int cmd, String extraMessage, Mtv data, UserMtv userData, long time) throws SocketException {
		if(mNioHandler == null) {
			throw new SocketException("NioHandler is null !");
		}
		return mNioHandler.createRequestMessage(cmd, extraMessage, data, userData, time);
	}
	
	public void writeRequestMessage(SocketChannel socketChannel, RequestMessage requestMessage) throws SocketException {
		if(mNioHandler == null) {
			throw new SocketException("NioHandler is null !");
		}
		mNioHandler.writeRequestMessage(socketChannel, requestMessage);
	}
	
	public void startSocketConnectThread(String serverIp, int serverPort) {
		mSocketConnectTask = new SocketConnectTask(serverIp, serverPort);
		new Thread(mSocketConnectTask).start();
	}
	
	private class SocketConnectTask implements Runnable {
		private String mServerIp;
		private int mServerPort;
		private Selector mSelector;
		private SocketChannel mSocketChannel;
		
		public SocketConnectTask(String serverIp, int serverPort) {
			mServerIp = serverIp;
			mServerPort = serverPort;
		}
		
		private void initClient(String serverIp, int serverPort) {
			try {
				mSocketChannel = SocketChannel.open(); //create socket channel
				mSelector = Selector.open();
				mSocketChannel.configureBlocking(false); //non blocking
				// 客户端连接服务器,其实方法执行并没有实现连接，需要调  
		        //用channel.finishConnect();才能完成连接  
				boolean connectResult = mSocketChannel.connect(new InetSocketAddress(serverIp, serverPort));
				Log.d(LOG_TAG, "connectResult:"+connectResult);
				mSocketChannel.register(mSelector, SelectionKey.OP_CONNECT);
				MainApplication.s_socketChannel = mSocketChannel;
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		
		/**
		 * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理 
		 */
		private void listening() {
			try {
				if(mSelector == null) {
					Log.e(LOG_TAG, "selector is null");
					return;
				}
				
				while(mSelector.select() > 0) {
					Iterator<SelectionKey> it = mSelector.selectedKeys().iterator();
					while(it.hasNext()) {
						SelectionKey sk = it.next();
						it.remove();
						if(!sk.isValid())
							continue;
						execute(sk);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void execute(SelectionKey sk) {
			try {
				if(sk.isConnectable())
					mNioHandler.handleConnect(sk);
				if(sk.isReadable())
					mNioHandler.handleRead(sk);
				if(sk.isWritable())
					mNioHandler.handleWrite(sk);
			} catch (CancelledKeyException e) {
				sk.cancel();
			}
		}

		@Override
		public void run() {
			initClient(mServerIp, mServerPort);
			listening();
		}
		
		public void close() {
			try {
				if(mSelector != null)
					mSelector.close();
				if(mSocketChannel != null)
					mSocketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeChannel() {
		if(mSocketConnectTask != null)
			mSocketConnectTask.close();
	}
}
