import java.awt.*;
import java.awt.geom.GeneralPath;

public class Brick {

    private Shape brickFace;
    private final Shape brickFaceNew;

    private Color border;
    private Color inner;

    private int fullStrength;
    private int strength;

    private double breakProbability;

    private final int score;
    private int scoreMultiplier;

    private boolean broken;
    private boolean crackable;

    private final GeneralPath crack = new GeneralPath();

    public Brick(int brickID, int x, int y, int width, int height, Dimension area){

        getBrickInfo(brickID);
        this.broken = false;
        this.strength = fullStrength;
        this.score = scoreMultiplier * area.width/width;
        this.brickFace = new Rectangle(new Point(x,y), new Dimension(width,height));
        this.brickFaceNew = new Rectangle(new Point(x,y), new Dimension(width,height));
    }

    public void getBrickInfo(int brickID){

        switch (brickID) {
            case 1 -> {
                //Clay Brick
                fullStrength = 1;
                scoreMultiplier = 10;
                breakProbability = 1;
                crackable = false;
                border = Color.GRAY;
                inner = new Color(178, 34, 34).darker();
            }
            case 2 -> {
                //Steel Brick
                fullStrength = 1;
                scoreMultiplier = 40;
                breakProbability = 0.4;
                crackable = false;
                border = Color.BLACK;
                inner = new Color(203, 203, 201);
            }
            case 3 -> {
                //Cement Brick
                fullStrength = 2;
                scoreMultiplier = 40;
                breakProbability = 1;
                crackable = true;
                border = new Color(217, 199, 175);
                inner = new Color(147, 147, 147);
            }
            case 4 -> {
                //Concrete Brick
                fullStrength = 2;
                scoreMultiplier = 100;
                breakProbability = 0.4;
                crackable = true;
                border = new Color(24, 22, 16);
                inner = new Color(50, 46, 46);
            }
        }
    }

    public Color getBorder() {
        return border;
    }

    public Color getInner() {
        return inner;
    }

    public Shape getBrickFace() {
        return brickFace;
    }

    public void setBrickFace(Shape brickFace){
        this.brickFace = brickFace;
    }

    public Shape getBrickFaceNew() {
        return brickFaceNew;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public double getBreakProbability() {
        return breakProbability;
    }

    public int getFullStrength() {
        return fullStrength;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public GeneralPath getCrack() {
        return crack;
    }

    public boolean isCrackable() {
        return crackable;
    }

    public int getScore() {
        return score;
    }
}
