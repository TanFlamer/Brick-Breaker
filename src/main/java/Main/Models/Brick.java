package Main.Models;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Public class Brick is used to create bricks for the game with the given brickId, point and dimensions. It holds all
 * the relevant information about the bricks such as colour, position, strength, break probability, crack and score.
 * All the setters and getters required are also here.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class Brick {

    /**
     * This variable is used to hold the shape of the individual brick.
     */
    private Shape brickFace;
    /**
     * This variable is used to hold the backup shape of the individual brick. It is used to override the brick face
     * during repairs to remove the crack.
     */
    private Shape brickFaceNew;
    /**
     * The border colour of the brick.
     */
    private Color border;
    /**
     * The inner colour of the brick.
     */
    private Color inner;
    /**
     * The full strength of the brick.
     */
    private int fullStrength;
    /**
     * The current strength of the brick.
     */
    private int strength;
    /**
     * The probability of damage to the brick.
     */
    private double breakProbability;
    /**
     * The score of the brick when broken.
     */
    private final int score;
    /**
     * The score multiplier of the brick.
     */
    private int scoreMultiplier;
    /**
     * The flag to see if the brick is broken.
     */
    private boolean broken;
    /**
     * The flag to see if the brick is able to form a crack.
     */
    private boolean crackable;
    /**
     * The general path to hold the crack formed.
     */
    private GeneralPath crack;

    /**
     * This constructor is used to create a new brick with the given brickID, position and dimensions. The brickID
     * identifies the 4 different bricks.
     * @param brickID The ID of the brick to be created.
     * @param x The x-coordinate of the top-left corner of the new brick.
     * @param y The y-coordinate of the top-left corner of the new brick.
     * @param width The width of the new brick.
     * @param height The height of the new brick.
     * @param area The dimensions of the screen so that the width of the screen can be used to calculate the score of
     *             the new brick.
     */
    public Brick(int brickID, int x, int y, int width, int height, Dimension area){

        getBrickInfo(brickID,x,y,width,height);
        this.broken = false;
        this.strength = fullStrength;
        this.score = scoreMultiplier * area.width/width;
        this.brickFace = new Rectangle(new Point(x,y), new Dimension(width,height));
    }

    /**
     * This method is used to identify which type of new brick to create. All bricks are similar with only a few key
     * differences. The Clay and Cement bricks are just Steel and Concrete bricks but with 100% damage probability.
     * Similarly, the Cement and Concrete bricks are just crackable Clay and Steel bricks. The other attributes such as
     * colour, score multiplier and full strength are also defined here. A crack is defined here is the brick is
     * crackable. A second brick face is created to serve as a backup when repairing the cracked brick.
     * @param brickID The brickID of the new brick.
     * @param x The x-coordinate of the top-left corner of the new brick.
     * @param y The y-coordinate of the top-left corner of the new brick.
     * @param width The width of the new brick.
     * @param height The height of the new brick.
     */
    public void getBrickInfo(int brickID, int x, int y, int width, int height){

        switch (brickID) {
            case 1 -> {
                //Clay Brick
                fullStrength = 1;
                scoreMultiplier = 10;
                breakProbability = 1;
                crackable = false;
                border = Color.GRAY;
                inner = new Color(178, 34, 34).darker();
                crack = null;
                brickFaceNew = null;
            }
            case 2 -> {
                //Steel Brick
                fullStrength = 1;
                scoreMultiplier = 40;
                breakProbability = 0.4;
                crackable = false;
                border = Color.BLACK;
                inner = new Color(203, 203, 201);
                crack = null;
                brickFaceNew = null;
            }
            case 3 -> {
                //Cement Brick
                fullStrength = 2;
                scoreMultiplier = 40;
                breakProbability = 1;
                crackable = true;
                border = new Color(217, 199, 175);
                inner = new Color(147, 147, 147);
                crack = new GeneralPath();
                brickFaceNew = new Rectangle(new Point(x,y), new Dimension(width,height));
            }
            case 4 -> {
                //Concrete Brick
                fullStrength = 2;
                scoreMultiplier = 100;
                breakProbability = 0.4;
                crackable = true;
                border = new Color(24, 22, 16);
                inner = new Color(50, 46, 46);
                crack = new GeneralPath();
                brickFaceNew = new Rectangle(new Point(x,y), new Dimension(width,height));
            }
        }
    }

    /**
     * This method is used to return the border colour of the brick.
     * @return The border colour of the brick is returned.
     */
    public Color getBorder() {
        return border;
    }

    /**
     * This method is used to return the inner colour of the brick.
     * @return The inner colour of the brick is returned.
     */
    public Color getInner() {
        return inner;
    }

    /**
     * This method is used to return the brick face.
     * @return The brick face is returned.
     */
    public Shape getBrickFace() {
        return brickFace;
    }

    /**
     * This method is used to change the brick face. It is used to add a crack to the brick and repair the brick using
     * the backup brick face.
     * @param brickFace This is the new brick face of the brick.
     */
    public void setBrickFace(Shape brickFace){
        this.brickFace = brickFace;
    }

    /**
     * This method is used to return the backup brick face to repair the original brick face.
     * @return The backup brick face is returned.
     */
    public Shape getBrickFaceNew() {
        return brickFaceNew;
    }

    /**
     * This method returns the condition of the brick.
     * @return The condition of the brick is returned.
     */
    public boolean isBroken() {
        return broken;
    }

    /**
     * This method is used to change the condition of the brick.
     * @param broken This is the new condition of the brick.
     */
    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    /**
     * This method returns the damage probability of the brick to calculate if brick is undamaged in the collision.
     * @return The damage probability of the brick is returned.
     */
    public double getBreakProbability() {
        return breakProbability;
    }

    /**
     * This method returns the full strength of the brick.
     * @return The full strength of the brick is returned.
     */
    public int getFullStrength() {
        return fullStrength;
    }

    /**
     * This method returns the current strength of the brick.
     * @return The current strength of the brick is returned.
     */
    public int getStrength() {
        return strength;
    }

    /**
     * This method changes the current strength of the brick. It is used to decrease hit-points of the brick when
     * damaged and to restore the hit-points of the brick when repaired.
     * @param strength The new strength of the brick.
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * This method returns the general path of the brick so that crack can be formed.
     * @return The general path of the brick is returned.
     */
    public GeneralPath getCrack() {
        return crack;
    }

    /**
     * This method returns the crackable condition of the brick.
     * @return The crackable condition of the brick is returned.
     */
    public boolean isCrackable() {
        return crackable;
    }

    /**
     * This method returns the score of the brick when broken.
     * @return The score of the brick is returned.
     */
    public int getScore() {
        return score;
    }
}
