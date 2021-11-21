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



public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";
    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(0,255,0);


    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static final Color BG_COLOR = Color.WHITE;

    private Timer gameTimer;

    private Wall wall;

    private String message;

    private boolean showPauseMenu;

    private Font menuFont;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int strLen;

    private DebugConsole debugConsole;


    public GameBoard(JFrame owner){
        super();

        strLen = 0; //initial string length is 0
        showPauseMenu = false; //initially do not show pause menu



        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE); //menu font


        this.initialize();
        message = "";
        //define all bricks, player and ball
        wall = new Wall(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));

        debugConsole = new DebugConsole(owner,wall,this);
        //initialize the first level
        wall.nextLevel(); //move to next level

        gameTimer = new Timer(10,e ->{ //10 millisecond delay between action and action listener
            wall.move(); //move player and ball every 10 millisecond
            wall.findImpacts(); //detect impact of ball
            message = String.format("Bricks: %d Balls %d",wall.getBrickCount(),wall.getBallCount()); //show brick and ball count
            if(wall.isBallLost()){ //if ball leaves bottom border
                if(wall.ballEnd()){ //if all balls are used up
                    wall.wallReset(); //reset wall
                    message = "Game over"; //show game over message
                }
                wall.ballReset(); //reset player and ball position
                gameTimer.stop(); //stop timer and action listener
            }
            else if(wall.isDone()){ //if all bricks destroyed
                if(wall.hasLevel()){ //if next level exists
                    message = "Go to Next Level";
                    gameTimer.stop(); //stop timer and action listener
                    wall.ballReset(); //reset player and ball position
                    wall.wallReset(); //reset wall
                    wall.nextLevel(); //move to next level
                }
                else{ //if no levels left
                    message = "ALL WALLS DESTROYED";
                    gameTimer.stop(); //stop timer and action listener
                }
            }

            repaint(); //repaint components
        });

    }



    private void initialize(){ //initialize JFrame
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT)); //set frame size
        this.setFocusable(true); //set focusable
        this.requestFocusInWindow(); //request focus
        this.addKeyListener(this); //add listeners
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(Color.BLUE); //set blue colour
        g2d.drawString(message,250,225); //set message colour as blue

        drawBall(wall.ball,g2d); //draw ball with inner and border colours

        for(Brick b : wall.bricks) //loop through bricks
            if(!b.isBroken()) //if brick is not broken
                drawBrick(b,g2d); //colour brick

        drawPlayer(wall.player,g2d); //colour player

        if(showPauseMenu) //if pause menu shown
            drawMenu(g2d); //colour pause menu

        Toolkit.getDefaultToolkit().sync(); //sync toolkit graphics
    }

    private void clear(Graphics2D g2d){ //clear colour
        Color tmp = g2d.getColor(); //hold blue colour
        g2d.setColor(BG_COLOR); //get white colour
        g2d.fillRect(0,0,getWidth(),getHeight()); //fill frame with white colour
        g2d.setColor(tmp); //reset blue colour
    }

    private void drawBrick(Brick brick,Graphics2D g2d){
        Color tmp = g2d.getColor(); //hold blue colour

        g2d.setColor(brick.getInnerColor()); //get brick inner colour
        g2d.fill(brick.getBrick()); //fill brick with inner colour

        g2d.setColor(brick.getBorderColor()); //get brick border colour
        g2d.draw(brick.getBrick()); //draw brick border with colour


        g2d.setColor(tmp); //reset blue colour
    }

    private void drawBall(Ball ball,Graphics2D g2d){
        Color tmp = g2d.getColor(); //hold blue colour

        Shape s = ball.getBallFace(); //get ball face

        g2d.setColor(ball.getInnerColor()); //set colour as inner colour
        g2d.fill(s); //fill inner ball with colour

        g2d.setColor(ball.getBorderColor()); //set colour as border colour
        g2d.draw(s); //draw ball border with colour

        g2d.setColor(tmp); //reset blue colour
    }

    private void drawPlayer(Player p,Graphics2D g2d){
        Color tmp = g2d.getColor(); //hold blue colour

        Shape s = p.getPlayerFace(); //get player shape
        g2d.setColor(Player.INNER_COLOR); //get player inner colour
        g2d.fill(s); //fill player with inner colour

        g2d.setColor(Player.BORDER_COLOR); //get player border colour
        g2d.draw(s); //draw player border with colour

        g2d.setColor(tmp); //reset blue colour
    }

    private void drawMenu(Graphics2D g2d){ //colour pause menu
        obscureGameBoard(g2d); //fill pause menu with black colour
        drawPauseMenu(g2d); //draw pause menu
    }

    private void obscureGameBoard(Graphics2D g2d){ //fill pause menu with black colour

        Composite tmp = g2d.getComposite(); //hold current composite
        Color tmpColor = g2d.getColor(); //hold blue colour

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f); //get alpha composite
        g2d.setComposite(ac); //set alpha composite

        g2d.setColor(Color.BLACK); //get black colour
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT); //fill frame with black colour

        g2d.setComposite(tmp); //reset composite
        g2d.setColor(tmpColor); //reset blue colour
    }

    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont(); //hold current font
        Color tmpColor = g2d.getColor(); //hold blue colour


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
        g2d.setColor(tmpColor); //reset blue colour
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { //nothing
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) { //check keyboard input
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
                gameTimer.stop(); //stop timer and action listener
                break;
            case KeyEvent.VK_SPACE: //press space
                if(!showPauseMenu) //if game not paused
                    if(gameTimer.isRunning()) //if timer is running
                        gameTimer.stop(); //stop timer and action listener
                    else //else if timer is stopped
                        gameTimer.start(); //start timer and action listener
                break;
            case KeyEvent.VK_F1: //press f1 + alt + shift
                if(keyEvent.isAltDown() && keyEvent.isShiftDown())
                    debugConsole.setVisible(true); //show debug console
            default: //press anything else
                wall.player.stop(); //stop player
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { //if key released, stop player
        wall.player.stop();
    }

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
            wall.wallReset(); //reset walls
            showPauseMenu = false; //close pause menu
            repaint(); //repaint components
        }
        else if(exitButtonRect.contains(p)){ //if exit pressed
            System.exit(0); //close game
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) { //nothing

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) { //nothing

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) { //nothing

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) { //nothing

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) { //nothing

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) { //if mouse moved
        Point p = mouseEvent.getPoint(); //get mouse position
        if(exitButtonRect != null && showPauseMenu) { //if pause menu shown and exit button is drawn
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //if mouse on button, show hand cursor
            else //else
                this.setCursor(Cursor.getDefaultCursor()); //use default mouse cursor
        }
        else{ //if pause menu not shown
            this.setCursor(Cursor.getDefaultCursor()); //use default mouse cursor
        }
    }

    public void onLostFocus(){ //if game not in focus
        gameTimer.stop(); //stop timer and action listener
        message = "Focus Lost";
        repaint(); //repaint components
    }

}
