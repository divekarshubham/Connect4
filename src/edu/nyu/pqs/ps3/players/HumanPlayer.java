/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.players;

import java.awt.*;

/**
 * This class is used to represent and store the  details of a Human Player
 */
public class HumanPlayer implements Player {
    int playerId;
    String playerName;
    Color playerColor;

    /**
     * Constructor to initialize all parameters
     */
    public HumanPlayer(int playerId, String playerName, Color playerColor) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerColor = playerColor;
    }

    /**
     * @return Used to retrieve the player ID
     */
    @Override
    public int getPlayerId() {
        return playerId;
    }

    /**
     * @return Used to retrieve the player name
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return Used to retrieve the player token color
     */
    @Override
    public Color getTokenColor() {
        return playerColor;
    }

    /**
     * @return making a move for the game
     */
    @Override
    public void play() {
        //blank intentionally
    }

    @Override
    public String toString() {
        return "Player" + playerId;
    }
}
