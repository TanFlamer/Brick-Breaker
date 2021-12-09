package Main;

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
     * @param gameSounds GameSounds to add BGM and sound effects to the game.
     * @param area Dimensions of game screen to set game boundaries.
     */
    public GameBoardController(JFrame owner,GameBoard gameBoard,GameSounds gameSounds,Dimension area) {
        this.gameBoard = gameBoard;
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

    /**
     * This method controls and updates the entire game. First, a power up is spawned if available. Then, the player
     * and ball are moved. After that, any impacts between the player, ball and bricks are checked and processed.
     * Then, the time and score are calculated and the game messages are updated. Finally, the game checks to see
     * if the game has done anything special like losing the ball or destroying all the walls and responds accordingly.
     * The method is not accessed when game is paused or has ended.
     * @throws FileNotFoundException This method throws FileNotFoundException when audio file is not found.
     */
    public void update() throws FileNotFoundException {
        if(gameBoard.isNotPaused()&&!gameBoard.isEnded()) {
            powerUpRandomSpawn();
            movePlayer();
            moveBall();
            findImpacts(powerUp.isCollected());
            calculateScoreAndTime();
            gameChecks();
            generateGameMessages();
        }
        else {
            generateGameMessages();
        }
    }

    /**
     * This method defines the movement of the player. The new position of the player is calculated by adding the move
     * amount of the player to the old position of the player. The new position of the player is then set as the old
     * position. The player is then moved to this new position. By calling this method in quick succession, movement
     * can be simulated.
     */
    public void movePlayer(){ //move player
        double x = player.getMidPoint().getX() + player.getMoveAmount(); //get player location after move
        double y = player.getMidPoint().getY() + player.getVerticalMoveAmount(); //get player location after move
        if(x < player.getMin() || x > player.getMax() || y < player.getTop() || y > player.getBottom()) //if X-coordinate exceeds min or max value
            return; //stop player from moving
        player.setMidPoint(new Point((int)x,(int)y)); //set new player point
        player.getPlayerFace().setLocation(player.getMidPoint().x - (int)player.getPlayerFace().getWidth()/2,player.getMidPoint().y); //set new player location
    }

    /**
     * This method defines the movement of the ball. The new position of the ball is calculated by adding the speed of
     * the ball to the old position of the ball. The ball is then moved to this new position. By calling this method
     * in quick succession, movement can be simulated.
     */
    public void moveBall(){ //move ball according to speed
        RectangularShape tmp = (RectangularShape) ball.getBallFace();
        ball.setCenter(new Point((int)(ball.getCenter().getX() + ball.getSpeedX()),(int)(ball.getCenter().getY() + ball.getSpeedY()))); //set ball at new location according to speed

        double w = tmp.getWidth(); //get ball width
        double h = tmp.getHeight(); //get ball height

        tmp.setFrame((ball.getCenter().getX() -(w / 2)),(ball.getCenter().getY() - (h / 2)),w,h); //make rectangle shape
        ball.setBallFace(tmp);
    }

    /**
     * This method moves player to the left by the default move amount.
     */
    public void moveLeft(){ //move player left by default amount
        player.setMoveAmount(-DEF_MOVE_AMOUNT);
    }

    /**
     * This method moves player to the right by the default move amount.
     */
    public void moveRight(){ //move player right by default amount
        player.setMoveAmount(DEF_MOVE_AMOUNT);
    }

    /**
     * This method moves player up by the default move amount.
     */
    public void moveUp(){ //move player right by default amount
        player.setVerticalMoveAmount(-DEF_MOVE_AMOUNT);
    }

    /**
     * This method moves player down by the default move amount.
     */
    public void moveDown(){ //move player right by default amount
        player.setVerticalMoveAmount(DEF_MOVE_AMOUNT);
    }

    /**
     * This method stops the player movement by setting move amount to 0.
     */
    public void stop(){ //move player right by default amount
        player.setMoveAmount(0);
        player.setVerticalMoveAmount(0);
    }

    /**
     * This method reverses the game pause flag. If the game is paused, the game resumes and vice versa. If the game
     * resumes, the start time is updated, message flag updated and BGM starts. If it is paused, BGM stops playing.
     */
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

    /**
     * This method sets the ball speed to a random value. For horizontal speed the range is -2 to 2. For vertical speed,
     * if the player is at the bottom, the range is -1 to -2. If the player is at the top, the range is 1 to 2. The
     * ball can never have a speed of 0 in either direction.
     */
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

    /**
     * This method reverses the horizontal speed of the ball to simulate collision and deflection.
     */
    public void reverseX(){
        ball.setSpeedX(-ball.getSpeedX());
    }

    /**
     * This method reverses the vertical speed of the ball to simulate collision and deflection.
     */
    public void reverseY(){
        ball.setSpeedY(-ball.getSpeedY());
    }

    /**
     * This method calculates the current level score and time and the total level score and time. The score is counted
     * based on the number of bricks broken. The time is incremented by the second by using a start time. When current
     * time exceeds start time, 1 second is added and the start time is reset. This allows the player to pause the game
     * as the start time is also reset when game is resumed.
     */
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

    /**
     * This method returns the total score of the previous levels.
     * @return The total score of the previous levels is returned.
     */
    public int returnPreviousLevelsScore(){
        int total = 0;
        for(int i = gameBoard.getLevel();i > 1; i--){
            total += gameBoard.getScore(i-1);
        }
        return total;
    }

    /**
     * This method returns the total tie of the previous levels.
     * @return The total time of the previous levels is returned.
     */
    public int returnPreviousLevelsTime(){
        int total = 0;
        for(int i = gameBoard.getLevel();i > 1; i--){
            total += gameBoard.getTime(i-1);
        }
        return total;
    }

    /**
     * This method checks the game to see if any special conditions are fulfilled such as losing the ball, breaking
     * all the bricks or collecting the power up and processes the game accordingly.
     * @throws FileNotFoundException This method throws FileNotFoundException when audio file is not found.
     */
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
            new ScoreBoard(owner, gameBoard.getLevel(),gameBoard.getScoreAndTime(),choice);

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
                new ScoreBoard(owner, 0,gameBoard.getScoreAndTime(),choice);
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

    /**
     * This method resets the data of the whole level. First, the bricks are all repaired and the ball and player are
     * moved back to their initial position. The level and total time and scores are then reset. Finally, the power up
     * spawns are also reset.
     */
    public void resetLevelData(){
        wallReset();
        ballReset();
        resetLevelScoreAndTime();
        resetTotalScoreAndTime();
        powerUp.setCollected(false);
        powerUp.setSpawned(false);
        gameBoard.setPowerUpSpawns(0);
    }

    /**
     * This method loads the next level into the game. The boolean parameter checks to see if the player has completed
     * the last level. If the player has not, the scores and time of the last level are reset. If the player has, the
     * scores and time are untouched. The method repairs all broken blocks of the current level before loading in the
     * bricks of the new level. Player and ball are moved to their default position and brick count and ball count are
     * reset. The player cannot load past the 5th level.
     * @param trueProgression This parameter checks to see if the player has completed the last level.
     */
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

    /**
     * This method loads the previous level into the game. The previous level data is reset with this method. The
     * method repairs all broken blocks of the current level before loading in the bricks of the previous level. Player
     * and ball are moved to their default position and brick count and ball count are reset. The player cannot load
     * before the 1st level.
     */
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

    /**
     * This method resets the total level score and time by overriding it with the previous level scores and times.
     */
    public void resetTotalScoreAndTime(){
        gameBoard.setScore(0,returnPreviousLevelsScore());
        gameBoard.setTime(0,returnPreviousLevelsTime());
    }

    /**
     * This method resets the current level score and time.
     */
    public void resetLevelScoreAndTime(){
        gameBoard.setScore(gameBoard.getLevel(),0);
        gameBoard.setTime(gameBoard.getLevel(),0);
    }

    /**
     * This method is used to reset the player and the ball to the default starting position. It is triggered when
     * the current ball has left the page border. This can occur with the bottom or top border depending on the player
     * orientation. The ball is reset with a new horizontal and vertical speed. The ball lost flag is then set to false.
     */
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

    /**
     * This method is used to move the ball to a given point. The given point is set as the new midpoint of the ball
     * and the ball is then moved to this point.
     * @param p This is the new center point of the ball.
     */
    public void ballMoveTo(Point p){ //teleport ball to point p
        ball.setCenter(p);

        RectangularShape tmp = (RectangularShape) ball.getBallFace();
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((ball.getCenter().getX() - (w / 2)),(ball.getCenter().getY() - (h / 2)),w,h);
        ball.setBallFace(tmp);
    }

    /**
     * This method is used to move the player to a given point. The given point is set as the new midpoint of the
     * player and the player is then moved to this point.
     * @param p This is the new center point of the player.
     */
    public void playerMoveTo(Point p){ //teleport player to point p
        player.setMidPoint(p);

        Rectangle tmp = player.getPlayerFace();
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((player.getMidPoint().getX() - (w / 2)),player.getMidPoint().getY(),w,h);
        player.setPlayerFace(tmp);
    }

    /**
     * This method is used to move the power up to a given point. The given point is set as the new midpoint of the
     * power up and the power up is then moved to this point.
     * @param p This is the new center point of the power up.
     */
    public void powerUpMoveTo(Point p){
        powerUp.setMidPoint(p);

        RectangularShape tmp = (RectangularShape) powerUp.getPowerUp();

        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((powerUp.getMidPoint().getX() - w/2),(powerUp.getMidPoint().getY() - h/2),w,h);
        powerUp.setPowerUp(tmp);
    }

    /**
     * This method is used to spawn a power up at a random point of a line. The player accumulates a power up spawn
     * every minute. The next power up will spawn after the last one is collected and used and if the player has a spare
     * power up spawn.
     */
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

    /**
     * This method checks to see if the power up is collected by checking to see if any of the 5 points of the ball are
     * inside the power up.
     * @return This method returns a boolean to signal if the power up is collected.
     */
    public boolean powerUpCollected(){
        return (powerUp.getPowerUp().contains(ball.getCenter())||powerUp.getPowerUp().contains(ball.getUp())||powerUp.getPowerUp().contains(ball.getDown())||powerUp.getPowerUp().contains(ball.getLeft())||powerUp.getPowerUp().contains(ball.getRight()));
    }

    /**
     * This method is used to reset the condition of a level. All the bricks are repaired by removing the cracks
     * and setting their broken flag to false. The brick count and ball count is also reset.
     */
    public void wallReset(){
        for(Brick b : bricks[gameBoard.getLevel()-1])
            repair(b); //reset brick to full strength
        gameBoard.setBrickCount(bricks[gameBoard.getLevel()-1].length);
        resetBallCount();
    }

    /**
     * This method is used to reset number of balls the player has.
     */
    public void resetBallCount(){
        if(choice[gameBoard.getLevel()-1][8]!=0) {
            gameBoard.setBallCount(choice[gameBoard.getLevel()-1][8]);
        }
        else{
            gameBoard.setBallCount(3);
        }
    }

    /**
     * This method is used to repair the bricks by setting their broken flag to false and resetting their strength. The
     * crack on the bricks are also removed if they are present.
     * @param b The brick to be repaired.
     */
    public void repair(Brick b){ //repair brick
        b.setBroken(false);
        b.setStrength(b.getFullStrength());
        if(b.isCrackable()) {
            b.getCrack().reset(); //remove crack
            b.setBrickFace(b.getBrickFaceNew());
        }
    }

    /**
     * This method checks for any impact of the ball with the player. If player face contains the bottom side of
     * the ball inside and orientation of the player is at the bottom, then impact has occurred. If player face
     * contains the top side of the ball inside and orientation of the player is at the top, then impact
     * has occurred.
     *
     * @param b The ball which is checked for impact with the player.
     * @return A boolean to signify if impact between the ball and player has occurred is returned.
     */
    public boolean ballPlayerImpact(Ball b){ //scan to see if player contains bottom side of ball
        return player.getPlayerFace().contains(b.getCenter()) && (player.getPlayerFace().contains(b.getDown())||player.getPlayerFace().contains(b.getUp()));
    }

    /**
     * This method is used to check for any impacts of the ball and define all the behaviours when impact with
     * the ball occurs. If impact occurs with the player, vertical direction of ball is reversed. If impact occurs
     * with a brick, the method below will compute the outcome. If impact occurs with the left and right page borders,
     * horizontal direction of ball is reversed. For a game with bottom player orientation, if impact occurs with the
     * top page border, vertical direction of ball is reversed. For a game with top player orientation, if impact occurs
     * with the bottom page border, vertical direction of ball is reversed. Finally, if ball leaves the bottom page
     * border on bottom player orientation or leaves the top page border on top player orientation, ball is lost,
     * ball count decreases and player and ball position are reset.
     *
     * @param collected This boolean is used to check if the power up has been collected. If it is, then deflection
     *                  of the ball is disabled and bricks are destroyed on touch with the ball.
     */
    public void findImpacts(boolean collected){ //impact method
        if(ballPlayerImpact(ball)){ //if player hits ball
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

            if(ball.getCenter().getY() < 0||(ball.getCenter().getY() > area.height && collected)){ //if ball hits top border
                gameSounds.playSoundEffect("Bounce");
                reverseY(); //reverse Y-direction
            }
            else if(ball.getCenter().getY() > area.height){ //if ball hits bottom border
                gameBoard.setBallCount(gameBoard.getBallCount()-1);
                gameBoard.setBallLost(true);
            }
        }
        else if(choice[gameBoard.getLevel()-1][9]==1){

            if(ball.getCenter().getY() > area.height||(ball.getCenter().getY() < 0 && collected)){ //if ball hits top border
                gameSounds.playSoundEffect("Bounce");
                reverseY(); //reverse Y-direction
            }
            else if(ball.getCenter().getY() < 0){ //if ball hits bottom border
                gameBoard.setBallCount(gameBoard.getBallCount()-1);
                gameBoard.setBallLost(true);
            }
        }
    }

    /**
     * This method is used to check for any impacts of the ball and the bricks. If impact occurs with a brick, it
     * checks if the brick is already broken. If brick is already broken, no impact occurs and the ball just passes
     * through. If brick is not broken, impact occurs and brick count will decrease if the brick is broken. The
     * direction of impact between the ball and brick are taken and the direction of motion of the ball is reversed.
     * If power up has been collected then ball deflection is disabled and bricks are destroyed instantly on collision
     * with the ball.
     *
     * @param collected This boolean is used to check if the power up has been collected. If it is, then deflection
     *                  of the ball is disabled and bricks are destroyed on touch with the ball.
     * @return This method returns a boolean to signify if the brick impacted by the ball is unbroken at the start
     *         of the collision but is broken by the impact with the ball. This is so that brick count can be deceased.
     */
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

    /**
     * This method is used to check for any impacts of the ball and the left and right page border.
     * @return This method returns a boolean to signify if the ball is leaving the left or right page border.
     */
    private boolean impactBorder(){ //if ball impacts left or right border
        Point2D p = ball.getCenter();
        return ((p.getX() < 0) ||(p.getX() > area.width));
    }

    /**
     * This method checks to see if impact occurs between the ball and brick. If the brick is already broken, then
     * no impact occurs. If the brick is scanned to see if any points of the ball is contained within the brick face.
     * If there is, then collision has occurred and the direction of collision is returned.
     *
     * @param b This is the ball that is checked against the bricks.
     * @param brick This is the brick that is checked.
     * @return This method returns 0 if no collision occurs, else it returns a code to signify the direction
     *         of collision.
     */
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

    /**
     * This method is used to check if an impact occurs with a brick. If the brick is already broken then
     * no impact occurs. If the brick is unbroken, impact occurs and the condition of the brick is returned.
     * If the brick is crackable, the point of impact and crack direction are used to generate a crack and
     * update the brick.
     *
     * @param point The point of impact of the ball and the brick.
     * @param dir The direction of travel of the crack.
     * @param b This is the brick that is checked.
     * @return This method returns a boolean to signify the condition of the brick.
     */
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

    /**
     * This method causes an impact to the brick if the random probability is less than the damage probability. If
     * impact occurs, then brick strength is reduced and brick broken condition is updated. There are different
     * sound effects for successful and unsuccessful impacts.
     * @param b This is the brick that is checked.
     * @return This method returns a boolean to signal if impact is successful.
     */
    private boolean impact(Brick b){
        if(random.nextDouble() < b.getBreakProbability()){ //if random probability less than DAMAGE_PROBABILITY
            gameSounds.playSoundEffect("Damage");
            b.setStrength(b.getStrength()-1); //reduce brick strength
            b.setBroken(b.getStrength() == 0); //if strength = 0, signal brick broken
            return true;
        }
        gameSounds.playSoundEffect("Deflect");
        return false;
    }

    /**
     * This method creates a crack from the impact point in the given direction.
     * @param point The impact point of the ball and brick.
     * @param direction The direction of travel of the crack in the brick.
     * @param b This is the brick to be cracked.
     */
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

    /**
     * This method creates a crack from the impact point to the randomly selected end point.
     * @param start The impact point between the ball and brick.
     * @param end A randomly selected point on the other side of the brick from the point of impact.
     * @param b This is the brick to be cracked.
     */
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

    /**
     * This method appends the new crack to the brick and updates the brick face.
     * @param b This is the brick to be cracked.
     */
    private void updateBrick(Brick b){
        GeneralPath gp = b.getCrack(); //draw crack
        gp.append(b.getBrickFace(),false); //append crack to brick
        b.setBrickFace(gp);
    }

    /**
     * This method selects a random point between 2 points in a given direction to generate the crack.
     * @param from The first point that makes up a line with the second point.
     * @param to The second point that makes up a line with the first point.
     * @param direction The direction of the line.
     * @return This method returns a random point on the other side of the brick from the point of impact.
     */
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

    /**
     * This method chooses a random point for the next step of the crack to travel to.
     * @return A random point for the next step of the crack to travel to is returned.
     */
    private int randomInBounds(){ //get random addition to Y-coordinate
        int n = (DEF_CRACK_DEPTH * 2) + 1;
        return random.nextInt(n) - DEF_CRACK_DEPTH; //return random number between -bound to bound
    }

    /**
     * This method generates the game messages depending on the message flags and game data. The messages are then
     * rendered into the game.
     */
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
