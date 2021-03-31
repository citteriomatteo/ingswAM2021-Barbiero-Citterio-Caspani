package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


/**
 * Unit test for simple it.polimi.ingsw.App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );

    }
    @Test
    public void testX(){
        App app = new App();
        for(int i=0; i<50; i++){
            assertEquals(app.prova(i), i+1);
        }
    }
}
