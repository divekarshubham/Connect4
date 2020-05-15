/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;

/**
 * This class is used to initialize all the parameters required for the computer player
 * for knowing the state of the machine
 */
public class AIParameters {

    private AIDifficulty difficulty;
    private int otherPlayer;
    private ConnectFourModel model;

    /**
     * @throws IllegalArgumentException if any of the parameters are invalid
     */
    public AIParameters(AIDifficulty difficulty, int otherPlayer, ConnectFourModel model) {
        if(difficulty == null)
            throw new IllegalArgumentException("Invalid difficulty");
        if(otherPlayer<0)
            throw new IllegalArgumentException("Invalid difficulty");
        if (model == null)
            throw new IllegalArgumentException("Invalid model");
        this.difficulty = difficulty;
        this.otherPlayer = otherPlayer;
        this.model = model;
    }

    /**
     * @return Gives the difficulty chosen by the player
     */
    public AIDifficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return Gives the id of the human player
     */
    public int getOtherPlayer() {
        return otherPlayer;
    }

    /**
     * @return Gives the Connect4 model
     */
    public ConnectFourModel getModel() {
        return model;
    }

}
