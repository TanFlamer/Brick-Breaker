import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.io.FileNotFoundException;
import java.util.Random;

public class GameBoardController {

    public static final int DEF_WIDTH = 600;
    public static final int DEF_HEIGHT = 450;
    private static final int DEF_MOVE_AMOUNT = 5;

    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    public static final int LEFT = 10;
    public static final int RIGHT = 20;
    public static final int UP = 30;
    public static final int DOWN = 40;

    public static final int VERTICAL = 100;
    public static final int HORIZONTAL = 200;

    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;

    private static Random random = new Random();

    private GameBoard gameBoard;
    private BrickBreaker brickBreaker;
    private GameSounds gameSounds;
    private JFrame owner;
    private Player player;
    private Ball ball;
    private GodModePowerUp powerUp;
    private int[][] scoreAndTime;
    private int[][] choice;
    private int brickCount;
    private int ballCount;
    private int startTime;
    private int level = 0;

    private boolean ballLost = false;
    private boolean pauseFlag = true;
    private boolean endFlag = false;

    private Point startPoint;
    private Point playerStartPoint;

    public GameBoardController(JFrame owner,GameBoard gameBoard,BrickBreaker brickBreaker,GameSounds gameSounds) {
        this.gameBoard = gameBoard;
        this.brickBreaker = brickBreaker;
        this.gameSounds = gameSounds;
        this.owner = owner;
        this.player = gameBoard.getPlayer();
        this.ball = gameBoard.getBall();
        this.powerUp = gameBoard.getPowerUp();
        this.choice = gameBoard.getChoice();
        this.scoreAndTime = gameBoard.getScoreAndTime();

        if(choice[0][9]==0) {
            this.startPoint = ball.getCenter(); //start point = initial ball position
            this.playerStartPoint = player.getMidPoint();
        }
        else if (choice[0][9]==1){
            this.startPoint = new Point(DEF_WIDTH/2,20); //start point = initial ball position
            this.playerStartPoint = new Point(DEF_WIDTH/2,10); //start point = initial ball position
        }
        nextLevel(false);
    }

    public void update() throws FileNotFoundException {
        if(!pauseFlag&&!endFlag) {
            powerUpRandomSpawn();
            movePlayer();
            moveBall();
            findImpacts(powerUp.isCollected(), choice[level-1][9]);
            calculateScoreAndTime();
            gameChecks();
        }
    }

    public void movePlayer(){ //move player
        double x = player.getMidPoint().getX() + player.getMoveAmount(); //get player location after move
        if(x < player.getMin() || x > player.getMax()) //if X-coordinate exceeds min or max value
            return; //stop player from moving
        player.setMidPoint(new Point((int)x,(int)player.getMidPoint().getY())); //else set new ball point
        player.getPlayerFace().setLocation(player.getMidPoint().x - (int)player.getPlayerFace().getWidth()/2,player.getMidPoint().y); //set new player location
    }

    public void moveBall(){ //move ball according to speed
        RectangularShape tmp = (RectangularShape) ball.getBallFace();
        ball.setCenter(new Point((int)(ball.getCenter().getX() + ball.getSpeedX()),(int)(ball.getCenter().getY() + ball.getSpeedY()))); //set ball at new location according to speed

        double w = tmp.getWidth(); //get ball width
        double h = tmp.getHeight(); //get ball height

        tmp.setFrame((ball.getCenter().getX() -(w / 2)),(ball.getCenter().getY() - (h / 2)),w,h); //make rectangle shape
        ball.setBallFace(tmp);
    }

    public void moveLeft(){ //move player left by default amount
        player.setMoveAmount(-DEF_MOVE_AMOUNT);
    }

    public void moveRight(){ //move player right by default amount
        player.setMoveAmount(DEF_MOVE_AMOUNT);
    }

    public void stop(){ //move player right by default amount
        player.setMoveAmount(0);
    }

    public void reversePauseFlag(){
        pauseFlag = !pauseFlag;
        if(!pauseFlag){
            startTime = (int) java.time.Instant.now().getEpochSecond();
            gameBoard.setMessageFlag(0);
            gameSounds.getBgm().start();
        }
        else {
            gameSounds.getBgm().stop();
        }
    }

    public boolean getPauseFlag(){
        return pauseFlag;
    }

