package edu.nyu.pqs.ps3.has.app;

import java.awt.*;

/**
 * Has functions that must be implemented by any player
 */
public interface Player {

    /**
     * players strategy to play
     */
    void play();

    /**
     * @return player id
     */
    int getPlayerId();

    /**
     * @return disc color
     */
    Color getCoinColor();

}
