package com.hewei.rusiablocks;

import java.net.SocketAddress;

import com.hewei.player.Player;
import com.hewei.rusiablocks.game.Constants;
import com.hewei.rusiablocks.game.Game;
import com.hewei.rusiablocks.game.Game.Action;
import com.hewei.rusiablocks.game.LocalGame;
import com.hewei.rusiablocks.game.RemoteGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CompetionActivity extends Activity implements RemoteGame.RemoteGameListener,
		Game.GameListener, DialogInterface.OnClickListener,
		ControllerView.OnActionListener {
	public static final String EXTRA_REMOTE_ADDR = "CompetionActivity.REMOTE_ADDR";
	private static final String TAG = "CompetionActivity";
	
	private Game mLocalGame;
	private RemoteGame mRemoteGame;
	
	ViewHolder mLocalHolder;
	ViewHolder mRemoteHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competion);
		
		mLocalHolder = new ViewHolder(R.id.gmvLocal);
		mRemoteHolder = new ViewHolder(R.id.gmvRemote);
		
		// init controller first
		ControllerView controlerView = (ControllerView) findViewById(R.id.controler);
		controlerView.init(R.layout.simple_controler, this);
		
		SocketAddress remoteAddr = (SocketAddress) getIntent().getSerializableExtra(EXTRA_REMOTE_ADDR);
		if (remoteAddr != null) {
			mRemoteGame = new RemoteGame();
			mRemoteGame.setGameListener(this);
			mRemoteGame.init(Constants.DATA_PORT);
			
			// 直接开始
			Player player = new Player("test");
			mLocalGame = new LocalGame(player, remoteAddr);
			mLocalGame.setGameListener(this);
			mLocalGame.start();
		} else {
			throw new IllegalArgumentException("intent must contain remote and local address info");
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.quit_game_check);
		
		builder.setNegativeButton(R.string.continue_game, this);
		builder.setPositiveButton(R.string.quit_game, this);
		builder.create().show();
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "remote update.");
		updateRemoteView();
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		Log.d(TAG, "local step.");
		updateLocalView();
	}

	@Override
	public void onGameOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameLevelChanged(int newLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetScore(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewBlock() {
		// TODO Auto-generated method stub
		
	}
	
	private final class ViewHolder {
		public ViewHolder(int parentId) {
			View parent = findViewById(parentId);
			mWallView = (WallView) parent.findViewById(R.id.vWall);
			mScoreView = (TextView) parent.findViewById(R.id.tvScore);
			mLevelView = (TextView) parent.findViewById(R.id.tvLevel);
		}
		
		WallView mWallView;
		TextView mScoreView;
		TextView mLevelView;
	}
	
	private void updateLocalView() {
		mLocalHolder.mWallView.setWallAndBlock(mLocalGame.getWall(), mLocalGame.getCurBlock());
	}
	
	private void updateRemoteView() {
		mRemoteHolder.mWallView.setWallAndBlock(mRemoteGame.getWall(), mRemoteGame.getCurBlock());
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		case DialogInterface.BUTTON_POSITIVE:
			stopGame();
			finish();
			break;
		}
	}
	
	private void stopGame() {
		if (mLocalGame != null) {
			mLocalGame.stop();
		}
		if (mRemoteGame != null) {
			mRemoteGame.stop();
		}
	}

	@Override
	public void onAction(Action action) {
		// TODO Auto-generated method stub
		mLocalGame.onAction(action);
	}
}
