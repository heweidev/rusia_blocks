package com.hewei.rusiablocks.game;

import android.os.Handler;

public class GameClock implements Runnable {
	private static final int MIN_INTERVAL = 50;
	private static final int MAX_INTERVAL = 500;
	
	private ClockListener mListener;
	private Handler mHandler;
	private int mInterval;
	
	public interface ClockListener {
		void onStep();
	};
	
	public GameClock() {
		mHandler = new Handler();
	}
	
	public void setInterval(int val) {
		if (val > MAX_INTERVAL) {
			mInterval = MAX_INTERVAL;
		} else if (val < MIN_INTERVAL) {
			mInterval = MIN_INTERVAL;
		} else {
			mInterval = val;
		}
	}
	
	public void setClockListener(ClockListener listener) {
		mListener = listener;
	}
	
	public void start() {
		mHandler.postDelayed(this, mInterval);
	}
	
	public void stop() {
		mHandler.removeCallbacks(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onStep();
		}
		
		mHandler.postDelayed(this, mInterval);
	}
}
