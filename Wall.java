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
import java.io.IOException;
import java.util.Random;

/**
 * Public class Wall is responsible for the generation and control for each level. Public class Wall creates and
 * positions all the bricks in the 4 levels. The player and the ball are also generated and positioned in this
 * class. Public class Wall also controls the behaviour of all the different entities of the game. Crack
 * generation on the bricks and movement of the player and ball are controlled from here. Public class Wall is
 * also responsible for the detection of game progression. By detecting the ball count and brick count, public
 * class Wall is able to deduce if the level is completed or failed. The methods to advance to the next level
 * or go back to a previous level are also found here.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class Wall {

    /**
     * This is the number of levels in the game.
     */
    private static final int LEVELS_COUNT = 4;
    /**
     * This is the code for CLAY Brick.
     */
    private static final int CLAY = 1;
    /**
     * This is the code for STEEL Brick.
     */
    private static final int STEEL = 2;
    /**
     * This is the code for CEMENT Brick.
     */
    private static final int CEMENT = 3;
    /**
     * This is the code for CONCRETE Brick.
     */
    private static final int CONCRETE = 4;

    /**
     * This is the randomizer to simulate randomness for the custom levels.
     */
    private Random rnd;
    /**
     * This is the area of the game screen.
     */
    private Rectangle area;

    /**
     * This is the array of Bricks to hold the current bricks of a level.
     */
    Brick[] bricks;
    /**
     * This is the ball used in the game.
     */
    Ball ball;
    /**
     * This is the player in the game.
     */
    Player player;

    /**
     * This is the double array of Bricks to hold the bricks generated for all 4 levels.
     */
    private Brick[][] levels;

    /**
     * This is the double array to hold all the choices of the player in the custom console to create the custom levels
     */
    private int[][] choice;
    /**
     * This is the current level number.
     */
    private int level;
    /**
     * This is the flag to signal the timer that a new level has been loaded and to reload the correct time.
     */
    public int flag = 0;

    /**
     * This is the start point of the ball and the player.
     */
    private Point startPoint;
    /**
     * This is the number of bricks which are currently not broken.
     */
    private int brickCount;
    /**
     * This is the number of balls that the player has left.
     */
    private int ballCount;
    /**
     * This is the boolean to signal if the ball has left the bottom page border and to reset the ball and player.
     */
    private boolean ballLost;

    /**
     * This constructor controls the generation of the bricks in each level by taking all the basic information
     * such as brick dimensions, brick count and line count to generate the default levels. The constructor also
     * receives the choices entered by the player in the custom console to generate custom levels. Ball and player
     * generation also occur here by making use of the initial ball position given.
     *
     * @param drawArea This is the area of the game screen.
     * @param brickCount This is the number of bricks which are generated in a default level.
     * @param lineCount This is the number of lines of bricks generated in a default level.
     * @param brickDimensionRatio This is the width-to-height ratio of a default brick.
     * @param ballPos This is the default position of the ball and the midpoint of the default position of the player.
     * @param choice This is the double array containing all the choices selected by the player in the custom console to
     *               control generation of custom levels.
     */
    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos, int[][] choice) {

        this.startPoint = new Point(ballPos); //start point = initial ball position
        this.choice = choice;

        levels = makeCustomLevels(drawArea,brickCount,lineCount,brickDimensionRatio,choice); //make levels
        level = 0; //start at level 0

        if(choice[level][8]!=0){
            ballCount = choice[level][8];
        }
        else {
            ballCount = 3; //start with 3 balls
        }
        ballLost = false; //ball initially not lost

        rnd = new Random(); //get random number

        makeBall(ballPos); //make ball at position (300,430)

        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2; //random speed for ball in X-axis, - for left, + for right
        }while(speedX == 0); //continue loop if speed-X is 0
        do{
            speedY = -rnd.nextInt(3); //random speed for ball in Y-axis, always - for up
        }while(speedY == 0); //continue loop if speed-Y is 0

        ball.setSpeed(speedX,speedY); //set ball speed

        player = new Player((Point) ballPos.clone(),150,10, drawArea); //make new player

        area = drawArea; //rectangle at (0,0) with 600 width and 450 height
    }

	/**
     * This method controls the generation for all the custom levels and returns an array of Bricks for a custom level.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param row This is the number of rows of bricks entered by the player. It is used unless the player chooses
     *            a level type which uses random rows of bricks.
     * @param brickRow This is the number of bricks in a row entered by the player. It is used unless the player chooses
     *                 a level type which uses random number of bricks in a row.
     * @param levelGen This is the type of level entered by the player. It controls the type of level generated from
     *                 the number of rows of bricks to the number of bricks in a row.
     * @param brickType This is the number of brick types that will spawn entered by the player. It is used unless the
     *                  player chooses a level type which uses random number of brick types which means that all 4 brick
     *                  types are used.
     * @param brick1 This is the first brick type entered by the player. It is used unless the player chooses a level type
     *               which uses random number of brick types.
     * @param brick2 This is the second brick type entered by the player. It is used unless the player chooses a level type
     *               which uses random number of brick types or if the player chooses less than 2 brick type.
     * @param brick3 This is the third brick type entered by the player. It is used unless the player chooses a level type
     *               which uses random number of brick types or if the player chooses less than 3 brick type.
     * @param brick4 This is the last brick type entered by the player. It is used unless the player chooses a level type
     *               which uses random number of brick types or if the player chooses less than 4 brick type.
     * @return The method returns an array of Bricks which is used for the generation of a custom level.
     */
    private Brick[] makeAllLevel(Rectangle drawArea, int row, int brickRow, int levelGen, int brickType, int brick1, int brick2, int brick3, int brick4){

        rnd = new Random(); //get random number
        int randRow = 0,randBrickRow = 0;

        int [] brickNum = {1,2,3,4,5,6,8,10,12,15};
        int [] brickRand = {1,2,3,4};
        int [] brickChoice = {brick1,brick2,brick3,brick4};

        for (int i = 0; i < brickRand.length; i++) {
            int randomIndexToSwap = rnd.nextInt(brickRand.length);
            int temp = brickRand[randomIndexToSwap];
            brickRand[randomIndexToSwap] = brickRand[i];
            brickRand[i] = temp;
        }

        if(levelGen%4==0){
            randRow = rnd.nextInt(10) + 1;
            randBrickRow = brickNum[rnd.nextInt(10)];
        }
        else{
            randRow = row;
            randBrickRow = brickRow;
        }

        double brickLength = drawArea.getWidth() / randBrickRow;
        double brickHeight = 20;

        Brick[] tmp  = new Brick[(randRow * randBrickRow) + (randRow / 2)];

        Dimension brickSize = new Dimension((int) brickLength, (int) brickHeight); //get dimensions of brickSize
        Point p = new Point(); //new point at (0,0)

        int twoRows = 2 * randBrickRow + 1;

        for(int i = 0; i < tmp.length; i++){ //loop through whole array

            if(i % twoRows < randBrickRow){ //even row

                double x = i % twoRows * brickLength; // x = get corner X-coordinate of brick
                double y = i / twoRows * 2 * brickHeight; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
            }
            else{ //odd row

                int posX = i % twoRows - randBrickRow; //get position of brick on odd row

                double x = (posX * brickLength) - (brickLength / 2); // x = get corner X-coordinate of brick
                double y = (i / twoRows * 2 + 1) * brickHeight; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
            }

            if((levelGen-1)/4==0){

                if(levelGen%4==0||levelGen%4==3){
                    tmp[i] = makeBrick(p,brickSize,brickRand[i%4]);
                }
                else if(levelGen%4==1){
                    tmp[i] = makeBrick(p,brickSize,brickChoice[i%(brickType+1)]+1);
                }
                else if(levelGen%4==2){
                    tmp[i] = makeBrick(p,brickSize,brickRand[i%(brickType+1)]);
                }
            }
            else if((levelGen-1)/4==1){

                if(levelGen%4==0||levelGen%4==3){
                    tmp[i] = makeBrick(p,brickSize,rnd.nextInt(4)+1);
                }
                else if(levelGen%4==1){
                    tmp[i] = makeBrick(p,brickSize,brickChoice[rnd.nextInt(brickType+1)]+1);
                }
                else if(levelGen%4==2){
                    tmp[i] = makeBrick(p,brickSize,brickRand[rnd.nextInt(brickType+1)]);
                }
            }
        }
        return tmp;
    }

    /**
     * This method is used to generate an array of 31 bricks in 3 lines, all of which are the same type. This
     * method is used to generate an array of Clay Bricks for the first default level. By studying the code closely,
     * we can conclude that this method works exactly the same as the method for making the other 3 default chessboard
     * levels if we specify both typeA and typeB brick to be the same. So, the method body is replaced with the
     * method call for the makeChessboardLevel to improve readability.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param brickCnt This is the number of bricks to be generated in the default levels.
     * @param lineCnt This is the number of lines of bricks to be generated in the default levels.
     * @param brickSizeRatio This is the width-to-height ratio of the default bricks.
     * @param type This is the type of brick to be generated for the whole array of Bricks.
     * @return This method returns an array of Clay Bricks for the first default level.
     */
    private Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int type){
        return makeChessboardLevel(drawArea,brickCnt,lineCnt,brickSizeRatio,type,type);
    }

    /**
     * This method is used to generate an array of 31 bricks in 3 lines. The bricks are of 2 different types
     * depending on the conditions specified. This method controls the generation of the other 3 default levels.
     * For even rows, even bricks are of typeA while odd bricks are of typeB. For odd rows, bricks 6, 7 and 11
     * are of typeA while the rest are typeB. The brick generation code has been rewritten to improve readability.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param brickCnt This is the number of bricks to be generated in the default levels.
     * @param lineCnt This is the number of lines of bricks to be generated in the default levels.
     * @param brickSizeRatio This is the width-to-height ratio of the default bricks.
     * @param typeA This is the first type of brick to be generated for bricks satisfying certain conditions.
     * @param typeB This is the second type of brick to be generated for bricks satisfying certain conditions.
     * @return This method returns an array of  Bricks for the other 3 default levels.
     */
    private Brick[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){

        int brickOnLine = brickCnt / lineCnt; //number of bricks on single line (number of bricks/number of lines)

        int centerLeft = brickOnLine / 2 - 1; // 10/2 - 1 = 5 - 1 = 4
        int centerRight = brickOnLine / 2 + 1; // 10/2 + 1 = 5 + 1 = 6

        double brickLen = drawArea.getWidth() / brickOnLine; //get brick length (width of area/number of bricks)
        double brickHgt = brickLen / brickSizeRatio; //get brick height (brick length/brick size ratio)

        Brick[] tmp  = new Brick[(brickCnt-(brickCnt % lineCnt))+(lineCnt / 2)]; //array of Bricks which account for extra brick in odd rows

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt); //get dimensions of brickSize
        Point p = new Point(); //new point at (0,0)

        int twoRows = 2 * brickOnLine + 1;

        for(int i = 0; i < tmp.length; i++){ //loop through whole array

            if(i % twoRows < brickOnLine){ //even row

                double x = i % twoRows * brickLen; // x = get corner X-coordinate of brick
                double y = i / twoRows * 2 * brickHgt; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
                tmp[i] = ((i % twoRows) % 2 == 0) ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
            }
            else{ //odd row

                int posX = i % twoRows - brickOnLine; //get position of brick on odd row

                double x = (posX * brickLen) - (brickLen / 2); // x = get corner X-coordinate of brick
                double y = (i / twoRows * 2 + 1) * brickHgt; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
                tmp[i] = ((posX > centerLeft && posX <= centerRight) || posX == 10) ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
            }
        }
        return tmp;
    }

	/**
     * This method creates a new rubber ball at the specified point.
     * @param ballPos This is the point where the new rubber ball will be created.
     */
    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos); //make ball at position (300,430)
    }

	/**
     * This method is used to create 4 levels to be loaded into the game. At first, the 4 default levels are created
     * and loaded into the double array of Bricks. Then, the level type entered by the player is read. If the level
     * type for that level entered is not default, a custom level is generated and overwrites the array of Bricks
     * for that level.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param brickCount This is the number of bricks to be generated in the default levels.
     * @param lineCount This is the number of lines of bricks to be generated in the default levels.
     * @param brickDimensionRatio This is the width-to-height ratio of the default bricks.
     * @param choice This is the double array containing all the choices selected by the player in the custom console to
     *               control generation of custom levels.
     * @return The method returns a double array of Bricks which contain the bricks generated for all 4 level.
     */
    private Brick[][] makeCustomLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio,int[][] choice){

        Brick[][] tmp = makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio); //get default levels

        int [] brickNum = {1,2,3,4,5,6,8,10,12,15};
        int brickRow = 0;

        for(int i = 0;i < 4;i++){

            brickRow = brickNum[choice[i][2]];

            if(choice[i][0]!=0)
                tmp[i] = makeAllLevel(drawArea,choice[i][1]+1,brickRow,choice[i][0],choice[i][3],choice[i][4],choice[i][5],choice[i][6],choice[i][7]);
        }
        return tmp;
    }

	/**
     * This method creates the 4 default levels and loads them into a double array of Bricks.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param brickCount This is the number of bricks to be generated in the default levels.
     * @param lineCount This is the number of lines of bricks to be generated in the default levels.
     * @param brickDimensionRatio This is the width-to-height ratio of the default bricks.
     * @return The method returns a double array of Bricks which contain the bricks generated for all default 4 level.
     */
    private Brick[][] makeLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio){
        Brick[][] tmp = new Brick[LEVELS_COUNT][]; //4 levels with 31 bricks each
        //get column for each 2d-array
        tmp[0] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY);
        tmp[1] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CEMENT);
        tmp[2] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,STEEL);
        tmp[3] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,STEEL,CEMENT);
        return tmp;
    }

	/**
     * This method controls the movement of the player and the ball and is called every cycle of the timer
     * to simulate movement.
     */
    public void move(){ //define move function
        player.move(); //move player
        ball.move(); //move ball
    }

	/**
     * This method is used to check for any impacts of the ball and define all the behaviours when impact with
     * the ball occurs. If impact occurs with the player, vertical direction of ball is reversed. If impact occurs
     * with a brick, the method below will compute the outcome. If impact occurs with the left and right page borders, horizontal direction of ball is reversed.
     * If impact occurs with the top page border, vertical direction of ball is reversed. Finally, if ball leaves the
     * bottom page border, ball is lost, ball count decreases and player nad ball position are reset.
     */
    public void findImpacts(){ //impact method
        if(player.impact(ball)){ //if player hits ball
            ball.reverseY(); //reverse Y-direction
        }
        else if(impactWall()){
            /*for efficiency reverse is done into method impactWall
             * because for every brick program checks for horizontal and vertical impacts
             */
            brickCount--; //minus brick count
        }
        else if(impactBorder()) { //if ball impacts border
            ball.reverseX(); //reverse X-direction
        }
        else if(ball.getPosition().getY() < area.getY()){ //if ball hits top border
            ball.reverseY(); //reverse Y-direction
        }
        else if(ball.getPosition().getY() > area.getY() + area.getHeight()){ //if ball hits bottom border
            ballCount--; //ball lost
            ballLost = true; //ball lost is true
        }
    }

	/**
     * This method is used to check for any impacts of the ball and the bricks. If impact occurs with a brick, it
     * checks if the brick is already broken. If brick is already broken, no impact occurs and the ball just passes
     * through. If brick is not broken, impact occurs and brick count will decrease if the brick is broken. The
     * direction of impact between the ball and brick are taken and the direction of motion of the ball is reversed.
     *
     * @return This method returns a boolean to signify if the brick impacted by the ball is unbroken at the start
     *         of the collision but is broken by the impact with the ball. This is so that brick count can be deceased.
     */
    private boolean impactWall(){ //method to check impact with wall
        for(Brick b : bricks){
            //Vertical Impact
            switch (b.findImpact(ball)) {
                case Brick.UP_IMPACT -> {
                    ball.reverseY();
                    return b.setImpact(ball.down, Brick.Crack.UP);
                }
                case Brick.DOWN_IMPACT -> {
                    ball.reverseY();
                    return b.setImpact(ball.up, Brick.Crack.DOWN);
                } //Horizontal Impact
                case Brick.LEFT_IMPACT -> {
                    ball.reverseX();
                    return b.setImpact(ball.right, Brick.Crack.RIGHT);
                }
                case Brick.RIGHT_IMPACT -> {
                    ball.reverseX();
                    return b.setImpact(ball.left, Brick.Crack.LEFT);
                }
            }
        }
        return false;
    }

	/**
     * This method is used to check for any impacts of the ball and the left and right page border.
     * @return This method returns a boolean to signify if the ball is leaving the left or right page border.
     */
    private boolean impactBorder(){ //if ball impacts left or right border
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

	/**
     * This method is used to return the brick count or number of bricks left unbroken.
     * @return This method return an integer to show number of bricks left unbroken.
     */
    public int getBrickCount(){
        return brickCount;
    }

	/**
     * This method is used to return the ball count or number of ball left for use.
     * @return This method return an integer to show number of ball left for use.
     */
    public int getBallCount(){
        return ballCount;
    }

	/**
     * This method is used to check if the current ball is lost.
     * @return This method returns a boolean to signify if the current ball has left the bottom page border.
     */
    public boolean isBallLost(){ //if ball leaves bottom border
        return ballLost;
    }

	/**
     * This method is used to reset the player and the ball to the default starting position. It is triggered when
     * the current ball has left the bottom page border. The ball is reset with a new horizontal and vertical speed.
     * The ball lost flag is then set to false.
     */
    public void ballReset(){ //when ball is lost
        player.moveTo(startPoint); //move player at position (300,430)
        ball.moveTo(startPoint); //move ball at position (300,430)
        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY); //set random speed for new ball
        ballLost = false; //ball is found
    }

	/**
     * This method is used to reset the condition of a level. All the bricks are repaired by removing the cracks
     * and setting their broken flag to false. The brick count and ball count is also reset.
     *
     * @param level This integer signifies the current level number so that the correct ball count can be obtained
     *              if the player has changed the ball count in the custom console.
     */
    public void wallReset(int level){
        for(Brick b : bricks)
            b.repair(); //reset brick to full strength
        brickCount = bricks.length; //reset brick count

        if(choice[level][8]!=0) {
            ballCount = choice[level][8]; //reset ball count
        }
        else{
            ballCount = 3;
        }
    }

	/**
     * This method is used to check if all available balls are used up.
     * @return This method returns a boolean to signify if the current ball count is 0.
     */
    public boolean ballEnd(){ //if all balls are used up
        return ballCount == 0;
    }

	/**
     * This method is used to check if all blocks are broken
     * @return This method returns a boolean to signify if current brick count is 0.
     */
    public boolean isDone(){ //if all bricks destroyed
        return brickCount == 0;
    }

	/**
     * This method is used to advance to the next level. If current level is last level, then method does not
     * activate. The method repairs all broken blocks of the current level before loading in the bricks of the
     * new level. Player and ball are moved to their default position and brick count and ball count are reset.
     * A flag is also triggered to signal to timer to reload the correct time.
     */
    public void nextLevel(){
        if(level==4)
            return;

        if(level!=0)
            wallReset(level-1);

        bricks = levels[level++]; //load next level
        brickCount = bricks.length; //reset brick count

        if(choice[level-1][8]!=0) {
            ballCount = choice[level-1][8]; //reset ball count
        }
        else{
            ballCount = 3;
        }
        flag = 1;
    }

	/**
     * This method is used to move to the previous level. If current level is first level, then method does not
     * activate. The method repairs all broken blocks of the current level before loading in the bricks of the
     * previous level. Player and ball are moved to their default position and brick count and ball count are reset.
     * A flag is also triggered to signal to timer to reload the correct time.
     */
    public void previousLevel(){
        if(level==1)
            return;

        wallReset(level-1);
        level -= 2;
        bricks = levels[level++]; //load previous level
        brickCount = bricks.length; //reset brick count

        if(choice[level-1][8]!=0) {
            ballCount = choice[level-1][8]; //reset ball count
        }
        else{
            ballCount = 3;
        }
        flag = 1;
    }

	/**
     * This method is used to check if there is still a level after current level.
     * @return This method returns a boolean which signifies if current level is less than total level length
     */
    public boolean hasLevel(){ //if next level exists
        return level < levels.length;
    }

	/**
     * This method is used to get current level number.
     * @return This method returns an integer which is the current level number.
     */
    public int getLevel(){
        return level;
    }

	/**
     * This method is used to change the horizontal speed of the ball.
     * @param s This is the new horizontal speed of the ball.
     */
    public void setBallXSpeed(int s){
        ball.setXSpeed(s);
    }

	/**
     * This method is used to change the vertical speed of the ball.
     * @param s This is the new vertical speed of the ball.
     */
    public void setBallYSpeed(int s){
        ball.setYSpeed(s);
    }

	/**
     * This method is used to reset number of balls the player has.
     */
    public void resetBallCount(){
        ballCount = 3;
    }

	/**
     * This method is used to create a brick of a certain type with a given size at a given point.
     * @param point This is the top-left corner of the new brick.
     * @param size This is the dimensions of the new brick.
     * @param type This is the type of the new brick.
     * @return This method returns a new brick of a certain type with a given size at a given point.
     */
    private Brick makeBrick(Point point, Dimension size, int type){ //make new bricks
        return switch (type) {
            case CLAY -> new ClayBrick(point, size);
            case STEEL -> new SteelBrick(point, size);
            case CEMENT -> new CementBrick(point, size);
            case CONCRETE -> new ConcreteBrick(point, size);
            default -> throw new IllegalArgumentException(String.format("Unknown Type:%d\n", type));
        }; //return new brick
    }
}