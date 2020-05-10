package edu.nyu.pqs.ps3.backup;

public class PlayerFactory {

    int playerCount = 1;

    private PlayerFactory(){
        throw new AssertionError("Impossible scenario");
    }

    public Player getPlayer(PlayerType type, String name, Connect4Model model){
        if(playerCount > 2)
            throw new IllegalStateException("Cannot create more players");

        if (type == PlayerType.HUMAN)
            return new HumanPlayer(playerCount++, name, model);
        else
            return new ComputerPlayer(playerCount++, name, model);
    }
}
