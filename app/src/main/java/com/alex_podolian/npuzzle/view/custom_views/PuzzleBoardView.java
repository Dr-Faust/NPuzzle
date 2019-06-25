package com.alex_podolian.npuzzle.view.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.alex_podolian.npuzzle.model.PuzzleBoard;
import com.alex_podolian.npuzzle.model.callbacks.OnComplete;
import com.alex_podolian.npuzzle.model.callbacks.OnSolvePuzzle;
import com.alex_podolian.npuzzle.presenter.Presenter;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class PuzzleBoardView extends View implements OnSolvePuzzle {

	private Context context;
	private Presenter presenter;
	private int puzzleSize;
	private int textSize;
	private int blockSize = 0;
	private ArrayList<Integer> startMap;
	private ArrayList<Integer> goalMap;
	private Paint paint;
	private PuzzleBoard puzzleBoard;
	private ArrayList<PuzzleBoard> animation;
	private OnComplete callback;
	private boolean isInPuzzleMode;

	public PuzzleBoardView(Context context, int puzzleSize, int textSize, ArrayList<Integer> startMap,
	                       ArrayList<Integer> goalMap, Presenter presenter, boolean isInPuzzleMode) {
		super(context);
		this.context = context;
		this.presenter = presenter;
		this.puzzleSize = puzzleSize;
		this.textSize = textSize;
		this.startMap = startMap;
		this.goalMap = goalMap;
		this.isInPuzzleMode = isInPuzzleMode;
		animation = null;
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (puzzleBoard != null) {
			if (animation != null && animation.size() > 0) {
				puzzleBoard = animation.remove(0);
				puzzleBoard.onDraw(canvas, paint);
				if (animation.size() == 0) {
					animation = null;
					Toast.makeText(context, "Solved!", Toast.LENGTH_LONG).show();
				} else {
					this.postInvalidateDelayed(300);
				}
			} else {
				puzzleBoard.onDraw(canvas, paint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initView();
	}

	public void initView() {
		int containerWidth = getMeasuredWidth();
		if (containerWidth == 0) {
			return;
		}
		blockSize = containerWidth / puzzleSize;
		if (animation == null || animation.size() < 1) {
			puzzleBoard = new PuzzleBoard(context, puzzleSize, textSize, startMap, goalMap, blockSize);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isInPuzzleMode) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					return true;
				case MotionEvent.ACTION_UP:
					if (blockSize == 0) {
						return false;
					}
					int j = (int) (event.getY() / blockSize);
					int i = (int) (event.getX() / blockSize);
					if (i < puzzleSize && j < puzzleSize) {
						puzzleBoard.onTouch(i, j);
						invalidate();
					}
			}
		}
		return super.onTouchEvent(event);
	}

	public void shuffleBoard() {
		if (animation != null && animation.size() > 0) {
			Toast.makeText(context, "Come on!! it's in solving process!", Toast.LENGTH_LONG).show();
			return;
		}
		if (animation == null) {
			puzzleBoard.shuffleBoard();
		}
	}

	public void solve(OnComplete callback, int algorithm, int heuristics) {
		if (animation != null && animation.size() > 0 || puzzleBoard == null) {
			Toast.makeText(context, "Wait! It's already in process.", Toast.LENGTH_LONG).show();
			callback.onCompleted(null, 0, 0);
			return;
		}
		if (puzzleBoard.isGoal()){
			Toast.makeText(context, "It's already solved! Shuffle it!", Toast.LENGTH_LONG).show();
			callback.onCompleted(null, 0, 0);
			return;
		}
		this.callback = callback;
		presenter.solvePuzzle(puzzleBoard, this, algorithm, heuristics);
	}

	@Override
	public void onPuzzleSolved(ArrayList<PuzzleBoard> steps, long complexityInTime, long complexityInSize) {
		animation = steps;
		invalidate();
		callback.onCompleted(steps, complexityInTime, complexityInSize);
	}
}
