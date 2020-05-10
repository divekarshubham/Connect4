package edu.nyu.pqs.ps3.has.app;

/**
 * Controller makes changes to the model based on action made by the observer in view
 */
public class ConnectFourController {
    ConnectFourModel model;

    /**
     * ConnectFour controller takes model as parameter, generally view is also created in the controller, but in this case
     * as view is not updated by controller it has not been created in the controller
     * @param model
     */
    public ConnectFourController(ConnectFourModel model){
        if(model == null){
            throw new NullPointerException("model cannot be null");
        }
        this.model = model;
    }

    /**
     * Prepares model to start 2 player game
     */
    public void startTwoPlayerGame(){
        model.initialize();
        model.createGrid();
        Player player1 = PlayerFactory.buildPlayer(PlayerType.HUMANPLAYER, model.generatePlayerId(), model.gerenaratePlayerColor(), model);
        Player player2 = PlayerFactory.buildPlayer(PlayerType.HUMANPLAYER, model.generatePlayerId(), model.gerenaratePlayerColor(), model);
        model.addPlayer(player1);
        model.addPlayer(player2);
        model.startGame();
    }

    /**
     * Prepares model to start 1 player game
     */
    public void startOnePlayerGame(){
        model.initialize();
        model.createGrid();
        Player player1 = PlayerFactory.buildPlayer(PlayerType.COMPUTERPLAYER, model.generatePlayerId(), model.gerenaratePlayerColor(), model);
        Player player2 = PlayerFactory.buildPlayer(PlayerType.HUMANPLAYER, model.generatePlayerId(), model.gerenaratePlayerColor(), model);
        model.addPlayer(player1);
        model.addPlayer(player2);
        model.startGame();
    }

    /**
     * Notifies the model, move made by the observer(human player)
     */
    public void makeMove(int position){
        model.makeMove(position);
    }
}
