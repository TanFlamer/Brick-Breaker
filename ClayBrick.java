package test;

import java.awt.*;

/**
 * Public class ClayBrick is the subclass of Abstract public class Brick. It creates and returns a clay brick with
 * the given name, position, dimensions, inner and border colours and score multiplier.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class ClayBrick extends Brick {

    /**
     * The name of the clay brick.
     */
    private static final String NAME = "Clay Brick";
    /**
     * The inner colour of the clay brick.
     */
    private static final Color DEF_INNER = new Color(178, 34, 34).darker();
    /**
     * The border colour of the clay brick.
     */
    private static final Color DEF_BORDER = Color.GRAY;
    /**
     * The strength of the clay brick.
     */
    private static final int CLAY_STRENGTH = 1;

    /**
     * The score multiplier of the clay brick.
     */
    private static final int SCORE_MULTIPLIER = 10;

    /**
     * This constructor creates a clay brick with the basic information such as name, position, dimensions, inner
     * and border colours, strength and score multiplier.
     * @param point The position of the new brick.
     * @param size The dimensions of the new brick.
     */
    public ClayBrick(Point point, Dimension size){ //get point and size of clay brick
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CLAY_STRENGTH,SCORE_MULTIPLIER); //get all basic information of clay brick
    }

    /**
     * This method is used to create clay bricks with the given position and given dimensions.
     * @param pos  This is the top-left position of the brick.
     * @param size This is the dimensions of the brick.
     * @return The clay bricks with the given position and given dimensions are returned.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size); //make clay brick at given point and given size
    }

    /**
     * This method returns the brick face of the clay brick.
     * @return The brick face of the clay brick is returned.
     */
    @Override
    public Shape getBrick() {
        return super.brickFace; //get brick to be coloured
    }
}
