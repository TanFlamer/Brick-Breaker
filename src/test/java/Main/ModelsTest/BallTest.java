package Main.ModelsTest;

import Main.Models.Ball;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BallTest tests to see if the methods of Ball class which are all setters and getters are all working properly. The
 * getters are tested first to ensure that they are working before using them to test the setters.
 */
class BallTest {

    /**
     * Ball object used in the tests.
     */
    Ball ball;

    /**
     * A new ball object is created before every test.
     */
    @BeforeEach
    void setUp() {
        ball = new Ball(new Point(100,100),10);
    }

    /**
     * The ball object is cleaned up after each test.
     */
    @AfterEach
    void tearDown() {
        ball = null;
    }

    /**
     * This tests the getter for ball face.
     */
    @Test
    void getBallFace() {
        assertEquals(ball.getBallFace(),new Ellipse2D.Double(95,95,10,10));
    }

    /**
     * This tests the getter for ball center point.
     */
    @Test
    void getCenter() {
        assertEquals(ball.getCenter(),new Point(100,100));
    }

    /**
     * This tests the getter for ball horizontal speed.
     */
    @Test
    void getSpeedX() {
        assertEquals(ball.getSpeedX(),0);
    }

    /**
     * This tests the getter for ball vertical speed.
     */
    @Test
    void getSpeedY() {
        assertEquals(ball.getSpeedY(),0);
    }

    /**
     * This tests the getter for ball inner colour.
     */
    @Test
    void getInner() {
        assertEquals(ball.getInner(),new Color(255, 219, 88));
    }

    /**
     * This tests the getter for ball border colour.
     */
    @Test
    void getBorder() {
        assertEquals(ball.getBorder(),ball.getInner().darker().darker());
    }

    /**
     * This tests the getter for ball top point.
     */
    @Test
    void getUp() {
        assertEquals(ball.getUp(),new Point(100,95));
    }

    /**
     * This tests the getter for ball bottom point.
     */
    @Test
    void getDown() {
        assertEquals(ball.getDown(),new Point(100,105));
    }

    /**
     * This tests the getter for ball left point.
     */
    @Test
    void getLeft() {
        assertEquals(ball.getLeft(),new Point(95,100));
    }

    /**
     * This tests the getter for ball right point.
     */
    @Test
    void getRight() {
        assertEquals(ball.getRight(),new Point(105,100));
    }

    /**
     * This tests the setter for ball center point using the tested getter.
     */
    @Test
    void setCenter() {
        ball.setCenter(new Point(50,50));
        assertEquals(ball.getCenter(),new Point(50,50));
    }

    /**
     * This tests the setter for ball top, bottom, left and right points using the tested getters.
     */
    @Test
    void setPoints() {
        ball.setPoints(new Point(50,50));
        assertEquals(ball.getUp(),new Point(50,45));
        assertEquals(ball.getDown(),new Point(50,55));
        assertEquals(ball.getLeft(),new Point(45,50));
        assertEquals(ball.getRight(),new Point(55,50));
    }

    /**
     * This tests the setter for ball face using the tested getter.
     */
    @Test
    void setBallFace() {
        ball.setBallFace(new Ellipse2D.Double(45,45,10,10));
        assertEquals(ball.getBallFace(),new Ellipse2D.Double(45,45,10,10));
    }

    /**
     * This tests the setter for ball horizontal speed using the tested getter.
     */
    @Test
    void setSpeedX() {
        ball.setSpeedX(2);
        assertEquals(ball.getSpeedX(),2);
    }

    /**
     * This tests the setter for ball vertical speed using the tested getter.
     */
    @Test
    void setSpeedY() {
        ball.setSpeedY(2);
        assertEquals(ball.getSpeedY(),2);
    }
}