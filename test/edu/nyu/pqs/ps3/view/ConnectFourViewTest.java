package edu.nyu.pqs.ps3.view;

import edu.nyu.pqs.ps3.model.ConnectFourController;
import edu.nyu.pqs.ps3.model.ConnectFourModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectFourViewTest {
    private ConnectFourModel model;
    private ConnectFourView view;
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

        view = new ConnectFourView(new ConnectFourController(model));
        view.initializeGame();
    }
    @Test
    public void makeBoard() {
        view.initBoard(6,7);
    }
}