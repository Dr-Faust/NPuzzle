package com.alex_podolian.npuzzle.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.app.Config;
import com.alex_podolian.npuzzle.model.callbacks.OnCreateMap;
import com.alex_podolian.npuzzle.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateMapTask extends AsyncTask<Void, Void, LinearLayout[][]> {

	@SuppressLint("StaticFieldLeak")
	private Context context;
	private final int puzzleSize;
	private final OnCreateMap callback;

	public CreateMapTask(int puzzleSize, OnCreateMap callback, Context context) {
		this.callback = callback;
		this.puzzleSize = puzzleSize;
		this.context = context;
	}

	@Override
	protected LinearLayout[][] doInBackground(Void... params) {
		LinearLayout[][] matrix = new LinearLayout[puzzleSize][puzzleSize];
		int id = Config.START_ID;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				matrix[i][j] = createTilBox(puzzleSize, id);
				id++;
			}
		}
		return matrix;
	}

	@Override
	protected void onPostExecute(LinearLayout[][] matrix) {
		super.onPostExecute(matrix);
		if (callback != null) {
			callback.onMapCreated(matrix);
		}
	}

	private LinearLayout createTilBox(int puzzleSize, int id) {
		int textSize = Utils.calculateTextSize(puzzleSize);

		int textLength = 1;
		if (puzzleSize > 3 && puzzleSize < 11) {
			textLength = 2;
		} else if (puzzleSize > 10) {
			textLength = 3;
		}

		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int pxWidth = displayMetrics.widthPixels;
		int boxSize = pxWidth / (puzzleSize + 1);

		View view = LayoutInflater.from(context).inflate(R.layout.text_input_layout, null);
		LinearLayout llBox = view.findViewById(R.id.ll_box);

		TextInputLayout tilBox = view.findViewById(R.id.til_box);
		tilBox.setHintEnabled(false);
		tilBox.setId(id);

		TextInputEditText etBox = view.findViewById(R.id.et_box);
		etBox.setGravity(Gravity.CENTER);
		etBox.setTextSize(textSize);
		etBox.setInputType(InputType.TYPE_CLASS_NUMBER);
		etBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textLength)});
		etBox.setPadding(0,0,0,0);

		ViewGroup.LayoutParams params = etBox.getLayoutParams();
		params.width = boxSize;
		params.height = boxSize;
		etBox.setLayoutParams(params);

		return llBox;
	}
}
