package edu.nyu.pqs.ps3.backup;

public class HumanPlayer extends Connect4Listner implements Player{

    String playerName;
    int id;

    public HumanPlayer(int id, String name, Connect4Model model) {
        playerName = name;
        this.id = id;
        this.model = model;
        this.model.attach(this);
    }

    @Override
    public void play() {

    }

    @Override
    public void update() {

    }

    @Override
    public void gameStarts() {

    }

    @Override
    public void gameEnds(int winnerID) {

    }
}
