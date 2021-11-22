package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

class Score { //class for linked list to hold all scores
    int ranking,score;
    String name; //player name

    public Score(int ranking,String name,int score){
        this.ranking = ranking;
        this.name = name;
        this.score = score;
    }
}

class ScoreComp implements Comparator<Score> { //comparator to sort score in descending order
    @Override
    public int compare(Score comp1, Score comp2) {
        return Integer.compare(comp2.score, comp1.score);
    }
}

public class Highscore extends JComponent {

    private static final String RANKING = "RANKING";
    private static final String NAME = "NAME";
    private static final String SCORE = "SCORE";
    private static final String BUTTON = "BACK";

    private static final Color BG_COLOR = Color.BLUE.darker().darker();
    private static final Color TEXT_COLOR = new Color(115, 50, 241);//egyptian blue
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;

    private GameFrame owner;

    private Rectangle menuFace;
    private Rectangle backButton;

    private Font InfoFont;
    private Font TitleFont;
    private Font buttonFont;

    private boolean backClicked;

    public Highscore(GameFrame owner,Dimension area) throws IOException {

        this.setFocusable(true); //set focusable
        this.requestFocusInWindow();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) { //if mouse clicked
                Point p = mouseEvent.getPoint(); //get mouse coordinate
                if(backButton.contains(p)){ //if mouse inside start button
                    owner.enableHomeMenu(); //start game
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) { //if mouse is held down
                Point p = mouseEvent.getPoint(); //get mouse coordinate
                if(backButton.contains(p)){ //if mouse inside start button
                    backClicked = true; //save start input
                    repaint(backButton.x,backButton.y,backButton.width+1,backButton.height+1); //redraw start button
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) { //if mouse released
                if(backClicked){ //if start button clicked
                    backClicked = false; //reset flag
                    repaint(backButton.x,backButton.y,backButton.width+1,backButton.height+1); //redraw start button
                } //buttons will turn normal for a second before game starts
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent mouseEvent) { //if mouse moved
                Point p = mouseEvent.getPoint(); //get mouse coordinates
                if(backButton.contains(p)) //if mouse inside either button
                    owner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //turn mouse cursor into hand cursor
                else
                    owner.setCursor(Cursor.getDefaultCursor()); //else use default mouse cursor
            }
        });

        this.owner = owner;

        menuFace = new Rectangle(new Point(0,0),area); //make menu face
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        backButton = new Rectangle(btnDim); //makes buttons

        TitleFont = new Font("Noto Mono",Font.BOLD,20);
        InfoFont = new Font("Noto Mono",Font.PLAIN,17);
        buttonFont = new Font("Monospaced",Font.PLAIN,backButton.height-2);

        LinkedList<Score> newScore = new LinkedList<>(); //linked list to hold treatment plans

        this.Load(newScore); //load scores into linked list

        newScore.sort(new ScoreComp());
        newScore = this.Renumber(newScore);

