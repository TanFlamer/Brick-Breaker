package Main.Others;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Public class InfoScreen is used to display information about the game and explains all the different
 * functionalities of the game so that the player may better enjoy the game.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class InfoScreen extends JDialog {

    /**
     * Title string.
     */
    private static final String TITLE = "Game Info";
    /**
     * JFrame used to center the InfoScreen.
     */
    private final JFrame owner;
    /**
     * HomeMenu which is repainted when InfoScreen closes.
     */
    private final HomeMenu homeMenu;
    /**
     * JPanel which is loaded with labels and images and which in turn is loaded into a JScrollPane for viewing.
     */
    JPanel panel = new JPanel();

    /**
     * This constructor is called to create an info screen with all the relevant information about the game.
     * The info screen is filled with labels with text and images and is loaded into a JScrollPane for viewing.
     *
     * @param owner JFrame used to center the InfoScreen.
     * @param homeMenu HomeMenu which is repainted when InfoScreen closes.
     * @param gameSounds GameSounds to control BGM when InfoScreen opens and closes.
     * @throws IOException This constructor throws IOException if the image for InfoScreen do not exist.
     */
    public InfoScreen(JFrame owner,HomeMenu homeMenu,GameSounds gameSounds) throws IOException {

        this.owner = owner;
        this.homeMenu = homeMenu;
        initialize(gameSounds);

        panel.setLayout(new GridBagLayout()); //set layout
        GridBagConstraints constraints = new GridBagConstraints();

        makeLabels("Welcome to Brick Destroy Version 1.0",constraints, 0);

        makeLabels("--------------------------------------------",constraints, 1);

        BufferedImage menuPic = ImageIO.read(new File("image/MainMenu.png"));
        JLabel image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 3);

        makeLabels("This is the main menu screen. There are 6 buttons on the main menu",constraints, 4);
        makeLabels("which are Start, Customise, HighScores, Exit, Random Level Showcase and Info.",constraints, 5);

        makeLabels("----------------------------------------",constraints, 6);

        makeLabels("Start - enters into the main game",constraints, 7);
        makeLabels("Customise - customise all 5 levels of the game",constraints, 8);
        makeLabels("HighScores - view top 10 highscores for all levels and total highscores",constraints, 9);
        makeLabels("Exit - close and exit game",constraints, 10);
        makeLabels("Random Level Showcase - click to enlarge, will load 20 different sample levels each click",constraints, 11);
        makeLabels("Info - get an overview of the game",constraints, 12);

        makeLabels("--------------------------------------------",constraints, 13);

        makeLabels("Controls for the game are listed below.",constraints, 14);
        makeLabels("SPACEBAR - start and stop the game",constraints, 15);
        makeLabels("W Key - move player up (available in free movement mode)",constraints, 16);
        makeLabels("S Key - move player down (available in free movement mode)",constraints, 17);
        makeLabels("A Key - move player left",constraints, 18);
        makeLabels("D Key - move player right",constraints, 19);
        makeLabels("UP Arrow Key - increase ball upwards speed (available in free movement mode)",constraints, 20);
        makeLabels("DOWN Arrow Key - increase ball downwards speed (available in free movement mode)",constraints, 21);
        makeLabels("LEFT Arrow Key - increase ball leftwards speed (available in free movement mode)",constraints, 22);
        makeLabels("RIGHT Arrow Key - increase ball rightwards speed (available in free movement mode)",constraints, 23);
        makeLabels("ESC Key - show pause menu",constraints, 24);
        makeLabels("F1 + Alt + SHIFT - show debug console",constraints, 25);

        makeLabels("-----------------------------------",constraints, 26);

        menuPic = ImageIO.read(new File("image/PauseMenu.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 27;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 28);

        makeLabels("This is the pause menu. There are 3 buttons on the screen which are Continue, Restart and Exit.",constraints, 29);
        makeLabels("Continue - go back to game",constraints, 30);
        makeLabels("Restart - restart the current level",constraints, 31);
        makeLabels("Exit - closes and exit game",constraints, 32);

        makeLabels("--------------------------------------------",constraints, 33);

        menuPic = ImageIO.read(new File("image/DebugConsole.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 34;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 35);

        makeLabels("This is the debug console. There are 4 buttons and 2 sliders. ",constraints, 36);
        makeLabels("The 4 buttons are Skip Level, Reset Balls, Previous Level and Reset Position.",constraints, 37);
        makeLabels("The 2 sliders control ball speed.",constraints, 38);

        makeLabels("--------------------------------------------",constraints, 39);

        makeLabels("Skip Level - skip to next level, will not work on last level",constraints, 40);
        makeLabels("Reset Balls - reset ball count to full",constraints, 41);
        makeLabels("Previous Level - go back to previous level, will not work on first level",constraints, 42);
        makeLabels("Reset Position - reset player and ball position",constraints, 43);
        makeLabels("Left Slider - controls ball horizontal velocity",constraints, 44);
        makeLabels("Right Slider - controls ball vertical velocity",constraints, 45);

        makeLabels("--------------------------------------------",constraints, 46);

        menuPic = ImageIO.read(new File("image/CustomConsole.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 47;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 48);

        makeLabels("This is the custom console. There are 12 combo boxes and 6 buttons.",constraints, 49);
        makeLabels("The 12 combo boxes are Level, Free Movement, Player Position, Number of Balls,",constraints, 50);
        makeLabels("Level Generation, Rows of Bricks, Bricks in a Row, Types of Bricks, Brick 1 - 4.",constraints, 51);
        makeLabels("The 6 buttons are Reset, Save, True Random, Ordered (Minimal), Default and Randomise All.",constraints, 52);

        makeLabels("--------------------------------------------",constraints, 53);

        makeLabels("Level - choose which level to customise",constraints, 54);
        makeLabels("Number of Balls - controls number of ball in each level, goes from 1 to 10, default is 3",constraints, 55);
        makeLabels("Free Movement - controls player and ball movement, considered a cheat and score and time are not counted, default is disabled",constraints, 56);//renumber
        makeLabels("Player Position - controls player starting position, top or bottom, default is bottom",constraints, 57);
        makeLabels("Level Generation - controls type of level",constraints, 58);
        makeLabels("Rows of Bricks - controls number of rows of bricks, goes from 1 to 10",constraints, 59);
        makeLabels("Bricks in a Row - controls number of bricks in a even row, goes from 1, 2, 3, 4, 5, 6, 8, 10, 12 and 15",constraints, 60);
        makeLabels("Types of Bricks - controls the type of bricks that spawn, goes from 1 to 4",constraints, 61);
        makeLabels("Brick 1 - 4 - chooses which type of brick to spawn from Clay, Steel, Cement and Concrete",constraints, 62);

        makeLabels("--------------------------------------------",constraints, 63);

        makeLabels("Reset - resets all combo boxes to default choice",constraints, 64);
        makeLabels("Save - saves custom settings for the current level",constraints, 65);
        makeLabels("True Random - sets all 5 levels to True Random setting",constraints, 66);
        makeLabels("Ordered (Min) - sets all 5 levels to Ordered (Min) setting",constraints, 67);
        makeLabels("Default - sets all 5 levels to Default setting",constraints, 68);
        makeLabels("Randomise All - randomise all combo box settings for all 5 levels",constraints, 69);

        makeLabels("--------------------------------------------",constraints, 70);

        menuPic = ImageIO.read(new File("image/LevelGen.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 71;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 72);

        makeLabels("These are the options for the Level Generation combo box.",constraints, 73);
        makeLabels("There are 3 main types of settings which are Default, Ordered and Random.",constraints, 74);

        makeLabels("--------------------------------------------",constraints, 75);

        makeLabels("Default - default level of the game",constraints, 76);
        makeLabels("Ordered - bricks are arranged in a cyclic manner",constraints, 77);
        makeLabels("Random - bricks are generated randomly",constraints, 78);

        makeLabels("--------------------------------------------",constraints, 79);

        makeLabels("There are 9 options which are Default, True Ordered, Ordered (Max), Ordered (Mid), Ordered (Min),",constraints, 80);
        makeLabels("Random (Min), Random (Mid), Random (Max) and True Random.",constraints, 81);

        makeLabels("--------------------------------------------",constraints, 82);

        makeLabels("Default - does not allow user to customise anything, disables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 83);
        makeLabels("True Ordered - allows the user to fully customise the level, enables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 84);
        makeLabels("Ordered (Max) - allows the user to customise everything except choosing which type of bricks spawn, enables Rows of Bricks,",constraints, 85);
        makeLabels("Bricks in a Row, Types of Bricks and disables Brick 1 - 4",constraints, 86);
        makeLabels("Ordered (Mid) - allows the user to customise everything except brick spawn, enables Rows of Bricks,",constraints, 87);
        makeLabels("Bricks in a Row and disables Types of Bricks, Brick 1 - 4",constraints, 88);
        makeLabels("Ordered (Min) - does not allow user to customise anything, disables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 89);
        makeLabels("Random (Min) - allows the user to fully customise the level, enables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 90);
        makeLabels("Random (Mid) - allows the user to customise everything except choosing which type of bricks spawn, enables Rows of Bricks,",constraints, 91);
        makeLabels("Bricks in a Row, Types of Bricks and disables Brick 1 - 4",constraints, 92);
        makeLabels("Random (Max) - allows the user to customise everything except brick spawn, enables Rows of Bricks, Bricks in a Row",constraints, 93);
        makeLabels("and disables Types of Bricks, Brick 1 - 4",constraints, 94);
        makeLabels("True Random - does not allow user to customise anything, disables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 95);

        makeLabels("--------------------------------------------",constraints, 96);

        makeLabels("There are 5 levels in this game.The goal of this game is to destroy all the bricks on the screen using the ball.",constraints, 97);
        makeLabels("The ball bounces off everything including the player, bricks and page borders.",constraints, 98);
        makeLabels("The only exception is the starting page border for both player positions. The ball count decreases when the ball leaves",constraints, 99);
        makeLabels("the starting page border. When ball count reaches 0, the level and score are reset. Destroying bricks awards players with points.",constraints, 100);
        makeLabels("The point are calculated using the formula score = brick multiplier * number of bricks in an even row.",constraints, 101);

        makeLabels("--------------------------------------------",constraints, 102);

        makeLabels("Brick multiplier for different bricks are given below.",constraints, 103);
        makeLabels("Clay - 10 | Steel - 40 | Cement - 40 | Concrete - 100",constraints, 104);

        makeLabels("--------------------------------------------",constraints, 105);

        menuPic = ImageIO.read(new File("image/Counters.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 106;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 107); //renumber

        makeLabels("These are the counters for the game.",constraints, 108);
        makeLabels("They keep track of the brick and ball count, level and total score and time",constraints, 109);
        makeLabels("and also the God Mode orbs and time left.",constraints, 110);

        makeLabels("--------------------------------------------",constraints, 111);

        menuPic = ImageIO.read(new File("image/BrickTypes.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 112;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 113);

        makeLabels("From left to right, we have clay brick, steel brick, cement brick and concrete brick.",constraints, 114);
        makeLabels("Characteristics of the 4 bricks are given below.",constraints, 115);

        makeLabels("--------------------------------------------",constraints, 116);

        makeLabels("Clay - destroyed in 1 hit",constraints, 117);
        makeLabels("Cement - destroyed in 2 hits, will form crack after 1st hit",constraints, 118);
        makeLabels("Steel - destroyed with a 40% probability when ball hits",constraints, 119);
        makeLabels("Concrete - destroyed in 2 hits, each with a 40% probability when ball hits",constraints, 120);

        makeLabels("--------------------------------------------",constraints, 121);

        menuPic = ImageIO.read(new File("image/GodModePowerUp.png")); //renumber
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 122;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 123);

        makeLabels("This is the God Mod Power Up. One orb is spawned every minute.",constraints, 124);
        makeLabels("Collecting this orb allows the player to enter God Mode for 10 seconds.",constraints, 125);
        makeLabels("The ball will not deflect on collision with brick and will continue moving until brick is destroyed",constraints, 126);
        makeLabels("The ball will also bounce off the starting page border and cannot lose during that 10 seconds.",constraints, 127);

        makeLabels("--------------------------------------------",constraints, 128);

        menuPic = ImageIO.read(new File("image/HighscoreBoard.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 129;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 130);

        makeLabels("This is the highscore board. There are 3 buttons on the page which are PREVIOUS, BACK and NEXT.",constraints, 131);
        makeLabels("There are 6 highscore pages, 1 for each of the 5 levels and 1 for the total highscores.",constraints, 132);

        makeLabels("--------------------------------------------",constraints, 133);

        makeLabels("PREVIOUS - go to previous highscore page",constraints, 134);
        makeLabels("BACK - go back to main menu",constraints, 135);
        makeLabels("NEXT - go to next highscore page",constraints, 136);

        makeLabels("--------------------------------------------",constraints, 137);

        makeLabels("Beat all 5 levels and try to get the highscore for each individual level and the total highscore in the shortest time!",constraints, 138);

        JScrollPane mainScreen = new JScrollPane(panel);
        mainScreen.getViewport().setPreferredSize(new Dimension(760, 500));
        this.add(mainScreen);
        this.setSize(300,300);
        this.pack();
    }

    /**
     * This method is used to set the InfoScreen title and to add in window listeners.
     * @param gameSounds This is used to add BGM to InfoScreen.
     */
    private void initialize(GameSounds gameSounds) {

        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.addWindowListener(new WindowAdapter() {
            /**
             * This window listener is called when the InfoScreen is closing and repaints the HomeMenu.
             * @param windowEvent This parameter is used to track the InfoScreen window.
             */
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                homeMenu.repaint();
                gameSounds.setBgm("Menu");
            }

            /**
             * This window listener is called when the InfoScreen is activated and centers the InfoScreen on
             * the screen.
             *
             * @param windowEvent This parameter is used to track the InfoScreen window.
             */
            @Override
            public void windowActivated(WindowEvent windowEvent) { //when debug console loaded
                setLocation(); //set debug console location
                gameSounds.setBgm("Info");
            }
        });

        this.setFocusable(true);
    }

    /**
     * This method is used to center the CustomConsole by using the JFrame location.
     */
    private void setLocation(){ //set debug console location
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }

    /**
     * This method creates JLabels with text which are loaded into a JPanel which is then loaded into a JScrollPane.
     * @param label The text on the JLabels.
     * @param constraints The constraints on the position of the JLabel.
     * @param y The y-position of the JLabel.
     */
    private void makeLabels(String label, GridBagConstraints constraints, int y){ //make buttons and add listener
        constraints.gridx = 0;
        constraints.gridy = y;
        panel.add(new JLabel(label),constraints);
    }
}
