package com.hewei.rusiablocks;

import com.hewei.rusiablocks.block.Block;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class BlockView extends View {
	private static final String TAG = "BlockView";	
	private static final int BLOCK_BOUND_SIZE = 4;
	private static Drawable mBlockDrawable;
	private static int DOT_WIDTH, DOT_HEIGHT;
	
	private Block mBlock;
	private Rect mHelperRect;
	
	public BlockView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public BlockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context ctx) {
		if (mBlockDrawable == null) {
			mBlockDrawable = ctx.getResources().getDrawable(R.drawable.dot);
			DOT_WIDTH = mBlockDrawable.getIntrinsicWidth();
			DOT_HEIGHT = mBlockDrawable.getIntrinsicHeight();
		}
		
		mHelperRect = new Rect();
	}
	
	public void setBlock(Block block) {
		mBlock = block;
		setMinimumWidth(mBlock.getWidth());
		setMinimumHeight(mBlock.getHeight());
		
		invalidate();
	}
	
	private void drawBlock(Canvas canvas, Block block) {
		if (block == null) {
			return;
		}
	
		int width = block.getWidth();
		int height = block.getHeight();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (block.getAt(j, i) == Block.STATE_FILL) {
					int left = j * DOT_WIDTH;
					int top = (BLOCK_BOUND_SIZE - i - 1) * DOT_HEIGHT;
					mHelperRect.set(left, top, left + DOT_WIDTH, top + DOT_HEIGHT);
					mBlockDrawable.setBounds(mHelperRect);
					mBlockDrawable.draw(canvas);
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub		
		if (mBlock != null) {
			drawBlock(canvas, mBlock);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if (mBlock == null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else {
			this.setMeasuredDimension(BLOCK_BOUND_SIZE * DOT_WIDTH + 2,
					BLOCK_BOUND_SIZE * DOT_HEIGHT + 2);	
		}
	}
}
