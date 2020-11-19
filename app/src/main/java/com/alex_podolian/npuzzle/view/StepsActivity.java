package com.alex_podolian.npuzzle.view;

import android.os.Bundle;
import android.view.View;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.adapter.StepsPagerAdapter;
import com.alex_podolian.npuzzle.databinding.ActivityStepsBinding;
import com.alex_podolian.npuzzle.utils.RLogs;
import com.alex_podolian.npuzzle.utils.ViewPagerPageSelectedListener;

import java.util.ArrayList;

public class StepsActivity extends BaseActivity implements View.OnClickListener {

    private ActivityStepsBinding binding;
    ArrayList<ArrayList<Integer>> listMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarSteps.setNavigationOnClickListener(v -> onBackPressed());
        binding.btnToFirst.setOnClickListener(this);
        binding.btnToLast.setOnClickListener(this);

        ArrayList<Integer> listMapsIntegers = getIntent().getIntegerArrayListExtra("list_maps");
        int textSize = getIntent().getIntExtra("text_size", 24);
        int puzzleSize = getIntent().getIntExtra("puzzle_size", 3);

        listMaps = new ArrayList<>();
        for (int index = 0; index < listMapsIntegers.size(); ) {
            ArrayList<Integer> map = new ArrayList<>();
            for (int i = 0; i < puzzleSize * puzzleSize; i++) {
                map.add(listMapsIntegers.get(index++));
            }
            listMaps.add(map);
        }

        binding.stepsDots.setPagesCount(listMaps.size() - 1);
        binding.viewpagerSteps.setAdapter(new StepsPagerAdapter(this, listMaps, puzzleSize, textSize));
        binding.viewpagerSteps.addOnPageChangeListener(new ViewPagerPageSelectedListener() {
            @Override
            public void onPageSelected(int pagePosition) {
                binding.stepsDots.setCurrentPageNumber(pagePosition);
            }
        });
        RLogs.w("CURR ITEM = " + binding.viewpagerSteps.getCurrentItem());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_to_first:
                binding.viewpagerSteps.setCurrentItem(0);
                break;
            case R.id.btn_to_last:
                binding.viewpagerSteps.setCurrentItem(listMaps.size() - 1);
                break;
        }
    }
}
