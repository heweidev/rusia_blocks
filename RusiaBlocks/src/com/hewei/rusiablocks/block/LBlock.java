package com.hewei.rusiablocks.block;

import java.util.Random;

public class LBlock extends Block {
	private static final int BLOCK_SIZE = 3;
	
	private static int[][] mData1 = new int[][] {
		{STATE_FILL, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
	};
	
	private static int[][] mData2 = new int[][] {
		{STATE_EMPTY, STATE_EMPTY, STATE_EMPTY},
		{STATE_FILL, STATE_FILL, STATE_FILL},
		{STATE_FILL, STATE_EMPTY, STATE_EMPTY},
	};
	
	private static int[][] mData3 = new int[][] {
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_FILL},
	};
	
	private static int[][] mData4 = new int[][] {
		{STATE_EMPTY, STATE_EMPTY, STATE_EMPTY},
		{STATE_EMPTY, STATE_EMPTY, STATE_FILL},
		{STATE_FILL, STATE_FILL, STATE_FILL},
	};
	
	private static int[][] mData5 = new int[][] {
		{STATE_EMPTY, STATE_FILL, STATE_FILL},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
	};
	
	private static int[][] mData6 = new int[][] {
		{STATE_EMPTY, STATE_EMPTY, STATE_EMPTY},
		{STATE_FILL, STATE_FILL, STATE_FILL},
		{STATE_EMPTY, STATE_EMPTY, STATE_FILL},
	};
	
	private static int[][] mData7 = new int[][] {
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_FILL, STATE_FILL, STATE_EMPTY},
	};
	
	private static int[][] mData8 = new int[][] {
		{STATE_EMPTY, STATE_EMPTY, STATE_EMPTY},
		{STATE_FILL, STATE_EMPTY, STATE_EMPTY},
		{STATE_FILL, STATE_FILL, STATE_FILL},
	};
	
	private static final int[][][] mDataArray = new int[][][] {
		mData1, mData2, mData3, mData4,
		mData5, mData6, mData7, mData8,
	};
	
	public LBlock() {
		this(0, 0);
	}
	
	public LBlock(int x, int y) {
		super(BLOCK_SIZE, BLOCK_SIZE, x, y);
		
		Random rand = new Random();
		float d = rand.nextFloat();
		mCur = (int)(mDataArray.length * d);
		mData = mDataArray[mCur];
	}
	
	@Override
	public void transform() {
		// TODO Auto-generated method stub
		if (mCur < 4) {
			mCur = (mCur + 1) % 4;
		} else {
			mCur = 4 + (mCur + 1) % 4;
		}
		
		mData = mDataArray[mCur];
	}

	@Override
	public int getModeCount() {
		// TODO Auto-generated method stub
		return mDataArray.length;
	}

	@Override
	int[][] getModeData(int idx) {
		// TODO Auto-generated method stub
		if (idx < 0 || idx > mDataArray.length) {
			return Block.dumy;
		}
		
		return mDataArray[idx];
	}
}
