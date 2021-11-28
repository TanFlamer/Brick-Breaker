package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;


/**
 * Created by filippo on 04/09/16.
 *
 */
public class ClayBrick extends Brick {

    private static final String NAME = "Clay Brick";
    private static final Color DEF_INNER = new Color(178, 34, 34).darker();
    private static final Color DEF_BORDER = Color.GRAY;
    private static final int CLAY_STRENGTH = 1;

    private static final int SCORE_MULTIPLIER = 10;

    public ClayBrick(Point point, Dimension size){ //get point and size of clay brick
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CLAY_STRENGTH,SCORE_MULTIPLIER); //get all basic information of clay brick
    }

    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size); //make clay brick at given point and given size
    }

    @Override
    public Shape getBrick() {
        return super.brickFace; //get brick to be coloured
    }

}
