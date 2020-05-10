package edu.nyu.pqs.ps3.view;

import edu.nyu.pqs.ps3.model.ConnectFourController;
import edu.nyu.pqs.ps3.model.ConnectFourModel;
import edu.nyu.pqs.ps3.players.HumanPlayer;

import java.awt.*;

public class TestMain2 {
    private static ConnectFourModel model;
    private static ConnectFourView view;
    public static void main(String[] args) {
        int board[][] = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 2, 1, 1, 0},
                {0, 0, 0, 2, 2, 2, 1},
        };

        model = new ConnectFourModel.Builder().build();
        view = new ConnectFourView(new ConnectFourController(model));

    }
}
