package Main;/*
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

/**
 * Public class DebugConsole is the container for the DebugPanel. Window listeners are added to this public class
 * to repaint GameBoard when DebugConsole closes or set DebugPanel JSlider values and centers the screen when
 * DebugConsole opens.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class DebugConsole extends JDialog {

    /**
     * Title for the DebugConsole.
     */
    private static final String TITLE = "Debug Console";

    /**
     * JFrame which is used to center the DebugConsole.
     */
    private final JFrame owner;
    /**
     * The DebugPanel which is contained within DebugConsole.
     */
    private final DebugPanel debugPanel;
    /**
     * GameBoard which used to get ball speed for the JSlider.
     */
    private final GameBoard gameBoard;

    /**
     * This constructor is used to load the DebugPanel into the DebugConsole and to add in window listeners.
     * @param owner JFrame which is used to center the DebugConsole.
     * @param gameEngine GameEngine to fetch the GameBoard.
     * @param gameSounds GameSounds to control BGM when DebugConsole opens and closes.
     */
    public DebugConsole(JFrame owner, GameEngine gameEngine, GameSounds gameSounds){

        this.owner = owner;
        this.gameBoard = gameEngine.getGameBoard();
        initialize(gameSounds); //call debug console

        debugPanel = new DebugPanel(gameEngine);
        this.add(debugPanel,BorderLayout.CENTER); //add debug panel to center

        this.pack(); //resize debug console
    }

    /**
     * This method is used to set the DebugConsole title and to add in window listeners.
     * @param gameSounds This is used to add BGM to DebugConsole.
     */
    private void initialize(GameSounds gameSounds){ //call debug console

        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.addWindowListener(new WindowAdapter() {
            /**
             * This window listener is called when the DebugConsole is closing and repaints the GameBoard.
             * @param windowEvent This parameter is used to track the DebugConsole window.
             */
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                gameSounds.getBgm().stop();
            }

            /**
             * This window listener is called when the DebugConsole is activated and centers the DebugConsole on
             * the screen and sets the JSlider values in the DebugPanel.
             *
             * @param windowEvent This parameter is used to track the DebugConsole window.
             */
            @Override
            public void windowActivated(WindowEvent windowEvent) { //when debug console loaded
                setLocation(); //set debug console location
                Ball b = gameBoard.getBall(); //get ball
                debugPanel.setValues(b.getSpeedX(),b.getSpeedY()); //show current ball speed on slider
                gameSounds.setSongID(0);
                gameSounds.setBgm("Debug");
            }
        });
        this.setFocusable(true);
    }

    /**
     * This method is used to center the DebugConsole by using the JFrame location.
     */
    private void setLocation(){ //set debug console location
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }
}
