package edu.nyu.pqs.ps3.backup;

import java.util.ArrayList;
import java.util.List;

class Connect4Model {
    private List<Connect4Listner> listners = new ArrayList<>();
    private int board[][] = new int[6][7];
    int numCols = board[0].length;
    int numRows = board.length;

    void initializeBoard(int[][] board) {
        this.board = board;
    }

    void insert(int column, int playerID) {
        int row = 5;
        while (board[row][column] != 0) {
            row--;
            if (row < 0)
                throw new IllegalArgumentException("Cannot insert here");
        }

        board[row][column] = playerID;
        checkForVictory();
    }

    protected int checkForVictory() {
        int result = 0;
        //TODO: check these bounds
        for (int row = numRows - 1; row > numRows - 4; row--) {
            for (int col = 0; col < numCols; col++) {
                if (board[row][col] != 0) {
                    result = 0;
                    int element = board[row][col];
                    if ((result = checkVertically(row, col, element)) == 0) {
                        if ((result = checkHorizontally(row, col, element)) == 0) {
                            if ((result = checkDiagonally(row, col, element)) == 0) {
                                continue;
                            }
                        }
                    }
                } else {
                    continue;
                }
                fireGameEnd(result);
                return result;
            }
        }

        fireNextMove();
        return 0;
    }

    private void fireGameEnd(int winnerID) {
        for (Connect4Listner l : listners)
            l.gameEnds(winnerID);
    }

    private void fireNextMove() {
        for (Connect4Listner l : listners)
            l.update();
    }

    private int checkDiagonally(int row, int col, int element) {
        if (col <= numCols-4 && board[row - 1][col + 1] == element && board[row - 1][col + 2] == element && board[row - 3][col + 3] == element)
            return element;
        if (col >= 3 && board[row - 1][col - 1] == element && board[row - 1][col - 2] == element && board[row - 3][col - 3] == element)
            return element;
        return 0;
    }

    private int checkHorizontally(int row, int col, int element) {
        if (col <= numCols-4 && board[row][col + 1] == element && board[row][col + 2] == element && board[row][col + 3] == element)
            return element;
        return 0;
    }

    private int checkVertically(int row, int col, int element) {
        if (board[row - 1][col] == element && board[row - 2][col] == element && board[row - 3][col] == element)
            return element;
        return 0;
    }


    public void attach(Connect4Listner listner) {
        listners.add(listner);
    }

    void printBoard() {
        for (int[] row : board) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }
}