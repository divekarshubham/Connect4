package edu.nyu.pqs.ps3.has.view;

import edu.nyu.pqs.ps3.has.app.ConnectFourController;
import edu.nyu.pqs.ps3.has.app.ConnectFourModel;

/**
     * This is the entry point for the Application to run. The instance of model is created and is passed to all the views.
     * All the views have the same instance of the model. Observer pattern has been used, hence all the views have the same
     * state at after every action performed.
     */
public class App {
    public static void main(String args[]) {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        ConnectFourController controller = new ConnectFourController(model);
        new ConnectFourGameView(model, controller);
        new ConnectFourGameView(model, controller);
        new ConnectFourGameView(model, controller);
        new ConnectFourGameView(model, controller);
    }
}

