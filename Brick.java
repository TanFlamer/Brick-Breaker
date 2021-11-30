package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Abstract public class Brick is used as a base to create bricks for the game and to define all the different
 * basic methods of the ball. The actual method to make the bricks will be defined in the subclass.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
abstract public class Brick  {

    /**
     * The depth of the crack.
     */
    public static final int DEF_CRACK_DEPTH = 1;
    /**
     * The steps of the crack.
     */
    public static final int DEF_STEPS = 35;

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
     * The width of the game screen.
     */
    private static final int DEF_WIDTH = 600;

    /**
     * Public class crack is used to define the behaviour of the crack in the crackable bricks.
     */
    public class Crack{

        /**
         * The section of cracks.
         */
        private static final int CRACK_SECTIONS = 3;
        /**
         * The probability for a crack to jump.
         */
        private static final double JUMP_PROBABILITY = 0.7;

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
         * The path traced by the crack.
         */
        private GeneralPath crack; //crack trajectory

        /**
         * The depth of the crack.
         */
        private int crackDepth;
        /**
         * The steps of the crack.
         */
        private int steps;


        /**
         * This constructor gets the values for the depth and the steps of the crack and creates a new path for the
         * crack to travel.
         * @param crackDepth The depth of the crack.
         * @param steps The steps of the crack.
         */
        public Crack(int crackDepth, int steps){ //make crack

            crack = new GeneralPath();
            this.crackDepth = crackDepth;
            this.steps = steps;
        }

        /**
         * This method returns the new crack drawn.
         * @return The new crack drawn is returned.
         */
        public GeneralPath draw(){

            return crack;
        }

        /**
         * This method removes the crack from the brick when brick is reset.
         */
        public void reset(){
            crack.reset(); //remove crack
        }

