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
		int inversions = 0;
		ArrayList<Integer> testDone = new ArrayList<>();

		for (int indexGoal = 0; indexGoal < puzzleSize * puzzleSize - 1; indexGoal++) {
			for (int indexPuzzle = 0; indexPuzzle < startMap.indexOf(goalMap.get(indexGoal)); indexPuzzle++) {
				if (!testDone.contains(startMap.get(indexPuzzle))) {
					inversions++;
				}
			}
			testDone.add(goalMap.get(indexGoal));
		}
		int dist = manhattan(puzzleSize, startMap, goalMap);
		return dist % 2 == 0 && inversions % 2 == 0 || dist % 2 != 0 && inversions % 2 != 0;
	}

	public static ArrayList<Integer> getValidMap(ArrayList<String> map) {
		ArrayList<Integer> validMap = new ArrayList<>();
		boolean puzzleSizeObtained = false;
		int puzzleSize = 0;
		int count;

		for (String line : map) {
			line = line.trim();
			String[] splittedLines = line.split(" ");
			count = 0;
			if (line.contains("#") && line.indexOf("#") == 0) {
				continue;
			}
			for (String splittedLine : splittedLines) {
//				RLogs.w("SPLITTED LINE #" + index + " = " + splittedLine + " MATCHES = " + splittedLine.matches("\\d"));
				if (splittedLine.matches("\\d+")) {
					int value = Integer.parseInt(splittedLine);
					if (!puzzleSizeObtained) {
						puzzleSize = value;
						puzzleSizeObtained = true;
					} else if (count < puzzleSize && value < puzzleSize * puzzleSize) {
						validMap.add(value);
						count++;
					}
				}
			}
		}
		if (validMap.size() == puzzleSize * puzzleSize) {
			return validMap;
		}
		else {
			return null;
		}
	}
}
