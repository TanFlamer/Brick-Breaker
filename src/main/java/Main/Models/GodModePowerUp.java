package Main.Models;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Public class GodModePowerUp is used to create a power up for the game with the given center point and diameter.
 * It holds all the relevant information about the power up such as colour, position and spawn and collected flags
 * so that the power up can be rendered in the game. All the setters and getters required are also here.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class GodModePowerUp {

    /**
     * This variable is used to hold the shape of the power up and to move the power up to other points.
     */
    private Shape powerUp;
    /**
     * This point is defined as the midpoint of the power up.
     */
    private Point midPoint;
    /**
     * The inner colour of the power up.
     */
    private final Color inner = Color.RED;
    /**
     * The border colour of the power up.
     */
    private final Color border = Color.BLACK;
    /**
     * A flag to signal if the power up has spawned and is ready for collection.
     */
    private boolean spawned = false;
    /**
     * A flag to signal if the power up has been collected and to start God Mode.
     */
    private boolean collected = false;

    /**
     * This constructor creates a power up at the given midpoint and given diameter.
     * @param midPoint This is the midpoint of the new power up.
     * @param diameter This is the diameter of the new power up.
     */
    public GodModePowerUp(Point midPoint, int diameter){
        this.midPoint = midPoint;
        this.powerUp = new Ellipse2D.Double(midPoint.x - (double) diameter/2,midPoint.y - (double) diameter/2,diameter,diameter);
    }

    /**
     * This method is used to return the midpoint of the power up.
     * @return The midpoint of the power up is returned.
     */
    public Point getMidPoint() {
        return midPoint;
    }

    /**
     * This method is used to set the new midpoint of the power up to move the power up.
     * @param midPoint This is the new midpoint of the power up.
     */
    public void setMidPoint(Point midPoint) {
        this.midPoint = midPoint;
    }

    /**
     * This method is used to return the power up face.
     * @return The power up face is returned.
     */
    public Shape getPowerUp() {
        return powerUp;
    }

    /**
     * This method is used to change the power up face. It is used to move the power up to a new location.
     * @param powerUp This is the new power up face of the ball.
     */
    public void setPowerUp(Shape powerUp) {
        this.powerUp = powerUp;
    }

    /**
     * This method is used to return the inner colour of the power up.
     * @return The inner colour of the power up is returned.
     */
    public Color getInner() {
        return inner;
    }

    /**
     * This method is used to return the border colour of the power up.
     * @return The border colour of the power up is returned.
     */
    public Color getBorder() {
        return border;
    }

    /**
     * This method is used to return the spawn condition of the power up.
     * @return The spawn condition of the power up is returned.
     */
    public boolean isSpawned() {
        return spawned;
    }

    /**
     * This method is used to set the new spawn condition of the power up.
     * @param spawned The new spawn condition of the power up.
     */
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    /**
     * This method is used to return the collection condition of the power up.
     * @return The collection condition of the power up is returned.
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * This method is used to set the new collection condition of the power up.
     * @param collected The new collection condition of the power up.
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
