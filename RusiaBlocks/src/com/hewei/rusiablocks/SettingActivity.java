package com.hewei.rusiablocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		
		findViewById(R.id.bkg_color_layout).setOnClickListener(this);
		findViewById(R.id.screen_direction_layout).setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bkg_color_layout:
			break;
		case R.id.screen_direction_layout:
			break;
		default:
			break;
		}
	}
}
