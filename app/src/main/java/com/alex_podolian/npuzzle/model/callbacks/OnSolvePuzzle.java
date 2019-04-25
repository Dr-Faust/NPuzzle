package com.alex_podolian.npuzzle.model.callbacks;

import com.alex_podolian.npuzzle.model.PuzzleBoard;

import java.util.ArrayList;

public interface OnSolvePuzzle {
	void onPuzzleSolved(ArrayList<PuzzleBoard> steps);
}
