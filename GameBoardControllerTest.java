package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardControllerTest {

    GameFrame owner;
    GameBoard gameBoard;
    GameSounds gameSounds;
    GameBoardController controller;

    @BeforeEach
    void setUp() throws IOException {
        owner = new GameFrame();
        gameBoard = new GameBoard(new int[5][11],new Dimension(600,450));
        gameSounds = new GameSounds();
        controller = new GameBoardController(owner,gameBoard,gameSounds,new Dimension(600,450));
    }

    @AfterEach
    void tearDown() {
        owner = null;
        gameBoard = null;
        gameSounds = null;
        controller = null;
    }

    @Test
    void update() {
    }

    @Test
    void movePlayer() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getPlayer().setMoveAmount(5);
        gameBoard.getPlayer().setVerticalMoveAmount(5);
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(205,205));
    }

    @Test
    void moveBall() {
        gameBoard.getBall().setCenter(new Point(100,100));
        gameBoard.getBall().setSpeedX(2);
        gameBoard.getBall().setSpeedY(2);
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(102,102));
    }

    @Test
    void moveLeft() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.moveLeft();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(195,200));
    }

    @Test
    void moveRight() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.moveRight();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(205,200));
    }

    @Test
    void moveUp() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.moveUp();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,195));
    }

    @Test
    void moveDown() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.moveDown();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,205));
    }

    @Test
    void stop() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.moveLeft();
        controller.moveUp();
        controller.stop();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,200));
    }

    @Test
    void reversePauseFlag() {
        gameBoard.setPauseFlag(true);
        controller.reversePauseFlag();
        assertEquals(gameBoard.getStartTime(),(int) java.time.Instant.now().getEpochSecond());
        assertTrue(gameBoard.isNotPaused());
        assertEquals(gameBoard.getMessageFlag(),0);
    }

    @RepeatedTest(5)
    void setBallSpeed() {
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
        controller.setBallSpeed();
        assertTrue(gameBoard.getBall().getSpeedX()>=-2 && gameBoard.getBall().getSpeedX()<=2);
        assertTrue(gameBoard.getBall().getSpeedY()>=-2 && gameBoard.getBall().getSpeedY()<=0);
    }

    @Test
    void reverseX() {
        gameBoard.getBall().setSpeedX(2);
        controller.reverseX();
        assertEquals(gameBoard.getBall().getSpeedX(),-2);
    }

    @Test
    void reverseY() {
        gameBoard.getBall().setSpeedY(1);
        controller.reverseY();
        assertEquals(gameBoard.getBall().getSpeedY(),-1);
    }

    @Test
    void calculateScoreAndTime() {
        gameBoard.setLevel(4);
        gameBoard.setScore(0,1000);
        gameBoard.setTime(0,140);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        gameBoard.setScore(4,400);
        gameBoard.setTime(4,50);
        controller.calculateScoreAndTime();
        assertEquals(gameBoard.getScore(0),600);
        assertEquals(gameBoard.getScore(4),0);
        assertEquals(gameBoard.getTime(0),141);
        assertEquals(gameBoard.getTime(4),51);
    }

    @Test
    void returnPreviousLevelsScore() {
        gameBoard.setScore(1,100);
        gameBoard.setScore(2,200);
        gameBoard.setScore(3,300);
        gameBoard.setLevel(3);
        assertEquals(controller.returnPreviousLevelsScore(),300);
    }

    @Test
    void returnPreviousLevelsTime() {
        gameBoard.setTime(1,20);
        gameBoard.setTime(2,30);
        gameBoard.setTime(3,40);
        gameBoard.setTime(4,50);
        gameBoard.setTime(5,60);
        gameBoard.setLevel(5);
        assertEquals(controller.returnPreviousLevelsTime(),140);
    }

    @Test
    void gameChecks() throws FileNotFoundException {
        gameBoard.setLevel(2);
        controller.gameChecks();
        assertEquals(gameBoard.getMessageFlag(),0);
        assertFalse(gameBoard.isNotPaused());
        assertFalse(gameBoard.isEnded());
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
    }

    @Test
    void resetLevelData() {
        gameBoard.setBrickCount(20);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        gameBoard.setLevel(4);
        gameBoard.setScore(0,1000);
        gameBoard.setTime(0,140);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        gameBoard.setScore(4,400);
        gameBoard.setTime(4,50);
        controller.resetLevelData();
        assertEquals(gameBoard.getBrickCount(),31);
        assertEquals(gameBoard.getBallCount(),3);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertTrue(gameBoard.getBall().getSpeedX()>=-2 && gameBoard.getBall().getSpeedX()<=2);
        assertTrue(gameBoard.getBall().getSpeedY()>=-2 && gameBoard.getBall().getSpeedY()<=0);
        assertFalse(gameBoard.isBallLost());
        assertEquals(gameBoard.getScore(0),600);
        assertEquals(gameBoard.getScore(4),0);
        assertEquals(gameBoard.getTime(0),90);
        assertEquals(gameBoard.getTime(4),0);
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),0);
    }

    @Test
    void nextLevel() {
        gameBoard.setBrickCount(0);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        gameBoard.setLevel(2);
        gameBoard.setScore(0,300);
        gameBoard.setTime(0,50);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        controller.nextLevel(true);
        assertEquals(gameBoard.getLevel(),3);
        assertEquals(gameBoard.getBrickCount(),31);
        assertEquals(gameBoard.getBallCount(),3);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertTrue(gameBoard.getBall().getSpeedX()>=-2 && gameBoard.getBall().getSpeedX()<=2);
        assertTrue(gameBoard.getBall().getSpeedY()>=-2 && gameBoard.getBall().getSpeedY()<=0);
        assertFalse(gameBoard.isBallLost());
        assertEquals(gameBoard.getScore(0),300);
        assertEquals(gameBoard.getScore(3),0);
        assertEquals(gameBoard.getTime(0),50);
        assertEquals(gameBoard.getTime(3),0);
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),0);
    }

    @Test
    void previousLevel() {
        gameBoard.setBrickCount(20);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        gameBoard.setLevel(3);
        gameBoard.setScore(0,600);
        gameBoard.setTime(0,90);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        controller.previousLevel();
        assertEquals(gameBoard.getLevel(),2);
        assertEquals(gameBoard.getBrickCount(),31);
        assertEquals(gameBoard.getBallCount(),3);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertTrue(gameBoard.getBall().getSpeedX()>=-2 && gameBoard.getBall().getSpeedX()<=2);
        assertTrue(gameBoard.getBall().getSpeedY()>=-2 && gameBoard.getBall().getSpeedY()<=0);
        assertFalse(gameBoard.isBallLost());
        assertEquals(gameBoard.getScore(0),100);
        assertEquals(gameBoard.getScore(2),0);
        assertEquals(gameBoard.getTime(0),20);
        assertEquals(gameBoard.getTime(2),0);
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),0);
    }

    @Test
    void resetTotalScoreAndTime() {
        gameBoard.setLevel(4);
        gameBoard.setScore(0,1000);
        gameBoard.setTime(0,140);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        gameBoard.setScore(4,400);
        gameBoard.setTime(4,50);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),600);
        assertEquals(gameBoard.getTime(0),90);
    }

    @Test
    void resetLevelScoreAndTime() {
        gameBoard.setLevel(3);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        controller.resetLevelScoreAndTime();
        assertEquals(gameBoard.getScore(3),0);
        assertEquals(gameBoard.getTime(3),0);
    }

    @Test
    void ballReset() {
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        controller.ballReset();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertTrue(gameBoard.getBall().getSpeedX()>=-2 && gameBoard.getBall().getSpeedX()<=2);
        assertTrue(gameBoard.getBall().getSpeedY()>=-2 && gameBoard.getBall().getSpeedY()<=0);
        assertFalse(gameBoard.isBallLost());
    }

    @Test
    void ballMoveTo() {
        gameBoard.getBall().setCenter(new Point(100,100));
        controller.ballMoveTo(new Point(200,200));
        assertEquals(gameBoard.getBall().getCenter(),new Point(200,200));
    }

    @Test
    void playerMoveTo() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.playerMoveTo(new Point(300,300));
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,300));
    }

    @Test
    void powerUpMoveTo() {
        gameBoard.getPowerUp().setMidPoint(new Point(300,300));
        controller.powerUpMoveTo(new Point(250,250));
        assertEquals(gameBoard.getPowerUp().getMidPoint(),new Point(250,250));
    }

    @Test
    void powerUpRandomSpawn() {
        gameBoard.setLevel(2);
        gameBoard.setTime(2,100);
        gameBoard.setPowerUpSpawns(1);
        gameBoard.getPowerUp().setCollected(false);
        gameBoard.getPowerUp().setSpawned(false);
        controller.powerUpRandomSpawn();
        assertTrue(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),2);
    }

    @Test
    void powerUpCollected() {
        controller.powerUpMoveTo(new Point(100,100));
        controller.ballMoveTo(new Point(110,100));
        assertTrue(controller.powerUpCollected());
        controller.ballMoveTo(new Point(200,100));
        assertFalse(controller.powerUpCollected());
    }

    @Test
    void wallReset() {
    }

    @Test
    void resetBallCount() {
    }

    @Test
    void repair() {
    }

    @Test
    void ballPlayerImpact() {
    }

    @Test
    void findImpacts() {
    }

    @Test
    void findImpact() {
    }

    @Test
    void setImpact() {
    }
}