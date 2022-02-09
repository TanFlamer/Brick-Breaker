package Main.Consoles;
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
import Main.MVC.GameBoardController;
import Main.MVC.GameEngine;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Public class DebugPanel is used to call methods to reset ball count, reset ball and player position, move to
 * previous or next level and set the ball movement speed.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class DebugPanel extends JPanel {

    /**
     * Background colour of white for the DebugPanel.
     */
    private static final Color DEF_BKG = Color.WHITE;

    /**
     * JSlider to set ball horizontal speed.
     */
    private JSlider ballXSpeed;
    /**
     * JSlider to set ball vertical speed.
     */
    private JSlider ballYSpeed;
    /**
     * Controller to get methods to add functionality to the debug options.
     */
    private final GameBoardController controller;

    private final JButton resetBalls;

    private final JButton resetPosition;

    /**
     * This constructor is used to initialize the DebugPanel and add the JButtons and JSliders.
     * @param gameEngine GameEngine to fetch the controller to add functionality to the debug options.
     */
    public DebugPanel(GameEngine gameEngine){

        this.controller = gameEngine.getController();
        initialize();

        JButton skipLevel = makeButton("Skip Level", e -> controller.nextLevel(false));
        JButton previousLevel = makeButton("Previous Level", e -> controller.previousLevel());

        resetBalls = makeButton("Reset Balls", e -> controller.resetBallCount());
        resetPosition = makeButton("Reset Position", e -> controller.ballReset());

        ballXSpeed = makeSlider(e -> gameEngine.getGameBoard().getBalls()[0].setSpeedX(ballXSpeed.getValue()));
        ballYSpeed = makeSlider(e -> gameEngine.getGameBoard().getBalls()[0].setSpeedY(ballYSpeed.getValue()));

        this.add(skipLevel);
        this.add(resetBalls);

        this.add(previousLevel);
        this.add(resetPosition);

        this.add(ballXSpeed);
        this.add(ballYSpeed);
    }

    /**
     * This method sets the DebugPanel background to white and sets the layout to GridLayout.
     */
    private void initialize(){
        this.setBackground(DEF_BKG);
        this.setLayout(new GridLayout(3,2));
    }

    /**
     * This method is used to add the given string to the JButton and to add action listeners to the JButtons.
     * @param title This is the text on the JButton.
     * @param e This is the action listener assigned to the JButton.
     * @return This method returns a JButton with the given string which is assigned an action listener.
     */
    private JButton makeButton(String title, ActionListener e){
        JButton out = new JButton(title);
        out.addActionListener(e);
        return out;
    }

    /**
     * This method is used to make JSliders with the given min and max values and to add action listeners to the
     * JSliders.
     *
     * @param e This is the action listener assigned to the JSlider.
     * @return This method returns a JSlider with the given min and max values which is assigned an action listener.
     */
    private JSlider makeSlider(ChangeListener e){
        JSlider out = new JSlider(-4, 4);
        out.setMajorTickSpacing(1);
        out.setSnapToTicks(true);
        out.setPaintTicks(true);
        out.addChangeListener(e);
        return out;
    }

    /**
     * This method sets the JSlider values to the current speed of the ball.
     * @param x This is the current horizontal speed of the ball.
     * @param y This is the current vertical speed of the ball.
     */
    public void setValues(int x,int y){
        ballXSpeed.setValue(x);
        ballYSpeed.setValue(y);
    }

    public void enableSlider(boolean bool){
        ballXSpeed.setEnabled(bool);
        ballYSpeed.setEnabled(bool);
    }

    public void enableButton(boolean bool){
        resetBalls.setEnabled(bool);
        resetPosition.setEnabled(bool);
    }
}
