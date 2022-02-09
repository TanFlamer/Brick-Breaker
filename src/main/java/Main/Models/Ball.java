package Main.Models;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Public class Ball is used to create a ball for the game with the given center point and diameter. It holds all the
 * relevant information about the ball such as colour, position and speed so that the ball can be rendered in the game.
 * All the setters and getters required are also here.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class Ball {

    /**
     * This variable is used to hold the shape of the ball and to move the ball to other points.
     */
    private Shape ballFace;
    /**
     * This is the speed of the ball in the horizontal direction.
     */
    private int speedX = 0;
    /**
     * This is the speed of the ball in the vertical direction.
     */
    private int speedY = 0;
    /**
     * The diameter of the ball.
     */
    private final int diameter;
    /**
     * This point is defined as the center point of the ball.
     */
    private Point center;
    /**
     * This point is defined as the top point of the ball.
     */
    private Point up;
    /**
     * This point is defined as the bottom point of the ball.
     */
    private Point down;
    /**
     * This point is defined as the left point of the ball.
     */
    private Point left;
    /**
     * This point is defined as the right point of the ball.
     */
    private Point right;
    /**
     * The inner colour of the ball.
     */
    private final Color inner = new Color(255, 219, 88);
    /**
     * The border colour of the ball.
     */
    private final Color border = inner.darker().darker();

    private final Color powerUp = Color.red;

    private boolean lost;

    private boolean collected;

    /**
     * This constructor creates a ball at the given center point and given diameter. The other 4 points of the ball are
     * defined relative to the center point.
     * @param center This is the center point of the new ball.
     * @param diameter This is the diameter of the new ball.
     */
    public Ball(Point center,int diameter){
        this.center = center;
        this.diameter = diameter;
        this.up = new Point(center.x,center.y - diameter/2);
        this.down = new Point(center.x,center.y + diameter/2);
        this.left = new Point(center.x - diameter/2,center.y);
        this.right = new Point(center.x + diameter/2,center.y);
        this.ballFace = new Ellipse2D.Double(center.x - (double) diameter/2,center.y - (double) diameter/2,diameter,diameter);
        this.lost = false;
        this.collected = false;
    }

    /**
     * This method is used to set the new center point of the ball to move the ball. The other 4 points of the ball
     * are also updated.
     * @param center This is the new center point of the ball.
     */
    public void setCenter(Point center) {
        this.center = center;
        setPoints(center);
    }

    /**
     * This method is used to set the 4 other points of the ball when the ball moves.
     * @param center This is the new center point of the ball.
     */
    public void setPoints(Point center){
        this.up = new Point(center.x,center.y - diameter/2);
        this.down = new Point(center.x,center.y + diameter/2);
        this.left = new Point(center.x - diameter/2,center.y);
        this.right = new Point(center.x + diameter/2,center.y);
    }

    /**
     * This method is used to return the ball face.
     * @return The ball face is returned.
     */
    public Shape getBallFace() {
        return ballFace;
    }

    /**
     * This method is used to change the ball face. It is used to move the ball to a new location to simulate movement.
     * @param ballFace This is the new ball face of the ball.
     */
    public void setBallFace(Shape ballFace) {
        this.ballFace = ballFace;
    }

    /**
     * This method is used to return the center point of the ball.
     * @return The center point of the ball is returned.
     */
    public Point getCenter() {
        return center;
    }

    /**
     * This method is used to return horizontal speed of the ball.
     * @return The horizontal speed of the ball is returned.
     */
    public int getSpeedX() {
        return speedX;
    }

    /**
     * This method is used to change the horizontal speed of the ball.
     * @param speedX This is the new horizontal speed of the ball.
     */
    public void setSpeedX(int speedX){
        this.speedX = speedX;
    }

    /**
     * This method is used to return vertical speed of the ball.
     * @return The vertical speed of the ball is returned.
     */
    public int getSpeedY() {
        return speedY;
    }

    /**
     * This method is used to change the vertical speed of the ball.
     * @param speedY This is the new vertical speed of the ball.
     */
    public void setSpeedY(int speedY){
        this.speedY = speedY;
    }

    /**
     * This method is used to return the inner colour of the ball.
     * @return The inner colour of the ball is returned.
     */
    public Color getInner() {
        return inner;
    }

    /**
     * This method is used to return the border colour of the ball.
     * @return The border colour of the ball is returned.
     */
    public Color getBorder() {
        return border;
    }

    /**
     * This method is used to return the top point of the ball.
     * @return The top point is returned.
     */
    public Point getUp() {
        return up;
    }

    /**
     * This method is used to return the bottom point of the ball.
     * @return The bottom point is returned.
     */
    public Point getDown() {
        return down;
    }

    /**
     * This method is used to return the left point of the ball.
     * @return The left point is returned.
     */
    public Point getLeft() {
        return left;
    }

    /**
     * This method is used to return the right point of the ball.
     * @return The right point is returned.
     */
    public Point getRight() {
        return right;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public Color getPowerUp() {
        return powerUp;
    }
}
