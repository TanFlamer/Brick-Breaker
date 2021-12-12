package Main.MVC;

import Main.Models.Ball;
import Main.Models.Brick;
import Main.Models.GodModePowerUp;
import Main.Models.Player;
import Main.Others.LevelGeneration;

import java.awt.*;

/**
 * Public class GameBoard is the Model of the MVC design pattern and is responsible for holding all the entity
 * information of the game such as bricks, ball and player. All state information about the entities such as position,
 * game information such as score and time and game flags are saved here. The controller manipulates the information
 * here while the renderer renders the information here. The information is read and manipulated using setters and
 * getters.
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
        player = new Player(new Point(area.width/2,area.height-20),150,10,area);
        ball = new Ball(new Point(area.width/2,area.height-20),BALL_DIAMETER);
        powerUp = new GodModePowerUp(new Point(area.width/2,area.height/2),POWER_UP_DIAMETER);
        bricks = (new LevelGeneration(choice,area,LEVELS_COUNT)).makeCustomLevels(new Rectangle(0,0,area.width,area.height));
        scoreAndTime = new int[LEVELS_COUNT+1][2];
        gameMessages = new String[6];
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
