package com.alex_podolian.npuzzle.model;

import android.os.AsyncTask;
import com.alex_podolian.npuzzle.model.callbacks.OnSolvePuzzle;
import com.alex_podolian.npuzzle.utils.RLogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SolvePuzzleTask extends AsyncTask<Void, Void, ArrayList<PuzzleBoard>> {

	private PuzzleBoard     puzzleBoard;
	private OnSolvePuzzle   callback;

	private Comparator<PuzzleBoard> comparator = (puzzleBoard1, puzzleBoard2) -> {
//		int one = puzzleBoard1.manhattan() + puzzleBoard1.hamming();
//		int two = puzzleBoard2.manhattan() + puzzleBoard2.hamming();
//		return one - two;

		return puzzleBoard1.euclidean() - puzzleBoard2.euclidean();
//		return puzzleBoard1.manhattan() - puzzleBoard2.manhattan();
	};

	public SolvePuzzleTask(PuzzleBoard puzzleBoard, OnSolvePuzzle callback) {
		this.puzzleBoard = puzzleBoard;
		this.callback = callback;
	}

	@Override
	protected ArrayList<PuzzleBoard> doInBackground(Void... params) {
		RLogs.w("SOLVING...");
		PriorityQueue<PuzzleBoard> frontier = new PriorityQueue<>(1, comparator);
		ArrayList<PuzzleBoard> cameFrom = new ArrayList<>();
		frontier.add(puzzleBoard);
		puzzleBoard.setPreviousBoard(null);

		int count = 0;
		while(!frontier.isEmpty() && count++ < 500000) {

			PuzzleBoard current = frontier.poll();

			if (current.isGoal()) {
				RLogs.w("SOLVED!");
				ArrayList<PuzzleBoard> steps = new ArrayList<>();
				while(current.getPreviousBoard() != null) {
					steps.add(current);
					current = current.getPreviousBoard();
				}
				RLogs.w("STEPS = " + steps.size());
				Collections.reverse(steps);
				return steps;
			}

			for (PuzzleBoard neighbor : current.neighbours()) {
				if (!cameFrom.contains(neighbor)) {
					frontier.add(neighbor);
				}
				if (!cameFrom.contains(current)) {
					cameFrom.add(current);
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<PuzzleBoard> steps) {
		super.onPostExecute(steps);
		if (callback != null) {
			callback.onPuzzleSolved(steps);
		}
	}

	//				RLogs.w("CONGRATS!! SOLVED!!");
//				for (Integer node : current.getPuzzleMap()) {
//					RLogs.w("NODE = " + node);
//				}
//
//				ArrayList<PuzzleBlock> puzzleBlocks = current.getPuzzleBlocks();
//
//				for (PuzzleBlock node : puzzleBlocks) {
//					RLogs.w("BLOCK ID = " + node.getId());
//				}
//
//				for (PuzzleBlock node : puzzleBlocks) {
//					RLogs.w("BLOCK INDEX = " + puzzleBlocks.indexOf(node));
//				}
//
//				RLogs.w("STEP NUMBER = " + current.getStepNumber());
//				Toast.makeText(context, "Solved!", Toast.LENGTH_SHORT).show();
}
