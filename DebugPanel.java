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
     * JButton to skip to next level.
     */
    private JButton skipLevel;
    /**
     * JButton to reset ball count.
     */
    private JButton resetBalls;

    /**
     * JButton to go to previous level.
     */
    private JButton previousLevel;
    /**
     * JButton to reset player and ball position.
     */
    private JButton resetPosition;

    /**
     * JSlider to set ball horizontal speed.
     */
    private JSlider ballXSpeed;
    /**
     * JSlider to set ball vertical speed.
     */
    private JSlider ballYSpeed;

    private GameBoardController controller;

    /**
     * This constructor is used to initialize the DebugPanel and add the JButtons and JSliders.
     *
     */
    public DebugPanel(GameEngine gameEngine){

        this.controller = gameEngine.getController();
        initialize(); //initialize debug panel

        skipLevel = makeButton("Skip Level",e -> controller.nextLevel(false)); //make new buttons
        resetBalls = makeButton("Reset Balls",e -> controller.resetBallCount());

        previousLevel = makeButton("Previous Level",e -> controller.previousLevel());
        resetPosition = makeButton("Reset Position",e -> controller.ballReset());

        ballXSpeed = makeSlider(-4,4,e -> gameEngine.getGameBoard().getBall().setSpeedX(ballXSpeed.getValue())); //make new sliders
        ballYSpeed = makeSlider(-4,4,e -> gameEngine.getGameBoard().getBall().setSpeedY(ballYSpeed.getValue())); //show and set speed of ball

        this.add(skipLevel); //add buttons
        this.add(resetBalls);

        this.add(previousLevel);
        this.add(resetPosition);

        this.add(ballXSpeed); //add sliders
        this.add(ballYSpeed);
    }

    /**
     * This method sets the DebugPanel background to white and sets the layout to GridLayout.
     */
    private void initialize(){ //initialize debug panel
        this.setBackground(DEF_BKG); //set background colour
        this.setLayout(new GridLayout(3,2)); //set layout
    }

    /**
     * This method is used to add the given string to the JButton and to add action listeners to the JButtons.
     * @param title This is the text on the JButton.
     * @param e This is the action listener assigned to the JButton.
     * @return This method returns a JButton with the given string which is assigned an action listener.
     */
    private JButton makeButton(String title, ActionListener e){ //make buttons and add listener
        JButton out = new JButton(title);
        out.addActionListener(e);
        return out;
    }

    /**
     * This method is used to make JSliders with the given min and max values and to add action listeners to the
     * JSliders.
     *
     * @param min This is the min value of the JSlider.
     * @param max This is the max value of the JSlider.
     * @param e This is the action listener assigned to the JSlider.
     * @return This method returns a JSlider with the given min and max values which is assigned an action listener.
     */
    private JSlider makeSlider(int min, int max, ChangeListener e){ //make sliders and add listener
        JSlider out = new JSlider(min,max);
        out.setMajorTickSpacing(1); //set tick spacing
        out.setSnapToTicks(true); //snap to tick
        out.setPaintTicks(true); //paint ticks
        out.addChangeListener(e);
        return out;
    }

    /**
     * This method sets the JSlider values to the current speed of the ball.
     * @param x This is the current horizontal speed of the ball.
     * @param y This is the current vertical speed of the ball.
     */
    public void setValues(int x,int y){ //show current ball speed on slider
        ballXSpeed.setValue(x);
        ballYSpeed.setValue(y);
    }
}
