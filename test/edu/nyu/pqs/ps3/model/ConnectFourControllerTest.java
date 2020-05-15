package edu.nyu.pqs.ps3.model;

import edu.nyu.pqs.ps3.players.AIDifficulty;
import edu.nyu.pqs.ps3.players.PlayerFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectFourControllerTest {
    @Before
    public void setUp() throws Exception {
        PlayerFactory.clearPlayers();
    }

    @Test(expected = NullPointerException.class)
    public void ConnectFourController_modelNull() {
        new ConnectFourController(null);
    }

    @Test
    public void testStartGameWithPlayer() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        ConnectFourController controller = new ConnectFourController(model);
        controller.startGameWithPlayer("Player1", "Player2");
        assertTrue(model.getPlayers().size() == 2);
        assertNotNull(model.getBoard());
    }

    @Test
    public void testStartGameWithComputer() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        ConnectFourController controller = new ConnectFourController(model);
        controller.startGameWithComputer("Player1", AIDifficulty.HARD);
        assertTrue(model.getPlayers().size() == 2);
        assertNotNull(model.getBoard());
    }

    @Test
    public void testPlay() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        ConnectFourController controller = new ConnectFourController(model);
        controller.startGameWithPlayer("Player1", "Player2");
        controller.play(0);
        assertTrue(model.getBoard()[model.getNumRows()-1][0] != 0);
    }

    @Test
    public void testResetGame() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        ConnectFourController controller = new ConnectFourController(model);
        controller.startGameWithPlayer("Player1", "Player2");
        controller.play(0);
        controller.resetGame();
        assertEquals(0, model.getBoard()[model.getNumRows()-1][0]);
        assertEquals(0, model.getPlayers().size());

    }
}