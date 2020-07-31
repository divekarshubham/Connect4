/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;
import edu.nyu.pqs.ps3.model.Result;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The computer AI offers 3 modes of difficulty:
 * EASY: where a one-step lookahead is followed to determine win else random move is generated
 * MEDIUM: AI uses minimax algorithm with depth of 6
 * HARD: AI uses minimax algorithm with depth of 10
 *
 * The current model runs a bit slow, bur can be improved following iterative deepening or bitboards
 * to further improve the performance
 */
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

    /**
     * Constructor initializes the required parameters and sets the difficulty
     */
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

    /**
     * @return Used to retrieve the player ID
     */
    @Override
    public int getPlayerId() {
        return playerId;
    }
    /**
     * @return Used to retrieve the player name
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }
    /**
     * @return Used to retrieve the player token color
     */
    @Override
    public Color getTokenColor() {
        return playerColor;
    }
    /**
     * @return making a move for the game, next column is selected
     * based on the difficulty level set
     */
    @Override
    public void play() {
        if (difficulty == AIDifficulty.EASY) {
            int col = oneStepLookAhead(model.getBoard());
            model.insertToken(col);
        } else if (difficulty == AIDifficulty.MEDIUM) {
            board = generateBoardCopy(model.getBoard());
            int col = minimax(board, minDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 0).getCol();
            while (columnIsFull(model.getBoard(), col))
                col = new Random().nextInt(numCols - 1);
            model.insertToken(col);
        } else {
            board = generateBoardCopy(model.getBoard());
            int col = minimax(board, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 0).getCol();
            while (columnIsFull(model.getBoard(), col))
                col = new Random().nextInt(numCols - 1);
            model.insertToken(col);
        }
    }

    /**
     * Checks if inserting a token in any of the columns will result in a win
     * if so then returns the column number
     * @param board Copy of the original board
     * @return value of column if it results in a victory or a random column
     */
    private int oneStepLookAhead(int[][] board) {
        for (int col = 0; col < numCols; col++) {
            int[][] newBoard = insertInBoard(col, board, playerId);
            if (checkGameStatus(newBoard) == Result.WIN)
                return col;
        }
        return new Random().nextInt(numCols - 1);
    }

    /**
     * Minimax algorithm with alpha=beta pruning. It recurses to find the best position of insertion
     * in the board. If the player is computer then it evaluates the best outcome by maximizing over
     * the evaluation of the board. Else it tries to minimize the evaluation in case of human player.
     * Redundant nodes are cancelled out with A/B pruning
     * @param board board to evaluate
     * @param depth maximum depth to recurse to
     * @param alpha Maximum value seen yet
     * @param beta Minimum value seen yer
     * @param maximizingPlayer if the player is computer or the other
     * @param column parent of the call
     * @return Tuple of column to insert and the evaluation of the column
     */
    private Pair minimax(int[][] board, int depth, int alpha, int beta, boolean maximizingPlayer, int column) {
        if (depth == 0)
            return new Pair(column, evaluateBoard(board));
        Result result = checkGameStatus(board);
        if (result == Result.WIN) {
            if (maximizingPlayer)
                return new Pair(column, -10000);
            else
                return new Pair(column, 10000);
        } else if (result == Result.DRAW)
            return new Pair(column, 0);

        /**
         * If the player is maximizing then evaluate the best outcome
         */
        int retcol = column;
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            int lastColInsertedIn = 0;
            while (columnIsFull(board, lastColInsertedIn))
                lastColInsertedIn++;
            int[][] newBoard = insertInBoard(lastColInsertedIn, board, playerId);

            for (int col = lastColInsertedIn; col < numCols; col++) {
                if (columnIsFull(newBoard, col) && col != lastColInsertedIn)
                    continue;
                if (col != 0) {
                    changeColInsertion(newBoard, col, lastColInsertedIn, playerId);
                    lastColInsertedIn = col;
                }
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
            /**
             * If the player is minimizing then evaluate the worst outcome
             */
            int minEval = Integer.MAX_VALUE;
            int lastColInsertedIn = 0;
            while (columnIsFull(board, lastColInsertedIn))
                lastColInsertedIn++;
            int[][] newBoard = insertInBoard(lastColInsertedIn, board, otherPlayerId);
            for (int col = 0; col < numCols; col++) {
                if (columnIsFull(newBoard, col) && col != lastColInsertedIn)
                    continue;
                if (col != 0) {
                    changeColInsertion(newBoard, col, lastColInsertedIn, otherPlayerId);
                    lastColInsertedIn = col;
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

    /**
     *  Make a copy of the board for evaluation
     */
    private int[][] generateBoardCopy(int[][] board) {
        int tempBoard[][] = new int[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                tempBoard[row][col] = board[row][col];
            }
        }

        return tempBoard;
    }

    /**
     * @return Checks if the column is full by checking the top element
     */
    private boolean columnIsFull(int[][] newBoard, int column) {
        return newBoard[0][column] != 0;
    }

    /**
     * Undo previous placement and insert into new column. This is done so
     * that a new board is not required to be generated for each iteration
     */
    private void changeColInsertion(int[][] newBoard, int newCol, int oldCol, int playerId) {
        int row = 0;
        while (newBoard[row][oldCol] != playerId) {
            row++;
        }
        newBoard[row][oldCol] = 0;

        row = numRows - 1;
        while (true) {
            if (newBoard[row][newCol] == 0)
                break;
            row--;
        }
        newBoard[row][newCol] = playerId;
    }

    /**
     *  Center based evaluation, the farther the token is from the center
     *  lesser the score. This is done for spatial analysis of the board.
     *  It first calculates a score based on the longest chain of the current player
     *  and then tweaks the score based on token positions
     * @param newBoard board to evaluate
     * @return spatial evaluation of the board
     */
    private int evaluateBoard(int[][] newBoard) {
        int score = longest_chain(newBoard) * 10;
        int center = numCols / 2;
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                if (newBoard[row][col] != 0) {
                    if (newBoard[row][col] == playerId)
                        score -= Math.abs(center - col);
                    else
                        score += Math.abs(center - col);
                }
        return score;
    }

    /**
     *  Calculates the longest chain of the user currently on the board by checking the
     *  length from each player cell
     * @param newBoard board to evaluate
     * @return length of the longest chain
     */
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

    /**
     * @return the longest chain formed from each cell in each of the direction
     *  vertical/horizontal/diagonal
     */
    private int maxLengthFromCell(int[][] newBoard, int row, int col) {
        List<Integer> directions = new ArrayList<>();
        directions.add(chainLength(newBoard, row, col, new Pair(1, 1)) + chainLength(newBoard, row, col, new Pair(-1, -1)) + 1);
        directions.add(chainLength(newBoard, row, col, new Pair(1, 0)) + chainLength(newBoard, row, col, new Pair(-1, 0)) + 1);
        directions.add(chainLength(newBoard, row, col, new Pair(0, 1)) + chainLength(newBoard, row, col, new Pair(0, -1)) + 1);
        directions.add(chainLength(newBoard, row, col, new Pair(-1, 1)) + chainLength(newBoard, row, col, new Pair(1, -1)) + 1);

        return Collections.max(directions);
    }

    /**
     * Finds longest chain in a certain direction
     */
    private int chainLength(int[][] newBoard, int row, int col, Pair direction) {
        int count = 0;
        while (row >= 0 && row < numRows && col >= 0 && col < numCols && newBoard[row][col] == playerId) {
            row += direction.getCol();
            col += direction.getVal();
            count++;
        }
        return count - 1;
    }

    /**
     * Generate a new copy of the board and insert the token into desired column
     */
    private int[][] insertInBoard(int col, int[][] board, int id) {
        int[][] newBoard = generateBoardCopy(board);
        for (int row = numRows - 1; row >= 0; row--)
            if (newBoard[row][col] == 0) {
                newBoard[row][col] = id;
                break;
            }

        return newBoard;
    }

    /**
     * Checks if adding a disc to a column, results in a win/draw/no_outcome.
     *
     * @return outcome of the operation
     */
     Result checkGameStatus(int[][] newBoard) {
        if (gameDraw(newBoard))
            return Result.DRAW;
        else if (gameWon(newBoard))
            return Result.WIN;
        return Result.NONE;
    }

    /**
     * Starting from the bottom row, it checks to see if the player has the required amount of
     * tokens in a row to win by checking in each directions(up, right and diagonal)
     *
     * @return true if tokensToWin in a row found, false otherwise
     */
    private  boolean gameWon(int[][] newBoard) {
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

    /**
     * Check vertically up if there are required amount of tokens in a row
     *
     * @param row     Row of the position to start
     * @param col     Col of the position to start
     * @param element Player id
     * @return true if tokensToWin in a row found, false otherwise
     */
    private boolean checkVertically(int[][] newBoard, int row, int col, int element) {
        int endRow = row - tokensToWin;
        for (int curr_row = row - 1; curr_row > endRow; curr_row--)
            if (newBoard[curr_row][col] != element)
                return false;
        return true;
    }

    /**
     * Check horizontally right if there are required amount of tokens in a row
     *
     * @param row     Row of the position to start
     * @param col     Col of the position to start
     * @param element Player id
     * @return true if tokensToWin in a row found, false otherwise
     */
    private boolean checkHorizontally(int[][] newBoard, int row, int col, int element) {
        if (col > numCols - tokensToWin)
            return false;
        int endCol = col + tokensToWin;
        for (int curr_col = col + 1; curr_col < endCol; curr_col++)
            if (newBoard[row][curr_col] != element)
                return false;
        return true;
    }

    /**
     * Check diagonally up (left and right) if there are required amount of tokens in a row
     *
     * @param row     Row of the position to start
     * @param col     Col of the position to start
     * @param element Player id
     * @return true if tokensToWin in a row found, false otherwise
     */
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

    /**
     * If none of the positions of the top row contain 0, it means the board is full
     *
     * @return False if any empty position found, true otherwise
     */
    private boolean gameDraw(int[][] newBoard) {
        for (int element : newBoard[0]) {
            if (element == 0)
                return false;
        }
        return true;
    }

    /**
     * Helper class to get a tuple, which is used in minimax and for
     * denoting a direction
     */
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

    @Override
    public String toString() {
        return "Player" + playerId +": Computer";
    }

}
