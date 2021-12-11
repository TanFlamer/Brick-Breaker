package Main.ModelsTest;

import Main.Models.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * PlayerTest tests to see if the methods of Player class which are all setters and getters are all working properly.
 * The getters are tested first to ensure that they are working before using them to test the setters.
 */
class PlayerTest {

    /**
     * Player object used in the tests.
     */
    Player player;

    /**
     * A new player object is created before every test.
     */
    @BeforeEach
    void setUp() {
        player = new Player(new Point(100,100),150,10,new Dimension(600,450));
    }

    /**
     * The player object is cleaned up after each test.
     */
    @AfterEach
    void tearDown() {
        player = null;
    }

    /**
     * This tests the getter for player midpoint.
     */
    @Test
    void getMidPoint() {
        assertEquals(player.getMidPoint(),new Point(100,100));
    }

    /**
     * This tests the getter for player move amount.
     */
    @Test
    void getMoveAmount() {
        assertEquals(player.getMoveAmount(),0);
    }

    /**
     * This tests the getter for player min horizontal position.
     */
    @Test
    void getMin() {
        assertEquals(player.getMin(),75);
    }

    /**
     * This tests the getter for player max horizontal position.
     */
    @Test
    void getMax() {
        assertEquals(player.getMax(),525);
    }

    /**
     * This tests the getter for player face.
     */
    @Test
    void getPlayerFace() {
        assertEquals(player.getPlayerFace(),new Rectangle(new Point(25, 100),new Dimension(150,10)));
    }

    /**
     * This tests the getter for player border colour.
     */
    @Test
    void getBorder() {
        assertEquals(player.getBorder(),Color.GREEN.darker().darker());
    }

    /**
     * This tests the getter for player inner colour.
     */
    @Test
    void getInner() {
        assertEquals(player.getInner(),Color.GREEN);
    }

    /**
     * This tests the getter for player max vertical position.
     */
    @Test
    void getTop() {
        assertEquals(player.getTop(),0);
    }

    /**
     * This tests the getter for player min vertical position.
     */
    @Test
    void getBottom() {
        assertEquals(player.getBottom(),440);
    }

    /**
     * This tests the getter for player vertical move amount.
     */
    @Test
    void getVerticalMoveAmount() {
        assertEquals(player.getVerticalMoveAmount(),0);
    }

    /**
     * This tests the setter for player midpoint using the tested getter.
     */
    @Test
    void setMidPoint() {
        player.setMidPoint(new Point(200,200));
        assertEquals(player.getMidPoint(),new Point(200,200));
    }

    /**
     * This tests the setter for player move amount using the tested getter.
     */
    @Test
    void setMoveAmount() {
        player.setMoveAmount(5);
        assertEquals(player.getMoveAmount(),5);
    }

    /**
     * This tests the setter for player face using the tested getter.
     */
    @Test
    void setPlayerFace() {
        player.setPlayerFace(new Rectangle(new Point(200, 200),new Dimension(100,20)));
        assertEquals(player.getPlayerFace(),new Rectangle(new Point(200, 200),new Dimension(100,20)));
    }

    /**
     * This tests the setter for player vertical move amount using the tested getter.
     */
    @Test
    void setVerticalMoveAmount() {
        player.setVerticalMoveAmount(5);
        assertEquals(player.getVerticalMoveAmount(),5);
    }
}