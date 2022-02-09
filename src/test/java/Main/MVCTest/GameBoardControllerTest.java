package Main.MVCTest;

import Main.MVC.GameBoard;
import Main.MVC.GameBoardController;
import Main.Others.GameSounds;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.awt.*;

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
}