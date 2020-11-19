package com.alex_podolian.npuzzle.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.alex_podolian.npuzzle.R;

public class PuzzleBlock {

    private Context context;
    private int id;
    private float x;
    private float y;
    private float blockSize;
    private int textSize;
    private int strokeWidth = 5;

    public PuzzleBlock(Context context, int id, int x, int y, int blockSize, int textSize) {
        this.context = context;
        this.id = id;
        this.x = x;
        this.y = y;
        this.blockSize = blockSize;
        this.textSize = textSize;
    }

    public void onDraw(@NonNull Canvas canvas, @NonNull Paint paint) {
//		RLogs.w("ON DRAW!");

        if (id == 0) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(context.getResources().getColor(R.color.backgroundColor));
//			paint.setColor(Color.TRANSPARENT);
            paint.setStrokeWidth(strokeWidth * context.getResources().getDisplayMetrics().scaledDensity);
            canvas.drawRect(x, y, x + blockSize, y + blockSize, paint);
            return;
        }

        // fill
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getResources().getColor(R.color.blockColor));
        paint.setStrokeWidth(strokeWidth * context.getResources().getDisplayMetrics().scaledDensity);
        canvas.drawRoundRect(new RectF(x, y, x + blockSize, y + blockSize), 20, 20, paint);
//		canvas.drawRect(x, y, x + blockSize, y + blockSize, paint);

        // text
        paint.setTextSize(textSize * context.getResources().getDisplayMetrics().scaledDensity);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(context.getResources().getColor(R.color.primaryDarkColor));
        canvas.drawText(String.valueOf(id), x + blockSize / 2, y + blockSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);

        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(context.getResources().getColor(R.color.backgroundColor));
//		paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(strokeWidth * context.getResources().getDisplayMetrics().scaledDensity);
        canvas.drawRect(x, y, x + blockSize, y + blockSize, paint);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
