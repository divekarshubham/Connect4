/**
 * This code is submission of pqs Assignment1 to implement address Book Library
 *
 * @author  Himani Shah (has482)
 */
package edu.nyu.pqs.ps3.has.app;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * ConnectFourModel class acts like model for the application. It has all the game and winning logic. It drives the game
 * based on the action performed by the view.
 */
public class ConnectFourModel{
    private int[][] internalGrid;
    private ArrayList<GameListener> listeners= new ArrayList();
    private ArrayList<Player> players;
    private Player currentPlayer;
    private int playerId;
    private int gridNoOfRows;
    private int gridNoOfColumns;
    private int needToWin;

    /*
     * The Outcome of the game can be WIN, DRAW or NO_OUTCOME after each move.
     */
    enum Outcome {
        WIN,
        DRAW,
        NO_OUTCOME
    }

    /**
     * Returns the current board state.
     * @return current board state
     */
     int[][] getInternalGrid() {
        return internalGrid;
    }

    /**
     * The Constructor, initializes model
     */
    public ConnectFourModel(Builder builder){
        listeners = new ArrayList();
        this.gridNoOfRows = builder.gridNoOfRows;
        this.gridNoOfColumns = builder.gridNoOfColumns;
        this.needToWin = builder.needToWin;
    }

    public static class Builder {
        private int gridNoOfColumns = 7;
        private int gridNoOfRows = 6;
        private int needToWin = 4;

        public Builder noOfColumns(int column) {
            if (column < 4 || column> 20) {
                throw new IllegalArgumentException("Cannot have more than 20 or less than 4 columns");
            }
            this.gridNoOfColumns = column;
            return this;
        }

        public Builder noOfRows(int rows) {
            if (rows < 4 || rows> 20) {
                throw new IllegalArgumentException("Cannot have more than 20 or less than 4 rows");
            }
            this.gridNoOfRows = rows;
            return this;
        }

        public Builder inARowToWin(int win) {
            if (win < 3 || win > 10) {
                throw new IllegalArgumentException("Must be between 3 and 10");
            }
            needToWin = win;
            return this;
        }

        public ConnectFourModel build() {
            return new ConnectFourModel(this);
        }
    }

    /**
     * Only one instance of model can be created and used by all the observers.
     * @return ConnectFourModel instance
     */
//    public static ConnectFourModel getInstance() {
//        if (single_instance == null)
//            single_instance = new ConnectFourModel();
//        return single_instance;
//    }

    /**
     * Initialize the game, by initializing the board, player start id and players ArrayList
     * initializes listeners arraylist, board row, columns, internal board and discs in a row to win the game.
     */
     void initialize() {
        internalGrid = new int[gridNoOfRows][gridNoOfColumns];
        for (int i = 0; i < gridNoOfRows; i++) {
            for (int j = 0; j < gridNoOfColumns; j++) {
                internalGrid[i][j] = -1;
            }
        }
        playerId = 1;
        players = new ArrayList();
    }

    /**
     * Adds player to players ArrayList, this method build a player object using PLayerFactory class
     * @param player player type (COMPUTERPLAYER/HUMANPLAYER)
     * @throws NullPointerException if the playerType argument is null
     */
     void addPlayer(Player player) {
        if(player==null){
            throw new NullPointerException("players cannot be null");
        }
        players.add(player);
    }

    /**
     * Starts the game by setting the current player, to 1st player in the array list, notifies the observer that the game has started,
     * notifies the observer about the current players turn
     * Lets the current player play
     */
     void startGame() {
        setCurrentPlayer(players.get(0));
        eventStartGame();
        eventYourTurn(currentPlayer);
        currentPlayer.play();
    }

    /**
     * Makes the move based on the action performed/chosen by player.
     * It gets the row position for the corresponding column where the disc is added
     * Updates the observer with new grid with updated move
     * Disables an action button, if any of the column is full.
     * Generates outcome of the move
     *
     * @param column disc column position
     */
     void makeMove(int column){
        if(column<0 || column>gridNoOfColumns){
            throw new IllegalArgumentException("column not of range");
        }
        int row = addCoinGetRowPosition(column);
        eventAddDisc(row, column);
        if(checkIfColumnFull(column)) {
            eventDisableFullColumn(column);
        }
        generateOutcomeAndNextStep();
    }

