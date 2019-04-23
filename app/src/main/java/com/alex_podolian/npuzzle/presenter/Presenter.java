package com.alex_podolian.npuzzle.presenter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.model.Model;
import com.alex_podolian.npuzzle.model.callbacks.OnCreateMap;
import com.alex_podolian.npuzzle.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Presenter {

	private Model model;

	public Presenter(Model model) {
		this.model = model;
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
					if (option.equals("random")) {
						intent.putExtra("option", "random");
						intent.putExtra("text_size", Utils.calculateTextSize(puzzleSize));
						intent.putIntegerArrayListExtra("map", Utils.makeSpiralMap(puzzleSize));
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

	public void createMap(int puzzleSize, OnCreateMap callback, Context context) {
		model.createMap(puzzleSize, callback, context);
	}
}
