package com.alex_podolian.npuzzle.utils;

import android.graphics.Point;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class Utils {

    private Utils() {}

    public static boolean isNullOrEmpty(@Nullable String string) {
        return (string == null || string.isEmpty() || string.equalsIgnoreCase("null"));
    }

    public static int calculateTextSize(int puzzleSize) {
        int textSize = 24;
        for (int i = 3; i < puzzleSize; i++) {
            if (textSize == 6) {
                break;
            }
            textSize = textSize - 2;
        }
        return textSize;
    }

    private static int[][] createMapMatrix(ArrayList<Integer> map, int puzzleSize) {
        int[][] mapMatrix = new int[puzzleSize][puzzleSize];
        int index = 0;
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                mapMatrix[i][j] = map.get(index++);
            }
        }
        return mapMatrix;
    }

    public static int[][] makeSpiralMatrix(int puzzleSize) {
        int value = 1;
        int minCol = 0;
        int maxCol = puzzleSize - 1;
        int minRow = 0;
        int maxRow = puzzleSize - 1;
        int[][] spiralMatrix = new int[puzzleSize][puzzleSize];

        while (value <= puzzleSize * puzzleSize) {
            for (int i = minCol; i <= maxCol; i++) {
                if (value == puzzleSize * puzzleSize)
                    spiralMatrix[minRow][i] = 0;
                else
                    spiralMatrix[minRow][i] = value;
                value++;
            }
            for (int i = minRow + 1; i <= maxRow; i++) {
                if (value == puzzleSize * puzzleSize)
                    spiralMatrix[i][maxCol] = 0;
                else
                    spiralMatrix[i][maxCol] = value;
                value++;
            }
            for (int i = maxCol - 1; i >= minCol; i--) {
                if (value == puzzleSize * puzzleSize)
                    spiralMatrix[maxRow][i] = 0;
                else
                    spiralMatrix[maxRow][i] = value;
                value++;
            }
            for (int i = maxRow - 1; i >= minRow + 1; i--) {
                if (value == puzzleSize * puzzleSize)
                    spiralMatrix[i][minCol] = 0;
                else
                    spiralMatrix[i][minCol] = value;
                value++;
            }
            minCol++;
            minRow++;
            maxCol--;
            maxRow--;
        }
        return spiralMatrix;
    }

    public static ArrayList<Integer> makeSpiralMap(int puzzleSize) {
        int[][] spiralMatrix = makeSpiralMatrix(puzzleSize);
        ArrayList<Integer> spiralMap = new ArrayList<>();

        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                spiralMap.add(spiralMatrix[i][j]);
            }
        }
        return spiralMap;
    }

    private static Point swapNode(int i, int j, int[][] matrix, Point emptyNodeIndex) {
        int id = matrix[i][j];
        matrix[i][j] = 0;
        matrix[emptyNodeIndex.x][emptyNodeIndex.y] = id;
        emptyNodeIndex = new Point(i, j);
        return emptyNodeIndex;
    }

    public static int[][] shuffleMatrix(int[][] matrix, int puzzleSize) {
        Point emptyNodeIndex = new Point(0, 0);
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                if (matrix[i][j] == 0) {
                    emptyNodeIndex = new Point(i, j);
                }
            }
        }
        for (int i = 0; i < 100; i++) {
            ArrayList<Point> options = new ArrayList<>();
            if (emptyNodeIndex.x + 1 < puzzleSize) {
                options.add(new Point(emptyNodeIndex.x + 1, emptyNodeIndex.y));
            }
            if (emptyNodeIndex.x - 1 >= 0) {
                options.add(new Point(emptyNodeIndex.x - 1, emptyNodeIndex.y));
            }
            if (emptyNodeIndex.y + 1 < puzzleSize) {
                options.add(new Point(emptyNodeIndex.x, emptyNodeIndex.y + 1));
            }
            if (emptyNodeIndex.y - 1 >= 0) {
                options.add(new Point(emptyNodeIndex.x, emptyNodeIndex.y - 1));
            }
            Collections.shuffle(options);
            Point selectedPoint = options.get(0);
            emptyNodeIndex = swapNode(selectedPoint.x, selectedPoint.y, matrix, emptyNodeIndex);
        }
        return matrix;
    }
}