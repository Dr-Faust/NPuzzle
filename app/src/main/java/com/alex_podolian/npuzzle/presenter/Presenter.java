package com.alex_podolian.npuzzle.presenter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.app.Config;
import com.alex_podolian.npuzzle.model.Model;
import com.alex_podolian.npuzzle.model.PuzzleBoard;
import com.alex_podolian.npuzzle.model.callbacks.OnComplete;
import com.alex_podolian.npuzzle.model.callbacks.OnCreateMap;
import com.alex_podolian.npuzzle.model.callbacks.OnSolvePuzzle;
import com.alex_podolian.npuzzle.utils.Utils;
import com.alex_podolian.npuzzle.utils.Validator;
import com.alex_podolian.npuzzle.view.PuzzleActivity;
import com.alex_podolian.npuzzle.view.custom_views.PuzzleBoardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class Presenter {

	private Model model;
	private int algorithm;
	private int heuristics;
	private int gameMode = 0;

	public Presenter(Model model) {
		this.model = model;
	}

	public int gameMode() {
		return gameMode;
	}

	public void selectGameMode(Context context) {
		String[] modes = context.getResources().getStringArray(R.array.game_mode);
		new AlertDialog.Builder(context, R.style.DialogTheme)
			.setSingleChoiceItems(modes, gameMode, (dialogInterface, position) -> {
				gameMode = position;
			})
			.setTitle("Select game mode")
			.setPositiveButton("Ok", (dialog, which) -> {

			})
			.show();
	}

	public void selectPuzzleSize(final Context context, final Class<?> cls, String option) {
		final TextInputEditText editText = new TextInputEditText(context);
		editText.setGravity(Gravity.CENTER);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

		final FrameLayout layout = new FrameLayout(context);
		layout.setPaddingRelative(200,15,200,0);
		layout.addView(editText);

		final AlertDialog alert = new AlertDialog.Builder(context, R.style.DialogTheme)
			.setView(layout)
			.setTitle("Enter desired puzzle size")
			.setPositiveButton("Continue", null)
			.create();

		alert.setOnShowListener(dialog -> {
			Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
			button.setOnClickListener(view -> {
				String text = Objects.requireNonNull(editText.getText()).toString();
				if (!Utils.isNullOrEmpty(text)) {
					int puzzleSize = Integer.parseInt(text);
					if (puzzleSize < 3) {
						Toast.makeText(context, "Minimum size must be 3", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(context, cls);
					intent.putExtra("puzzle_size", puzzleSize);
					intent.putExtra("text_size", Utils.calculateTextSize(puzzleSize));
					intent.putIntegerArrayListExtra("goal_map",
						gameMode == 0 ? Utils.makeSpiralMap(puzzleSize) : Utils.makeClassicMap(puzzleSize));
					if (option.equals("random")) {
						intent.putExtra("option", "random");
						intent.putIntegerArrayListExtra("start_map",
							gameMode == 0 ? Utils.makeSpiralMap(puzzleSize) : Utils.makeClassicMap(puzzleSize));
					}
					context.startActivity(intent);
					dialog.dismiss();
				} else {
					Toast.makeText(context, "Please enter size", Toast.LENGTH_SHORT).show();
				}
			});
		});
		alert.show();
	}

	public void selectHeuristic(ProgressDialog progressDialog, PuzzleBoardView puzzleBoardView,
	                            Context context, OnComplete callback) {
		algorithm = 0;
		heuristics = 0;
		String[] listAlgorithm = context.getResources().getStringArray(R.array.algorithm);
		new AlertDialog.Builder(context, R.style.DialogTheme)
			.setSingleChoiceItems(listAlgorithm, algorithm, (dialogInterface, position) -> {
				algorithm = position;
			})
			.setTitle("Select algorithm")
			.setPositiveButton("Continue", (dialog, which) -> {
				String[] listHeuristics = context.getResources().getStringArray(R.array.heuristics);
				new AlertDialog.Builder(context, R.style.DialogTheme)
				.setSingleChoiceItems(listHeuristics, heuristics, (dialogInterface, position) -> {
					heuristics = position;
				})
				.setTitle("Select heuristic")
				.setPositiveButton("Continue", (dialog1, which1) -> {
//					RLogs.w("ALGORITHM = " + algorithm + " HEURISTICS = "  + heuristics);
					progressDialog.show();
					puzzleBoardView.solve(callback, algorithm, heuristics);
				})
				.show();
			})
			.show();
	}

	public void loadPuzzleFromFile(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("text/plain");
		activity.startActivityForResult(intent, Config.READ_REQUEST_CODE);
	}

	public void createMap(int puzzleSize, OnCreateMap callback, Context context) {
		model.createMap(puzzleSize, callback, context);
	}

	public void createPuzzle(ArrayList<String> map, Context context) {
		ArrayList<Integer> validMap;
		validMap = Validator.getValidMap(map);
		if (validMap == null) {
			Toast.makeText(context, "Invalid map", Toast.LENGTH_SHORT).show();
		} else {
			int puzzleSize = (int) Math.sqrt(validMap.size());
			if (!Validator.isSolvable(puzzleSize, validMap,
				gameMode == 0 ? Utils.makeSpiralMap(puzzleSize) : Utils.makeClassicMap(puzzleSize))) {
				Toast.makeText(context, "Puzzle is unsolvable! Please edit map.", Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(context, PuzzleActivity.class);
				intent.putExtra("puzzle_size", puzzleSize);
				intent.putExtra("option", "new");
				intent.putExtra("text_size", Utils.calculateTextSize(puzzleSize));
				intent.putIntegerArrayListExtra("start_map", validMap);
				intent.putIntegerArrayListExtra("goal_map",
					gameMode == 0 ? Utils.makeSpiralMap(puzzleSize) : Utils.makeClassicMap(puzzleSize));
				context.startActivity(intent);
			}
		}
	}

	public void solvePuzzle(PuzzleBoard puzzleBoard, OnSolvePuzzle callback, int algorithm, int heuristics) {
		model.solvePuzzle(puzzleBoard, callback, algorithm, heuristics);
	}
}
