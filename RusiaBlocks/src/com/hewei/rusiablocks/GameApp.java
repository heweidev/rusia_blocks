package com.hewei.rusiablocks;

import android.app.Application;
import android.content.Context;

public class GameApp extends Application {
	public static Context sContext;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sContext = this;
	}
}
