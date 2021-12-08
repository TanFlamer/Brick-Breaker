package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.io.FileNotFoundException;

/**
 * Public class GameBoard is responsible for loading in the GameBoard, Controller and Renderer. It connects the game
 * timer to the game data which is updated and rendered every cycle to simulate gameplay. It also handles all the
 * inputs by the user such as key presses and mouse clicks.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class GameEngine {

    /**
     * Continue button area.
     */
    private Rectangle continueButtonRect;
    /**
     * Exit button area.
     */
    private Rectangle exitButtonRect;
    /**
     * Restart button area.
     */
    private Rectangle restartButtonRect;
    /**
     * Continue string to get an estimate of options area.
     */
    private static final String CONTINUE = "Continue";
    /**
     * Text size of 30 for the font to get an estimate of options area.
     */
    private static final int TEXT_SIZE = 30;
    /**
     * Font for pause menu to get an estimate of options area.
     */
    private final Font menuFont;
    /**
     * The dimensions of the game screen to draw the pause menu.
     */
    private final Dimension area;
    /**
     * JFrame to get mouse cursor to change to hand cursor.
     */
    private final JFrame owner;
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
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE); //menu font
        this.owner = owner;
        this.area = area;
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

    public void handleReleaseEvent(){
        controller.stop();
    }

    public void handleMouseClick(MouseEvent mouseEvent){

        Point p = mouseEvent.getPoint(); //get mouse click point
        if(!gameBoard.isShowPauseMenu()) //if game not paused
            return; //return
        if(continueButtonRect.contains(p)){ //if continue pressed
            gameBoard.setShowPauseMenu(false); //close pause menu
        }
        else if(restartButtonRect.contains(p)){ //if restart pressed
            gameBoard.setMessageFlag(4);
            controller.resetLevelData();
            gameBoard.setShowPauseMenu(false); //close pause menu
        }
        else if(exitButtonRect.contains(p)){ //if exit pressed
            System.exit(0); //close game
        }
    }

    public void handleMouseMotion(MouseEvent mouseEvent){

        Point p = mouseEvent.getPoint(); //get mouse position
        if(exitButtonRect != null && gameBoard.isShowPauseMenu()) { //if pause menu shown and exit button is drawn
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                owner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //if mouse on button, show hand cursor
            else //else
                owner.setCursor(Cursor.getDefaultCursor()); //use default mouse cursor
        }
        else{ //if pause menu not shown
            owner.setCursor(Cursor.getDefaultCursor()); //use default mouse cursor
        }
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
        drawPauseMenuChoices(g);
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

    /**
     * This method is used to draw the detection boxes for the pause menu options.
     * @param g This parameter is used to get the graphics to draw the detection boxes for the pause menu options.
     */
    private void drawPauseMenuChoices(Graphics g){

        Graphics2D g2d = (Graphics2D)g;

        g2d.setFont(menuFont); //set menu font

        int x = area.width / 8; //get position of continue button
        int y = area.height / 4;

        if(continueButtonRect == null){ //if continue button not drawn
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds(); //get rectangle shape button for continue
            continueButtonRect.setLocation(x,y-continueButtonRect.height); //set location of continue button
        }

        y *= 2; //get position of restart button

        if(restartButtonRect == null){ //if restart button not drawn
            restartButtonRect = (Rectangle) continueButtonRect.clone(); //clone rectangle of continue as own shape
            restartButtonRect.setLocation(x,y-restartButtonRect.height); //set location of restart button
        }

        y *= 3.0/2; //get position of exit button

        if(exitButtonRect == null){ //if exit button not drawn
            exitButtonRect = (Rectangle) continueButtonRect.clone(); //clone rectangle of continue as own
            exitButtonRect.setLocation(x,y-exitButtonRect.height); //set location of exit button
        }
    }

    /**
     * This method is used to signal to the game that the focus is lost and to show the focus lost message and pause the
     * game.
     */
    public void onLostFocus(){
        if(gameBoard.isNotPaused()){
            controller.reversePauseFlag();
        }
        gameBoard.setMessageFlag(5);
    }
}
