/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Public class SteelBrick is the subclass of Abstract public class Brick. It creates and returns a steel brick with
 * the given name, position, dimensions, inner and border colours, score multiplier and damage probability.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class SteelBrick extends Brick {

    /**
     * The name of the steel brick.
     */
    private static final String NAME = "Steel Brick";
    /**
     * The inner colour of the steel brick.
     */
    private static final Color DEF_INNER = new Color(203, 203, 201);
    /**
     * The border colour of the steel brick.
     */
    private static final Color DEF_BORDER = Color.BLACK;
    /**
     * The strength of the steel brick.
     */
    private static final int STEEL_STRENGTH = 1;
    /**
     * The probability of damage to the steel brick.
     */
    private static final double STEEL_PROBABILITY = 0.4;

    /**
     * The score multiplier of the steel brick.
     */
    private static final int SCORE_MULTIPLIER = 40;

    /**
     * Randomizer to calculate damage probability of steel brick.
     */
    private Random rnd;
    /**
     * The brick face of the steel brick.
     */
    private Shape brickFace;

    /**
     * This constructor creates a steel brick with the basic information such as name, position, dimensions, inner
     * and border colours, strength and score multiplier.
     * @param point The position of the new brick.
     * @param size The dimensions of the new brick.
     */
    public SteelBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,STEEL_STRENGTH,SCORE_MULTIPLIER); //get all basic information of clay brick
        rnd = new Random();
        brickFace = super.brickFace; //get brick face
    }

    /**
     * This method is used to create steel bricks with the given position and given dimensions.
     * @param pos  This is the top-left position of the brick.
     * @param size This is the dimensions of the brick.
     * @return The steel bricks with the given position and given dimensions are returned.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size); //make steel brick at given point and given size
    }

    /**
     * This method returns the brick face of the steel brick.
     * @return The brick face of the steel brick is returned.
     */
    @Override
    public Shape getBrick() {
        return brickFace; //get brick to be coloured
    }

    /**
     * This method is used to check if an impact occurs with a steel brick. If the brick is already broken then
     * no impact occurs. If the brick is unbroken, impact occurs with a certain probability and the condition of
     * the brick is returned.
     *
     * @param point The point of impact of the ball and the brick.
     * @param dir   The direction of travel of the crack.
     * @return This method returns a boolean to signify the condition of the brick.
     */
    public boolean setImpact(Point2D point , int dir){
        if(super.isBroken()) //if already broken then no impact
            return false;
        impact(); //call impact function to determine if damage occurs
        return  super.isBroken(); //retain same condition
    }

    /**
     * This method causes an impact to the steel brick if the random probability is less than the damage
     * probability.
     */
    public void impact(){
        if(rnd.nextDouble() < STEEL_PROBABILITY){ //if random probability less than STEEL_PROBABILITY
            super.impact(); //signal impact and decrease brick strength
        }
    }
}
