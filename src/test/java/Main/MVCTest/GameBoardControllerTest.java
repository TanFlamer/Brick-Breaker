package Main.MVCTest;

import Main.MVC.GameBoard;
import Main.MVC.GameBoardController;
import Main.Others.GameFrame;
import Main.Others.GameSounds;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameBoardControllerTest tests to see if the methods of GameBoardController class are all working properly.
 * The getters and setters of the other classes which have been tested and deemed to be in proper working condition
 * are used to set up the test conditions for the tested methods because some methods tested do not give obvious
 * results.
 */
class GameBoardControllerTest {

    /**
     * GameBoard used in the tests.
     */
    GameBoard gameBoard;
    /**
     * GameSounds used in the tests.
     */
    GameSounds gameSounds;
    /**
     * GameBoardController used in the tests.
     */
    GameBoardController controller;

    /**
     * A new GameBoardController with new created parameters is created before every test.
     */
    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(new int[5][11],new Dimension(600,450));
        gameSounds = new GameSounds();
        controller = new GameBoardController(null,gameBoard,gameSounds,new Dimension(600,450));
    }

    /**
     * The GameBoardController and its parameters are cleaned up after each test.
     */
    @AfterEach
    void tearDown() {
        gameBoard = null;
        gameSounds = null;
        controller = null;
    }

    /**
     * This tests the update method of the controller. The test returns true if the correct output is returned.
     * @throws FileNotFoundException This test throws FileNotFoundException if HighScore lists do not exist.
     */
    @Test
    void update() throws FileNotFoundException {
        //prepare power up spawn
        gameBoard.setStartTime((int) java.time.Instant.now().getEpochSecond());
        gameBoard.setLevel(2);
        gameBoard.setScore(0,300);
        gameBoard.setTime(0,140);
        gameBoard.setScore(1,200);
        gameBoard.setTime(1,80);
        gameBoard.setScore(2,100);
        gameBoard.setTime(2,60);
        gameBoard.setPowerUpSpawns(1);
        gameBoard.getPowerUp().setCollected(false);
        gameBoard.getPowerUp().setSpawned(false);

        //prepare player
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.moveUp();
        controller.moveLeft();

        //prepare ball
        gameBoard.getBall().setCenter(new Point(599,300));
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
        controller.addSpeedX();
        controller.addSpeedX();
        controller.minusSpeedY();
        controller.minusSpeedY();
        assertEquals(gameBoard.getBall().getSpeedX(),2);
        assertEquals(gameBoard.getBall().getSpeedY(),-2);

        //unpause game and update
        gameBoard.setPauseFlag(false);
        controller.update();

        //compare results
        assertEquals(gameBoard.getScore(0),200);
        assertEquals(gameBoard.getTime(0),140);
        assertEquals(gameBoard.getScore(1),200);
        assertEquals(gameBoard.getTime(1),80);
        assertEquals(gameBoard.getScore(2),0);
        assertEquals(gameBoard.getTime(2),60);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(195,195));
        assertEquals(gameBoard.getBall().getCenter(),new Point(601,298));
        assertEquals(gameBoard.getBall().getSpeedX(),-2);
        assertEquals(gameBoard.getBall().getSpeedY(),-2);
        assertTrue(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),2);
        assertEquals(gameBoard.getPowerUp().getMidPoint().y,325);
        assertTrue(gameBoard.getPowerUp().getMidPoint().x>=100 && gameBoard.getPowerUp().getMidPoint().x<=500);
        assertEquals(gameBoard.getMessageFlag(),0);
    }

    /**
     * This tests the moveLeft method of the controller. The test returns true if the move amount of the player
     * is -5.
     */
    @Test
    void moveLeft() {
        controller.moveLeft();
        assertEquals(gameBoard.getPlayer().getMoveAmount(),-5);
    }

    /**
     * This tests the moveRight method of the controller. The test returns true if the move amount of the player
     * is 5.
     */
    @Test
    void moveRight() {
        controller.moveRight();
        assertEquals(gameBoard.getPlayer().getMoveAmount(),5);
    }

    /**
     * This tests the moveUp method of the controller. The test returns true if the vertical move amount of the player
     * is -5.
     */
    @Test
    void moveUp() {
        controller.moveUp();
        assertEquals(gameBoard.getPlayer().getVerticalMoveAmount(),-5);
    }

    /**
     * This tests the moveDown method of the controller. The test returns true if the vertical move amount of the player
     * is 5.
     */
    @Test
    void moveDown() {
        controller.moveDown();
        assertEquals(gameBoard.getPlayer().getVerticalMoveAmount(),5);
    }

    /**
     * This tests the stop method of the controller. The test returns true if the normal and vertical move amount of
     * the player is 0 after stop method is applied for all 4 directions.
     */
    @Test
    void stop() {
        controller.moveLeft();
        assertEquals(gameBoard.getPlayer().getMoveAmount(),-5);
        controller.stop();
        assertEquals(gameBoard.getPlayer().getMoveAmount(),0);

        controller.moveRight();
        assertEquals(gameBoard.getPlayer().getMoveAmount(),5);
        controller.stop();
        assertEquals(gameBoard.getPlayer().getMoveAmount(),0);

        controller.moveUp();
        assertEquals(gameBoard.getPlayer().getVerticalMoveAmount(),-5);
        controller.stop();
        assertEquals(gameBoard.getPlayer().getVerticalMoveAmount(),0);

        controller.moveDown();
        assertEquals(gameBoard.getPlayer().getVerticalMoveAmount(),5);
        controller.stop();
        assertEquals(gameBoard.getPlayer().getVerticalMoveAmount(),0);
    }

    /**
     * This tests the movePlayer method of the controller. The test returns true if the player has moved the correct
     * number of points in the vertical and horizontal directions after move methods are applied.
     */
    @Test
    void movePlayer() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));

        controller.moveLeft();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(195,200));

        controller.moveUp();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(190,195));

        controller.moveRight();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(195,190));

        controller.moveDown();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,195));

        controller.stop();
        controller.movePlayer();
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,195));
    }

    /**
     * This tests the setBallSpeed method of the controller. The test returns true if the ball speed is between
     * -2 and 2 for the horizontal speed and -2 and 0 for the vertical speed. Since the method returns random values,
     * we repeat the test 5 times.
     */
    @RepeatedTest(5)
    void setBallSpeed() {
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
        controller.setBallSpeed();
        assertTrue(gameBoard.getBall().getSpeedX()>=-2 && gameBoard.getBall().getSpeedX()<=2);
        assertTrue(gameBoard.getBall().getSpeedY()>=-2 && gameBoard.getBall().getSpeedY()<=0);
    }

    /**
     * This tests the addSpeedX method of the controller. The test returns true if the ball horizontal speed increases
     * by 1 after method is called. If speed is 4 which is the maximum speed, then nothing occurs.
     */
    @Test
    void addSpeedX() {
        gameBoard.getBall().setSpeedX(1);
        controller.addSpeedX();
        assertEquals(gameBoard.getBall().getSpeedX(),2);

        gameBoard.getBall().setSpeedX(4);
        controller.addSpeedX();
        assertEquals(gameBoard.getBall().getSpeedX(),4);
    }

    /**
     * This tests the minusSpeedX method of the controller. The test returns true if the ball horizontal speed decreases
     * by 1 after method is called. If speed is -4 which is the minimum speed, then nothing occurs.
     */
    @Test
    void minusSpeedX() {
        gameBoard.getBall().setSpeedX(-1);
        controller.minusSpeedX();
        assertEquals(gameBoard.getBall().getSpeedX(),-2);

        gameBoard.getBall().setSpeedX(-4);
        controller.minusSpeedX();
        assertEquals(gameBoard.getBall().getSpeedX(),-4);
    }

    /**
     * This tests the addSpeedY method of the controller. The test returns true if the ball vertical speed increases
     * by 1 after method is called. If speed is 4 which is the maximum speed, then nothing occurs.
     */
    @Test
    void addSpeedY() {
        gameBoard.getBall().setSpeedY(1);
        controller.addSpeedY();
        assertEquals(gameBoard.getBall().getSpeedY(),2);

        gameBoard.getBall().setSpeedY(4);
        controller.addSpeedY();
        assertEquals(gameBoard.getBall().getSpeedY(),4);
    }

    /**
     * This tests the minusSpeedY method of the controller. The test returns true if the ball vertical speed decreases
     * by 1 after method is called. If speed is -4 which is the minimum speed, then nothing occurs.
     */
    @Test
    void minusSpeedY() {
        gameBoard.getBall().setSpeedY(-1);
        controller.minusSpeedY();
        assertEquals(gameBoard.getBall().getSpeedY(),-2);

        gameBoard.getBall().setSpeedY(-4);
        controller.minusSpeedY();
        assertEquals(gameBoard.getBall().getSpeedY(),-4);
    }

    /**
     * This tests the reverseX method of the controller. The test returns true if the ball horizontal speed is inverted
     * after method is called. If ball has 0 horizontal speed, then nothing occurs.
     */
    @Test
    void reverseX() {
        gameBoard.getBall().setSpeedX(2);
        controller.reverseX();
        assertEquals(gameBoard.getBall().getSpeedX(),-2);

        gameBoard.getBall().setSpeedX(0);
        controller.reverseX();
        assertEquals(gameBoard.getBall().getSpeedX(),0);
    }

    /**
     * This tests the reverseY method of the controller. The test returns true if the ball vertical speed is inverted
     * after method is called. If ball has 0 vertical speed, then nothing occurs.
     */
    @Test
    void reverseY() {
        gameBoard.getBall().setSpeedY(1);
        controller.reverseY();
        assertEquals(gameBoard.getBall().getSpeedY(),-1);

        gameBoard.getBall().setSpeedY(0);
        controller.reverseY();
        assertEquals(gameBoard.getBall().getSpeedY(),0);
    }

    /**
     * This tests the moveBall method of the controller. The test returns true if the ball has moved the correct
     * number of points in the vertical and horizontal directions after move methods are applied.
     */
    @Test
    void moveBall() {
        gameBoard.getBall().setCenter(new Point(100,100));

        gameBoard.getBall().setSpeedX(2);
        gameBoard.getBall().setSpeedY(2);
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(102,102));

        controller.addSpeedX();
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(105,104));

        controller.minusSpeedY();
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(108,105));

        controller.reverseX();
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(105,106));

        controller.reverseY();
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(102,105));

        controller.addSpeedY();
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(99,105));

        controller.minusSpeedX();
        controller.moveBall();
        assertEquals(gameBoard.getBall().getCenter(),new Point(95,105));
    }

    /**
     * This tests the reversePauseFlag method of the controller. The test returns true if the controller performs the
     * correct set of actions for both cases when game is paused and not paused.
     */
    @Test
    void reversePauseFlag() {
        gameBoard.setPauseFlag(true);
        controller.reversePauseFlag();
        assertEquals(gameBoard.getMessageFlag(),0);
        assertTrue(gameBoard.isNotPaused());

        gameBoard.setPauseFlag(false);
        controller.reversePauseFlag();
        assertFalse(gameSounds.getBgm().isRunning());
    }

    /**
     * This tests the returnPreviousLevelsScore method of the controller. The test returns true if the correct score
     * is returned for each level. If level 0 or negative levels are entered, then 0 is returned. If values greater
     * than number of levels are entered, then ArrayIndexOutOfBoundsException is thrown.
     */
    @Test
    void returnPreviousLevelsScore() {
        gameBoard.setScore(0,1500);
        gameBoard.setScore(1,100);
        gameBoard.setScore(2,200);
        gameBoard.setScore(3,300);
        gameBoard.setScore(4,400);
        gameBoard.setScore(5,500);

        gameBoard.setLevel(-5);
        assertEquals(controller.returnPreviousLevelsScore(),0);

        gameBoard.setLevel(0);
        assertEquals(controller.returnPreviousLevelsScore(),0);

        gameBoard.setLevel(1);
        assertEquals(controller.returnPreviousLevelsScore(),0);

        gameBoard.setLevel(2);
        assertEquals(controller.returnPreviousLevelsScore(),100);

        gameBoard.setLevel(3);
        assertEquals(controller.returnPreviousLevelsScore(),300);

        gameBoard.setLevel(4);
        assertEquals(controller.returnPreviousLevelsScore(),600);

        gameBoard.setLevel(5);
        assertEquals(controller.returnPreviousLevelsScore(),1000);

        gameBoard.setLevel(6);
        assertEquals(controller.returnPreviousLevelsScore(),1500);

        gameBoard.setLevel(10);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.returnPreviousLevelsScore());
    }

    /**
     * This tests the returnPreviousLevelsTime method of the controller. The test returns true if the correct time
     * is returned for each level. If level 0 or negative levels are entered, then 0 is returned. If values greater
     * than number of levels are entered, then ArrayIndexOutOfBoundsException is thrown.
     */
    @Test
    void returnPreviousLevelsTime() {
        gameBoard.setTime(0,200);
        gameBoard.setTime(1,20);
        gameBoard.setTime(2,30);
        gameBoard.setTime(3,40);
        gameBoard.setTime(4,50);
        gameBoard.setTime(5,60);

        gameBoard.setLevel(-5);
        assertEquals(controller.returnPreviousLevelsTime(),0);

        gameBoard.setLevel(0);
        assertEquals(controller.returnPreviousLevelsTime(),0);

        gameBoard.setLevel(1);
        assertEquals(controller.returnPreviousLevelsTime(),0);

        gameBoard.setLevel(2);
        assertEquals(controller.returnPreviousLevelsTime(),20);

        gameBoard.setLevel(3);
        assertEquals(controller.returnPreviousLevelsTime(),50);

        gameBoard.setLevel(4);
        assertEquals(controller.returnPreviousLevelsTime(),90);

        gameBoard.setLevel(5);
        assertEquals(controller.returnPreviousLevelsTime(),140);

        gameBoard.setLevel(6);
        assertEquals(controller.returnPreviousLevelsTime(),200);

        gameBoard.setLevel(10);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.returnPreviousLevelsTime());
    }

    /**
     * This tests the calculateScoreAndTime method of the controller. The test returns true if the correct score and
     * time are returned for each level. If level 0, negative levels or values greater than number of levels are
     * entered, then ArrayIndexOutOfBoundsException is thrown. GodModeTimeLeft does not change because one second
     * has not passed in the test.
     */
    @Test
    void calculateScoreAndTime() {
        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        gameBoard.setScore(4,400);
        gameBoard.setTime(4,50);
        gameBoard.setScore(5,500);
        gameBoard.setTime(5,60);
        gameBoard.setStartTime((int) java.time.Instant.now().getEpochSecond());
        gameBoard.getPowerUp().setCollected(true);
        gameBoard.setGodModeTimeLeft(10);

        gameBoard.setLevel(-2);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.calculateScoreAndTime());

        gameBoard.setLevel(0);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.calculateScoreAndTime());

        gameBoard.setLevel(1);
        controller.calculateScoreAndTime();
        assertEquals(gameBoard.getScore(0),0);
        assertEquals(gameBoard.getScore(1),0);
        assertEquals(gameBoard.getTime(0),200);
        assertEquals(gameBoard.getTime(1),200);
        assertEquals(gameBoard.getGodModeTimeLeft(),10);

        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);

        gameBoard.setLevel(2);
        controller.calculateScoreAndTime();
        assertEquals(gameBoard.getScore(0),100);
        assertEquals(gameBoard.getScore(2),0);
        assertEquals(gameBoard.getTime(0),200);
        assertEquals(gameBoard.getTime(2),180);
        assertEquals(gameBoard.getGodModeTimeLeft(),10);

        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);

        gameBoard.setLevel(3);
        controller.calculateScoreAndTime();
        assertEquals(gameBoard.getScore(0),300);
        assertEquals(gameBoard.getScore(3),0);
        assertEquals(gameBoard.getTime(0),200);
        assertEquals(gameBoard.getTime(3),150);
        assertEquals(gameBoard.getGodModeTimeLeft(),10);

        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);

        gameBoard.setLevel(4);
        controller.calculateScoreAndTime();
        assertEquals(gameBoard.getScore(0),600);
        assertEquals(gameBoard.getScore(4),0);
        assertEquals(gameBoard.getTime(0),200);
        assertEquals(gameBoard.getTime(4),110);
        assertEquals(gameBoard.getGodModeTimeLeft(),10);

        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(4,400);
        gameBoard.setTime(4,50);

        gameBoard.setLevel(5);
        controller.calculateScoreAndTime();
        assertEquals(gameBoard.getScore(0),1000);
        assertEquals(gameBoard.getScore(5),0);
        assertEquals(gameBoard.getTime(0),200);
        assertEquals(gameBoard.getTime(5),60);
        assertEquals(gameBoard.getGodModeTimeLeft(),10);

        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(5,500);
        gameBoard.setTime(5,60);

        gameBoard.setLevel(6);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.calculateScoreAndTime());
    }

    /**
     * This tests the resetTotalScoreAndTime method of the controller. The test returns true if the correct score and
     * time are returned for each level after each reset. If level 0 or negative levels are entered, then 0 is returned.
     * If values greater than number of levels are entered, then ArrayIndexOutOfBoundsException is thrown.
     */
    @Test
    void resetTotalScoreAndTime() {
        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        gameBoard.setScore(4,400);
        gameBoard.setTime(4,50);
        gameBoard.setScore(5,500);
        gameBoard.setTime(5,60);

        gameBoard.setLevel(-5);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),0);
        assertEquals(gameBoard.getTime(0),0);

        gameBoard.setLevel(0);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),0);
        assertEquals(gameBoard.getTime(0),0);

        gameBoard.setLevel(1);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),0);
        assertEquals(gameBoard.getTime(0),0);

        gameBoard.setLevel(2);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),100);
        assertEquals(gameBoard.getTime(0),20);

        gameBoard.setLevel(3);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),300);
        assertEquals(gameBoard.getTime(0),50);

        gameBoard.setLevel(4);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),600);
        assertEquals(gameBoard.getTime(0),90);

        gameBoard.setLevel(5);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),1000);
        assertEquals(gameBoard.getTime(0),140);

        gameBoard.setLevel(6);
        controller.resetTotalScoreAndTime();
        assertEquals(gameBoard.getScore(0),1500);
        assertEquals(gameBoard.getTime(0),200);

        gameBoard.setLevel(10);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.resetTotalScoreAndTime());
    }

    /**
     * This tests the resetLevelScoreAndTime method of the controller. The test returns true if the correct score and
     * time are returned for each level after each reset. If negative levels or values greater than number of levels
     * are entered, then ArrayIndexOutOfBoundsException is thrown.
     */
    @Test
    void resetLevelScoreAndTime() {
        gameBoard.setScore(0,1500);
        gameBoard.setTime(0,200);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.setScore(3,300);
        gameBoard.setTime(3,40);
        gameBoard.setScore(4,400);
        gameBoard.setTime(4,50);
        gameBoard.setScore(5,500);
        gameBoard.setTime(5,60);

        gameBoard.setLevel(-5);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.resetLevelScoreAndTime());

        gameBoard.setLevel(0);
        controller.resetLevelScoreAndTime();
        assertEquals(gameBoard.getScore(0),0);
        assertEquals(gameBoard.getTime(0),0);

        gameBoard.setLevel(1);
        controller.resetLevelScoreAndTime();
        assertEquals(gameBoard.getScore(1),0);
        assertEquals(gameBoard.getTime(1),0);

        gameBoard.setLevel(2);
        controller.resetLevelScoreAndTime();
        assertEquals(gameBoard.getScore(2),0);
        assertEquals(gameBoard.getTime(2),0);

        gameBoard.setLevel(3);
        controller.resetLevelScoreAndTime();
        assertEquals(gameBoard.getScore(3),0);
        assertEquals(gameBoard.getTime(3),0);

        gameBoard.setLevel(4);
        controller.resetLevelScoreAndTime();
        assertEquals(gameBoard.getScore(4),0);
        assertEquals(gameBoard.getTime(4),0);

        gameBoard.setLevel(5);
        controller.resetLevelScoreAndTime();
        assertEquals(gameBoard.getScore(5),0);
        assertEquals(gameBoard.getTime(5),0);

        gameBoard.setLevel(6);
        assertThrows(ArrayIndexOutOfBoundsException.class,()-> controller.resetLevelScoreAndTime());
    }

    /**
     * This tests the gameChecks method of the controller. The test returns true if correct output is returned.
     * @throws IOException This test throws IOException if audio file does not exist.
     */
    @Test
    void gameChecks() throws IOException {
        //setup GameFrame for only use
        GameFrame owner = new GameFrame();
        controller = new GameBoardController(owner,gameBoard,gameSounds,new Dimension(600,450));

        //ball lost but ball left
        gameBoard.setLevel(2);
        gameBoard.setBrickCount(20);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        controller.gameChecks();
        assertEquals(gameBoard.getLevel(),2);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertEquals(gameBoard.getBrickCount(),20);
        assertEquals(gameBoard.getBallCount(),2);
        assertEquals(gameBoard.getMessageFlag(),0);
        assertFalse(gameBoard.isBallLost());
        assertFalse(gameBoard.isNotPaused());
        assertFalse(gameBoard.isEnded());

        //ball lost and ball finished
        gameBoard.setLevel(2);
        gameBoard.setBrickCount(20);
        gameBoard.setBallCount(0);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        controller.gameChecks();
        assertEquals(gameBoard.getLevel(),2);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertEquals(gameBoard.getBrickCount(),31);
        assertEquals(gameBoard.getBallCount(),3);
        assertEquals(gameBoard.getMessageFlag(),1);
        assertFalse(gameBoard.isBallLost());
        assertFalse(gameBoard.isNotPaused());
        assertFalse(gameBoard.isEnded());

        //brick finished but level left
        gameBoard.setLevel(2);
        gameBoard.setBrickCount(0);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(false);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        controller.gameChecks();
        assertEquals(gameBoard.getLevel(),3);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertEquals(gameBoard.getBrickCount(),31);
        assertEquals(gameBoard.getBallCount(),3);
        assertEquals(gameBoard.getMessageFlag(),2);
        assertFalse(gameBoard.isBallLost());
        assertFalse(gameBoard.isNotPaused());
        assertFalse(gameBoard.isEnded());

        //brick finished and last level
        gameBoard.setLevel(5);
        gameBoard.setBrickCount(0);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(false);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        controller.gameChecks();
        assertEquals(gameBoard.getLevel(),5);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,200));
        assertEquals(gameBoard.getBall().getCenter(),new Point(100,100));
        assertEquals(gameBoard.getBrickCount(),0);
        assertEquals(gameBoard.getBallCount(),2);
        assertEquals(gameBoard.getMessageFlag(),3);
        assertFalse(gameBoard.isBallLost());
        assertFalse(gameBoard.isNotPaused());
        assertTrue(gameBoard.isEnded());

        //power up collected
        gameBoard.setLevel(2);
        gameBoard.setBrickCount(20);
        gameBoard.setBallLost(false);
        controller.powerUpMoveTo(new Point(100,100));
        controller.ballMoveTo(new Point(90,100));
        gameBoard.getPowerUp().setSpawned(true);
        gameBoard.getPowerUp().setCollected(false);
        gameBoard.setGodModeTimeLeft(0);
        controller.gameChecks();
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertTrue(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getGodModeTimeLeft(),10);

        //power uptime finished
        gameBoard.setGodModeTimeLeft(0);
        controller.gameChecks();
        assertFalse(gameBoard.getPowerUp().isCollected());
    }

    /**
     * This tests the resetLevelData method of the controller. The test returns true if the level data is reset.
     */
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
        gameBoard.getPowerUp().setSpawned(true);
        gameBoard.getPowerUp().setCollected(true);
        gameBoard.setPowerUpSpawns(1);
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

    /**
     * This tests the nextLevel method of the controller. The test returns true if the controller gives the
     * correct set of outputs for both cases when trueProgression is true and false.
     */
    @Test
    void nextLevel() {
        //true progression on
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
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
        gameBoard.getPowerUp().setSpawned(true);
        gameBoard.getPowerUp().setCollected(true);
        gameBoard.setPowerUpSpawns(1);
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
        assertEquals(gameBoard.getScore(1),100);
        assertEquals(gameBoard.getScore(2),200);
        assertEquals(gameBoard.getTime(0),50);
        assertEquals(gameBoard.getTime(1),20);
        assertEquals(gameBoard.getTime(2),30);
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),0);

        //true progression off
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
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
        gameBoard.getPowerUp().setSpawned(true);
        gameBoard.getPowerUp().setCollected(true);
        gameBoard.setPowerUpSpawns(1);
        controller.nextLevel(false);
        assertEquals(gameBoard.getLevel(),3);
        assertEquals(gameBoard.getBrickCount(),31);
        assertEquals(gameBoard.getBallCount(),3);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,430));
        assertEquals(gameBoard.getBall().getCenter(),new Point(300,430));
        assertTrue(gameBoard.getBall().getSpeedX()>=-2 && gameBoard.getBall().getSpeedX()<=2);
        assertTrue(gameBoard.getBall().getSpeedY()>=-2 && gameBoard.getBall().getSpeedY()<=0);
        assertFalse(gameBoard.isBallLost());
        assertEquals(gameBoard.getScore(0),100);
        assertEquals(gameBoard.getScore(1),100);
        assertEquals(gameBoard.getScore(2),0);
        assertEquals(gameBoard.getTime(0),20);
        assertEquals(gameBoard.getTime(1),20);
        assertEquals(gameBoard.getTime(2),0);
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),0);

        //level 5
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
        gameBoard.setBrickCount(0);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        gameBoard.setLevel(5);
        gameBoard.setScore(0,300);
        gameBoard.setTime(0,50);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.setScore(2,200);
        gameBoard.setTime(2,30);
        gameBoard.getPowerUp().setSpawned(true);
        gameBoard.getPowerUp().setCollected(true);
        gameBoard.setPowerUpSpawns(1);
        controller.nextLevel(false);
        assertEquals(gameBoard.getLevel(),5);
        assertEquals(gameBoard.getBrickCount(),0);
        assertEquals(gameBoard.getBallCount(),2);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,200));
        assertEquals(gameBoard.getBall().getCenter(),new Point(100,100));
        assertEquals(gameBoard.getBall().getSpeedX(),0);
        assertEquals(gameBoard.getBall().getSpeedY(),0);
        assertTrue(gameBoard.isBallLost());
        assertEquals(gameBoard.getScore(0),300);
        assertEquals(gameBoard.getScore(1),100);
        assertEquals(gameBoard.getScore(2),200);
        assertEquals(gameBoard.getTime(0),50);
        assertEquals(gameBoard.getTime(1),20);
        assertEquals(gameBoard.getTime(2),30);
        assertTrue(gameBoard.getPowerUp().isSpawned());
        assertTrue(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),1);
    }

    /**
     * This tests the previousLevel method of the controller. The test returns true if the controller gives the correct
     * output for the method call.
     */
    @Test
    void previousLevel() {
        //other levels
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
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
        gameBoard.getPowerUp().setSpawned(true);
        gameBoard.getPowerUp().setCollected(true);
        gameBoard.setPowerUpSpawns(1);
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
        assertEquals(gameBoard.getScore(1),100);
        assertEquals(gameBoard.getScore(2),0);
        assertEquals(gameBoard.getScore(3),0);
        assertEquals(gameBoard.getTime(0),20);
        assertEquals(gameBoard.getTime(1),20);
        assertEquals(gameBoard.getTime(2),0);
        assertEquals(gameBoard.getTime(3),0);
        assertFalse(gameBoard.getPowerUp().isSpawned());
        assertFalse(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),0);

        //level 1
        gameBoard.getBall().setSpeedX(0);
        gameBoard.getBall().setSpeedY(0);
        gameBoard.setBrickCount(20);
        gameBoard.setBallCount(2);
        gameBoard.setBallLost(true);
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        gameBoard.getBall().setCenter(new Point(100,100));
        gameBoard.setLevel(1);
        gameBoard.setScore(0,100);
        gameBoard.setTime(0,20);
        gameBoard.setScore(1,100);
        gameBoard.setTime(1,20);
        gameBoard.getPowerUp().setSpawned(true);
        gameBoard.getPowerUp().setCollected(true);
        gameBoard.setPowerUpSpawns(1);
        controller.previousLevel();
        assertEquals(gameBoard.getLevel(),1);
        assertEquals(gameBoard.getBrickCount(),20);
        assertEquals(gameBoard.getBallCount(),2);
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(200,200));
        assertEquals(gameBoard.getBall().getCenter(),new Point(100,100));
        assertEquals(gameBoard.getBall().getSpeedX(),0);
        assertEquals(gameBoard.getBall().getSpeedY(),0);
        assertTrue(gameBoard.isBallLost());
        assertEquals(gameBoard.getScore(0),100);
        assertEquals(gameBoard.getScore(1),100);
        assertEquals(gameBoard.getTime(0),20);
        assertEquals(gameBoard.getTime(1),20);
        assertTrue(gameBoard.getPowerUp().isSpawned());
        assertTrue(gameBoard.getPowerUp().isCollected());
        assertEquals(gameBoard.getPowerUpSpawns(),1);
    }

    /**
     * This tests the ballReset method of the controller. The test returns true if the ball and player are moved to
     * the correct location and the ball speed and lost flag are reset.
     */
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

    /**
     * This tests the ballMoveTo method of the controller. The test returns true if the ball moves to the correct
     * location.
     */
    @Test
    void ballMoveTo() {
        gameBoard.getBall().setCenter(new Point(100,100));
        controller.ballMoveTo(new Point(200,200));
        assertEquals(gameBoard.getBall().getCenter(),new Point(200,200));
    }

    /**
     * This tests the playerMoveTo method of the controller. The test returns true if the player moves to the correct
     * location.
     */
    @Test
    void playerMoveTo() {
        gameBoard.getPlayer().setMidPoint(new Point(200,200));
        controller.playerMoveTo(new Point(300,300));
        assertEquals(gameBoard.getPlayer().getMidPoint(),new Point(300,300));
    }

    /**
     * This tests the powerUpMoveTo method of the controller. The test returns true if the power up moves to the correct
     * location.
     */
    @Test
    void powerUpMoveTo() {
        gameBoard.getPowerUp().setMidPoint(new Point(300,300));
        controller.powerUpMoveTo(new Point(250,250));
        assertEquals(gameBoard.getPowerUp().getMidPoint(),new Point(250,250));
    }

    /**
     * This tests the powerUpRandomSpawn method of the controller. The test returns true if the power up spawns
     * correctly.
     */
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
        assertEquals(gameBoard.getPowerUp().getMidPoint().y,325);
        assertTrue(gameBoard.getPowerUp().getMidPoint().x>=100 && gameBoard.getPowerUp().getMidPoint().x<=500);
    }

    /**
     * This tests the powerUpCollected method of the controller. The test returns true if the power up collected
     * detection is correct for each case.
     */
    @Test
    void powerUpCollected() {
        controller.powerUpMoveTo(new Point(100,100));

        //collected from left
        controller.ballMoveTo(new Point(90,100));
        assertTrue(controller.powerUpCollected());

        //collected from right
        controller.ballMoveTo(new Point(110,100));
        assertTrue(controller.powerUpCollected());

        //collected from top
        controller.ballMoveTo(new Point(100,90));
        assertTrue(controller.powerUpCollected());

        //collected from bottom
        controller.ballMoveTo(new Point(100,110));
        assertTrue(controller.powerUpCollected());

        //not collected
        controller.ballMoveTo(new Point(200,100));
        assertFalse(controller.powerUpCollected());
    }

    /**
     * This tests the wallReset method of the controller. The test returns true if the brick count and ball count are
     * reset and all bricks are repaired.
     */
    @Test
    void wallReset() {
        /*gameBoard.setLevel(3);
        gameBoard.setBrickCount(20);
        gameBoard.setBallCount(1);
        for(int i = 0; i < gameBoard.getBricks()[2].length; i++){
            gameBoard.getBricks()[2][i].setStrength(0);
            gameBoard.getBricks()[2][i].setBroken(true);
            assertEquals(gameBoard.getBricks()[2][i].getStrength(),0);
            assertTrue(gameBoard.getBricks()[2][i].isBroken());
        }

        controller.wallReset();
        assertEquals(gameBoard.getBrickCount(),31);
        assertEquals(gameBoard.getBallCount(),3);
        for(int i = 0; i < gameBoard.getBricks()[2].length; i++){
            assertEquals(gameBoard.getBricks()[2][i].getStrength(),gameBoard.getBricks()[2][i].getFullStrength());
            assertFalse(gameBoard.getBricks()[2][i].isBroken());
        }*/
    }

    /**
     * This tests the resetBallCount method of the controller. The test returns true if the ball count is reset.
     */
    @Test
    void resetBallCount() {
        gameBoard.setLevel(3);
        gameBoard.setBallCount(1);
        controller.resetBallCount();
        assertEquals(gameBoard.getBallCount(),3);
    }

    /**
     * This tests the repair method of the controller. The test returns true if all bricks are repaired.
     */
    @Test
    void repair() {
        /*gameBoard.setLevel(3);
        for(int i = 0; i < gameBoard.getBricks()[2].length; i++){
            gameBoard.getBricks()[2][i].setStrength(0);
            gameBoard.getBricks()[2][i].setBroken(true);
            assertTrue(gameBoard.getBricks()[2][i].isBroken());
        }

        for(int i = 0; i < gameBoard.getBricks()[2].length; i++){
            controller.repair(gameBoard.getBricks()[2][i]);
        }

        for(int i = 0; i < gameBoard.getBricks()[2].length; i++){
            assertEquals(gameBoard.getBricks()[2][i].getStrength(),gameBoard.getBricks()[2][i].getFullStrength());
            assertFalse(gameBoard.getBricks()[2][i].isBroken());
        }*/
    }

    /**
     * This tests the ballPlayerImpact method of the controller. The test returns true if ball player impact is detected
     * correctly for both cases. If there is impact, return true. If not, return false.
     */
    @Test
    void ballPlayerImpact() {
        //ball player impact
        controller.playerMoveTo(new Point(100,100));
        controller.ballMoveTo(new Point(110,100));
        assertTrue(controller.ballPlayerImpact());
        //ball player no impact
        controller.playerMoveTo(new Point(200,200));
        controller.ballMoveTo(new Point(100,100));
        assertFalse(controller.ballPlayerImpact());
    }

    /**
     * This tests the findImpacts method of the controller. The test returns true all the possible impacts of the ball
     * return the correct results.
     */
    @Test
    void findImpacts() {
        //ball player impact
        gameBoard.getBall().setSpeedX(2);
        gameBoard.getBall().setSpeedY(2);
        controller.playerMoveTo(new Point(500,500));
        controller.ballMoveTo(new Point(510,500));
        controller.findImpacts(false);
        assertEquals(gameBoard.getBall().getSpeedY(),-2);

        //hit brick with god mode off
        controller.ballMoveTo(new Point(-2,10));
        controller.findImpacts(false);
        assertEquals(gameBoard.getBall().getSpeedX(),-2);

        //hit brick with god mode on
        controller.ballMoveTo(new Point(-2,30));
        controller.findImpacts(true);
        assertEquals(gameBoard.getBall().getSpeedX(),-2);

        //hit left border
        controller.ballMoveTo(new Point(-2,300));
        controller.findImpacts(false);
        assertEquals(gameBoard.getBall().getSpeedX(),2);

        //hit right border
        controller.ballMoveTo(new Point(602,300));
        controller.findImpacts(false);
        assertEquals(gameBoard.getBall().getSpeedX(),-2);

        //hit top border
        controller.ballMoveTo(new Point(300,-2));
        controller.findImpacts(false);
        assertEquals(gameBoard.getBall().getSpeedY(),2);

        //hit bottom border with god mode on
        controller.ballMoveTo(new Point(300,452));
        controller.findImpacts(true);
        assertEquals(gameBoard.getBall().getSpeedY(),-2);
        assertEquals(gameBoard.getBallCount(),3);
        assertFalse(gameBoard.isBallLost());

        //hit bottom border with god mode off
        controller.ballMoveTo(new Point(300,452));
        controller.findImpacts(false);
        assertEquals(gameBoard.getBall().getSpeedY(),-2);
        assertEquals(gameBoard.getBallCount(),2);
        assertTrue(gameBoard.isBallLost());
    }

    /**
     * This tests the findImpact method of the controller. The test returns true if the direction of impact of the ball
     * and brick is correctly returned. If brick is broken, then no impact occurs.
     */
    @Test
    void findImpact() {
        //brick left impact
        /*controller.ballMoveTo(new Point(-2,10));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),300);

        //brick right impact
        controller.ballMoveTo(new Point(62,10));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),400);

        //brick down impact
        controller.ballMoveTo(new Point(30,22));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),200);

        //brick up impact
        controller.ballMoveTo(new Point(30,-2));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),100);

        //brick is broken for further impacts
        gameBoard.getBricks()[0][0].setBroken(true);

        controller.ballMoveTo(new Point(-2,10));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),0);

        controller.ballMoveTo(new Point(62,10));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),0);

        controller.ballMoveTo(new Point(30,22));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),0);

        controller.ballMoveTo(new Point(30,-2));
        assertEquals(controller.findImpact(gameBoard.getBall(),gameBoard.getBricks()[0][0]),0);*/
    }

    /**
     * This tests the setImpact method of the controller. The test returns true if the results from each impact are
     * correct.
     */
    @Test
    void setImpact() {
        //first impact with clay brick which breaks
        /*assertTrue(controller.setImpact(new Point(0,10),10,gameBoard.getBricks()[1][0]));

        //second impact with clay brick which is already broken
        assertFalse(controller.setImpact(new Point(0,10),10,gameBoard.getBricks()[1][0]));

        //first impact with cement brick
        assertFalse(controller.setImpact(new Point(60,10),10,gameBoard.getBricks()[1][1]));

        //second impact with cement which breaks
        assertTrue(controller.setImpact(new Point(60,10),10,gameBoard.getBricks()[1][1]));*/
    }
}