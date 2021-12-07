import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GodModePowerUp {

    private Shape powerUp;

    private Point midPoint;

    private final Color inner = Color.RED;
    private final Color border = Color.BLACK;

    private boolean spawned = false;
    private boolean collected = false;

    public GodModePowerUp(Point midPoint, int diameter){
        this.midPoint = midPoint;
        this.powerUp = new Ellipse2D.Double(midPoint.x - diameter/2,midPoint.y - diameter/2,diameter,diameter);
    }

    public Point getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(Point midPoint) {
        this.midPoint = midPoint;
    }

    public Shape getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(Shape powerUp) {
        this.powerUp = powerUp;
    }

    public Color getInner() {
        return inner;
    }

    public Color getBorder() {
        return border;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
