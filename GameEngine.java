import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class GameEngine {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 450;
    public static final int BALL_DIAMETER = 10;
    public static final int POWER_UP_DIAMETER = 20;

    private Player player;
    private Ball ball;
    private GodModePowerUp powerUp;
    private GameBoard gameBoard;
    private GameBoardController controller;
    private GameBoardRenderer renderer;

    public GameEngine(JFrame owner,int[][] choice,BrickBreaker brickBreaker) {
        player = new Player(new Point(WIDTH/2,HEIGHT-20),150,10,new Rectangle(WIDTH,HEIGHT));
        ball = new Ball(new Point(WIDTH/2,HEIGHT-20),BALL_DIAMETER);
        powerUp = new GodModePowerUp(new Point(WIDTH/2,HEIGHT/2),POWER_UP_DIAMETER);
        gameBoard = new GameBoard(player,ball,powerUp,choice);
        controller = new GameBoardController(owner,gameBoard,brickBreaker);
        renderer = new GameBoardRenderer(gameBoard);
    }

    public void update() throws FileNotFoundException {
        controller.update();
    }

    public void render(Graphics g) {
        renderer.render(g);
    }

    public GameBoardController getController(){
        return controller;
    }

    public GameBoard getGameBoard(){
        return gameBoard;
    }
}
