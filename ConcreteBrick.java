package test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;


public class ConcreteBrick extends Brick {

    private static final String NAME = "Concrete Brick";
    private static final Color DEF_INNER = new Color(50, 46, 46);
    private static final Color DEF_BORDER = new Color(24, 22, 16);
    private static final int CONCRETE_STRENGTH = 2;
    private static final double CONCRETE_PROBABILITY = 0.4;

    private static final int SCORE_MULTIPLIER = 100;

    private Crack crack;
    private Random rnd;
    private Shape brickFace;


    public ConcreteBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CONCRETE_STRENGTH,SCORE_MULTIPLIER); //get all basic information of clay brick
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS); //define crack information
        rnd = new Random();
        brickFace = super.brickFace; //get brick face
    }

    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size); //make cement brick at given point and given size
    }

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


    @Override
    public Shape getBrick() {
        return brickFace; //get brick to be coloured
    }

    private void updateBrick(){
        if(!super.isBroken()){ //if brick is not broken
            GeneralPath gp = crack.draw(); //draw crack
            gp.append(super.brickFace,false); //append crack to brick
            brickFace = gp; //set new brick face with crack
        }
    }

    public void repair(){ //repair brick
        super.repair(); //reset broken flag and reset strength
        crack.reset(); //remove crack
        brickFace = super.brickFace; //reset brick face
    }

    public void impact(){
        if(rnd.nextDouble() < CONCRETE_PROBABILITY){ //if random probability less than CONCRETE_PROBABILITY
            super.impact(); //signal impact and decrease brick strength
        }
    }
}
