import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class GameEngine {

    private GameBoard gameBoard;
    private GameBoardController controller;
    private GameBoardRenderer renderer;

    public GameEngine(JFrame owner,int[][] choice,BrickBreaker brickBreaker,GameSounds gameSounds) {
        gameBoard = new GameBoard(choice);
        controller = new GameBoardController(owner,gameBoard,brickBreaker,gameSounds);
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
