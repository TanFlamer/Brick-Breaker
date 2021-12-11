package Main.ModelsTest;

import Main.Models.GodModePowerUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GodModePowerUpTest tests to see if the methods of GodModePowerUp class which are all setters and getters are all
 * working properly. The getters are tested first to ensure that they are working before using them to test the setters.
 */
class GodModePowerUpTest {

    /**
     * PowerUp object used in the tests.
     */
    GodModePowerUp powerUp;

    /**
     * A new powerUp object is created before every test.
     */
    @BeforeEach
    void setUp() {
        powerUp = new GodModePowerUp(new Point(100,100),20);
    }

    /**
     * The powerUp object is cleaned up after each test.
     */
    @AfterEach
    void tearDown() {
        powerUp = null;
    }

    /**
     * This tests the getter for powerUp midpoint.
     */
    @Test
    void getMidPoint() {
        assertEquals(powerUp.getMidPoint(),new Point(100,100));
    }

    /**
     * This tests the getter for powerUp face.
     */
    @Test
    void getPowerUp() {
        assertEquals(powerUp.getPowerUp(),new Ellipse2D.Double(90,90,20,20));
    }

    /**
     * This tests the getter for powerUp inner colour.
     */
    @Test
    void getInner() {
        assertEquals(powerUp.getInner(),Color.RED);
    }

    /**
     * This tests the getter for powerUp border colour.
     */
    @Test
    void getBorder() {
        assertEquals(powerUp.getBorder(),Color.BLACK);
    }

    /**
     * This tests the getter for powerUp spawn condition.
     */
    @Test
    void isSpawned() {
        assertFalse(powerUp.isSpawned());
    }

    /**
     * This tests the getter for powerUp collected condition.
     */
    @Test
    void isCollected() {
        assertFalse(powerUp.isCollected());
    }

    /**
     * This tests the setter for powerUp midpoint using the tested getter.
     */
    @Test
    void setMidPoint() {
        powerUp.setMidPoint(new Point(200,200));
        assertEquals(powerUp.getMidPoint(),new Point(200,200));
    }

    /**
     * This tests the setter for powerUp face using the tested getter.
     */
    @Test
    void setPowerUp() {
        powerUp.setPowerUp(new Ellipse2D.Double(200,200,10,10));
        assertEquals(powerUp.getPowerUp(),new Ellipse2D.Double(200,200,10,10));
    }

    /**
     * This tests the setter for powerUp spawn condition using the tested getter.
     */
    @Test
    void setSpawned() {
        powerUp.setSpawned(true);
        assertTrue(powerUp.isSpawned());
    }

    /**
     * This tests the setter for powerUp collected condition using the tested getter.
     */
    @Test
    void setCollected() {
        powerUp.setCollected(true);
        assertTrue(powerUp.isCollected());
    }
}