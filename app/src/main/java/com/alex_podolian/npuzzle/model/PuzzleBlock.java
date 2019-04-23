package com.alex_podolian.npuzzle.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.alex_podolian.npuzzle.R;

public class PuzzleBlock {

	private Context context;
	private int id;
	private float x;
	private float y;
	private float blockSize;
	private int textSize;
	private int strokeWidth = 3;

	public PuzzleBlock (Context context, int id, int x, int y, int blockSize, int textSize) {
		this.context = context;
		this.id = id;
		this.x = x;
		this.y = y;
		this.blockSize = blockSize;
		this.textSize = textSize;
	}

	public void onDraw(@NonNull Canvas canvas, @NonNull Paint paint) {
		if (id == 0) {
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(context.getResources().getColor(R.color.backgroundColor));
			canvas.drawRect(x, y, x + blockSize, y + blockSize, paint);
			return;
		}

		// fill
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(context.getResources().getColor(R.color.blockColor));
		canvas.drawRect(x, y, x + blockSize, y + blockSize, paint);

		// text
		paint.setTextSize(textSize * context.getResources().getDisplayMetrics().scaledDensity);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(context.getResources().getColor(R.color.primaryDarkColor));
		canvas.drawText(String.valueOf(id), x + blockSize / 2, y + blockSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);

		// border
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(context.getResources().getColor(R.color.backgroundColor));
		paint.setStrokeWidth(strokeWidth * context.getResources().getDisplayMetrics().scaledDensity);
		canvas.drawRect(x, y, x + blockSize, y + blockSize, paint);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
