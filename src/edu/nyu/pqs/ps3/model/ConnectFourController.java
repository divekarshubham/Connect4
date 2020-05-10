package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.ComputerPlayer;
import edu.nyu.pqs.ps3.players.HumanPlayer;
import edu.nyu.pqs.ps3.players.PlayerFactory;
import edu.nyu.pqs.ps3.players.PlayerType;

import java.awt.*;

public class ConnectFourController {

    private ConnectFourModel model;
    public ConnectFourController(ConnectFourModel model){
        this.model = model;
    }

    public void startGameWithPlayer(String player1, String player2){
        model.resetBoard();
        model.addPlayers(new HumanPlayer(1,"SD", Color.RED));
        model.addPlayers(new HumanPlayer(2,"AMS", Color.YELLOW));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player1, null));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player2, null));
        model.gameStart();
    }

    public void startGameWithComputer(){
        model.resetBoard();
        model.addPlayers(new HumanPlayer(1,"SD", Color.RED));
        model.addPlayers(new ComputerPlayer(2,"AMS", Color.YELLOW, model, 1));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player1, null));
//        model.addPlayers(PlayerFactory.getPlayer(PlayerType.HUMAN, player2, null));
        model.gameStart();

    }

    public void play(int column){
        model.insertToken(column);
    }

    public ConnectFourModel getModel(){
        return model;
    }
}
