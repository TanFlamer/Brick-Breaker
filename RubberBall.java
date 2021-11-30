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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Public class RubberBall is the subclass of Abstract public class Ball. It creates and returns a rubber ball with
 * the given center point, radius, inner and border colour.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class RubberBall extends Ball {

    /**
     * The radius of the rubber ball.
     */
    private static final int DEF_RADIUS = 10;
    /**
     * The inner colour of the rubber ball.
     */
    private static final Color DEF_INNER_COLOR = new Color(255, 219, 88);
    /**
     * The border colour of the rubber ball.
     */
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    /**
     * This constructor creates a rubber ball with basic information such as center point, radius, inner and border colour.
     * @param center This is the center point of the new ball.
     */
    public RubberBall(Point2D center){ //get all basic information of rubber ball
        super(center,DEF_RADIUS,DEF_RADIUS,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }

    /**
     * This method is used to create a rubber ball with the given center point and given dimensions.
     * @param center  This is the center point of the ball.
     * @param radiusA This is the width of the ball.
     * @param radiusB This is the height of the ball.
     * @return This method returns a rubber ball with the given center point and given dimensions.
     */
    @Override
    protected Shape makeBall(Point2D center, int radiusA, int radiusB) { //create a ball with radius A/B

        double x = center.getX() - (radiusA / 2);
        double y = center.getY() - (radiusB / 2);

        return new Ellipse2D.Double(x,y,radiusA,radiusB);
    }
}
