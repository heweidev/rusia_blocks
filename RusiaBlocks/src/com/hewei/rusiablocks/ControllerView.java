package com.hewei.rusiablocks;

import com.hewei.rusiablocks.game.Game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ControllerView extends RelativeLayout implements View.OnClickListener {
	private OnActionListener mActionListener;
	
	public ControllerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public interface OnActionListener {
		void onAction(Game.Action action);
	}
	
	public void init(int resId, OnActionListener listener) {
		mActionListener = listener;
		inflate(getContext(), resId, this);
		
		findViewById(R.id.bvLeft).setOnClickListener(this);
		findViewById(R.id.bvRight).setOnClickListener(this);
		findViewById(R.id.bvDown).setOnClickListener(this);
		findViewById(R.id.bvTransform).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bvLeft:
			mActionListener.onAction(Game.Action.LEFT);
			break;
		case R.id.bvRight:
			mActionListener.onAction(Game.Action.RIGHT);
			break;
		case R.id.bvDown:
			mActionListener.onAction(Game.Action.DOWN);
			break;
		case R.id.bvTransform:
			mActionListener.onAction(Game.Action.TRANSFORM);
			break;
		default:
			break;
		}
	}
}
