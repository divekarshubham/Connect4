/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
//TODO: get players from factory
package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.*;

/**
 * Controller class that serves as a medium for communication between the view and the model.
 * The controller is passed to the view and it helps pass the signals back to the model and
 * provides necessary mediations.
 */
public class ConnectFourController {

    private ConnectFourModel model;

    /**
     * Need to initialize a controller with a model which it will control
     *
     * @param model connectfour model to play with
     */
    public ConnectFourController(ConnectFourModel model) {
        this.model = model;
    }

    /**
     * Prepares the model for a 2 player game
     * This uses the PlayerFactory to generate the players
     */
    public void startGameWithPlayer(String player1, String player2) {

//        model.addPlayers(new HumanPlayer(1, "SD", Color.RED));
//        model.addPlayers(new HumanPlayer(2, "AMS", Color.YELLOW));
        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player1, null, null));
        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player2, null, null));
        model.gameStart();
    }

    /**
     * Prepares the model for a game with the computer
     * This uses the PlayerFactory to generate the players
     */
    public void startGameWithComputer(String player1, AIDifficulty difficulty) {
//        model.addPlayers(new HumanPlayer(1, "SD", Color.RED));
//        model.addPlayers(new ComputerPlayer(2, "AMS", Color.YELLOW, model, 1, difficulty));
        Player humanPlayer = PlayerFactory.getPlayer(PlayerType.HUMAN, player1, null, null);
        AIParameters params = new AIParameters(difficulty, humanPlayer.getPlayerId(), getModel());
        Player computerPlayer = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", null, params);
        model.addPlayers(humanPlayer);
        model.addPlayers(computerPlayer);
        model.gameStart();

    }

    /**
     * Notifies the model to insert the token into given column
     *
     * @param column column to insert
     */
    public void play(int column) {
        model.insertToken(column);
    }

    /**
     * Used to clear the board and player list to start a new game
     */
    public void resetGame() {
        model.resetBoard();
        model.resetPlayers();
        PlayerFactory.clearPlayers();
    }

    /**
     * @return The model which can be used in the views to attach to
     */
    public ConnectFourModel getModel() {
        return model;
    }
}
