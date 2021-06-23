package it.polimi.ingsw.gameLogic.model.essentials;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResTypeTest {

    @Test
    public void testImagesRecognition(){
        for(ResType res : ResType.values())
            assertSame(res, ResType.valueOfImage(res.asImage()));
    }
}
