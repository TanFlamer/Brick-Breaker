package Main.Others;

import Main.Models.Brick;

import java.awt.*;
import java.util.Random;

/**
 * Public class LevelGeneration is responsible for generating the brick for all 5 levels of the game. It receives the
 * player choices in the custom menu and generates the bricks accordingly.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class LevelGeneration {

    /**
     * Number of levels in the game.
     */
    public final int levelCount;

    /**
     * BrickID for Clay Bricks.
     */
    private static final int CLAY = 1;
    /**
     * BrickID for Steel Bricks.
     */
    private static final int STEEL = 2;
    /**
     * BrickID for Cement Bricks.
     */
    private static final int CEMENT = 3;
    /**
     * BrickID for Concrete Bricks.
     */
    private static final int CONCRETE = 4;

    /**
     * This is the randomizer to simulate randomness for the custom levels.
     */
    private static final Random random = new Random();

    /**
     * This is the double array to hold all the choices of the player in the custom console to create the custom levels.
     */
    private final int[][] choice;
    /**
     * This is the area of the game screen and is used to generate brick position.
     */
    private final Dimension area;

    /**
     * This method sends the custom choices, game screen area and level count to this class.
     * @param choice The choices of the player in the custom menu.
     * @param area The area of the game screen to calculate brick score.
     * @param levelCount The level count of the game.
     */
    public LevelGeneration(int[][] choice,Dimension area,int levelCount){
        this.choice = choice;
        this.area = area;
        this.levelCount = levelCount;
    }

    /**
     * This method controls the generation for all the custom levels and returns an array of Bricks for a custom level.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param brickRow This is the number of bricks in a row entered by the player. It is used unless the player chooses
     *                 a level type which uses random number of bricks in a row.
     * @param level This is the level number and is used to check the level custom choices.
     * @return The method returns an array of Bricks which is used for the generation of a custom level.
     */
    private Brick[][] makeAllLevel(Rectangle drawArea, int brickRow, int level){

        int randRow,randBrickRow;

        int [] brickNum = {1,2,3,4,5,6,8,10,12,15};
        int [] brickRand = {1,2,3,4};
        int [] brickChoice = {choice[level][4],choice[level][5],choice[level][6],choice[level][7]};

        for (int i = 0; i < brickRand.length; i++) {
            int randomIndexToSwap = random.nextInt(brickRand.length);
            int temp = brickRand[randomIndexToSwap];
            brickRand[randomIndexToSwap] = brickRand[i];
            brickRand[i] = temp;
        }

        if(choice[level][0]%4==0){
            randRow = random.nextInt(10) + 1;
            randBrickRow = brickNum[random.nextInt(10)];
        }
        else{
            randRow = choice[level][1] + 1;
            randBrickRow = brickRow;
        }

        double brickLength = drawArea.getWidth() / randBrickRow;
        double brickHeight = 20;

        Brick[][] tmp  = new Brick[2][];
        Brick[] even = new Brick[(randRow+1)/2 * randBrickRow];
        Brick[] odd = new Brick[randRow/2 * (randBrickRow+1)];

        Dimension brickSize = new Dimension((int) brickLength, (int) brickHeight);
        int brickTotal = (randRow * randBrickRow) + (randRow / 2);

        for(int i = 0; i < brickTotal; i++){

            Point p = getBrickLocation(drawArea,i,randBrickRow,(int)brickLength,(int)brickHeight,level);
            int twoRows = 2 * randBrickRow + 1;
            int evenRow = (i / twoRows) * randBrickRow;
            int oddRow = (i / twoRows) * (randBrickRow+1);

            if((choice[level][0]-1)/4==0){

                if(i % twoRows < randBrickRow){ //even row

                    if(choice[level][0]%4==0||choice[level][0]%4==3)
                        even[evenRow + i % twoRows] = makeBrick(p,brickSize,brickRand[i%4]);
                    else if(choice[level][0]%4==1)
                        even[evenRow + i % twoRows] = makeBrick(p,brickSize,brickChoice[i%(choice[level][3]+1)]+1);
                    else if(choice[level][0]%4==2)
                        even[evenRow + i % twoRows] = makeBrick(p,brickSize,brickRand[i%(choice[level][3]+1)]);
                }
                else{ //odd row

                    int posX = i % twoRows - randBrickRow; //get position of brick on odd row
                    if(choice[level][0]%4==0||choice[level][0]%4==3)
                        odd[oddRow + posX] = makeBrick(p,brickSize,brickRand[i%4]);
                    else if(choice[level][0]%4==1)
                        odd[oddRow + posX] = makeBrick(p,brickSize,brickChoice[i%(choice[level][3]+1)]+1);
                    else if(choice[level][0]%4==2)
                        odd[oddRow + posX] = makeBrick(p,brickSize,brickRand[i%(choice[level][3]+1)]);
                }
            }
            else if((choice[level][0]-1)/4==1){

                if(i % twoRows < randBrickRow){ //even row

                    if(choice[level][0]%4==0||choice[level][0]%4==3)
                        even[evenRow + i % twoRows] = makeBrick(p,brickSize,random.nextInt(4)+1);
                    else if(choice[level][0]%4==1)
                        even[evenRow + i % twoRows] = makeBrick(p,brickSize,brickChoice[random.nextInt(choice[level][3]+1)]+1);
                    else if(choice[level][0]%4==2)
                        even[evenRow + i % twoRows] = makeBrick(p,brickSize,brickRand[random.nextInt(choice[level][3]+1)]);
                }
                else{ //odd row

                    int posX = i % twoRows - randBrickRow; //get position of brick on odd row
                    if(choice[level][0]%4==0||choice[level][0]%4==3)
                        odd[oddRow + posX] = makeBrick(p,brickSize,random.nextInt(4)+1);
                    else if(choice[level][0]%4==1)
                        odd[oddRow + posX] = makeBrick(p,brickSize,brickChoice[random.nextInt(choice[level][3]+1)]+1);
                    else if(choice[level][0]%4==2)
                        odd[oddRow + posX] = makeBrick(p,brickSize,brickRand[random.nextInt(choice[level][3]+1)]);
                }
            }
        }
        tmp[0] = even;
        tmp[1] = odd;
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
     * @return This method returns an array of Clay Bricks for the first default level.
     */
    private Brick[][] makeSingleTypeLevel(Rectangle drawArea){
        return makeChessboardLevel(drawArea, CLAY, CLAY, 0);
    }

    /**
     * This method is used to generate an array of 31 bricks in 3 lines. The bricks are of 2 different types
     * depending on the conditions specified. This method controls the generation of the other 3 default levels.
     * For even rows, even bricks are of typeA while odd bricks are of typeB. For odd rows, bricks 6, 7 and 11
     * are of typeA while the rest are typeB. The brick generation code has been rewritten to improve readability.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param typeA This is the first type of brick to be generated for bricks satisfying certain conditions.
     * @param typeB This is the second type of brick to be generated for bricks satisfying certain conditions.
     * @param level This is the level number and is used to check the level orientation.
     * @return This method returns an array of Bricks for the other 3 default levels.
     */
    private Brick[][] makeChessboardLevel(Rectangle drawArea, int typeA, int typeB, int level){

        int brickOnLine = 30 / 3; //number of bricks on single line (number of bricks/number of lines)

        int centerLeft = brickOnLine / 2 - 1; // 10/2 - 1 = 5 - 1 = 4
        int centerRight = brickOnLine / 2 + 1; // 10/2 + 1 = 5 + 1 = 6

        double brickLen = drawArea.getWidth() / brickOnLine; //get brick length (width of area/number of bricks)
        double brickHgt = brickLen / 3; //get brick height (brick length/brick size ratio)

        Brick[][] tmp  = new Brick[2][]; //array of Bricks which account for extra brick in odd rows
        Brick[] even = new Brick[20];
        Brick[] odd = new Brick[11];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);

        int twoRows = 2 * brickOnLine + 1;

        for(int i = 0; i < 31; i++){

            Point p = getBrickLocation(drawArea,i,brickOnLine,(int)brickLen,(int)brickHgt,level);
            int rowNum = (i / twoRows) * brickOnLine;

            if(i % twoRows < brickOnLine){ //even row
                even[rowNum + i % twoRows] = ((i % twoRows) % 2 == 0) ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
            }
            else{ //odd row
                int posX = i % twoRows - brickOnLine; //get position of brick on odd row
                odd[rowNum + posX] = ((posX > centerLeft && posX <= centerRight) || posX == 10) ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
            }
        }
        tmp[0] = even;
        tmp[1] = odd;
        return tmp;
    }

    /**
     * This method is used to calculate the brick positions for brick generation. The level orientation must be
     * checked to make sure that the bricks are spawned in the right places.
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param i This is the number of the current brick being generated.
     * @param brickOnLine This is the number of bricks on an even line.
     * @param brickLength This is the length of a brick.
     * @param brickHeight This is the height of a brick.
     * @param level This is the level number and is used to check the level orientation.
     * @return This method returns the new brick position for brick generation.
     */
    private Point getBrickLocation(Rectangle drawArea, int i, int brickOnLine,int brickLength, int brickHeight, int level){

        Point p = new Point();
        double x = 0,y = 0;
        int twoRows = 2 * brickOnLine + 1;

        if(i % twoRows < brickOnLine){ //even row

            if(choice[level][9]==0){
                x = i % twoRows * brickLength; // x = get corner X-coordinate of brick
                y = Math.floor((double) i / twoRows) * 2 * brickHeight; // y = get corner Y-coordinate of brick
            }
            else if(choice[level][9]==1){
                x = drawArea.getWidth() - ((i%twoRows)+1)*brickLength;
                y = drawArea.getHeight() - (2*(Math.floor((double) i / twoRows))+1)*brickHeight;
            }
        }
        else{ //odd row

            int posX = i % twoRows - brickOnLine; //get position of brick on odd row
            if(choice[level][9]==0){
                x = (posX * brickLength) - ((double)brickLength / 2); // x = get corner X-coordinate of brick
                y = (Math.floor((double) i / twoRows) * 2 + 1) * brickHeight; // y = get corner Y-coordinate of brick
            }
            else if(choice[level][9]==1){
                x = drawArea.getWidth() + (double)brickLength/2 - (posX+1)*brickLength;
                y = drawArea.getHeight() - 2*(Math.floor((double) i / twoRows)+1)*brickHeight;
            }
        }
        p.setLocation(x,y); //set corner coordinate of brick
        return p;
    }

    /**
     * This method is used to create 5 levels to be loaded into the game. At first, the 5 default levels are created
     * and loaded into the double array of Bricks. Then, the level type entered by the player is read. If the level
     * type for that level entered is not default, a custom level is generated and overwrites the array of Bricks
     * for that level.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @return The method returns a double array of Bricks which contain the bricks generated for all 4 level.
     */
    public Brick[][][] makeCustomLevels(Rectangle drawArea){

        Brick[][][] tmp = makeDefaultLevels(drawArea); //get default levels

        int [] brickNum = {1,2,3,4,5,6,8,10,12,15};

        for(int i = 0;i < 5;i++){

            int brickRow = brickNum[choice[i][2]];

            if(choice[i][0]!=0)
                tmp[i] = makeAllLevel(drawArea,brickRow,i);
        }
        return tmp;
    }

    /**
     * This method creates the 5 default levels and loads them into a double array of Bricks.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @return The method returns a double array of Bricks which contain the bricks generated for all default 4 level.
     */
    private Brick[][][] makeDefaultLevels(Rectangle drawArea){
        Brick[][][] tmp = new Brick[levelCount][][]; //5 levels with 31 bricks each
        tmp[0] = makeSingleTypeLevel(drawArea);
        tmp[1] = makeChessboardLevel(drawArea, CLAY,CEMENT,1);
        tmp[2] = makeChessboardLevel(drawArea, CLAY,STEEL,2);
        tmp[3] = makeChessboardLevel(drawArea, STEEL,CEMENT,3);
        tmp[4] = makeChessboardLevel(drawArea, STEEL,CONCRETE,4);
        return tmp;
    }

    /**
     * This method is used to create a brick of a certain type with a given size at a given point.
     * @param point This is the top-left corner of the new brick.
     * @param size This is the dimensions of the new brick.
     * @param type This is the type of the new brick.
     * @return This method returns a new brick of a certain type with a given size at a given point.
     */
    private Brick makeBrick(Point point, Dimension size, int type){ //make new bricks
        return new Brick(type,point.x,point.y,size.width,size.height,area);
    }
}
