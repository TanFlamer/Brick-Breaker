import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball {

    private Shape ballFace;

    private int speedX = 0;
    private int speedY = 0;
    private int diameter;

    private Point center;
    private Point up;
    private Point down;
    private Point left;
    private Point right;

    private Color inner = new Color(255, 219, 88);
    private Color border = inner.darker().darker();

    public Ball(Point center,int diameter){
        this.center = center;
        this.diameter = diameter;
        this.up = new Point(center.x,center.y - diameter/2);
        this.down = new Point(center.x,center.y + diameter/2);
        this.left = new Point(center.x - diameter/2,center.y);
        this.right = new Point(center.x + diameter/2,center.y);
        this.ballFace = new Ellipse2D.Double(center.x-diameter/2,center.y-diameter/2,diameter,diameter);
    }

    public void setCenter(Point center) {
        this.center = center;
        setPoints(center);
    }

    public void setPoints(Point center){
        this.up = new Point(center.x,center.y - diameter/2);
        this.down = new Point(center.x,center.y + diameter/2);
        this.left = new Point(center.x - diameter/2,center.y);
        this.right = new Point(center.x + diameter/2,center.y);
    }

    public Shape getBallFace() {
        return ballFace;
    }

    public void setBallFace(Shape ballFace) {
        this.ballFace = ballFace;
    }

    public Point getCenter() {
        return center;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX){
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY){
        this.speedY = speedY;
    }

    public Color getInner() {
        return inner;
    }

    public Color getBorder() {
        return border;
    }

    public Point getUp() {
        return up;
    }

    public Point getDown() {
        return down;
    }

    public Point getLeft() {
        return left;
    }

    public Point getRight() {
        return right;
    }
}
