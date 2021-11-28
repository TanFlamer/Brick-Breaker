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


public class SteelBrick extends Brick {

    private static final String NAME = "Steel Brick";
    private static final Color DEF_INNER = new Color(203, 203, 201);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int STEEL_STRENGTH = 1;
    private static final double STEEL_PROBABILITY = 0.4;

    private static final int SCORE_MULTIPLIER = 40;

    private Random rnd;
    private Shape brickFace;

    public SteelBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,STEEL_STRENGTH,SCORE_MULTIPLIER); //get all basic information of clay brick
        rnd = new Random();
        brickFace = super.brickFace; //get brick face
    }

    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size); //make steel brick at given point and given size
    }

    @Override
    public Shape getBrick() {
        return brickFace; //get brick to be coloured
    }

    public  boolean setImpact(Point2D point , int dir){
        if(super.isBroken()) //if already broken then no impact
            return false;
        impact(); //call impact function to determine if damage occurs
        return  super.isBroken(); //retain same condition
    }

    public void impact(){
        if(rnd.nextDouble() < STEEL_PROBABILITY){ //if random probability less than STEEL_PROBABILITY
            super.impact(); //signal impact and decrease brick strength
        }
    }
}
