package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CustomPanel extends JPanel implements ActionListener {

    private static final Color DEF_BKG = Color.WHITE;

    private JComboBox level,ballCount,levelGen,row,brickRow,brickType,brick1,brick2,brick3,brick4;
    private JButton reset,save,trueRandom,minimalOrdered,allDefault,randomise;
    private JLabel levelNum,levelChoice;

    private Random rnd;

    String[] balls = {"Default","1","2","3","4","5","6","7","8","9","10"};
    String[] levelType = {"Default","True Ordered","Ordered (Maximal)","Ordered (Moderate)","Ordered (Minimal)","Random (Minimal)","Random (Moderate)","Random (Maximal)","True Random"};
    String[] brickRows = {"1","2","3","4","5","6","7","8","9","10"};
    String[] brickInRow = {"1","2","3","4","5","6","8","10","12","15"};
    String[] Num = {"1","2","3","4"};
    String[] brickTypes = {"Clay","Steel","Cement","Concrete"};
    String[] labels = {"Level","Number of Balls","Level Generation","Rows of Bricks","Bricks in a Row","Types of Bricks","Brick 1","Brick 2","Brick 3","Brick 4"};
    String[] buttonLabels = {"Reset","Save","True Random","Ordered (Minimal)","Default","Randomise All"};

    private int[][] choice = new int[4][9];

    public CustomPanel(){

        initialize();

        makeJLabels(labels[0]);
        level = makeComboBox(Num);
        level.addActionListener(this);
        this.add(level);

        makeJLabels(labels[1]);
        ballCount = makeComboBox(balls);
        ballCount.addActionListener(this);
        this.add(ballCount);

        makeJLabels(labels[2]);
        levelGen = makeComboBox(levelType);
        levelGen.addActionListener(this);
        this.add(levelGen);

        makeJLabels(labels[3]);
        row = makeComboBox(brickRows);
        this.add(row);

        makeJLabels(labels[4]);
        brickRow = makeComboBox(brickInRow);
        this.add(brickRow);

        makeJLabels(labels[5]);
        brickType = makeComboBox(Num);
        brickType.addActionListener(this);
        this.add(brickType);

        makeJLabels(labels[6]);
        brick1 = makeComboBox(brickTypes);
        this.add(brick1);

        makeJLabels(labels[7]);
        brick2 = makeComboBox(brickTypes);
        this.add(brick2);

        makeJLabels(labels[8]);
        brick3 = makeComboBox(brickTypes);
        this.add(brick3);

        makeJLabels(labels[9]);
        brick4 = makeComboBox(brickTypes);
        this.add(brick4);

        row.setEnabled(false);
        brickRow.setEnabled(false);
        brickType.setEnabled(false);
        brick1.setEnabled(false);
        brick2.setEnabled(false);
        brick3.setEnabled(false);
        brick4.setEnabled(false);

        reset = makeJButton(buttonLabels[0]);
        reset.addActionListener(this);
        this.add(reset);

        save = makeJButton(buttonLabels[1]);
        save.addActionListener(this);
        this.add(save);

        trueRandom = makeJButton(buttonLabels[2]);
        trueRandom.addActionListener(this);
        this.add(trueRandom);

        minimalOrdered = makeJButton(buttonLabels[3]);
        minimalOrdered.addActionListener(this);
        this.add(minimalOrdered);

        allDefault = makeJButton(buttonLabels[4]);
        allDefault.addActionListener(this);
        this.add(allDefault);

        randomise = makeJButton(buttonLabels[5]);
        randomise.addActionListener(this);
        this.add(randomise);

        levelNum = makeLabels("Level "+ level.getSelectedItem() +":");
        this.add(levelNum);

        levelChoice = makeLabels((String)levelGen.getSelectedItem());
        this.add(levelChoice);
    }

    private void initialize(){ //initialize debug panel
        this.setBackground(DEF_BKG); //set background colour
        this.setLayout(new GridLayout(14,2)); //set layout
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

    public int[][] getChoice(){
        return choice;
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

    private void setIndex(){
        row.setSelectedIndex(choice[level.getSelectedIndex()][1]);
        brickRow.setSelectedIndex(choice[level.getSelectedIndex()][2]);
        brickType.setSelectedIndex(choice[level.getSelectedIndex()][3]);
        levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
        brick1.setSelectedIndex(choice[level.getSelectedIndex()][4]);
        brick2.setSelectedIndex(choice[level.getSelectedIndex()][5]);
        brick3.setSelectedIndex(choice[level.getSelectedIndex()][6]);
        brick4.setSelectedIndex(choice[level.getSelectedIndex()][7]);
        ballCount.setSelectedIndex(choice[level.getSelectedIndex()][8]);
    }

    private void resetMessage(){
        levelNum.setText("Level "+ level.getSelectedItem() +":");
        levelChoice.setText((String)levelGen.getSelectedItem());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==level){
            setIndex();
            resetMessage();
        }
        else if(e.getSource()==levelGen){

            if(levelGen.getSelectedIndex()%4==0){
                row.setEnabled(false);
                brickRow.setEnabled(false);
                brickType.setEnabled(false);
                brickFalse();
            }
            else if(levelGen.getSelectedIndex()%4==1){
                row.setEnabled(true);
                brickRow.setEnabled(true);
                brickType.setEnabled(true);
                manageBrick();
            }
            else if(levelGen.getSelectedIndex()%4==2){
                row.setEnabled(true);
                brickRow.setEnabled(true);
                brickType.setEnabled(true);
                brickFalse();
            }
            else if(levelGen.getSelectedIndex()%4==3){
                row.setEnabled(true);
                brickRow.setEnabled(true);
                brickType.setEnabled(false);
                brickFalse();
            }
        }
        else if(e.getSource()==brickType){

            if(!(levelGen.getSelectedIndex()%4==2)){
                manageBrick();
            }
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
            ballCount.setSelectedIndex(0);
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
            choice[level.getSelectedIndex()][8] = ballCount.getSelectedIndex();
            resetMessage();
        }
        else if(e.getSource()==trueRandom){

            for(int i = 0; i < 4;i++){
                choice[i][0] = 8;
            }
            levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
            resetMessage();
        }
        else if(e.getSource()==minimalOrdered){

            for(int i = 0; i < 4;i++){
                choice[i][0] = 4;
            }
            levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
            resetMessage();
        }
        else if(e.getSource()==allDefault){

            for(int i = 0; i < 4;i++){
                choice[i][0] = 0;
            }
            levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
            resetMessage();
        }
        else if(e.getSource()==randomise){

            rnd = new Random(); //get random number
            for(int i = 0; i < 4;i++){
                choice[i][0] = rnd.nextInt(9);
                choice[i][1] = rnd.nextInt(10);
                choice[i][2] = rnd.nextInt(10);
                choice[i][3] = rnd.nextInt(4);
                choice[i][4] = rnd.nextInt(4);
                choice[i][5] = rnd.nextInt(4);
                choice[i][6] = rnd.nextInt(4);
                choice[i][7] = rnd.nextInt(4);
                choice[i][8] = rnd.nextInt(11);
            }
            setIndex();
            resetMessage();
        }
    }
}
