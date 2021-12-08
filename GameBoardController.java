import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Public class GameBoardController is responsible for processing all the entity behaviour in the game. All movement
 * and collision are processed here, all score and time are calculated here and all flags are processed here. After
 * the data is processed, the new data is loaded into the GameBoard so that it can be rendered by the renderer.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class GameBoardController {

    /**
     * The move amount of the player.
     */
    private static final int DEF_MOVE_AMOUNT = 5;
    /**
     * The code for impact to the top of brick.
     */
    public static final int UP_IMPACT = 100;
    /**
     * The code for impact to the bottom of brick.
     */
    public static final int DOWN_IMPACT = 200;
    /**
     * The code for impact to the left of brick.
     */
    public static final int LEFT_IMPACT = 300;
    /**
     * The code for impact to the right of brick.
     */
    public static final int RIGHT_IMPACT = 400;
    /**
     * The code for the left side of the brick.
     */
    public static final int LEFT = 10;
    /**
     * The code for the right side of the brick.
     */
    public static final int RIGHT = 20;
    /**
     * The code for the top side of the brick.
     */
    public static final int UP = 30;
    /**
     * The code for the bottom side of the brick.
     */
    public static final int DOWN = 40;
    /**
     * The code to choose a random point of the vertical side of the brick.
     */
    public static final int VERTICAL = 100;
    /**
     * The code to choose a random point of the horizontal side of the brick.
     */
    public static final int HORIZONTAL = 200;
    /**
     * The depth of the crack.
     */
    public static final int DEF_CRACK_DEPTH = 1;
    /**
     * The steps of the crack.
     */
    public static final int DEF_STEPS = 35;

    /**
     * Randomizer to get random values for power up position, crack path and brick damage probability.
     */
    private static final Random random = new Random();

    /**
     * GameBoard to get all game data.
     */
    private final GameBoard gameBoard;
    /**
     * BrickBreaker to repaint after ScoreBoard closes.
     */
    private final BrickBreaker brickBreaker;
    /**
     * GameSounds to add BGM and sound effects to the game.
     */
    private final GameSounds gameSounds;
    /**
     * JFrame to center ScoreBoard on screen.
     */
    private final JFrame owner;
    /**
     * This is the double array of Bricks to hold the bricks generated for all 5 levels.
     */
    private final Brick[][] bricks;
    /**
     * Player to manipulate movement.
     */
    private final Player player;
    /**
     * Ball to manipulate movement and collision.
     */
    private final Ball ball;
    /**
     * Power up to manipulate position, spawn and collection.
     */
    private final GodModePowerUp powerUp;
    /**
     * Choices of player in CustomConsole to correctly manipulate game data.
     */
    private final int[][] choice;
    /**
     * Dimensions of game screen to set game boundaries.
     */
    private final Dimension area;

    /**
     * This constructor loads in GameBoard where the game data is kept. Game data is read using getters to be
     * manipulated. After that, the old data is overwritten by the new manipulated data by using setters. Game sounds
     * such as BGM and sound effects are also added by loading in GameSounds.
     * @param owner JFrame to center ScoreBoard on screen.
     * @param gameBoard GameBoard to get all game data.
     * @param brickBreaker BrickBreaker to repaint after ScoreBoard closes.
     * @param gameSounds GameSounds to add BGM and sound effects to the game.
     * @param area Dimensions of game screen to set game boundaries.
     */
    public GameBoardController(JFrame owner,GameBoard gameBoard,BrickBreaker brickBreaker,GameSounds gameSounds,Dimension area) {
        this.gameBoard = gameBoard;
        this.brickBreaker = brickBreaker;
        this.gameSounds = gameSounds;
        this.owner = owner;
        this.bricks = gameBoard.getBricks();
        this.player = gameBoard.getPlayer();
        this.ball = gameBoard.getBall();
        this.powerUp = gameBoard.getPowerUp();
        this.choice = gameBoard.getChoice();
        this.area = area;
        nextLevel(false);
    }

    public void update() throws FileNotFoundException {
        if(gameBoard.isNotPaused()&&!gameBoard.isEnded()) {
            powerUpRandomSpawn();
            movePlayer();
            moveBall();
            findImpacts(powerUp.isCollected(), choice[gameBoard.getLevel()-1][9]);
            calculateScoreAndTime();
            generateGameMessages();
            gameChecks();
        }
        else {
            generateGameMessages();
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
        gameBoard.setPauseFlag(gameBoard.isNotPaused());
        if(gameBoard.isNotPaused()){
            gameBoard.setStartTime((int) java.time.Instant.now().getEpochSecond());
            gameBoard.setMessageFlag(0);
            gameSounds.getBgm().start();
        }
        else {
            gameSounds.getBgm().stop();
        }
    }

    public void setBallSpeed(){
        int speedX,speedY;
        do{
            speedX = random.nextInt(5) - 2; //random speed for ball in X-axis, - for left, + for right
        }while(speedX == 0); //continue loop if speed-X is 0
        do{
            if(choice[gameBoard.getLevel()-1][9]==0)
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

        gameBoard.setScore(0,returnPreviousLevelsScore());
        for(Brick b: bricks[gameBoard.getLevel()-1]){
            if(b.isBroken()){
                gameBoard.setScore(0,gameBoard.getScore(0) + b.getScore());
            }
        }
        gameBoard.setScore(gameBoard.getLevel(),gameBoard.getScore(0) - returnPreviousLevelsScore());

        if((int) java.time.Instant.now().getEpochSecond() > gameBoard.getStartTime()){
            gameBoard.setTime(0,gameBoard.getTime(0)+1);
            gameBoard.setStartTime((int) java.time.Instant.now().getEpochSecond());

            if(powerUp.isCollected()) {
                gameBoard.setGodModeTimeLeft(gameBoard.getGodModeTimeLeft()-1);
            }
        }
        gameBoard.setTime(gameBoard.getLevel(),gameBoard.getTime(0) - returnPreviousLevelsTime());
    }

    public int returnPreviousLevelsScore(){
        int total = 0;
        for(int i = gameBoard.getLevel();i > 1; i--){
            total += gameBoard.getScore(i-1);
        }
        return total;
    }

    public int returnPreviousLevelsTime(){
        int total = 0;
        for(int i = gameBoard.getLevel();i > 1; i--){
            total += gameBoard.getTime(i-1);
        }
        return total;
    }

    public void gameChecks() throws FileNotFoundException {

        if(gameBoard.isBallLost()){ //ball is lost

            if(gameBoard.getBallCount() == 0){ //if ball count 0
                resetLevelData();
                gameBoard.setMessageFlag(1);
                gameSounds.playSoundEffect("GameOver");
            }
            else {
                ballReset();
                gameSounds.playSoundEffect("BallLost");
            }

            if(gameBoard.isNotPaused())
                reversePauseFlag();
        }
        else if(gameBoard.getBrickCount() == 0){ //if level complete / brick count 0

            gameSounds.playSoundEffect("NextLevel");
            new ScoreBoard(owner,brickBreaker,gameBoard.getLevel(),gameBoard.getScoreAndTime(),choice);

            if(gameBoard.getLevel() < bricks.length){ //if level left / level number < total level
                wallReset();
                ballReset();
                gameBoard.setMessageFlag(2);
                nextLevel(true);
                if(gameBoard.isNotPaused())
                    reversePauseFlag();
            }
            else {
                gameSounds.playSoundEffect("LastLevel");
                if(gameBoard.isNotPaused())
                    reversePauseFlag();
                new ScoreBoard(owner,brickBreaker,0,gameBoard.getScoreAndTime(),choice);
                gameBoard.setMessageFlag(3);
                gameBoard.setEndFlag(true);
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

    public void resetLevelData(){
        wallReset();
        ballReset();
        resetLevelScoreAndTime();
        resetTotalScoreAndTime();
        powerUp.setCollected(false);
        powerUp.setSpawned(false);
        gameBoard.setPowerUpSpawns(0);
    }

    public void nextLevel(boolean trueProgression){
        if(gameBoard.getLevel()==5)
            return;

        if(gameBoard.getLevel()!=0)
            wallReset();

        if(!trueProgression)
            resetLevelScoreAndTime();

        gameBoard.setLevel(gameBoard.getLevel()+1);
        gameBoard.setBrickCount(bricks[gameBoard.getLevel()-1].length);
        resetLevelData();
        gameSounds.setBgm("BGM"+gameBoard.getLevel());
    }

    public void previousLevel(){
        if(gameBoard.getLevel()==1)
            return;

        resetLevelScoreAndTime();
        wallReset();
        gameBoard.setLevel(gameBoard.getLevel()-1);
        gameBoard.setBrickCount(bricks[gameBoard.getLevel()-1].length);
        resetLevelData();
        gameSounds.setBgm("BGM"+gameBoard.getLevel());
    }

    public void resetTotalScoreAndTime(){
        gameBoard.setScore(0,returnPreviousLevelsScore());
        gameBoard.setTime(0,returnPreviousLevelsTime());
    }

    public void resetLevelScoreAndTime(){
        gameBoard.setScore(gameBoard.getLevel(),0);
        gameBoard.setTime(gameBoard.getLevel(),0);
    }

    public void ballReset(){ //when ball is lost

        if(choice[gameBoard.getLevel()-1][9]==0) {
            gameBoard.setBallStartPoint(new Point(area.width/2,area.height-20));
            gameBoard.setPlayerStartPoint(new Point(area.width/2,area.height-20));
        }
        else if (choice[gameBoard.getLevel()-1][9]==1){
            gameBoard.setBallStartPoint(new Point(area.width/2,20));
            gameBoard.setPlayerStartPoint(new Point(area.width/2,10));
        }

        ballMoveTo(gameBoard.getBallStartPoint());
        playerMoveTo(gameBoard.getPlayerStartPoint());

        setBallSpeed();
        gameBoard.setBallLost(false);
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

        if(gameBoard.getPowerUpSpawns() < (gameBoard.getTime(gameBoard.getLevel())/60 + 1) && !powerUp.isSpawned() && !powerUp.isCollected()) {
            x = random.nextInt(401) + 100;
            if (choice[gameBoard.getLevel() - 1][9] == 0) {
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
        for(Brick b : bricks[gameBoard.getLevel()-1])
            repair(b); //reset brick to full strength
        gameBoard.setBrickCount(bricks[gameBoard.getLevel()-1].length);
        resetBallCount();
    }

    public void resetBallCount(){
        if(choice[gameBoard.getLevel()-1][8]!=0) {
            gameBoard.setBallCount(choice[gameBoard.getLevel()-1][8]);
        }
        else{
            gameBoard.setBallCount(3);
        }
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
            gameBoard.setBrickCount(gameBoard.getBrickCount()-1);
        }
        else if(impactBorder()) { //if ball impacts border
            gameSounds.playSoundEffect("Bounce");
            reverseX(); //reverse X-direction
        }
        else if(choice[gameBoard.getLevel()-1][9]==0){
            if(ball.getCenter().getY() < 0){ //if ball hits top border
                gameSounds.playSoundEffect("Bounce");
                reverseY(); //reverse Y-direction
            }
            else if(ball.getCenter().getY() > area.height){ //if ball hits bottom border
                gameBoard.setBallCount(gameBoard.getBallCount()-1);
                gameBoard.setBallLost(true);
            }
        }
        else if(choice[gameBoard.getLevel()-1][9]==1){
            if(ball.getCenter().getY() < 0){ //if ball hits top border
                gameBoard.setBallCount(gameBoard.getBallCount()-1);
                gameBoard.setBallLost(true);
            }
            else if(ball.getCenter().getY() > area.height){ //if ball hits bottom border
                gameSounds.playSoundEffect("Bounce");
                reverseY(); //reverse Y-direction
            }
        }
    }

    private boolean impactWall(boolean collected){ //method to check impact with wall
        for(Brick b : bricks[gameBoard.getLevel()-1]){
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
        return ((p.getX() < 0) ||(p.getX() > area.width));
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

    private void generateGameMessages(){

        String message = null;
        if(gameBoard.getMessageFlag()==0)
            message = String.format("Bricks: %d  Balls %d",gameBoard.getBrickCount(),gameBoard.getBallCount());
        else if(gameBoard.getMessageFlag()==1)
            message = "Game over"; //show game over message
        else if(gameBoard.getMessageFlag()==2)
            message = "Go to Next Level";
        else if(gameBoard.getMessageFlag()==3)
            message = "ALL WALLS DESTROYED";
        else if(gameBoard.getMessageFlag()==4)
            message = "Restarting Game...";
        else if(gameBoard.getMessageFlag()==5)
            message = "Focus Lost";

        gameBoard.setGameMessages(0,message);

        String totalScore = String.format("Total Score %d", gameBoard.getScoreAndTime()[0][0]);
        gameBoard.setGameMessages(1,totalScore);
        String levelScore = String.format("Level %d Score %d", gameBoard.getLevel(), gameBoard.getScoreAndTime()[gameBoard.getLevel()][0]);
        gameBoard.setGameMessages(2,levelScore);

        int systemClock = gameBoard.getScoreAndTime()[0][1];
        int totalMinutes = systemClock/60;
        int totalSeconds = systemClock%60;

        int levelClock = gameBoard.getScoreAndTime()[gameBoard.getLevel()][1];
        int levelMinutes = levelClock/60;
        int levelSeconds = levelClock%60;

        String totalTime = String.format("Total Time %02d:%02d", totalMinutes, totalSeconds);
        gameBoard.setGameMessages(3,totalTime);
        String levelTime = String.format("Level %d Time %02d:%02d", gameBoard.getLevel(), levelMinutes, levelSeconds);
        gameBoard.setGameMessages(4,levelTime);

        String godMode;
        if(gameBoard.getPowerUp().isCollected())
            godMode = String.format("God Mode Activated %d", gameBoard.getGodModeTimeLeft());
        else
            godMode = String.format("God Mode Orbs Left %d", (gameBoard.getScoreAndTime()[gameBoard.getLevel()][1]/60 + 1) - gameBoard.getPowerUpSpawns());

        gameBoard.setGameMessages(5,godMode);
    }
}
