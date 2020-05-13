package edu.nyu.pqs.ps3.model;
//TODO: make UI : choosing name and color
//TODO: make computerplayer
//TODO: write tests
//TODO: check scope of each function
//TODO: remove players from factory for round2
//TODO: fix otherplayerid
//TODO: give player choice for colors
//TODO: add players turn to introduction
import edu.nyu.pqs.ps3.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ConnectFourModel {

    private final int board [][];
    private final int numRows;

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getTokensToWin() {
        return tokensToWin;
    }

    private final int numCols;
    private final int tokensToWin;
    Player currentPlayer;
    List<Listner> listners = new ArrayList<>();
    List<Player> players = new ArrayList<>();

    public int[][] getBoard() {
        return board;
    }

    public static class Builder {
        //TODO: input validations and compatibility with no of tokens
        private int numRows = 6;
        private int numCols = 7;
        private int tokensToWin = 4;
        private int board[][];

        public Builder setNumRows(int val){
            numRows = val;
            return this;
        }
        public Builder setNumCols(int val){
            numCols = val;
            return this;
        }

        public Builder initWithBoard(int board[][]){
            this.board = board;
            return this;
        }

        public Builder setTokensNeededToWin(int val){
            if(val > numCols || val > numRows)
                throw new IllegalArgumentException("Tokens to win cannot be more than no of cols or rows");
            tokensToWin = val;
            return this;
        }
        public ConnectFourModel build(){
            return new ConnectFourModel(this);
        }
    }

    private ConnectFourModel(Builder builder){
        if(builder.board != null){
            this.board = builder.board;
            this.numCols = board[0].length;
            this.numRows = board.length;
            this.tokensToWin = builder.tokensToWin;
        }
        else {
            this.numCols = builder.numCols;
            this.numRows = builder.numRows;
            this.board = new int[numRows][numCols];
            this.tokensToWin = builder.tokensToWin;
        }
    }

    public void resetBoard(){
        for(int row = 0 ; row<numRows; row++)
            for(int col = 0; col < numCols; col++)
                board[row][col] = 0;

    }

    public void attachListner(Listner listner){
        listners.add(listner);
    }

    public void addPlayers(Player p){
        players.add(p);
    }

    public void gameStart(){
        fireGameStart();
        fireInitBoard(numRows, numCols);
        currentPlayer = players.get(0);
        currentPlayer.play();
        firePlayerTurn(currentPlayer);
    }

    private void nextMove(){
        Result outcome = getResult();
        if(outcome == Result.DRAW || outcome == Result.WIN)
            fireGameEnd(outcome, currentPlayer);
        else {
            updatePlayerTurn();
            currentPlayer.play();
            firePlayerTurn(currentPlayer);
        }
    }

    private Result getResult() {
        if(gameWon())
            return Result.WIN;
        if (gameDraw())
            return Result.DRAW;
        else
            return Result.NONE;
    }

    //make private
    boolean gameWon() {
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

    private boolean checkVertically(int row, int col, int element) {
        int endRow = row - tokensToWin;
        for(int curr_row = row-1; curr_row > endRow ; curr_row--)
            if(board[curr_row][col] != element)
                return false;
        return true;
    }
    private boolean checkHorizontally(int row, int col, int element) {
        if(col > numCols - tokensToWin)
            return false;
        int endCol = col + tokensToWin;
        for(int curr_col = col+1; curr_col < endCol ; curr_col++)
            if(board[row][curr_col] != element)
                return false;
        return true;
    }
    private boolean checkDiagonally(int row, int col, int element) {
        int left = col -1;
        int right = col +1;
        int leftCount=1;
        int rightCount=1;
        for(int curr_row = row-1; curr_row > row - tokensToWin ; curr_row--){
            if(left >= 0 && board[curr_row][left] == element)
                leftCount++;
            if(right < numCols && board[curr_row][right] == element)
                rightCount++;

            left--;
            right++;
        }
        if(leftCount == tokensToWin || rightCount == tokensToWin)
            return true;

        return false;
    }

    private boolean gameDraw() {
        for(int[] row: board){
            for(int element : row){
                if (element == 0)
                    return false;
            }
        }
        return true;
    }

    public void updatePlayerTurn() {
        int index;
        for(index=0; index< players.size();index++){
            if (players.get(index) == currentPlayer){
                break;
            }
        }
        index = (index+1)%players.size();
        currentPlayer = players.get(index);
    }

    public void insertToken(int column){
        System.out.println("insert called");
        if(columnIsFull(column)) {
            printBoard();
            System.out.println("col:"+column+"\nnumrows="+numRows);
            for(int a : board[numRows-1])
                System.out.print(a+" ");
            throw new IllegalArgumentException("Column already full");
        }

        int row = numRows -1;
        while (board[row][column] != 0) {
            row--;
        }
        board[row][column] = currentPlayer.getPlayerId();
        fireAddTokenToColumn(row, column);
        if(columnIsFull(column))
            fireDisableAddingTokensTo(column);
        nextMove();
    }

    private boolean columnIsFull(int column) {
        return  board[0][column] != 0;
    }

    private void fireGameStart() {
        for (Listner listner : listners){
            listner.gameStart();
        }
    }

    private void fireInitBoard(int rows, int columns) {
        for (Listner listner : listners){
            listner.initBoard(rows, columns);
        }
    }

    private void firePlayerTurn(Player currentPlayer) {
        for (Listner listner : listners){
            listner.playerTurn(currentPlayer);
        }
    }

    private void fireAddTokenToColumn(int row, int column) {
        for (Listner listner : listners){
            listner.addTokenToPosition(row, column, currentPlayer.getTokenColor());
        }
    }

    private void fireDisableAddingTokensTo(int column) {
        for (Listner listner : listners){
            listner.disableAddingTokensTo(column);
        }
    }

    private void fireGameEnd(Result outcome, Player currentPlayer) {
        System.out.println("Game "+ outcome);
        printBoard();
        for (Listner listner : listners){
            listner.gameEnd(outcome, currentPlayer);
        }
    }

    public void startTest(){
        currentPlayer = players.get(0);
    }
    int testWin(){
        if(gameWon()){
            return currentPlayer.getPlayerId();
        }
        else return -1;
    }
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
