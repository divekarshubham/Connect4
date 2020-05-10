package edu.nyu.pqs.ps3.backup;

public class ComputerPlayer extends Connect4Listner implements Player {

    String computerName;
    int id;

    public ComputerPlayer(int id, String name, Connect4Model model) {
        computerName = name;
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
