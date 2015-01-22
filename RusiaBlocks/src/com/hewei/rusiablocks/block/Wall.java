package com.hewei.rusiablocks.block;

import java.util.Arrays;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Wall extends Block {
	public static final int WALL_WIDTH = 10;
	public static final int WALL_HEIGHT = 20;
	
	public Wall() {
		super(WALL_WIDTH, WALL_HEIGHT);
	}
	
	public Wall(int width, int height) {
		super(width, height);
	}
	
	@Override
	public void transform() {
		// TODO Auto-generated method stub
		
	}
	
	public static Wall testWall() {
		Wall wall = new Wall();
		
		Random rand = new Random();
		
		for (int i = 0; i < wall.getWidth(); i++) {
			for (int j = 0; j < wall.getHeight(); j++) {
				if (rand.nextFloat() > 0.5) {
					wall.mData[i][j] = Block.STATE_FILL;
				} else {
					wall.mData[i][j] = Block.STATE_EMPTY;
				}	
			}
		}
		
		return wall;
	}
	
	public void fill() {
		for (int i = 0; i < WALL_WIDTH; i++) {
			for (int j = 0; j < WALL_HEIGHT; j++) {
				mData[i][j] = Block.STATE_FILL;
			}
		}
	}
	
	public void clear() {
		for (int i = 0; i < WALL_WIDTH; i++) {
			for (int j = 0; j < WALL_HEIGHT; j++) {
				mData[i][j] = Block.STATE_EMPTY;
			}
		}
	}
	
	public int getScore() {
		int score = 0;

		for (int i = 0; i < WALL_HEIGHT; i++) {
			boolean bOK = true;
			for (int j = 0; j < WALL_WIDTH; j++) {
				if (mData[j][i] != STATE_FILL) {
					bOK = false;
					break;
				}
			}
			
			if (bOK) {
				score += 1;
			}
		}
		
		return score;
	}

	@Override
	public int getCurMode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getTopmost() {
		for (int h = 0; h < WALL_HEIGHT; h++) {
			for (int w = 0; w < WALL_WIDTH; w++) {
				if (mData[w][h] != STATE_FILL) {
					return h;
				}
			}
		}
		
		return 0;
	}

	public JSONObject doSerial() {
		JSONObject obj = new JSONObject();
		try {
			int top = getTopmost();
			obj.put("version", 1);
			obj.put("width", WALL_WIDTH);
			obj.put("height", WALL_HEIGHT);
			obj.put("top", top);
			
			JSONArray jsonArray = new JSONArray();
			for (int h = top; h < WALL_HEIGHT; h++) {
				JSONArray line = new JSONArray();
				for (int w = 0; w < WALL_WIDTH; w++) {
					if (mData[w][h] == STATE_EMPTY) {
						line.put(w);
					}
				}
				jsonArray.put(line);
			}
			obj.put("data", jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Wall create(JSONObject obj) throws JSONException {
		//int version = obj.getInt("version");
		int width = obj.getInt("width");
		int height = obj.getInt("height");
		int top = obj.getInt("top");
		JSONArray array = obj.getJSONArray("data");
		Wall wall = new Wall(width, height);
		wall.fill();
		
		for (int i = top; i < height; i++) {
			Object item = array.get(i);
			if (item instanceof JSONArray) {
				JSONArray data = (JSONArray)item;
				for (int j = 0; j < data.length(); j++) {
					int idx = data.getInt(j);
					wall.setAt(idx, i, STATE_EMPTY);
				}
			} else {
				throw new JSONException("need line to be a array object");
			}
		}
		
		return wall;
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

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof Wall) || !super.equals(o)) {
			return false;
		}
		
		Wall wall = (Wall)o;
		for (int i = 0; i < mData.length; i++) {
			if (!Arrays.equals(wall.mData[i], mData[i])) {
				return false;
			}
		}
		
		return true;
	}
}