    /*
     * Generates outcome based on the move performed by the player.
     *
     * If the move results in win, observers are notified that they won, game stops and player is asked if they want to play again.
     * If the move results in draw, observers are notified that there was a draw, game stops and player is asked if they want to play again.
     * If the move results in no result, observers are notified with next turn and model waits for player to play
     *
     * @throws NullPointerException If outcome is null
     */
    private void generateOutcomeAndNextStep() {
        Outcome outcome = checkResult();
        if(outcome == Outcome.WIN){
            eventWinGame();
        }
        else if(outcome == ConnectFourModel.Outcome.DRAW){
            eventDrawGame();
        }
        else {
            UpdateTurn();
            eventYourTurn(currentPlayer);
            currentPlayer.play();
        }
    }

    /**
     * creates the board
     */
    public void createGrid() {
        eventMakeBoard();
    }

    /**
     * Checks if the column is full
     * @param column column position
     * @return if column is full
     */
     boolean checkIfColumnFull(int column) {
        return internalGrid[0][column] != -1;
    }

    /**
     * Updates turn based on the previous players turn.
     * @throws IllegalArgumentException if previous player is not in the players list
     */
     void UpdateTurn() {
        for(int i=0; i<players.size(); i++){
            if(currentPlayer == players.get(i)){
                if(i == players.size()-1){
                    setCurrentPlayer(players.get(0));
                    break;
                }
                else {
                    setCurrentPlayer(players.get(i+1));
                    break;
                }
            }
        }
    }

    /*
     * Auto generates player id
     */
     int generatePlayerId(){
        return playerId++;
    }

    /*
     * Autogenerates player color
     */
     Color gerenaratePlayerColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

    /**
     * Registers observers/listeners
     * @param listener
     */
    public void registerListener(GameListener listener) {
        if(listener == null){
            throw new NullPointerException("listener cannot be null");
        }
        listeners.add(listener);
    }

    /**
     * Unregisters observers/listeners
     * @param listener
     */
    public void unregisterListener(GameListener listener) {
        if (listeners.isEmpty()) {
            throw new NullPointerException("Observers list is empty");
        }
        if (!listeners.contains(listener)) {
            throw new IllegalArgumentException("This listener is not in the list of observers");
        }
        listeners.remove(listener);
    }

    /**
     * get current player object
     * @return current player object
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Set current player
     * @param currentPlayer current player object
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @return Number of rows in the board
     */
    public int getGridNoOfRows() {
        return gridNoOfRows;
    }

    /**
     * @return Number of columns in the board
     */
    public int getGridNoOfColumns() {
        return gridNoOfColumns;
    }

    /**
     * @return Number of discs in a row to win
     */
    public int getNeedToWin() {
        return needToWin;
    }

    /**
     * @return playerid
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * @return observers list
     */
    public ArrayList<GameListener> getListeners() {
        return listeners;
    }

    /**
     * @return players list
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Sets internal grid
     * @param internalGrid
     */
    void setInternalGrid(int[][] internalGrid) {
        this.internalGrid = internalGrid;
    }

    /**
     * Set player id
     * @param playerId
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /*
     * Add a Disc to a particular column and get the corresponding row position
     * @throws IllegalArgumentException if column position is out of range
     * @throws IllegalArgumentException if we try to add a disc in column that is full
     * @returns corresponding row position.
     */
     int addCoinGetRowPosition(int column) {
        if(column<0 || column>=gridNoOfColumns){
            throw new IllegalArgumentException("Column position out of range");
        }
        for (int row = gridNoOfRows-1; row >= 0; row--) {
            if (internalGrid[row][column] == -1) {
                internalGrid[row][column] = currentPlayer.getPlayerId();
                return row;
            }
        }
        throw new IllegalArgumentException("No space in this column");
    }

