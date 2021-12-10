package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Class Score is used as a linked list to load and hold the basic information about the player such as ranking,
 * name, level type, time and score after completing a level. Class Score is also used to generate the highscore
 * list after every level and to generate the top 10 highscore on the HomeMenu.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
class Score { //class for linked list to hold all scores
    /**
     * The ranking of the player.
     */
    int ranking;
    /**
     * The score of the player.
     */
    int score;
    /**
     * The time of the player.
     */
    int time;
    /**
     * The name of the player.
     */
    String name;
    /**
     * The level type of the player.
     */
    String levelType; //player name

    /**
     * This constructor is called to add new entries to the linked list.
     * @param ranking The ranking of the new entry.
     * @param name The name of the new entry.
     * @param levelType The level type of the new entry.
     * @param time The time of the new entry.
     * @param score The score of the new entry.
     */
    public Score(int ranking,String name,String levelType,int time,int score){
        this.ranking = ranking;
        this.name = name;
        this.levelType = levelType;
        this.time = time;
        this.score = score;
    }
}

/**
 * Class ScoreComp is a comparator to compare the scores and time of all the players to generate a ranking list.
 * First, the score is sorted in descending order so that the higher the score, the higher the ranking. If there
 * are equal scores, then the time is taken into consideration. The time is sorted in ascending order so that
 * the lower the time, the higher the ranking. This means that for equal scores, the one with the lower time
 * is higher on the ranking.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
class ScoreComp implements Comparator<Score> { //comparator to sort score in descending order
    @Override
    public int compare(Score comp1, Score comp2) {

        if(comp1.score == comp2.score){
            return Integer.compare(comp1.time, comp2.time);
        }
        else if (comp2.score > comp1.score){
            return 1;
        }
        else {
            return -1;
        }
    }
}

/**
 * Public class Highscore is responsible for generating and showing the top 10 highest scores for each level and
 * the total highscore for the game on the Highscore Board which can be reached from the HomeMenu. Every time the
 * highscore lists are loaded, they are first resorted using the comparator. Then, the list is renumbered
 * according to the new ordering. Although the lists are not expected to have any issue in the ranking since
 * they are already resorted in another class, these 2 actions serve to act as a precaution in case there is
 * an unexpected issue with the highscore lists.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class Highscore extends JComponent {

    /**
     * Rank string.
     */
    private static final String RANKING = "RANK";
    /**
     * Name string.
     */
    private static final String NAME = "NAME";
    /**
     * Category string.
     */
    private static final String CATEGORY = "CATEGORY";
    /**
     * Time string.
     */
    private static final String TIME = "TIME";
    /**
     * Score string.
     */
    private static final String SCORE = "SCORE";
    /**
     * Back string for the back button.
     */
    private static final String BACKBUTTON = "BACK";
    /**
     * Next string for the next button.
     */
    private static final String NEXTBUTTON = "NEXT";
    /**
     * Previous string for the previous button.
     */
    private static final String PREVIOUSBUTTON = "PREVIOUS";
    /**
     * Total HighScores string.
     */
    private static final String HIGHSCORE = "TOTAL HIGHSCORES";
    /**
     * Text colour of purple.
     */
    private static final Color TEXT_COLOR = new Color(255, 2, 248);
    /**
     * Clicked button colour of brighter purple.
     */
    private static final Color CLICKED_BUTTON_COLOR = TEXT_COLOR.brighter();
    /**
     * Clicked text colour of white.
     */
    private static final Color CLICKED_TEXT = Color.WHITE;

    /**
     * Rectangle for the menu face.
     */
    private final Rectangle menuFace;
    /**
     * Rectangle for the back button.
     */
    private final Rectangle backButton;
    /**
     * Rectangle for the next button.
     */
    private final Rectangle nextButton;
    /**
     * Rectangle for the previous button.
     */
    private final Rectangle previousButton;
    /**
     * Font for the ranking list.
     */
    private final Font InfoFont;
    /**
     * Font for the ranking titles.
     */
    private final Font TitleFont;
    /**
     * Font for the button text.
     */
    private final Font buttonFont;

    /**
     * Boolean to check if back button is clicked.
     */
    private boolean backClicked;
    /**
     * Boolean to check if next button is clicked.
     */
    private boolean nextClicked;
    /**
     * Boolean to check if previous button is clicked.
     */
    private boolean previousClicked;

    /**
     * Integer to choose which level highscores are chosen to be displayed.
     */
    private int level;
    /**
     * Image for Highscore background.
     */
    private final Image newImage;

    /**
     * This constructor is used to create a menu face to display the highscores for each level and the total
     * highscores for the game. Buttons are also created and assigned to mouse listeners to detect mouse clicks
     * and to load other highscore pages or to return to the HomeMenu.
     *
     * @param owner This GameFrame is used to link to HighScore so that HighScore can access ite methods.
     * @param area This is the size of the HomeMenu and is used to load HighScore with the same size.
     */
    public Highscore(GameFrame owner,Dimension area) throws IOException {

        this.setFocusable(true); //set focusable
        this.requestFocusInWindow();

        BufferedImage myPicture = ImageIO.read(new File("image/HighScoreBackground.png"));
        newImage = myPicture.getScaledInstance((int)area.getWidth(),(int)area.getHeight(), Image.SCALE_DEFAULT);

        this.addMouseListener(new MouseAdapter() {

            /**
             * This mouse listener is called when mouse is clicked and load relevant events if the relevant button
             * is clicked.
             * @param mouseEvent This parameter is used to track the mouse.
             */
            @Override
            public void mouseClicked(MouseEvent mouseEvent) { //if mouse clicked
                Point p = mouseEvent.getPoint(); //get mouse coordinate
                if(backButton.contains(p)){ //if mouse inside start button
                    owner.enableHomeMenu(); //start game
                }
                else if(previousButton.contains(p)){ //if mouse inside start button
                    level--;
                    repaint();
                }
                else if(nextButton.contains(p)){ //if mouse inside start button
                    level++;
                    repaint();
                }
            }

            /**
             * This mouse listener is called when mouse is pressed and held and sets clicked flag to true and repaints
             * the button if a button is pressed and held.
             * @param mouseEvent This parameter is used to track the mouse.
             */
            @Override
            public void mousePressed(MouseEvent mouseEvent) { //if mouse is held down
                Point p = mouseEvent.getPoint(); //get mouse coordinate
                if(backButton.contains(p)){ //if mouse inside start button
                    backClicked = true; //save start input
                    repaint(backButton.x,backButton.y,backButton.width+1,backButton.height+1); //redraw start button
                }
                else if(previousButton.contains(p)){ //if mouse inside start button
                    previousClicked = true; //save start input
                    repaint(previousButton.x,previousButton.y,previousButton.width+1,previousButton.height+1); //redraw start button
                }
                else if(nextButton.contains(p)){ //if mouse inside start button
                    nextClicked = true; //save start input
                    repaint(nextButton.x,nextButton.y,nextButton.width+1,nextButton.height+1); //redraw start button
                }
            }

            /**
             * This mouse listener is called when mouse is released and sets clicked flag to false and repaints
             * the button if a button is released.
             * @param mouseEvent This parameter is used to track the mouse.
             */
            @Override
            public void mouseReleased(MouseEvent mouseEvent) { //if mouse released
                if(backClicked){ //if start button clicked
                    backClicked = false; //reset flag
                    repaint(backButton.x,backButton.y,backButton.width+1,backButton.height+1); //redraw start button
                } //buttons will turn normal for a second before game starts
                else if(previousClicked){ //if start button clicked
                    previousClicked = false; //reset flag
                    repaint(previousButton.x,previousButton.y,previousButton.width+1,previousButton.height+1); //redraw start button
                }
                else if(nextClicked){ //if start button clicked
                    nextClicked = false; //reset flag
                    repaint(nextButton.x,nextButton.y,nextButton.width+1,nextButton.height+1); //redraw start button
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            /**
             * This mouse motion listener is called when mouse is moved and changes the mouse cursor to hand cursor if
             * the mouse cursor is inside a button.
             * @param mouseEvent This parameter is used to track the mouse motion.
             */
            @Override
            public void mouseMoved(MouseEvent mouseEvent) { //if mouse moved
                Point p = mouseEvent.getPoint(); //get mouse coordinates
                if(backButton.contains(p)||previousButton.contains(p)||nextButton.contains(p)) //if mouse inside either button
                    owner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //turn mouse cursor into hand cursor
                else
                    owner.setCursor(Cursor.getDefaultCursor()); //else use default mouse cursor
            }
        });

        menuFace = new Rectangle(new Point(0,0),area); //make menu face
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 4, area.height / 12);
        backButton = new Rectangle(btnDim); //makes buttons
        nextButton = new Rectangle(btnDim);
        previousButton = new Rectangle(btnDim);

        TitleFont = new Font("Noto Mono",Font.BOLD,20);
        InfoFont = new Font("Noto Mono",Font.PLAIN,17);
        buttonFont = new Font("Monospaced",Font.BOLD,backButton.height-2);

        level = 0;
    }

    /**
     * This method is responsible for painting and repainting the HighScore.
     * @param g This graphics class is used to paint the HighScore.
     */
    public void paint(Graphics g){ //draw main menu
        try {
            drawScoreBoard((Graphics2D)g);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for painting and repainting the HighScore.
     * @param g2d This graphics class is used to paint the HighScore.
     * @throws FileNotFoundException This IOException is thrown if the highscore list does not exist.
     */
    private void drawScoreBoard(Graphics2D g2d) throws FileNotFoundException {

        g2d.drawImage(newImage,0,0,null);
        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
         */
        Color prevColor = g2d.getColor(); //save previous colour
        Font prevFont = g2d.getFont(); //save previous font

        double x = menuFace.getX(); //get coordinates of menu
        double y = menuFace.getY();

        g2d.translate(x,y); //move all points for processing

        //method calls
        drawText(g2d,(level % 6 + 6) % 6); //draw menu text
        drawButton(g2d); //draw menu button
        //end of methods calls

        g2d.translate(-x,-y); //move points back
        g2d.setFont(prevFont); //get previous font
        g2d.setColor(prevColor); //get previous colour
    }

    /**
     * This method is responsible for painting and repainting the text of the Highscore such as the player ranking,
     * name, category, time and score.
     *
     * @param g2d This graphics class is used to paint the text of Highscore.
     * @param level This is the level for which the current highscore list is showing for.
     * @throws FileNotFoundException This IOException is thrown if the highscore list does not exist.
     */
    private void drawText(Graphics2D g2d, int level) throws FileNotFoundException { //draw menu text

        LinkedList<Score> newScore = new LinkedList<>(); //linked list to hold treatment plans

        Load(newScore,level); //load scores into linked list

        newScore.sort(new ScoreComp());
        Renumber(newScore);

        g2d.setColor(TEXT_COLOR); //get text colour
        g2d.setFont(TitleFont); //get text font

        int sX,sY;

        sY = (int)(menuFace.getHeight()*0.1);

        sX = (int)(menuFace.getWidth()*0.28); //get coordinates of text start point

        if(level==0) {
            g2d.drawString(HIGHSCORE, sX, sY); //draw greetings
        }
        else {
            g2d.drawString("LEVEL "+level+" HIGHSCORES", sX, sY); //draw greetings
        }

        sY += (int)(menuFace.getHeight()*0.069);

        sX = (int)(menuFace.getWidth()*0.02); //get coordinates of text start point
        g2d.drawString(RANKING,sX,sY); //draw greetings

        sX = (int)(menuFace.getWidth()*0.18); //get coordinates of text start point
        g2d.drawString(NAME,sX,sY); //draw greetings

        sX = (int)(menuFace.getWidth()*0.39); //get coordinates of text start point
        g2d.drawString(CATEGORY,sX,sY); //draw greetings

        sX = (int)(menuFace.getWidth()*0.7); //get coordinates of text start point
        g2d.drawString(TIME,sX,sY); //draw greetings

        sX = (int)(menuFace.getWidth()*0.83); //get coordinates of text start point
        g2d.drawString(SCORE,sX,sY); //draw greetings

        g2d.setFont(InfoFont); //get text font

        int count = 0;

        for(Score print : newScore) {

            if(count++==10){
                break;
            }

            String time = String.format("%02d:%02d",print.time/60,print.time%60);

            sY += (int) menuFace.getHeight() * 0.069; //add 10% of String height between the two strings

            sX = (int)(menuFace.getWidth()*0.06); //get coordinates of text start point
            g2d.drawString(Integer.toString(print.ranking), sX, sY); //draw credits*/

            sX = (int)(menuFace.getWidth()*0.16); //get coordinates of text start point
            g2d.drawString(print.name, sX, sY); //draw credits*/

            sX = (int)(menuFace.getWidth()*0.39); //get coordinates of text start point
            g2d.drawString(print.levelType, sX, sY); //draw credits*/

            sX = (int)(menuFace.getWidth()*0.71); //get coordinates of text start point
            g2d.drawString(time, sX, sY); //draw credits*/

            sX = (int)(menuFace.getWidth()*0.84); //get coordinates of text start point
            g2d.drawString(Integer.toString(print.score), sX, sY); //draw credits*/
        }
    }

    /**
     * This method is responsible for painting and repainting the buttons of Highscore.
     * @param g2d This graphics class is used to paint the buttons of Highscore.
     */
    private void drawButton(Graphics2D g2d){ //draw menu buttons

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D btxtRect = buttonFont.getStringBounds(BACKBUTTON,frc); //get bounds of menu buttons
        Rectangle2D ntxtRect = buttonFont.getStringBounds(NEXTBUTTON,frc); //get bounds of menu buttons
        Rectangle2D ptxtRect = buttonFont.getStringBounds(PREVIOUSBUTTON,frc); //get bounds of menu buttons

        g2d.setFont(buttonFont); //set button font

        int x = (menuFace.width - backButton.width) / 9; //get coordinates of text start point
        int y =(int) ((menuFace.height - backButton.height) * 0.95);

        previousButton.setLocation(x,y); //set start button location

        x = (int)(previousButton.getWidth() - ptxtRect.getWidth()) / 2; //get start text location
        y = (int)(previousButton.getHeight() - ptxtRect.getHeight()) / 2;

        x += previousButton.x;
        y += previousButton.y + (previousButton.height * 0.9);

        if(previousClicked){ //if start button clicked
            Color tmp = g2d.getColor(); //save current colour
            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(previousButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(PREVIOUSBUTTON,x,y); //draw start text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(previousButton); //draw default start button
            g2d.drawString(PREVIOUSBUTTON,x,y); //draw default start text
        }

        x = previousButton.x;
        y = previousButton.y;

        x += 1.2 * previousButton.getWidth();

        backButton.setLocation(x,y); //set start button location

        x = (int)(backButton.getWidth() - btxtRect.getWidth()) / 2; //get start text location
        y = (int)(backButton.getHeight() - btxtRect.getHeight()) / 2;

        x += backButton.x;
        y += backButton.y + (backButton.height * 0.85);

        if(backClicked){ //if start button clicked
            Color tmp = g2d.getColor(); //save current colour
            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(backButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(BACKBUTTON,x,y); //draw start text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(backButton); //draw default start button
            g2d.drawString(BACKBUTTON,x,y); //draw default start text
        }

        x = backButton.x;
        y = backButton.y;

        x += 1.2 * backButton.getWidth();

        nextButton.setLocation(x,y); //set start button location

        x = (int)(nextButton.getWidth() - ntxtRect.getWidth()) / 2; //get start text location
        y = (int)(nextButton.getHeight() - ntxtRect.getHeight()) / 2;

        x += nextButton.x;
        y += nextButton.y + (nextButton.height * 0.85);

        if(nextClicked){ //if start button clicked
            Color tmp = g2d.getColor(); //save current colour
            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(nextButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(NEXTBUTTON,x,y); //draw start text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(nextButton); //draw default start button
            g2d.drawString(NEXTBUTTON,x,y); //draw default start text
        }
    }

    /**
     * This method is used to load the highscore lists into linked lists so that the top 10 highscores for each
     * level and the total highscore for the game can be shown.
     *
     * @param loadScore The linked list where the highscore list is loaded into.
     * @param level This is the level for which the highscore list is loaded.
     * @throws FileNotFoundException This IOException is thrown if the highscore list does not exist.
     */
    private void Load(LinkedList<Score> loadScore, int level) throws FileNotFoundException {

        String[] hold = new String[5]; //make array of strings with 8 elements

        File file=new File("highscores/highscore"+level+".txt"); //load location
        Scanner sc=new Scanner(file);

        while(sc.hasNextLine()){ //tokenize string using , and stop when list is empty
            StringTokenizer st = new StringTokenizer(sc.nextLine(),",");

            while (st.hasMoreTokens()) { //temporarily save info of treatment in each loop
                for (int count = 0; count<5; count++){
                    hold[count] = st.nextToken();
                }
                Score input = new Score(Integer.parseInt(hold[0]), hold[1], hold[2], Integer.parseInt(hold[3]),Integer.parseInt(hold[4]));
                loadScore.add(input); //change each string to correct type and load in linked list
            }
        }
    }

    /**
     * This method is called to renumber the linked list after resorting it. The new rankings will be in ascending
     * order in increments of 1 starting from 1.
     *
     * @param holdScore The linked list which is to be renumbered.
     */
    private void Renumber(LinkedList<Score> holdScore){

        int num = 1; //array of int to count number of treatment of each type

        for(Score renum : holdScore){ //count number of treatment of each type
            renum.ranking = num++;
        }
    }
}
