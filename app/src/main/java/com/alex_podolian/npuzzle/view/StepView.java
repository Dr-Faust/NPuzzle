package com.alex_podolian.npuzzle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.alex_podolian.npuzzle.model.PuzzleBlock;

public class StepView extends View {

	private Context context;
	private int puzzleSize;
	private int textSize;
	private int blockSize = 0;
	private int[][] matrix;
	private PuzzleBlock[][] puzzleMatrix;
	private Paint paint;
	private Point emptyBlockIndex;
	private int containerWidth = 0;

	public StepView(Context context, int puzzleSize, int textSize, int[][] matrix) {
		super(context);
		this.context = context;
		this.puzzleSize = puzzleSize;
		this.textSize = textSize;
		this.matrix = matrix;
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
		initStepBoard();
	}

	private void initEmptyBlockIndex() {
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				if (matrix[i][j] == 0) {
					emptyBlockIndex = new Point(i, j);
				}
			}
		}
	}

	private void initStepBoard() {
		puzzleMatrix = new PuzzleBlock[puzzleSize][puzzleSize];
		int x = 0;
		int y = 0;
		blockSize = containerWidth / puzzleSize;
		for (int i = 0; i < puzzleSize; i++) {
			for (int j = 0; j < puzzleSize; j++) {
				puzzleMatrix[i][j] = new PuzzleBlock(context, matrix[i][j], x, y, blockSize, textSize);
				x += blockSize;
			}
			x = 0;
			y += blockSize;
		}
	}
}
