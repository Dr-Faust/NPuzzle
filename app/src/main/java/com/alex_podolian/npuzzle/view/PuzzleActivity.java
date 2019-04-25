package com.alex_podolian.npuzzle.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.model.PuzzleBoard;
import com.alex_podolian.npuzzle.model.callbacks.OnComplete;
import com.alex_podolian.npuzzle.utils.Utils;
import com.alex_podolian.npuzzle.utils.Validator;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PuzzleActivity extends BaseActivity implements View.OnClickListener, OnComplete {

	@BindView(R.id.toolbar_board)       Toolbar toolbar;
	@BindView(R.id.cl_puzzle_container) ConstraintLayout clPuzzleContainer;
	@BindView(R.id.btn_shuffle)         MaterialButton btnShuffle;
	@BindView(R.id.btn_solve)           MaterialButton btnSolve;
	@BindView(R.id.btn_show_steps)      MaterialButton btnShowSteps;


	private PuzzleBoardView puzzleBoardView;
	private ProgressDialog progressDialog;
	private int puzzleSize;
	private int textSize;
	private ArrayList<Integer> map;
	private ArrayList<Integer> listMaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);

		ButterKnife.bind(this);
		btnShowSteps.setVisibility(View.GONE);

		toolbar.setNavigationOnClickListener(v -> onBackPressed());
		btnShuffle.setOnClickListener(this);
		btnSolve.setOnClickListener(this);
		btnShowSteps.setOnClickListener(this);

		String option = getIntent().getStringExtra("option");
		puzzleSize = getIntent().getIntExtra("puzzle_size", 3);
		textSize = getIntent().getIntExtra("text_size", 24);
		map = getIntent().getIntegerArrayListExtra("map");

		if (option.equals("new")) {
			puzzleBoardView = new PuzzleBoardView(this, puzzleSize, textSize, map, presenter);
		} else if (option.equals("random")){
			puzzleBoardView = new PuzzleBoardView(this, puzzleSize, textSize,
				Utils.shuffleMap(map, puzzleSize), presenter);
		}
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		puzzleBoardView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
			displaymetrics.widthPixels - 50));
		clPuzzleContainer.addView(puzzleBoardView);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_shuffle:
				btnShowSteps.setVisibility(View.GONE);
				puzzleBoardView.shuffleBoard();
				puzzleBoardView.invalidate();
				break;
			case R.id.btn_solve:
				if (Validator.isSolvable(puzzleSize, map, Utils.makeSpiralMap(puzzleSize))) {
					progressDialog = new ProgressDialog(this);
					progressDialog.setMessage("Solving...");
					progressDialog.setCancelable(false);
					progressDialog.show();
					puzzleBoardView.solve(this);
				} else {
					Toast.makeText(this, "Puzzle is unsolvable! Please shuffle.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_show_steps:
				Intent intent = new Intent(this, StepsActivity.class);
				intent.putIntegerArrayListExtra("list_maps", listMaps);
				intent.putExtra("text_size", textSize);
				intent.putExtra("puzzle_size", puzzleSize);
				startActivity(intent);
				break;
		}
	}

	@Override
	public void onCompleted(ArrayList<PuzzleBoard> steps) {
		if (steps != null) {
			listMaps = new ArrayList<>();
			for (PuzzleBoard puzzleBoard : steps) {
				listMaps.addAll(puzzleBoard.getPuzzleMap());
			}
			btnShowSteps.setVisibility(View.VISIBLE);
		}
		progressDialog.dismiss();
	}
}
