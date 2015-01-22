package com.hewei.rusiablocks.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import android.os.Handler;
import android.util.Log;

public class GamePeer implements Runnable {
	private static final String TAG = "GamePeer";
	private Thread mThread; 
	private OnFindServerListener mListener;
	private Handler mHandler = new Handler();
	
	public GamePeer(OnFindServerListener listener) {
		mListener = listener;
	}
	
	public void init() {
		mThread = new Thread(this);
		mThread.start();
	}
	
	public void uninit() {
		mThread.interrupt();
	}
	
	public interface OnFindServerListener {
		void onFindServer(SocketAddress addr);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(Constants.BROADCAST_PORT);
			byte[] buffer = new byte[1024];
			DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
			socket.receive(pack);
			
			final SocketAddress serverAddr = new InetSocketAddress(pack.getAddress(), Constants.DATA_PORT);
			String str = new String(pack.getData());
			
			Log.d(TAG, "recv " + str);	
			Log.d(TAG, pack.getAddress().toString());
			Log.d(TAG, "port = " + pack.getPort());
			Log.d(TAG, pack.getSocketAddress().toString());
			
			pack.setData(("recv: " + str).getBytes());
			socket.send(pack);
			
			if (mListener != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mListener.onFindServer(serverAddr);
					}		
				});
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (socket != null) {
			socket.close();
		}
	}
}
