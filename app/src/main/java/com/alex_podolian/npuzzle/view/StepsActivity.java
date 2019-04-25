package com.alex_podolian.npuzzle.view;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.adapter.StepsPagerAdapter;
import com.alex_podolian.npuzzle.utils.RLogs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsActivity extends BaseActivity implements View.OnClickListener {

	@BindView(R.id.toolbar_steps)       Toolbar toolbar;
	@BindView(R.id.viewpager_steps)     ViewPager viewPager;
	@BindView(R.id.btn_to_first)        AppCompatImageButton btnToFirst;
	@BindView(R.id.btn_to_last)         AppCompatImageButton btnToLast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steps);

		ButterKnife.bind(this);
		toolbar.setNavigationOnClickListener(v -> onBackPressed());

		btnToFirst.setOnClickListener(this);
		btnToLast.setOnClickListener(this);

		ArrayList<Integer> listMapsIntegers = getIntent().getIntegerArrayListExtra("list_maps");
		int textSize = getIntent().getIntExtra("text_size", 24);
		int puzzleSize = getIntent().getIntExtra("puzzle_size", 3);

		ArrayList<ArrayList<Integer>> listMaps = new ArrayList<>();
		for (int index = 0; index < listMapsIntegers.size(); ) {
			ArrayList<Integer> map = new ArrayList<>();
			for (int i = 0; i < puzzleSize * puzzleSize; i++) {
				map.add(listMapsIntegers.get(index++));
			}
			listMaps.add(map);
		}

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

//		ViewPager viewPager = findViewById(R.id.viewpager_steps);
		viewPager.setAdapter(new StepsPagerAdapter(this, listMaps, puzzleSize, textSize, displaymetrics.widthPixels));
		RLogs.w("CURR ITEM = " + viewPager.getCurrentItem());

//		AppCompatImageButton btnToFirst = findViewById(R.id.btn_to_first);
//		AppCompatImageButton btnToLast = findViewById(R.id.btn_to_last);

//		btnToFirst.setOnClickListener(this);
//		btnToLast.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_to_first:
				break;
			case R.id.btn_to_last:
				break;
		}
	}
}
