package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.io.FileNotFoundException;

/**
 * Public class BrickBreaker is responsible for loading in the GameEngine to start generating the game. The gamer timer
 * is also started and loops through update and draw cycles to simulate gameplay. Game listeners can be found here,
 * and they keep track of the key inputs and mouse inputs on the pause menu. The detection boxes for the pause menu
 * options are created here. The DebugConsole is also created here.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class BrickBreaker extends JComponent {

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
     * Timer to loop through update and draw cycles.
     */
    private Timer gameTimer;
    /**
     * GameEngine to get the results of user inputs.
     */
    private final GameEngine engine;
    /**
     * DebugConsole to debug the game.
     */
    private final DebugConsole debugConsole;
    /**
     * The dimensions of the game screen to draw the pause menu.
     */
    private final Dimension area;

    /**
     * This constructor is used to start the game by calling the GameEngine. The game timer is also started to start the
     * update and draw cycles. Game listeners are also added to keep track and respond to user inputs. The DebugConsole
     * is also loaded in.
     * @param owner The JFrame screen used to center the DebugConsole and the game.
     * @param choice The custom choice of the player in customising the game levels.
     * @param gameSounds The BGM and sound effects of the game.
     * @param area The area of the game screen to draw the pause menu.
     */
    public BrickBreaker(JFrame owner,int[][] choice,GameSounds gameSounds,Dimension area) {
        super();
        this.area = area;
        GameEngine engine = new GameEngine(owner,choice,this,gameSounds,area);
        this.engine = engine;
        this.initialize(owner);
        debugConsole = new DebugConsole(owner,engine,this,gameSounds);
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE); //menu font

        // Game loop.
        gameTimer = new Timer(10,e ->{
            try {
                engine.update();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            repaint();

            if(engine.getGameBoard().isEnded())
                gameTimer.stop();
        });
        gameTimer.start();
    }

    /**
     * This method is used to add listeners to the JFrame to receive player inputs for the game.
     * @param owner This parameter is used to set the mouse cursor to hand cursor when pause menu is opened and
     *              mouse cursor is inside an option.
     */
    private void initialize(JFrame owner) { //initialize JFrame
        this.setPreferredSize(area); //set frame size
        this.setFocusable(true); //set focusable
        this.requestFocusInWindow(); //request focus

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                handleEvent(keyEvent);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) { //if key released, stop player
                engine.getController().stop();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            /**
             * This mouse listener is called when the mouse is clicked and relevant events are loaded if the pause
             * menu is loaded and the mouse is inside a button.
             * @param mouseEvent This parameter is used to track mouse clicks.
             */
            @Override
            public void mouseClicked(MouseEvent mouseEvent) { //if mouse clicked

                Point p = mouseEvent.getPoint(); //get mouse click point
                if(!engine.getGameBoard().isShowPauseMenu()) //if game not paused
                    return; //return
                if(continueButtonRect.contains(p)){ //if continue pressed
                    engine.getGameBoard().setShowPauseMenu(false); //close pause menu
                    repaint(); //repaint components
                }
                else if(restartButtonRect.contains(p)){ //if restart pressed
                    engine.getGameBoard().setMessageFlag(4);
                    engine.getController().resetLevelData();
                    engine.getGameBoard().setShowPauseMenu(false); //close pause menu
                    repaint(); //repaint components
                }
                else if(exitButtonRect.contains(p)){ //if exit pressed
                    System.exit(0); //close game
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            /**
             * This mouse motion listener is called when the mouse is moved and the mouse cursor is changed to
             * hand cursor if the mouse cursor is inside a button in the pause menu.
             * @param mouseEvent This parameter is used to track mouse movement.
             */
            @Override
            public void mouseMoved(MouseEvent mouseEvent) { //if mouse moved

                Point p = mouseEvent.getPoint(); //get mouse position
                if(exitButtonRect != null && engine.getGameBoard().isShowPauseMenu()) { //if pause menu shown and exit button is drawn
                    if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                        owner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //if mouse on button, show hand cursor
                    else //else
                        owner.setCursor(Cursor.getDefaultCursor()); //use default mouse cursor
                }
                else{ //if pause menu not shown
                    owner.setCursor(Cursor.getDefaultCursor()); //use default mouse cursor
                }
            }
        });
    }

    /**
     * This method is used to respond to the key inputs by the player.
     * @param keyEvent The key presses by the player to get the key codes.
     */
    public void handleEvent(KeyEvent keyEvent) {

        switch(keyEvent.getKeyCode()){

            case KeyEvent.VK_A: //press A
                engine.getController().moveLeft(); //player moves left
                break;

            case KeyEvent.VK_D: //press D
                engine.getController().moveRight(); //player moves right
                break;

            case KeyEvent.VK_ESCAPE: //press esc
                if(!engine.getGameBoard().isEnded()) {
                    engine.getGameBoard().setShowPauseMenu(!engine.getGameBoard().isShowPauseMenu());
                    repaint(); //repaint components
                }
                if(engine.getGameBoard().isNotPaused())
                    engine.getController().reversePauseFlag();
                break;

            case KeyEvent.VK_SPACE: //press space
                if(!engine.getGameBoard().isShowPauseMenu()) //if game not paused
                    engine.getController().reversePauseFlag();
                break;

            case KeyEvent.VK_F1: //press f1 + alt + shift
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()) {
                    if(engine.getGameBoard().isNotPaused()){
                        engine.getController().reversePauseFlag();
                    }
                    if(!engine.getGameBoard().isEnded())
                        debugConsole.setVisible(true); //show debug console
                }
                break;

            default: //press anything else
                engine.getController().stop(); //stop player
        }
    }

    /**
     * This method is used to draw the graphics for the entire game. The game renderer is called from here to render
     * the game. The detection boxes for the pause menu options are also drawn here.
     * @param g This parameter is used to get the graphics to draw the game.
     */
    public void paint(Graphics g) {
        engine.render(g);
        drawPauseMenuChoices(g);
        Toolkit.getDefaultToolkit().sync();
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
        if(engine.getGameBoard().isNotPaused()){
            engine.getController().reversePauseFlag();
        }
        engine.getGameBoard().setMessageFlag(5);
        repaint(); //repaint components
    }
}
