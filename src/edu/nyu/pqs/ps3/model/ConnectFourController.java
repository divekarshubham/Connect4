package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.*;

import java.awt.*;

public class ConnectFourController {

    private ConnectFourModel model;
    public ConnectFourController(ConnectFourModel model){
        this.model = model;
    }

    public void startGameWithPlayer(String player1, String player2){

        model.addPlayers(new HumanPlayer(1,"SD", Color.RED));
        model.addPlayers(new HumanPlayer(2,"AMS", Color.YELLOW));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player1, null));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player2, null));
        model.gameStart();
    }

    public void startGameWithComputer(int difficult){
        //TODO: move this difficulty to controller
        AIDifficulty difficulty = AIDifficulty.EASY;
        if(difficult == 2)
            difficulty = AIDifficulty.HARD;
        else if(difficult == 1)
            difficulty = AIDifficulty.MEDIUM;

        model.addPlayers(new HumanPlayer(1,"SD", Color.RED));
        model.addPlayers(new ComputerPlayer(2,"AMS", Color.YELLOW, model, 1, difficulty));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player1, null));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player2, null));
        model.gameStart();

    }

    public void play(int column){
        model.insertToken(column);
    }

    public void resetGame(){
        model.resetBoard();
        PlayerFactory.clearPlayers();
    }
    public ConnectFourModel getModel(){
        return model;
    }
}
