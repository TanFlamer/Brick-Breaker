package Main.MVCTest;

import Main.MVC.GameBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameBoardTest tests to see if the methods of GameBoard class which are all setters and getters are all working
 * properly. The getters are tested first to ensure that they are working before using them to test the setters.
 */
class GameBoardTest {

    /**
     * GameBoard object used in the tests.
     */
    GameBoard gameBoard;

    /**
     * A new gameboard object is created before every test.
     */
    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(new int[5][11],new Dimension(600,450));
    }

    /**
     * The gameboard object is cleaned up after each test.
     */
    @AfterEach
    void tearDown() {
        gameBoard = null;
    }

    /**
     * This tests the getter for the array of bricks. Test returns true if array of bricks exist.
     */
    @Test
    void getBricks() {
        assertNotNull(gameBoard.getBricks());
    }

    /**
     * This tests the getter for the player. Test returns true if player exist.
     */
    @Test
    void getPlayer() {
        assertNotNull(gameBoard.getPlayer());
    }

    /**
     * This tests the getter for the ball. Test returns true if ball exist.
     */
    @Test
    void getBall() {
        assertNotNull(gameBoard.getBall());
    }

    /**
     * This tests the getter for the array of player choices.
     */
    @Test
    void getChoice() {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 11; j++)
                assertEquals(gameBoard.getChoice()[i][j],0);
    }

    /**
     * This tests the getter for the level number.
     */
    @Test
    void getLevel() {
        assertEquals(gameBoard.getLevel(),0);
    }

    /**
     * This tests the getter for the brick count.
     */
    @Test
    void getBrickCount() {
        assertEquals(gameBoard.getBrickCount(),0);
    }

    /**
     * This tests the getter for the ball count.
     */
    @Test
    void getBallCount() {
        assertEquals(gameBoard.getBallCount(),3);
    }

    /**
     * This tests the getter for the array of results (score/time).
     */
    @Test
    void getScoreAndTime() {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 1; j++)
                assertEquals(gameBoard.getScoreAndTime()[i][j],0);
    }

    /**
     * This tests the getter for the score.
     */
    @Test
    void getScore() {
        assertEquals(gameBoard.getScore(0),0);
    }

    /**
     * This tests the getter for the time.
     */
    @Test
    void getTime() {
        assertEquals(gameBoard.getTime(0),0);
    }

    /**
     * This tests the getter for the message flag.
     */
    @Test
    void getMessageFlag() {
        assertEquals(gameBoard.getMessageFlag(),0);
    }

    /**
     * This tests the getter for the power up. Test returns true if power up exists.
     */
    @Test
    void getPowerUp() {
        assertNotNull(gameBoard.getPowerUp());
    }

    /**
     * This tests the getter for the God Mode time left.
     */
    @Test
    void getGodModeTimeLeft() {
        assertEquals(gameBoard.getGodModeTimeLeft(),0);
    }

    /**
     * This tests the getter for the power up spawns.
     */
    @Test
    void getPowerUpSpawns() {
        assertEquals(gameBoard.getPowerUpSpawns(),0);
    }

    /**
     * This tests the getter for the start time.
     */
    @Test
    void getStartTime() {
        assertEquals(gameBoard.getStartTime(),0);
    }

    /**
     * This tests the getter for the ball start point.
     */
    @Test
    void getBallStartPoint() {
        assertEquals(gameBoard.getBallStartPoint(),new Point(0,0));
    }

    /**
     * This tests the getter for the player start point.
     */
    @Test
    void getPlayerStartPoint() {
        assertEquals(gameBoard.getBallStartPoint(),new Point(0,0));
    }

    /**
     * This tests the getter for the game messages. Tests return true if null because messages have not been
     * generated yet.
     */
    @Test
    void getGameMessages() {
        assertNull(gameBoard.getGameMessages(0));
    }

    /**
     * This tests the getter for the pause menu flag.
     */
    @Test
    void isShowPauseMenu() {
        assertFalse(gameBoard.isShowPauseMenu());
    }

    /**
     * This tests the getter for the ball lost flag.
     */
    @Test
    void isBallLost() {
        assertFalse(gameBoard.isBallLost());
    }

    /**
     * This tests the getter for the not of the game pause flag.
     */
    @Test
    void isNotPaused() {
        assertFalse(gameBoard.isNotPaused());
    }

    /**
     * This tests the getter for the game end flag.
     */
    @Test
    void isEnded() {
        assertFalse(gameBoard.isEnded());
    }

    /**
     * This tests the setter for level number using the tested getter.
     */
    @Test
    void setLevel() {
        gameBoard.setLevel(2);
        assertEquals(gameBoard.getLevel(),2);
    }

    /**
     * This tests the setter for brick count using the tested getter.
     */
    @Test
    void setBrickCount() {
        gameBoard.setBrickCount(15);
        assertEquals(gameBoard.getBrickCount(),15);
    }

    /**
     * This tests the setter for ball count using the tested getter.
     */
    @Test
    void setBallCount() {
        gameBoard.setBallCount(5);
        assertEquals(gameBoard.getBallCount(),5);
    }

    /**
     * This tests the setter for score using the tested getter.
     */
    @Test
    void setScore() {
        gameBoard.setScore(2,100);
        assertEquals(gameBoard.getScore(2),100);
    }

    /**
     * This tests the setter for time using the tested getter.
     */
    @Test
    void setTime() {
        gameBoard.setTime(1,200);
        assertEquals(gameBoard.getTime(1),200);
    }

    /**
     * This tests the setter for message flag using the tested getter.
     */
    @Test
    void setMessageFlag() {
        gameBoard.setMessageFlag(3);
        assertEquals(gameBoard.getMessageFlag(),3);
    }

    /**
     * This tests the setter for God Mode time left using the tested getter.
     */
    @Test
    void setGodModeTimeLeft() {
        gameBoard.setGodModeTimeLeft(5);
        assertEquals(gameBoard.getGodModeTimeLeft(),5);
    }

    /**
     * This tests the setter for pause menu flag using the tested getter.
     */
    @Test
    void setShowPauseMenu() {
        gameBoard.setShowPauseMenu(true);
        assertTrue(gameBoard.isShowPauseMenu());
    }

    /**
     * This tests the setter for power up spawns using the tested getter.
     */
    @Test
    void setPowerUpSpawns() {
        gameBoard.setPowerUpSpawns(3);
        assertEquals(gameBoard.getPowerUpSpawns(),3);
    }

    /**
     * This tests the setter for start time using the tested getter.
     */
    @Test
    void setStartTime() {
        gameBoard.setStartTime(10);
        assertEquals(gameBoard.getStartTime(),10);
    }

    /**
     * This tests the setter for ball lost flag using the tested getter.
     */
    @Test
    void setBallLost() {
        gameBoard.setBallLost(true);
        assertTrue(gameBoard.isBallLost());
    }

    /**
     * This tests the setter for game pause flag using the tested getter.
     */
    @Test
    void setPauseFlag() {
        gameBoard.setPauseFlag(false);
        assertTrue(gameBoard.isNotPaused());
    }

    /**
     * This tests the setter for game end flag using the tested getter.
     */
    @Test
    void setEndFlag() {
        gameBoard.setEndFlag(true);
        assertTrue(gameBoard.isEnded());
    }

    /**
     * This tests the setter for ball start point using the tested getter.
     */
    @Test
    void setBallStartPoint() {
        gameBoard.setBallStartPoint(new Point(100,100));
        assertEquals(gameBoard.getBallStartPoint(),new Point(100,100));
    }

    /**
     * This tests the setter for player start point using the tested getter.
     */
    @Test
    void setPlayerStartPoint() {
        gameBoard.setPlayerStartPoint(new Point(100,100));
        assertEquals(gameBoard.getPlayerStartPoint(),new Point(100,100));
    }

    /**
     * This tests the setter for game messages using the tested getter.
     */
    @Test
    void setGameMessages() {
        gameBoard.setGameMessages(0,"Test");
        assertEquals(gameBoard.getGameMessages(0),"Test");
    }
}