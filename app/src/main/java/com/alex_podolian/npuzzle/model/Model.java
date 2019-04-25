package com.alex_podolian.npuzzle.model;

import android.content.Context;

import com.alex_podolian.npuzzle.model.callbacks.OnCreateMap;
import com.alex_podolian.npuzzle.model.callbacks.OnSolvePuzzle;

public class Model {

	public void createMap(int puzzleSize, OnCreateMap callback, Context context) {
		CreateMapTask task = new CreateMapTask(puzzleSize, callback, context);
		task.execute();
	}

	public void solvePuzzle(PuzzleBoard puzzleBoard, OnSolvePuzzle callback) {
		SolvePuzzleTask task = new SolvePuzzleTask(puzzleBoard, callback);
		task.execute();
	}
}
