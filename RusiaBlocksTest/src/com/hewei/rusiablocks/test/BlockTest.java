package com.hewei.rusiablocks.test;

import org.json.JSONException;
import org.json.JSONObject;

import com.hewei.rusiablocks.block.Block;
import com.hewei.rusiablocks.block.KBlock;
import com.hewei.rusiablocks.block.LBlock;
import com.hewei.rusiablocks.block.LineBlock;
import com.hewei.rusiablocks.block.SquareBlock;
import com.hewei.rusiablocks.block.Wall;
import com.hewei.rusiablocks.block.ZBlock;

import android.test.AndroidTestCase;
import android.util.Log;

public class BlockTest extends AndroidTestCase {
	private static final String TAG = "BlockTest";

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	public void testWallSerial() {
		Wall wall = Wall.testWall();
		Log.d(TAG, wall.toString());
		JSONObject obj = wall.doSerial();
		Log.d(TAG, obj.toString());
		Wall newWall = null;
		try {
			newWall = Wall.create(obj);
			Log.d(TAG, newWall.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(wall.equals(newWall));
	}
	
	public void testBlockSerial() {
		Class[] clsArray = new Class[] {
			KBlock.class, LBlock.class, LineBlock.class, SquareBlock.class, ZBlock.class
		};
		
		for (Class cls : clsArray) {
			Block block = null;
		
			try {
				block = (Block) cls.newInstance();
				block.setX(1);
				block.setY(2);
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Log.d(TAG, "test " + cls.toString());

			if (block != null) {
				JSONObject obj = block.doSerial();
				Block newBlock = null;
				try {
					newBlock = Block.create(obj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (!block.equals(newBlock)) {
					Log.d(TAG, "old: " + block.toString());
					Log.d(TAG, "new: " + newBlock.toString());
					assertTrue(false);
				}
				
				//assertTrue(block.equals(newBlock));
			}
		}
	}
}
