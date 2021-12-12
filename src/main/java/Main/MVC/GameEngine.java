package Main.MVC;

import Main.Consoles.DebugConsole;
import Main.Others.GameSounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    private final Renderer renderer;
    /**
     * DebugConsole to debug the game.
     */
    private final DebugConsole debugConsole;
    /**
     * GameSounds to play BGM and sound effects for the game.
     */
    private final GameSounds gameSounds;
    /**
     * Choices of player in CustomConsole to correctly manipulate game data.
     */
    private final int[][] choice;

    /**
     * This constructor initialises the GameBoard, Controller and Renderer so that the game data can be saved,
     * manipulated and rendered.
     * @param owner JFrame used to center screen in ScoreBoard.
     * @param choice Player choice from custom console to be generated into levels in GameBoard.
     * @param gameSounds GameSounds to add BGM and sound effects to the game.
     * @param area Dimensions of the game screen to set game boundaries and draw pause menu.
     * @throws IOException This constructor throws IOException if game background image is not found.
     */
    public GameEngine(JFrame owner, int[][] choice, GameSounds gameSounds, Dimension area) throws IOException {
        gameBoard = new GameBoard(choice,area);
        controller = new GameBoardController(owner,gameBoard,gameSounds,area);
        renderer = new GameBoardRenderer(gameBoard,area);
        debugConsole = new DebugConsole(owner,this, gameSounds);
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);
        this.choice = choice;
        this.gameSounds = gameSounds;
        this.owner = owner;
        this.area = area;
    }

    /**
     * This method is used to respond to the key inputs by the player.
     * @param keyEvent The key presses by the player to get the key codes.
     */
    public void handleEvent(KeyEvent keyEvent) {

        switch(keyEvent.getKeyCode()){

            case KeyEvent.VK_A:
                controller.moveLeft();
                break;

            case KeyEvent.VK_D:
                controller.moveRight();
                break;

            case KeyEvent.VK_W:
                if(choice[gameBoard.getLevel()-1][10]==1)
                    controller.moveUp();
                break;

            case KeyEvent.VK_S:
                if(choice[gameBoard.getLevel()-1][10]==1)
                    controller.moveDown();
                break;

            case KeyEvent.VK_UP:
                if(choice[gameBoard.getLevel()-1][10]==1)
                    controller.minusSpeedY();
                break;

            case KeyEvent.VK_DOWN:
                if(choice[gameBoard.getLevel()-1][10]==1)
                    controller.addSpeedY();
                break;

            case KeyEvent.VK_LEFT:
                if(choice[gameBoard.getLevel()-1][10]==1)
                    controller.minusSpeedX();
                break;

            case KeyEvent.VK_RIGHT:
                if(choice[gameBoard.getLevel()-1][10]==1)
                    controller.addSpeedX();
                break;

            case KeyEvent.VK_ESCAPE:
                if(!gameBoard.isEnded()) {
                    gameBoard.setShowPauseMenu(!gameBoard.isShowPauseMenu());
                }
                if(gameBoard.isNotPaused())
                    controller.reversePauseFlag();
                break;

            case KeyEvent.VK_SPACE:
                if(!gameBoard.isShowPauseMenu())
                    controller.reversePauseFlag();
                break;

            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()) {
                    if(gameBoard.isNotPaused()){
                        controller.reversePauseFlag();
                    }
                    if(!gameBoard.isEnded())
                        debugConsole.setVisible(true);
                }
                break;

            default:
                controller.stop();
        }
    }

    /**
     * This method is used to respond to the key releases by the player.
     */
    public void handleReleaseEvent(){
        controller.stop();
    }

    /**
     * This method is used to respond to the mouse clicks by the player.
     * @param mouseEvent The mouse clicks by the player.
     */
    public void handleMouseClick(MouseEvent mouseEvent){

        Point p = mouseEvent.getPoint();
        if(!gameBoard.isShowPauseMenu())
            return;
        if(continueButtonRect.contains(p)){
            gameBoard.setShowPauseMenu(false);
        }
        else if(restartButtonRect.contains(p)){
            gameBoard.setMessageFlag(4);
            controller.resetLevelData();
            gameBoard.setShowPauseMenu(false);
            gameSounds.setBgm("BGM"+gameBoard.getLevel());
            gameSounds.getBgm().stop();
        }
        else if(exitButtonRect.contains(p)){
            System.exit(0);
        }
    }

    /**
     * This method is used to respond to the mouse motion of the player.
     * @param mouseEvent The mouse motion of the player.
     */
    public void handleMouseMotion(MouseEvent mouseEvent){

        Point p = mouseEvent.getPoint();
        if(exitButtonRect != null && gameBoard.isShowPauseMenu()) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                owner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                owner.setCursor(Cursor.getDefaultCursor());
        }
        else{
            owner.setCursor(Cursor.getDefaultCursor());
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

        g2d.setFont(menuFont);

        int x = area.width / 8;
        int y = area.height / 4;

        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds();
            continueButtonRect.setLocation(x,y-continueButtonRect.height);
        }

        y *= 2;

        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone();
            restartButtonRect.setLocation(x,y-restartButtonRect.height);
        }

        y *= 3.0/2;

        if(exitButtonRect == null){
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
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
