package com.alex_podolian.npuzzle.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.model.PuzzleBlock;
import com.alex_podolian.npuzzle.utils.RLogs;
import com.alex_podolian.npuzzle.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

@SuppressLint("ViewConstructor")
public class PuzzleBoardView extends View {

	private Context context;
	private int puzzleSize;
	private int textSize;
	private int blockSize = 0;
	private int[][] startMatrix;
	private int[][] goalMatrix;
	private PuzzleBlock[][] puzzleMatrix;
	private Paint paint;
	private Point emptyBlockIndex;
	private int containerWidth = 0;

	public PuzzleBoardView(Context context, int puzzleSize, int textSize, int[][] startMatrix) {
		super(context);
		this.context = context;
		this.puzzleSize = puzzleSize;
		this.textSize = textSize;
		this.startMatrix = startMatrix;
		this.goalMatrix = Utils.makeSpiralMatrix(puzzleSize);
		paint = new Paint();
		paint.setAntiAlias(true);
		initEmptyBlockIndex();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				puzzleMatrix[i][j].onDraw(canvas, paint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		containerWidth = getMeasuredWidth();
		if (containerWidth == 0) {
			return;
		}
		initPuzzleBoard();
	}

	private void initEmptyBlockIndex() {
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				if (startMatrix[i][j] == 0) {
					emptyBlockIndex = new Point(i, j);
				}
			}
		}
	}

	private void initPuzzleBoard() {
		puzzleMatrix = new PuzzleBlock[puzzleSize][puzzleSize];
		int x = 0;
		int y = 0;
		blockSize = containerWidth / puzzleSize;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				puzzleMatrix[i][j] = new PuzzleBlock(context, startMatrix[i][j], x, y, blockSize, textSize);
				x += blockSize;
			}
			x = 0;
			y += blockSize;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event != null) {
			switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					RLogs.d("Event down: " + event.getX() + ":" + event.getY());
					return true;
				case MotionEvent.ACTION_UP:
					if (blockSize == 0) {
						return false;
					}
					int i = (int) (event.getY() / blockSize);
					int j = (int) (event.getX() / blockSize);

					if (i + 1 < puzzleSize && i + 1 == emptyBlockIndex.x && j == emptyBlockIndex.y) {
						makeMove(i, j);
					} else if (i - 1 >= 0 && i - 1 == emptyBlockIndex.x && j == emptyBlockIndex.y) {
						makeMove(i, j);
					} else if (j + 1 < puzzleSize && i == emptyBlockIndex.x && j + 1 == emptyBlockIndex.y) {
						makeMove(i, j);
					} else if (j - 1 >= 0 && i == emptyBlockIndex.x && j - 1 == emptyBlockIndex.y) {
						makeMove(i, j);
					}
					RLogs.d("Event up: " + event.getX() + ":" + event.getY());
			}

		}

		return super.onTouchEvent(event);
	}

	private void makeMove(int i, int j) {
		swapBlock(i, j);
		invalidate();
		if (isSolution()) {
			new AlertDialog.Builder(context, R.style.DialogTheme)
				.setTitle("Congratulations!")
				.setCancelable(false)
				.setMessage("You made it!")
				.setPositiveButton("Continue", (dialog, which) -> {
					Intent intent = new Intent(context, MainActivity.class);
					context.startActivity(intent);
					invalidate();
					dialog.dismiss();
				})
				.show();
		}
	}

	private void swapBlock(int i, int j) {
		int id = puzzleMatrix[i][j].getId();
		puzzleMatrix[i][j].setId(0);
		puzzleMatrix[emptyBlockIndex.x][emptyBlockIndex.y].setId(id);
		emptyBlockIndex = new Point(i, j);
	}

	private boolean isSolution() {
		int count = 1;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				if (puzzleMatrix[i][j].getId() != count && count != puzzleSize * puzzleSize) {
					return false;
				}
				count++;
			}
		}
		return true;
	}

	public void shuffleBoard() {
		for (int i = 0; i < 100; i++) {
			ArrayList<Point> options = new ArrayList<>();
			if (emptyBlockIndex.x + 1 < puzzleSize) {
				options.add(new Point(emptyBlockIndex.x + 1, emptyBlockIndex.y));
			}
			if (emptyBlockIndex.x - 1 >= 0) {
				options.add(new Point(emptyBlockIndex.x - 1, emptyBlockIndex.y));
			}
			if (emptyBlockIndex.y + 1 < puzzleSize) {
				options.add(new Point(emptyBlockIndex.x, emptyBlockIndex.y + 1));
			}
			if (emptyBlockIndex.y - 1 >= 0) {
				options.add(new Point(emptyBlockIndex.x, emptyBlockIndex.y - 1));
			}
			Collections.shuffle(options);
			Point selectedPoint = options.get(0);
			swapBlock(selectedPoint.x, selectedPoint.y);
		}
	}

	public int hamming() {
		int count = 0;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				if (startMatrix[i][j] != 0 && startMatrix[i][j] != goalMatrix[i][j]) {
					count++;
				}
			}
		}
		return count;
	}

	public int manhattan() {
		int count = 0;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				if (startMatrix[i][j] != 0 && startMatrix[i][j] != goalMatrix[i][j]) {
					int dx = (goalMatrix[i][j] - 1) / puzzleSize;
					int dy = goalMatrix[i][j] - 1 - dx * puzzleSize;
					count += Math.abs(i - dx) + Math.abs(j - dy);
				}
			}
		}
		return count;
	}
}
