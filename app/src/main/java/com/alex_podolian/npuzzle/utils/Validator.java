package com.alex_podolian.npuzzle.utils;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Validator {

	private static int manhattan(int puzzleSize, ArrayList<Integer> startMap, ArrayList<Integer> goalMap) {
		int dist = 0;

		for(Integer node : startMap) {
			if (node != 0 && goalMap.indexOf(node) != startMap.indexOf(node)) {
				int xGoal = goalMap.indexOf(node) / puzzleSize;
				int yGoal = goalMap.indexOf(node) % puzzleSize;
				int xTab = startMap.indexOf(node) / puzzleSize;
				int yTab = startMap.indexOf(node) % puzzleSize;
				dist += abs(xGoal - xTab) + abs(yGoal - yTab);
			}
		}
		return dist;
	}

	public static boolean isSolvable(int puzzleSize, ArrayList<Integer> startMap, ArrayList<Integer> goalMap) {
		int nbInv = 0;
		ArrayList<Integer> testDone = new ArrayList<>();

		for(int indexGoal = 0; indexGoal < puzzleSize * puzzleSize - 1; indexGoal++) {
			for(int indexPuzzle = 0; indexPuzzle < startMap.indexOf(goalMap.get(indexGoal)); indexPuzzle++) {
				if (!testDone.contains(startMap.get(indexPuzzle))) {
					nbInv++;
				}
			}
			testDone.add(goalMap.get(indexGoal));
		}

		RLogs.w("TEST DONE SIZE = " + testDone.size());
		int dist = manhattan(puzzleSize, startMap, goalMap);
		RLogs.w("DIST = " + dist);
		RLogs.w("NB INV = " + nbInv);
		boolean solvable;
		if (dist % 2 == 0 && nbInv % 2 == 0 || dist % 2 != 0 && nbInv % 2 != 0) {
			RLogs.w("SOLVABLE!!!");
			solvable = true;
		} else {
			RLogs.w("UNSOLVABLE!!!");
			solvable = false;
		}
		return solvable;
	}
}
