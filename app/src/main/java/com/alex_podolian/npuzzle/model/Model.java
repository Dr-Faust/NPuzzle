package com.alex_podolian.npuzzle.model;

import android.content.Context;

import com.alex_podolian.npuzzle.model.callbacks.OnCreateMap;

public class Model {
	public void createMap(int puzzleSize, OnCreateMap callback, Context context) {
		CreateMapTask createMapTask = new CreateMapTask(puzzleSize, callback, context);
		createMapTask.execute();
	}
}
