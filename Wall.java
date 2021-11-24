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


public class Wall {

    private static final int LEVELS_COUNT = 4;

    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;
    private static final int CONCRETE = 4;

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

    int[][] choice = new int[4][8];

    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos) throws IOException {

        this.startPoint = new Point(ballPos); //start point = initial ball position

        levels = makeCustomLevels(drawArea,brickCount,lineCount,brickDimensionRatio); //make levels
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

    private Brick[] makeRandomLevel(int row, int brickRow){

        int brickLength = 600 / brickRow;
        int brickHeight = 20;

        Brick[] tmp  = new Brick[(row * brickRow) + (row / 2)];

        Dimension brickSize = new Dimension(brickLength, brickHeight); //get dimensions of brickSize
        Point p = new Point(); //new point at (0,0)
        rnd = new Random(); //get random number

        int twoRows = 2 * brickRow + 1;

        for(int i = 0; i < tmp.length; i++){ //loop through whole array

            if(i % twoRows < brickRow){ //even row

                double x = i % twoRows * brickLength; // x = get corner X-coordinate of brick
                double y = i / twoRows * 2 * brickHeight; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
            }
            else{ //odd row

                int posX = i % twoRows - brickRow; //get position of brick on odd row

                double x = (posX * brickLength) - (brickLength / 2); // x = get corner X-coordinate of brick
                double y = (i / twoRows * 2 + 1) * brickHeight; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
            }

            if(rnd.nextDouble()>=0&&rnd.nextDouble()<0.25){
                tmp[i] = makeBrick(p,brickSize,CLAY);
            }
            else if(rnd.nextDouble()>=0.25&&rnd.nextDouble()<0.5){
                tmp[i] = makeBrick(p,brickSize,STEEL);
            }
            else if(rnd.nextDouble()>=0.5&&rnd.nextDouble()<0.75){
                tmp[i] = makeBrick(p,brickSize,CEMENT);
            }
            else{
                tmp[i] = makeBrick(p,brickSize,CONCRETE);
            }
        }
        return tmp;
    }

    private Brick[] makeCustomLevel(int row, int brickRow, int brickType, int brick1, int brick2, int brick3, int brick4){

        int brickLength = 600 / brickRow;
        int brickHeight = 20;

        Brick[] tmp  = new Brick[(row * brickRow) + (row / 2)];

        Dimension brickSize = new Dimension(brickLength, brickHeight); //get dimensions of brickSize
        Point p = new Point(); //new point at (0,0)

        int twoRows = 2 * brickRow + 1;

        for(int i = 0; i < tmp.length; i++){ //loop through whole array

            if(i % twoRows < brickRow){ //even row

                double x = i % twoRows * brickLength; // x = get corner X-coordinate of brick
                double y = i / twoRows * 2 * brickHeight; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
            }
            else{ //odd row

                int posX = i % twoRows - brickRow; //get position of brick on odd row

                double x = (posX * brickLength) - (brickLength / 2); // x = get corner X-coordinate of brick
                double y = (i / twoRows * 2 + 1) * brickHeight; // y = get corner Y-coordinate of brick

                p.setLocation(x,y); //set corner coordinate of brick
            }

            if(brickType == 0){
                tmp[i] = makeBrick(p,brickSize,brick1+1);
            }
            else if(brickType == 1){

                if(i%2==0){
                    tmp[i] = makeBrick(p,brickSize,brick1+1);
                }
                else{
                    tmp[i] = makeBrick(p,brickSize,brick2+1);
                }
            }
            else if(brickType == 2){

                if(i%3==0){
                    tmp[i] = makeBrick(p,brickSize,brick1+1);
                }
                else if(i%3==1){
                    tmp[i] = makeBrick(p,brickSize,brick2+1);
                }
                else{
                    tmp[i] = makeBrick(p,brickSize,brick3+1);
                }
            }
            else{
                if(i%4==0){
                    tmp[i] = makeBrick(p,brickSize,brick1+1);
                }
                else if(i%4==1){
                    tmp[i] = makeBrick(p,brickSize,brick2+1);
                }
                else if(i%4==2){
                    tmp[i] = makeBrick(p,brickSize,brick3+1);
                }
                else{
                    tmp[i] = makeBrick(p,brickSize,brick4+1);
                }
            }
        }
        return tmp;
    }

    //makeSingleTypeLevel is just makeChessboardLevel with same brick type
    private Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int type){
        return makeChessboardLevel(drawArea,brickCnt,lineCnt,brickSizeRatio,type,type);
    }

    //rewrote level generation to make it easier to read
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

    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos); //make ball at position (300,430)
    }

    private Brick[][] makeCustomLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio){

        Brick[][] tmp = makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio); //get default levels
        int brickRow = 0;
        choice = CustomPanel.choice;

        for(int i = 0;i < 4;i++){

            if(choice[i][2]>=0&&choice[i][2]<6){
                brickRow = choice[i][2] + 1;
            }
            else if(choice[i][2]==6){
                brickRow = 8;
            }
            else if(choice[i][2]==7){
                brickRow = 10;
            }
            else if(choice[i][2]==8){
                brickRow = 12;
            }
            else if(choice[i][2]==9){
                brickRow = 15;
            }

            if(choice[i][0]==1){
                tmp[i] = makeRandomLevel(choice[i][1]+1,brickRow);
            }
            else if(choice[i][0]==2){
                tmp[i] = makeCustomLevel(choice[i][1]+1,brickRow,choice[i][3],choice[i][4],choice[i][5],choice[i][6],choice[i][7]);
            }
        }
        return tmp;
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
        return switch (type) {
            case CLAY -> new ClayBrick(point, size);
            case STEEL -> new SteelBrick(point, size);
            case CEMENT -> new CementBrick(point, size);
            case CONCRETE -> new ConcreteBrick(point, size);
            default -> throw new IllegalArgumentException(String.format("Unknown Type:%d\n", type));
        }; //return new brick
    }
}
