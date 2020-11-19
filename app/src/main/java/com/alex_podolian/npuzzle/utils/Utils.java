package com.alex_podolian.npuzzle.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Utils {

    private Utils() {
    }

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

    public static int[][] ArrayToMatrix(ArrayList<Integer> arr, int puzzleSize) {
        int[][] mapMatrix = new int[puzzleSize][puzzleSize];
        int index = 0;
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                mapMatrix[i][j] = arr.get(index++);
            }
        }
        return mapMatrix;
    }

    public static ArrayList<Integer> MatrixToArray(int[][] matrix, int puzzleSize) {
        ArrayList<Integer> arr = new ArrayList<>(puzzleSize * puzzleSize);
        int index = 0;
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                arr.add(matrix[i][j]);
            }
        }
        return arr;
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
        RLogs.w("SPIRAL MAP = " + spiralMap);
        return spiralMap;
    }

    public static ArrayList<Integer> makeClassicMap(int puzzleSize) {
        ArrayList<Integer> classicMap = new ArrayList<>();

        for (int i = 1; i <= puzzleSize * puzzleSize; i++) {
            classicMap.add(i == puzzleSize * puzzleSize ? 0 : i);
        }
        RLogs.w("CLASSIC MAP = " + classicMap);
        return classicMap;
    }

    private static void swapNodes(int i, int j, ArrayList<Integer> puzzleMap) {
        int id = puzzleMap.get(j);
        puzzleMap.set(j, puzzleMap.get(i));
        puzzleMap.set(i, id);
    }

    private static Point getEmptyPoint(ArrayList<Integer> puzzleMap, int puzzleSize) {
        int emptyX = 0;
        int emptyY = 0;
        for (int i = 0; i < puzzleSize * puzzleSize; i++) {
            if (puzzleMap.get(i) == 0) {
                emptyX = i % puzzleSize;
                emptyY = i / puzzleSize;
                break;
            }
        }
        return new Point(emptyX, emptyY);
    }

    public static ArrayList<Integer> shuffleMap(ArrayList<Integer> puzzleMap, int puzzleSize) {

        for (int i = 0; i < 300; i++) {
            ArrayList<Point> options = new ArrayList<>();
            int emptyX = getEmptyPoint(puzzleMap, puzzleSize).x;
            int emptyY = getEmptyPoint(puzzleMap, puzzleSize).y;

            if (emptyX + 1 < puzzleSize) {
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
            swapNodes(xyToIndex(selectedPoint.x, selectedPoint.y, puzzleSize),
                    xyToIndex(emptyX, emptyY, puzzleSize), puzzleMap);
        }
        return puzzleMap;
    }

    public static int xyToIndex(int x, int y, int puzzleSize) {
        return x + y * puzzleSize;
    }

    public static ArrayList<String> mapFromFile(Uri uri, Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = null;
        if (inputStream != null) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        ArrayList<String> map = new ArrayList<>();
        if (reader != null) {
            for (String line; (line = reader.readLine()) != null; ) {
                map.add(line);
            }
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return map;
    }
}