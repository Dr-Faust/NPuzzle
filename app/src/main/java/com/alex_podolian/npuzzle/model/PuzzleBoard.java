//package com.alex_podolian.npuzzle.model;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//
//import java.util.ArrayList;
//
//
//public class PuzzleBoard {
//
//    private int puzzleSize;
//    private static final int[][] NEIGHBOUR_COORDINATES = {
//            {-1, 0},
//            {1, 0},
//            {0, -1},
//            {0, 1}
//    };
//    private ArrayList<PuzzleBlock> blocks;
//    private PuzzleBoard previousBoard;
//    private int stepNumber = 0;
//
//    PuzzleBoard(Bitmap bitmap, int parentWidth) {
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,true);
//        int tileWidth = scaledBitmap.getWidth()/ puzzleSize;
//        blocks = new ArrayList<>();
//        for (int i = 0; i < puzzleSize; i++) {
//            for (int j = 0; j < puzzleSize; j++) {
//                int tileNumber = i * puzzleSize + j;
//                if (tileNumber != puzzleSize * puzzleSize - 1){
//                    Bitmap tileBitmap = Bitmap.createBitmap(scaledBitmap, j*tileWidth, i*tileWidth, tileWidth, tileWidth);
//                    PuzzleTile tile = new PuzzleTile(tileBitmap, tileNumber);
//                    blocks.add(tile);
//                }
//                else{
//                    blocks.add(null);
//                }
//            }
//        }
//    }
//
//    public PuzzleBoard getPreviousBoard() {
//        return previousBoard;
//    }
//
//    public void setPreviousBoard(PuzzleBoard previousBoard) {
//        this.previousBoard = previousBoard;
//    }
//
//    PuzzleBoard(PuzzleBoard otherBoard, int stepNumber) {
//        blocks = (ArrayList<PuzzleTile>) otherBoard.blocks.clone();
//        previousBoard = otherBoard;
//        this.stepNumber = stepNumber + 1;
//    }
//
//    public void reset() {
//        // Nothing for now but you may have things to reset once you implement the solver.
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null)
//            return false;
//        return blocks.equals(((PuzzleBoard) o).blocks);
//    }
//
//    public void draw(Canvas canvas) {
//        if (blocks == null) {
//            return;
//        }
//        for (int i = 0; i < puzzleSize * puzzleSize; i++) {
//            PuzzleTile tile = blocks.get(i);
//            if (tile != null) {
//                tile.draw(canvas, i % puzzleSize, i / puzzleSize);
//            }
//        }
//    }
//
//    public boolean click(float x, float y) {
//        for (int i = 0; i < puzzleSize * puzzleSize; i++) {
//            PuzzleTile tile = blocks.get(i);
//            if (tile != null) {
//                if (tile.isClicked(x, y, i % puzzleSize, i / puzzleSize)) {
//                    return tryMoving(i % puzzleSize, i / puzzleSize);
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean resolved() {
//        for (int i = 0; i < puzzleSize * puzzleSize - 1; i++) {
//            PuzzleTile tile = blocks.get(i);
//            if (tile == null || tile.getNumber() != i)
//                return false;
//        }
//        return true;
//    }
//
//    private int XYtoIndex(int x, int y) {
//        return x + y * puzzleSize;
//    }
//
//    protected void swapTiles(int i, int j) {
//        PuzzleTile temp = blocks.get(i);
//        blocks.set(i, blocks.get(j));
//        blocks.set(j, temp);
//    }
//
//    private boolean tryMoving(int tileX, int tileY) {
//        for (int[] delta : NEIGHBOUR_COORDINATES) {
//            int nullX = tileX + delta[0];
//            int nullY = tileY + delta[1];
//            if (nullX >= 0 && nullX < puzzleSize && nullY >= 0 && nullY < puzzleSize &&
//                    blocks.get(XYtoIndex(nullX, nullY)) == null) {
//                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
//                return true;
//            }
//
//        }
//        return false;
//    }
//
//    public ArrayList<PuzzleBoard> neighbours() {
//        ArrayList<PuzzleBoard> result = new ArrayList<>();
//        int emptyTileX = 0;
//        int emptyTileY = 0;
//        for(int i = 0; i< puzzleSize * puzzleSize; i++){
//            if(blocks.get(i) == null){
//                emptyTileX = i % puzzleSize;
//                emptyTileY = i / puzzleSize;
//                break;
//            }
//        }
//        for(int[] coordinates : NEIGHBOUR_COORDINATES){
//            int neighbourX = emptyTileX + coordinates[0];
//            int neighbourY = emptyTileY + coordinates[1];
//            if(neighbourX >= 0 && neighbourX < puzzleSize && neighbourY >= 0 && neighbourY < puzzleSize){
//                PuzzleBoard neighbourBoard = new PuzzleBoard(this, stepNumber);
//                neighbourBoard.swapTiles((XYtoIndex(neighbourX,neighbourY)),XYtoIndex(emptyTileX,emptyTileY));
//                result.add(neighbourBoard);
//            }
//        }
//        return result;
//    }
//
//    public int priority() {
//        int manhattanDistance = 0;
//        for(int i = 0; i < puzzleSize * puzzleSize; i++){
//            PuzzleTile tile = blocks.get(i);
//            if(tile != null){
//                int correctPosition = tile.getNumber();
//                int correctX = correctPosition % puzzleSize;
//                int correctY = correctPosition / puzzleSize;
//                int currentX = i % puzzleSize;
//                int currentY = i / puzzleSize;
//
//                manhattanDistance += Math.abs(currentX - correctX) + Math.abs(currentY - correctY);
//            }
//        }
//        return manhattanDistance + stepNumber;
//    }
//
//}
