import java.awt.*;
import java.awt.font.FontRenderContext;

public class GameBoardRenderer implements Renderer {

    private static final Color BG_COLOR = Color.WHITE;

    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";

    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(0,255,0);

    private Font menuFont;
    private int strLen = 0;

    private GameBoard gameBoard;

    private String message = "";
    private String totalScore = "";
    private String levelScore = "";
    private String totalTime = "";
    private String levelTime = "";
    private String godMode = "";

    public GameBoardRenderer(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE); //menu font
    }

    @Override
    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        clear(g2d);

        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 600, 450);

        drawMessages(g2d);

        for(Brick b : gameBoard.getBrick()) { //loop through bricks
            if(!b.isBroken())
                drawBrick(b, g2d); //colour brick
        }

        if(!gameBoard.getPowerUp().isCollected()&&gameBoard.getPowerUp().isSpawned()){
            drawPowerUp(gameBoard.getPowerUp(),g2d);
        }

        drawBall(gameBoard.getBall(),g2d);
        drawPlayer(gameBoard.getPlayer(),g2d);

        if(gameBoard.isShowPauseMenu()) //if pause menu shown
            drawMenu(g2d); //colour pause menu
    }

    private void clear(Graphics2D g2d){ //clear colour

        g2d.setColor(BG_COLOR); //get white colour
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT); //fill frame with white colour
    }

    private void drawBrick(Brick brick,Graphics2D g2d){

        g2d.setColor(brick.getInner()); //get brick inner colour
        g2d.fill(brick.getBrickFace()); //fill brick with inner colour

        g2d.setColor(brick.getBorder()); //get brick border colour
        g2d.draw(brick.getBrickFace()); //draw brick border with colour
    }

    private void drawBall(Ball ball,Graphics2D g2d){

        Shape s = ball.getBallFace(); //get ball face

        g2d.setColor(ball.getInner()); //set colour as inner colour
        g2d.fill(s); //fill inner ball with colour

        g2d.setColor(ball.getBorder()); //set colour as border colour
        g2d.draw(s); //draw ball border with colour
    }

    private void drawPlayer(Player p,Graphics2D g2d){

        Shape s = p.getPlayerFace(); //get player shape

        g2d.setColor(p.getInner()); //get player inner colour
        g2d.fill(s); //fill player with inner colour

        g2d.setColor(p.getBorder()); //get player border colour
        g2d.draw(s); //draw player border with colour
    }

    private void drawMessages(Graphics2D g2d){

        if(gameBoard.getMessageFlag()==0)
            message = String.format("Bricks: %d  Balls %d",gameBoard.getBrickCount(),gameBoard.getBallCount());
        else if(gameBoard.getMessageFlag()==1)
            message = "Game over"; //show game over message
        else if(gameBoard.getMessageFlag()==2)
            message = "Go to Next Level";
        else if(gameBoard.getMessageFlag()==3)
            message = "ALL WALLS DESTROYED";
        else if(gameBoard.getMessageFlag()==4)
            message = "Restarting Game...";
        else if(gameBoard.getMessageFlag()==5)
            message = "Focus Lost";

        totalScore = String.format("Total Score %d",gameBoard.getScoreAndTime()[0][0]);
        levelScore = String.format("Level %d Score %d",gameBoard.getLevel(),gameBoard.getScoreAndTime()[gameBoard.getLevel()][0]);

        int systemClock = gameBoard.getScoreAndTime()[0][1];
        int totalMinutes = systemClock/60;
        int totalSeconds = systemClock%60;

        int levelClock = gameBoard.getScoreAndTime()[gameBoard.getLevel()][1];
        int levelMinutes = levelClock/60;
        int levelSeconds = levelClock%60;

        totalTime = String.format("Total Time %02d:%02d",totalMinutes,totalSeconds);
        levelTime = String.format("Level %d Time %02d:%02d",gameBoard.getLevel(),levelMinutes,levelSeconds);

        if(gameBoard.getPowerUp().isCollected())
            godMode = String.format("God Mode Activated %d", gameBoard.getGodModeTimeLeft());
        else
            godMode = String.format("God Mode Orbs Left %d", (gameBoard.getScoreAndTime()[gameBoard.getLevel()][1]/60 + 1) - gameBoard.getPowerUpSpawns());

        g2d.setColor(Color.BLUE); //set blue colour

        if(gameBoard.getChoice()[gameBoard.getLevel()-1][9]==0) {
            g2d.drawString(message, 250, 225); //set message colour as blue
            g2d.drawString(totalScore, 250, 240); //set message colour as blue
            g2d.drawString(levelScore, 250, 255); //set message colour as blue
            g2d.drawString(totalTime, 250, 270); //set message colour as blue
            g2d.drawString(levelTime, 250, 285); //set message colour as blue
            g2d.drawString(godMode, 250, 300); //set message colour as blue
        }
        else if(gameBoard.getChoice()[gameBoard.getLevel()-1][9]==1){
            g2d.drawString(message, 250, 150); //set message colour as blue
            g2d.drawString(totalScore, 250, 165); //set message colour as blue
            g2d.drawString(levelScore, 250, 180); //set message colour as blue
            g2d.drawString(totalTime, 250, 195); //set message colour as blue
            g2d.drawString(levelTime, 250, 210); //set message colour as blue
            g2d.drawString(godMode, 250, 225); //set message colour as blue
        }
    }

    private void drawPowerUp(GodModePowerUp powerUp,Graphics2D g2d){

        Shape s = powerUp.getPowerUp();

        g2d.setColor(powerUp.getInner());
        g2d.fill(s);

        g2d.setColor(powerUp.getBorder());
        g2d.draw(s);
    }

    private void drawMenu(Graphics2D g2d){ //colour pause menu
        obscureGameBoard(g2d); //fill pause menu with black colour
        drawPauseMenu(g2d); //draw pause menu
    }

    private void obscureGameBoard(Graphics2D g2d){ //fill pause menu with black colour

        Composite tmp = g2d.getComposite(); //hold current composite

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f); //get alpha composite
        g2d.setComposite(ac); //set alpha composite

        g2d.setColor(Color.BLACK); //get black colour
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT); //fill frame with black colour

        g2d.setComposite(tmp); //reset composite
    }

    private void drawPauseMenu(Graphics2D g2d){

        Font tmpFont = g2d.getFont(); //hold current font

        g2d.setFont(menuFont); //set menu font
        g2d.setColor(MENU_COLOR); //set menu colour

        if(strLen == 0){ //if string length is 0
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width; //get width of bound string
        }

        int x = (DEF_WIDTH - strLen) / 2; //get position of pause menu
        int y = DEF_HEIGHT / 10;

        g2d.drawString(PAUSE,x,y); //draw pause menu

        x = DEF_WIDTH / 8; //get position of continue button
        y = DEF_HEIGHT / 4;

        g2d.drawString(CONTINUE,x,y); //draw continue button

        y *= 2; //get position of restart button

        g2d.drawString(RESTART,x,y); //draw restart button

        y *= 3.0/2; //get position of exit button

        g2d.drawString(EXIT,x,y); //draw exit button

        g2d.setFont(tmpFont); //reset font
    }
}
