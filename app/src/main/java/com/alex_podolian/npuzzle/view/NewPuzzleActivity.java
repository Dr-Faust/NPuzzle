package com.alex_podolian.npuzzle.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.app.Config;
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

	@BindView(R.id.gl_new_container)   GridLayout glNewContainer;
	@BindView(R.id.toolbar_new)     Toolbar toolbar;
	@BindView(R.id.btn_continue)    MaterialButton btnContinue;

	private ProgressDialog progressDialog;
	private int puzzleSize;
	private LinearLayout[][] llMatrix;
	private int textSize;
	private ArrayList<Integer> goalMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_puzzle);

		ButterKnife.bind(this);

		toolbar.setNavigationOnClickListener(v -> onBackPressed());
		btnContinue.setOnClickListener(this);
		puzzleSize = getIntent().getIntExtra("puzzle_size", 3);
		textSize = getIntent().getIntExtra("text_size", 24);
		goalMap = getIntent().getIntegerArrayListExtra("goal_map");

		initMap(puzzleSize);
	}

	private void initMap(int puzzleSize) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Creating map...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		presenter.createMap(puzzleSize,this, this);

		glNewContainer.setRowCount(puzzleSize);
		glNewContainer.setColumnCount(puzzleSize);
//		glNewContainer.setAlignmentMode(GridLayout.ALIGN_MARGINS);
		glNewContainer.setRowOrderPreserved(false);
	}

	@Override
	public void onClick(View view) {
		ArrayList<Integer> map = new ArrayList<>();
		int index = 0;
		int id = Config.START_ID;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				TextInputLayout tilBox = llMatrix[i][j].findViewById(id);
				String value = Objects.requireNonNull(tilBox.getEditText()).getText().toString();
				id++;
				if (!Utils.isNullOrEmpty(value)) {
					map.add(Integer.parseInt(value));
					if (map.get(index) >= puzzleSize * puzzleSize) {
						Toast.makeText(this, "Values must be from 0 to " +
							(puzzleSize * puzzleSize - 1) + ".", Toast.LENGTH_SHORT).show();
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
		if (Validator.isSolvable(puzzleSize, map,
			presenter.gameMode() == 0 ? Utils.makeSpiralMap(puzzleSize) : Utils.makeClassicMap(puzzleSize))) {
			Intent intent = new Intent(this, PuzzleActivity.class);
			intent.putExtra("option", "new");
			intent.putExtra("puzzle_size", puzzleSize);
			intent.putExtra("text_size", textSize);
			intent.putIntegerArrayListExtra("start_map", map);
			intent.putIntegerArrayListExtra("goal_map", goalMap);
			startActivity(intent);
		} else {
			Toast.makeText(this, "Puzzle is unsolvable! Please edit map.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onMapCreated(LinearLayout[][] llMatrix) {
		this.llMatrix = llMatrix;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				glNewContainer.addView(llMatrix[i][j]);
			}
		}
		progressDialog.dismiss();
	}
}
