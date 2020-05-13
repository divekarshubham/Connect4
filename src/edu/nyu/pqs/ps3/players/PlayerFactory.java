/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.players;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PlayerFactory class is used to generate instance of player based on player type
 */
public class PlayerFactory {
    static int maxNoOfPlayers = 2;
    static int currentPlayerCount = 0;
    static List<Color> availableColors = new ArrayList<>();

    /**
     * Private constructor to make PlayerFactory non-initializable
     */
    private PlayerFactory() {
        throw new AssertionError("Impossible scenario");
    }

    /**
     * Function builds a player instance of a particular type with name, player Id, and color as parameters
     *
     * @param type       one of the player types Human/Computer
     * @param name       name of the player
     * @param color      color preferred by a player, auto-genrated if none provided
     * @param parameters parameters required for the computer player
     * @return instance of the player requested
     * @throws IllegalArgumentException if any of the parameters is invalid
     */
    public static Player getPlayer(PlayerType type, String name, Color color, AIParameters parameters) {
        if (currentPlayerCount >= maxNoOfPlayers)
            throw new IllegalArgumentException("Max number of players initialized");
        if (type == null)
            throw new IllegalArgumentException("PlayerType invalid");
        if (name == null || name.equals(""))
            throw new IllegalArgumentException("Player name invalid");
        if (color == null)
            color = getRandomColor();
        if (type == PlayerType.COMPUTER && parameters == null)
            throw new IllegalArgumentException("AI parameters cannot be null");
        if (type == PlayerType.HUMAN) {
            currentPlayerCount++;
            return new HumanPlayer(currentPlayerCount, name, color);
        } else {
            currentPlayerCount++;
            return new ComputerPlayer(currentPlayerCount, name, color, parameters.getModel(),
                    parameters.getOtherPlayer(), parameters.getDifficulty());
        }
    }

    /**
     * Clears the players for a new game
     */
    public static void clearPlayers() {
        availableColors.clear();
        currentPlayerCount = 0;
    }

    /**
     * Generate a random color if not provided by the user
     *
     * @return Random color from the availabel colors list
     */
    private static Color getRandomColor() {
        if (availableColors.size() == 0)
            availableColors = new ArrayList<>(List.of(Color.BLACK, Color.ORANGE, Color.YELLOW,
                    Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN));

        Random rand = new Random();
        int colorIdx = rand.nextInt(availableColors.size());
        Color randomColor = availableColors.remove(colorIdx);
        return randomColor;
    }
}
