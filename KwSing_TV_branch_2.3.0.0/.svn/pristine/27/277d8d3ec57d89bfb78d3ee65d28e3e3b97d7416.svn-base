/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.context.MainApplication;
import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.logic.service
 * 
 * @Date Mar 17, 2014 11:33:22 AM
 * 
 * @Author wangming
 * 
 */
public class NioServerService extends Service implements Runnable {
	private static final String LOG_TAG = "SocketConnectService";
	private Selector mSelector;
	private ServerSocketChannel mServerSocketChannel;
	private EventBus mEventbBus;
	private List<SocketChannel> mSocketChannelList = new ArrayList<SocketChannel>();
	// 缓冲区大小
	private static final int BUFFER_SIZE_READ = Constants.BUFFER_SIZE_READ;
	private static final int BUFFER_SIZE_WRITE = Constants.BUFFER_SIZE_WRITE;
	
	public void onEvent(final Message msg) {
		ResponseMessage responseMessage = null;
		if(msg.what >= MessageCommons.CMD_TO_PLAY) {
			final Object[] objArray = (Object[]) msg.obj;
			if(null != objArray[1] && MessageCommons.RESULT_IS_NOT_PLAYING == (Integer)(objArray[1])) {
				responseMessage = createResponseMessage(
						msg.what, 
						MessageCommons.RESULT_IS_NOT_PLAYING, 
						System.currentTimeMillis(), null);
				startWriteResponseMessage((SocketChannel)objArray[0], responseMessage);
				return;
			}
		}
		switch (msg.what) {
		case MessageCommons.CMD_ORDERED_MTV_WRITE:
			final Object[] objArray = (Object[]) msg.obj;
			responseMessage = createResponseMessage(
					msg.what, 
					MessageCommons.RESULT_SUCCESS, 
					System.currentTimeMillis(), 
					objArray[1]);
			startWriteResponseMessage((SocketChannel)objArray[0], responseMessage);
			break;
		case MessageCommons.RESULT_TO_SWITCH_ORIGINAL:
		case MessageCommons.RESULT_TO_SWITCH_ACCOMP:
		case MessageCommons.RESULT_TO_PLAY:
		case MessageCommons.RESULT_TO_PAUSE:
			responseMessage = createResponseMessage(
					msg.what, 
					MessageCommons.RESULT_SUCCESS, 
					System.currentTimeMillis(), 
					null);
			writeResponseToAllClient(responseMessage);
			break;
		case MessageCommons.RESULT_ORDERED_MTV_TOP:
			final Object[] objArrayTop = (Object[]) msg.obj;
			responseMessage = createResponseMessage(
					msg.what, 
					MessageCommons.RESULT_SUCCESS, 
					System.currentTimeMillis(), 
					null);
			startWriteResponseMessage((SocketChannel)objArrayTop[0], responseMessage);
			break;
		case MessageCommons.RESULT_ORDERED_MTV_DELETE:
			final Object[] objArrayDel = (Object[]) msg.obj;
			responseMessage = createResponseMessage(
					msg.what, 
					MessageCommons.RESULT_SUCCESS, 
					System.currentTimeMillis(), 
					null);
			startWriteResponseMessage((SocketChannel)objArrayDel[0], responseMessage);
			break;
		default:
			break;
		}
	}
	
	private void writeResponseToAllClient(ResponseMessage responseMessage) {
		for(SocketChannel channel : mSocketChannelList) {
			if(channel.isOpen()) {
				startWriteResponseMessage(channel, responseMessage);
			}
		}
	}
	
