/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Public class GameBoard is responsible for loading the game and keeping track of all the gam inputs and recording
 * the scores and times of the player. Fist, the bricks for each level are created and the debug console is loaded.
 * The game then moves to the first level and the timer is started and keeps track of all inputs, scores and times
 * of the player.
 */
public class GameBoard extends JComponent {

    /**
     * Continue string.
     */
    private static final String CONTINUE = "Continue";
    /**
     * Restart string
     */
    private static final String RESTART = "Restart";
    /**
     * Exit string.
     */
    private static final String EXIT = "Exit";
    /**
     * Pause Menu String
     */
    private static final String PAUSE = "Pause Menu";
    /**
     * Text size of 30.
     */
    private static final int TEXT_SIZE = 30;
    /**
     * Menu colour of green.
     */
    private static final Color MENU_COLOR = new Color(0,255,0);

    /**
     * Screen width of 600.
     */
    private static final int DEF_WIDTH = 600;
    /**
     * Screen height of 450.
     */
    private static final int DEF_HEIGHT = 450;

    /**
     * White colour to clear the background.
     */
    private static final Color BG_COLOR = Color.WHITE;

    /**
     * Timer to scan for player inputs, scores and times.
     */
    private Timer gameTimer;

    /**
     * Public class Wall to load the bricks for the 4 levels.
     */
    private Wall wall;

    /**
     * Message to show current game condition, brick count and ball count.
     */
    private String message;

    /**
     * String to display total score of player.
     */
    private String totalScore;
    /**
     * String to display level score of player.
     */
    private String levelScore;
    /**
     * String to display total time of player.
     */
    private String totalTime;
    /**
     * String to display level time of player.
     */
    private String levelTime;

    /**
     * Boolean to show if pause menu is activated.
     */
    private boolean showPauseMenu;

    /**
     * Integer to record the time clock resumes.
     */
    public int startTime = (int) java.time.Instant.now().getEpochSecond();

    /**
     * Font for menu.
     */
    private Font menuFont;

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
     * Integer to record length of string "Pause Menu".
     */
    private int strLen;

    /**
     * Used to set debug console visible when Shift+Alt+F1 is pressed.
     */
    private DebugConsole debugConsole;

    /**
     * Double array of integer to record player scores and times.
     */
    private int[][] scoreLevel = new int[6][2];


    private Shape powerUp = new Ellipse2D.Double(0,0,20,20);

    private Random rnd = new Random();

    private boolean collected = false;
    private boolean spawned = false;

    private int x;
    private int y;
    private int godModeStartTime;
    private String godMode;

    /**
     * This constructor is used to initialize the game by adding listeners, loading the bricks for all levels and the
     * debug console and to start the timer to record all player inputs, scores and times.
     *
     * @param owner This parameter is used to add listeners to the JFrame and center the debug console.
     * @param choice This parameter is used to send the player choices from the custom console to public class Wall
     *               for brick generation and to Scoreboard to load the categories for the ScoreBoard.
     */
    public GameBoard(JFrame owner, int[][] choice) {
        super();

        strLen = 0; //initial string length is 0
        showPauseMenu = false; //initially do not show pause menu

        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE); //menu font