    public void setBallSpeed(){
        int speedX,speedY;
        do{
            speedX = random.nextInt(5) - 2; //random speed for ball in X-axis, - for left, + for right
        }while(speedX == 0); //continue loop if speed-X is 0
        do{
            if(choice[level-1][9]==0)
                speedY = -random.nextInt(3); //random speed for ball in Y-axis, always - for up
            else
                speedY = random.nextInt(3); //random speed for ball in Y-axis, always - for up
        }while(speedY == 0); //continue loop if speed-Y is 0

        ball.setSpeedX(speedX);
        ball.setSpeedY(speedY);
    }

    public void reverseX(){
        ball.setSpeedX(-ball.getSpeedX());
    }

    public void reverseY(){
        ball.setSpeedY(-ball.getSpeedY());
    }

    public void calculateScoreAndTime(){

        scoreAndTime[0][0] = returnPreviousLevelsScore();
        for(Brick b: gameBoard.getBrick()){
            if(b.isBroken()){
                scoreAndTime[0][0] += b.getScore();
            }
        }
        gameBoard.setScore(0,scoreAndTime[0][0]);
        storeLevelScore();
        gameBoard.setScore(level,scoreAndTime[level][0]);

        if((int) java.time.Instant.now().getEpochSecond() > startTime){
            scoreAndTime[0][1]++;
            startTime = (int) java.time.Instant.now().getEpochSecond();

            if(powerUp.isCollected()) {
                gameBoard.setGodModeTimeLeft(gameBoard.getGodModeTimeLeft()-1);
            }
        }
        gameBoard.setTime(0,scoreAndTime[0][1]);
        storeLevelTime();
        gameBoard.setTime(level,scoreAndTime[level][1]);
    }

    public void storeLevelScore(){
        scoreAndTime[level][0] = scoreAndTime[0][0] - returnPreviousLevelsScore();
    }

    public void storeLevelTime(){
        scoreAndTime[level][1] = scoreAndTime[0][1] - returnPreviousLevelsTime();
    }

    public int returnPreviousLevelsScore(){
        int total = 0;
        for(int i = level;i > 1; i--){
            total += scoreAndTime[i-1][0];
        }
        return total;
    }

    public int returnPreviousLevelsTime(){
        int total = 0;
        for(int i = level;i > 1; i--){
            total += scoreAndTime[i-1][1];
        }
        return total;
    }

    public void gameChecks() throws FileNotFoundException {

        if(isBallLost()){

            if(ballEnd()){
                resetLevelScoreAndTime();
                resetTotalScoreAndTime();
                wallReset();
                powerUp.setCollected(false);
                powerUp.setSpawned(false);
                gameBoard.setPowerUpSpawns(0);
                gameBoard.setMessageFlag(1);
                gameSounds.playSoundEffect("GameOver");
            }
            else {
                gameSounds.playSoundEffect("BallLost");
            }

            ballReset();
            if(!pauseFlag)
                reversePauseFlag();
        }
        else if(isLevelDone()){

            gameSounds.playSoundEffect("NextLevel");
            new ScoreBoard(owner,brickBreaker,level,scoreAndTime,choice);

            if(hasLevel()){
                wallReset();
                ballReset();
                gameBoard.setMessageFlag(2);
                nextLevel(true);
                if(!pauseFlag)
                    reversePauseFlag();
            }
            else {
                gameSounds.playSoundEffect("LastLevel");
                if(!pauseFlag)
                    reversePauseFlag();
                new ScoreBoard(owner,brickBreaker,0,scoreAndTime,choice);
                gameBoard.setMessageFlag(3);
                endFlag = true;
            }
        }

        if(powerUpCollected() && powerUp.isSpawned()&& !powerUp.isCollected()){
            gameSounds.playSoundEffect("Pickup");
            powerUp.setCollected(true);
            powerUp.setSpawned(false);
            gameBoard.setGodModeTimeLeft(10);
        }

        if(powerUp.isCollected()){
            if(gameBoard.getGodModeTimeLeft()==0){
                powerUp.setCollected(false);
            }
        }
    }

    public boolean isBallLost(){ //if ball leaves bottom border
        return ballLost;
    }

    public boolean ballEnd(){ //if all balls are used up
        return ballCount == 0;
    }

    public boolean isLevelDone(){ //if all bricks destroyed
        return brickCount == 0;
    }

    public boolean hasLevel(){ //if next level exists
        return level < gameBoard.getBricks().length;
    }

