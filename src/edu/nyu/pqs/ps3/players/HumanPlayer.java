package edu.nyu.pqs.ps3.players;

import java.awt.*;

public class HumanPlayer implements Player {
    int playerId;
    String playerName;
    Color playerColor;

    public HumanPlayer(int playerId, String playerName, Color playerColor){
        this.playerId = playerId;
        this.playerName = playerName;
        this. playerColor = playerColor;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public Color getTokenColor() {
        return playerColor;
    }

    @Override
    public void play() {
        //blank intentionally
    }

    @Override
    public String toString(){
        return "Player"+playerId;
    }
}
