package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.Player;

import java.awt.*;

public interface Listner {
    public void gameStart();
    public void playerTurn(Player player);
    public void gameEnd(Result result, Player player);
    public void addTokenToPosition(int row, int column, Color color);
    public void disableAddingTokensTo(int column);
    public void initBoard(int row, int column);
}
