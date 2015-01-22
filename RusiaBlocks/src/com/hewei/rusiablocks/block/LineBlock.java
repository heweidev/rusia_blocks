package com.hewei.rusiablocks.block;

import java.util.Random;

public class LineBlock extends Block {
	private static int[][] mData1, mData2;
	private static final int BLOCK_SIZE = 4;
	
	public LineBlock() {
		this(0, 0);
	}
	
	public LineBlock(int x, int y) {
		super(BLOCK_SIZE, BLOCK_SIZE, x, y);
		
		Random rand = new Random();
		if (rand.nextFloat() < 0.5) {
			mData = mData1;
			mCur = 0;
		} else {
			mData = mData2;
			mCur = 1;
		}
	}
	
	@Override
	public void transform() {
		// TODO Auto-generated method stub
		if (mData == mData1) {
			mData = mData2;
		} else {
			mData = mData1;
		}
	}
	
	static {
		mData1 = new int[BLOCK_SIZE][BLOCK_SIZE];
		for (int i = 0; i < BLOCK_SIZE; i++) {
			mData1[i][1] = Block.STATE_FILL;
		}
		
		mData2 = new int[BLOCK_SIZE][BLOCK_SIZE];
		for (int i = 0; i < BLOCK_SIZE; i++) {
			mData2[1][i] = Block.STATE_FILL;
		}
	}

	@Override
	public int getModeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	int[][] getModeData(int idx) {
		// TODO Auto-generated method stub
		return idx == 0 ? mData1 : mData2;
	}
}
