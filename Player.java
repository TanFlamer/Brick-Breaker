import java.awt.*;

public class Player {

    private Rectangle playerFace;

    private Point midPoint;

    private int moveAmount;
    private int min;
    private int max;

    private Color border = Color.GREEN.darker().darker();
    private Color inner = Color.GREEN;

    public Player(Point midPoint,int width,int height,Rectangle container){
        this.midPoint = midPoint;
        moveAmount = 0;
        min = container.x + (width / 2);
        max = min + container.width - width;
        playerFace = new Rectangle(new Point((int) (midPoint.getX() - (width / 2)), (int) midPoint.getY()),new Dimension(width,height)); //make rectangle player
    }

    public Point getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(Point midPoint) {
        this.midPoint = midPoint;
    }

    public int getMoveAmount() {
        return moveAmount;
    }

    public void setMoveAmount(int moveAmount) {
        this.moveAmount = moveAmount;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public Rectangle getPlayerFace() {
        return playerFace;
    }

    public void setPlayerFace(Rectangle playerFace) {
        this.playerFace = playerFace;
    }

    public Color getBorder() {
        return border;
    }

    public Color getInner() {
        return inner;
    }
}
