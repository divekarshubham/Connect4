package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.HumanPlayer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ConnectFourModelTest {

    private ConnectFourModel model;
    @Before
    public void setUp() throws Exception {
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 2, 2, 1},
        };

        model = new ConnectFourModel.Builder().initWithBoard(board).build();
        model.addPlayers(new HumanPlayer(1,"SD", Color.RED));
        model.addPlayers(new HumanPlayer(2,"AMS", Color.YELLOW));
        model.startTest();
        model.printBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insertToken() {
        model.insertToken(1);
        model.insertToken(2);
        model.insertToken(3);
        model.insertToken(4);
        model.printBoard();
    }

    @Test
    public void checkForVerticalVictory() {
        model.updatePlayerTurn();
        model.insertToken(3); //p2
        model.insertToken(3);   //p2
        model.printBoard();
        Assert.assertEquals(2, model.testWin());
    }

    @Test
    public void checkForHorizontalVictory() {
        model.updatePlayerTurn();
        //model.insertToken(1); //p2
        model.insertToken(2);//p2
        model.printBoard();
        Assert.assertEquals(2, model.testWin());
    }

    @Test
    public void checkForDiagonalVictory() {
        model.insertToken(3); //p1
        model.insertToken(3); //p1
        model.printBoard();
        Assert.assertEquals(1, model.testWin());
    }
}