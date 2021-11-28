package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by filippo on 04/09/16.
 *
 */
abstract public class Brick  {

    public static final int MIN_CRACK = 1;
    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;


    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    private static final int DEF_WIDTH = 600;

    public class Crack{

        private static final int CRACK_SECTIONS = 3;
        private static final double JUMP_PROBABILITY = 0.7;

        public static final int LEFT = 10;
        public static final int RIGHT = 20;
        public static final int UP = 30;
        public static final int DOWN = 40;
        public static final int VERTICAL = 100;
        public static final int HORIZONTAL = 200;

        private GeneralPath crack; //crack trajectory

        private int crackDepth;
        private int steps;


        public Crack(int crackDepth, int steps){ //make crack

            crack = new GeneralPath();
            this.crackDepth = crackDepth;
            this.steps = steps;
        }

        public GeneralPath draw(){

            return crack;
        }

        public void reset(){
            crack.reset(); //remove crack
        }

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
                    y += jumps(jump,JUMP_PROBABILITY); //add random random number between -bound to bound+1 with 1 - JUMP_PROBABILITY

                path.lineTo(x,y); //draw crack to (x,y)

            }

            path.lineTo(end.x,end.y); //draw crack to final position
            crack.append(path,true); //connect crack to brick
        }

        private int randomInBounds(int bound){ //get random addition to Y-coordinate
            int n = (bound * 2) + 1;
            return rnd.nextInt(n) - bound; //return random number between -bound to bound+1
        }

        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions); //get low point
            int up = low * (divisions - 1); //get high point

            return  (i > low) && (i < up); //check if between both points
        }

        private int jumps(int bound,double probability){

            if(rnd.nextDouble() > probability) //if random probability greater than JUMP_PROBABILITY
                return randomInBounds(bound); //return random number between -bound to bound+1
            return  0; //else return 0

        }

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

    private static Random rnd;

    private String name;
    Shape brickFace;

    private Color border;
    private Color inner;

    private int fullStrength;
    private int strength;

    private int score;

    private boolean broken;

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

    protected abstract Shape makeBrickFace(Point pos,Dimension size); //override in subclass

    public  boolean setImpact(Point2D point , int dir){ //get point of impact and impact direction
        if(broken) //if already broken then no impact
            return false;
        impact(); //else signal impact
        return broken; //return flag from impact()
    }

    public abstract Shape getBrick(); //override in subclass

    public Color getBorderColor(){ //get border colour of brick
        return  border;
    }

    public Color getInnerColor(){ //get inner colour of brick
        return inner;
    }


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

    public final boolean isBroken(){ //signal condition of brick
        return broken;
    }

    public void repair() { //repair brick
        broken = false; //reset broken flag
        strength = fullStrength; //set strength to full strength
    }

    public void impact(){ //if impact occurs
        strength--; //reduce brick strength
        broken = (strength == 0); //if strength = 0, signal brick broken
    }

    public int getScore(){
        return score;
    }
}