        this.Save(newScore);
    }

    public void paint(Graphics g){ //draw main menu
        try {
            drawScoreBoard((Graphics2D)g);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void drawScoreBoard(Graphics2D g2d) throws FileNotFoundException {

        drawContainer(g2d); //draw main menu
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

        //methods calls
        drawText(g2d); //draw menu text
        drawButton(g2d); //draw menu button
        //end of methods calls

        g2d.translate(-x,-y); //move points back
        g2d.setFont(prevFont); //get previous font
        g2d.setColor(prevColor); //get previous colour
    }

    private void drawContainer(Graphics2D g2d){

        Color prev = g2d.getColor(); //save previous colour

        g2d.setColor(BG_COLOR); //get background colour
        g2d.fill(menuFace); //fill menu with green

        g2d.setColor(prev); //get previous colour
    }

    private void drawText(Graphics2D g2d) throws FileNotFoundException { //draw menu text

        LinkedList<Score> newScore = new LinkedList<>(); //linked list to hold treatment plans

        this.Load(newScore); //load scores into linked list

        newScore.sort(new ScoreComp());
        newScore = this.Renumber(newScore);

        g2d.setColor(TEXT_COLOR); //get text colour
        g2d.setFont(TitleFont); //get text font

        int sX,sY;

        sY = (int)(menuFace.getHeight()*0.1);

        sX = (int)(menuFace.getWidth()*0.1); //get coordinates of text start point
        g2d.drawString(RANKING,sX,sY); //draw greetings

        sX = (int)(menuFace.getWidth()*0.4); //get coordinates of text start point
        g2d.drawString(NAME,sX,sY); //draw greetings

        sX = (int)(menuFace.getWidth()*0.7); //get coordinates of text start point
        g2d.drawString(SCORE,sX,sY); //draw greetings

        g2d.setFont(InfoFont); //get text font

        int count = 0;

        for(Score print : newScore) {

            if(count++==10){
                break;
            }

            sY += (int) menuFace.getHeight() * 0.076; //add 10% of String height between the two strings

            sX = (int)(menuFace.getWidth()*0.18); //get coordinates of text start point
            g2d.drawString(Integer.toString(print.ranking), sX, sY); //draw credits*/

            sX = (int)(menuFace.getWidth()*0.4); //get coordinates of text start point
            g2d.drawString(print.name, sX, sY); //draw credits*/

            sX = (int)(menuFace.getWidth()*0.75); //get coordinates of text start point
            g2d.drawString(Integer.toString(print.score), sX, sY); //draw credits*/
        }
    }

    private void drawButton(Graphics2D g2d){ //draw menu buttons

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D btxtRect = buttonFont.getStringBounds(BUTTON,frc); //get bounds of menu buttons

        g2d.setFont(buttonFont); //set button font

        int x = (menuFace.width - backButton.width) / 2; //get coordinates of text start point
        int y =(int) ((menuFace.height - backButton.height) * 0.95);

        backButton.setLocation(x,y); //set start button location

        x = (int)(btxtRect.getWidth() - btxtRect.getWidth()) / 2; //get start text location
        y = (int)(btxtRect.getHeight() - btxtRect.getHeight()) / 2;

        x += backButton.x + (backButton.width / 3.3);
        y += backButton.y + (backButton.height * 0.85);

        if(backClicked){ //if start button clicked
            Color tmp = g2d.getColor(); //save current colour
            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(backButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(BUTTON,x,y); //draw start text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(backButton); //draw default start button
            g2d.drawString(BUTTON,x,y); //draw default start text
        }
    }

    public void Load(LinkedList<Score> loadScore) throws FileNotFoundException {

        File file=new File("highscore.txt"); //load location
        Scanner sc=new Scanner(file);

        String[] hold = new String[3]; //make array of strings with 8 elements

        while(sc.hasNextLine()){ //tokenize string using , and stop when list is empty
            StringTokenizer st = new StringTokenizer(sc.nextLine(),",");

            while (st.hasMoreTokens()) { //temporarily save info of treatment in each loop
                for (int count = 0; count<3; count++){
                    hold[count] = st.nextToken();
                }
                Score input = new Score(Integer.parseInt(hold[0]),hold[1],Integer.parseInt(hold[2]));
                loadScore.add(input); //change each string to correct type and load in linked list
            }
        }
    }

    public void Save(LinkedList<Score> saveScore) throws IOException {

        PrintWriter writer = new PrintWriter("highscore.txt", StandardCharsets.UTF_8); //save location (can add code to change location)

        for(Score print : saveScore) { //save line by line with for loop
            writer.format("%d,%S,%d\n", print.ranking, print.name, print.score);
        }
        writer.close(); //close writer
    }

    public LinkedList<Score> Renumber(LinkedList<Score> holdScore){

        int num = 1; //array of int to count number of treatment of each type

        for(Score renum : holdScore){ //count number of treatment of each type
            renum.ranking = num++;
        }
        return holdScore; //return renumbered linked list
    }

    public LinkedList<Score> Insert(LinkedList<Score> holdScore) {

        int ranking = 0,score = 0;
        String name = "null";

        Score newScore = new Score(ranking,name,score);
        holdScore.add(newScore); //add new entry into linked list

        return holdScore; //return new linked list
    }
}
