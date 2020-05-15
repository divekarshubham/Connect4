package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class HumanPlayerTest {
    @Before
    public void setUp() throws Exception {
        PlayerFactory.clearPlayers();
    }

    @Test
    public void HumanPlayer_test() {
        Player p = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", Color.BLUE, null);
        assertTrue(p instanceof HumanPlayer);
        assertTrue(p.getPlayerId() == 1);
        assertTrue(p.getTokenColor() == Color.BLUE);
    }

    @Test
    public void testToString() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        Player p = PlayerFactory.getPlayer(PlayerType.HUMAN, "Jane Doe", Color.BLUE, null);
        assertTrue(p instanceof HumanPlayer);
        assertTrue(p.toString().equals("Player1"));
    }
}