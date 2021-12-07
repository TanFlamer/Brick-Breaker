
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
     * @throws IOException This constructor throws IOException if the image for InfoScreen do not exist.
     */
    public InfoScreen(JFrame owner,HomeMenu homeMenu,GameSounds gameSounds) throws IOException {

        this.owner = owner;
        this.homeMenu = homeMenu;
        initialize(gameSounds);

        panel.setLayout(new GridBagLayout()); //set layout
        GridBagConstraints constraints = new GridBagConstraints();

        makeLabels("Welcome to Brick Destroy Version 0.1",constraints, 0);

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
        makeLabels("Customise - customise all 4 levels of the game",constraints, 8);
        makeLabels("HighScores - view highscores for all levels and total highscores",constraints, 9);
        makeLabels("Exit - close and exit game",constraints, 10);
        makeLabels("Random Level Showcase - click to enlarge, will load 10 different sample levels each click",constraints, 11);
        makeLabels("Info - get an overview of the game",constraints, 12);

        makeLabels("--------------------------------------------",constraints, 13);

        makeLabels("Controls for the game are listed below.",constraints, 14);
        makeLabels("SPACEBAR - start and stop the game",constraints, 15);
        makeLabels("A Key - move player left",constraints, 16);
        makeLabels("D Key - move player right",constraints, 17);
        makeLabels("ESC Key - show pause menu",constraints, 18);
        makeLabels("F1 + Alt + SHIFT - show debug console",constraints, 19);

        makeLabels("-----------------------------------",constraints, 20);

        menuPic = ImageIO.read(new File("image/PauseMenu.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 21;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 22);

        makeLabels("This is the pause menu. There are 3 buttons on the screen which are Continue, Restart and Exit.",constraints, 23);
        makeLabels("Continue - go back to game",constraints, 24);
        makeLabels("Restart - restart the current level",constraints, 25);
        makeLabels("Exit - closes and exit game",constraints, 26);

        makeLabels("--------------------------------------------",constraints, 27);

        menuPic = ImageIO.read(new File("image/DebugConsole.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 28;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 29);

        makeLabels("This is the debug console. There are 4 buttons and 2 sliders. ",constraints, 30);
        makeLabels("The 4 buttons are Skip Level, Reset Balls, Previous Level and Reset Position.",constraints, 31);
        makeLabels("The 2 sliders control ball speed.",constraints, 32);

        makeLabels("--------------------------------------------",constraints, 33);

        makeLabels("Skip Level - skip to next level, will not work on last level",constraints, 34);
        makeLabels("Reset Balls - reset ball count to full",constraints, 35);
        makeLabels("Previous Level - go back to previous level, will not work on first level",constraints, 36);
        makeLabels("Reset Position - reset player and ball position",constraints, 37);
        makeLabels("Left Slider - controls ball horizontal velocity",constraints, 38);
        makeLabels("Right Slider - controls ball vertical velocity",constraints, 39);

        makeLabels("--------------------------------------------",constraints, 40);

        menuPic = ImageIO.read(new File("image/CustomConsole.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 41;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 42);

        makeLabels("This is the custom console. There are 10 combo boxes and 6 buttons.",constraints, 43);
        makeLabels("The 10 combo boxes are Level, Number of Balls, Level Generation, Rows of Bricks, Bricks in a Row, Types of Bricks, Brick 1 - 4.",constraints, 44);
        makeLabels("The 6 buttons are Reset, Save, True Random, Ordered (Minimal), Default and Randomise All.",constraints, 45);

        makeLabels("--------------------------------------------",constraints, 46);

        makeLabels("Level - choose which level to customise",constraints, 47);
        makeLabels("Number of Balls - controls number of ball in each level, goes from 1 to 10, default is 3",constraints, 48);
        makeLabels("Level Generation - controls type of level",constraints, 49);
        makeLabels("Rows of Bricks - controls number of rows of bricks, goes from 1 to 10",constraints, 50);
        makeLabels("Bricks in a Row - controls number of bricks in a even row, goes from 1, 2, 3, 4, 5, 6, 8, 10, 12 and 15",constraints, 51);
        makeLabels("Types of Bricks - controls the type of bricks that spawn, goes from 1 to 4",constraints, 52);
        makeLabels("Brick 1 - 4 - chooses which type of brick to spawn from Clay, Steel, Cement and Concrete",constraints, 53);

        makeLabels("--------------------------------------------",constraints, 54);

        makeLabels("Reset - resets all combo boxes to default choice",constraints, 55);
        makeLabels("Save - saves custom settings for the current level",constraints, 56);
        makeLabels("True Random - sets all 4 levels to True Random setting",constraints, 57);
        makeLabels("Ordered (Min) - sets all 4 levels to Ordered (Min) setting",constraints, 58);
        makeLabels("Default - sets all 4 levels to Default setting",constraints, 59);
        makeLabels("Randomise All - randomise all combo box settings for all 4 levels",constraints, 60);

        makeLabels("--------------------------------------------",constraints, 61);

        menuPic = ImageIO.read(new File("image/LevelGen.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 62;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 63);

        makeLabels("These are the options for the Level Generation combo box.",constraints, 64);
        makeLabels("There are 3 main types of settings which are Default, Ordered and Random.",constraints, 65);

        makeLabels("--------------------------------------------",constraints, 66);

        makeLabels("Default - default level of the game",constraints, 67);
        makeLabels("Ordered - bricks are arranged in a cyclic manner",constraints, 68);
        makeLabels("Random - bricks are generated randomly",constraints, 69);

        makeLabels("--------------------------------------------",constraints, 70);

        makeLabels("There are 9 options which are Default, True Ordered, Ordered (Maximal), Ordered (Moderate), Ordered (Minimal),",constraints, 71);
        makeLabels("Random (Minimal), Random (Moderate), Random (Maximal) and True Random.",constraints, 72);

        makeLabels("--------------------------------------------",constraints, 73);

        makeLabels("Default - does not allow user to customise anything, disables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 74);
        makeLabels("True Ordered - allows the user to fully customise the level, enables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 75);
        makeLabels("Ordered (Max) - allows the user to customise everything except choosing which type of bricks spawn, enables Rows of Bricks,",constraints, 76);
        makeLabels("Bricks in a Row, Types of Bricks and disables Brick 1 - 4",constraints, 77);
        makeLabels("Ordered (Mid) - allows the user to customise everything except brick spawn, enables Rows of Bricks,",constraints, 78);
        makeLabels("Bricks in a Row and disables Types of Bricks, Brick 1 - 4",constraints, 79);
        makeLabels("Ordered (Min) - does not allow user to customise anything, disables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 80);
        makeLabels("Random (Min) - allows the user to fully customise the level, enables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 81);
        makeLabels("Random (Mid) - allows the user to customise everything except choosing which type of bricks spawn, enables Rows of Bricks,",constraints, 82);
        makeLabels("Bricks in a Row, Types of Bricks and disables Brick 1 - 4",constraints, 83);
        makeLabels("Random (Max) - allows the user to customise everything except brick spawn, enables Rows of Bricks, Bricks in a Row",constraints, 84);
        makeLabels("and disables Types of Bricks, Brick 1 - 4",constraints, 85);
        makeLabels("True Random - does not allow user to customise anything, disables Rows of Bricks, Bricks in a Row, Types of Bricks and Brick 1 - 4",constraints, 86);

        makeLabels("--------------------------------------------",constraints, 87);

        makeLabels("There are 4 levels in this game.The goal of this game is to destroy all the bricks on the screen using the ball.",constraints, 88);
        makeLabels("The ball bounces off everything including the player, bricks and page borders.",constraints, 90);
        makeLabels("The only exception is the lower page border The ball count decreases when the ball leaves the lower page border.",constraints, 91);
        makeLabels("When all count reaches 0, the level and score are reset. Destroying bricks awards players with points.",constraints, 92);
        makeLabels("The point are calculated using the formula score = brick multiplier * number of bricks in an even row.",constraints, 93);

        makeLabels("--------------------------------------------",constraints, 94);

        makeLabels("Brick multiplier for different bricks are given below.",constraints, 95);
        makeLabels("Clay - 10 | Steel - 40 | Cement - 40 | Concrete - 100",constraints, 96);

        makeLabels("--------------------------------------------",constraints, 97);

        menuPic = ImageIO.read(new File("image/BrickTypes.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 98;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 99);

        makeLabels("From left to right, we have clay brick, steel brick, cement brick and concrete brick.",constraints, 100);
        makeLabels("Characteristics of the 4 bricks are given below.",constraints, 101);

        makeLabels("--------------------------------------------",constraints, 102);

        makeLabels("Clay - destroyed in 1 hit",constraints, 103);
        makeLabels("Cement - destroyed in 2 hits, will form crack after 1st hit",constraints, 104);
        makeLabels("Steel - destroyed with a 40% probability when ball hits",constraints, 105);
        makeLabels("Concrete - destroyed in 2 hits, each with a 40% probability when ball hits",constraints, 106);

        makeLabels("--------------------------------------------",constraints, 107);

        menuPic = ImageIO.read(new File("image/HighscoreBoard.png"));
        image = new JLabel(new ImageIcon(menuPic));
        constraints.gridx = 0;
        constraints.gridy = 108;
        panel.add(image,constraints);

        makeLabels("--------------------------------------------",constraints, 109);

        makeLabels("This is the highscore board. There are 3 buttons on the page which are PREVIOUS, BACK and NEXT.",constraints, 110);
        makeLabels("There are 5 highscore pages, 1 for each of the 4 levels and 1 for the total highscores.",constraints, 111);

        makeLabels("--------------------------------------------",constraints, 112);

        makeLabels("PREVIOUS - go to previous highscore page",constraints, 113);
        makeLabels("BACK - go back to main menu",constraints, 114);
        makeLabels("NEXT - go to next highscore page",constraints, 115);

        makeLabels("--------------------------------------------",constraints, 116);

        makeLabels("Beat all 4 levels and try to get the highscore for each individual level and the total highscore in the shortest time!",constraints, 117);

        JScrollPane mainScreen = new JScrollPane(panel);
        mainScreen.getViewport().setPreferredSize(new Dimension(760, 500));
        this.add(mainScreen);
        this.setSize(300,300);
        this.pack();
    }

    /**
     * This method is used to set the InfoScreen title and to add in window listeners.
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
