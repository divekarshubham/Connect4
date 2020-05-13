/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.Player;

import java.awt.*;

/**
 * Observer that listens to the state change of the game
 */
public interface Listner {
    /**
     * Called by model when game starts
     */
    public void gameStart();

    /**
     * Called by model to signal making the board
     */
    public void initBoard(int row, int column);

    /**
     * Called by model to notify that the player should play their turn
     */
    public void playerTurn(Player player);

    /**
     * Called by model when game ends
     */
    public void gameEnd(Result result, Player player);

    /**
     * Called by model to insert the token of a player at a given position
     */
    public void addTokenToPosition(int row, int column, Color color);

    /**
     * Called by model when a column if full and should not be inserted into
     */
    public void disableAddingTokensTo(int column);

}