    /*
     * Checks if adding a disc to a column, results in a win/draw/no_outcome. Need to check all the player positions in the grid to
     * handle the situation where the 3 discs are in a row with one missing space in between.
     *
     * @return True/False if the move would result in a win
     */
    Outcome checkResult() {
        if(isDraw()) return Outcome.DRAW;

        for (int i = 0; i < gridNoOfRows; i++) {
            for (int j = 0; j < gridNoOfColumns; j++) {
                if (isWin(i, j)) {
                    return Outcome.WIN;
                }
            }
        }
        return Outcome.NO_OUTCOME;
    }

    /*
     * If there are no cells available in the grid and there was no winner, it results in a draw
     */
     boolean isDraw() {
        for (int i = 0; i < gridNoOfColumns; i++) {
            if (internalGrid[0][i] == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if players disc at a particular position in the grid results in a win.
     * We check vertiacally upwards, horizontally right, diagonally left upwards and diagonally right upwards.
     * We dont need to check for other scenarios as we check at each position in the grid.
     *
     * @param row row position
     * @param column column position
     * @return True/False, if disc in that position, results in a win.
     */
     boolean isWin(int row, int column) {
        if (checkVertical(row, column)) return true;
        if (checkHorizontal(row, column)) return true;
        if (checkDiagonallyLeft(row, column)) return true;
        if (checkDiagonallyRight(row, column)) return true;
        return false;
    }

    /*
     * Checks vertically if position results in a win
     */
     boolean checkVertical(int row, int column) {
        if (row <= gridNoOfRows-needToWin) {
            for (int i = 0; i < needToWin; i++) {
                if (internalGrid[row + i][column] != currentPlayer.getPlayerId()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Checks horizontally if position results in a win
     */
     boolean checkHorizontal(int row, int column) {
        if (column <= gridNoOfColumns-needToWin) {
            for (int i = 0; i < needToWin; i++) {
                if (internalGrid[row][column + i] != currentPlayer.getPlayerId()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Checks diagonally left if position results in a win
     */
     boolean checkDiagonallyLeft(int row, int column) {
        if ((row - (needToWin-1) >= 0) && (column - (needToWin-1) >= 0)) {
            for (int i = 0; i < needToWin; i++) {
                if (internalGrid[row - i][column - i] != currentPlayer.getPlayerId()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Checks diagonally right if position results in a win
     */
     boolean checkDiagonallyRight(int row, int column) {
        if ((row - (needToWin-1) >= 0) && (column + (needToWin-1) <= gridNoOfColumns-1)) {
            for (int i = 0; i < needToWin; i++) {
                if (internalGrid[row - i][column + i] != currentPlayer.getPlayerId()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Disables column in the observer if it is full
     */
    void eventDisableFullColumn(int columnposition) {
        for(GameListener listener: listeners){
            listener.disableAddDiscButton(columnposition);
        }
    }

    /*
     * Notifies the observers that the game is a draw, enables options to play again and disables action buttons to play the game.
     */
     void eventDrawGame() {
        for(GameListener listener: listeners){
            listener.draw();
            listener.enableOptions();
            listener.diableAllDiscAddButton();
        }
    }

    /*
     * Notifies the observers that the game is a win, displays winning player
     * enables options to play again and disables action buttons to play the game.
     */
     void eventWinGame() {
        for(GameListener listener: listeners){
            listener.youWon(currentPlayer);
            listener.enableOptions();
            listener.diableAllDiscAddButton();
        }
    }

    /*
     * notifies next turn to observers
     */
     void eventYourTurn(Player player) {
        for(GameListener listener: listeners){
            listener.yourTurn(player);
        }
    }

    /*
     * Notifies the observers that the game has started
     */
     void eventStartGame() {
        for(GameListener listener: listeners){
            listener.gameStart();
        }
    }

    /*
     * Updates the board with the latest move
     */
     void eventAddDisc(int rowposition, int columnposition) {
        for(GameListener listener: listeners){
            listener.addDiscToSlot(currentPlayer.getCoinColor(), columnposition, rowposition);
        }
    }

    /*
     * creates board for all the observers
     */
     void eventMakeBoard() {
        for(GameListener listener: listeners){
            listener.makeBoard(gridNoOfRows,gridNoOfColumns);
            listener.disableOptions();
        }
    }

}
