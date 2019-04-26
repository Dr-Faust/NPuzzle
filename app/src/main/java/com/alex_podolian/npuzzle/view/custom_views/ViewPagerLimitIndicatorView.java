package com.alex_podolian.npuzzle.view.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.alex_podolian.npuzzle.R;


public class ViewPagerLimitIndicatorView extends View {
    private static final int RE_DRAW = 1;
    private static final int ANIMATE_DELAY = 8;
    private static final float DOTS_RATIO = 0.65f;
    private ViewPagerIndicatorDotsColor currentDotsColor;
    private Paint paintActiveDot = new Paint();
    private Paint paintDot = new Paint();

    private float currentDotSideSize;
    private float mediumDotSideSize;
    private float smallDotSideSize;
    private float dotPadding;
    private float dotSizeDifOnePercent;
    private float dotPaddingDifOnePercent;

    private float currAnimateFlag = 0f;
    private final float ONE_ANIMATE_STEP = 8f;
    private final float ANIMATE_PAGE_STEP = 100;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RE_DRAW) {
                invalidate();
            }
        }
    };

    private int currentPageNumber = 0;
    private int pagesCount = 0;

    private float centerX;
    private float centerY;
    private AnimateStepData animateStepData;

    public enum ViewPagerIndicatorDotsColor {
        WHITE,
        GRAY,
        GRAY_AND_GREEN,
        WHITE_AND_GREEN
    }

    public ViewPagerLimitIndicatorView(Context context) {
        super(context);
        init();
    }

    public ViewPagerLimitIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPagerLimitIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
        invalidate();
    }

    public void setCurrentPageNumber(int currPage) {
        if (currPage <= pagesCount) {
            if (currPage - currentPageNumber == 1) {
                currAnimateFlag = ANIMATE_PAGE_STEP;
            }
            if (currPage - currentPageNumber == -1) {
                currAnimateFlag = ANIMATE_PAGE_STEP * -1;
            }
            currentPageNumber = currPage;
            handler.removeCallbacksAndMessages(null);
            invalidate();
        } else {
            throw new RuntimeException("The currentPage value cannot be bigger than total countPages!");
        }
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    private void init() {
        currentDotSideSize = getResources().getDimensionPixelSize(R.dimen.active_indicator_side_size);
        mediumDotSideSize = currentDotSideSize * DOTS_RATIO;
        smallDotSideSize = mediumDotSideSize * DOTS_RATIO;
        dotPadding = currentDotSideSize * 2.5f;
        dotSizeDifOnePercent = (currentDotSideSize - mediumDotSideSize) / 100;
        dotPaddingDifOnePercent = dotPadding / 100;

        currentDotsColor = ViewPagerIndicatorDotsColor.GRAY_AND_GREEN;
        setColorsByCurrentTheme();
        paintActiveDot.setStyle(Paint.Style.FILL);
        paintActiveDot.setAntiAlias(true);
        paintDot.setStyle(Paint.Style.FILL);
        paintDot.setAntiAlias(true);

        animateStepData = new AnimateStepData();
    }

    public void setDotsColor(@NonNull ViewPagerIndicatorDotsColor color) {
        currentDotsColor = color;
        setColorsByCurrentTheme();
        invalidate();
    }

    private void setColorsByCurrentTheme() {
        switch (currentDotsColor) {
            case GRAY_AND_GREEN:
                setForGrayAndGreenDotsColor();
                break;
            case GRAY:
                setForGrayDotsColor();
                break;
            case WHITE:
                setForWhiteDotsColor();
                break;
            case WHITE_AND_GREEN:
                setForWhiteAndGreenDotsColor();
                break;
        }
    }

    private void setForWhiteDotsColor() {
        paintActiveDot.setColor(Color.WHITE);
        paintActiveDot.setAlpha(255);
        paintDot.setColor(Color.WHITE);
        paintDot.setAlpha(150);
    }

    private void setForWhiteAndGreenDotsColor() {
        paintActiveDot.setAlpha(255);
        paintActiveDot.setColor(getResources().getColor(R.color.primaryLightColor));
        paintDot.setColor(Color.WHITE);
        paintDot.setAlpha(150);
    }

    private void setForGrayDotsColor() {
        paintActiveDot.setColor(Color.GRAY);
        paintActiveDot.setAlpha(255);
        paintDot.setColor(Color.GRAY);
        paintDot.setAlpha(150);
    }

    private void setForGrayAndGreenDotsColor() {
        paintActiveDot.setColor(getResources().getColor(R.color.primaryLightColor));
        paintActiveDot.setAlpha(255);
        paintDot.setColor(Color.GRAY);
        paintDot.setAlpha(150);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (pagesCount == 0) return;
        calculateCurrAnimateStep();
        scheduleNextAnimateFrameIfNeed();

        animateStepData.isAnimateRun = currAnimateFlag != 0;
        if (animateStepData.isAnimateRun) {
            animateStepData.isTurnPageToForward = currAnimateFlag > 0f;
            animateStepData.dotsAnimateSizeDifference = dotSizeDifOnePercent * (ANIMATE_PAGE_STEP - Math.abs(currAnimateFlag));
            animateStepData.dotsXOffsetDifference = (dotPaddingDifOnePercent * (ANIMATE_PAGE_STEP - Math.abs(currAnimateFlag)));
        }

        // draw dots
        drawActiveDot(canvas, animateStepData);
        drawLeftDots(canvas, animateStepData);
        drawRightDots(canvas, animateStepData);
    }

    private void calculateCurrAnimateStep() {
        if (Math.abs(currAnimateFlag) < ONE_ANIMATE_STEP) {
            currAnimateFlag = 0f;
        }

        if (currAnimateFlag != 0f) {
            if (currAnimateFlag < 0) {
                currAnimateFlag += ONE_ANIMATE_STEP;
            } else {
                currAnimateFlag -= ONE_ANIMATE_STEP;
            }
        }
    }

    private void scheduleNextAnimateFrameIfNeed() {
        if (currAnimateFlag != 0f) {
            handler.sendMessageDelayed(handler.obtainMessage(RE_DRAW), ANIMATE_DELAY);
        }
    }

    private void drawActiveDot(@NonNull Canvas canvas, @NonNull AnimateStepData stepData) {
        // def
        if (!stepData.isAnimateRun) {
            canvas.drawCircle(centerX, centerY, currentDotSideSize, paintActiveDot);
            return;
        }

        // animate
        float radius = mediumDotSideSize + stepData.dotsAnimateSizeDifference;
        if (stepData.isTurnPageToForward) {
            canvas.drawCircle((centerX - stepData.dotsXOffsetDifference) + dotPadding, centerY, radius, paintActiveDot);
        } else {
            canvas.drawCircle((centerX + stepData.dotsXOffsetDifference) - dotPadding, centerY, radius, paintActiveDot);
        }
    }

    private void drawLeftDots(@NonNull Canvas canvas, @NonNull AnimateStepData stepData) {
        // draw 1st left dot
        if (currentPageNumber > 0) {
            float radius = mediumDotSideSize;
            if (stepData.isAnimateRun) { // animate
                radius = calculateRadiusForMediumDot(stepData, true);

                if (stepData.isTurnPageToForward) {
                    canvas.drawCircle((centerX - stepData.dotsXOffsetDifference), centerY, radius, paintDot);
                } else {
                    canvas.drawCircle((centerX + stepData.dotsXOffsetDifference) - dotPadding * 2, centerY, radius, paintDot);
                }
            } else { // def
                canvas.drawCircle((centerX - dotPadding), centerY, radius, paintDot);
            }
        }

        // draw 2nd left dot
        if (currentPageNumber > 1) {
            float radius = smallDotSideSize;
            if (stepData.isAnimateRun) { // animate
                radius = calculateRadiusForSmallDot(stepData, true);

                if (stepData.isTurnPageToForward) {
                    canvas.drawCircle((centerX - stepData.dotsXOffsetDifference) - dotPadding, centerY, radius, paintDot);
                } else {
                    canvas.drawCircle((centerX + stepData.dotsXOffsetDifference) - dotPadding * 3, centerY, radius, paintDot);
                }
            } else { // def
                canvas.drawCircle((centerX - dotPadding) - dotPadding, centerY, radius, paintDot);
            }
        }
    }

    private void drawRightDots(@NonNull Canvas canvas, @NonNull AnimateStepData stepData) {
        // draw 1st right dot
        if (currentPageNumber < pagesCount) {
            float radius = mediumDotSideSize;
            if (stepData.isAnimateRun) { // animate
                radius = calculateRadiusForMediumDot(stepData, false);

                if (stepData.isTurnPageToForward) {
                    canvas.drawCircle((centerX - stepData.dotsXOffsetDifference) + dotPadding * 2, centerY, radius, paintDot);
                } else {
                    canvas.drawCircle((centerX + stepData.dotsXOffsetDifference), centerY, radius, paintDot);
                }
            } else { // def
                canvas.drawCircle((centerX + dotPadding), centerY, radius, paintDot);
            }
        }

        // draw 2nd right dot
        if (currentPageNumber < (pagesCount - 1)) {
            float radius = smallDotSideSize;
            if (stepData.isAnimateRun) { // animate
                radius = calculateRadiusForSmallDot(stepData, false);

                if (stepData.isTurnPageToForward) {
                    canvas.drawCircle((centerX - stepData.dotsXOffsetDifference) + dotPadding * 3, centerY, radius, paintDot);
                } else {
                    canvas.drawCircle((centerX + stepData.dotsXOffsetDifference) + dotPadding, centerY, radius, paintDot);
                }
            } else { // def
                canvas.drawCircle((centerX + dotPadding) + dotPadding, centerY, radius, paintDot);
            }
        }
    }

    private float calculateRadiusForMediumDot(@NonNull AnimateStepData stepData, boolean isLeftDot) {
        float radius;

        // left
        if (isLeftDot) {
            if (stepData.isTurnPageToForward) {
                radius = currentDotSideSize - stepData.dotsAnimateSizeDifference;
                radius = radius < mediumDotSideSize ? mediumDotSideSize : radius;
            } else {
                radius = smallDotSideSize + stepData.dotsAnimateSizeDifference;
                radius = radius > mediumDotSideSize ? mediumDotSideSize : radius;
            }
            return radius;
        }

        // right
        if (stepData.isTurnPageToForward) {
            radius = smallDotSideSize + stepData.dotsAnimateSizeDifference;
            radius = radius > mediumDotSideSize ? mediumDotSideSize : radius;
        } else {
            radius = currentDotSideSize - stepData.dotsAnimateSizeDifference;
            radius = radius < mediumDotSideSize ? mediumDotSideSize : radius;
        }
        return radius;
    }

    private float calculateRadiusForSmallDot(@NonNull AnimateStepData stepData, boolean isLeftDot) {
        float radius;

        // left
        if (isLeftDot) {
            if (stepData.isTurnPageToForward) {
                radius = mediumDotSideSize - stepData.dotsAnimateSizeDifference;
                radius = radius < smallDotSideSize ? smallDotSideSize : radius;
            } else {
                radius = 0 + stepData.dotsAnimateSizeDifference;
                radius = radius > smallDotSideSize ? smallDotSideSize : radius;
            }
            return radius;
        }

        // right
        if (stepData.isTurnPageToForward) {
            radius = 0 + stepData.dotsAnimateSizeDifference;
            radius = radius > smallDotSideSize ? smallDotSideSize : radius;
        } else {
            radius = mediumDotSideSize - stepData.dotsAnimateSizeDifference;
            radius = radius < smallDotSideSize ? smallDotSideSize : radius;
        }
        return radius;
    }

    static class AnimateStepData {
        float dotsAnimateSizeDifference;
        float dotsXOffsetDifference;
        boolean isTurnPageToForward;
        boolean isAnimateRun;
    }
}

