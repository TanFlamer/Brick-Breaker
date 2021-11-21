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
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;



public class DebugPanel extends JPanel {

    private static final Color DEF_BKG = Color.WHITE;


    private JButton skipLevel;
    private JButton resetBalls;

    private JSlider ballXSpeed;
    private JSlider ballYSpeed;

    private Wall wall;

    public DebugPanel(Wall wall){

        this.wall = wall;

        initialize(); //initialize debug panel

        skipLevel = makeButton("Skip Level",e -> wall.nextLevel()); //make new buttons
        resetBalls = makeButton("Reset Balls",e -> wall.resetBallCount());

        ballXSpeed = makeSlider(-4,4,e -> wall.setBallXSpeed(ballXSpeed.getValue())); //make new sliders
        ballYSpeed = makeSlider(-4,4,e -> wall.setBallYSpeed(ballYSpeed.getValue())); //show and set speed of ball

        this.add(skipLevel); //add buttons
        this.add(resetBalls);

        this.add(ballXSpeed); //add sliders
        this.add(ballYSpeed);

    }

    private void initialize(){ //initialize debug panel
        this.setBackground(DEF_BKG); //set background colour
        this.setLayout(new GridLayout(2,2)); //set layout
    }

    private JButton makeButton(String title, ActionListener e){ //make buttons and add listener
        JButton out = new JButton(title);
        out.addActionListener(e);
        return  out;
    }

    private JSlider makeSlider(int min, int max, ChangeListener e){ //make sliders and add listener
        JSlider out = new JSlider(min,max);
        out.setMajorTickSpacing(1); //set tick spacing
        out.setSnapToTicks(true); //snap to tick
        out.setPaintTicks(true); //paint ticks
        out.addChangeListener(e);
        return out;
    }

    public void setValues(int x,int y){ //show current ball speed on slider
        ballXSpeed.setValue(x);
        ballYSpeed.setValue(y);
    }

}
