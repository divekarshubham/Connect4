package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;
import edu.nyu.pqs.ps3.model.Result;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ComputerPlayerTest {

    @Before
    public void setUp() throws Exception {
        PlayerFactory.clearPlayers();
    }

    @Test
    public void init_ComputerPlayer() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters params = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.CYAN, params);
        model.addPlayers(player);
        assertTrue(model.getPlayers().size() == 1);
        assertTrue(model.getPlayers().get(0).getPlayerId() == 1);
        assertTrue(model.getPlayers().get(0).getTokenColor() == Color.CYAN);
        assertTrue(player instanceof ComputerPlayer);

    }

    @Test
    public void ComputerPlayer_toString() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters params = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.CYAN, params);
        assertTrue(player instanceof ComputerPlayer);
        assertTrue(player.toString().contains("Computer"));

    }

    @Test
    public void testPlay(){
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters params = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.CYAN, params);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        model.addPlayers(player1);
        model.addPlayers(player2);

        model.gameStart();
        int tokens = 0;
        for(int j=0; j< model.getNumCols(); j++)
            if(model.getBoard()[5][j] == player1.getPlayerId())
                tokens++;

        assertEquals(1, tokens);

    }

    @Test
    public void testPlay_horizontalWin(){
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 2, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 1, 1, 1, 2},  //board[5][2] should become 1
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        AIParameters params = new AIParameters(AIDifficulty.HARD, 2, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.CYAN, params);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        ComputerPlayer cp = (ComputerPlayer)player1;
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.gameStart();
        assertEquals(cp.checkGameStatus(model.getBoard()), Result.WIN);
        assertEquals(1, model.getBoard()[5][2]);
    }

    @Test
    public void testPlay_verticalWin(){
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0},  //board[2][4] should become 1
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 2},
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        AIParameters params = new AIParameters(AIDifficulty.EASY, 2, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.CYAN, params);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        ComputerPlayer cp = (ComputerPlayer)player1;
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.gameStart();
        assertEquals(cp.checkGameStatus(model.getBoard()), Result.WIN);
        assertEquals(1, model.getBoard()[2][4]);
    }

    @Test
    public void testPlay_diagonalWin(){
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 2, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 2},  //board[5][2] should become 2
        };

        ConnectFourModel model = new ConnectFourModel.Builder().initWithBoard(board).build();
        AIParameters params = new AIParameters(AIDifficulty.EASY, 1, model);
        Player player2 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", null, null);
        Player player1 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.CYAN, params);
        ComputerPlayer cp = (ComputerPlayer)player1;
        model.addPlayers(player1);
        model.addPlayers(player2);
        model.gameStart();
        assertEquals(cp.checkGameStatus(model.getBoard()), Result.WIN);
        assertEquals(2, model.getBoard()[5][2]);
    }

}