package Main.Others;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Public class HomeMenu is the first screen the player sees when starting the game. Public class HomeMenu contains
 * 5 buttons which have different functionalities and also a PopUpMenu which shows some sample photos of some
 * random levels.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class HomeMenu extends JComponent {

    /**
     * The greeting.
     */
    private static final String GREETINGS = "Welcome to:";
    /**
     * The game name.
     */
    private static final String GAME_TITLE = "Brick Destroy";
    /**
     * The game version.
     */
    private static final String CREDITS = "Version 1.0";
    /**
     * Start text.
     */
    private static final String START_TEXT = "Start";
    /**
     * Menu Text.
     */
    private static final String MENU_TEXT = "Exit";

    /**
     * HighScores text.
     */
    private static final String SCORE_TEXT = "HighScores";
    /**
     * Customise text.
     */
    private static final String CUSTOM_TEXT = "Customise";
    /**
     * Info text.
     */
    private static final String INFO_TEXT = "INFO";

    /**
     * Venetian Red border colour
     */
    private static final Color BORDER_COLOR = new Color(134, 4, 148);
    /**
     * School bus yellow dash border colour
     */
    private static final Color DASH_BORDER_COLOR = new Color(8, 39, 243);
    /**
     * Purple text colour
     */
    private static final Color TEXT_COLOR = new Color(130, 7, 127, 255);
    /**
     * Brighter green clicked button colour
     */
    private static final Color CLICKED_BUTTON_COLOR = TEXT_COLOR.brighter();
    /**
     * White clicked text colour
     */
    private static final Color CLICKED_TEXT = Color.WHITE;
    /**
     * Border size of 5
     */
    private static final int BORDER_SIZE = 5;
    /**
     * Array of floats of dashes
     */
    private static final float[] DASHES = {12,6};

    /**
     * Menu Face Rectangle
     */
    private final Rectangle menuFace;
    /**
     * Start button rectangle
     */
    private final Rectangle startButton;
    /**
     * Menu button rectangle
     */
    private final Rectangle menuButton;

    /**
     * Score button rectangle
     */
    private final Rectangle scoreButton;
    /**
     * Custom button rectangle
     */
    private final Rectangle customButton;
    /**
     * Image button rectangle
     */
    private final Rectangle imageButton;
    /**
     * Info button rectangle
     */
    private final Rectangle infoButton;

    /**
     * Basic stroke for border
     */
    private final BasicStroke borderStoke;
    /**
     * Basic stroke for border with no dashes
     */
    private final BasicStroke borderStoke_noDashes;

    /**
     * Font for greeting string
     */
    private final Font greetingsFont;
    /**
     * Font for game title
     */
    private final Font gameTitleFont;
    /**
     * Font for credits
     */
    private final Font creditsFont;
    /**
     * Font for buttons
     */
    private final Font buttonFont;

    /**
     * Font for text
     */
    private final Font textFont;

    /**
     * PopUpMenu to show random sample levels
     */
    private final JPopupMenu popupmenu = new JPopupMenu();

    /**
     * Boolean to check if start button clicked
     */
    private boolean startClicked;
    /**
     * Boolean to check if menu button clicked
     */
    private boolean menuClicked;

    /**
     * Boolean to check if score button clicked
     */
    private boolean scoreClicked;
    /**
     * Boolean to check if custom button clicked
     */
    private boolean customClicked;
    /**
     * Boolean to check if info button clicked
     */
    private boolean infoClicked;
    /**
     * Boolean to check if PopUpImage clicked
     */
    private boolean imageClicked;
    /**
     * Image for HomeMenu background.
     */
    private final Image newImage;

    /**
     * This constructor is used to load all the buttons on the main menu and link them to their functions. Listeners
     * are also matched to their relevant buttons to detect clicks to load the events.
     *
     * @param owner This GameFrame is used to link with the HomeMenu so that HomeMenu can access its methods.
     * @param area This is the size of the HomeMenu.
     * @throws IOException This constructor throws IOException if menu background image is not found.
     */
    public HomeMenu(GameFrame owner,Dimension area) throws IOException {

        this.setFocusable(true);
        this.requestFocusInWindow();

        BufferedImage myPicture = ImageIO.read(new File("image/BrickBreakerBackground.png"));
        newImage = myPicture.getScaledInstance((int)area.getWidth(),(int)area.getHeight(), Image.SCALE_DEFAULT);

        this.addMouseListener(new MouseAdapter() {

            /**
             * This mouse listener is called when mouse is clicked and load relevant events if the relevant button
             * is clicked.
             * @param mouseEvent This parameter is used to track the mouse.
             */
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Point p = mouseEvent.getPoint();
                if(startButton.contains(p)){
                    try {
                        owner.enableGameBoard();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(menuButton.contains(p)){
                    System.out.println("Goodbye " + System.getProperty("user.name"));
                    System.exit(0);
                }
                else if(customButton.contains(p)){
                    owner.enableCustomConsole();
                }
                else if(scoreButton.contains(p)){
                    owner.enableHighscoreBoard();
                }
                else if(imageButton.contains(p)) {
                    popupmenu.show(owner, -150, -150);
                    repaint();
                }
                else if(infoButton.contains(p)) {
                    owner.enableInfoScreen();
                }
            }

            /**
             * This mouse listener is called when mouse is pressed and held and sets clicked flag to true and repaints
             * the button if a button is pressed and held.
             * @param mouseEvent This parameter is used to track the mouse.
             */
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Point p = mouseEvent.getPoint();
                if(startButton.contains(p)){
                    startClicked = true;
                    repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
                }
                else if(menuButton.contains(p)){
                    menuClicked = true;
                    repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
                }
                else if(customButton.contains(p)){
                    customClicked = true;
                    repaint(customButton.x,customButton.y,customButton.width+1,customButton.height+1);
                }
                else if(scoreButton.contains(p)){
                    scoreClicked = true;
                    repaint(scoreButton.x,scoreButton.y,scoreButton.width+1,scoreButton.height+1);
                }
                else if(infoButton.contains(p)){
                    infoClicked = true;
                    repaint(infoButton.x,infoButton.y,infoButton.width+1,infoButton.height+1);
                }
                else if(imageButton.contains(p)){
                    imageClicked = true;
                }
            }

            /**
             * This mouse listener is called when mouse is released and sets clicked flag to false and repaints
             * the button if a button is released.
             * @param mouseEvent This parameter is used to track the mouse.
             */
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if(startClicked){
                    startClicked = false;
                    repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
                }
                else if(menuClicked){
                    menuClicked = false;
                    repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
                }
                else if(customClicked){
                    customClicked = false;
                    repaint(customButton.x,customButton.y,customButton.width+1,customButton.height+1);
                }
                else if(scoreClicked){
                    scoreClicked = false;
                    repaint(scoreButton.x,scoreButton.y,scoreButton.width+1,scoreButton.height+1);
                }
                else if(infoClicked){
                    infoClicked = false;
                    repaint(infoButton.x,infoButton.y,infoButton.width+1,infoButton.height+1);
                }
                else if(imageClicked){
                    imageClicked = false;
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
            public void mouseMoved(MouseEvent mouseEvent) {
                Point p = mouseEvent.getPoint();
                if(startButton.contains(p) || menuButton.contains(p) || customButton.contains(p) || scoreButton.contains(p) || imageButton.contains(p) || infoButton.contains(p)) //if mouse inside either button
                    owner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                else
                    owner.setCursor(Cursor.getDefaultCursor());
            }
        });

        menuFace = new Rectangle(new Point(0,0),area);
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        startButton = new Rectangle(btnDim);
        menuButton = new Rectangle(btnDim);

        customButton = new Rectangle(btnDim);
        scoreButton = new Rectangle(btnDim);

        imageButton = new Rectangle(new Dimension(90,90));
        infoButton = new Rectangle( new Dimension(area.width / 4, area.height / 6));

        borderStoke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        greetingsFont = new Font("Noto Mono",Font.BOLD,25);
        gameTitleFont = new Font("Noto Mono",Font.BOLD,40);
        creditsFont = new Font("Noto Mono",Font.BOLD,18);
        buttonFont = new Font("Monospaced",Font.BOLD,startButton.height-2);
        textFont = new Font("Noto Mono",Font.BOLD,18);
    }

    /**
     * This method is responsible for painting and repainting the HomeMenu.
     * @param g This graphics class is used to paint the HomeMenu.
     */
    public void paint(Graphics g){
        try {
            drawMenu((Graphics2D)g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for painting and repainting the HomeMenu.
     * @param g2d This graphics class is used to paint the HomeMenu.
     * @throws IOException This IOException is thrown if the image for the PopUpMenu does not exist.
     */
    public void drawMenu(Graphics2D g2d) throws IOException {

        drawContainer(g2d);
        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
         */
        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = menuFace.getX();
        double y = menuFace.getY();

        g2d.translate(x,y); //move all points for processing

        drawText(g2d);
        drawButton(g2d);
        drawImage(g2d);

        g2d.translate(-x,-y); //move points back
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    /**
     * This method is responsible for painting and repainting the menu face of the HomeMenu.
     * @param g2d This graphics class is used to paint the menu face of the HomeMenu.
     */
    private void drawContainer(Graphics2D g2d) {

        Color prev = g2d.getColor();
        Stroke tmp = g2d.getStroke();

        g2d.drawImage(newImage,0,0,null);

        g2d.setStroke(borderStoke_noDashes);
        g2d.setColor(DASH_BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(borderStoke);
        g2d.setColor(BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(tmp);
        g2d.setColor(prev);
    }

    /**
     * This method is responsible for painting and repainting the text of the HomeMenu.
     * @param g2d This graphics class is used to paint the text of the HomeMenu.
     */
    private void drawText(Graphics2D g2d){

        g2d.setColor(TEXT_COLOR);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D greetingsRect = greetingsFont.getStringBounds(GREETINGS,frc);
        Rectangle2D gameTitleRect = gameTitleFont.getStringBounds(GAME_TITLE,frc);
        Rectangle2D creditsRect = creditsFont.getStringBounds(CREDITS,frc);

        int sX,sY;

        sX = (int)(menuFace.getWidth() - greetingsRect.getWidth()) / 2;
        sY = (int)(menuFace.getHeight() / 5);

        g2d.setFont(greetingsFont);
        g2d.drawString(GREETINGS,sX,sY);

        sX = (int)(menuFace.getWidth() - gameTitleRect.getWidth()) / 2;
        sY += (int) gameTitleRect.getHeight() * 0.8;

        g2d.setFont(gameTitleFont);
        g2d.drawString(GAME_TITLE,sX,sY);

        sX = (int)(menuFace.getWidth() - creditsRect.getWidth()) / 2;
        sY += (int) creditsRect.getHeight() * 1.1;

        g2d.setFont(creditsFont);
        g2d.drawString(CREDITS,sX,sY);

        g2d.setFont(textFont);

        sX = (menuFace.width - startButton.width) / 12;
        sY =(int) ((menuFace.height - startButton.height) * 0.6);

        sX = sX - 15;
        sY += imageButton.height + 18;
        g2d.drawString("Random Level",sX,sY);

        sX = sX + 18;
        sY += 15;
        g2d.drawString("Showcase",sX,sY);

        sX = (menuFace.width - startButton.width) / 12 + 5;
        sY =(int) ((menuFace.height - startButton.height) * 0.6) - 5;
        g2d.drawString("Click me!",sX,sY);

        sX = (menuFace.width - startButton.width) / 12 + 2 * startButton.width + 10;
        sY =(int) ((menuFace.height - startButton.height) * 0.6) + startButton.height + 8;
        g2d.drawString("Click me!",sX,sY);
    }

    /**
     * This method is responsible for painting and repainting the buttons of the HomeMenu.
     * @param g2d This graphics class is used to paint the buttons of the HomeMenu.
     */
    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);
        Rectangle2D mTxtRect = buttonFont.getStringBounds(MENU_TEXT,frc);

        Rectangle2D hTxtRect = buttonFont.getStringBounds(SCORE_TEXT,frc);
        Rectangle2D cTxtRect = buttonFont.getStringBounds(CUSTOM_TEXT,frc);
        Rectangle2D iTxtRect = buttonFont.getStringBounds(INFO_TEXT,frc);

        g2d.setFont(buttonFont);

        int x = (menuFace.width - startButton.width) / 2;
        int y =(int) ((menuFace.height - startButton.height) * 0.6);

        startButton.setLocation(x,y);

        x = (int)(startButton.getWidth() - txtRect.getWidth()) / 2;
        y = (int)(startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);

        if(startClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(startButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(START_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(startButton);
            g2d.drawString(START_TEXT,x,y);
        }

        x = startButton.x;
        y = startButton.y;

        y *= 1.2;

        customButton.setLocation(x,y);

        x = (int)(customButton.getWidth() - cTxtRect.getWidth()) / 2;
        y = (int)(customButton.getHeight() - cTxtRect.getHeight()) / 2;

        x += customButton.x;
        y += customButton.y + (startButton.height * 0.9);

        if(customClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(customButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(CUSTOM_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(customButton);
            g2d.drawString(CUSTOM_TEXT,x,y);
        }

        x = customButton.x;
        y = customButton.y;

        y *= 1.18;

        scoreButton.setLocation(x,y);

        x = (int)(scoreButton.getWidth() - hTxtRect.getWidth()) / 2;
        y = (int)(scoreButton.getHeight() - hTxtRect.getHeight()) / 2;

        x += scoreButton.x;
        y += scoreButton.y + (startButton.height * 0.9);

        if(scoreClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(scoreButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(SCORE_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(scoreButton);
            g2d.drawString(SCORE_TEXT,x,y);
        }

        x = scoreButton.x;
        y = scoreButton.y;

        y *= 1.15;

        menuButton.setLocation(x,y);

        x = (int)(menuButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(menuButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += menuButton.x;
        y += menuButton.y + (startButton.height * 0.9);

        if(menuClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(menuButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(MENU_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(menuButton);
            g2d.drawString(MENU_TEXT,x,y);
        }

        x = (menuFace.width - startButton.width) / 12;
        y =(int) ((menuFace.height - startButton.height) * 0.6);

        x += 2 * startButton.width - 5;
        y += 2 * startButton.height - 12;

        infoButton.setLocation(x,y);

        x = (int)(infoButton.getWidth() - iTxtRect.getWidth()) / 2;
        y = (int)(infoButton.getHeight() - iTxtRect.getHeight()) / 2;

        x += infoButton.x;
        y += infoButton.y + (startButton.height * 0.9);

        if(infoClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(infoButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(INFO_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(infoButton);
            g2d.drawString(INFO_TEXT,x,y);
        }

        x = (menuFace.width - startButton.width) / 12;
        y =(int) ((menuFace.height - startButton.height) * 0.6);

        imageButton.setLocation(x,y);
    }

    /**
     * This method is responsible for painting and repainting the images of the HomeMenu.
     * @param g2d This graphics class is used to paint the images of the HomeMenu.
     * @throws IOException This IOException is thrown if the image for the PopUpMenu does not exist.
     */
    private void drawImage(Graphics2D g2d) throws IOException {

        Random rnd = new Random();
        int rand = rnd.nextInt(20);

        if(popupmenu.getComponentCount()!=0){
            popupmenu.remove(0);
        }

        JMenuItem jMenuItem = new JMenuItem(new ImageIcon("image/ImageShowcase/BrickDestroy"+ rand +".png"));
        popupmenu.add(jMenuItem);

        BufferedImage myPicture = ImageIO.read(new File("image/ImageShowcase/BrickDestroy"+ rand +".png"));
        Image newImage = myPicture.getScaledInstance(90, 90, Image.SCALE_DEFAULT);

        int x = (menuFace.width - startButton.width) / 12;
        int y =(int) ((menuFace.height - startButton.height) * 0.6);

        g2d.drawImage(newImage,x,y,this);
    }
}
