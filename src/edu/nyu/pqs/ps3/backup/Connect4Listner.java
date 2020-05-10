package edu.nyu.pqs.ps3.backup;

public abstract class Connect4Listner {
    protected Connect4Model model;
    public abstract void update();
    public abstract void gameStarts();
    public abstract void gameEnds(int winnerID);
}
