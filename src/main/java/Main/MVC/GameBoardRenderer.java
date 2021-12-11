package Main.MVC;

import Main.Models.Ball;
import Main.Models.Brick;
import Main.Models.GodModePowerUp;
import Main.Models.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Public class GameBoardRenderer is the View of the MVC design pattern and is responsible for rendering all the
 * graphics in the game. The renderer renders everything from the bricks, ball and player to the game messages and the
 * pause menu.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class GameBoardRenderer implements Renderer {

    /**
     * White colour to clear the background.
     */
    private static final Color BG_COLOR = Color.WHITE;
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
     * Menu words colour of green.
     */
    private static final Color MENU_COLOR = new Color(0,255,0);
    /**
     * Font for pause menu words.
     */
    private final Font menuFont;
    /**
     * Integer to record length of string "Pause Menu" to center the pause menu title.
     */
    private int strLen = 0;
    /**
     * GameBoard to get all information needed to draw the ball, bricks, player and game messages.
     */
    private final GameBoard gameBoard;
    /**
     * Dimensions of the game screen to draw the pause menu options.
     */
    private final Dimension area;
    /**
     * Image for Game background.
     */
    private final Image newImage;

    /**
     * This constructor loads in the GameBoard so that renderer can get all information for rendering and the screen
     * dimensions to draw the pause menu.
     * @param gameBoard GameBoard to get all information needed to draw the ball, bricks, player and game messages.
     * @param area Dimensions of the game screen to draw the pause menu options.
     * @throws IOException This constructor throws IOException if game background image is not found.
     */
    public GameBoardRenderer(GameBoard gameBoard,Dimension area) throws IOException {
        this.gameBoard = gameBoard;
        this.area = area;
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE); //menu font
        BufferedImage myPicture = ImageIO.read(new File("image/BrickBreakerGameBackground.png"));
        newImage = myPicture.getScaledInstance((int)area.getWidth(),(int)area.getHeight(), Image.SCALE_DEFAULT);
    }

    /**
     * This method is used to render the graphics for the entire game screen. The game screen is first cleared with a
     * white background. Then, a gray background is loaded. The game condition, score and time strings are drawn in
     * blue and then the ball, bricks, player and power up are drawn. Finally, if the pause menu is loaded,the pause
     * menu is drawn with its buttons.
     * @param g This parameter is used to control the graphics such as colour, composite and font.
     */
    @Override
    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        clear(g2d);

        g2d.drawImage(newImage,0,0,null);

        drawMessages(g2d);

        for(Brick b : gameBoard.getBricks()[gameBoard.getLevel()-1]) { //loop through bricks
            if(!b.isBroken())
                drawBrick(b, g2d); //colour brick
        }

        if(!gameBoard.getPowerUp().isCollected() && gameBoard.getPowerUp().isSpawned()){
            drawPowerUp(gameBoard.getPowerUp(),g2d);
        }

        drawBall(gameBoard.getBall(),g2d);
        drawPlayer(gameBoard.getPlayer(),g2d);

        if(gameBoard.isShowPauseMenu()) //if pause menu shown
            drawMenu(g2d); //colour pause menu
    }

    /**
     * This method is used to clear the background by filling it with white colour.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void clear(Graphics2D g2d){ //clear colour

        g2d.setColor(BG_COLOR); //get white colour
        g2d.fillRect(0,0, area.width, area.height); //fill frame with white colour
    }

    /**
     * This method is used to draw the bricks by filling inner colour and drawing outer colour.
     * @param brick This parameter tells the method what colour to use for the bricks.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawBrick(Brick brick,Graphics2D g2d){

        g2d.setColor(brick.getInner()); //get brick inner colour
        g2d.fill(brick.getBrickFace()); //fill brick with inner colour

        g2d.setColor(brick.getBorder()); //get brick border colour
        g2d.draw(brick.getBrickFace()); //draw brick border with colour
    }

    /**
     * This method is used to draw the ball by filling inner colour and drawing outer colour.
     * @param ball This parameter tells the method what colour to use for the ball.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawBall(Ball ball, Graphics2D g2d){

        Shape s = ball.getBallFace(); //get ball face

        g2d.setColor(ball.getInner()); //set colour as inner colour
        g2d.fill(s); //fill inner ball with colour

        g2d.setColor(ball.getBorder()); //set colour as border colour
        g2d.draw(s); //draw ball border with colour
    }

    /**
     * This method is used to draw the player by filling inner colour and drawing outer colour.
     * @param p This parameter tells the method what colour to use for the player.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawPlayer(Player p, Graphics2D g2d){

        Shape s = p.getPlayerFace(); //get player shape

        g2d.setColor(p.getInner()); //get player inner colour
        g2d.fill(s); //fill player with inner colour

        g2d.setColor(p.getBorder()); //get player border colour
        g2d.draw(s); //draw player border with colour
    }

    /**
     * This method is used to draw the game messages to show game condition such as number of bricks and balls left
     * and game focus.
     * @param g2d This parameter is used to control the colour and font of the game messages.
     */
    private void drawMessages(Graphics2D g2d){

        Font tmpFont = g2d.getFont(); //hold current font

        g2d.setFont(new Font("Monospaced",Font.BOLD,16)); //set menu font
        g2d.setColor(Color.BLACK); //set blue colour

        if(gameBoard.getChoice()[gameBoard.getLevel()-1][9]==0 && gameBoard.getGameMessages(0)!=null) {
            g2d.drawString(gameBoard.getGameMessages(0), 210, 225); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(1), 210, 240); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(2), 210, 255); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(3), 210, 270); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(4), 210, 285); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(5), 210, 300); //set message colour as blue
        }
        else if(gameBoard.getChoice()[gameBoard.getLevel()-1][9]==1 && gameBoard.getGameMessages(0)!=null){
            g2d.drawString(gameBoard.getGameMessages(0), 210, 150); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(1), 210, 165); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(2), 210, 180); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(3), 210, 195); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(4), 210, 210); //set message colour as blue
            g2d.drawString(gameBoard.getGameMessages(5), 210, 225); //set message colour as blue
        }
        g2d.setFont(tmpFont);
    }

    /**
     * This method is used to draw a red circle power up which triggers God mode for the player for 10 seconds
     * when collected.
     * @param powerUp This parameter tells the method what colour to use for the power up.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawPowerUp(GodModePowerUp powerUp, Graphics2D g2d){

        Shape s = powerUp.getPowerUp();

        g2d.setColor(powerUp.getInner());
        g2d.fill(s);

        g2d.setColor(powerUp.getBorder());
        g2d.draw(s);
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
        g2d.fillRect(0,0,area.width,area.height); //fill frame with black colour

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

        int x = (area.width - strLen) / 2; //get position of pause menu
        int y = area.height / 10;

        g2d.drawString(PAUSE,x,y); //draw pause menu

        x = area.width / 8; //get position of continue button
        y = area.height / 4;

        g2d.drawString(CONTINUE,x,y); //draw continue button

        y *= 2; //get position of restart button

        g2d.drawString(RESTART,x,y); //draw restart button

        y *= 3.0/2; //get position of exit button

        g2d.drawString(EXIT,x,y); //draw exit button

        g2d.setFont(tmpFont); //reset font
    }
}
