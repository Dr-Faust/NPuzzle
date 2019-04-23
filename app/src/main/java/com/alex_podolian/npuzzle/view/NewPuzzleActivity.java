package com.alex_podolian.npuzzle.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.model.callbacks.OnCreateMap;
import com.alex_podolian.npuzzle.utils.Utils;
import com.alex_podolian.npuzzle.utils.Validator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPuzzleActivity extends BaseActivity implements View.OnClickListener, OnCreateMap {

	@BindView(R.id.new_container)   GridLayout newContainer;
	@BindView(R.id.toolbar_new)     Toolbar toolbar;
	@BindView(R.id.btn_continue)    MaterialButton btnContinue;

	private ProgressDialog progressDialog;
	private int puzzleSize;
	private TextInputLayout[][] tilMatrix;
	private int textSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_puzzle);

		ButterKnife.bind(this);

		toolbar.setNavigationOnClickListener(v -> onBackPressed());
		btnContinue.setOnClickListener(this);
		puzzleSize = getIntent().getIntExtra("puzzle_size", 3);

		initMap(puzzleSize);
	}

	private void initMap(int puzzleSize) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Creating map...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		presenter.createMap(puzzleSize,this, this);

		newContainer.setRowCount(puzzleSize);
		newContainer.setColumnCount(puzzleSize);
		newContainer.setAlignmentMode(GridLayout.ALIGN_MARGINS);
		newContainer.setRowOrderPreserved(false);
	}

	@Override
	public void onClick(View view) {
		ArrayList<Integer> map = new ArrayList<>();
		int index = 0;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				String value = Objects.requireNonNull(tilMatrix[i][j].getEditText()).getText().toString();
				if (!Utils.isNullOrEmpty(value)) {
					map.add(Integer.parseInt(value));
					if (map.get(index) >= puzzleSize * puzzleSize) {
						Toast.makeText(this, "Values must be from 0 to " + (puzzleSize * puzzleSize - 1) + ".", Toast.LENGTH_SHORT).show();
						return;
					}
					index++;
				} else {
					Toast.makeText(this, "Please fill in all boxes.", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		int count;
		for(int i = 0; i < puzzleSize * puzzleSize; i++) {
			count = 0;
			for(Integer node : map) {
				if (i == node) {
					count++;
				}
				if (count > 1) {
					Toast.makeText(this, "Values should not be repeated.", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		if (Validator.isSolvable(puzzleSize, map, Utils.makeSpiralMap(puzzleSize))) {
			Intent intent = new Intent(this, PuzzleActivity.class);
			intent.putExtra("option", "new");
			intent.putExtra("puzzle_size", puzzleSize);
			intent.putExtra("text_size", textSize);
			intent.putIntegerArrayListExtra("map", map);
			startActivity(intent);
		} else {
			Toast.makeText(this, "Puzzle is unsolvable! Please edit map.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onMapCreated(TextInputLayout[][] tilMatrix, int textSize) {
		this.tilMatrix = tilMatrix;
		this.textSize = textSize;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				newContainer.addView(tilMatrix[i][j]);
			}
		}
		progressDialog.dismiss();
	}
}
