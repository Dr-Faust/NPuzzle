package com.alex_podolian.npuzzle.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.databinding.ActivityPuzzleBinding;
import com.alex_podolian.npuzzle.model.PuzzleBoard;
import com.alex_podolian.npuzzle.model.callbacks.OnComplete;
import com.alex_podolian.npuzzle.utils.Utils;
import com.alex_podolian.npuzzle.utils.Validator;
import com.alex_podolian.npuzzle.view.custom_views.PuzzleBoardView;

import java.util.ArrayList;

public class PuzzleActivity extends BaseActivity implements View.OnClickListener, OnComplete {

    private ActivityPuzzleBinding binding;
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
        binding = ActivityPuzzleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnShowSteps.setVisibility(View.GONE);
        binding.tvSteps.setVisibility(View.GONE);
        binding.tvComplexityInTime.setVisibility(View.GONE);
        binding.tvComplexityInSize.setVisibility(View.GONE);
        binding.toolbarBoard.setNavigationOnClickListener(v -> onBackPressed());
        String option = getIntent().getStringExtra("option");

        if (option.equals("new")) {
            binding.btnReset.setOnClickListener(this);
            binding.btnShuffle.setVisibility(View.GONE);
        } else if (option.equals("random")) {
            binding.btnShuffle.setOnClickListener(this);
            binding.btnReset.setVisibility(View.GONE);
        }

        binding.btnSolve.setOnClickListener(this);
        binding.btnShowSteps.setOnClickListener(this);

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
        binding.clPuzzleContainer.addView(puzzleBoardView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_shuffle:
                binding.btnShowSteps.setVisibility(View.GONE);
                binding.tvSteps.setVisibility(View.GONE);
                binding.tvComplexityInTime.setVisibility(View.GONE);
                binding.tvComplexityInSize.setVisibility(View.GONE);
                puzzleBoardView.shuffleBoard();
                puzzleBoardView.invalidate();
                break;
            case R.id.btn_reset:
                binding.btnShowSteps.setVisibility(View.GONE);
                binding.tvSteps.setVisibility(View.GONE);
                binding.tvComplexityInTime.setVisibility(View.GONE);
                binding.tvComplexityInSize.setVisibility(View.GONE);
                puzzleBoardView.initView();
                puzzleBoardView.invalidate();
                break;
            case R.id.btn_solve:
                if (Validator.isSolvable(puzzleSize, startMap, goalMap)) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Solving...");
                    progressDialog.setCancelable(false);
                    presenter.selectHeuristic(progressDialog, puzzleBoardView, this, this);
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
            binding.tvSteps.setText("Total steps: " + steps.size());
            binding.tvComplexityInTime.setText("Complexity in time: " + complexityInTime);
            binding.tvComplexityInSize.setText("Complexity in size: " + complexityInSize);
            binding.btnShowSteps.setVisibility(View.VISIBLE);
            binding.tvSteps.setVisibility(View.VISIBLE);
            binding.tvComplexityInTime.setVisibility(View.VISIBLE);
            binding.tvComplexityInSize.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();
    }
}
