/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.model;
//TODO: write tests
//TODO: check scope of each function
//TODO: remove all sysouts

import edu.nyu.pqs.ps3.players.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ConnectFourModel class is the main computing agency that represents the board for the game. This contains the
 * insertion and winning logic and can be controlled by the action of Listners and Players.
 */

public class ConnectFourModel {

    private final int board[][];
    private final int numRows;
    private final int numCols;
    private final int tokensToWin;
    private Player currentPlayer;
    private List<Listner> listners = new ArrayList<>();
    private List<Player> players = new ArrayList<>();


    /**
     * Builder to initialize the model and input validations
     *
     * @throws IllegalArgumentException if input validations fail
     */
    public static class Builder {
        private int numRows = 6;
        private int numCols = 7;
        private int tokensToWin = 4;
        private int board[][];
        private int minNumCol = 4;
        private int maxNumCol = 20;
        private int minNumRow = 5;
        private int maxNumRows = 25;

        public Builder setNumRows(int val) {
            if (val < minNumRow || val > maxNumRows) {
                throw new IllegalArgumentException("Cannot have more than 25 or less than 5 rows");
            }
            numRows = val;
            return this;
        }

        public Builder setNumCols(int val) {
            if (val < minNumCol || val > maxNumCol) {
                throw new IllegalArgumentException("Cannot have more than 20 or less than 4 columns");
            }
            numCols = val;
            return this;
        }

        public Builder initWithBoard(int board[][]) {
            this.board = board;
            return this;
        }

        public Builder setTokensNeededToWin(int val) {
            tokensToWin = val;
            return this;
        }

        public ConnectFourModel build() {
            if (tokensToWin > numCols || tokensToWin > numRows)
                throw new IllegalArgumentException("Tokens to win cannot be more than no of cols or rows");
            return new ConnectFourModel(this);
        }
    }

    /**
     * The builder can either initialize the model with a predefined board or start with a new one
     *
     * @param builder Builder object to init parameters
     */
    private ConnectFourModel(Builder builder) {
        if (builder.board != null) {
            this.board = builder.board;
            this.numCols = board[0].length;
            this.numRows = board.length;
            this.tokensToWin = builder.tokensToWin;
        } else {
            this.numCols = builder.numCols;
            this.numRows = builder.numRows;
            this.board = new int[numRows][numCols];
            this.tokensToWin = builder.tokensToWin;
        }
    }

    /**
     * @return Number of rows of the board
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * @return Number of columns of the board
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * @return Number of tokens in a row required to win
     */
    public int getTokensToWin() {
        return tokensToWin;
    }

