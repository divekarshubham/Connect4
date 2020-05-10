/**
 * This code is submission of pqs Assignment4 to implement Connect Four
 *
 * @author  Himani Shah (has482)
 */
package edu.nyu.pqs.ps3.has.app;

import java.awt.*;

/**
 * The HumanPlayer player class is for the Human players. The Human player objects are create in this class .
 */
public class HumanPlayer implements Player {
    int playerId;
    Color color;

    /**
     * create human player object with with player id and disc color as parameters
     * @param playerId Autogenerated unique id for each player
     * @param color Autogenerated color for each player
     */
    HumanPlayer(int playerId, Color color){
        this.playerId = playerId;
        this.color = color;
    }

    /**
     * @return disc color
     */
    public Color getCoinColor() {
        return color;
    }

    /**
     * Human player plays using the view
     */
    public void play() { }

    /**
     * @return player id
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * @return player details
     */
    public String toString(){
        return "player:" + playerId;
    }
}
