/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.view;

import edu.nyu.pqs.ps3.model.ConnectFourController;
import edu.nyu.pqs.ps3.model.ConnectFourModel;

public class App {
    private static ConnectFourModel model;
    private static ConnectFourView view;

    public static void main(String[] args) {

        model = new ConnectFourModel.Builder().build();
        view = new ConnectFourView(new ConnectFourController(model));

    }
}
