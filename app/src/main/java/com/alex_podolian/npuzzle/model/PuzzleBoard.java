package com.alex_podolian.npuzzle.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import androidx.annotation.NonNull;

import com.alex_podolian.npuzzle.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class PuzzleBoard {

    private static final int[][] NEIGHBOUR_COORDINATES = {
        {-1, 0},
        {1, 0},
        {0, -1},
        {0, 1}
    };

    private Context context;
    private int textSize;
    private int blockSize;
    private int puzzleSize;
    private ArrayList<PuzzleBlock> puzzleBlocksArr;
    private ArrayList<Integer> puzzleMap;
    private ArrayList<Integer> goalMap;
    private PuzzleBoard previousBoard;
    private int stepNumber = 0;

    public PuzzleBoard(Context context, int puzzleSize, int textSize, ArrayList<Integer> puzzleMap, int blockSize) {
        this.context = context;
        this.textSize = textSize;
        this.blockSize = blockSize;
        this.puzzleSize = puzzleSize;
        goalMap = Utils.makeSpiralMap(puzzleSize);
        this.puzzleMap = puzzleMap;
        initPuzzleMatrix(blockSize, textSize);
    }

    private void initPuzzleMatrix(int blockSize, int textSize) {
        puzzleBlocksArr = new ArrayList<>();
	    int id;
        for (int i = 0; i < puzzleSize * puzzleSize; i++) {
	        id = puzzleMap.get(i);
	        PuzzleBlock puzzleBlock = new PuzzleBlock(context, id, (i % puzzleSize) * blockSize,
		        (i / puzzleSize) * blockSize, blockSize, textSize);
	        puzzleBlocksArr.add(puzzleBlock);
        }
    }

    private Point getEmptyBlockPoint() {
        int emptyX = 0;
        int emptyY = 0;
        for(int i = 0; i < puzzleSize * puzzleSize; i++) {
            if(puzzleMap.get(i) == 0) {
                emptyX = i % puzzleSize;
                emptyY = i / puzzleSize;
                break;
            }
        }
        return new Point(emptyX, emptyY);
    }

    public PuzzleBoard getPreviousBoard() {
        return previousBoard;
    }

    public void setPreviousBoard(PuzzleBoard previousBoard) {
        this.previousBoard = previousBoard;
    }

    public ArrayList<Integer> getPuzzleMap() {
        return puzzleMap;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    private PuzzleBoard(PuzzleBoard previousBoard, int stepNumber) {
        this.context = previousBoard.context;
        this.textSize = previousBoard.textSize;
        this.blockSize = previousBoard.blockSize;
        this.puzzleSize = previousBoard.puzzleSize;
        this.goalMap = previousBoard.goalMap;
        this.previousBoard = previousBoard;
        this.stepNumber = stepNumber + 1;
        this.puzzleMap = new ArrayList<>(previousBoard.puzzleMap);
        this.puzzleBlocksArr = new ArrayList<>();
        int id;
        for (int i = 0; i < puzzleSize * puzzleSize; i++) {
            id = previousBoard.puzzleMap.get(i);
            PuzzleBlock puzzleBlock = new PuzzleBlock(context, id, (i % puzzleSize) * blockSize,
                (i / puzzleSize) * blockSize, blockSize, textSize);
            puzzleBlocksArr.add(puzzleBlock);
        }
    }

    @Override
    public boolean equals(Object o) {
        return puzzleMap.equals(((PuzzleBoard) o).puzzleMap);
    }

    public void onDraw(@NonNull Canvas canvas, @NonNull Paint paint) {
        if (puzzleBlocksArr == null) {
            return;
        }
        for (int i = 0; i < puzzleSize * puzzleSize; i++) {
            if (puzzleBlocksArr.get(i) != null) {
                puzzleBlocksArr.get(i).onDraw(canvas, paint);
            }
        }
    }

    public void onTouch(int i, int j) {
        Point emptyPoint = getEmptyBlockPoint();

        if (i + 1 < puzzleSize && i + 1 == emptyPoint.x && j == emptyPoint.y) {
            makeMove(i, j, emptyPoint.x, emptyPoint.y);
        } else if (i - 1 >= 0 && i - 1 == emptyPoint.x && j == emptyPoint.y) {
            makeMove(i, j, emptyPoint.x, emptyPoint.y);
        } else if (j + 1 < puzzleSize && i == emptyPoint.x && j + 1 == emptyPoint.y) {
            makeMove(i, j, emptyPoint.x, emptyPoint.y);
        } else if (j - 1 >= 0 && i == emptyPoint.x && j - 1 == emptyPoint.y) {
            makeMove(i, j, emptyPoint.x, emptyPoint.y);
        }
    }

    private void makeMove(int i, int j, int emptyX, int emptyY) {
        swapBlocks(Utils.xyToIndex(i, j, puzzleSize),
            Utils.xyToIndex(emptyX, emptyY, puzzleSize));
//        if (isGoal()) {
//            new AlertDialog.Builder(context, R.style.DialogTheme)
//                .setTitle("Congratulations!")
//                .setCancelable(false)
//                .setMessage("You made it!")
//                .setPositiveButton("Continue", (dialog, which) -> {
//                    Intent intent = new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
//                    dialog.dismiss();
//                }).show();
//        }
    }

    public void shuffleBoard() {
        for (int i = 0; i < 300; i++) {
            ArrayList<Point> options = new ArrayList<>();
            int emptyX = getEmptyBlockPoint().x;
            int emptyY = getEmptyBlockPoint().y;

            if (emptyX+ 1 < puzzleSize) {
                options.add(new Point(emptyX + 1, emptyY));
            }
            if (emptyX - 1 >= 0) {
                options.add(new Point(emptyX - 1, emptyY));
            }
            if (emptyY + 1 < puzzleSize) {
                options.add(new Point(emptyX, emptyY + 1));
            }
            if (emptyY - 1 >= 0) {
                options.add(new Point(emptyX, emptyY - 1));
            }
            Collections.shuffle(options);
            Point selectedPoint = options.get(0);
            swapBlocks(Utils.xyToIndex(selectedPoint.x, selectedPoint.y, puzzleSize),
                Utils.xyToIndex(emptyX, emptyY, puzzleSize));
        }
    }

    private void swapBlocks(int i, int j) {
        int id = puzzleBlocksArr.get(j).getId();
        puzzleBlocksArr.get(j).setId(puzzleBlocksArr.get(i).getId());
        puzzleBlocksArr.get(i).setId(id);

        id = puzzleMap.get(j);
        puzzleMap.set(j, puzzleMap.get(i));
        puzzleMap.set(i, id);
    }

    public Iterable<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> result = new ArrayList<>();
        int emptyBlockX = 0;
        int emptyBlockY = 0;
        for(int i = 0; i < puzzleSize * puzzleSize; i++) {
            if(puzzleMap.get(i) == 0) {
                emptyBlockX = i % puzzleSize;
                emptyBlockY = i / puzzleSize;
                break;
            }
        }
        for (int[] coordinates : NEIGHBOUR_COORDINATES) {
            int neighbourX = emptyBlockX + coordinates[0];
            int neighbourY = emptyBlockY + coordinates[1];
            if (neighbourX >= 0 && neighbourX < puzzleSize && neighbourY >= 0 && neighbourY < puzzleSize) {
                PuzzleBoard neighbourBoard = new PuzzleBoard(this, stepNumber);
                neighbourBoard.swapBlocks((Utils.xyToIndex(neighbourX, neighbourY, puzzleSize)),
                    Utils.xyToIndex(emptyBlockX, emptyBlockY, puzzleSize));
                result.add(neighbourBoard);
            }
        }
        return result;
    }

    public boolean isGoal()  {
        return puzzleMap.equals(goalMap);
    }

    public int manhattan() {
        int distance = 0;
        
        for(Integer node : puzzleMap) {
            if (node != 0 && goalMap.indexOf(node) != puzzleMap.indexOf(node)) {
                int xGoal = goalMap.indexOf(node) / puzzleSize;
                int yGoal = goalMap.indexOf(node) % puzzleSize;
                int xCurrent = puzzleMap.indexOf(node) / puzzleSize;
                int yCurrent = puzzleMap.indexOf(node) % puzzleSize;
                distance += abs(xGoal - xCurrent) + abs(yGoal - yCurrent);
            }
        }
        return distance;
    }

    public int euclidean() {
        int distance = 0;

        for(Integer node : puzzleMap) {
            if (node != 0 && goalMap.indexOf(node) != puzzleMap.indexOf(node)) {
                int xGoal = goalMap.indexOf(node) / puzzleSize;
                int yGoal = goalMap.indexOf(node) % puzzleSize;
                int xCurrent = puzzleMap.indexOf(node) / puzzleSize;
                int yCurrent = puzzleMap.indexOf(node) % puzzleSize;
                distance += sqrt(pow((xGoal - xCurrent), 2) + pow((yGoal - yCurrent), 2));
            }
        }
        return distance;
    }

    public int blocksOutOfRowAndColumn() {
        int dist = 0;
        for(Integer node : puzzleMap) {
            if (node != 0 && goalMap.indexOf(node) != puzzleMap.indexOf(node)) {
                int goalNodeIndex =  goalMap.indexOf(node);
                int puzzleNodeIndex = puzzleMap.indexOf(node);
                if (puzzleNodeIndex / puzzleSize != goalNodeIndex / puzzleSize) {
                    dist++;
                }
                if (puzzleNodeIndex % puzzleSize != goalNodeIndex % puzzleSize) {
                    dist++;
                }
            }
        }
        return dist;
    }

   public int linearConflict() {
       int dist = 0;
       dist = manhattan();
       dist += linearVerticalConflict();
       dist += linearHorizontalConflict();
       return dist;
   }

    private int linearVerticalConflict() {
        int linearConflict = 0;
        int index = 0;
        for (int row = 0; row < puzzleSize; row++) {
            int max = -1;
            for (int col = 0; col < puzzleSize; col++) {
                int value =  puzzleMap.get(index);
                index++;
                if (value != 0 && (value - 1) / puzzleSize == row) {
                    if (value > max) {
                        max = value;
                    } else {
                        linearConflict++;
                    }
                }
            }
        }
        return linearConflict;
    }

    private int linearHorizontalConflict() {
        int linearConflict = 0;
        int index = 0;
        for (int col = 0; col < puzzleSize; col++) {
            int max = -1;
            for (int row = 0; row < puzzleSize; row++) {
                int value =  puzzleMap.get(index);
                index++;
                if (value != 0 && value % puzzleSize == col + 1) {
                    if (value > max) {
                        max = value;
                    } else {
                        linearConflict++;
                    }
                }
            }
        }
        return linearConflict;
    }

    public int misplacedBlocks() {
        int count = 0;
        for(Integer node : puzzleMap) {
            if (node != 0 && goalMap.indexOf(node) != puzzleMap.indexOf(node)) {
                count++;
            }
        }
        return count;
    }
}
