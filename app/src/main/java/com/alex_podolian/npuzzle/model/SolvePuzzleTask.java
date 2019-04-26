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
	private int             heuristics;

	private Comparator<PuzzleBoard> manhattan = (puzzleBoard1, puzzleBoard2) ->
		puzzleBoard1.manhattan() - puzzleBoard2.manhattan();
	private Comparator<PuzzleBoard> euclidean = (puzzleBoard1, puzzleBoard2) ->
		puzzleBoard1.euclidean() - puzzleBoard2.euclidean();
	private Comparator<PuzzleBoard> BlocksOutOfRowAndCol = (puzzleBoard1, puzzleBoard2) ->
		puzzleBoard1.blocksOutOfRowAndColumn() - puzzleBoard2.blocksOutOfRowAndColumn();
	private Comparator<PuzzleBoard> linearConflict = (puzzleBoard1, puzzleBoard2) ->
		puzzleBoard1.linearConflict() - puzzleBoard2.linearConflict();
	private Comparator<PuzzleBoard> hamming = (puzzleBoard1, puzzleBoard2) ->
		puzzleBoard1.misplacedBlocks() - puzzleBoard2.misplacedBlocks();


	public SolvePuzzleTask(PuzzleBoard puzzleBoard, OnSolvePuzzle callback, int heuristics) {
		this.puzzleBoard = puzzleBoard;
		this.callback = callback;
		this.heuristics = heuristics;
	}

	@Override
	protected ArrayList<PuzzleBoard> doInBackground(Void... params) {
		RLogs.w("SOLVING...");
		PriorityQueue<PuzzleBoard> frontier;
		switch (heuristics) {
			case 0:
				frontier = new PriorityQueue<>(1, manhattan);
				break;
			case 1:
				frontier = new PriorityQueue<>(1, euclidean);
				break;
			case 3:
				frontier = new PriorityQueue<>(1, BlocksOutOfRowAndCol);
				break;
			case 4:
				frontier = new PriorityQueue<>(1, linearConflict);
				break;
			case 5:
				frontier = new PriorityQueue<>(1, hamming);
				break;
			default:
				frontier = new PriorityQueue<>(1, manhattan);
		}

		frontier.add(puzzleBoard);
		puzzleBoard.setPreviousBoard(null);
		ArrayList<PuzzleBoard> cameFrom = new ArrayList<>();

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
}
