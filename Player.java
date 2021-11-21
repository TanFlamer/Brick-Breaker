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
import java.awt.geom.Rectangle2D;


public class Player {


    public static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    public static final Color INNER_COLOR = Color.GREEN;

    private static final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerFace;
    private Point ballPoint;
    private int moveAmount;
    private int min;
    private int max;


    public Player(Point ballPoint,int width,int height,Rectangle container) {
        this.ballPoint = ballPoint; //ball position
        moveAmount = 0; //player has not moved
        playerFace = makeRectangle(width, height); //make rectangle player
        min = container.x + (width / 2); //set min position as half of width from left order
        max = min + container.width - width; //set max position as half of width from right border

    }

    private Rectangle makeRectangle(int width,int height){
        Point p = new Point((int)(ballPoint.getX() - (width / 2)),(int)ballPoint.getY()); //get corner coordinate
        return  new Rectangle(p,new Dimension(width,height)); //make rectangle player
    }

    public boolean impact(Ball b){ //scan to see if player contains down side of ball
        return playerFace.contains(b.getPosition()) && playerFace.contains(b.down) ;
    }

    public void move(){ //move player
        double x = ballPoint.getX() + moveAmount; //get player location after move
        if(x < min || x > max) //if X-coordinate exceeds min or max value
            return; //stop player from moving
        ballPoint.setLocation(x,ballPoint.getY()); //else set new ball point
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y); //set new player location
    }

    public void moveLeft(){ //move player left by default amount
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    public void movRight(){ //move player right by default amount
        moveAmount = DEF_MOVE_AMOUNT;
    }

    public void stop(){ //player does not move
        moveAmount = 0;
    }

    public Shape getPlayerFace(){ //get player face
        return  playerFace;
    }

    public void moveTo(Point p){ //teleport player to point p
        ballPoint.setLocation(p); //set ball location at p
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y); //set player location
    }
}
