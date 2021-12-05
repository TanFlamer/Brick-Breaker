import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.io.FileNotFoundException;

public class BrickBreaker extends JComponent {

    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;

    private static final String CONTINUE = "Continue";

    private static final int TEXT_SIZE = 30;

    private Font menuFont;

    private Timer gameTimer;

    private GameEngine engine;
    private DebugConsole debugConsole;

    int[][] choice;

    public BrickBreaker(JFrame owner,int[][] choice) {
        super();
        this.choice = choice;
        GameEngine engine = new GameEngine(owner,choice,this);
        this.engine = engine;
        this.initialize(owner);
        debugConsole = new DebugConsole(owner,engine,this);
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE); //menu font

        // Game loop.
        gameTimer = new Timer(10,e ->{
            try {
                engine.update();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            repaint();

            if(engine.getController().getEndFlag())
                gameTimer.stop();
        });
        gameTimer.start();
    }

    private void initialize(JFrame owner) { //initialize JFrame
        this.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT)); //set frame size
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
                    engine.getController().ballReset();
                    engine.getController().wallReset();
                    engine.getController().resetLevelScoreAndTime();
                    engine.getController().resetTotalScoreAndTime();
                    engine.getGameBoard().setShowPauseMenu(false); //close pause menu
                    engine.getGameBoard().getPowerUp().setSpawned(false);
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

    public void handleEvent(KeyEvent keyEvent) {

        switch(keyEvent.getKeyCode()){

            case KeyEvent.VK_A: //press A
                engine.getController().moveLeft(); //player moves left
                break;

            case KeyEvent.VK_D: //press D
                engine.getController().moveRight(); //player moves right
                break;

            case KeyEvent.VK_ESCAPE: //press esc
                engine.getGameBoard().setShowPauseMenu(!engine.getGameBoard().isShowPauseMenu());
                if(!engine.getController().getEndFlag())
                    repaint(); //repaint components
                if(!engine.getController().getPauseFlag()){
                    engine.getController().reversePauseFlag();
                }
                break;

            case KeyEvent.VK_SPACE: //press space
                if(!engine.getGameBoard().isShowPauseMenu()) { //if game not paused
                    engine.getController().reversePauseFlag();
                }
                break;

            case KeyEvent.VK_F1: //press f1 + alt + shift
                if(keyEvent.isAltDown() && keyEvent.isShiftDown()) {
                    if(!engine.getController().getPauseFlag()){
                        engine.getController().reversePauseFlag();
                    }
                    debugConsole.setVisible(true); //show debug console
                }
                break;

            default: //press anything else
                engine.getController().stop(); //stop player
        }
    }

    public void paint(Graphics g) {
        engine.render(g);
        drawPauseMenuChoices(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawPauseMenuChoices(Graphics g){

        Graphics2D g2d = (Graphics2D)g;

        g2d.setFont(menuFont); //set menu font

        int x = DEF_WIDTH / 8; //get position of continue button
        int y = DEF_HEIGHT / 4;

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

    public void onLostFocus(){
        if(!engine.getController().getPauseFlag()){
            engine.getController().reversePauseFlag();
        }
        engine.getGameBoard().setMessageFlag(5);
        repaint(); //repaint components
    }
}
