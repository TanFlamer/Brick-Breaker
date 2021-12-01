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

/**
 * Public class Player creates a rectangle shape with which the player controls to bounce the ball towards the bricks
 * The player with the given position, width, height, shape and methods are created.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class Player {

    /**
     * The border colour of the player.
     */
    public static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    /**
     * The inner colour of the player.
     */
    public static final Color INNER_COLOR = Color.GREEN;

    /**
     * The move amount of the player.
     */
    private static final int DEF_MOVE_AMOUNT = 5;

    /**
     * The rectangle shape of the player.
     */
    private Rectangle playerFace;
    /**
     * The midpoint of the player
     */
    private Point ballPoint;
    /**
     * The move amount of the player.
     */
    private int moveAmount;
    /**
     * The minimum position possible for the player.
     */
    private int min;
    /**
     * The maximum position possible for the player.
     */
    private int max;

    /**
     * This constructor creates a player with the given position, width, height and shape.
     * @param ballPoint The midpoint of the player.
     * @param width The width of the player.
     * @param height The height of the player.
     * @param container The shape of the player.
     */
    public Player(Point ballPoint,int width,int height,Rectangle container,int playerPosition) {
        this.ballPoint = ballPoint; //ball position
        moveAmount = 0; //player has not moved
        playerFace = makeRectangle(width, height, playerPosition); //make rectangle player
        min = container.x + (width / 2); //set min position as half of width from left order
        max = min + container.width - width; //set max position as half of width from right border
    }

    /**
     * This method creates a rectangle shape for the player with the given width and height.
     * @param width The width of the player.
     * @param height The height of the player.
     * @return The rectangle shape for the player with the given width and height is returned.
     */
    private Rectangle makeRectangle(int width,int height,int playerPosition){
        Point p = null;
        if(playerPosition==0||playerPosition==1) {
            p = new Point((int) (ballPoint.getX() - (width / 2)), (int) ballPoint.getY()); //get corner coordinate
        }
        return  new Rectangle(p,new Dimension(width,height)); //make rectangle player
    }

    /**
     * This method checks for any impact of the ball with the player. If player face contains the bottom side of
     * the ball inside, then impact has occurred.
     *
     * @param b The ball which is checked for impact with the player.
     * @return A boolean to signify if impact between the ball and player has occurred is returned.
     */
    public boolean impact(Ball b, int playerPosition){ //scan to see if player contains bottom side of ball
        if(playerPosition==0){
            return playerFace.contains(b.getPosition()) && playerFace.contains(b.down);
        }
        else if (playerPosition==1){
            return playerFace.contains(b.getPosition()) && playerFace.contains(b.up);
        }
        return false;
    }

    /**
     * This method defines the movement of the player. The new position of the ball is calculated by adding the move
     * amount of the player to the old position of the player. The new position of the player is then set as the old
     * position. The player is then moved to this new position. By calling this method in quick succession, movement
     * can be simulated.
     */
    public void move(){ //move player
        double x = ballPoint.getX() + moveAmount; //get player location after move
        if(x < min || x > max) //if X-coordinate exceeds min or max value
            return; //stop player from moving
        ballPoint.setLocation(x,ballPoint.getY()); //else set new ball point
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y); //set new player location
    }

    /**
     * This method moves player to the left by the default move amount.
     */
    public void moveLeft(){ //move player left by default amount
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    /**
     * This method moves player to the right by the default move amount.
     */
    public void movRight(){ //move player right by default amount
        moveAmount = DEF_MOVE_AMOUNT;
    }

    /**
     * This method stops the player movement by setting move amount to 0.
     */
    public void stop(){ //player does not move
        moveAmount = 0;
    }

    /**
     * This method is used to return the player face.
     * @return The player face is returned.
     */
    public Shape getPlayerFace(){ //get player face
        return  playerFace;
    }

    /**
     * This method is used to move the player to a given point. The given point is set as the new midpoint of the player,
     * and the player is then moved to this point.
     * @param p This is the new center point of the player.
     */
    public void moveTo(Point p){ //teleport player to point p
        ballPoint.setLocation(p); //set ball location at p
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y); //set player location
    }
}
