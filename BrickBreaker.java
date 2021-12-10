package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

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
     * Timer to loop through update and draw cycles.
     */
    private Timer gameTimer;
    /**
     * GameEngine to get the results of user inputs.
     */
    private final GameEngine engine;
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
     * @throws IOException This constructor throws IOException if game background image is not found.
     */
    public BrickBreaker(JFrame owner,int[][] choice,GameSounds gameSounds,Dimension area) throws IOException {
        super();
        this.area = area;
        GameEngine engine = new GameEngine(owner,choice,gameSounds,area);
        this.engine = engine;
        this.initialize();

        // Game loop.
        gameTimer = new Timer(10,e ->{
            try {
                engine.update();
            }
            catch (FileNotFoundException ex) {
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
     */
    private void initialize() { //initialize JFrame
        this.setPreferredSize(area); //set frame size
        this.setFocusable(true); //set focusable
        this.requestFocusInWindow(); //request focus

        this.addKeyListener(new KeyAdapter() {

            /**
             * This method is used to respond to the key inputs by the player.
             * @param keyEvent The key pressed by the player to get the key codes.
             */
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                engine.handleEvent(keyEvent);
            }

            /**
             * This method is used to respond to the key releases by the player.
             * @param keyEvent The key released by the player.
             */
            @Override
            public void keyReleased(KeyEvent keyEvent) { //if key released, stop player
                engine.handleReleaseEvent();
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
                engine.handleMouseClick(mouseEvent);
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
                engine.handleMouseMotion(mouseEvent);
            }
        });
    }

    /**
     * This method is used to draw the graphics for the entire game. The game renderer is called from here to render
     * the game. The detection boxes for the pause menu options are also drawn here.
     * @param g This parameter is used to get the graphics to draw the game.
     */
    public void paint(Graphics g) {
        engine.render(g);
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * This method is used to signal to the game that the focus is lost and to show the focus lost message and pause the
     * game.
     */
    public void onLostFocus(){
        engine.onLostFocus();
    }
}
