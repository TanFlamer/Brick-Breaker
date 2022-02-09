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
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);
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

        for(Brick[] brick : gameBoard.getBricks()[gameBoard.getLevel()-1]) {
            for(Brick b: brick)
                if(!b.isBroken())
                    drawBrick(b, g2d);
        }

        if(!gameBoard.getPowerUp().isCollected() && gameBoard.getPowerUp().isSpawned()){
            drawPowerUp(gameBoard.getPowerUp(),g2d);
        }

        drawBall(gameBoard.getBalls(),g2d);
        drawPlayer(gameBoard.getPlayer(),g2d);

        if(gameBoard.isShowPauseMenu())
            drawMenu(g2d);
    }

    /**
     * This method is used to clear the background by filling it with white colour.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void clear(Graphics2D g2d){

        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0, area.width, area.height);
    }

    /**
     * This method is used to draw the bricks by filling inner colour and drawing outer colour.
     * @param brick This parameter tells the method what colour to use for the bricks.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawBrick(Brick brick,Graphics2D g2d){

        g2d.setColor(brick.getInner());
        g2d.fill(brick.getBrickFace());

        g2d.setColor(brick.getBorder());
        g2d.draw(brick.getBrickFace());
    }

    /**
     * This method is used to draw the ball by filling inner colour and drawing outer colour.
     * @param balls This parameter holds all the available balls.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawBall(Ball[] balls, Graphics2D g2d){

        for(Ball ball: balls) {
            if(!ball.isLost()) {
                Shape s = ball.getBallFace();

                if (!ball.isCollected())
                    g2d.setColor(ball.getInner());
                else
                    g2d.setColor(ball.getPowerUp());
                g2d.fill(s);

                g2d.setColor(ball.getBorder());
                g2d.draw(s);
            }
        }
    }

    /**
     * This method is used to draw the player by filling inner colour and drawing outer colour.
     * @param p This parameter tells the method what colour to use for the player.
     * @param g2d This parameter is used to control the graphics such as colour.
     */
    private void drawPlayer(Player p, Graphics2D g2d){

        Shape s = p.getPlayerFace();

        g2d.setColor(p.getInner());
        g2d.fill(s);

        g2d.setColor(p.getBorder());
        g2d.draw(s);
    }

    /**
     * This method is used to draw the game messages to show game condition such as number of bricks and balls left
     * and game focus.
     * @param g2d This parameter is used to control the colour and font of the game messages.
     */
    private void drawMessages(Graphics2D g2d){

        Font tmpFont = g2d.getFont();

        g2d.setFont(new Font("Monospaced",Font.BOLD,16));
        g2d.setColor(Color.BLACK);

        if(gameBoard.getChoice()[gameBoard.getLevel()-1][9]==0 && gameBoard.getGameMessages(0)!=null) {
            g2d.drawString(gameBoard.getGameMessages(0), 210, 225);
            g2d.drawString(gameBoard.getGameMessages(1), 210, 240);
            g2d.drawString(gameBoard.getGameMessages(2), 210, 255);
            g2d.drawString(gameBoard.getGameMessages(3), 210, 270);
            g2d.drawString(gameBoard.getGameMessages(4), 210, 285);
            g2d.drawString(gameBoard.getGameMessages(5), 210, 300);
        }
        else if(gameBoard.getChoice()[gameBoard.getLevel()-1][9]==1 && gameBoard.getGameMessages(0)!=null){
            g2d.drawString(gameBoard.getGameMessages(0), 210, 150);
            g2d.drawString(gameBoard.getGameMessages(1), 210, 165);
            g2d.drawString(gameBoard.getGameMessages(2), 210, 180);
            g2d.drawString(gameBoard.getGameMessages(3), 210, 195);
            g2d.drawString(gameBoard.getGameMessages(4), 210, 210);
            g2d.drawString(gameBoard.getGameMessages(5), 210, 225);
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
    private void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    /**
     * This method is used to add black composite to the pause menu.
     * @param g2d This parameter is used to control the graphics such as colour, composite and font.
     */
    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,area.width,area.height);

        g2d.setComposite(tmp);
    }

    /**
     * This method is used to draw the strings and buttons of the pause menu.
     * @param g2d This parameter is used to control the graphics such as colour and font.
     */
    private void drawPauseMenu(Graphics2D g2d){

        Font tmpFont = g2d.getFont();

        g2d.setFont(menuFont);
        g2d.setColor(MENU_COLOR);

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width;
        }

        int x = (area.width - strLen) / 2;
        int y = area.height / 10;

        g2d.drawString(PAUSE,x,y);

        x = area.width / 8;
        y = area.height / 4;

        g2d.drawString(CONTINUE,x,y);

        y *= 2;

        g2d.drawString(RESTART,x,y);

        y *= 3.0/2;

        g2d.drawString(EXIT,x,y);

        g2d.setFont(tmpFont);
    }
}
