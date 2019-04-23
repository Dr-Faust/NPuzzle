package com.alex_podolian.npuzzle.view;

import android.os.Bundle;
import android.view.View;

import com.alex_podolian.npuzzle.R;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	@BindView(R.id.btn_new)     MaterialButton btnNew;
	@BindView(R.id.btn_random)  MaterialButton btnRandom;
	@BindView(R.id.btn_load)    MaterialButton btnLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);
		btnNew.setOnClickListener(this);
		btnRandom.setOnClickListener(this);
		btnLoad.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_new:
				presenter.selectPuzzleSize(this, NewPuzzleActivity.class, "new");
				break;
			case R.id.btn_random:
				presenter.selectPuzzleSize(this, PuzzleActivity.class, "random");
				break;
			case R.id.btn_load:
				break;
		}
	}
}