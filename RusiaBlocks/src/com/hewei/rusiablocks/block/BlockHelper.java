package com.hewei.rusiablocks.block;

import android.util.Log;

public class BlockHelper {
	private static final String TAG = "BlockHelper";
	
	/*
	 * left, right, down, transform 这几种移动的准则是有效范围内方格数不变 
	 */
	
	public static boolean canLeft(Wall wall, Block block) {
		int x = block.getX();
		int y = block.getY();
		
		for (int i = 0; i < block.getWidth(); i++) {
			for (int j = 0; j < block.getHeight(); j++) {
				if (block.getAt(i, j) == Block.STATE_FILL &&
						(x + i <= 0 || wall.getAt(x + i - 1, y + j) == Block.STATE_FILL)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static boolean canRight(Wall wall, Block block) {
		int x = block.getX();
		int y = block.getY();
		
		for (int i = block.getWidth() - 1; i >= 0 ; i--) {
			for (int j = 0; j < block.getHeight(); j++) {
				if (block.getAt(i, j) == Block.STATE_FILL &&
						(x + i >= wall.getWidth() - 1 || wall.getAt(x + i + 1, y + j - 1) == Block.STATE_FILL)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static boolean canDown(Wall wall, Block block) {
		int x = block.getX();
		int y = block.getY();
		
		//Log.d(TAG, "---------- test -----------");
		//Log.d(TAG, wall.toString());
		//Log.d(TAG, block.toString());
		
		for (int j = 0; j < block.getHeight(); j++) {
			for (int i = 0; i < block.getWidth() ; i++) {	
				if (block.getAt(i, j) == Block.STATE_FILL &&
						((y + j) <= 0 || wall.getAt(x + i, y + j - 1) == Block.STATE_FILL)) {
					//Log.d(TAG, "i = " + i + ", j = " + j);
					return false;
				}
			}		
		}
		
		//Log.d(TAG, "---------- test end-----------");
		return true;
	}
	
	public static void transform(Wall wall, Block block) {
		if (!canTransform(wall, block)) {
			return;
		}
		
		block.transform();
		
		// 是否超出左边界
		boolean bGet = false;
		int firstIndex = 0;
		for (int i = 0; i < block.getWidth(); i++) {
			for (int j = 0; j < block.getHeight(); j++) {
				if (block.getAt(i, j) == Block.STATE_FILL) {
					bGet = true;
					firstIndex = i;
					break;
				}
			}

			if (bGet) {
				break;
			}
		}
		int absX = block.getX() + firstIndex;
		if (absX < 0) {
			block.move(-absX, 0);
			return;
		}
			
		// 是否超出右边界
		bGet = false;
		int lastIndex = block.getWidth() - 1;
		for (int i = block.getWidth() - 1; i >= 0; i--) {
			for (int j = 0; j < block.getHeight(); j++) {
				if (block.getAt(i, j) == Block.STATE_FILL) {
					bGet = true;
					lastIndex = i;
					break;
				}
			}
			
			if (bGet) {
				break;
			}
		}
		absX = block.getX() + lastIndex;	
		if (absX > wall.getWidth() - 1) {
			block.move(wall.getWidth() - 1 - absX, 0);
			return;
		}
	}
	
	public static boolean canTransform(Wall wall, Block block) {
		try {
			Block newBlock = (Block) block.clone();
			newBlock.transform();
			
			for (int i = newBlock.getWidth() - 1; i >= 0 ; i--) {
				for (int j = 0; j < newBlock.getHeight(); j++) {
					if (newBlock.getAt(i, j) == Block.STATE_FILL
							&& wall.getAt(newBlock.getX() + i, newBlock.getY() + j) == Block.STATE_FILL) {
						return false;
					}
				}
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static void concat(Wall wall, Block block) {
		for (int i = block.getWidth() - 1; i >= 0 ; i--) {
			for (int j = 0; j < block.getHeight(); j++) {
				if (block.getAt(i, j) == Block.STATE_FILL) {
					wall.setAt(block.getX() + i, block.getY() + j, Block.STATE_FILL);
				}
			}
		}
	}
	
	public static int getScore(Wall wall) {
		int line1 = -1;
		int line2 = -1;
		int line3 = -1;
		int line4 = -1;
		int score = 0;

		for (int j = 0; j < wall.getHeight(); j++) {
			boolean bOK = true;
			
			for (int i = 0; i < wall.getWidth() ; i++) {	
				if (wall.getAt(i, j) != Block.STATE_FILL) {
					bOK = false;
					break;
				}
			}
			
			if (bOK) {
				if (line1 == -1) {
					line1 = j;
					score = 10;
				} else if (line2 == -1) {
					line2 = j;
					score = 20;
				} else if (line3 == -1) {
					line3 = j;
					score = 50;
				} else if (line4 == -1) {
					line4 = j;
					score = 100;
				}
			}
		}
		
		if (line1 == -1) {
			return score;
		}
		
		int line = line1;
		for (int i = line1; i < wall.getHeight(); i++) {
			if (i != line1 && i != line2 && i != line3 && i != line4) {
				for (int j = 0; j < wall.getWidth(); j++) {
					wall.setAt(j, line, wall.getAt(j, i));
				}
				
				line += 1;
			} else {
				for (int j = 0; j < wall.getWidth(); j++) {
					wall.setAt(j, i, Block.STATE_EMPTY);
				}
			}
		}
		
		return score;
	}
	
	public static boolean testGameOver(Wall wall) {
		for (int i = 0; i < wall.getWidth(); i++) {
			if (wall.getAt(i, wall.getHeight()-1) == Block.STATE_FILL) {
				return true;
			}
		}
		
		return false;
	}
}
