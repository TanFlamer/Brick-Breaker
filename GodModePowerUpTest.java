package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static org.junit.jupiter.api.Assertions.*;

class GodModePowerUpTest {

    GodModePowerUp powerUp;

    @BeforeEach
    void setUp() {
        powerUp = new GodModePowerUp(new Point(100,100),20);
    }

    @AfterEach
    void tearDown() {
        powerUp = null;
    }

    @Test
    void getMidPoint() {
        assertEquals(powerUp.getMidPoint(),new Point(100,100));
    }

    @Test
    void getPowerUp() {
        assertEquals(powerUp.getPowerUp(),new Ellipse2D.Double(90,90,20,20));
    }

    @Test
    void getInner() {
        assertEquals(powerUp.getInner(),Color.RED);
    }

    @Test
    void getBorder() {
        assertEquals(powerUp.getBorder(),Color.BLACK);
    }

    @Test
    void isSpawned() {
        assertFalse(powerUp.isSpawned());
    }

    @Test
    void isCollected() {
        assertFalse(powerUp.isCollected());
    }

    @Test
    void setMidPoint() {
        powerUp.setMidPoint(new Point(200,200));
        assertEquals(powerUp.getMidPoint(),new Point(200,200));
    }

    @Test
    void setPowerUp() {
        powerUp.setPowerUp(new Ellipse2D.Double(200,200,10,10));
        assertEquals(powerUp.getPowerUp(),new Ellipse2D.Double(200,200,10,10));
    }

    @Test
    void setSpawned() {
        powerUp.setSpawned(true);
        assertTrue(powerUp.isSpawned());
    }

    @Test
    void setCollected() {
        powerUp.setCollected(true);
        assertTrue(powerUp.isCollected());
    }
}