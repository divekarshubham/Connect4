package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;
import edu.nyu.pqs.ps3.model.Result;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ComputerPlayer implements Player{
    int playerId;
    String playerName;
    Color playerColor;
    ConnectFourModel model;
    int board[][];
    int numRows;
    int numCols;
    int tokensToWin;
    int otherPlayerId;
    AIDifficulty difficulty;
    private  int[][] evaluationTable = {{3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3}};
    private int maxDepth = 10;

    public ComputerPlayer(int playerId, String playerName, Color playerColor, ConnectFourModel model, int otherPlayerId, AIDifficulty difficulty){
        this.playerId = playerId;
        this.playerName = playerName;
        this. playerColor = playerColor;
        this.model = model;
        numCols = model.getNumCols();
        numRows = model.getNumRows();
        tokensToWin = model.getTokensToWin();
        this.otherPlayerId = otherPlayerId;
        this.difficulty = difficulty;
    }

    private int[][] generateBoardCopy(int[][] board){
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
       if(difficulty == AIDifficulty.EASY)
           model.insertToken(new Random().nextInt(numCols-1));
       else {
           int col = minimax(generateBoardCopy(model.getBoard()), maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 0).getCol();
           model.insertToken(col);
       }


    }

    private Pair minimax(int[][] board,int depth,int alpha,int beta, boolean maximizingPlayer, int column){
        if(depth == 0)
            return new Pair(column, evaluateBoard(board));
        Result result = checkGameStatus(board);
        if(result == Result.WIN){
            //printBoard(board);
            //System.out.println("maxim: "+maximizingPlayer);
            if(maximizingPlayer)
                return new Pair(column, -10000);
            else
                return new Pair(column, 10000);
        }
        else if(result == Result.DRAW)
            return new Pair(column, 0);

        int retcol=column;
        if(maximizingPlayer){
            int maxEval = Integer.MIN_VALUE;
            for(int col=0; col< numCols; col++){
                int[][] newBoard = insertInBoard(col, board, playerId);
                int eval = minimax(newBoard, depth-1, alpha, beta, false, col).getVal();
                if(maxEval < eval) {
                    maxEval = eval;
                    retcol = col;
                }
                alpha = alpha > eval ? alpha : eval;
                if(beta<= alpha)
                    break;
            }
            return new Pair(retcol, maxEval);
        }else {
            int minEval = Integer.MAX_VALUE;
            for(int col=0; col< numCols; col++){
                int[][] newBoard = insertInBoard(col, board, otherPlayerId);
                int eval = minimax(newBoard, depth-1, alpha, beta, true, col).getVal();
                if(minEval > eval) {
                    minEval = eval;
                    retcol = col;
                }
                beta = beta < eval ? beta : eval;
                if(beta<= alpha)
                    break;
            }
            return new Pair(retcol, minEval);
        }
    }

    private int evaluateBoard(int[][] newBoard){
        if (difficulty == AIDifficulty.MEDIUM)
            return evaluateBoard_static(newBoard);
        return  evaluateBoard_center(newBoard);
    }

    private int evaluateBoard_center(int [][] newBoard) {
        int score = longest_chain(newBoard) * 10;
        int center = numCols/2;
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                if(newBoard[row][col] != 0){
                    //TODO: check this +/-
                    if(newBoard[row][col] == playerId)
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
                if(newBoard[row][col] != 0){
                    int temp = maxLengthFromCell(newBoard, row, col);
                    longest = longest > temp ? longest : temp;
                }
        return longest;
    }

    private int maxLengthFromCell(int[][] newBoard, int row, int col) {
        List<Integer> directions = new ArrayList<>();
        directions.add(chainLength(newBoard, row, col, new Pair(1,1)) + chainLength(newBoard, row, col, new Pair(-1,-1)) + 1 );
        directions.add(chainLength(newBoard, row, col, new Pair(1,0)) + chainLength(newBoard, row, col, new Pair(-1,0)) + 1 );
        directions.add(chainLength(newBoard, row, col, new Pair(0,1)) + chainLength(newBoard, row, col, new Pair(0,-1)) + 1 );
        directions.add(chainLength(newBoard, row, col, new Pair(-1,1)) + chainLength(newBoard, row, col, new Pair(1,-1)) + 1 );

        return Collections.max(directions);
    }

    private int chainLength(int[][] newBoard, int row, int col, Pair direction) {
        int count = 0;
        while (row >= 0 && row < numRows && col >= 0 && col < numCols && newBoard[row][col]==playerId){
            row += direction.getCol();
            col += direction.getVal();
            count++;
        }
        return count - 1;
    }


    private int[][] insertInBoard(int col, int[][] board, int id) {
        //System.out.println(col+" "+id);
        int[][] newBoard = generateBoardCopy(board);
        for(int row = numRows - 1; row >=0;row--)
            if(newBoard[row][col] == 0) {
                newBoard[row][col] = id;
                break;
            }

        return newBoard;
    }

    private int evaluateBoard_static(int [][] newBoard) {
        int utility = 138;
        int sum = 0;
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                if(newBoard[i][j] != 0){
                    if (newBoard[i][j] == playerId)
                        sum += evaluationTable[i][j];
                    else
                        sum -= evaluationTable[i][j];
                }
        return utility + sum;
    }

    private Result checkGameStatus(int [][] newBoard) {
        if (gameDraw(newBoard))
            return Result.DRAW;
        else if(gameWon(newBoard))
            return Result.WIN;
        return Result.NONE;
    }

    boolean gameWon(int [][] newBoard) {
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

    private boolean checkVertically(int [][] newBoard, int row, int col, int element) {
        int endRow = row - tokensToWin;
        for(int curr_row = row-1; curr_row > endRow ; curr_row--)
            if(newBoard[curr_row][col] != element)
                return false;
        return true;
    }
    private boolean checkHorizontally(int [][] newBoard, int row, int col, int element) {
        if(col > numCols - tokensToWin)
            return false;
        int endCol = col + tokensToWin;
        for(int curr_col = col+1; curr_col < endCol ; curr_col++)
            if(newBoard[row][curr_col] != element)
                return false;
        return true;
    }
    private boolean checkDiagonally(int [][] newBoard, int row, int col, int element) {
        int left = col -1;
        int right = col +1;
        int leftCount=1;
        int rightCount=1;
        for(int curr_row = row-1; curr_row > row - tokensToWin ; curr_row--){
            if(left >= 0 && newBoard[curr_row][left] == element)
                leftCount++;
            if(right < numCols && newBoard[curr_row][right] == element)
                rightCount++;

            left--;
            right++;
        }
        if(leftCount == tokensToWin || rightCount == tokensToWin)
            return true;

        return false;
    }

    private boolean gameDraw(int [][] newBoard) {
        for(int[] row: newBoard){
            for(int element : row){
                if (element == 0)
                    return false;
            }
        }
        return true;
    }

    private void printBoard(int[][] b){
        for (int[] row : b) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    public class Pair {
        private final int col;
        private final int val;

        public Pair(int col, int val) {
            this.col = col;
            this.val = val;
        }

        public int getCol(){ return col;}
        public int getVal(){ return val;}
    }
}
