package com.hewei.rusiablocks.game;

import java.util.Random;

import android.os.Parcel;
import android.os.Parcelable;

import com.hewei.player.Player;
import com.hewei.rusiablocks.block.Block;
import com.hewei.rusiablocks.block.BlockHelper;
import com.hewei.rusiablocks.block.KBlock;
import com.hewei.rusiablocks.block.LBlock;
import com.hewei.rusiablocks.block.LineBlock;
import com.hewei.rusiablocks.block.SquareBlock;
import com.hewei.rusiablocks.block.Wall;
import com.hewei.rusiablocks.block.ZBlock;

public class Game implements Parcelable, GameClock.ClockListener {
	private static final int MIN_DOWN_SPEED = 1;
	
	private Player mPlayer;
	private Wall mWall;
	private Block mCurBlock;
	private Block mNextBlock;
	private int mScore;
	private int mLevel;
	private boolean mRunning = false;
	private GameListener mGameListener;
	private GameClock mClock;
	private BlockGenerator mBlockGenerator;
	
	public interface GameListener {
		void onStep();
		void onGameOver();
		void onGameLevelChanged(int newLevel);
		void onGetScore(int score);
		void onNewBlock();
	}
	
	public interface BlockGenerator {
		Block nextBlock(Game game);
	}
	
	public enum Action {
		LEFT, RIGHT, DOWN, SDOWN, TRANSFORM
	}
	
	public Game(Player player) {
		mPlayer = player;
		mBlockGenerator = new DefaultGenerator();
		
		mLevel = mPlayer.getLevel();
		
		mWall = new Wall();
		mCurBlock = createBlockImpl();
		mNextBlock = createBlockImpl();
		
		mClock = new GameClock();
		mClock.setInterval(level2Interval(mPlayer.getLevel()));
		mClock.setClockListener(this);
	}
	
	public void start() {
		mRunning = true;
		mClock.start();
	}
	
	public void stop() {
		mRunning = false;
		mClock.stop();
	}
	
	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		if (!mRunning) {
			return;
		}

		if (BlockHelper.canDown(mWall, mCurBlock)) {
			mCurBlock.move(0, -1);
		} else {
			BlockHelper.concat(mWall, mCurBlock);
			int score = BlockHelper.getScore(mWall);
			if (score > 0) {
				mScore += score;
				if (mGameListener != null) {
					mGameListener.onGetScore(score);
				}
			}

			if (BlockHelper.testGameOver(mWall)) {
				if (mGameListener != null) {
					mGameListener.onGameOver();
				}
				mRunning = false;
			} else {
				int newLevel = (mScore / 500) + 1;
				if (newLevel != mLevel) {
					mLevel = newLevel;
					if (mGameListener != null) {
						mGameListener.onGameLevelChanged(newLevel);
					}
				}

				mCurBlock = mNextBlock;
				mNextBlock = createBlockImpl();
				
				if (mGameListener != null) {
					mGameListener.onNewBlock();
				}
			}
		}
		
		if (mGameListener != null) {
			mGameListener.onStep();
		}
	}

	public void onAction(Action action) {
		if (!mRunning) {
			return;
		}
		
		switch (action) {
		case LEFT:
			if (BlockHelper.canLeft(mWall, mCurBlock)) {
				mCurBlock.move(-1, 0);
			}
			break;
		case RIGHT:
			if (BlockHelper.canRight(mWall, mCurBlock)) {
				mCurBlock.move(1, 0);
			}
			break;
		case DOWN:
		case SDOWN:
			for (int i = 0; i < 2 * MIN_DOWN_SPEED; i++) {
				if (BlockHelper.canDown(mWall, mCurBlock)) {
					mCurBlock.move(0, -1);
				}
			}
			break;
		case TRANSFORM:
			if (BlockHelper.canTransform(mWall, mCurBlock)) {
				mCurBlock.transform();
			}
			break;
		default:
			break;
		}
	}
	
	public void setGameListener(GameListener listener) {
		mGameListener = listener;
	}
	
	public Block getCurBlock() {
		return mCurBlock;
	}
	
	public Block getNextBlock() {
		return mNextBlock;
	}
	
	public Wall getWall(){
		return mWall;
	}
	
	private Block createBlockImpl() {
		final int WALL_HEIGHT = mWall.getHeight();
		
		Block block = mBlockGenerator.nextBlock(this);
		block.setX(3);
		block.setY(WALL_HEIGHT);
		return block;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mWall.doSerial().toString());
		dest.writeString(mCurBlock.doSerial().toString());
		dest.writeString(mNextBlock.doSerial().toString());
	}

	public int getScore() {
		return mScore;
	}
	
	public int getLevel() {
		return mLevel;
	}
	
	private int level2Interval(int level) {
		return (10 - level)*50;
	}
	
	public class DefaultGenerator implements BlockGenerator {
		@Override
		public Block nextBlock(Game game) {
			Random rand = new Random();
			float d = rand.nextFloat();
			Block block = null;
			if (d < 0.2) {
				block = new LineBlock();
			} else if (d >= 0.2 && d < 0.4) {
				block = new KBlock();
			} else if (d >= 0.4 && d < 0.6) {
				block = new ZBlock();
			} else if (d >= 0.6 && d < 0.8) {
				block = new LBlock();
			} else {
				block = new SquareBlock();
			}
			return block;
		}
	}
}
