package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerFactory {
    static int maxNoOfPlayers = 2;
    static int currentPlayerCount = 0;
    static List<Color> availableColors = new ArrayList<>();

    private PlayerFactory(){
        throw new AssertionError("Impossible scenario");
    }

    public static Player getPlayer(PlayerType type, String name, Color color, ConnectFourModel model){
        if(currentPlayerCount >= maxNoOfPlayers)
            throw new IllegalArgumentException("Max number of players initialized");
        if(type == null)
            throw new IllegalArgumentException("PlayerType invalid");
        if(name == null || name.equals(""))
            throw new IllegalArgumentException("Player name invalid");
        if(color == null)
            color = getRandomColor();
        if (type == PlayerType.HUMAN) {
            currentPlayerCount++;
            return new HumanPlayer(currentPlayerCount, name, color);
        }
        else {
            currentPlayerCount++;
            //TODO: fix otherplayerid
            return new ComputerPlayer(currentPlayerCount, name, color, model, 1);
        }
    }

    private static Color getRandomColor() {
        if(availableColors.size() == 0)
            availableColors = new ArrayList<>(List.of(Color.BLUE, Color.ORANGE, Color.YELLOW,
                    Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN));

        Random rand = new Random();
        int colorIdx = rand.nextInt(availableColors.size());
        Color randomColor = availableColors.remove(colorIdx);
        return randomColor;
    }
}
