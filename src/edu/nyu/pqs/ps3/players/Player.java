/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.players;

import java.awt.*;

/**
 * The interface contains methods each player must implement to play
 */
public interface Player {
    /**
     * @return Used to retrieve the player ID
     */
    int getPlayerId();

    /**
     * @return Used to retrieve the player name
     */
    String getPlayerName();

    /**
     * @return Used to retrieve the player token color
     */
    Color getTokenColor();

    /**
     * @return making a move for the game
     */
    void play();
}
