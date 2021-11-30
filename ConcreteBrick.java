package test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Public class ConcreteBrick is the subclass of Abstract public class Brick. It creates and returns a concrete brick with
 * the given name, position, dimensions, inner and border colours, score multiplier, crack and damage probability.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class ConcreteBrick extends Brick {

    /**
     * The name of the concrete brick.
     */
    private static final String NAME = "Concrete Brick";
    /**
     * The inner colour of the concrete brick.
     */
    private static final Color DEF_INNER = new Color(50, 46, 46);
    /**
     * The border colour of the concrete brick.
     */
    private static final Color DEF_BORDER = new Color(24, 22, 16);
    /**
     * The strength of the concrete brick.
     */
    private static final int CONCRETE_STRENGTH = 2;
    /**
     * The probability of damage to the concrete brick.
     */
    private static final double CONCRETE_PROBABILITY = 0.4;

    /**
     * The score multiplier of the concrete brick.
     */
    private static final int SCORE_MULTIPLIER = 100;

    /**
     * The crack of the concrete brick.
     */
    private Crack crack;
    /**
     * Randomizer to calculate damage probability of concrete brick.
     */
    private Random rnd;
    /**
     * The brick face of the concrete brick.
     */
    private Shape brickFace;

    /**
     * This constructor creates a concrete brick with the basic information such as name, position, dimensions, inner
     * and border colours, strength, score multiplier and crack with crack depth and step.
     * @param point The position of the new brick.
     * @param size The dimensions of the new brick.
     */
    public ConcreteBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CONCRETE_STRENGTH,SCORE_MULTIPLIER); //get all basic information of clay brick
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS); //define crack information
        rnd = new Random();
        brickFace = super.brickFace; //get brick face
    }

    /**
     * This method is used to create concrete bricks with the given position and given dimensions.
     * @param pos  This is the top-left position of the brick.
     * @param size This is the dimensions of the brick.
     * @return The concrete bricks with the given position and given dimensions are returned.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size); //make cement brick at given point and given size
    }

    /**
     * This method is used to check if an impact occurs with a concrete brick. If the brick is already broken then
     * no impact occurs. If the brick is unbroken, impact occurs with a certain probability and the condition of
     * the brick is returned. If the concrete brick is unbroken, then a crack is added to the concrete brick.
     *
     * @param point The point of impact of the ball and the brick.
     * @param dir   The direction of travel of the crack.
     * @return This method returns a boolean to signify the condition of the brick.
     */
    @Override
    public boolean setImpact(Point2D point, int dir) { //get point of impact and impact direction
        if(super.isBroken()) //if already broken then no impact
            return false;
        impact(); //else signal impact to decrease strength

        if(!super.isBroken()){ //if not broken
            crack.makeCrack(point,dir); //make crack at point of impact and in given direction
            updateBrick(); //update brick to show crack
            return false; //signal not broken
        }
        return true; //signal broken
    }

    /**
     * This method returns the brick face of the concrete brick.
     * @return The brick face of the concrete brick is returned.
     */
    @Override
    public Shape getBrick() {
        return brickFace; //get brick to be coloured
    }

    /**
     * This method appends the new crack to the concrete brick and updates the brick face.
     */
    private void updateBrick(){
        if(!super.isBroken()){ //if brick is not broken
            GeneralPath gp = crack.draw(); //draw crack
            gp.append(super.brickFace,false); //append crack to brick
            brickFace = gp; //set new brick face with crack
        }
    }

    /**
     * This method is used to repair the concrete bricks by setting their broken flag to false and resetting their
     * strength. The crack on the concrete brick is also removed.
     */
    public void repair(){ //repair brick
        super.repair(); //reset broken flag and reset strength
        crack.reset(); //remove crack
        brickFace = super.brickFace; //reset brick face
    }

    /**
     * This method causes an impact to the concrete brick if the random probability is less than the damage
     * probability.
     */
    public void impact(){
        if(rnd.nextDouble() < CONCRETE_PROBABILITY){ //if random probability less than CONCRETE_PROBABILITY
            super.impact(); //signal impact and decrease brick strength
        }
    }
}