    public void nextLevel(boolean trueProgression){
        if(level==5)
            return;

        if(level!=0)
            wallReset();

        if(!trueProgression)
            resetLevelScoreAndTime();

        gameBoard.setBrick(gameBoard.getBricks()[level++]);
        gameBoard.setLevel(level);
        brickCount = gameBoard.getBrick().length; //reset brick count
        gameBoard.setBrickCount(brickCount);
        ballReset();
        resetBallCount();
        resetTotalScoreAndTime();
        powerUp.setCollected(false);
        powerUp.setSpawned(false);
        gameBoard.setPowerUpSpawns(0);
        gameSounds.setBgm("BGM"+level);
    }

    public void previousLevel(){
        if(level==1)
            return;

        resetLevelScoreAndTime();
        wallReset();
        level -= 2;
        gameBoard.setBrick(gameBoard.getBricks()[level++]);
        gameBoard.setLevel(level);
        brickCount = gameBoard.getBrick().length; //reset brick count
        gameBoard.setBrickCount(brickCount);
        ballReset();
        resetBallCount();
        resetTotalScoreAndTime();
        powerUp.setCollected(false);
        powerUp.setSpawned(false);
        gameBoard.setPowerUpSpawns(0);
        gameSounds.setBgm("BGM"+level);
    }

    public void resetTotalScoreAndTime(){
        scoreAndTime[0][0] = returnPreviousLevelsScore();
        gameBoard.setTime(0,scoreAndTime[0][0]);
        scoreAndTime[0][1] = returnPreviousLevelsTime();
        gameBoard.setTime(0,scoreAndTime[0][1]);
    }

    public void resetLevelScoreAndTime(){
        scoreAndTime[level][0] = 0;
        gameBoard.setScore(level,scoreAndTime[level][0]);
        scoreAndTime[level][1] = 0;
        gameBoard.setTime(level,scoreAndTime[level][1]);
    }

    public void ballReset(){ //when ball is lost

        if(choice[level-1][9]==0) {
            startPoint = new Point(DEF_WIDTH/2,DEF_HEIGHT-20); //start point = initial ball position
            playerStartPoint = new Point(DEF_WIDTH/2,DEF_HEIGHT-20);
        }
        else if (choice[level-1][9]==1){
            startPoint = new Point(DEF_WIDTH/2,20); //start point = initial ball position
            playerStartPoint = new Point(DEF_WIDTH/2,10); //start point = initial ball position
        }

        ballMoveTo(startPoint);
        playerMoveTo(playerStartPoint);

        setBallSpeed();
        ballLost = false; //ball is found
    }

    public void ballMoveTo(Point p){ //teleport ball to point p
        ball.setCenter(p);

        RectangularShape tmp = (RectangularShape) ball.getBallFace();
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((ball.getCenter().getX() - (w / 2)),(ball.getCenter().getY() - (h / 2)),w,h);
        ball.setBallFace(tmp);
    }

    public void playerMoveTo(Point p){ //teleport player to point p
        player.setMidPoint(p);

        Rectangle tmp = player.getPlayerFace();
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((player.getMidPoint().getX() - (w / 2)),player.getMidPoint().getY(),w,h);
        player.setPlayerFace(tmp);
    }

    public void powerUpMoveTo(Point p){
        powerUp.setMidPoint(p);

        RectangularShape tmp = (RectangularShape) powerUp.getPowerUp();

        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((powerUp.getMidPoint().getX() - w/2),(powerUp.getMidPoint().getY() - h/2),w,h);
        powerUp.setPowerUp(tmp);
    }

    public void powerUpRandomSpawn(){
        int x,y;

        if(gameBoard.getPowerUpSpawns() < (scoreAndTime[level][1]/60 + 1) && !powerUp.isSpawned() && !powerUp.isCollected()) {
            x = random.nextInt(401) + 100;
            if (choice[level - 1][9] == 0) {
                y = 325;
            } else {
                y = 125;
            }
            powerUp.setSpawned(true);
            powerUp.setCollected(false);
            powerUp.setMidPoint(new Point(x,y));
            powerUpMoveTo(powerUp.getMidPoint());
            gameBoard.setPowerUpSpawns(gameBoard.getPowerUpSpawns()+1);
        }
    }

    public boolean powerUpCollected(){
        return (powerUp.getPowerUp().contains(ball.getCenter())||powerUp.getPowerUp().contains(ball.getUp())||powerUp.getPowerUp().contains(ball.getDown())||powerUp.getPowerUp().contains(ball.getLeft())||powerUp.getPowerUp().contains(ball.getRight()));
    }

