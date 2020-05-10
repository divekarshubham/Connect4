package edu.nyu.pqs.ps3.players;

import java.awt.*;

public interface Player {
    int getPlayerId();
    String getPlayerName();
    Color getTokenColor();
    void play();
}
