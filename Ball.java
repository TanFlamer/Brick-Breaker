package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * Created by filippo on 04/09/16.
 *
 */
abstract public class Ball {

    private Shape ballFace;

    private Point2D center;

    Point2D up;
    Point2D down;
    Point2D left;
    Point2D right;

    private Color border;
    private Color inner;

    private int speedX;
    private int speedY;

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

    protected abstract Shape makeBall(Point2D center,int radiusA,int radiusB); //to be overridden in subclass

    public void move(){ //move ball according to speed
        RectangularShape tmp = (RectangularShape) ballFace;
        center.setLocation((center.getX() + speedX),(center.getY() + speedY)); //set ball at new location according to speed
        double w = tmp.getWidth(); //get ball width
        double h = tmp.getHeight(); //get ball height

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h); //make rectangle shape
        setPoints(w,h); //set new locations of up, down, left, right of ball


        ballFace = tmp; //set ball face as rectangle shape
    }

    public void setSpeed(int x,int y){ //set speed of ball
        speedX = x;
        speedY = y;
    }

    public void setXSpeed(int s){
        speedX = s;
    }

    public void setYSpeed(int s){
        speedY = s;
    }

    public void reverseX(){
        speedX *= -1;
    }

    public void reverseY(){
        speedY *= -1;
    }

    public Color getBorderColor(){
        return border;
    }

    public Color getInnerColor(){
        return inner;
    }

    public Point2D getPosition(){ //get position of ball
        return center;
    }

    public Shape getBallFace(){ //get ball face
        return ballFace;
    }

    public void moveTo(Point p){ //teleport ball to point p
        center.setLocation(p);

        RectangularShape tmp = (RectangularShape) ballFace;
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h);
        ballFace = tmp;
    }

    private void setPoints(double width,double height){ //set new locations of up, down, left, right of ball
        up.setLocation(center.getX(),center.getY()-(height / 2));
        down.setLocation(center.getX(),center.getY()+(height / 2));

        left.setLocation(center.getX()-(width / 2),center.getY());
        right.setLocation(center.getX()+(width / 2),center.getY());
    }

    public int getSpeedX(){
        return speedX;
    }

    public int getSpeedY(){
        return speedY;
    }


}
