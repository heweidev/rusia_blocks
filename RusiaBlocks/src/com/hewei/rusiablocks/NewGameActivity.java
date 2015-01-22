package com.hewei.rusiablocks;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.hewei.rusiablocks.game.GameBuilder;
import com.hewei.rusiablocks.game.GamePeer;
import com.hewei.rusiablocks.game.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NewGameActivity extends Activity implements View.OnClickListener,
		GameBuilder.OnPeerConnectListener,
		GamePeer.OnFindServerListener {
	private TextView mLogView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_guide);
		
		findViewById(R.id.btBuild).setOnClickListener(this);
		findViewById(R.id.btJoin).setOnClickListener(this);
		findViewById(R.id.btLocal).setOnClickListener(this);
		findViewById(R.id.btTest).setOnClickListener(this);
		
		mLogView = (TextView)findViewById(R.id.tvLog);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btBuild:
			new GameBuilder(this).init();
			break;
		case R.id.btLocal:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			
			finish();
			break;
		case R.id.btJoin:
			new GamePeer(this).init();
			break;
		case R.id.btTest:
			testCompetionGame();
			break;
		default:
			break;
		}
	}
	
	private void Log(String str) {
		StringBuilder builder = new StringBuilder();
		builder.append(mLogView.getText());
		builder.append("\n");
		builder.append(str);
		mLogView.setText(builder);
	}

	@Override
	public void onPeerConnect(SocketAddress addr) {
		// TODO Auto-generated method stub
		Log(addr.toString());
		startCompetionGame(addr);
	}

	@Override
	public void onFindServer(SocketAddress addr) {
		// TODO Auto-generated method stub
		Log(addr.toString());
		startCompetionGame(addr);
	}
	
	private void startCompetionGame(SocketAddress remote) {
		Intent intent = new Intent(this, CompetionActivity.class);
		intent.putExtra(CompetionActivity.EXTRA_REMOTE_ADDR, remote);
		startActivity(intent);
		
		finish();
	}
	
	// 测试自发自收
	private void testCompetionGame() {
		SocketAddress remote = new InetSocketAddress("127.0.0.1", Constants.DATA_PORT);
		startCompetionGame(remote);
	}
}
