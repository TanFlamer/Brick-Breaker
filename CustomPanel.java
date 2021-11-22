package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CustomPanel extends JPanel {

    private static final Color DEF_BKG = Color.WHITE;

    private JComboBox choice;
    JLabel label;

    String levelType[]={"Default","Random","Custom"};
    String brickRows[]={"1","2","3","4","5","6","7","8","9","10"};
    String brickInRow[]={"1","2","3","4","5","6","8","10","12","15"};
    String brickNum[]={"1","2","3","4"};
    String brickTypes[]={"Clay","Cement","Steel","Concrete"};

    String labels[]={"Level Generation","Rows of Bricks","Bricks in a Row","Types of Bricks","Brick 1","Brick 2","Brick 3","Brick 4","Reset","Confirm"};

    public CustomPanel(){

        initialize();

        makeJLabels(labels[0]);
        makeComboBox(levelType);

        makeJLabels(labels[1]);
        makeComboBox(brickRows);

        makeJLabels(labels[2]);
        makeComboBox(brickInRow);

        makeJLabels(labels[3]);
        makeComboBox(brickNum);

        makeJLabels(labels[4]);
        makeComboBox(brickTypes);

        makeJLabels(labels[5]);
        makeComboBox(brickTypes);

        makeJLabels(labels[6]);
        makeComboBox(brickTypes);

        makeJLabels(labels[7]);
        makeComboBox(brickTypes);

        makeJButton(labels[8]);
        makeJButton(labels[9]);
    }

    private void initialize(){ //initialize debug panel
        this.setBackground(DEF_BKG); //set background colour
        this.setLayout(new GridLayout(9,2)); //set layout
    }

    private void makeComboBox(String[] choice){ //make buttons and add listener
        JComboBox out = new JComboBox(choice);
        this.add(out);
    }

    private void makeJLabels(String label){ //make buttons and add listener
        JLabel out = new JLabel(label);
        this.add(out);
    }

    private void makeJButton(String label){ //make buttons and add listener
        JButton out = new JButton(label);
        this.add(out);
    }
}
