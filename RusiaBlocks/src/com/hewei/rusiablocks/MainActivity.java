package com.hewei.rusiablocks;

import com.hewei.player.Player;
import com.hewei.rusiablocks.game.Game;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, Game.GameListener {
	private static final String EXTRA_PLAYER_NAME = "player_name";
	private static final String CONFIG_NAME = "game_config";
	private static final String TAG = "MainActivity";
		
	private WallView mWallView;
	private BlockView mNextBlockView;
	private TextView mScoreView;
	private TextView mLevelView;
	private Button mCtrlButton;

	private boolean mRunning = false;
	private boolean mPaused = false;	// 菜单弹出时的状态
	
	// time mark for last back key click.
	private long mPreTime = 0;
	
	private GestureDetector mGestureDetector; 
	
	private Player mPlayer;
	private Game mGame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		UmengUpdateAgent.update(this);
		Log.d(TAG, "onCreate()");
		
		findViewById(R.id.bvLeft).setOnClickListener(this);
		findViewById(R.id.bvRight).setOnClickListener(this);
		findViewById(R.id.bvDown).setOnClickListener(this);
		findViewById(R.id.bvTransform).setOnClickListener(this);
		
		mWallView = (WallView) findViewById(R.id.vWall);
		mNextBlockView = (BlockView) findViewById(R.id.vBlock);
		
		int bkgColor = loadBkgColor();
		if (bkgColor != 0) {
			mWallView.setBackgroundColor(bkgColor);
			mNextBlockView.setBackgroundColor(bkgColor);
		}
		
		mScoreView = (TextView) findViewById(R.id.tvScore);
		mLevelView = (TextView) findViewById(R.id.tvLevel);
		
		mCtrlButton = (Button) findViewById(R.id.bvControl);
		mCtrlButton.setOnClickListener(this);
		
		String playerName = null;
		if (savedInstanceState != null) {
			playerName = savedInstanceState.getString("player");
		} else {
			Intent intent = this.getIntent();
			if (intent != null) {
				playerName = intent.getStringExtra(EXTRA_PLAYER_NAME);
			} 
		}
		
		if (playerName == null) {
			playerName = "default";
		}
		
		mPlayer = new Player(playerName);
		mPlayer.load();

		mGame = new Game(mPlayer);
		mGame.setGameListener(this);
		
		updateGameView();
		mNextBlockView.setBlock(mGame.getNextBlock());
		
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		MobclickAgent.updateOnlineConfig(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem item = menu.findItem(R.id.action_roate);
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			item.setTitle(R.string.oriention_land);
		} else {
			item.setTitle(R.string.oriention_port);
		}
		
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_roate:
			if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			break;
		case R.id.action_sel_bkg:
			ColorPickerDialog dialog = new ColorPickerDialog(this, "", mColorListener);
			dialog.show();
			break;
		case R.id.action_feedback:
			 FeedbackAgent agent = new FeedbackAgent(this);
			 agent.startFeedbackActivity();
			break;
		default:
			break;
		}
		
		return true;
	}
	
	private ColorPickerDialog.OnColorChangedListener mColorListener = new ColorPickerDialog.OnColorChangedListener() {

		@Override
		public void colorChanged(int color) {
			// TODO Auto-generated method stub
			mWallView.setBackgroundColor(color);
			mNextBlockView.setBackgroundColor(color);
			saveBkgColor(color);
		}
		
	};
	
	private int loadBkgColor() {
		SharedPreferences pref = getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		return pref.getInt("bkg_color", 0);
	}
	
	private void saveBkgColor(int color) {
		SharedPreferences pref = getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("bkg_color", color);
		editor.commit();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (mRunning && mPaused) {
			start();
		}
		mPaused = false;
		
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		mPaused = true;	
		if (mRunning) {
			stop();
		}
		
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable("game", mGame);
	}

	private void start() {
		mGame.start();	
		mRunning = true;	
		updateCtrlButton();
	}
	
	private void stop() {
		if (mGame != null) {
			mGame.stop();
		}
		mRunning = false;
		updateCtrlButton();
	}
	
	private void exit() {
		finish();
		System.exit(0);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		long curTime = System.currentTimeMillis();
		if (curTime - mPreTime < 2000) {
			exit();
		} else {
			mPreTime = curTime;
			Toast.makeText(this, R.string.exit_after_next_click, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bvLeft:
			mGame.onAction(Game.Action.LEFT);
			break;
		case R.id.bvRight:
			mGame.onAction(Game.Action.RIGHT);
			break;
		case R.id.bvDown:
			mGame.onAction(Game.Action.DOWN);
			break;
		case R.id.bvTransform:
			mGame.onAction(Game.Action.TRANSFORM);
			break;
		case R.id.bvControl:
			if (mRunning) {
				stop();
			} else {
				start();
			}
			break;
		default:
			break;
		}
		
		mWallView.invalidate();
	}
	
	private class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if (Math.abs(velocityX) > Math.abs(velocityY)) {
				if (velocityX > 0) {
					mGame.onAction(Game.Action.RIGHT);
				} else {
					mGame.onAction(Game.Action.LEFT);
				}
			} else {
				if (velocityY > 0) {
					mGame.onAction(Game.Action.DOWN);
				}
			}
			
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			mGame.onAction(Game.Action.TRANSFORM);
			return true;
		}		
	}

	private void updateGameView() {
		int score = mGame.getScore();
		int level = mGame.getLevel();
		
		mLevelView.setText("等级: " + String.valueOf(level));
		mScoreView.setText("分数: " + String.valueOf(score));
		mWallView.setWallAndBlock(mGame.getWall(), mGame.getCurBlock());
	}
	
	private void updateCtrlButton() {
		if (mRunning) {
			mCtrlButton.setText(R.string.action_pause);
		} else {
			mCtrlButton.setText(R.string.action_start);
		}
	}

	@Override
	public void onGameOver() {
		// TODO Auto-generated method stub
		Log.d(TAG, "game over!");
		
		stop();
		start();
	}

	@Override
	public void onGameLevelChanged(int newLevel) {
		// TODO Auto-generated method stub
		Log.d(TAG, "game level changed! newLevel = " + newLevel);
	}

	@Override
	public void onGetScore(int score) {
		// TODO Auto-generated method stub
		Log.d(TAG, "get score: " + score);
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		updateGameView();
	}

	@Override
	public void onNewBlock() {
		// TODO Auto-generated method stub
		mNextBlockView.setBlock(mGame.getNextBlock());
	}
}
