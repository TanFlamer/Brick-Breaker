package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BrickTest {

    Brick brick;

    @BeforeEach
    void setUp() {
        brick = new Brick(4,0,0,60,20,new Dimension(600,450));
    }

    @AfterEach
    void tearDown() {
        brick = null;
    }

    @Test
    void getBorder() {
        assertEquals(brick.getBorder(),new Color(24, 22, 16));
    }

    @Test
    void getInner() {
        assertEquals(brick.getInner(),new Color(50, 46, 46));
    }

    @Test
    void getBrickFace() {
        assertEquals(brick.getBrickFace(),new Rectangle(new Point(0,0), new Dimension(60,20)));
    }

    @Test
    void getBrickFaceNew() {
        assertEquals(brick.getBrickFaceNew(),new Rectangle(new Point(0,0), new Dimension(60,20)));
    }

    @Test
    void getBreakProbability() {
        assertEquals(brick.getBreakProbability(),0.4);
    }

    @Test
    void getFullStrength() {
        assertEquals(brick.getFullStrength(),2);
    }

    @Test
    void getStrength() {
        assertEquals(brick.getStrength(),2);
    }

    @Test
    void getCrack() {
        assertNotNull(brick.getCrack());
    }

    @Test
    void getScore() {
        assertEquals(brick.getScore(),1000);
    }

    @Test
    void isBroken() {
        assertFalse(brick.isBroken());
    }

    @Test
    void isCrackable() {
        assertTrue(brick.isCrackable());
    }

    @Test
    void getBrickInfo() {
        brick.getBrickInfo(1,100,100,100,20);
        assertEquals(brick.getFullStrength(),1);
        assertEquals(brick.getBreakProbability(),1);
        assertFalse(brick.isCrackable());
        assertEquals(brick.getBorder(),Color.GRAY);
        assertEquals(brick.getInner(),new Color(178, 34, 34).darker());
        assertNull(brick.getCrack());
        assertNull(brick.getBrickFaceNew());
    }

    @Test
    void setBrickFace() {
        brick.setBrickFace(new Rectangle(new Point(100,100), new Dimension(100,40)));
        assertEquals(brick.getBrickFace(),new Rectangle(new Point(100,100), new Dimension(100,40)));
    }

    @Test
    void setBroken() {
        brick.setBroken(true);
        assertTrue(brick.isBroken());
    }

    @Test
    void setStrength() {
        brick.setStrength(1);
        assertEquals(brick.getStrength(),1);
    }
}