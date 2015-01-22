package com.hewei.rusiablocks.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.hewei.player.Player;

public class LocalGame extends Game implements Handler.Callback {
	private static final int MSG_UPDATE = 1234;
	
	private SocketAddress mRemoteAddr;
	private Handler mHandler;
	private HandlerThread mThread;

	public LocalGame(Player player, SocketAddress remoteAddr) {
		super(player);
		// TODO Auto-generated constructor stub
		mRemoteAddr = remoteAddr;
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		super.onStep();
		
		JSONObject json = new JSONObject();
		try {
			json.put("level", getLevel());
			json.put("score", getScore());
			json.put("wall", getWall().doSerial());
			json.put("block", getCurBlock().doSerial());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 如果因网络原因之前的信息还没有发送成功，直接丢掉
		mHandler.removeMessages(MSG_UPDATE);
		mHandler.obtainMessage(MSG_UPDATE, json).sendToTarget();;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		
		mThread = new HandlerThread("game sender");
		mThread.start();
		mHandler = new Handler(mThread.getLooper(), this);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
		mThread.quit();
	}

	private void sendData(Object o) {
		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket();
			socket.connect(mRemoteAddr);
			
			byte[] data = o.toString().getBytes();
			DatagramPacket pack = new DatagramPacket(data, data.length);
			socket.send(pack);
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

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MSG_UPDATE:
			sendData(msg.obj);
			break;
		}
		
		return false;
	}
}
