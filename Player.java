package Main;

import java.awt.*;

/**
 * Public class Player creates a rectangle shape with which the player controls to bounce the ball towards the bricks.
 * The player with the given midpoint and dimensions are created. All the setters and getters required are also here.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class Player {

    /**
     * This variable is used to hold the shape of the player.
     */
    private Rectangle playerFace;
    /**
     * This point is defined as the midpoint of the player.
     */
    private Point midPoint;
    /**
     * The move amount of the player.
     */
    private int moveAmount;
    /**
     * The vertical move amount of the player.
     */
    private int verticalMoveAmount;
    /**
     * The min position of the player on the screen.
     */
    private final int min;
    /**
     * The max position of the player on the screen.
     */
    private final int max;
    /**
     * The max top position of the player on the screen.
     */
    private final int top;
    /**
     * The min bottom position of the player on the screen.
     */
    private final int bottom;
    /**
     * The border colour of the brick.
     */
    private final Color border = Color.GREEN.darker().darker();
    /**
     * The inner colour of the brick.
     */
    private final Color inner = Color.GREEN;

    /**
     * This constructor is used to create a player with the given midpoint and dimensions. The min and max position of
     * the player is also defined here using the dimensions of the screen.
     * @param midPoint The midpoint of the new player.
     * @param width The width of the new player.
     * @param height The height of the new player.
     * @param area The dimensions of the screen to define the min and max position of the player.
     */
    public Player(Point midPoint,int width,int height,Dimension area){
        this.midPoint = midPoint;
        moveAmount = 0;
        verticalMoveAmount = 0;
        min = width / 2;
        max = min + area.width - width;
        top = 0;
        bottom = area.height - height;
        playerFace = new Rectangle(new Point((int) (midPoint.getX() - (width / 2)), (int) midPoint.getY()),new Dimension(width,height)); //make rectangle player
    }

    /**
     * This method is used to return the midpoint of the player. It is used to check for the current position of the
     * player so that the player can be drawn.
     * @return The midpoint of the player is returned.
     */
    public Point getMidPoint() {
        return midPoint;
    }

    /**
     * This method is used to set the new midpoint of the player to move the player.
     * @param midPoint This is the new midpoint of the player.
     */
    public void setMidPoint(Point midPoint) {
        this.midPoint = midPoint;
    }

    /**
     * This method is used to return the move amount of the player. It is the amount that the player moves in the
     * next update cycle.
     * @return The move amount of the player is returned.
     */
    public int getMoveAmount() {
        return moveAmount;
    }

    /**
     * This method is change the move amount of the player. It is the amount that the player moves in the next update
     * cycle.
     * @param moveAmount This is the amount the player moves in the next update cycle.
     */
    public void setMoveAmount(int moveAmount) {
        this.moveAmount = moveAmount;
    }

    /**
     * This method is used to return the min position of the player on the screen.
     * @return The min position of the player is returned.
     */
    public int getMin() {
        return min;
    }

    /**
     * This method is used to return the max position of the player on the screen.
     * @return The max position of the player is returned.
     */
    public int getMax() {
        return max;
    }

    /**
     * This method is used to return the player face.
     * @return The player face is returned.
     */
    public Rectangle getPlayerFace() {
        return playerFace;
    }

    /**
     * This method is used to change the player face. It is used to move the player to a new location to simulate
     * movement.
     * @param playerFace This is the new player face of the player.
     */
    public void setPlayerFace(Rectangle playerFace) {
        this.playerFace = playerFace;
    }

    /**
     * This method is used to return the border colour of the player.
     * @return The border colour of the player is returned.
     */
    public Color getBorder() {
        return border;
    }

    /**
     * This method is used to return the inner colour of the player.
     * @return The inner colour of the player is returned.
     */
    public Color getInner() {
        return inner;
    }

    /**
     * This method is used to return the top max position of the player on the screen.
     * @return The top max position of the player is returned.
     */
    public int getTop() {
        return top;
    }

    /**
     * This method is used to return the bottom min position of the player on the screen.
     * @return The bottom min position of the player is returned.
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * This method is used to return the vertical move amount of the player. It is the amount that the player moves in the
     * next update cycle.
     * @return The vertical move amount of the player is returned.
     */
    public int getVerticalMoveAmount() {
        return verticalMoveAmount;
    }

    /**
     * This method is change the vertical move amount of the player. It is the amount that the player moves in the
     * next update cycle.
     * @param verticalMoveAmount This is the amount the player moves vertically in the next update cycle.
     */
    public void setVerticalMoveAmount(int verticalMoveAmount) {
        this.verticalMoveAmount = verticalMoveAmount;
    }
}