    public void wallReset(){
        for(Brick b : gameBoard.getBrick())
            repair(b); //reset brick to full strength
        brickCount = gameBoard.getBrick().length; //reset brick count
        gameBoard.setBrickCount(brickCount);
        resetBallCount();
    }

    public void resetBallCount(){
        if(choice[level-1][8]!=0) {
            ballCount = choice[level-1][8]; //reset ball count
        }
        else{
            ballCount = 3;
        }
        gameBoard.setBallCount(ballCount);
    }

    public void repair(Brick b){ //repair brick
        b.setBroken(false);
        b.setStrength(b.getFullStrength());
        b.getCrack().reset(); //remove crack
        b.setBrickFace(b.getBrickFaceNew());
    }

    public boolean ballPlayerImpact(Ball b, int playerPosition){ //scan to see if player contains bottom side of ball
        if(playerPosition==0){
            return player.getPlayerFace().contains(b.getCenter()) && player.getPlayerFace().contains(b.getDown());
        }
        else if (playerPosition==1){
            return player.getPlayerFace().contains(b.getCenter()) && player.getPlayerFace().contains(b.getUp());
        }
        return false;
    }

    public void findImpacts(boolean collected, int playerPosition){ //impact method
        if(ballPlayerImpact(ball,playerPosition)){ //if player hits ball
            gameSounds.playSoundEffect("Bounce");
            reverseY(); //reverse Y-direction
        }
        else if(impactWall(collected)){
            brickCount--; //minus brick count
            gameBoard.setBrickCount(brickCount);
        }
        else if(impactBorder()) { //if ball impacts border
            gameSounds.playSoundEffect("Bounce");
            reverseX(); //reverse X-direction
        }
        else if(choice[level-1][9]==0){
            if(ball.getCenter().getY() < 0){ //if ball hits top border
                gameSounds.playSoundEffect("Bounce");
                reverseY(); //reverse Y-direction
            }
            else if(ball.getCenter().getY() > DEF_HEIGHT){ //if ball hits bottom border
                ballCount--; //ball lost
                gameBoard.setBallCount(ballCount);
                ballLost = true; //ball lost is true
            }
        }
        else if(choice[level-1][9]==1){
            if(ball.getCenter().getY() < 0){ //if ball hits top border
                ballCount--; //ball lost
                gameBoard.setBallCount(ballCount);
                ballLost = true; //ball lost is true
            }
            else if(ball.getCenter().getY() > DEF_HEIGHT){ //if ball hits bottom border
                gameSounds.playSoundEffect("Bounce");
                reverseY(); //reverse Y-direction
            }
        }
    }

    private boolean impactWall(boolean collected){ //method to check impact with wall
        for(Brick b : gameBoard.getBrick()){
            //Vertical Impact
            switch (findImpact(ball,b)) {
                case UP_IMPACT -> {
                    if(!collected)
                        reverseY();
                    return setImpact(ball.getDown(),UP,b);
                }
                case DOWN_IMPACT -> {
                    if(!collected)
                        reverseY();
                    return setImpact(ball.getUp(),DOWN,b);
                } //Horizontal Impact
                case LEFT_IMPACT -> {
                    if(!collected)
                        reverseX();
                    return setImpact(ball.getRight(),LEFT,b);
                }
                case RIGHT_IMPACT -> {
                    if(!collected)
                        reverseX();
                    return setImpact(ball.getLeft(),RIGHT,b);
                }
            }
        }
        return false;
    }

    private boolean impactBorder(){ //if ball impacts left or right border
        Point2D p = ball.getCenter();
        return ((p.getX() < 0) ||(p.getX() > DEF_WIDTH));
    }

    public final int findImpact(Ball b,Brick brick){ //get direction of impact
        if(brick.isBroken()) //if already broken return 0
            return 0;
        int out  = 0;
        if(brick.getBrickFace().contains(b.getRight())) //else get direction of impact if brick face detects ball directional face
            out = LEFT_IMPACT;
        else if(brick.getBrickFace().contains(b.getLeft()))
            out = RIGHT_IMPACT;
        else if(brick.getBrickFace().contains(b.getUp()))
            out = DOWN_IMPACT;
        else if(brick.getBrickFace().contains(b.getDown()))
            out = UP_IMPACT;
        return out; //return direction of impact
    }

