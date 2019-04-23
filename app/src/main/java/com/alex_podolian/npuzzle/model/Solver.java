package com.alex_podolian.npuzzle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by gilad on 12/19/15.
 */
public class Solver {

    private Board initialState;
    int minMoves = Integer.MAX_VALUE;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        initialState = initial;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (minMoves == Integer.MAX_VALUE) {
            minMoves = solution().size();
        }
        return minMoves;
    }

    // sequence of boards in a shortest solution
    public ArrayList<Board> solution() {
        Map<Board, Board> parents = new HashMap<Board, Board>();
        ArrayList<Board> path = new ArrayList<Board>();
        PriorityQueue<Board> frontier = new PriorityQueue<Board>();

        frontier.add(initialState);
        parents.put(initialState, null);

        int count = 0;

        boolean solvable = false;
        Board goalBoard = null;

        while (!frontier.isEmpty() && count++ < 500000) {
            Board nextBoard = frontier.poll();

            if (nextBoard.isGoal()) {
                solvable = true;
                goalBoard = nextBoard;
                break;
            }

            for (Board neighbor : nextBoard.neighbors()) {
                if (parents.get(neighbor) == null) {
                    frontier.add(neighbor);
                    parents.put(neighbor, nextBoard);
                }
            }
        }

        if (solvable) {
            Board board = goalBoard;
            path.add(goalBoard);
            while (board != initialState) {
                board = parents.get(board);
                path.add(board);
            }

            Collections.reverse(path);
        }

        return path;
    }
}