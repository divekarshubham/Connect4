/**
 * This code is submission of pqs Assignment4 to implement Connect Four
 *
 * @author  Himani Shah (has482)
 */
package edu.nyu.pqs.ps3.has.app;

import java.awt.*;

/**
 * PlayerFactory class is used to generate instance of player based on player type
 */
 class PlayerFactory {

    /**
     * buildPlayer function builds a player instance of a particular type eith player Id and color as parameters
     *
     *  @param playertype one of the player types Human/Computer
     *  @param playerId id for each player
     *  @param color Disc color for the player
     *
     * @throws IllegalArgumentException if player type or color is null or player id is negative
     *
     * @return player object based on the player type
     */

     static Player buildPlayer(PlayerType playertype, int playerId, Color color, ConnectFourModel model){
        Player player = null;
        if(playertype == null){
            throw new IllegalArgumentException("player cannot be null");
        }
        if(playerId <=0){
            throw new IllegalArgumentException("player id cannot be negative or zero");
        }
        if(color == null){
            throw new IllegalArgumentException("Color cannot be null");
        }
         if(model == null){
             throw new IllegalArgumentException("Model cannot be null");
         }
        if(playertype == PlayerType.HUMANPLAYER){
            player = new HumanPlayer(playerId, color);
        }
        if(playertype == PlayerType.COMPUTERPLAYER){
            player = new ComputerPlayer(playerId, color, model);
        }
        return player;
    }
}
