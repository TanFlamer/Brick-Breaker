package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

/**
 * Public class GameBoard is responsible for loading in the GameBoard, Controller and Renderer. It connects the game
 * timer to the game data which is updated and rendered every cycle to simulate gameplay.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class GameEngine {

    /**
     * GameBoard to hold all game data.
     */
    private final GameBoard gameBoard;
    /**
     * Controller to manipulate game data.
     */
    private final GameBoardController controller;
    /**
     * Renderer to render game graphics.
     */
    private final GameBoardRenderer renderer;
    /**
     * DebugConsole to debug the game.
     */
    private final DebugConsole debugConsole;

    /**
     * This constructor initialises the GameBoard, Controller and Renderer so that the game data can be saved,
     * manipulated and rendered.
     * @param owner JFrame used to center screen in ScoreBoard.
     * @param choice Player choice from custom console to be generated into levels in GameBoard.
     * @param brickBreaker BrickBreaker to be repainted after ScoreBoard closes.
     * @param gameSounds GameSounds to add BGM and sound effects to the game.
     * @param area Dimensions of the game screen to set game boundaries and draw pause menu.
     */
    public GameEngine(JFrame owner,int[][] choice,BrickBreaker brickBreaker,GameSounds gameSounds,Dimension area) {
        gameBoard = new GameBoard(choice,area);
        controller = new GameBoardController(owner,gameBoard,brickBreaker,gameSounds,area);
        renderer = new GameBoardRenderer(gameBoard,area);
        debugConsole = new DebugConsole(owner,this,brickBreaker,gameSounds);
    }

    /**
     * This method is used to respond to the key inputs by the player.
     * @param keyEvent The key presses by the player to get the key codes.
     */
    public void handleEvent(KeyEvent keyEvent) {

        switch(keyEvent.getKeyCode()){

            case KeyEvent.VK_A: //press A
                controller.moveLeft(); //player moves left
                break;

            case KeyEvent.VK_D: //press D
                controller.moveRight(); //player moves right
                break;

            case KeyEvent.VK_ESCAPE: //press esc
                if(!gameBoard.isEnded()) {
                    gameBoard.setShowPauseMenu(!gameBoard.isShowPauseMenu());
                }
                if(gameBoard.isNotPaused())
                    controller.reversePauseFlag();
                break;

            case KeyEvent.VK_SPACE: //press space
                if(!gameBoard.isShowPauseMenu()) //if game not paused
                    controller.reversePauseFlag();
                break;

            case KeyEvent.VK_F1: //press f1 + alt + shift
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()) {
                    if(gameBoard.isNotPaused()){
                        controller.reversePauseFlag();
                    }
                    if(!gameBoard.isEnded())
                        debugConsole.setVisible(true); //show debug console
                }
                break;

            default: //press anything else
                controller.stop(); //stop player
        }
    }

    public void handleReleaseEvent(KeyEvent keyEvent){
        controller.stop();
    }

    /**
     * This method calls the update method in Controller to update game data.
     * @throws FileNotFoundException This method throws FileNotFoundException when audio file is not found.
     */
    public void update() throws FileNotFoundException {
        controller.update();
    }

    /**
     * This method calls the render method in Renderer to render game graphics.
     * @param g This is used to control the graphics during the rendering of game graphics.
     */
    public void render(Graphics g) {
        renderer.render(g);
    }

    /**
     * This method returns the Controller to access the methods within.
     * @return The Controller is returned.
     */
    public GameBoardController getController(){
        return controller;
    }

    /**
     * This method returns the GameBoard to access the data within.
     * @return The GameBoard is returned.
     */
    public GameBoard getGameBoard(){
        return gameBoard;
    }
}
