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


public class Wall {

    private static final int LEVELS_COUNT = 4;

    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;

    private Random rnd;
    private Rectangle area;

    Brick[] bricks;
    Ball ball;
    Player player;

    private Brick[][] levels;
    private int level;

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;

    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){

        this.startPoint = new Point(ballPos); //start point = initial ball position

        levels = makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio); //make levels
        level = 0; //start at level 0

        ballCount = 3; //start with 3 balls
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

    private Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int type){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt; //make brickCount divisible by lineCount

        int brickOnLine = brickCnt / lineCnt; //number of bricks on single line (number of bricks/number of lines)

        double brickLen = drawArea.getWidth() / brickOnLine; //get brick length (width of area/number of bricks)
        double brickHgt = brickLen / brickSizeRatio; //get brick height (brick length/brick size ratio)

        //brickSizeRatio = brick Length/brick Height = 6/2

        brickCnt += lineCnt / 2; //add half of lineCount to brickCount to account for extra brick in odd rows

        Brick[] tmp  = new Brick[brickCnt]; //array of Bricks with size brickCount

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt); //get dimensions of brickSize
        Point p = new Point(); //new point at (0,0)

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine; // line = i/number of bricks on single line

            if(line == lineCnt) //break loop when each line has 10 bricks (holes at the end of odd rows)
                break;

            double x = (i % brickOnLine) * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2)); // x = get corner X-coordinate of brick
            // if x is on even line, x = x, else move x half a brick length to the left
            double y = (line) * brickHgt; // y = get corner Y-coordinate of brick
            p.setLocation(x,y); //set corner coordinate of brick

            tmp[i] = makeBrick(p,brickSize,type); //make new brick at point p with size and type
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){ //fill in empty bricks to the right at odd row
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = new ClayBrick(p,brickSize);
        }
        return tmp;

    }

    private Brick[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt; //make brickCount divisible by lineCount

        int brickOnLine = brickCnt / lineCnt; //number of bricks on single line (number of bricks/number of lines)

        int centerLeft = brickOnLine / 2 - 1; // 10/2 - 1 = 5 - 1 = 4
        int centerRight = brickOnLine / 2 + 1; // 10/2 + 1 = 5 + 1 = 6

        double brickLen = drawArea.getWidth() / brickOnLine; //get brick length (width of area/number of bricks)
        double brickHgt = brickLen / brickSizeRatio; //get brick height (brick length/brick size ratio)

        //brickSizeRatio = brick Length/brick Height = 6/2

        brickCnt += lineCnt / 2; //add half of lineCount to brickCount to account for extra brick in odd rows

        Brick[] tmp  = new Brick[brickCnt]; //array of Bricks with size brickCount

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt); //get dimensions of brickSize
        Point p = new Point(); //new point at (0,0)

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine; // line = i/number of bricks on single line
            if(line == lineCnt) //break loop when each line has 10 bricks (holes at the end of odd rows)
                break;
            int posX = i % brickOnLine; //get number of brick of each line
            double x = posX * brickLen; //get X-coordinate of brick of each line
            x =(line % 2 == 0) ? x : (x - (brickLen / 2)); // x = get corner X-coordinate of brick
            // if x is on even line, x = x, else move x half a brick length to the left
            double y = (line) * brickHgt; // y = get corner Y-coordinate of brick
            p.setLocation(x,y); //set corner coordinate of brick

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            // make typeA brick if line is even and brick is even OR line is odd and brick is between 4 and 6
            // else make typeB brick
            tmp[i] = b ?  makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){ //fill in empty bricks to the right at odd row with typeA bricks
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }
        return tmp;
    }

    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos); //make ball at position (300,430)
    }

    private Brick[][] makeLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio){
        Brick[][] tmp = new Brick[LEVELS_COUNT][]; //4 levels with 31 bricks each
        //get column for each 2d-array
        tmp[0] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY);
        tmp[1] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CEMENT);
        tmp[2] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,STEEL);
        tmp[3] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,STEEL,CEMENT);
        return tmp;
    }

    public void move(){ //define move function
        player.move(); //move player
        ball.move(); //move ball
    }

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

    private boolean impactWall(){ //method to check impact with wall
        for(Brick b : bricks){
            switch(b.findImpact(ball)) {
                //Vertical Impact
                case Brick.UP_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.down, Brick.Crack.UP);
                case Brick.DOWN_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.up,Brick.Crack.DOWN);

                //Horizontal Impact
                case Brick.LEFT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.right,Brick.Crack.RIGHT);
                case Brick.RIGHT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.left,Brick.Crack.LEFT);
            }
        }
        return false;
    }

    private boolean impactBorder(){ //if ball impacts left or right border
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    public int getBrickCount(){
        return brickCount;
    }

    public int getBallCount(){
        return ballCount;
    }

    public boolean isBallLost(){ //if ball leaves bottom border
        return ballLost;
    }

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

    public void wallReset(){
        for(Brick b : bricks)
            b.repair(); //reset brick to full strength
        brickCount = bricks.length; //reset brick count
        ballCount = 3; //reset ball count
    }

    public boolean ballEnd(){ //if all balls are used up
        return ballCount == 0;
    }

    public boolean isDone(){ //if all bricks destroyed
        return brickCount == 0;
    }

    public void nextLevel(){
        bricks = levels[level++]; //load next level
        this.brickCount = bricks.length; //reset brick count
    }

    public boolean hasLevel(){ //if next level exists
        return level < levels.length;
    }

    public void setBallXSpeed(int s){
        ball.setXSpeed(s);
    }

    public void setBallYSpeed(int s){
        ball.setYSpeed(s);
    }

    public void resetBallCount(){
        ballCount = 3;
    }

    private Brick makeBrick(Point point, Dimension size, int type){ //make new bricks
        Brick out;
        switch(type){ //get type of brick
            case CLAY:
                out = new ClayBrick(point,size); //make clay brick at given point and given size
                break;
            case STEEL:
                out = new SteelBrick(point,size); //make steel brick at given point and given size
                break;
            case CEMENT:
                out = new CementBrick(point, size); //make cement brick at given point and given size
                break;
            default: //brick type does not exist
                throw  new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
        }
        return  out; //return new brick
    }

}
