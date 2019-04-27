//package com.alex_podolian.npuzzle.model.algorithms;
//
//import android.os.AsyncTask;
//
//import com.alex_podolian.npuzzle.model.PuzzleBoard;
//import com.alex_podolian.npuzzle.model.callbacks.OnSolvePuzzle;
//import com.alex_podolian.npuzzle.utils.RLogs;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.PriorityQueue;
//
//public class DijkstraTask extends AsyncTask<Void, Void, ArrayList<PuzzleBoard>> {
//
//	private PuzzleBoard     puzzleBoard;
//	private OnSolvePuzzle   callback;
//
//	private Comparator<PuzzleBoard> comparator = (puzzleBoard1, puzzleBoard2) ->
//		puzzleBoard1.getStep() - puzzleBoard2.getStep();
//
//
//	public DijkstraTask(PuzzleBoard puzzleBoard, OnSolvePuzzle callback) {
//		this.puzzleBoard = puzzleBoard;
//		this.callback = callback;
//	}
//
//	@Override
//	protected ArrayList<PuzzleBoard> doInBackground(Void... params) {
//		RLogs.w("SOLVING...");
//		PriorityQueue<PuzzleBoard> frontier = new PriorityQueue<>(1, comparator);
//
//		frontier.add(puzzleBoard);
//		puzzleBoard.setPreviousBoard(null);
//		ArrayList<PuzzleBoard> cameFrom = new ArrayList<>();
//		int[] costSoFar = new int[] {0};
//
//		int count = 0;
//		int complexityInSize = 0;
//		int newCost = 0;
//		while(!frontier.isEmpty() && count < 500000) {
//			PuzzleBoard current = frontier.poll();
//			cameFrom.add(current);
//			complexityInSize += frontier.size();
//
//			if (current.isGoal()) {
//				RLogs.w("SOLVED!");
//				ArrayList<PuzzleBoard> steps = new ArrayList<>();
//				while(current.getPreviousBoard() != null) {
//					steps.add(current);
//					current = current.getPreviousBoard();
//				}
//				RLogs.w("STEPS = " + steps.size());
//				RLogs.w("COMPLEXITY IN TIME = " + count);
//				RLogs.w("COMPLEXITY IN SIZE = " + complexityInSize);
//				Collections.reverse(steps);
//				return steps;
//			}
//			for (PuzzleBoard neighbor : current.neighbours()) {
//				newCost = costSoFar[count++] + 1;
//				for (int i = 0; i < costSoFar.length; i++) {
//					if (neighbor.getStep() == costSoFar[i] || ) {
//
//					}
//				}
//
//				if (cameFrom.contains(neighbor)) {
//					continue;
//				}
//				if (frontier.contains(neighbor)) {
//					continue;
//				}
//				frontier.add(neighbor);
//			}
//		}
//		return null;
//	}
//
//	@Override
//	protected void onPostExecute(ArrayList<PuzzleBoard> steps) {
//		super.onPostExecute(steps);
//		if (callback != null) {
//			callback.onPuzzleSolved(steps);
//		}
//	}
//}
