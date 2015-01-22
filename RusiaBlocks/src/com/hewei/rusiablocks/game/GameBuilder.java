package com.hewei.rusiablocks.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.util.Log;

public class GameBuilder implements Runnable {
	private static final String TAG = "GameBuilder";
	private Thread mThread;
	private OnPeerConnectListener mListener;
	private Handler mHandler;

	public GameBuilder(OnPeerConnectListener listener) {
		mListener = listener;
		mHandler = new Handler();
	}
	
	public void init() {
		mThread = new Thread(this);
		mThread.start();
	}
	
	public void uninit() {
		mThread.interrupt();
	}
	
	public interface OnPeerConnectListener {
		void onPeerConnect(SocketAddress addr);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket socket = null;
		InetAddress broadcastAddr = null;
		
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
			socket.setSoTimeout(1000);
			
			broadcastAddr = InetAddress.getByName("255.255.255.255");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if (socket == null) {
			return;
		}
		if (broadcastAddr == null) {
			return;
		}
		
		byte[] buffer = new byte[1024];
		DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
		pack.setData("Hello World".getBytes());
		pack.setAddress(broadcastAddr);
		pack.setPort(Constants.BROADCAST_PORT);
		
		while (true) {
			try {
				socket.send(pack);
				socket.receive(pack);
				
				final SocketAddress peerAddr = new InetSocketAddress(pack.getAddress(), Constants.DATA_PORT);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mListener.onPeerConnect(peerAddr);
					}
				});
				
				Log.d(TAG, pack.getData().toString());
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
		
		if (socket != null) {
			socket.close();
		}
		
		Log.d(TAG, "builder exit!");
	}
}