	private void startWriteResponseMessage(final SocketChannel socketChannel, final ResponseMessage responseMessage) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					writeResponseMessage(socketChannel, Constants.BUFFER_SIZE_WRITE, responseMessage);
				} catch (IOException e) {
					e.printStackTrace();
					try {
						socketChannel.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * 使用EventBus分发消息
	 */
	private Handler mUpdateUiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(!MainApplication.isSingActivityAliving && msg.what >= MessageCommons.CMD_TO_PLAY) {
				Message errorMsg = new Message();
				errorMsg.what = msg.what;
				Object[] objArray = (Object[]) msg.obj;
				errorMsg.obj = new Object[]{objArray[0], MessageCommons.RESULT_IS_NOT_PLAYING};
				EventBus.getDefault().post(errorMsg);
				return;
			}
			EventBus.getDefault().post(msg);
		}
	};
	
    public class NioServerBinder extends Binder{
        
        public NioServerService getService(){
            return NioServerService.this;
        }
    }
	    
	    private NioServerBinder nioServerBander = new NioServerBinder();
		private MulticastSocket multicastSocket;
		private InetAddress groupAddress;
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.e(LOG_TAG, "onBind");
		mEventbBus = EventBus.getDefault();
		mEventbBus.register(this);
		startSocketServer();
		return nioServerBander;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		super.onUnbind(intent);
		Log.e(LOG_TAG, "onUnbind");
		return false;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(LOG_TAG, "onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(LOG_TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void startSocketServer() {
		Thread serverThread = new Thread(this);
		serverThread.start();
//		startSendUdpPackThread();
	}
	
//	private void startSendUdpPackThread() {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				sendDataPackage();
//			}
//		}).start();
//	}
	
	
	@Override
	public void onDestroy() {
		Log.e(LOG_TAG, "onDestroy");
		if(mEventbBus != null)
			mEventbBus.unregister(this);
		stopServer();
		stopSelf();
		super.onDestroy();
	}

	@Override
	public void run() {
		Log.d(LOG_TAG, "server service running");
		initServer(Constants.SERVER_PORT);
		listening();
	}
	
	private String getLocalIpAddress() {  
		try {  
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                     InetAddress inetAddress = enumIpAddr.nextElement();  
                     if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {  
                    	 return inetAddress.getHostAddress().toString();  
                     }  
                }  
             }  
	     } catch (SocketException ex) {  
	         KuwoLog.e("WifiPreference IpAddress", ex.toString());  
	     }  
          return null;  
    }   
	