        /**
         * This method creates a crack from the impact point in the given direction.
         * @param point The impact point of the ball and brick.
         * @param direction The direction of travel of the crack in the brick.
         */
        protected void makeCrack(Point2D point, int direction){ //get point of impact and crack direction
            Rectangle bounds = Brick.this.brickFace.getBounds(); //get brick bounds

            Point impact = new Point((int)point.getX(),(int)point.getY()); //get point of impact
            Point start = new Point(); //start point of crack
            Point end = new Point(); //end point of crack

            switch(direction){ //get direction of crack
                case LEFT:
                    start.setLocation(bounds.x + bounds.width, bounds.y); //top of right border
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height); //bottom of right border
                    Point tmp = makeRandomPoint(start,end,VERTICAL); //random point of right border
                    makeCrack(impact,tmp); //make crack from point of impact to random point of right border

                    break;
                case RIGHT:
                    start.setLocation(bounds.getLocation()); //top of left border
                    end.setLocation(bounds.x, bounds.y + bounds.height); //bottom of left border
                    tmp = makeRandomPoint(start,end,VERTICAL); //random point of left border
                    makeCrack(impact,tmp); //make crack from point of impact to random point of left border

                    break;
                case UP:
                    start.setLocation(bounds.x, bounds.y + bounds.height); //left of bottom border
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height); //right of bottom border
                    tmp = makeRandomPoint(start,end,HORIZONTAL); //random point of bottom border
                    makeCrack(impact,tmp); //make crack from point of impact to random point of bottom border
                    break;
                case DOWN:
                    start.setLocation(bounds.getLocation()); //left of top border
                    end.setLocation(bounds.x + bounds.width, bounds.y); //right of top border
                    tmp = makeRandomPoint(start,end,HORIZONTAL); //random point of top border
                    makeCrack(impact,tmp); //make crack from point of impact to random point of top border
                    break;
            }
        }

        /**
         * This method creates a crack from the impact point to the randomly selected end point.
         * @param start The impact point between the ball and brick.
         * @param end A randomly selected point on the other side of the brick from the point of impact.
         */
        protected void makeCrack(Point start, Point end){ //make crack

            GeneralPath path = new GeneralPath(); //path of crack

            path.moveTo(start.x,start.y); //start of crack

            double w = (end.x - start.x) / (double)steps; //get width of each step
            double h = (end.y - start.y) / (double)steps; //get height of each step

            int bound = crackDepth; //get crack depth
            int jump  = bound * 5; //5 times crack depth

            double x,y;

            for(int i = 1; i < steps;i++){

                x = (i * w) + start.x; //get next X-coordinate of next step
                y = (i * h) + start.y + randomInBounds(bound); //get next Y-coordinate of next step (random)

                if(inMiddle(i,CRACK_SECTIONS,steps)) //if i is between high and low points
                    y += jumps(jump,JUMP_PROBABILITY); //add random number between -5*bound to 5*bound with 1 - JUMP_PROBABILITY

                path.lineTo(x,y); //draw crack to (x,y)
            }
            path.lineTo(end.x,end.y); //draw crack to final position
            crack.append(path,true); //connect crack to brick
        }

        /**
         * This method chooses a random point for the next step of the crack to travel to.
         * @param bound The depth of the crack.
         * @return A random point for the next step of the crack to travel to is returned.
         */
        private int randomInBounds(int bound){ //get random addition to Y-coordinate
            int n = (bound * 2) + 1;
            return rnd.nextInt(n) - bound; //return random number between -bound to bound
        }

        /**
         * This method checks to see if Integer i is between 2 points, high and low. The function seem to always
         * return false?
         * @param i The integer to be compared to the 2 points.
         * @param steps The section of cracks which is 3.
         * @param divisions The number of steps of the crack.
         * @return This method returns a boolean to signify if Integer i is between the 2 points. The method seems
         *         to always return false?
         */
        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions); //get low point
            int up = low * (divisions - 1); //get high point

            return  (i > low) && (i < up); //check if between both points
        }

        /**
         * This method checks to see if a jump occurs in the crack with a certain probability.
         * @param bound The depth of the crack.
         * @param probability The probability that a jump in the crack occurs.
         * @return A random increment for the next step of the crack to travel to is returned if the event occurs or
         *         a 0 is returned is the event fails.
         */
        private int jumps(int bound,double probability){

            if(rnd.nextDouble() > probability) //if random probability greater than JUMP_PROBABILITY
                return randomInBounds(bound); //return random number between -5*bound to 5*bound
            return  0; //else return 0
        }

        /**
         * This method selects a random point between 2 points in a given direction.
         * @param from The first point that makes up a line with the second point.
         * @param to The second point that makes up a line with the first point.
         * @param direction The direction of the line.
         * @return This method returns a random point on the other side of the brick from the point of impact.
         */
        private Point makeRandomPoint(Point from,Point to, int direction){ //select random point between 2 given points

            Point out = new Point(); //new point
            int pos; //new coordinate

            switch(direction){
                case HORIZONTAL:
                    pos = rnd.nextInt(to.x - from.x) + from.x; //get random X-coordinate between from.x to to.x
                    out.setLocation(pos,to.y); //set out to new point
                    break;
                case VERTICAL:
                    pos = rnd.nextInt(to.y - from.y) + from.y; //get random Y-coordinate between from.y to to.y
                    out.setLocation(to.x,pos); //set out to new point
                    break;
            }
            return out; //return new point
        }
    }

    /**
     * The randomizer to choose the random end point of the crack.
     */
    private static Random rnd;

    /**
     * The name of the brick created.
     */
    private String name;
    /**
     * The brick face of the bricks used to check for impacts with the ball.
     */
    Shape brickFace;

    /**
     * The border colour of the brick.
     */
    private Color border;
    /**
     * The inner colour of the brick.
     */
    private Color inner;

    /**
     * The number of hits a brick can take before breaking.
     */
    private int fullStrength;
    /**
     * The current number of hits the brick can still take before breaking.
     */
    private int strength;

    /**
     * The score of the brick awarded to the player when broken. It is calculated with the formula
     * score = number of bricks in a row * score multiplier
     */
    private int score;

    /**
     * The boolean to check if the brick is broken.
     */
    private boolean broken;

    /**
     * This constructor is used to create a new brick with the given name, position, dimensions, border and inner colour,
     * strength and score multiplier. The score of the brick is also calculated here.
     *
     * @param name The name of the new brick.
     * @param pos The position of the new brick.
     * @param size The dimensions of the new brick.
     * @param border The border colour of the new brick.
     * @param inner The inner colour of the new brick.
     * @param strength The strength of the new brick.
     * @param multiplier The score multiplier of the new brick.
     */
    public Brick(String name, Point pos,Dimension size,Color border,Color inner,int strength,int multiplier){ //define basic brick to be overridden
        rnd = new Random();
        broken = false; //condition of brick
        this.name = name; //name of brick
        brickFace = makeBrickFace(pos,size); //make brick at given point with given size
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength; //strength of brick

        this.score = (int) (DEF_WIDTH / size.getWidth()) * multiplier;
    }

    /**
     * This abstract method is used to create bricks with the given position and given dimensions. This abstract
     * method is to be overridden in the subclass.
     *
     * @param pos This is the top-left position of the brick.
     * @param size This is the dimensions of the brick.
     * @return This method returns a brick with the given position and given dimensions.
     */
    protected abstract Shape makeBrickFace(Point pos,Dimension size); //override in subclass

    /**
     * This method is used to check if an impact occurs with a brick. If the brick is already broken then
     * no impact occurs. If the brick is unbroken, impact occurs and the condition of the brick is returned.
     * This method is overridden in the crackable bricks and uses the point of impact and crack direction to
     * update their bricks.
     *
     * @param point The point of impact of the ball and the brick.
     * @param dir The direction of travel of the crack.
     * @return This method returns a boolean to signify the condition of the brick.
     */
    public boolean setImpact(Point2D point , int dir){ //get point of impact and impact direction
        if(broken) //if already broken then no impact
            return false;
        impact(); //else signal impact
        return broken; //return flag from impact()
    }

    /**
     * This method returns the brick face of the brick.
     * @return The brick face of the brick is returned.
     */
    public abstract Shape getBrick(); //override in subclass

    /**
     * This method returns the border colour of the brick.
     * @return The border colour of the brick is returned.
     */
    public Color getBorderColor(){ //get border colour of brick
        return  border;
    }

    /**
     * This method returns the inner colour of the brick.
     * @return The inner colour of the brick is returned.
     */
    public Color getInnerColor(){ //get inner colour of brick
        return inner;
    }

    /**
     * This method checks to see if impact occurs between the ball and brick. If the brick is already broken, then
     * no impact occurs. If the brick is scanned to see if any points of the ball is contained within the brick face.
     * If there is, then collision has occurred and the direction of collision is returned.
     *
     * @param b This is the ball that is checked against the bricks.
     * @return This method returns 0 if no collision occurs, else it returns a code to signify the direction
     *         of collision.
     */
    public final int findImpact(Ball b){ //get direction of impact
        if(broken) //if already broken return 0
            return 0;
        int out  = 0;
        if(brickFace.contains(b.right)) //else get direction of impact if brick face detects ball directional face
            out = LEFT_IMPACT;
        else if(brickFace.contains(b.left))
            out = RIGHT_IMPACT;
        else if(brickFace.contains(b.up))
            out = DOWN_IMPACT;
        else if(brickFace.contains(b.down))
            out = UP_IMPACT;
        return out; //return direction of impact
    }

    /**
     * This method returns the condition of the brick.
     * @return The condition of the brick is returned.
     */
    public final boolean isBroken(){ //signal condition of brick
        return broken;
    }

    /**
     * This method is used to repair the bricks by setting their broken flag to false and resetting their strength.
     * For crackable bricks, this method is overridden and the crack is also removed.
     */
    public void repair() { //repair brick
        broken = false; //reset broken flag
        strength = fullStrength; //set strength to full strength
    }

    /**
     * This method reduces the strength of the brick as successful collision has occurred. Then, the condition of
     * the brick is updated.
     */
    public void impact(){ //if impact occurs
        strength--; //reduce brick strength
        broken = (strength == 0); //if strength = 0, signal brick broken
    }

    /**
     * This method returns the score of the broken brick.
     * @return The score of the broken brick is returned.
     */
    public int getScore(){
        return score;
    }
}





