package test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * Public class CementBrick is the subclass of Abstract public class Brick. It creates and returns a cement brick with
 * the given name, position, dimensions, inner and border colours, score multiplier and crack.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class CementBrick extends Brick {

    /**
     * The name of the cement brick.
     */
    private static final String NAME = "Cement Brick";
    /**
     * The inner colour of the cement brick.
     */
    private static final Color DEF_INNER = new Color(147, 147, 147);
    /**
     * The border colour of the cement brick.
     */
    private static final Color DEF_BORDER = new Color(217, 199, 175);
    /**
     * The strength of the cement brick.
     */
    private static final int CEMENT_STRENGTH = 2;

    /**
     * The score multiplier of the cement brick.
     */
    private static final int SCORE_MULTIPLIER = 40;

    /**
     * The crack of the cement brick.
     */
    private Crack crack;
    /**
     * The brick face of the cement brick.
     */
    private Shape brickFace;

    /**
     * This constructor creates a cement brick with the basic information such as name, position, dimensions, inner
     * and border colours, strength, score multiplier and crack with crack depth and step.
     * @param point The position of the new brick.
     * @param size The dimensions of the new brick.
     */
    public CementBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH,SCORE_MULTIPLIER); //get all basic information of clay brick
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS); //define crack information
        brickFace = super.brickFace; //get brick face
    }

    /**
     * This method is used to create cement bricks with the given position and given dimensions.
     * @param pos  This is the top-left position of the brick.
     * @param size This is the dimensions of the brick.
     * @return The cement bricks with the given position and given dimensions are returned.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size); //make cement brick at given point and given size
    }

    /**
     * This method is used to check if an impact occurs with a cement brick. If the brick is already broken then
     * no impact occurs. If the brick is unbroken, impact occurs and the condition of the brick is returned.
     * If the cement brick is unbroken, then a crack is added to the cement brick.
     * @param point The point of impact of the ball and the brick.
     * @param dir   The direction of travel of the crack.
     * @return This method returns a boolean to signify the condition of the brick.
     */
    @Override
    public boolean setImpact(Point2D point, int dir) { //get point of impact and impact direction
        if(super.isBroken()) //if already broken then no impact
            return false;
        super.impact(); //else signal impact to decrease strength

        if(!super.isBroken()){ //if not broken
            crack.makeCrack(point,dir); //make crack at point of impact and in given direction
            updateBrick(); //update brick to show crack
            return false; //signal not broken
        }
        return true; //signal broken
    }

    /**
     * This method returns the brick face of the cement brick.
     * @return The brick face of the cement brick is returned.
     */
    @Override
    public Shape getBrick() {
        return brickFace; //get brick to be coloured
    }

    /**
     * This method appends the new crack to the cement brick and updates the brick face.
     */
    private void updateBrick(){
        if(!super.isBroken()){ //if brick is not broken
            GeneralPath gp = crack.draw(); //draw crack
            gp.append(super.brickFace,false); //append crack to brick
            brickFace = gp; //set new brick face with crack
        }
    }

    /**
     * This method is used to repair the cement bricks by setting their broken flag to false and resetting their
     * strength. The crack on the cement brick is also removed.
     */
    public void repair(){ //repair brick
        super.repair(); //reset broken flag and reset strength
        crack.reset(); //remove crack
        brickFace = super.brickFace; //reset brick face
    }
}
