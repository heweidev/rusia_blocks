package com.hewei.rusiablocks;

import com.hewei.rusiablocks.block.Block;
import com.hewei.rusiablocks.block.Wall;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class WallView extends View {
	private static final String TAG = "WallView";
	
	private static Drawable mBlockDrawable;
	private static int DOT_WIDTH, DOT_HEIGHT;
	
	private Paint mPaint;
	
	private Wall mWall;
	private Block mBlock;
	private Rect mHelperRect;
	private int mWallHeight;
	
	public WallView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public WallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public WallView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context ctx) {
		if (mBlockDrawable == null) {
			mBlockDrawable = ctx.getResources().getDrawable(R.drawable.dot);
			//DOT_WIDTH = mBlockDrawable.getIntrinsicWidth();
			//DOT_HEIGHT = mBlockDrawable.getIntrinsicHeight();
		}
		
		mPaint = new Paint();
		mHelperRect = new Rect();
	}
	
	public void setWallAndBlock(Wall wall, Block block) {
		mWall = wall;
		mBlock = block;
		
		mWallHeight = mWall.getHeight();
		
		setMinimumWidth(wall.getWidth());
		setMinimumHeight(wall.getHeight());
		
		invalidate();
	}
	
	private void drawBlock(Canvas canvas, Block block) {
		int width = block.getWidth();
		int height = block.getHeight();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (block.getAt(j, i) == Block.STATE_FILL) {
					int left = (block.getX() + j) * DOT_WIDTH;
					int top = (mWallHeight - 1 - (block.getY() + i)) * DOT_HEIGHT;
					mHelperRect.set(left, top, left + DOT_WIDTH, top + DOT_HEIGHT);
					mBlockDrawable.setBounds(mHelperRect);
					mBlockDrawable.draw(canvas);
					
					//Log.d(TAG, "left = " + left + ", top =" + top + ", state = " + mWall.getAt(i, j));
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		mPaint.setColor(Color.BLUE);	
		mPaint.setStyle(Style.STROKE); 
		
		DOT_WIDTH = this.getMeasuredWidth() / 10;
		DOT_HEIGHT = DOT_WIDTH;
		
		if (mWall != null) {
			drawBlock(canvas, mWall);
		}
		
		if (mBlock != null) {
			drawBlock(canvas, mBlock);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub	
		int specMode, specSize, targetWith;
		int orientation = getResources().getConfiguration().orientation;
		
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			specMode = MeasureSpec.getMode(widthMeasureSpec);
			specSize = MeasureSpec.getSize(widthMeasureSpec);	
			
			if (specMode == MeasureSpec.AT_MOST) {
				targetWith = specSize * 2/3;
			} else {
				targetWith = specSize;
			}
		} else {
			specMode = MeasureSpec.getMode(heightMeasureSpec);
			specSize = MeasureSpec.getSize(heightMeasureSpec);
			
			if (specMode == MeasureSpec.AT_MOST) {
				targetWith = specSize / 2;
			} else {
				targetWith = specSize;
			}
		}
		
		if (targetWith % 10 != 0) {
			targetWith = (targetWith / 10) * 10; 
		}
		setMeasuredDimension(targetWith, targetWith * 2);
	}
}
