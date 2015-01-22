package com.hewei.rusiablocks.block;

import java.util.Random;

public class KBlock extends Block {
	static final int BLOCK_SIZE = 3;
	
	private static int[][] mData1 = new int[][] {
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_FILL, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
	};
	
	private static int[][] mData2 = new int[][] {
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_EMPTY, STATE_FILL, STATE_FILL},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
	};
	
	private static int[][] mData3 = new int[][] {
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
		{STATE_FILL, STATE_FILL, STATE_FILL},
		{STATE_EMPTY, STATE_EMPTY, STATE_EMPTY},
	};
	
	private static int[][] mData4 = new int[][] {
		{STATE_EMPTY, STATE_EMPTY, STATE_EMPTY},
		{STATE_FILL, STATE_FILL, STATE_FILL},
		{STATE_EMPTY, STATE_FILL, STATE_EMPTY},
	};
	
	private static final int[][][] mDataArray = new int[][][] {
		mData1, mData2, mData3, mData4,
	};
	
	public KBlock() {
		this(0, 0);
	}
	
	public KBlock(int x, int y) {
		super(BLOCK_SIZE, BLOCK_SIZE, x, y);
		
		Random rand = new Random();
		float d = rand.nextFloat();
		mCur = (int)d * mDataArray.length;
		mData = mDataArray[mCur];
	}
	
	@Override
	public void transform() {
		// TODO Auto-generated method stub
		mCur = (mCur + 1) % mDataArray.length;
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
