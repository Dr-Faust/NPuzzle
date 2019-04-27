package com.alex_podolian.npuzzle.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.model.PuzzleBoard;
import com.alex_podolian.npuzzle.model.callbacks.OnComplete;
import com.alex_podolian.npuzzle.utils.Utils;
import com.alex_podolian.npuzzle.utils.Validator;
import com.alex_podolian.npuzzle.view.custom_views.PuzzleBoardView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PuzzleActivity extends BaseActivity implements View.OnClickListener, OnComplete {

	@BindView(R.id.toolbar_board)           Toolbar toolbar;
	@BindView(R.id.cl_puzzle_container)     ConstraintLayout clPuzzleContainer;
	@BindView(R.id.btn_shuffle)             MaterialButton btnShuffle;
	@BindView(R.id.btn_reset)               MaterialButton btnReset;
	@BindView(R.id.btn_solve)               MaterialButton btnSolve;
	@BindView(R.id.btn_show_steps)          MaterialButton btnShowSteps;
	@BindView(R.id.tv_steps)                TextView tvSteps;
	@BindView(R.id.tv_complexity_in_time)   TextView tvComplexityInTime;
	@BindView(R.id.tv_complexity_in_size)   TextView tvComplexityInSize;


	private PuzzleBoardView puzzleBoardView;
	private ProgressDialog progressDialog;
	private int puzzleSize;
	private int textSize;
	private ArrayList<Integer> startMap;
	private ArrayList<Integer> goalMap;
	private ArrayList<Integer> listMaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);

		ButterKnife.bind(this);
		btnShowSteps.setVisibility(View.GONE);
		tvSteps.setVisibility(View.GONE);
		tvComplexityInTime.setVisibility(View.GONE);
		tvComplexityInSize.setVisibility(View.GONE);
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
		String option = getIntent().getStringExtra("option");

		if (option.equals("new")) {
			btnReset.setOnClickListener(this);
			btnShuffle.setVisibility(View.GONE);
		}
		else if (option.equals("random")) {
			btnShuffle.setOnClickListener(this);
			btnReset.setVisibility(View.GONE);
		}

		btnSolve.setOnClickListener(this);
		btnShowSteps.setOnClickListener(this);

		puzzleSize = getIntent().getIntExtra("puzzle_size", 3);
		textSize = getIntent().getIntExtra("text_size", 24);
		startMap = getIntent().getIntegerArrayListExtra("start_map");
		goalMap = getIntent().getIntegerArrayListExtra("goal_map");

		if (option.equals("new")) {
			puzzleBoardView = new PuzzleBoardView(this, puzzleSize, textSize, startMap,
				goalMap, presenter, true);
		} else if (option.equals("random")) {
			puzzleBoardView = new PuzzleBoardView(this, puzzleSize, textSize,
				Utils.shuffleMap(startMap, puzzleSize), goalMap, presenter, true);
		}
		clPuzzleContainer.addView(puzzleBoardView);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_shuffle:
				btnShowSteps.setVisibility(View.GONE);
				tvSteps.setVisibility(View.GONE);
				tvComplexityInTime.setVisibility(View.GONE);
				tvComplexityInSize.setVisibility(View.GONE);
				puzzleBoardView.shuffleBoard();
				puzzleBoardView.invalidate();
				break;
			case R.id.btn_reset:
				btnShowSteps.setVisibility(View.GONE);
				tvSteps.setVisibility(View.GONE);
				tvComplexityInTime.setVisibility(View.GONE);
				tvComplexityInSize.setVisibility(View.GONE);
				puzzleBoardView.initView();
				puzzleBoardView.invalidate();
				break;
			case R.id.btn_solve:
				if (Validator.isSolvable(puzzleSize, startMap, goalMap)) {
					progressDialog = new ProgressDialog(this);
					progressDialog.setMessage("Solving...");
					progressDialog.setCancelable(false);
					presenter.selectHeuristic(progressDialog, puzzleBoardView,this, this);
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
	public void onCompleted(ArrayList<PuzzleBoard> steps, long complexityInTime, long complexityInSize) {
		if (steps != null) {
			listMaps = new ArrayList<>();
			for (PuzzleBoard puzzleBoard : steps) {
				listMaps.addAll(puzzleBoard.getPuzzleMap());
			}
			tvSteps.setText("Total steps: " + steps.size());
			tvComplexityInTime.setText("Complexity in time: " + complexityInTime);
			tvComplexityInSize.setText("Complexity in size: " + complexityInSize);
			btnShowSteps.setVisibility(View.VISIBLE);
			tvSteps.setVisibility(View.VISIBLE);
			tvComplexityInTime.setVisibility(View.VISIBLE);
			tvComplexityInSize.setVisibility(View.VISIBLE);
		}
		progressDialog.dismiss();
	}
}
