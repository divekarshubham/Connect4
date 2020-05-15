package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.*;
import edu.nyu.pqs.ps3.view.ConnectFourView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.net.http.WebSocket;

import static org.junit.Assert.*;

public class ConnectFourModelTest {

    private ConnectFourModel model;
    @Before
    public void setUp() throws Exception {
        PlayerFactory.clearPlayers();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectFourBuilder_rowOutOfRangeLow() {
        new ConnectFourModel.Builder().setNumRows(1).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectFourBuilder_rowOutOfRangeHigh() {
        new ConnectFourModel.Builder().setNumRows(26).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectFourBuilder_columnOutOfRangeLow() {
        new ConnectFourModel.Builder().setNumCols(-10).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectFourBuilder_columnOutOfRangeHigh() {
        new ConnectFourModel.Builder().setNumCols(21).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectFourBuilder_tokensToWinRangeHigh() {
        new ConnectFourModel.Builder().setTokensNeededToWin(21).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectFourBuilder_tokensToWinRangeLow() {
        new ConnectFourModel.Builder().setTokensNeededToWin(-1).build();
    }

    @Test
    public void testConnectFourBuilder_rowCorrect() {
        ConnectFourModel model = new ConnectFourModel.Builder().setNumRows(8).build();
        assertEquals(model.getNumRows(), 8);
    }

    @Test
    public void testConnectFourBuilder_columnCorrect() {
        ConnectFourModel model = new ConnectFourModel.Builder().setNumCols(7).build();
        assertEquals(model.getNumCols(), 7);
    }

    @Test
    public void testConnectFourBuilder_winCorrect() {
        ConnectFourModel model = new ConnectFourModel.Builder().setTokensNeededToWin(5).build();
        assertEquals(model.getTokensToWin(), 5);
    }

    @Test
    public void initialize_testGridLengthAndInitializationDefault() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        int[][] grid = model.getBoard();
        assertEquals(grid.length, 6);
        assertEquals(grid[0].length, 7);
        for(int i=0; i< grid.length; i++){
            for(int j=0;j<grid[0].length;j++){
                assertEquals(grid[i][j], 0);
            }
        }
    }

    @Test
    public void initialize_testGridLengthAndInitializationOther() {
        ConnectFourModel model = new ConnectFourModel.Builder().setNumRows(5).setNumCols(6).build();
        int[][] grid = model.getBoard();
        assertEquals(grid.length, 5);
        assertEquals(grid[0].length, 6);
        for(int i=0; i< grid.length; i++){
            for(int j=0;j<grid[0].length;j++){
                assertEquals(grid[i][j], 0);
            }
        }
    }

    @Test(expected = NullPointerException.class )
    public void addPlayer_checkPlayerNull() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        model.addPlayers(null);
    }

    @Test
    public void addPlayer_HumanPlayer() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        assertTrue(model.getPlayers().size() == 1);
        assertTrue(model.getPlayers().get(0).getPlayerId() == 1);
        assertTrue(model.getPlayers().get(0).getTokenColor() != null);
    }

    @Test
    public void addPlayer_ComputerPlayer() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters params = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Kamputer", null, params);
        model.addPlayers(player1);
        assertTrue(model.getPlayers().size() == 1);
        assertTrue(model.getPlayers().get(0).getPlayerId() == 1);
        assertTrue(model.getPlayers().get(0).getTokenColor() != null);
    }

    @Test
    public void addPlayers_MultiplePlayer() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        PlayerFactory.setMaxPlayers(4);
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN,"Jane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Bane Doe", null, null);
        Player player3 = PlayerFactory.getPlayer(PlayerType.HUMAN,"Lane Doe", null, null);
        Player player4 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Kane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.addPlayers(player3);
        model.addPlayers(player4);
        assertTrue(model.getPlayers().size() == 4);
        assertTrue(model.getPlayers().get(0).getPlayerId() == 1);
        assertTrue(model.getPlayers().get(1).getPlayerId() == 2);
        assertTrue(model.getPlayers().get(2).getPlayerId() == 3);
        assertTrue(model.getPlayers().get(3).getPlayerId() == 4);
        assertTrue(model.getPlayers().get(0).getTokenColor() != null);
    }