//	private void sendDataPackage() {
//		try {
//			 //IP协议为多点广播提供了这批特殊的IP地址，这些IP地址的范围是224.0.0.0至239.255.255.255  
//			groupAddress = InetAddress.getByName(Constants.MULTICAST_IP);
//			multicastSocket = new MulticastSocket(Constants.SERVER_UDP_PORT);
//			multicastSocket.setTimeToLive(1);//只发送给本局域网
//			multicastSocket.joinGroup(groupAddress);
//			
//			while(true) {
//				String tvIpAndPort = getLocalIpAddress()+":"+Constants.SERVER_PORT;
//				DatagramPacket pack = new DatagramPacket(tvIpAndPort.getBytes() , 
//						tvIpAndPort.getBytes().length, 
//						groupAddress, 
//						Constants.MULTICAST_PORT);
//				multicastSocket.send(pack);
//				Thread.currentThread().sleep(1000);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			if(multicastSocket != null)
//				try {
//					multicastSocket.leaveGroup(groupAddress);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} 
//	}

	private void initServer(int localPort) {
		try {
			mServerSocketChannel = ServerSocketChannel.open();
			mSelector = Selector.open();
			mServerSocketChannel.configureBlocking(false);//non blocking
			ServerSocket serverSocket = mServerSocketChannel.socket();
			serverSocket.setReuseAddress(true);
			InetSocketAddress address = new InetSocketAddress(localPort);
			serverSocket.bind(address);
			mServerSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void stopServer() {
		if(mServerSocketChannel != null) {
			try {
				mServerSocketChannel.close();
				mServerSocketChannel.socket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(mSelector != null)
			try {
				mSelector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
//		if(multicastSocket != null)
//			try {
//				multicastSocket.leaveGroup(groupAddress);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
	}
	
	private void listening() {
		try {
			if(mSelector == null)
				return;
			while(mSelector.select() > 0) {
				Iterator<SelectionKey> it = mSelector.selectedKeys().iterator();
				while(it.hasNext()) {
					SelectionKey sk = it.next();
					it.remove();
					if(!sk.isValid()) //如果无效，则循环
						continue;
					execute(sk);
				}
			}
		} catch (IOException e) {
			stopServer();
		}
	}
	
	/**
	 * execute [non blocking input and output stream]
	 * 
	 * @param serverSocketChannel
	 */
	private void execute(SelectionKey sk) {
		try {
			if(sk.isAcceptable()) 
				handleAccept(sk);
			if(sk.isReadable()) 
				handleRead(sk);
			if(sk.isWritable()) 
				handleWrite(sk);
		} catch (CancelledKeyException e) {
			sk.cancel();
		}
	}
	
	/**
	 * 这个是4个Object对象，有点恶心，调用时注意下，回头重定义下消息格式
	 * @param cmd
	 * @param data Object[4]: 0-SocketChannel 1-data 2-mtvData 3-extraMessage
	 */
	private void sendMessageToUI(int cmd, Object[] data) {
		Message msg = mUpdateUiHandler.obtainMessage();
		msg.what = cmd;
		msg.obj = data;
		mUpdateUiHandler.sendMessage(msg);
	}
	
	private void handleAccept(SelectionKey sk) {
		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) sk.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			mSocketChannelList.add(socketChannel);
			String remoteIp = socketChannel.socket().getRemoteSocketAddress().toString();
			//cmd is MessageCommons.RESULT_CONNECT_SUCCESS; data is ip
			sendMessageToUI(MessageCommons.RESULT_CONNECT_SUCCESS, new Object[]{socketChannel, remoteIp});
			
			socketChannel.configureBlocking(false);
			//=============send connect sucess message to client
	    	ResponseMessage responseMessage = createResponseMessage(
	    			MessageCommons.CMD_CONNECT, 
	    			MessageCommons.RESULT_CONNECT_SUCCESS, 
	    			System.currentTimeMillis(), null);
			writeResponseMessage(socketChannel, BUFFER_SIZE_WRITE, responseMessage);
			//==============
			socketChannel.register(sk.selector(), SelectionKey.OP_READ);
			Log.d(LOG_TAG, "accept client address: "+socketChannel.socket().getRemoteSocketAddress());
		} catch (IOException e) {
			try {
				sk.cancel();
				SocketChannel channel = (SocketChannel) sk.channel();
				channel.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void handleRead(SelectionKey sk) {
		// 获得与客户端通信的信道
	    SocketChannel socketChannel = (SocketChannel)sk.channel();
	    try {
			RequestMessage requestMessage = readRequestMessage(socketChannel, BUFFER_SIZE_READ);
			if(requestMessage == null)
				return;
			Log.d(LOG_TAG, requestMessage.toString());
			
			//当前处于子线程，必须使用Handler回调到UI线程处理  Object[4]
			sendMessageToUI(requestMessage.cmd, new Object[]{socketChannel, requestMessage.data, requestMessage.userData, requestMessage.extraMessage});
			
		} catch (IOException e) {
			try {
				sk.cancel();
				SocketChannel channel = (SocketChannel) sk.channel();
				channel.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void handleWrite(SelectionKey sk) {
		
	}
	
	private ResponseMessage createResponseMessage(int cmd, int result, long time, Object data) {
		ResponseMessage responseObject = new ResponseMessage();
		responseObject.cmd = cmd;
		responseObject.result = result;
		responseObject.time = time;
		responseObject.data = data;
		return responseObject;
	}
	
	private RequestMessage readRequestMessage(SocketChannel socketChannel, int bufferSize) throws IOException {
		RequestMessage requestMessage = new RequestMessage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		
		try {
			byte[] bytes;
			int size = 0;
			while ((size = socketChannel.read(buffer)) > 0) { //non blocking read request message
				buffer.flip();
				bytes = new byte[size];
				buffer.get(bytes);
				baos.write(bytes);
				buffer.clear();
			}
			if(size == -1) {
				socketChannel.close();
				socketChannel.socket().close();
				return requestMessage;
			}
			bytes = baos.toByteArray();
			requestMessage = MessageCommons.parseJsonToRequestMessage(new String(bytes));
			Log.d(LOG_TAG, requestMessage.toString());
		}catch (Exception e) {
			e.printStackTrace();
			requestMessage = null;
		}finally {
			try {
				baos.close();
			} catch(Exception ex) {}
		}
		return requestMessage;    
	}
	
	private void writeResponseMessage(SocketChannel socketChannel, int bufferSize, ResponseMessage responseMessage) throws IOException {
		String json = MessageCommons.parseResponseMessageToJson(responseMessage);
		if(TextUtils.isEmpty(json))
			return;
		byte[] bytes = json.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		buffer.limit();
		buffer.clear();
		buffer.put(bytes);
		buffer.flip();
		int bytesCount = socketChannel.write(buffer);
		Log.d(LOG_TAG, "bytes Count:"+bytesCount);
	}
}
