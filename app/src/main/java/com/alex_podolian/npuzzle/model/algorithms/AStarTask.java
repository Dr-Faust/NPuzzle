package com.alex_podolian.npuzzle.model.algorithms;

import android.os.AsyncTask;

import com.alex_podolian.npuzzle.model.PuzzleBoard;
import com.alex_podolian.npuzzle.model.callbacks.OnSolvePuzzle;
import com.alex_podolian.npuzzle.utils.RLogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AStarTask extends AsyncTask<Void, Void, ArrayList<PuzzleBoard>> {

	private PuzzleBoard     puzzleBoard;
	private OnSolvePuzzle   callback;
	private int             algorithm;
	private int             heuristics;
	private long            complexityInTime = 0;
	private long            complexityInSize = 0;

	private Comparator<PuzzleBoard> comparator = (puzzleBoard1, puzzleBoard2) -> {
		if (algorithm == 0) {
			return puzzleBoard1.getF(heuristics) - puzzleBoard2.getF(heuristics);
		} else {
			return puzzleBoard1.getH(heuristics) - puzzleBoard2.getH(heuristics);
		}
	};


	public AStarTask(PuzzleBoard puzzleBoard, OnSolvePuzzle callback, int algorithm, int heuristics) {
		this.puzzleBoard = puzzleBoard;
		this.callback = callback;
		this.algorithm = algorithm;
		this.heuristics = heuristics;
	}

	@Override
	protected ArrayList<PuzzleBoard> doInBackground(Void... params) {
		RLogs.w("SOLVING...");
		PriorityQueue<PuzzleBoard> frontier = new PriorityQueue<>(1, comparator);

		frontier.add(puzzleBoard);
		puzzleBoard.setPreviousBoard(null);
		ArrayList<PuzzleBoard> cameFrom = new ArrayList<>();

		int count = 0;
		while(!frontier.isEmpty() && count++ < 500000) {
			PuzzleBoard current = frontier.poll();
			cameFrom.add(current);
			complexityInTime++;
			if (frontier.size() > complexityInSize) {
				complexityInSize = frontier.size();
			}

			if (current.isGoal()) {
				RLogs.w("SOLVED!");
				ArrayList<PuzzleBoard> steps = new ArrayList<>();
				while(current.getPreviousBoard() != null) {
					steps.add(current);
					current = current.getPreviousBoard();
				}
				RLogs.w("STEPS = " + steps.size());
				RLogs.w("COMPLEXITY IN TIME = " + complexityInTime);
				RLogs.w("COMPLEXITY IN SIZE = " + complexityInSize);
				Collections.reverse(steps);
				return steps;
			}

			for (PuzzleBoard neighbor : current.neighbours()) {
				if (cameFrom.contains(neighbor)) {
					continue;
				}
				if (frontier.contains(neighbor)) {
					continue;
				}
				frontier.add(neighbor);
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<PuzzleBoard> steps) {
		super.onPostExecute(steps);
		if (callback != null) {
			callback.onPuzzleSolved(steps, complexityInTime, complexityInSize);
		}
	}
}
