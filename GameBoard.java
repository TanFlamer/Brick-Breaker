import java.awt.*;
import java.util.Random;

public class GameBoard {

    public static final int LEVELS_COUNT = 5;

    public static final int BALL_DIAMETER = 10;
    public static final int POWER_UP_DIAMETER = 20;

    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;
    private static final int CONCRETE = 4;

    private static final Random random = new Random();
    private final Brick[][] bricks;
    private Brick[] brick;
    private final int[][] scoreAndTime;
    private final int[][] choice;
    private final Dimension area;
    private Player player;
    private final Ball ball;
    private final GodModePowerUp powerUp;

    private int level = 0;
    private int messageFlag = 0;
    private int brickCount = 0;
    private int ballCount = 3;
    private int godModeTimeLeft = 0;
    private int powerUpSpawns = 0;
    private int startTime = 0;

    private boolean showPauseMenu = false;
    private boolean ballLost = false;
    private boolean pauseFlag = true;
    private boolean endFlag = false;

    public GameBoard(int[][] choice,Dimension area) {
        this.choice = choice;
        this.area = area;
        player = new Player(new Point(area.width/2,area.height-20),150,10,area);
        ball = new Ball(new Point(area.width/2,area.height-20),BALL_DIAMETER);
        powerUp = new GodModePowerUp(new Point(area.width/2,area.height/2),POWER_UP_DIAMETER);
        bricks = makeCustomLevels(new Rectangle(0,0,area.width,area.height), choice);
        scoreAndTime = new int[LEVELS_COUNT+1][2];
    }

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

    private Brick[] makeSingleTypeLevel(Rectangle drawArea){
        return makeChessboardLevel(drawArea, GameBoard.CLAY, GameBoard.CLAY, 0);
    }

    private Brick[] makeChessboardLevel(Rectangle drawArea, int typeA, int typeB, int level){

        int brickOnLine = 30 / 3; //number of bricks on single line (number of bricks/number of lines)

        int centerLeft = brickOnLine / 2 - 1; // 10/2 - 1 = 5 - 1 = 4
        int centerRight = brickOnLine / 2 + 1; // 10/2 + 1 = 5 + 1 = 6

        double brickLen = drawArea.getWidth() / brickOnLine; //get brick length (width of area/number of bricks)
        double brickHgt = brickLen / (double) 3; //get brick height (brick length/brick size ratio)

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

    private Point getBrickLocation(Rectangle drawArea, int i, int brickOnLine,int brickLength, int brickHeight, int level){

        Point p = new Point();
        double x = 0,y = 0;
        int twoRows = 2 * brickOnLine + 1;

        if(i % twoRows < brickOnLine){ //even row

            if(choice[level][9]==0){
                x = i % twoRows * brickLength; // x = get corner X-coordinate of brick
                y = i / twoRows * 2 * brickHeight; // y = get corner Y-coordinate of brick
            }
            else if(choice[level][9]==1){
                x = drawArea.getWidth() - ((i%twoRows)+1)*brickLength;
                y = drawArea.getHeight() - (2*(i/twoRows)+1)*brickHeight;
            }
        }
        else{ //odd row

            int posX = i % twoRows - brickOnLine; //get position of brick on odd row
            if(choice[level][9]==0){
                x = (posX * brickLength) - (brickLength / 2); // x = get corner X-coordinate of brick
                y = (i / twoRows * 2 + 1) * brickHeight; // y = get corner Y-coordinate of brick
            }
            else if(choice[level][9]==1){
                x = drawArea.getWidth() + brickLength/2 - (posX+1)*brickLength;
                y = drawArea.getHeight() - 2*(i/twoRows+1)*brickHeight;
            }
        }
        p.setLocation(x,y); //set corner coordinate of brick
        return p;
    }

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

    private Brick makeBrick(Point point, Dimension size, int type){ //make new bricks
        return new Brick(type,point.x,point.y,size.width,size.height,area);
    }

    public Brick[][] getBricks() {
        return bricks;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public Ball getBall() {
        return ball;
    }

    public int[][] getChoice(){
        return choice;
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBrickCount(){
        return brickCount;
    }

    public void setBrickCount(int brickCount) {
        this.brickCount = brickCount;
    }

    public int getBallCount(){
        return ballCount;
    }

    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }

    public Brick[] getBrick() {
        return brick;
    }

    public void setBrick(Brick[] brick) {
        this.brick = brick;
    }

    public int[][] getScoreAndTime() {
        return scoreAndTime;
    }

    public int getScore(int level){
        return scoreAndTime[level][0];
    }

    public int getTime(int level){
        return scoreAndTime[level][1];
    }

    public void setScore(int level, int score) {
        this.scoreAndTime[level][0] = score;
    }

    public void setTime(int level, int time) {
        this.scoreAndTime[level][1] = time;
    }

    public int getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }

    public GodModePowerUp getPowerUp() {
        return powerUp;
    }

    public int getGodModeTimeLeft() {
        return godModeTimeLeft;
    }

    public void setGodModeTimeLeft(int godModeTimeLeft) {
        this.godModeTimeLeft = godModeTimeLeft;
    }

    public boolean isShowPauseMenu() {
        return showPauseMenu;
    }

    public void setShowPauseMenu(boolean showPauseMenu) {
        this.showPauseMenu = showPauseMenu;
    }

    public int getPowerUpSpawns() {
        return powerUpSpawns;
    }

    public void setPowerUpSpawns(int powerUpSpawns) {
        this.powerUpSpawns = powerUpSpawns;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public boolean isBallLost() {
        return ballLost;
    }

    public void setBallLost(boolean ballLost) {
        this.ballLost = ballLost;
    }

    public boolean isPauseFlag() {
        return pauseFlag;
    }

    public void setPauseFlag(boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    public boolean isEndFlag() {
        return endFlag;
    }

    public void setEndFlag(boolean endFlag) {
        this.endFlag = endFlag;
    }
}
