package com.hewei.rusiablocks.block;

public class SquareBlock extends Block {
	static final int BLOCK_SIZE = 2;

	public SquareBlock() {
		this(0, 0);
	}
	
	public SquareBlock(int x, int y) {
		super(BLOCK_SIZE, BLOCK_SIZE, x, y);
		
		mData = new int[][] { 
			{ Block.STATE_FILL, Block.STATE_FILL },
			{ Block.STATE_FILL, Block.STATE_FILL },
		};
	}
	
	public SquareBlock(int x, int y, int mode) {
		this(x, y);
	}

	@Override
	public void transform() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCurMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getModeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	int[][] getModeData(int idx) {
		// TODO Auto-generated method stub
		return mData;
	}
}
