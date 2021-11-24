/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;


public class HomeMenu extends JComponent {

    private static final String GREETINGS = "Welcome to:";
    private static final String GAME_TITLE = "Brick Destroy";
    private static final String CREDITS = "Version 0.1";
    private static final String START_TEXT = "Start";
    private static final String MENU_TEXT = "Exit";

    private static final String SCORE_TEXT = "HighScores";
    private static final String CUSTOM_TEXT = "Customise";

    private static final Color BG_COLOR = Color.GREEN.darker();
    private static final Color BORDER_COLOR = new Color(200,8,21); //Venetian Red
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);//school bus yellow
    private static final Color TEXT_COLOR = new Color(16, 52, 166);//egyptian blue
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    private Rectangle menuFace;
    private Rectangle startButton;
    private Rectangle menuButton;

    private BasicStroke borderStoke;
    private BasicStroke borderStoke_noDashes;

    private Font greetingsFont;
    private Font gameTitleFont;
    private Font creditsFont;
    private Font buttonFont;

    private Rectangle scoreButton;
    private Rectangle customButton;

    private GameFrame owner;

    private boolean startClicked;
    private boolean menuClicked;

    private boolean scoreClicked;
    private boolean customClicked;

    public HomeMenu(GameFrame owner,Dimension area){

        this.setFocusable(true); //set focusable
        this.requestFocusInWindow();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) { //if mouse clicked
                Point p = mouseEvent.getPoint(); //get mouse coordinate
                if(startButton.contains(p)){ //if mouse inside start button
                    try {
                        owner.enableGameBoard(); //start game
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(menuButton.contains(p)){ //if mouse inside exit button
                    System.out.println("Goodbye " + System.getProperty("user.name"));
                    System.exit(0); //exit game
                }
                else if(customButton.contains(p)){ //if mouse inside exit button
                    owner.enableCustomConsole();
                }
                else if(scoreButton.contains(p)){ //if mouse inside exit button
                    owner.enableScoreBoard();
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) { //if mouse is held down
                Point p = mouseEvent.getPoint(); //get mouse coordinate
                if(startButton.contains(p)){ //if mouse inside start button
                    startClicked = true; //save start input
                    repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1); //redraw start button

                }
                else if(menuButton.contains(p)){ //if mouse inside exit button
                    menuClicked = true; //save exit input
                    repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1); //redraw exit button
                }
                else if(customButton.contains(p)){ //if mouse inside exit button
                    customClicked = true; //save exit input
                    repaint(customButton.x,customButton.y,customButton.width+1,customButton.height+1); //redraw exit button
                }
                else if(scoreButton.contains(p)){ //if mouse inside exit button
                    scoreClicked = true; //save exit input
                    repaint(scoreButton.x,scoreButton.y,scoreButton.width+1,scoreButton.height+1); //redraw exit button
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) { //if mouse released
                if(startClicked ){ //if start button clicked
                    startClicked = false; //reset flag
                    repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1); //redraw start button
                } //buttons will turn normal for a second before game starts
                else if(menuClicked){ //if exit button clicked
                    menuClicked = false; //reset flag
                    repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1); //redraw exit button
                }
                else if(customClicked){ //if exit button clicked
                    customClicked = false; //reset flag
                    repaint(customButton.x,customButton.y,customButton.width+1,customButton.height+1); //redraw exit button
                }
                else if(scoreClicked){ //if exit button clicked
                    scoreClicked = false; //reset flag
                    repaint(scoreButton.x,scoreButton.y,scoreButton.width+1,scoreButton.height+1); //redraw exit button
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent mouseEvent) { //if mouse moved
                Point p = mouseEvent.getPoint(); //get mouse coordinates
                if(startButton.contains(p) || menuButton.contains(p) || customButton.contains(p) || scoreButton.contains(p)) //if mouse inside either button
                    owner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //turn mouse cursor into hand cursor
                else
                    owner.setCursor(Cursor.getDefaultCursor()); //else use default mouse cursor
            }
        });

        this.owner = owner;

        menuFace = new Rectangle(new Point(0,0),area); //make menu face
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        startButton = new Rectangle(btnDim); //makes buttons
        menuButton = new Rectangle(btnDim);

        customButton = new Rectangle(btnDim);
        scoreButton = new Rectangle(btnDim);
        //border stroke lines
        borderStoke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        //text fonts
        greetingsFont = new Font("Noto Mono",Font.PLAIN,25);
        gameTitleFont = new Font("Noto Mono",Font.BOLD,40);
        creditsFont = new Font("Monospaced",Font.PLAIN,10);
        buttonFont = new Font("Monospaced",Font.PLAIN,startButton.height-2);
    }

    public void paint(Graphics g){ //draw main menu
        drawMenu((Graphics2D)g);
    }

    public void drawMenu(Graphics2D g2d){

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

        Stroke tmp = g2d.getStroke(); //save previous line style (stroke)

        g2d.setStroke(borderStoke_noDashes); //set new line style (stroke)
        g2d.setColor(DASH_BORDER_COLOR); //set new colour
        g2d.draw(menuFace); //draw outline of menu with current colour and stroke

        g2d.setStroke(borderStoke); //set new line style (stroke)
        g2d.setColor(BORDER_COLOR); //set new colour
        g2d.draw(menuFace); //draw outline of menu with current colour and stroke

        g2d.setStroke(tmp); //get previous line style (stroke)

        g2d.setColor(prev); //get previous colour
    }

    private void drawText(Graphics2D g2d){ //draw menu text

        g2d.setColor(TEXT_COLOR); //get text colour

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D greetingsRect = greetingsFont.getStringBounds(GREETINGS,frc); //get bounds of menu text
        Rectangle2D gameTitleRect = gameTitleFont.getStringBounds(GAME_TITLE,frc);
        Rectangle2D creditsRect = creditsFont.getStringBounds(CREDITS,frc);

        int sX,sY;

        sX = (int)(menuFace.getWidth() - greetingsRect.getWidth()) / 2; //get coordinates of text start point
        sY = (int)(menuFace.getHeight() / 4);

        g2d.setFont(greetingsFont); //get text font
        g2d.drawString(GREETINGS,sX,sY); //draw greetings

        sX = (int)(menuFace.getWidth() - gameTitleRect.getWidth()) / 2; //get coordinates of text start point
        sY += (int) gameTitleRect.getHeight() * 1.1;//add 10% of String height between the two strings

        g2d.setFont(gameTitleFont); //get text font
        g2d.drawString(GAME_TITLE,sX,sY); //draw game title

        sX = (int)(menuFace.getWidth() - creditsRect.getWidth()) / 2; //get coordinates of text start point
        sY += (int) creditsRect.getHeight() * 1.1; //add 10% of String height between the two strings

        g2d.setFont(creditsFont); //get text font
        g2d.drawString(CREDITS,sX,sY); //draw credits
    }

    private void drawButton(Graphics2D g2d){ //draw menu buttons

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc); //get bounds of menu buttons
        Rectangle2D mTxtRect = buttonFont.getStringBounds(MENU_TEXT,frc);

        Rectangle2D hTxtRect = buttonFont.getStringBounds(SCORE_TEXT,frc);
        Rectangle2D cTxtRect = buttonFont.getStringBounds(CUSTOM_TEXT,frc);

        g2d.setFont(buttonFont); //set button font

        int x = (menuFace.width - startButton.width) / 2; //get coordinates of text start point
        //int y =(int) ((menuFace.height - startButton.height) * 0.8);
        int y =(int) ((menuFace.height - startButton.height) * 0.6);

        startButton.setLocation(x,y); //set start button location

        x = (int)(startButton.getWidth() - txtRect.getWidth()) / 2; //get start text location
        y = (int)(startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);

        if(startClicked){ //if start button clicked
            Color tmp = g2d.getColor(); //save current colour
            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(startButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(START_TEXT,x,y); //draw start text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(startButton); //draw default start button
            g2d.drawString(START_TEXT,x,y); //draw default start text
        }

        x = startButton.x;
        y = startButton.y;

        y *= 1.2;

        customButton.setLocation(x,y); //set exit button location

        x = (int)(customButton.getWidth() - cTxtRect.getWidth()) / 2; //get custom text location
        y = (int)(customButton.getHeight() - cTxtRect.getHeight()) / 2;

        x += customButton.x;
        y += customButton.y + (startButton.height * 0.9);

        if(customClicked){ //if exit button clicked
            Color tmp = g2d.getColor(); //save current colour

            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(customButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(CUSTOM_TEXT,x,y); //draw exit text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(customButton); //draw default exit button
            g2d.drawString(CUSTOM_TEXT,x,y); //draw default exit text
        }

        x = customButton.x;
        y = customButton.y;

        y *= 1.18;

        scoreButton.setLocation(x,y); //set exit button location

        x = (int)(scoreButton.getWidth() - hTxtRect.getWidth()) / 2; //get exit text location
        y = (int)(scoreButton.getHeight() - hTxtRect.getHeight()) / 2;

        x += scoreButton.x;
        y += scoreButton.y + (startButton.height * 0.9);

        if(scoreClicked){ //if exit button clicked
            Color tmp = g2d.getColor(); //save current colour

            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(scoreButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(SCORE_TEXT,x,y); //draw exit text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(scoreButton); //draw default exit button
            g2d.drawString(SCORE_TEXT,x,y); //draw default exit text
        }

        x = scoreButton.x;
        y = scoreButton.y;

        y *= 1.15;

        menuButton.setLocation(x,y); //set exit button location

        x = (int)(menuButton.getWidth() - mTxtRect.getWidth()) / 2; //get exit text location
        y = (int)(menuButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += menuButton.x;
        y += menuButton.y + (startButton.height * 0.9);

        if(menuClicked){ //if exit button clicked
            Color tmp = g2d.getColor(); //save current colour

            g2d.setColor(CLICKED_BUTTON_COLOR); //get button clicked colour
            g2d.draw(menuButton); //draw button border to make it look brighter
            g2d.setColor(CLICKED_TEXT); //get text clicked colour
            g2d.drawString(MENU_TEXT,x,y); //draw exit text to make it look brighter
            g2d.setColor(tmp); //get previous colour
        }
        else{
            g2d.draw(menuButton); //draw default exit button
            g2d.drawString(MENU_TEXT,x,y); //draw default exit text
        }
    }
}
