package Main.ModelsTest;

import Main.Models.Brick;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BrickTest tests to see if the methods of Brick class which are all setters and getters are all working properly. The
 * getters are tested first to ensure that they are working before using them to test the setters.
 */
class BrickTest {

    /**
     * Brick object used in the tests.
     */
    Brick brick;

    /**
     * A new brick object is created before every test.
     */
    @BeforeEach
    void setUp() {
        brick = new Brick(4,0,0,60,20,new Dimension(600,450));
    }

    /**
     * The brick object is cleaned up after each test.
     */
    @AfterEach
    void tearDown() {
        brick = null;
    }

    /**
     * This tests the getter for brick border colour.
     */
    @Test
    void getBorder() {
        assertEquals(brick.getBorder(),new Color(24, 22, 16));
    }

    /**
     * This tests the getter for brick inner colour.
     */
    @Test
    void getInner() {
        assertEquals(brick.getInner(),new Color(50, 46, 46));
    }

    /**
     * This tests the getter for brick face.
     */
    @Test
    void getBrickFace() {
        assertEquals(brick.getBrickFace(),new Rectangle(new Point(0,0), new Dimension(60,20)));
    }

    /**
     * This tests the getter for new brick face.
     */
    @Test
    void getBrickFaceNew() {
        assertEquals(brick.getBrickFaceNew(),new Rectangle(new Point(0,0), new Dimension(60,20)));
    }

    /**
     * This tests the getter for brick break probability.
     */
    @Test
    void getBreakProbability() {
        assertEquals(brick.getBreakProbability(),0.4);
    }

    /**
     * This tests the getter for brick full strength.
     */
    @Test
    void getFullStrength() {
        assertEquals(brick.getFullStrength(),2);
    }

    /**
     * This tests the getter for brick current strength.
     */
    @Test
    void getStrength() {
        assertEquals(brick.getStrength(),2);
    }

    /**
     * This tests the getter for brick crack.
     */
    @Test
    void getCrack() {
        assertNotNull(brick.getCrack());
    }

    /**
     * This tests the getter for brick score.
     */
    @Test
    void getScore() {
        assertEquals(brick.getScore(),1000);
    }

    /**
     * This tests the getter for brick broken condition.
     */
    @Test
    void isBroken() {
        assertFalse(brick.isBroken());
    }

    /**
     * This tests the getter for brick crackable condition.
     */
    @Test
    void isCrackable() {
        assertTrue(brick.isCrackable());
    }

    /**
     * This tests the getter for brick info.
     */
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

    /**
     * This tests the setter for brick face using the tested getter.
     */
    @Test
    void setBrickFace() {
        brick.setBrickFace(new Rectangle(new Point(100,100), new Dimension(100,40)));
        assertEquals(brick.getBrickFace(),new Rectangle(new Point(100,100), new Dimension(100,40)));
    }

    /**
     * This tests the setter for brick broken condition using the tested getter.
     */
    @Test
    void setBroken() {
        brick.setBroken(true);
        assertTrue(brick.isBroken());
    }

    /**
     * This tests the setter for brick current strength using the tested getter.
     */
    @Test
    void setStrength() {
        brick.setStrength(1);
        assertEquals(brick.getStrength(),1);
    }
}