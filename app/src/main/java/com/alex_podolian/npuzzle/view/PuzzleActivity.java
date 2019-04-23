package com.alex_podolian.npuzzle.view;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.utils.Utils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PuzzleActivity extends BaseActivity implements View.OnClickListener {

	@BindView(R.id.toolbar_board)       Toolbar toolbar;
	@BindView(R.id.puzzle_container)    RelativeLayout rlPuzzleContainer;
	@BindView(R.id.btn_shuffle)         MaterialButton btnShuffle;
	@BindView(R.id.btn_solve)           MaterialButton btnSolve;


	private PuzzleBoardView puzzleBoardView;
	private int puzzleSize;
	private int textSize;
	private ArrayList<Integer> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);

		ButterKnife.bind(this);

		toolbar.setNavigationOnClickListener(v -> onBackPressed());
		btnShuffle.setOnClickListener(this);
		btnSolve.setOnClickListener(this);

		String option = getIntent().getStringExtra("option");
		puzzleSize = getIntent().getIntExtra("puzzle_size", 3);
		textSize = getIntent().getIntExtra("text_size", 24);
		map = getIntent().getIntegerArrayListExtra("map");

		int[][] mapMatrix = createMapMatrix(map, puzzleSize);
		if (option.equals("new")) {
			puzzleBoardView = new PuzzleBoardView(this, puzzleSize, textSize, mapMatrix);
		} else if (option.equals("random")){
			puzzleBoardView = new PuzzleBoardView(this, puzzleSize, textSize, Utils.shuffleMatrix(mapMatrix, puzzleSize));
		}
		rlPuzzleContainer.addView(puzzleBoardView);
	}

	private int[][] createMapMatrix(ArrayList<Integer> map, int puzzleSize) {
		int[][] mapMatrix = new int[puzzleSize][puzzleSize];
		int index = 0;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				mapMatrix[i][j] = map.get(index++);
			}
		}
		return mapMatrix;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_shuffle:
				puzzleBoardView.shuffleBoard();
				puzzleBoardView.invalidate();
				break;
			case R.id.btn_solve:
				break;
		}
	}
}
