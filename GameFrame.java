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
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;


public class GameFrame extends JFrame {

    private static final String DEF_TITLE = "Brick Destroy";

    private GameBoard gameBoard;
    private HomeMenu homeMenu;

    private Highscore highscore;
    private CustomConsole customConsole;

    private boolean gaming;

    public GameFrame() throws IOException {
        super();

        gaming = false; //game loses focus

        this.setLayout(new BorderLayout()); //set layout

        highscore = new Highscore(this,new Dimension(450,300)); //get highscore
        homeMenu = new HomeMenu(this,new Dimension(450,300)); //set main menu
        customConsole = new CustomConsole(this,homeMenu);
        this.add(homeMenu,BorderLayout.CENTER); //add main menu to centre

        this.setUndecorated(true); //set frame undecorated
    }

    public void initialize(){ //initialize game
        this.setTitle(DEF_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.autoLocate(); //reposition screen
        this.setVisible(true);
    }

    public void enableGameBoard() throws IOException { //start game
        this.dispose();
        this.remove(homeMenu); //remove main menu
        gameBoard = new GameBoard(this); //call game board
        this.add(gameBoard,BorderLayout.CENTER); //add main game
        this.setUndecorated(false);
        initialize(); //initialize game
        //to avoid problems with graphics focus controller is added here
        this.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(WindowEvent windowEvent) { //if game gains focus
                gaming = true; //set gaming flag true
                /*the first time the frame loses focus is because it has been disposed to install the GameBoard,
                  so went it regains the focus it's ready to play. of course calling a method such as 'onLostFocus'
                  is useful only if the GameBoard as been displayed at least once*/
            }

            @Override
            public void windowLostFocus(WindowEvent windowEvent) { //if game loses focus
                if(gaming) //if gaming flag true
                    gameBoard.onLostFocus(); //stop timer and action listener
            }
        }); //add listener
    }

    void autoLocate(){ //reposition screen
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / 2;
        int y = (size.height - this.getHeight()) / 2;
        this.setLocation(x,y);
    }

    public void enableScoreBoard(){
        this.dispose();
        this.remove(homeMenu); //remove main menu
        this.add(highscore,BorderLayout.CENTER); //add main game
        this.setUndecorated(true);
        this.setVisible(true);
    }

    public void enableHomeMenu(){
        this.dispose();
        this.remove(highscore); //remove main menu
        this.add(homeMenu,BorderLayout.CENTER); //add main game
        this.setUndecorated(true);
        this.setVisible(true);
    }

    public void enableCustomConsole(){
        customConsole.setVisible(true);
    }
}