package com.hewei.rusiablocks.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.hewei.rusiablocks.block.Block;
import com.hewei.rusiablocks.block.Wall;

public class RemoteGame implements Runnable {
	private static final int RECV_BUFFER_SIZE = 4096;
	
	private Wall mWall;
	private Block mCurBlock;
	
	private int mPort;
	private DatagramSocket mSocket;
	private byte[] mReceiveBuffer;
	private Handler mHandler = new Handler();
	private RemoteGameListener mListener;
	
	public interface RemoteGameListener {
		void onUpdate();
	}
	
	public void init(int port) {
		mPort = port;
		mReceiveBuffer = new byte[RECV_BUFFER_SIZE];
		
		new Thread(this).start();
	}
	
	public Wall getWall() {
		return mWall;
	}
	
	public Block getCurBlock() {
		return mCurBlock;
	}
	
	public void setGameListener(RemoteGameListener listener) {
		mListener = listener;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			mSocket = new DatagramSocket(mPort);
			
			while (true) {
				processData(mSocket);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (mSocket != null) {
			mSocket.disconnect();
			mSocket.close();
		}
	}
	
	private void processData(DatagramSocket socket) throws IOException {
		DatagramPacket pack = new DatagramPacket(mReceiveBuffer, mReceiveBuffer.length);
		socket.receive(pack);
		
		String strData = new String(pack.getData(), 0, pack.getLength());
		try {
			JSONObject json = new JSONObject(strData);
			int level = json.getInt("level");
			int score = json.getInt("score");
			JSONObject objWall = json.getJSONObject("wall");
			JSONObject objBlock = json.getJSONObject("block");
			
			final Wall wall = Wall.create(objWall);
			final Block block = Block.create(objBlock);
			
			if (mListener != null) {
				mHandler.post(new Runnable(){
					@Override
					public void run() {
						mWall = wall;
						mCurBlock = block;
						mListener.onUpdate();	
					}
				});
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void stop() {
		if (mSocket != null) {
			mSocket.disconnect();
			mSocket.close();
		}
	}
}
