package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(new int[5][11],new Dimension(600,450));
    }

    @AfterEach
    void tearDown() {
        gameBoard = null;
    }

    @Test
    void getBricks() {
        assertNotNull(gameBoard.getBricks());
    }

    @Test
    void getPlayer() {
        assertNotNull(gameBoard.getPlayer());
    }

    @Test
    void getBall() {
        assertNotNull(gameBoard.getBall());
    }

    @Test
    void getChoice() {
        assertNotNull(gameBoard.getChoice());
    }

    @Test
    void getLevel() {
        assertEquals(gameBoard.getLevel(),0);
    }

    @Test
    void getBrickCount() {
        assertEquals(gameBoard.getBrickCount(),0);
    }

    @Test
    void getBallCount() {
        assertEquals(gameBoard.getBallCount(),3);
    }

    @Test
    void getScoreAndTime() {
        assertNotNull(gameBoard.getScoreAndTime());
    }

    @Test
    void getScore() {
        assertEquals(gameBoard.getScore(0),0);
    }

    @Test
    void getTime() {
        assertEquals(gameBoard.getTime(0),0);
    }

    @Test
    void getMessageFlag() {
        assertEquals(gameBoard.getMessageFlag(),0);
    }

    @Test
    void getPowerUp() {
        assertNotNull(gameBoard.getPowerUp());
    }

    @Test
    void getGodModeTimeLeft() {
        assertEquals(gameBoard.getGodModeTimeLeft(),0);
    }

    @Test
    void getPowerUpSpawns() {
        assertEquals(gameBoard.getPowerUpSpawns(),0);
    }

    @Test
    void getStartTime() {
        assertEquals(gameBoard.getStartTime(),0);
    }

    @Test
    void getBallStartPoint() {
        assertEquals(gameBoard.getBallStartPoint(),new Point(0,0));
    }

    @Test
    void getPlayerStartPoint() {
        assertEquals(gameBoard.getBallStartPoint(),new Point(0,0));
    }

    @Test
    void getGameMessages() {
        assertNull(gameBoard.getGameMessages(0));
    }

    @Test
    void isShowPauseMenu() {
        assertFalse(gameBoard.isShowPauseMenu());
    }

    @Test
    void isBallLost() {
        assertFalse(gameBoard.isBallLost());
    }

    @Test
    void isNotPaused() {
        assertFalse(gameBoard.isNotPaused());
    }

    @Test
    void isEnded() {
        assertFalse(gameBoard.isEnded());
    }

    @Test
    void setLevel() {
        gameBoard.setLevel(2);
        assertEquals(gameBoard.getLevel(),2);
    }

    @Test
    void setBrickCount() {
        gameBoard.setBrickCount(15);
        assertEquals(gameBoard.getBrickCount(),15);
    }

    @Test
    void setBallCount() {
        gameBoard.setBallCount(5);
        assertEquals(gameBoard.getBallCount(),5);
    }

    @Test
    void setScore() {
        gameBoard.setScore(2,100);
        assertEquals(gameBoard.getScore(2),100);
    }

    @Test
    void setTime() {
        gameBoard.setTime(1,200);
        assertEquals(gameBoard.getTime(1),200);
    }

    @Test
    void setMessageFlag() {
        gameBoard.setMessageFlag(3);
        assertEquals(gameBoard.getMessageFlag(),3);
    }

    @Test
    void setGodModeTimeLeft() {
        gameBoard.setGodModeTimeLeft(5);
        assertEquals(gameBoard.getGodModeTimeLeft(),5);
    }

    @Test
    void setShowPauseMenu() {
        gameBoard.setShowPauseMenu(true);
        assertTrue(gameBoard.isShowPauseMenu());
    }

    @Test
    void setPowerUpSpawns() {
        gameBoard.setPowerUpSpawns(3);
        assertEquals(gameBoard.getPowerUpSpawns(),3);
    }

    @Test
    void setStartTime() {
        gameBoard.setStartTime(10);
        assertEquals(gameBoard.getStartTime(),10);
    }

    @Test
    void setBallLost() {
        gameBoard.setBallLost(true);
        assertTrue(gameBoard.isBallLost());
    }

    @Test
    void setPauseFlag() {
        gameBoard.setPauseFlag(false);
        assertTrue(gameBoard.isNotPaused());
    }

    @Test
    void setEndFlag() {
        gameBoard.setEndFlag(true);
        assertTrue(gameBoard.isEnded());
    }

    @Test
    void setBallStartPoint() {
        gameBoard.setBallStartPoint(new Point(100,100));
        assertEquals(gameBoard.getBallStartPoint(),new Point(100,100));
    }

    @Test
    void setPlayerStartPoint() {
        gameBoard.setPlayerStartPoint(new Point(100,100));
        assertEquals(gameBoard.getPlayerStartPoint(),new Point(100,100));
    }

    @Test
    void setGameMessages() {
        gameBoard.setGameMessages(0,"Test");
        assertEquals(gameBoard.getGameMessages(0),"Test");
    }
}