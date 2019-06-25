package com.alex_podolian.npuzzle.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.app.Config;
import com.alex_podolian.npuzzle.utils.Utils;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	@BindView(R.id.toolbar_main)    Toolbar toolbar;
	@BindView(R.id.btn_new)         MaterialButton btnNew;
	@BindView(R.id.btn_random)      MaterialButton btnRandom;
	@BindView(R.id.btn_load)        MaterialButton btnLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

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
				presenter.loadPuzzleFromFile(this);
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);
		if (requestCode == Config.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if (resultData != null) {
				Uri uri = resultData.getData();
				try {
					ArrayList<String> map = Utils.mapFromFile(uri, this);
					presenter.createPuzzle(map, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_game_mode:
				presenter.selectGameMode(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}