    @Test
    public void testInsertToken_checkMoveAddition() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters params = new AIParameters(AIDifficulty.EASY, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Kamputer", null, params);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN,"Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.gameStart();
        model.insertToken(3);
        model.insertToken(3);
        model.insertToken(3);
        model.insertToken(3);
        assertTrue(model.getBoard()[5][3] != 0);
        assertTrue(model.getBoard()[4][3] != 0);
        assertTrue(model.getBoard()[3][3] != 0);
        assertTrue(model.getBoard()[2][3] != 0);

        int sum =0;
        for(int i =0 ; i< model.getNumRows();i++){
            for(int j=0;j<model.getNumCols();j++){
                if(model.getBoard()[i][j] != 0){
                    sum++;
                }
            }
        }

        assertEquals(8, sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertToken_checkInvalidColumn() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters params = new AIParameters(AIDifficulty.EASY, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Kamputer", null, params);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.gameStart();
        model.insertToken(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertToken_checkColumnFull() {
        ConnectFourModel model = new ConnectFourModel.Builder().setNumRows(5).build();
        AIParameters params = new AIParameters(AIDifficulty.EASY, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.gameStart();
        model.insertToken(0);
        model.insertToken(0);
        model.insertToken(0);
        model.insertToken(0);
        model.insertToken(0);
        model.insertToken(0);
    }

    @Test
    public void testInsertToken_checkAlternating() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters params = new AIParameters(AIDifficulty.EASY, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.gameStart();
        model.insertToken(0);
        model.insertToken(0);
        model.insertToken(0);
        model.insertToken(0);
        assertEquals(model.getBoard()[5][0], model.getBoard()[3][0]);
        assertEquals(model.getBoard()[4][0], model.getBoard()[2][0]);
        assertNotEquals(model.getBoard()[5][0], model.getBoard()[4][0]);
    }

    @Test
    public void checkResults_win() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        int grid[][] = model.getBoard();
        grid[5][2] = 1;
        grid[4][2] = 1;
        grid[3][2] = 1;
        grid[2][2] = 1;
        assertEquals(model.getResult(), Result.WIN);
    }

    @Test
    public void checkResults_notWin() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        int grid[][] = model.getBoard();
        grid[5][2] = 1;
        grid[4][2] = 1;
        grid[3][2] = 2;
        grid[2][2] = 1;
        assertNotEquals(model.getResult(), Result.WIN);
    }

    @Test
    public void checkResults_noresult() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        int grid[][] = model.getBoard();
        grid[5][2] = 1;
        grid[4][2] = 1;
        grid[3][2] = 1;
        assertEquals(model.getResult(), Result.NONE);
    }

    @Test
    public void checkResults_draw() {
        ConnectFourModel model = new ConnectFourModel.Builder().setNumRows(5).setNumCols(4).build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        int grid[][] = model.getBoard();
        grid[0][0] = 1;
        grid[0][1] = 1;
        grid[0][2] = 1;
        grid[0][3] = 1;
        assertEquals(model.getResult(), Result.DRAW);
    }

    @Test
    public void checkResults_notDraw() {
        ConnectFourModel model = new ConnectFourModel.Builder().setNumRows(5).setNumCols(4).build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        int grid[][] = model.getBoard();
        grid[0][0] = 1;
        grid[0][1] = 0;
        grid[0][2] = 1;
        grid[0][3] = 1;
        assertNotEquals(model.getResult(), Result.DRAW);
    }
    @Test
    public void checkResults_withCustomBoard() {
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 2, 2, 1},
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        assertEquals(model.getResult(), Result.NONE);
    }

    @Test
    public void checkResults_winVertical() {
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 2, 2, 1},
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        model.insertToken(5);
        assertEquals(model.getResult(), Result.WIN);
    }

    @Test
    public void checkResults_winHorizontal() {
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 2, 2, 1},
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        model.insertToken(0);
        model.insertToken(2);
        assertEquals(model.getResult(), Result.WIN);
    }

    @Test
    public void checkResults_winDiagonalLeft() {
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 2, 2, 1},
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        model.insertToken(0);
        model.insertToken(3);
        model.insertToken(3);
        assertEquals(model.getResult(), Result.WIN);
    }

    @Test
    public void checkResults_winDiagonalRight() {
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, 2, 2},
                {0, 0, 0, 1, 1, 1, 2},
                {0, 0, 0, 2, 2, 2, 1},
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Pane Doe", null, null);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.startTest();
        model.insertToken(2);
        assertEquals(model.getResult(), Result.WIN);
    }


}