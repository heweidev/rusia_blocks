package com.hewei.player;

import com.hewei.rusiablocks.GameApp;

import android.content.Context;
import android.content.SharedPreferences;

public class Player {
	private static final String HIGHEST_SCORE = "highest_score";
	private static final String LEVEL = "level";
	private static final String NAME = "name";
	
	private int mHighestScore;
	private int mLevel;
	private String mName;
	
	public Player(String name) {
		mName = name;
	}
	
	public void load() {
		SharedPreferences sp = GameApp.sContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
		mHighestScore = sp.getInt(HIGHEST_SCORE, 0);
		mLevel = sp.getInt(LEVEL, 0);
		mName = sp.getString(NAME, "default");
	}
	
	public void save() {
		SharedPreferences sp = GameApp.sContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(HIGHEST_SCORE, mHighestScore);
		editor.putInt(LEVEL, mLevel);
		editor.putString(NAME, mName);
		editor.commit();
	}

	public int getHighestScore() {
		return mHighestScore;
	}

	public void setHighestScore(int score) {
		if (score > mHighestScore) {
			this.mHighestScore = score;
		}
	}

	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int mLevel) {
		this.mLevel = mLevel;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}
}
