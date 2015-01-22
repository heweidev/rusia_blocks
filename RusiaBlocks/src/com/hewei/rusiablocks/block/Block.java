package com.hewei.rusiablocks.block;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Block implements Cloneable {
	public static final int STATE_FILL = 1;
	public static final int STATE_EMPTY = 0;	
	public static final int STATE_NONE = -1;
	
	static int[][] dumy = new int[0][0];
	
	int mBlockWidth = 0;
	int mBlockHeight = 0;
	
	int[][] mData;
	int mX, mY;
	int mCur;
	
	public Block() {
		this(0, 0, 0, 0);
	}
	
	public Block(int width, int height) {
		this(width, height, 0, 0);
	}

	public Block(int width, int height, int x, int y) {
		mBlockWidth = width;
		mBlockHeight = height;
		mX = x;
		mY = y;
		
		mData = new int[mBlockWidth][mBlockHeight];
		for (int i = 0; i < mBlockWidth; i++) {
			for (int j = 0; j < mBlockHeight; j++) {
				mData[i][j] = STATE_EMPTY;
			}
		}
	}
	
	public Block(int x, int y, int mode) {
		init(x, y, mode);
	}
	
	public int getX() {
		return mX;
	}
	
	public int getY() {
		return mY;
	}
	
	public void setX(int x) {
		mX = x;
	}
	
	public void setY(int y) {
		mY = y;
	}
	
	public int getWidth() {
		return mBlockWidth;
	}
	
	public int getHeight() {
		return mBlockHeight;
	}
	
	public void move(int dx, int dy) {
		mX += dx;
		mY += dy;
	}
	
	public int getAt(int x, int y) {
		if (x >=0 && x < mBlockWidth && y >=0 && y < mBlockHeight) {
			return mData[x][y];
		} 
		
		return STATE_NONE;
	}
	
	public void setAt(int x, int y, int state) {
		if (x >=0 && x < mBlockWidth && y >=0 && y < mBlockHeight) {
			if (state == STATE_FILL) {
				mData[x][y] = STATE_FILL;
			} else if (state == STATE_EMPTY) {
				mData[x][y] = STATE_EMPTY;
			} else {
				
			}
		} 
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(mX);
		buffer.append(",");
		buffer.append(mY);
		buffer.append(",");
		buffer.append(mCur);
		buffer.append(",");
		buffer.append(mBlockWidth);
		buffer.append(",");
		buffer.append(mBlockHeight);
		buffer.append("\r\n");
		
		for (int i = mBlockHeight - 1; i >= 0; i--) {
			for (int j = 0; j < mBlockWidth; j++) {
				buffer.append(mData[j][i]);
				buffer.append(", ");
			}
			
			buffer.append("\r\n");
		}
		return buffer.toString();
	}
	
	public int getCurMode() {
		return mCur;
	}

	public abstract void transform();
	public abstract int getModeCount();
	
	abstract int[][] getModeData(int idx);
	
	public void init(int x, int y, int mode) {
		mX = x;
		mY = y;
		
		mBlockWidth = mData[0].length;
		mBlockHeight = mData.length;
		
		int[][] data = getModeData(mode);
		if (data == dumy) {
			mCur = 0;
			mData = getModeData(0);
		} else {
			mCur = mode;
			mData = data;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof Block)) {
			return false;
		}
		
		Block block = (Block)o;
		return block.mBlockWidth == mBlockWidth && block.mBlockHeight == mBlockHeight
				&& block.mX == mX && block.mY == mY && block.mCur == mCur;
	}
	
	public JSONObject doSerial() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", getClass().getName());
			obj.put("width", mBlockWidth);
			obj.put("height", mBlockHeight);
			obj.put("x", mX);
			obj.put("y", mY);
			obj.put("idx", getCurMode());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static Block create(JSONObject obj) throws JSONException {
		String name = obj.getString("name");
		int x = obj.getInt("x");
		int y = obj.getInt("y");
		int idx = obj.getInt("idx");
		Block block = null;
		
		try {
			Class<?> cls = Class.forName(name);
			block = (Block) cls.newInstance();
			block.init(x, y, idx);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (block != null) {
			return block;
		} else {
			throw new JSONException("data format error!");
		}
	}
}
