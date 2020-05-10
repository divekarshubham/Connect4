/**
 * This code is submission of pqs Assignment4 to implement Connect Four
 *
 * @author  Himani Shah (has482)
 */
package edu.nyu.pqs.ps3.has.app;

import java.awt.*;

/**
 * Observer/Listener that listens to the game
 */
public interface GameListener {

    /**
     * Called by model when game starts
     */
    void gameStart();

    /**
     * Called by model to notify next turn to observers
     */
    void yourTurn(Player p);

    /**
     * Called by model to notify winner
     */
    void youWon(Player p);

    /**
     * Called by model to notify draw
     */
    void draw();

    /**
     * Called by model to update board with latest move
     */
    void addDiscToSlot(Color coinColor, int position, int row);

    /**
     * Called by model to enable options(option to choose game type)
     */
    void enableOptions();

    /**
     * Called by model to disable all the buttons used to add disc to the board
     */
    void diableAllDiscAddButton();

    /**
     * Called by model disable the button used to add disc to the board at a particular column
     */
    void disableAddDiscButton(int columnposition);

    /**
     * Called by model to create board
     */
    void makeBoard(int row, int column);

    /**
     * Called by model to disable options(option to choose game type)
     */
    void disableOptions();
}
