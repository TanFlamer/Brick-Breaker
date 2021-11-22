package test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;


public class ConcreteBrick extends Brick {

    private static final String NAME = "Concrete Brick";
    private static final Color DEF_INNER = new Color(50, 46, 46);
    private static final Color DEF_BORDER = new Color(24, 22, 16);
    private static final int CONCRETE_STRENGTH = 3;

    private Crack crack;
    private Shape brickFace;


    public ConcreteBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CONCRETE_STRENGTH); //get all basic information of clay brick
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS); //define crack information
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
        super.impact(); //else signal impact to decrease strength

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
}