    /**
     * @return Current state of the board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Resets all values of the board to 0
     */
    public void resetBoard() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                board[row][col] = 0;
            }
        }

    }

    /**
     * Adds the new listner to the observers list
     *
     * @param listner listner to be added
     */
    public void attachListner(Listner listner) {
        listners.add(listner);
    }

    /**
     * Adds player to players ArrayList
     *
     * @param player Player built using the Player Factory
     * @throws NullPointerException if the player argument is null
     */
    public void addPlayers(Player player) {
        if (player == null) {
            throw new NullPointerException("Player cannot be null");
        }
        players.add(player);
    }

    /**
     * Used to remove all the attached players when starting new game
     */
    public void resetPlayers() {
        players.clear();
    }

    /**
     * Notifies all the observers that the game has started and they should initialize their board.
     * Sets the current player and notifies them to play
     */
    public void gameStart() {
        fireGameStart();
        fireInitBoard(numRows, numCols);
        currentPlayer = players.get(0);
        currentPlayer.play();
        firePlayerTurn(currentPlayer);
    }

    /**
     * Check the outcome of the last move. If the game if over then it notifies all observers to end,
     * else updates the current player and notifies them to play
     */
    private void nextMove() {
        Result outcome = getResult();
        if (outcome == Result.DRAW || outcome == Result.WIN)
            fireGameEnd(outcome, currentPlayer);
        else {
            updatePlayerTurn();
            currentPlayer.play();
            firePlayerTurn(currentPlayer);
        }
    }

    /**
     * The method obtains the id of the current player by searching for them in the player list
     * and updates the current player with the next consecutive player
     */
    public void updatePlayerTurn() {
        int index;
        for (index = 0; index < players.size(); index++) {
            if (players.get(index) == currentPlayer) {
                break;
            }
        }
        index = (index + 1) % players.size();
        currentPlayer = players.get(index);
    }

    /**
     * Checks if the column if full in the board
     *
     * @param column Column to check
     * @return If the column is full
     */
    private boolean columnIsFull(int column) {
        return board[0][column] != 0;
    }

    /**
     * Checks for the row to insert the token into and notifies all the observers that a token is placed.
     * If the column is full after insertion it broadcasts that the column is full.
     *
     * @param column Column to insert token into
     * @throws IllegalArgumentException if insertion happens in a full column
     */
    public void insertToken(int column) {
        if (columnIsFull(column)) {
            throw new IllegalArgumentException("Column already full");
        }

        int row = numRows - 1;
        while (board[row][column] != 0) {
            row--;
        }
        board[row][column] = currentPlayer.getPlayerId();
        fireAddTokenToColumn(row, column);
        if (columnIsFull(column))
            fireDisableAddingTokensTo(column);
        nextMove();
    }

    /**
     * Checks if adding a disc to a column, results in a win/draw/no_outcome.
     *
     * @return outcome of the operation
     */
    private Result getResult() {
        if (gameWon())
            return Result.WIN;
        if (gameDraw())
            return Result.DRAW;
        else
            return Result.NONE;
    }

    /**
     * Starting from the bottom row, it checks to see if the player has the required amount of
     * tokens in a row to win by checking in each directions(up, right and diagonal)
     *
     * @return true if tokensToWin in a row found, false otherwise
     */
    private boolean gameWon() {
        for (int row = numRows - 1; row > numRows - tokensToWin; row--) {
            for (int col = 0; col < numCols; col++) {
                if (board[row][col] != 0) {
                    int element = board[row][col];
                    if (checkVertically(row, col, element))
                        return true;
                    if (checkHorizontally(row, col, element))
                        return true;
                    if (checkDiagonally(row, col, element))
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
    private boolean checkVertically(int row, int col, int element) {
        int endRow = row - tokensToWin;
        for (int curr_row = row - 1; curr_row > endRow; curr_row--)
            if (board[curr_row][col] != element)
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
    private boolean checkHorizontally(int row, int col, int element) {
        if (col > numCols - tokensToWin)
            return false;
        int endCol = col + tokensToWin;
        for (int curr_col = col + 1; curr_col < endCol; curr_col++)
            if (board[row][curr_col] != element)
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
    private boolean checkDiagonally(int row, int col, int element) {
        int left = col - 1;
        int right = col + 1;
        int leftCount = 1;
        int rightCount = 1;
        for (int curr_row = row - 1; curr_row > row - tokensToWin; curr_row--) {
            if (left >= 0 && board[curr_row][left] == element)
                leftCount++;
            if (right < numCols && board[curr_row][right] == element)
                rightCount++;

            left--;
            right++;
        }
        if (leftCount == tokensToWin || rightCount == tokensToWin)
            return true;

        return false;
    }

    /**
     * If none of the positions of the board contain 0, it means the board is full
     *
     * @return False if any empty position found, true otherwise
     */
    private boolean gameDraw() {
        for (int[] row : board) {
            for (int element : row) {
                if (element == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Notifies all observers that the game has started
     */
    private void fireGameStart() {
        for (Listner listner : listners) {
            listner.gameStart();
        }
    }

    /**
     * Notifies all observers to build and initialize the board (rows*columns)
     */
    private void fireInitBoard(int rows, int columns) {
        for (Listner listner : listners) {
            listner.initBoard(rows, columns);
        }
    }

    /**
     * Notifies all observers that it is the currentPlayers turn to play
     *
     * @param currentPlayer Player's turn
     */
    private void firePlayerTurn(Player currentPlayer) {
        for (Listner listner : listners) {
            listner.playerTurn(currentPlayer);
        }
    }

    /**
     * Notifies all observers to add a token to the Position
     *
     * @param row    Row of the position to insert
     * @param column Column of the position to insert
     */
    private void fireAddTokenToColumn(int row, int column) {
        for (Listner listner : listners) {
            listner.addTokenToPosition(row, column, currentPlayer.getTokenColor());
        }
    }

    /**
     * Notifies all observers that the column is full
     *
     * @param column Column to disable
     */
    private void fireDisableAddingTokensTo(int column) {
        for (Listner listner : listners) {
            listner.disableAddingTokensTo(column);
        }
    }

    /**
     * Notifies all observers the the game has ended.
     *
     * @param outcome       Outcome of the last move
     * @param currentPlayer Player who made the last move
     */
    private void fireGameEnd(Result outcome, Player currentPlayer) {
        for (Listner listner : listners) {
            listner.gameEnd(outcome, currentPlayer);
        }
    }

    /**
     * Initialize the first player to begin the testing
     */
    void startTest() {
        currentPlayer = players.get(0);
    }

    /**
     * @return Generates result of the game move
     */
    int testWin() {
        if (gameWon()) {
            return currentPlayer.getPlayerId();
        } else return -1;
    }

    /**
     * Printing the board for testing
     */
    public void printBoard() {
        for (int[] row : board) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
