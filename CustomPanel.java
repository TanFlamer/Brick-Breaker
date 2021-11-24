package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomPanel extends JPanel implements ActionListener {

    private static final Color DEF_BKG = Color.WHITE;

    private JComboBox level,levelGen,row,brickRow,brickType,brick1,brick2,brick3,brick4;
    private JButton reset,save;
    private JLabel levelNum,levelChoice;

    String[] levelType = {"Default","Random","Custom"};
    String[] brickRows = {"1","2","3","4","5","6","7","8","9","10"};
    String[] brickInRow = {"1","2","3","4","5","6","8","10","12","15"};
    String[] Num = {"1","2","3","4"};
    String[] brickTypes = {"Clay","Steel","Cement","Concrete"};
    String[] labels = {"Level","Level Generation","Rows of Bricks","Bricks in a Row","Types of Bricks","Brick 1","Brick 2","Brick 3","Brick 4","Reset","Save"};

    static int[][] choice = new int[4][8];

    public CustomPanel(){

        initialize();

        makeJLabels(labels[0]);
        level = makeComboBox(Num);
        level.addActionListener(this);
        this.add(level);

        makeJLabels(labels[1]);
        levelGen = makeComboBox(levelType);
        levelGen.addActionListener(this);
        this.add(levelGen);

        makeJLabels(labels[2]);
        row = makeComboBox(brickRows);
        this.add(row);

        makeJLabels(labels[3]);
        brickRow = makeComboBox(brickInRow);
        this.add(brickRow);

        makeJLabels(labels[4]);
        brickType = makeComboBox(Num);
        brickType.addActionListener(this);
        this.add(brickType);

        makeJLabels(labels[5]);
        brick1 = makeComboBox(brickTypes);
        this.add(brick1);

        makeJLabels(labels[6]);
        brick2 = makeComboBox(brickTypes);
        this.add(brick2);

        makeJLabels(labels[7]);
        brick3 = makeComboBox(brickTypes);
        this.add(brick3);

        makeJLabels(labels[8]);
        brick4 = makeComboBox(brickTypes);
        this.add(brick4);

        row.setEnabled(false);
        brickRow.setEnabled(false);
        brickType.setEnabled(false);
        brick1.setEnabled(false);
        brick2.setEnabled(false);
        brick3.setEnabled(false);
        brick4.setEnabled(false);

        reset = makeJButton(labels[9]);
        reset.addActionListener(this);
        this.add(reset);

        save = makeJButton(labels[10]);
        save.addActionListener(this);
        this.add(save);

        levelNum = makeLabels("Level "+ level.getSelectedItem() +":");
        this.add(levelNum);

        levelChoice = makeLabels((String)levelGen.getSelectedItem());
        this.add(levelChoice);
    }

    private void initialize(){ //initialize debug panel
        this.setBackground(DEF_BKG); //set background colour
        this.setLayout(new GridLayout(11,2)); //set layout
    }

    private JComboBox makeComboBox(String[] choice){ //make buttons and add listener
        return new JComboBox(choice);
    }

    private void makeJLabels(String label){ //make buttons and add listener
        JLabel out = new JLabel(label);
        this.add(out);
    }

    private JLabel makeLabels(String label){ //make buttons and add listener
        return new JLabel(label);
    }

    private JButton makeJButton(String label){ //make buttons and add listener
        return new JButton(label);
    }

    private void manageBrick(){
        if(brickType.getSelectedIndex()==0){
            brick2.setEnabled(false);
            brick3.setEnabled(false);
            brick4.setEnabled(false);
        }
        else if(brickType.getSelectedIndex()==1){
            brick2.setEnabled(true);
            brick3.setEnabled(false);
            brick4.setEnabled(false);
        }
        else if(brickType.getSelectedIndex()==2){
            brick2.setEnabled(true);
            brick3.setEnabled(true);
            brick4.setEnabled(false);
        }
        else{
            brick2.setEnabled(true);
            brick3.setEnabled(true);
            brick4.setEnabled(true);
        }
        brick1.setEnabled(true);
    }

    private void brickFalse(){
        brick1.setEnabled(false);
        brick2.setEnabled(false);
        brick3.setEnabled(false);
        brick4.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==level){
            row.setSelectedIndex(choice[level.getSelectedIndex()][1]);
            brickRow.setSelectedIndex(choice[level.getSelectedIndex()][2]);
            brickType.setSelectedIndex(choice[level.getSelectedIndex()][3]);
            levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
            brick1.setSelectedIndex(choice[level.getSelectedIndex()][4]);
            brick2.setSelectedIndex(choice[level.getSelectedIndex()][5]);
            brick3.setSelectedIndex(choice[level.getSelectedIndex()][6]);
            brick4.setSelectedIndex(choice[level.getSelectedIndex()][7]);
            levelNum.setText("Level "+ level.getSelectedItem() +":");
            levelChoice.setText((String)levelGen.getSelectedItem());
        }
        else if(e.getSource()==levelGen){

            if(levelGen.getSelectedIndex()==0){
                row.setEnabled(false);
                brickRow.setEnabled(false);
                brickType.setEnabled(false);
                brickFalse();
            }
            else if(levelGen.getSelectedIndex()==1){
                row.setEnabled(true);
                brickRow.setEnabled(true);
                brickType.setEnabled(false);
                brickFalse();
            }
            else{
                row.setEnabled(true);
                brickRow.setEnabled(true);
                brickType.setEnabled(true);
                manageBrick();
            }
        }
        else if(e.getSource()==brickType){
            manageBrick();
        }
        else if(e.getSource()==reset){
            row.setSelectedIndex(0);
            brickRow.setSelectedIndex(0);
            brickType.setSelectedIndex(0);
            levelGen.setSelectedIndex(0);
            brick1.setSelectedIndex(0);
            brick2.setSelectedIndex(0);
            brick3.setSelectedIndex(0);
            brick4.setSelectedIndex(0);
        }
        else if(e.getSource()==save){
            choice[level.getSelectedIndex()][0] = levelGen.getSelectedIndex();
            choice[level.getSelectedIndex()][1] = row.getSelectedIndex();
            choice[level.getSelectedIndex()][2] = brickRow.getSelectedIndex();
            choice[level.getSelectedIndex()][3] = brickType.getSelectedIndex();
            choice[level.getSelectedIndex()][4] = brick1.getSelectedIndex();
            choice[level.getSelectedIndex()][5] = brick2.getSelectedIndex();
            choice[level.getSelectedIndex()][6] = brick3.getSelectedIndex();
            choice[level.getSelectedIndex()][7] = brick4.getSelectedIndex();
            levelNum.setText("Level "+ level.getSelectedItem() +":");
            levelChoice.setText((String)levelGen.getSelectedItem());
        }
    }
}
