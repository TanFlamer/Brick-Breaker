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

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Public class GameFrame is responsible for loading between all the different screens in the game. These screens
 * include the HomeMenu, GameBoard, HighScore Board, Custom Console and Info Screen. When the game is launched,
 * the GameFrame first loads in the HomeMenu. The buttons on the HomeMenu are then clicked to access all the
 * other screens.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class GameFrame extends JFrame {

    /**
     * The game title.
     */
    private static final String DEF_TITLE = "Brick Destroy";

    private static final int MENU_WIDTH = 450;
    private static final int MENU_HEIGHT = 300;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 450;

    /**
     * The GameBoard to play the game, accessed from the HomeMenu.
     */
    private BrickBreaker brickBreaker;
    /**
     * The HomeMenu to access all other screens through buttons.
     */
    private final HomeMenu homeMenu;

    /**
     * The HighScore Board, accessed from the HomeMenu.
     */
    private final Highscore highscore;
    /**
     * The Custom Console to customise the levels, accessed from the HomeMenu.
     */
    private final CustomConsole customConsole;
    /**
     * The Info Screen to explain the different aspects of the game, accessed from the HomeMenu.
     */
    private final InfoScreen infoScreen;

    private final GameSounds gameSounds;

    /**
     * Boolean to signal that the game has lost focus.
     */
    private boolean gaming;

    int[][] choice;

    /**
     * This constructor loads the HighScore Board, Custom Console, HomeMenu and Info Screen. Then, the HomeMenu
     * is shown and the player is able to access all other screens through the buttons.
     *
     * @throws IOException This constructor throws IOException when the HighScore list or the InfoScreen photos
     *                     do not exist.
     */
    public GameFrame() throws IOException {
        super();

        gaming = false; //game loses focus

        this.setLayout(new BorderLayout()); //set layout

        gameSounds = new GameSounds();
        highscore = new Highscore(this, new Dimension(MENU_WIDTH, MENU_HEIGHT)); //get highscore
        homeMenu = new HomeMenu(this, new Dimension(MENU_WIDTH, MENU_HEIGHT)); //set main menu
        customConsole = new CustomConsole(this,homeMenu,gameSounds);
        infoScreen = new InfoScreen(this,homeMenu,gameSounds);
        this.add(homeMenu, BorderLayout.CENTER); //add main menu to centre
        gameSounds.setBgm("Menu");
        this.choice = customConsole.getChoice();
        this.setUndecorated(true); //set frame undecorated
    }

    /**
     * This method is called when the game starts to load. The game title is set, the game screen is centered
     * and the game screen is made visible.
     */
    public void initialize() { //initialize game
        this.setTitle(DEF_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.autoLocate(); //reposition screen
        this.setVisible(true);
    }

    /**
     * This method is called when the player clicks the start button on the HomeMenu. The HomeMenu is removed and
     * the GameBoard is loaded. The game screen is centered, the game is initialized and window focus listeners
     * are added.
     */
    public void enableGameBoard() { //start game
        this.dispose();
        this.remove(homeMenu); //remove main menu
        brickBreaker = new BrickBreaker(this,choice,gameSounds,new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.add(brickBreaker, BorderLayout.CENTER); //add main game
        this.setUndecorated(false);
        initialize(); //initialize game
        //to avoid problems with graphics focus controller is added here
        this.addWindowFocusListener(new WindowAdapter() {
            /**
             * This window focus listener is called when game screen gains focus and sets gaming flag to true.
             * @param windowEvent This parameter is used to track the game screen.
             */
            @Override
            public void windowGainedFocus(WindowEvent windowEvent) { //if game gains focus
                gaming = true; //set gaming flag true
                /*the first time the frame loses focus is because it has been disposed to install the GameBoard,
                  so went it regains the focus it's ready to play. of course calling a method such as 'onLostFocus'
                  is useful only if the GameBoard as been displayed at least once*/
                gameSounds.getBgm().start();
            }

            /**
             * This window focus listener is called when game screen loses focus and sets game board to lose focus.
             * @param windowEvent This parameter is used to track the game screen.
             */
            @Override
            public void windowLostFocus(WindowEvent windowEvent) { //if game loses focus
                if (gaming) //if gaming flag true
                    brickBreaker.onLostFocus(); //stop timer and action listener
                gameSounds.getBgm().stop();
            }
        });
    }

    /**
     * This method is used to center the game screen on the window screen.
     */
    void autoLocate() { //reposition screen
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / 2;
        int y = (size.height - this.getHeight()) / 2;
        this.setLocation(x, y);
    }

    /**
     * This method is called when the player clicks the HighScores button on the HomeMenu. The HomeMenu is removed and
     * the HighScore Board is loaded. The HighScore Board is centered and made visible.
     */
    public void enableHighscoreBoard() {
        this.dispose();
        this.remove(homeMenu); //remove main menu
        this.add(highscore, BorderLayout.CENTER); //add main game
        this.setUndecorated(true);
        this.setVisible(true);
        gameSounds.setBgm("Highscore");
    }

    /**
     * This method is called when the player clicks the back button on the HighScore Board. The HighScore Board is
     * removed and the HomeMenu Board is reloaded, re-centered and made visible.
     */
    public void enableHomeMenu() {
        this.dispose();
        this.remove(highscore); //remove main menu
        this.add(homeMenu, BorderLayout.CENTER); //add main game
        this.setUndecorated(true);
        this.setVisible(true);
        gameSounds.setBgm("Menu");
    }

    /**
     * This method is called when the player clicks the Customise button on the HomeMenu. The Custom Console is
     * set visible.
     */
    public void enableCustomConsole() {
        customConsole.setVisible(true);
    }

    /**
     * This method is called when the player clicks the Info button on the HomeMenu. The Info Screen is set visible.
     */
    public void enableInfoScreen() {
        infoScreen.setVisible(true);
    }
}