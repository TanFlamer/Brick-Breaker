package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * Abstract public class Ball is used as a base to create a ball for the game and to define all the different
 * basic methods of the ball. The actual method to make the ball will be defined in the subclass.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
abstract public class Ball {

    /**
     * This variable is used to hold the shape of the ball and to move the ball to other points.
     */
    private Shape ballFace;

    /**
     * This point is defined as the center point of the ball.
     */
    private Point2D center;

    /**
     * This point is defined as the top point of the ball.
     */
    Point2D up;
    /**
     * This point is defined as the bottom point of the ball.
     */
    Point2D down;
    /**
     * This point is defined as the left point of the ball.
     */
    Point2D left;
    /**
     * This point is defined as the right point of the ball.
     */
    Point2D right;

    /**
     * This is the colour of the border of the ball.
     */
    private Color border;
    /**
     * This is the colour of the inner part of the ball.
     */
    private Color inner;

    /**
     * This is the speed of the ball in the horizontal direction.
     */
    private int speedX;
    /**
     * This is the speed of the ball in the vertical direction.
     */
    private int speedY;

    /**
     * This constructor is used to create a ball with basic information such as the center and the 4 directional points
     * of the ball, the radius of the ball, the border and inner colour of the ball and the initial speed of the ball.
     *
     * @param center This is the center point of the ball.
     * @param radiusA This is the width of the ball.
     * @param radiusB This is the height of the ball.
     * @param inner This is the inner colour of the ball.
     * @param border This is the border colour of the ball.
     */
    public Ball(Point2D center,int radiusA,int radiusB,Color inner,Color border){
        this.center = center; //get centre point of ball

        up = new Point2D.Double(); //top point of ball
        down = new Point2D.Double(); //bottom point of ball
        left = new Point2D.Double(); //left point of ball
        right = new Point2D.Double(); //right point of ball

        up.setLocation(center.getX(),center.getY()-(radiusB / 2)); //top point half a radius above centre point
        down.setLocation(center.getX(),center.getY()+(radiusB / 2)); //bottom point half a radius below centre point

        left.setLocation(center.getX()-(radiusA /2),center.getY()); //left point half a radius left of centre point
        right.setLocation(center.getX()+(radiusA /2),center.getY()); //right point half a radius rigth of centre point

        ballFace = makeBall(center,radiusA,radiusB);
        this.border = border; //ball border colour
        this.inner  = inner; //ball inner colour
        speedX = 0; //initial speed 0
        speedY = 0;
    }

    /**
     * This abstract method is used to create a ball with the given center point and given dimensions. This abstract
     * method is to be overridden in the subclass.
     *
     * @param center This is the center point of the ball.
     * @param radiusA This is the width of the ball.
     * @param radiusB This is the height of the ball.
     * @return This method returns a ball with the given center point and given dimensions.
     */
    protected abstract Shape makeBall(Point2D center,int radiusA,int radiusB); //to be overridden in subclass

    /**
     * This method defines the movement of the ball. The new position of the ball is calculated by adding the speed of
     * the ball to the old position of the ball. The ball is then moved to this new position. By calling this method
     * in quick succession, movement can be simulated.
     */
    public void move(){ //move ball according to speed
        RectangularShape tmp = (RectangularShape) ballFace;
        center.setLocation((center.getX() + speedX),(center.getY() + speedY)); //set ball at new location according to speed
        double w = tmp.getWidth(); //get ball width
        double h = tmp.getHeight(); //get ball height

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h); //make rectangle shape
        setPoints(w,h); //set new locations of up, down, left, right of ball

        ballFace = tmp; //set ball face as rectangle shape
    }

    /**
     * This method sets the new horizontal and vertical speed of the ball to the given value.
     * @param x The new horizontal speed of the ball.
     * @param y The new vertical speed of the ball.
     */
    public void setSpeed(int x,int y){ //set speed of ball
        speedX = x;
        speedY = y;
    }

    /**
     * This method sets the new horizontal speed of the ball to the given value.
     * @param s The new horizontal speed of the ball.
     */
    public void setXSpeed(int s){
        speedX = s;
    }

    /**
     * This method sets the new vertical speed of the ball to the given value.
     * @param s The new vertical speed of the ball.
     */
    public void setYSpeed(int s){
        speedY = s;
    }

    /**
     * This method reverses the horizontal speed of the ball to simulate collision and deflection.
     */
    public void reverseX(){
        speedX *= -1;
    }

    /**
     * This method reverses the vertical speed of the ball to simulate collision and deflection.
     */
    public void reverseY(){
        speedY *= -1;
    }

    /**
     * This method is used to return the border colour of the ball.
     * @return The border colour of the ball is returned.
     */
    public Color getBorderColor(){
        return border;
    }

    /**
     * This method is used to return the inner colour of the ball.
     * @return The inner colour of the ball is returned.
     */
    public Color getInnerColor(){
        return inner;
    }

    /**
     * This method is used to return the position of the ball.
     * @return The position of the ball is returned.
     */
    public Point2D getPosition(){ //get position of ball
        return center;
    }

    /**
     * This method is used to return the ball face.
     * @return The ball face is returned.
     */
    public Shape getBallFace(){ //get ball face
        return ballFace;
    }

    /**
     * This method is used to move the ball to a given point. The given point is set as the new midpoint of the ball,
     * and the ball is then moved to this point.
     * @param p This is the new center point of the ball.
     */
    public void moveTo(Point p){ //teleport ball to point p
        center.setLocation(p);

        RectangularShape tmp = (RectangularShape) ballFace;
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h);
        ballFace = tmp;
    }

    /**
     * This method is used to set the new directional points of the ball after the ball is moved.
     * @param width This is the width of the ball.
     * @param height This is the height of the ball.
     */
    private void setPoints(double width,double height){ //set new locations of up, down, left, right of ball
        up.setLocation(center.getX(),center.getY()-(height / 2));
        down.setLocation(center.getX(),center.getY()+(height / 2));

        left.setLocation(center.getX()-(width / 2),center.getY());
        right.setLocation(center.getX()+(width / 2),center.getY());
    }

    /**
     * This method returns the horizontal speed of the ball.
     * @return The horizontal speed of the ball is returned.
     */
    public int getSpeedX(){
        return speedX;
    }

    /**
     * This method returns the vertical speed of the ball.
     * @return The vertical speed of the ball is returned.
     */
    public int getSpeedY(){
        return speedY;
    }

}
