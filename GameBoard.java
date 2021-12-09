package Main;

import java.awt.*;
import java.util.Random;
import java.lang.Math;

/**
 * Public class GameBoard is responsible for loading in all entities of the game such as bricks,ball and player. All
 * state information about the entities such as position , game information such as score and time and game flags are
 * saved here. The controller manipulates the information here while the renderer renders the information here. The
 * information is read and manipulated using setters and getters.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class GameBoard {

    /**
     * Number of levels in the game.
     */
    public static final int LEVELS_COUNT = 5;

    /**
     * Diameter of the ball.
     */
    public static final int BALL_DIAMETER = 10;
    /**
     * Diameter of the power up.
     */
    public static final int POWER_UP_DIAMETER = 20;

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
     * This is the double array of Bricks to hold the bricks generated for all 5 levels.
     */
    private final Brick[][] bricks;
    /**
     * Double array of integer to record player scores and times.
     */
    private final int[][] scoreAndTime;
    /**
     * This is the double array to hold all the choices of the player in the custom console to create the custom levels.
     */
    private final int[][] choice;
    /**
     * This is the area of the game screen and is used to generate brick position.
     */
    private final Dimension area;
    /**
     * This is the player in the game.
     */
    private final Player player;
    /**
     * This is the ball used in the game.
     */
    private final Ball ball;
    /**
     * This is the power up in the game.
     */
    private final GodModePowerUp powerUp;

    /**
     * This is the current level number.
     */
    private int level = 0;
    /**
     * This is the message flag to signal type of message to render in game.
     */
    private int messageFlag = 0;
    /**
     * This is the current brick count.
     */
    private int brickCount = 0;
    /**
     * This is the current ball count.
     */
    private int ballCount = 3;
    /**
     * This is the God Mode time left.
     */
    private int godModeTimeLeft = 0;
    /**
     * This is the number of power up spawns left.
     */
    private int powerUpSpawns = 0;
    /**
     * This is the current start time used to count in-game time.
     */
    private int startTime = 0;

    /**
     * This is the flag to signal if pause menu is loaded.
     */
    private boolean showPauseMenu = false;
    /**
     * This is the flag to signal if ball is lost.
     */
    private boolean ballLost = false;
    /**
     * This is the flag to signal if game is paused.
     */
    private boolean pauseFlag = true;
    /**
     * This is the flag to signal if game has ended.
     */
    private boolean endFlag = false;

    /**
     * This is the start point of the ball.
     */
    private Point ballStartPoint = new Point(0,0);
    /**
     * This is the start point of the player.
     */
    private Point playerStartPoint = new Point(0,0);
    /**
     * This is the String array to hold the game messages.
     */
    private final String[] gameMessages;

    /**
     * This constructor loads in the game entities such as bricks, ball and player. The score and time are also
     * initialised. The choices of the player from the CustomConsole are used to generate the levels.
     * @param choice The choices of the player in the custom console to create the custom levels.
     * @param area The area of the game screen used to spawn the game entities.
     */
    public GameBoard(int[][] choice,Dimension area) {
        this.choice = choice;
        this.area = area;
        player = new Player(new Point(area.width/2,area.height-20),150,10,area);
        ball = new Ball(new Point(area.width/2,area.height-20),BALL_DIAMETER);
        powerUp = new GodModePowerUp(new Point(area.width/2,area.height/2),POWER_UP_DIAMETER);
        bricks = makeCustomLevels(new Rectangle(0,0,area.width,area.height), choice);
        scoreAndTime = new int[LEVELS_COUNT+1][2];
        gameMessages = new String[6];
    }

    /**
     * This method controls the generation for all the custom levels and returns an array of Bricks for a custom level.
     *
     * @param drawArea This is the area of the game screen. It is used to get the width of the screen so that
     *                 brick length can be determined by dividing the number of bricks.
     * @param choice The choices of the player in the custom console to create the custom levels.
     * @param brickRow This is the number of bricks in a row entered by the player. It is used unless the player chooses
     *                 a level type which uses random number of bricks in a row.
     * @param level This is the level number and is used to check the level custom choices.
     * @return The method returns an array of Bricks which is used for the generation of a custom level.
     */
    private Brick[] makeAllLevel(Rectangle drawArea, int[][] choice, int brickRow, int level){

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

        Brick[] tmp  = new Brick[(randRow * randBrickRow) + (randRow / 2)];

        Dimension brickSize = new Dimension((int) brickLength, (int) brickHeight); //get dimensions of brickSize

        for(int i = 0; i < tmp.length; i++){ //loop through whole array

            Point p = getBrickLocation(drawArea,i,randBrickRow,(int)brickLength,(int)brickHeight,level);

            if((choice[level][0]-1)/4==0){

                if(choice[level][0]%4==0||choice[level][0]%4==3){
                    tmp[i] = makeBrick(p,brickSize,brickRand[i%4]);
                }
                else if(choice[level][0]%4==1){
                    tmp[i] = makeBrick(p,brickSize,brickChoice[i%(choice[level][3]+1)]+1);
                }
                else if(choice[level][0]%4==2){
                    tmp[i] = makeBrick(p,brickSize,brickRand[i%(choice[level][3]+1)]);
                }
            }
            else if((choice[level][0]-1)/4==1){

                if(choice[level][0]%4==0||choice[level][0]%4==3){
                    tmp[i] = makeBrick(p,brickSize,random.nextInt(4)+1);
                }
                else if(choice[level][0]%4==1){
                    tmp[i] = makeBrick(p,brickSize,brickChoice[random.nextInt(choice[level][3]+1)]+1);
                }
                else if(choice[level][0]%4==2){
                    tmp[i] = makeBrick(p,brickSize,brickRand[random.nextInt(choice[level][3]+1)]);
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
     * @return This method returns an array of Clay Bricks for the first default level.
     */
    private Brick[] makeSingleTypeLevel(Rectangle drawArea){
        return makeChessboardLevel(drawArea, GameBoard.CLAY, GameBoard.CLAY, 0);
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
    private Brick[] makeChessboardLevel(Rectangle drawArea, int typeA, int typeB, int level){

        int brickOnLine = 30 / 3; //number of bricks on single line (number of bricks/number of lines)

        int centerLeft = brickOnLine / 2 - 1; // 10/2 - 1 = 5 - 1 = 4
        int centerRight = brickOnLine / 2 + 1; // 10/2 + 1 = 5 + 1 = 6

        double brickLen = drawArea.getWidth() / brickOnLine; //get brick length (width of area/number of bricks)
        double brickHgt = brickLen / 3; //get brick height (brick length/brick size ratio)

        Brick[] tmp  = new Brick[30+(3/2)]; //array of Bricks which account for extra brick in odd rows

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt); //get dimensions of brickSize

        int twoRows = 2 * brickOnLine + 1;

        for(int i = 0; i < tmp.length; i++){ //loop through whole array

            Point p = getBrickLocation(drawArea,i,brickOnLine,(int)brickLen,(int)brickHgt,level);

            if(i % twoRows < brickOnLine){ //even row
                tmp[i] = ((i % twoRows) % 2 == 0) ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
            }
            else{ //odd row
                int posX = i % twoRows - brickOnLine; //get position of brick on odd row
                tmp[i] = ((posX > centerLeft && posX <= centerRight) || posX == 10) ? makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
            }
        }
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
     * @param choice This is the double array containing all the choices selected by the player in the custom console to
     *               control generation of custom levels.
     * @return The method returns a double array of Bricks which contain the bricks generated for all 4 level.
     */
    private Brick[][] makeCustomLevels(Rectangle drawArea, int[][] choice){

        Brick[][] tmp = makeDefaultLevels(drawArea); //get default levels

        int [] brickNum = {1,2,3,4,5,6,8,10,12,15};

        for(int i = 0;i < 5;i++){

            int brickRow = brickNum[choice[i][2]];

            if(choice[i][0]!=0)
                tmp[i] = makeAllLevel(drawArea,choice,brickRow,i);
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
    private Brick[][] makeDefaultLevels(Rectangle drawArea){
        Brick[][] tmp = new Brick[LEVELS_COUNT][]; //4 levels with 31 bricks each
        //get column for each 2d-array
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

    /**
     * This method returns the double array of Bricks.
     * @return The double array of Bricks is returned.
     */
    public Brick[][] getBricks() {
        return bricks;
    }
    /**
     * This method returns the player.
     * @return The player is returned.
     */
    public Player getPlayer() {
        return player;
    }
    /**
     * This method returns the ball.
     * @return The ball is returned.
     */
    public Ball getBall() {
        return ball;
    }
    /**
     * This method returns the choices of the player in the CustomConsole.
     * @return The choices of the player in the CustomConsole is returned.
     */
    public int[][] getChoice(){
        return choice;
    }
    /**
     * This method returns the level number.
     * @return The level number is returned.
     */
    public int getLevel(){
        return level;
    }

    /**
     * This method changes the current level number.
     * @param level This is the new level number.
     */
    public void setLevel(int level) {
        this.level = level;
    }
    /**
     * This method returns the current brick count.
     * @return The current brick count is returned.
     */
    public int getBrickCount(){
        return brickCount;
    }

    /**
     * This method changes the current brick count.
     * @param brickCount This is the new brick count.
     */
    public void setBrickCount(int brickCount) {
        this.brickCount = brickCount;
    }
    /**
     * This method returns the current ball count.
     * @return The current ball count is returned.
     */
    public int getBallCount(){
        return ballCount;
    }
    /**
     * This method changes the current ball count.
     * @param ballCount This is the new ball count.
     */
    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }
    /**
     * This method returns all the scores and times of the player.
     * @return All the scores and times of the player are returned.
     */
    public int[][] getScoreAndTime() {
        return scoreAndTime;
    }

    /**
     * This method returns the score of the given level.
     * @param level The level of the score to be returned.
     * @return The score of the given level is returned.
     */
    public int getScore(int level){
        return scoreAndTime[level][0];
    }
    /**
     * This method returns the time of the given level.
     * @param level The level of the time to be returned.
     * @return The time of the given level is returned.
     */
    public int getTime(int level){
        return scoreAndTime[level][1];
    }
    /**
     * This method changes the score of the given level.
     * @param level The level of the score to be changed.
     * @param score The new score of the level.
     */
    public void setScore(int level, int score) {
        this.scoreAndTime[level][0] = score;
    }
    /**
     * This method changes the time of the given level.
     * @param level The level of the time to be changed.
     * @param time The new time of the level.
     */
    public void setTime(int level, int time) {
        this.scoreAndTime[level][1] = time;
    }

    /**
     * This method returns the message code to render the game message.
     * @return The message code to render the game message is returned.
     */
    public int getMessageFlag() {
        return messageFlag;
    }

    /**
     * This method changes the message code.
     * @param messageFlag This is the new message code.
     */
    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }

    /**
     * This method returns the power up.
     * @return The power up is returned.
     */
    public GodModePowerUp getPowerUp() {
        return powerUp;
    }
    /**
     * This method returns the God Mode time left.
     * @return The God Mode time left is returned.
     */
    public int getGodModeTimeLeft() {
        return godModeTimeLeft;
    }

    /**
     * This method changes the God Mode time left.
     * @param godModeTimeLeft This is the new God Mode time left.
     */
    public void setGodModeTimeLeft(int godModeTimeLeft) {
        this.godModeTimeLeft = godModeTimeLeft;
    }
    /**
     * This method returns the pause menu flag.
     * @return The pause menu flag is returned.
     */
    public boolean isShowPauseMenu() {
        return showPauseMenu;
    }
    /**
     * This method changes the pause menu flag.
     * @param showPauseMenu This is the new pause menu flag.
     */
    public void setShowPauseMenu(boolean showPauseMenu) {
        this.showPauseMenu = showPauseMenu;
    }
    /**
     * This method returns the God Mode orb spawns left.
     * @return The God Mode orb spawns left is returned.
     */
    public int getPowerUpSpawns() {
        return powerUpSpawns;
    }
    /**
     * This method changes the God Mode orb spawns left.
     * @param powerUpSpawns This is the new God Mode orb spawns left.
     */
    public void setPowerUpSpawns(int powerUpSpawns) {
        this.powerUpSpawns = powerUpSpawns;
    }
    /**
     * This method returns the start time of the game.
     * @return The start time of the game is returned.
     */
    public int getStartTime() {
        return startTime;
    }
    /**
     * This method changes the start time of the game.
     * @param startTime This is the new start time of the game.
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    /**
     * This method returns the ball lost flag.
     * @return The ball lost flag is returned.
     */
    public boolean isBallLost() {
        return ballLost;
    }
    /**
     * This method changes the ball lost flag.
     * @param ballLost This is the new ball lost flag.
     */
    public void setBallLost(boolean ballLost) {
        this.ballLost = ballLost;
    }
    /**
     * This method returns the not of the game pause flag.
     * @return The not of the game pause flag is returned.
     */
    public boolean isNotPaused() {
        return !pauseFlag;
    }
    /**
     * This method changes the game pause flag.
     * @param pauseFlag This is the new game pause flag.
     */
    public void setPauseFlag(boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }
    /**
     * This method returns the game end flag.
     * @return The game end flag is returned.
     */
    public boolean isEnded() {
        return endFlag;
    }
    /**
     * This method changes the game end flag.
     * @param endFlag This is the new game end flag.
     */
    public void setEndFlag(boolean endFlag) {
        this.endFlag = endFlag;
    }
    /**
     * This method returns the ball start point.
     * @return The ball start point is returned.
     */
    public Point getBallStartPoint() {
        return ballStartPoint;
    }
    /**
     * This method changes the ball start point.
     * @param ballStartPoint This is the new ball start point.
     */
    public void setBallStartPoint(Point ballStartPoint) {
        this.ballStartPoint = ballStartPoint;
    }
    /**
     * This method returns the player start point.
     * @return The player start point is returned.
     */
    public Point getPlayerStartPoint() {
        return playerStartPoint;
    }
    /**
     * This method changes the player start point.
     * @param playerStartPoint This is the new player start point.
     */
    public void setPlayerStartPoint(Point playerStartPoint) {
        this.playerStartPoint = playerStartPoint;
    }
    /**
     * This method returns the message specified with the code.
     * @param num The code of the message to be returned.
     * @return The message specified with the code is returned.
     */
    public String getGameMessages(int num) {
        return gameMessages[num];
    }
    /**
     * This method changes the specified game message with a new game message.
     * @param num The code of the message to be changed.
     * @param gameMessage The new game message.
     */
    public void setGameMessages(int num,String gameMessage) {
        this.gameMessages[num] = gameMessage;
    }
}
