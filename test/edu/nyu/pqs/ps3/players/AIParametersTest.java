package edu.nyu.pqs.ps3.players;

import edu.nyu.pqs.ps3.model.ConnectFourModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class AIParametersTest {

    @Test(expected = IllegalArgumentException.class)
    public void testDifficulty_Null() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        new AIParameters(null, 1, model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOtherPlayer_Invalid() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        new AIParameters(null, -1, model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModel_Null() {
        new AIParameters(null, 1, null);
    }

    @Test
    public void testValidParameters() {
        ConnectFourModel model = new ConnectFourModel.Builder().build();
        AIParameters parameters = new AIParameters(AIDifficulty.HARD, 1, model);
        assertEquals(AIDifficulty.HARD, parameters.getDifficulty());
        assertEquals(1, parameters.getOtherPlayer());
        assertEquals(model, parameters.getModel());
    }
}