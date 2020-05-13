package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;
import edu.nyu.pqs.ps3.model.Result;

import java.awt.*;
import java.util.*;
import java.util.List;

//TODO: fix filled col issue
public class ComputerPlayer implements Player {
    private int playerId;
    private String playerName;
    private Color playerColor;
    private ConnectFourModel model;
    private int board[][];
    private int numRows;
    private int numCols;
    private int tokensToWin;
    private int otherPlayerId;
    private AIDifficulty difficulty;
    private int maxDepth = 10;
    private int minDepth = 6;

    public ComputerPlayer(int playerId, String playerName, Color playerColor, ConnectFourModel model, int otherPlayerId, AIDifficulty difficulty) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.model = model;
        numCols = model.getNumCols();
        numRows = model.getNumRows();
        tokensToWin = model.getTokensToWin();
        this.otherPlayerId = otherPlayerId;
        this.difficulty = difficulty;
    }

    private int[][] generateBoardCopy(int[][] board) {
        int tempBoard[][] = new int[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                tempBoard[row][col] = board[row][col];
            }
        }

        return tempBoard;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public Color getTokenColor() {
        return playerColor;
    }

    @Override
    public void play() {
        //board = generateBoardCopy(model.getBoard());  //get grid status from model
        if (difficulty == AIDifficulty.EASY) {
            int col = oneStepLookAhead(model.getBoard());
            model.insertToken(col);
        } else if (difficulty == AIDifficulty.MEDIUM) {
            board = generateBoardCopy(model.getBoard());
            int col = minimax(board, minDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 0).getCol();
            if (columnIsFull(board, col))
                col = new Random().nextInt(numCols - 1);
            model.insertToken(col);
        } else {
            board = generateBoardCopy(model.getBoard());
            int col = minimax(board, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 0).getCol();
            if (columnIsFull(board, col))
                col = new Random().nextInt(numCols - 1);
            model.insertToken(col);
        }
    }

    private int oneStepLookAhead(int[][] board) {
        for (int col = 0; col < numCols; col++) {
            int[][] newBoard = insertInBoard(col, board, playerId);
            if (checkGameStatus(newBoard) == Result.WIN)
                return col;
        }
        return new Random().nextInt(numCols - 1);
    }

    private Pair minimax(int[][] board, int depth, int alpha, int beta, boolean maximizingPlayer, int column) {
        if (depth == 0)
            return new Pair(column, evaluateBoard(board));
        Result result = checkGameStatus(board);
        if (result == Result.WIN) {
            //printBoard(board);
            //System.out.println("maxim: "+maximizingPlayer);
            if (maximizingPlayer)
                return new Pair(column, -10000);
            else
                return new Pair(column, 10000);
        } else if (result == Result.DRAW)
            return new Pair(column, 0);

        int retcol = column;
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            int[][] newBoard = insertInBoard(0, board, playerId);
            //if(depth == maxDepth) printBoard(newBoard);
            for (int col = 0; col < numCols; col++) {
                if (columnIsFull(newBoard, col))
                    break;
                if (col != 0) {
                    changeColInsertion(newBoard, col, col - 1, playerId);
                }
                // if(depth == maxDepth) printBoard(newBoard);
                int eval = minimax(newBoard, depth - 1, alpha, beta, false, col).getVal();
                if (maxEval < eval) {
                    maxEval = eval;
                    retcol = col;
                }
                alpha = alpha > eval ? alpha : eval;
                if (beta <= alpha)
                    break;
            }
            return new Pair(retcol, maxEval);
        } else {
            int minEval = Integer.MAX_VALUE;
            int[][] newBoard = insertInBoard(0, board, otherPlayerId);
            for (int col = 0; col < numCols; col++) {
                if (columnIsFull(newBoard, col))
                    break;
                if (col != 0) {
                    changeColInsertion(newBoard, col, col - 1, otherPlayerId);
                }
                int eval = minimax(newBoard, depth - 1, alpha, beta, true, col).getVal();
                if (minEval > eval) {
                    minEval = eval;
                    retcol = col;
                }
                beta = beta < eval ? beta : eval;
                if (beta <= alpha)
                    break;
            }
            return new Pair(retcol, minEval);
        }
    }

    private boolean columnIsFull(int[][] newBoard, int column) {
        return newBoard[0][column] != 0;
    }

    private void changeColInsertion(int[][] newBoard, int newCol, int oldCol, int playerId) {
        int row = 0;
        while (newBoard[row][oldCol] != playerId) {
            row++;
        }
        newBoard[row][oldCol] = 0;

        row = numRows - 1;
        try {
            while (true) {
                if (newBoard[row][newCol] == 0)
                    break;
                row--;
            }
        } catch (ArrayIndexOutOfBoundsException ae) {
            printBoard(newBoard);
            System.out.println(row);
            System.out.println(newCol);
            System.out.println(columnIsFull(newBoard, newCol));
        }
        newBoard[row][newCol] = playerId;


    }

    private int evaluateBoard(int[][] newBoard) {
        int score = longest_chain(newBoard) * 10;
        int center = numCols / 2;
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                if (newBoard[row][col] != 0) {
                    //TODO: check this +/-
                    if (newBoard[row][col] == playerId)
                        score -= Math.abs(center - col);
                    else
                        score += Math.abs(center - col);
                }
        return score;
    }

    private int longest_chain(int[][] newBoard) {
        int longest = 0;
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                if (newBoard[row][col] != 0) {
                    int temp = maxLengthFromCell(newBoard, row, col);
                    longest = longest > temp ? longest : temp;
                }
        return longest;
    }

    private int maxLengthFromCell(int[][] newBoard, int row, int col) {
        List<Integer> directions = new ArrayList<>();
        directions.add(chainLength(newBoard, row, col, new Pair(1, 1)) + chainLength(newBoard, row, col, new Pair(-1, -1)) + 1);
        directions.add(chainLength(newBoard, row, col, new Pair(1, 0)) + chainLength(newBoard, row, col, new Pair(-1, 0)) + 1);
        directions.add(chainLength(newBoard, row, col, new Pair(0, 1)) + chainLength(newBoard, row, col, new Pair(0, -1)) + 1);
        directions.add(chainLength(newBoard, row, col, new Pair(-1, 1)) + chainLength(newBoard, row, col, new Pair(1, -1)) + 1);

        return Collections.max(directions);
    }

    private int chainLength(int[][] newBoard, int row, int col, Pair direction) {
        int count = 0;
        while (row >= 0 && row < numRows && col >= 0 && col < numCols && newBoard[row][col] == playerId) {
            row += direction.getCol();
            col += direction.getVal();
            count++;
        }
        return count - 1;
    }


    private int[][] insertInBoard(int col, int[][] board, int id) {
        //System.out.println(col+" "+id);
        int[][] newBoard = generateBoardCopy(board);
        for (int row = numRows - 1; row >= 0; row--)
            if (newBoard[row][col] == 0) {
                newBoard[row][col] = id;
                break;
            }

        return newBoard;
    }

    private Result checkGameStatus(int[][] newBoard) {
        if (gameDraw(newBoard))
            return Result.DRAW;
        else if (gameWon(newBoard))
            return Result.WIN;
        return Result.NONE;
    }

    boolean gameWon_k(int[][] newBoard) {
        int longest = 0;
        for (int row = numRows - 1; row > numRows - tokensToWin; row--) {
            for (int col = 0; col < numCols; col++) {
                int temp = maxLengthFromCell(newBoard, row, col);
                longest = longest > temp ? longest : temp;
            }
        }
        return longest >= 4;
    }

    boolean gameWon(int[][] newBoard) {
        for (int row = numRows - 1; row > numRows - tokensToWin; row--) {
            for (int col = 0; col < numCols; col++) {
                if (newBoard[row][col] != 0) {
                    int element = newBoard[row][col];
                    if (checkVertically(newBoard, row, col, element))
                        return true;
                    if (checkHorizontally(newBoard, row, col, element))
                        return true;
                    if (checkDiagonally(newBoard, row, col, element))
                        return true;
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    private boolean checkVertically(int[][] newBoard, int row, int col, int element) {
        int endRow = row - tokensToWin;
        for (int curr_row = row - 1; curr_row > endRow; curr_row--)
            if (newBoard[curr_row][col] != element)
                return false;
        return true;
    }

    private boolean checkHorizontally(int[][] newBoard, int row, int col, int element) {
        if (col > numCols - tokensToWin)
            return false;
        int endCol = col + tokensToWin;
        for (int curr_col = col + 1; curr_col < endCol; curr_col++)
            if (newBoard[row][curr_col] != element)
                return false;
        return true;
    }

    private boolean checkDiagonally(int[][] newBoard, int row, int col, int element) {
        int left = col - 1;
        int right = col + 1;
        int leftCount = 1;
        int rightCount = 1;
        for (int curr_row = row - 1; curr_row > row - tokensToWin; curr_row--) {
            if (left >= 0 && newBoard[curr_row][left] == element)
                leftCount++;
            if (right < numCols && newBoard[curr_row][right] == element)
                rightCount++;

            left--;
            right++;
        }
        if (leftCount == tokensToWin || rightCount == tokensToWin)
            return true;

        return false;
    }

    private boolean gameDraw(int[][] newBoard) {
        for (int[] row : newBoard) {
            for (int element : row) {
                if (element == 0)
                    return false;
            }
        }
        return true;
    }

    private void printBoard(int[][] b) {
        for (int[] row : b) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public class Pair {
        private final int col;
        private final int val;

        public Pair(int col, int val) {
            this.col = col;
            this.val = val;
        }

        public int getCol() {
            return col;
        }

        public int getVal() {
            return val;
        }
    }
}