    public boolean setImpact(Point2D point, int dir, Brick b) { //get point of impact and impact direction
        if(b.isBroken()) //if already broken then no impact
            return false;

        if(impact(b)) {
            if (!b.isBroken() && b.isCrackable()) { //if not broken
                makeCrack(point, dir, b); //make crack at point of impact and in given direction
                updateBrick(b); //update brick to show crack
                return false; //signal not broken
            }
        }
        return b.isBroken(); //signal broken
    }

    private boolean impact(Brick b){
        if(random.nextDouble() < b.getBreakProbability()){ //if random probability less than STEEL_PROBABILITY
            gameSounds.playSoundEffect("Damage");
            b.setStrength(b.getStrength()-1); //reduce brick strength
            b.setBroken(b.getStrength() == 0); //if strength = 0, signal brick broken
            return true;
        }
        gameSounds.playSoundEffect("Deflect");
        return false;
    }

    private void makeCrack(Point2D point, int direction, Brick b){ //get point of impact and crack direction

        Rectangle bounds = b.getBrickFace().getBounds(); //get brick bounds
        Point impact = new Point((int)point.getX(),(int)point.getY()); //get point of impact
        Point start = new Point(); //start point of crack
        Point end = new Point(); //end point of crack

        switch(direction){ //get direction of crack
            case LEFT:
                start.setLocation(bounds.x + bounds.width, bounds.y); //top of right border
                end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height); //bottom of right border
                Point tmp = makeRandomPoint(start,end,VERTICAL); //random point of right border
                makeCrack(impact,tmp,b); //make crack from point of impact to random point of right border

                break;
            case RIGHT:
                start.setLocation(bounds.getLocation()); //top of left border
                end.setLocation(bounds.x, bounds.y + bounds.height); //bottom of left border
                tmp = makeRandomPoint(start,end,VERTICAL); //random point of left border
                makeCrack(impact,tmp,b); //make crack from point of impact to random point of left border

                break;
            case UP:
                start.setLocation(bounds.x, bounds.y + bounds.height); //left of bottom border
                end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height); //right of bottom border
                tmp = makeRandomPoint(start,end,HORIZONTAL); //random point of bottom border
                makeCrack(impact,tmp,b); //make crack from point of impact to random point of bottom border
                break;
            case DOWN:
                start.setLocation(bounds.getLocation()); //left of top border
                end.setLocation(bounds.x + bounds.width, bounds.y); //right of top border
                tmp = makeRandomPoint(start,end,HORIZONTAL); //random point of top border
                makeCrack(impact,tmp,b); //make crack from point of impact to random point of top border
                break;
        }
    }

    private void makeCrack(Point start, Point end, Brick b){ //make crack

        GeneralPath path = new GeneralPath(); //path of crack

        path.moveTo(start.x,start.y); //start of crack

        double w = (end.x - start.x) / (double)DEF_STEPS; //get width of each step
        double h = (end.y - start.y) / (double)DEF_STEPS; //get height of each step

        double x,y;

        for(int i = 1; i < DEF_STEPS;i++){

            x = (i * w) + start.x; //get next X-coordinate of next step
            y = (i * h) + start.y + randomInBounds(); //get next Y-coordinate of next step (random)

            path.lineTo(x,y); //draw crack to (x,y)
        }
        path.lineTo(end.x,end.y); //draw crack to final position
        b.getCrack().append(path,true); //connect crack to brick
    }

    private void updateBrick(Brick b){
        if(!b.isBroken()){ //if brick is not broken
            GeneralPath gp = b.getCrack(); //draw crack
            gp.append(b.getBrickFace(),false); //append crack to brick
            b.setBrickFace(gp);
        }
    }

    private Point makeRandomPoint(Point from,Point to, int direction){ //select random point between 2 given points

        Point out = new Point(); //new point
        int pos; //new coordinate

        switch (direction) {
            case HORIZONTAL -> {
                pos = random.nextInt(to.x - from.x) + from.x; //get random X-coordinate between from.x to to.x
                out.setLocation(pos, to.y); //set out to new point
            }
            case VERTICAL -> {
                pos = random.nextInt(to.y - from.y) + from.y; //get random Y-coordinate between from.y to.y
                out.setLocation(to.x, pos); //set out to new point
            }
        }
        return out; //return new point
    }

    private int randomInBounds(){ //get random addition to Y-coordinate
        int n = (DEF_CRACK_DEPTH * 2) + 1;
        return random.nextInt(n) - DEF_CRACK_DEPTH; //return random number between -bound to bound
    }

    public boolean getEndFlag() {
        return endFlag;
    }
}