        this.initialize(owner);
        message = "";
        totalScore = "";
        levelScore = "";
        totalTime = "";
        levelTime = "";
        godMode = "";
        //define all bricks, player and ball
        wall = new Wall(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430),choice);

        debugConsole = new DebugConsole(owner,wall,this);
        //initialize the first level
        wall.nextLevel(); //move to next level

        gameTimer = new Timer(10,e ->{ //10-millisecond delay between action and action listener
            wall.move(); //move player and ball every 10 millisecond
            wall.findImpacts(collected); //detect impact of ball
            message = String.format("Bricks: %d  Balls %d",wall.getBrickCount(),wall.getBallCount()); //show brick and ball count

            if(wall.flag==1){
                scoreLevel[0][1] = returnPreviousLevelsTime(wall.getLevel());
                wall.flag = 0;
            }

            scoreLevel[0][0] = returnPreviousLevelsScore(wall.getLevel());
            for(Brick b: wall.bricks){
                if(b.isBroken()){
                    scoreLevel[0][0] += b.getScore();
                }
            }

            totalScore = String.format("Total Score %d",scoreLevel[0][0]);
            levelScore = String.format("Level %d Score %d",wall.getLevel(),scoreLevel[0][0]-returnPreviousLevelsScore(wall.getLevel()));


            int systemClock = (int) java.time.Instant.now().getEpochSecond() - startTime + scoreLevel[0][1];
            int totalMinutes = systemClock/60;
            int totalSeconds = systemClock%60;
            int levelMinutes = (systemClock - returnPreviousLevelsTime(wall.getLevel()))/60;
            int levelSeconds = (systemClock - returnPreviousLevelsTime(wall.getLevel()))%60;

            totalTime = String.format("Total Time %02d:%02d",totalMinutes,totalSeconds);
            levelTime = String.format("Level %d Time %02d:%02d",wall.getLevel(),levelMinutes,levelSeconds);

            if(levelSeconds==0 && !spawned){
                x = rnd.nextInt(401) + 100;
                y = 200;
                collected = false;
                spawned = true;
            }

            if(isCollected(wall.ball)){
                collected = true;
                spawned = false;
                godModeStartTime = systemClock;
            }

            if(collected){
                if(systemClock - godModeStartTime >= 10){
                    collected = false;
                }
                godMode = String.format("God Mode Activated %d",10 - (systemClock - godModeStartTime));
            }
            else {
                godMode = "God Mode Deactivated";
            }

            if(wall.isBallLost()){ //if ball leaves bottom border

                gameTimer.stop(); //stop timer and action listener
                saveElapsedTime();

                if(wall.ballEnd()){ //if all balls are used up
                    wall.wallReset(wall.getLevel()); //reset wall
                    message = "Game over"; //show game over message
                    wall.ballReset(); //reset player and ball position
                    scoreLevel[0][1] = returnPreviousLevelsTime(wall.getLevel());
                }
                else {
                    wall.ballReset(); //reset player and ball position
                }
            }
            else if(wall.isDone()){ //if all bricks destroyed

                storeLevelScore(wall.getLevel());
                gameTimer.stop(); //stop timer and action listener
                saveElapsedTime();
                storeLevelTime(wall.getLevel());

                try {
                    new ScoreBoard(owner,this,wall.getLevel(),scoreLevel,choice);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                if(wall.hasLevel()){ //if next level exists
                    message = "Go to Next Level";
                    wall.ballReset(); //reset player and ball position
                    wall.wallReset(wall.getLevel()); //reset wall
                    wall.nextLevel(); //move to next level
                }
                else{ //if no levels left
                    try {
                        new ScoreBoard(owner,this,0,scoreLevel,choice);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    message = "ALL WALLS DESTROYED";
                }
            }
            repaint(); //repaint components
        });
    }

    /**
     * This method is used to add listeners to the JFrame to receive player inputs for the game.
     * @param owner This parameter is used to set the mouse cursor to hand cursor when pause menu is opened and
     *              mouse cursor is inside a button.
     */
    private void initialize(JFrame owner){ //initialize JFrame
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT)); //set frame size
        this.setFocusable(true); //set focusable
        this.requestFocusInWindow(); //request focus

        this.addKeyListener(new KeyAdapter() {
            /**
             * This key listener is called when a key is pressed and the relevant event is processed in the game
             * if the key binding exists.
             * @param keyEvent This parameter is used to track the key presses.
             */
            @Override
            public void keyPressed(KeyEvent keyEvent) {

                switch(keyEvent.getKeyCode()){
                    case KeyEvent.VK_A: //press A
                        wall.player.moveLeft(); //player moves left
                        break;
                    case KeyEvent.VK_D: //press D
                        wall.player.movRight(); //player moves right
                        break;
                    case KeyEvent.VK_ESCAPE: //press esc
                        showPauseMenu = !showPauseMenu; //show pause menu
                        repaint(); //repaint components
                        if(gameTimer.isRunning()) {
                            gameTimer.stop(); //stop timer and action listener
                            saveElapsedTime();
                        }
                        break;
                    case KeyEvent.VK_SPACE: //press space
                        if(!showPauseMenu) { //if game not paused
                            if (gameTimer.isRunning()) { //if timer is running
                                gameTimer.stop(); //stop timer and action listener
                                saveElapsedTime();
                            }
                            else { //else if timer is stopped
                                gameTimer.start(); //start timer and action listener
                                startTime = (int) java.time.Instant.now().getEpochSecond();
                            }
                        }
                        break;
                    case KeyEvent.VK_F1: //press f1 + alt + shift
                        if(keyEvent.isAltDown() && keyEvent.isShiftDown()) {
                            if(gameTimer.isRunning()) {
                                gameTimer.stop();
                                saveElapsedTime();
                            }
                            debugConsole.setVisible(true); //show debug console
                        }
                    default: //press anything else
                        wall.player.stop(); //stop player
                }
            }

            /**
             * This key listener is called when a key is released and the player is stopped.
             * @param keyEvent This parameter is used to track the key releases.
             */
            @Override
            public void keyReleased(KeyEvent keyEvent) { //if key released, stop player
                wall.player.stop();
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
                if(!showPauseMenu) //if game not paused
                    return; //return
                if(continueButtonRect.contains(p)){ //if continue pressed
                    showPauseMenu = false; //close pause menu
                    repaint(); //repaint components
                }
                else if(restartButtonRect.contains(p)){ //if restart pressed
                    message = "Restarting Game...";
                    wall.ballReset(); //reset balls
                    wall.wallReset(wall.getLevel()); //reset walls
                    scoreLevel[0][1] = returnPreviousLevelsScore(wall.getLevel());
                    showPauseMenu = false; //close pause menu
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
                if(exitButtonRect != null && showPauseMenu) { //if pause menu shown and exit button is drawn
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
     * This method is used to draw the graphics for the entire game screen. The game screen is first cleared with a
     * white background. Then, a gray background is loaded. The game condition, score and time strings are drawn in
     * blue and then the ball, bricks and player are drawn. Finally, if the pause menu is loaded,the pause menu is
     * drawn with its buttons.
     *
     * @param g This parameter is used to control the graphics such as colour, composite and font.
     */
    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(Color.LIGHT_GRAY); //set blue colour
        g2d.fillRect(0,0,this.getWidth(),this.getHeight());

        g2d.setColor(Color.BLUE); //set blue colour
        g2d.drawString(message,250,225); //set message colour as blue
        g2d.drawString(totalScore,250,240); //set message colour as blue
        g2d.drawString(levelScore,250,255); //set message colour as blue
        g2d.drawString(totalTime,250,270); //set message colour as blue
        g2d.drawString(levelTime,250,285); //set message colour as blue
        g2d.drawString(godMode,250,300); //set message colour as blue

        drawBall(wall.ball,g2d); //draw ball with inner and border colours

        for(Brick b : wall.bricks) //loop through bricks
            if (!b.isBroken()) //if brick is not broken
                drawBrick(b,g2d); //colour brick

        drawPlayer(wall.player,g2d); //colour player

        if(showPauseMenu) //if pause menu shown
            drawMenu(g2d); //colour pause menu

        if(spawned&&!collected){
            drawPowerUp(g2d);
        }

        Toolkit.getDefaultToolkit().sync(); //sync toolkit graphics
    }

    /**
     * This method is used to clear the background by filling it with white colour.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void clear(Graphics2D g2d){ //clear colour

        g2d.setColor(BG_COLOR); //get white colour
        g2d.fillRect(0,0,getWidth(),getHeight()); //fill frame with white colour
    }

    /**
     * This method is used to draw the bricks by filling inner colour and drawing outer colour.
     * @param brick This parameter tells the method what colour to use for the bricks.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawBrick(Brick brick,Graphics2D g2d){

        g2d.setColor(brick.getInnerColor()); //get brick inner colour
        g2d.fill(brick.getBrick()); //fill brick with inner colour

        g2d.setColor(brick.getBorderColor()); //get brick border colour
        g2d.draw(brick.getBrick()); //draw brick border with colour
    }

    /**
     * This method is used to draw the ball by filling inner colour and drawing outer colour.
     * @param ball This parameter tells the method what colour to use for the ball.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawBall(Ball ball,Graphics2D g2d){

        Shape s = ball.getBallFace(); //get ball face

        g2d.setColor(ball.getInnerColor()); //set colour as inner colour
        g2d.fill(s); //fill inner ball with colour

        g2d.setColor(ball.getBorderColor()); //set colour as border colour
        g2d.draw(s); //draw ball border with colour
    }

    /**
     * This method is used to draw the player by filling inner colour and drawing outer colour.
     * @param p This parameter tells the method what colour to use for the player.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawPlayer(Player p,Graphics2D g2d){

        Shape s = p.getPlayerFace(); //get player shape

        g2d.setColor(Player.INNER_COLOR); //get player inner colour
        g2d.fill(s); //fill player with inner colour

        g2d.setColor(Player.BORDER_COLOR); //get player border colour
        g2d.draw(s); //draw player border with colour
    }

    /**
     * This method is used to draw the pause menu.
     * @param g2d This parameter is used to control the graphics such as colour, composite and font.
     */
    private void drawMenu(Graphics2D g2d){ //colour pause menu

        obscureGameBoard(g2d); //fill pause menu with black colour
        drawPauseMenu(g2d); //draw pause menu
    }

    /**
     * This method is used to add black composite to the pause menu.
     * @param g2d This parameter is used to control the graphics such as colour, composite and font.
     */
    private void obscureGameBoard(Graphics2D g2d){ //fill pause menu with black colour

        Composite tmp = g2d.getComposite(); //hold current composite

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f); //get alpha composite
        g2d.setComposite(ac); //set alpha composite

        g2d.setColor(Color.BLACK); //get black colour
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT); //fill frame with black colour

        g2d.setComposite(tmp); //reset composite
    }

    /**
     * This method is used to draw the strings and buttons of the pause menu.
     * @param g2d This parameter is used to control the graphics such as colour and font.
     */
    private void drawPauseMenu(Graphics2D g2d){

        Font tmpFont = g2d.getFont(); //hold current font

        g2d.setFont(menuFont); //set menu font
        g2d.setColor(MENU_COLOR); //set menu colour

        if(strLen == 0){ //if string length is 0
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width; //get width of bound string
        }

        int x = (this.getWidth() - strLen) / 2; //get position of pause menu
        int y = this.getHeight() / 10;

        g2d.drawString(PAUSE,x,y); //draw pause menu

        x = this.getWidth() / 8; //get position of continue button
        y = this.getHeight() / 4;


        if(continueButtonRect == null){ //if continue button not drawn
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds(); //get rectangle shape button for continue
            continueButtonRect.setLocation(x,y-continueButtonRect.height); //set location of continue button
        }

        g2d.drawString(CONTINUE,x,y); //draw continue button

        y *= 2; //get position of restart button

        if(restartButtonRect == null){ //if restart button not drawn
            restartButtonRect = (Rectangle) continueButtonRect.clone(); //clone rectangle of continue as own shape
            restartButtonRect.setLocation(x,y-restartButtonRect.height); //set location of restart button
        }

        g2d.drawString(RESTART,x,y); //draw restart button

        y *= 3.0/2; //get position of exit button

        if(exitButtonRect == null){ //if exit button not drawn
            exitButtonRect = (Rectangle) continueButtonRect.clone(); //clone rectangle of continue as own
            exitButtonRect.setLocation(x,y-exitButtonRect.height); //set location of exit button
        }

        g2d.drawString(EXIT,x,y); //draw exit button

        g2d.setFont(tmpFont); //reset font
    }

    private void drawPowerUp(Graphics2D g2d){

        RectangularShape tmp = (RectangularShape) powerUp;

        int width = powerUp.getBounds().width;
        int height = powerUp.getBounds().height;

        tmp.setFrame(x-width/2,y-height/2,width,height);
        powerUp = tmp;

        g2d.setColor(Color.RED);
        g2d.fill(powerUp);

        g2d.setColor(Color.BLACK);
        g2d.draw(powerUp);
    }

    private boolean isCollected(Ball b){
        return (powerUp.contains(b.getPosition())||powerUp.contains(b.up)||powerUp.contains(b.down)||powerUp.contains(b.left)||powerUp.contains(b.right));
    }

    /**
     * This method is used to signal to the game that the focus is lost and to show the message and save elapsed
     * time for the game clock.
     */
    public void onLostFocus(){ //if game not in focus
        if(gameTimer.isRunning()) {
            gameTimer.stop(); //stop timer and action listener
            saveElapsedTime();
        }
        message = "Focus Lost";
        repaint(); //repaint components
    }

    /**
     * This method is used to store current level score.
     * @param level This integer tells the method which level score to save.
     */
    public void storeLevelScore(int level){
        scoreLevel[level][0] = scoreLevel[0][0]- returnPreviousLevelsScore(level);
    }

    /**
     * This method is used to store current level time.
     * @param level This integer tells the method which level time to save.
     */
    public void storeLevelTime(int level){
        scoreLevel[level][1] = scoreLevel[0][1]- returnPreviousLevelsTime(level);
    }

    /**
     * This method returns the combined scores for all previous levels.
     * @param level This integer tells the method which level the game is currently on.
     * @return This method returns the combined scores for all previous levels.
     */
    public int returnPreviousLevelsScore(int level){
        int total = 0;
        for(int i = level;i > 1; i--){
            total += scoreLevel[i-1][0];
        }
        return total;
    }

    /**
     * This method returns the combined time for all previous levels.
     * @param level This integer tells the method which level the game is currently on.
     * @return This method returns the combined time for all previous levels.
     */
    public int returnPreviousLevelsTime(int level){
        int total = 0;
        for(int i = level;i > 1; i--){
            total += scoreLevel[i-1][1];
        }
        return total;
    }

    /**
     * This method saves the time elapsed between the game resuming and the game stopping.
     */
    public void saveElapsedTime(){
        scoreLevel[0][1] += (int) java.time.Instant.now().getEpochSecond() - startTime;
    }
}
