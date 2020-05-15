package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlayerFactoryTest {

    private ConnectFourModel model;
    @Before
    public void setUp() throws Exception {
       model = new ConnectFourModel.Builder().build();
       PlayerFactory.clearPlayers();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayer_playerTypeNull() {
        PlayerFactory.getPlayer(null, "Test", Color.BLACK, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayer_playerNameNull() {
        PlayerFactory.getPlayer(PlayerType.HUMAN, null, Color.BLACK, null);
    }

    @Test
    public void testGetPlayer_playerColorNull() {
        Player player = PlayerFactory.getPlayer(PlayerType.HUMAN, "Test", null,null);
        assertEquals(1, PlayerFactory.getCurrentPlayerCount());
        assertTrue(player.getTokenColor() != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayer_AIParamsNullForComputer() {
        PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.BLACK, null);
    }

    @Test
    public void testGetPlayer_AIParamsNullForHuman() {
        Player player = PlayerFactory.getPlayer(PlayerType.HUMAN, "Computer", Color.BLACK, null);
        assertEquals(1, PlayerFactory.getCurrentPlayerCount());
        assertTrue(player instanceof HumanPlayer);
    }

    @Test
    public void testGetPlayer_validComputer() {
        AIParameters parameters = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.BLACK, parameters);
        assertEquals(1, PlayerFactory.getCurrentPlayerCount());
        assertTrue(player instanceof ComputerPlayer);
    }

    @Test
    public void testGetPlayer_validHuman() {
        AIParameters parameters = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", Color.BLACK, parameters);
        assertEquals(1, PlayerFactory.getCurrentPlayerCount());
        assertTrue(player instanceof HumanPlayer);
    }

    @Test
    public void testClearPlayers() {
        AIParameters parameters = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", Color.BLACK, parameters);
        Player player2 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.BLACK, parameters);
        PlayerFactory.clearPlayers();
        assertEquals(0, PlayerFactory.getCurrentPlayerCount());
    }

    @Test
    public void testSetAndGetMaxPlayers() {
        PlayerFactory.setMaxPlayers(3);
        AIParameters parameters = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", Color.BLACK, parameters);
        Player player2 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.BLACK, parameters);
        Player player3 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Alex Doe", Color.BLACK, parameters);
        assertEquals(3,PlayerFactory.getCurrentPlayerCount());
        assertEquals(3,PlayerFactory.getMaxPlayers());
    }

    @Test(expected = IllegalStateException.class)
    public void testPlayerLimitExceed() {
        AIParameters parameters = new AIParameters(AIDifficulty.HARD, 1, model);
        Player player1 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", Color.BLACK, parameters);
        Player player2 = PlayerFactory.getPlayer(PlayerType.COMPUTER, "Computer", Color.BLACK, parameters);
        Player player3 = PlayerFactory.getPlayer(PlayerType.HUMAN, "Alex Doe", Color.BLACK, parameters);
    }
}