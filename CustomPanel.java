package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Public class CustomPanel is used customise the levels based on the player choice such as level type, number of
 * balls, number of rows of bricks, number of bricks in a row, number of brick types and brick types that spawn.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class CustomPanel extends JPanel implements ActionListener {

    /**
     * Background colour of white for the CustomPanel.
     */
    private static final Color DEF_BKG = Color.WHITE;

    /**
     * JComboBox for the level number.
     */
    private final JComboBox<String> level;
    /**
     * JComboBox for the ball count.
     */
    private final JComboBox<String> ballCount;
    /**
     * JComboBox for the level type.
     */
    private final JComboBox<String> levelGen;
    /**
     * JComboBox for the number of rows of bricks.
     */
    private final JComboBox<String> row;
    /**
     * JComboBox for the bricks in a row.
     */
    private final JComboBox<String> brickRow;
    /**
     * JComboBox for the number of brick types.
     */
    private final JComboBox<String> brickType;
    /**
     * JComboBox for the first brick type to spawn.
     */
    private final JComboBox<String> brick1;
    /**
     * JComboBox for the second brick type to spawn.
     */
    private final JComboBox<String> brick2;
    /**
     * JComboBox for the third brick type to spawn.
     */
    private final JComboBox<String> brick3;
    /**
     * JComboBox for the fourth brick type to spawn.
     */
    private final JComboBox<String> brick4;
    /**
     * JComboBox for the level orientation.
     */
    private final JComboBox<String> playerOrientation;
    /**
     * JComboBox for the level orientation.
     */
    private final JComboBox<String> freeMovement;
    /**
     * JButton to reset settings to default.
     */
    private final JButton reset;
    /**
     * JButton to save current settings.
     */
    private final JButton save;
    /**
     * JButton to set all levels to True Random level type.
     */
    private final JButton trueRandom;
    /**
     * JButton to set all levels to Ordered (Min) level type.
     */
    private final JButton minimalOrdered;
    /**
     * JButton to set all levels to Default level type.
     */
    private final JButton allDefault;
    /**
     * JButton to randomise all settings.
     */
    private final JButton randomise;

    /**
     * JLabel to show current level number.
     */
    private final JLabel levelNum;
    /**
     * JLabel to show current level type.
     */
    private final JLabel levelChoice;
    /**
     * JLabel to show current level number.
     */
    private final JLabel levelOrientation;
    /**
     * JLabel to show current level orientation.
     */
    private final JLabel levelOrientationChoice;
    /**
     * JLabel to show current level number.
     */
    private final JLabel movementOption;
    /**
     * JLabel to show current movement choice.
     */
    private final JLabel movementOptionChoice;

    /**
     * Double array of Integer to hold all player choices to send to GameBoard for level generation.
     */
    private final int[][] choice = new int[5][11];

    /**
     * This constructor is used to generate all the JComboBoxes, JButtons and JLabels on the CustomPanel for
     * the player to customise the levels.
     */
    public CustomPanel(){

        String[] balls = {"Default","1","2","3","4","5","6","7","8","9","10"};
        String[] levelType = {"Default","True Ordered","Ordered (Maximal)","Ordered (Moderate)","Ordered (Minimal)","Random (Minimal)","Random (Moderate)","Random (Maximal)","True Random"};
        String[] brickRows = {"1","2","3","4","5","6","7","8","9","10"};
        String[] brickInRow = {"1","2","3","4","5","6","8","10","12","15"};
        String[] Num = {"1","2","3","4","5"};
        String[] brickNum = {"1","2","3","4"};
        String[] brickTypes = {"Clay","Steel","Cement","Concrete"};
        String[] labels = {"Level","Number of Balls","Level Generation","Rows of Bricks","Bricks in a Row","Types of Bricks","Brick 1","Brick 2","Brick 3","Brick 4","Player Position","Free Movement"};
        String[] buttonLabels = {"Reset","Save","True Random","Ordered (Minimal)","Default","Randomise All"};
        String[] playerPosition = {"Bottom","Top"};
        String[] movementMode = {"Disabled","Enabled"};

        initialize();

        makeJLabels(labels[0]);
        level = makeComboBox(Num);
        level.addActionListener(this);
        this.add(level);

        makeJLabels(labels[11]);
        freeMovement = makeComboBox(movementMode);
        this.add(freeMovement);

        makeJLabels(labels[10]);
        playerOrientation = makeComboBox(playerPosition);
        this.add(playerOrientation);

        makeJLabels(labels[1]);
        ballCount = makeComboBox(balls);
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
        brickType = makeComboBox(brickNum);
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

        levelNum = makeLabels("Level "+ level.getSelectedItem() +" Level Type:");
        this.add(levelNum);

        levelChoice = makeLabels((String)levelGen.getSelectedItem());
        this.add(levelChoice);

        levelOrientation = makeLabels("Level "+ level.getSelectedItem() +" Orientation:");
        this.add(levelOrientation);

        levelOrientationChoice = makeLabels((String)playerOrientation.getSelectedItem());
        this.add(levelOrientationChoice);

        movementOption = makeLabels("Level "+ level.getSelectedItem() +" Free Movement:");
        this.add(movementOption);

        movementOptionChoice = makeLabels((String)freeMovement.getSelectedItem());
        this.add(movementOptionChoice);
    }

    /**
     * This method sets the CustomPanel background to white and sets the layout to GridLayout.
     */
    private void initialize(){ //initialize debug panel
        this.setBackground(DEF_BKG); //set background colour
        this.setLayout(new GridLayout(18,2)); //set layout
    }

    /**
     * This method is used to make JComboBoxes with the given array of strings as their options.
     * @param choice This array of strings are the choices for the JComboBoxes.
     * @return This method returns a JComboBox with the given array of strings as their options.
     */
    private JComboBox<String> makeComboBox(String[] choice){ //make buttons and add listener
        return new JComboBox<>(choice);
    }

    /**
     * This method is used to make JLabels with the given string as their label and adds it to the CustomPanel.
     * @param label This string is the label for the JLabel.
     */
    private void makeJLabels(String label){ //make buttons and add listener
        JLabel out = new JLabel(label);
        this.add(out);
    }

    /**
     * This method is used to make JLabels with the given string as their label which will change according to
     * the choices by the player.
     *
     * @param label This string is the initial label for the JLabel.
     * @return This method returns a JLabel with the given string as the initial label.
     */
    private JLabel makeLabels(String label){ //make buttons and add listener
        return new JLabel(label);
    }

    /**
     * This method is used to make JButtons with the given string as their label.
     * @param label This string is the label for the JButton.
     * @return This method returns a JButton with the given string as the label.
     */
    private JButton makeJButton(String label){ //make buttons and add listener
        return new JButton(label);
    }

    /**
     * This method returns the player choices as a double array of integers.
     * @return The player choices as a double array of integers is returned.
     */
    public int[][] getChoice(){
        return choice;
    }

    /**
     * The JComboBox brick types available for selection are reset every time the number of brick types is changed.
     */
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

    /**
     * The brick types are all disabled and are not available for selection.
     */
    private void brickFalse(){
        brick1.setEnabled(false);
        brick2.setEnabled(false);
        brick3.setEnabled(false);
        brick4.setEnabled(false);
    }

    /**
     * The choices made by a player for a specific level are loaded into the JComboBoxes every time that level is
     * selected in the JComboBox.
     */
    public void setIndex(){
        row.setSelectedIndex(choice[level.getSelectedIndex()][1]);
        brickRow.setSelectedIndex(choice[level.getSelectedIndex()][2]);
        brickType.setSelectedIndex(choice[level.getSelectedIndex()][3]);
        levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
        brick1.setSelectedIndex(choice[level.getSelectedIndex()][4]);
        brick2.setSelectedIndex(choice[level.getSelectedIndex()][5]);
        brick3.setSelectedIndex(choice[level.getSelectedIndex()][6]);
        brick4.setSelectedIndex(choice[level.getSelectedIndex()][7]);
        ballCount.setSelectedIndex(choice[level.getSelectedIndex()][8]);
        playerOrientation.setSelectedIndex(choice[level.getSelectedIndex()][9]);
        freeMovement.setSelectedIndex(choice[level.getSelectedIndex()][10]);
    }

    /**
     * The message showing the player level and level type is reset every time a new level is selected in the
     * JComboBox or when the player saves the current choices.
     */
    public void resetMessage(){
        levelNum.setText("Level "+ level.getSelectedItem() +" Level Type:");
        levelChoice.setText((String)levelGen.getSelectedItem());
        levelOrientation.setText("Level "+ level.getSelectedItem() +" Orientation:");
        levelOrientationChoice.setText((String)playerOrientation.getSelectedItem());
        movementOption.setText("Level "+ level.getSelectedItem() +" Free Movement:");
        movementOptionChoice.setText((String)freeMovement.getSelectedItem());
    }

    /**
     * This action listener tracks all actions performed in the CustomPanel such as JComboBoxes use and JButton
     * presses and loads the relevant responses.
     *
     * @param e This parameter is used to track the sections performed in the CustomPanel.
     */
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
            playerOrientation.setSelectedIndex(0);
            freeMovement.setSelectedIndex(0);
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
            choice[level.getSelectedIndex()][9] = playerOrientation.getSelectedIndex();
            choice[level.getSelectedIndex()][10] = freeMovement.getSelectedIndex();
            resetMessage();
        }
        else if(e.getSource()==trueRandom){

            for(int i = 0; i < 5;i++){
                choice[i][0] = 8;
            }
            levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
            resetMessage();
        }
        else if(e.getSource()==minimalOrdered){

            for(int i = 0; i < 5;i++){
                choice[i][0] = 4;
            }
            levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
            resetMessage();
        }
        else if(e.getSource()==allDefault){

            for(int i = 0; i < 5;i++){
                choice[i][0] = 0;
            }
            levelGen.setSelectedIndex(choice[level.getSelectedIndex()][0]);
            resetMessage();
        }
        else if(e.getSource()==randomise){

            Random rnd = new Random(); //get random number
            for(int i = 0; i < 5;i++){
                choice[i][0] = rnd.nextInt(9);
                choice[i][1] = rnd.nextInt(10);
                choice[i][2] = rnd.nextInt(10);
                choice[i][3] = rnd.nextInt(4);
                choice[i][4] = rnd.nextInt(4);
                choice[i][5] = rnd.nextInt(4);
                choice[i][6] = rnd.nextInt(4);
                choice[i][7] = rnd.nextInt(4);
                choice[i][8] = rnd.nextInt(11);
                choice[i][9] = rnd.nextInt(2);
                choice[i][10] = rnd.nextInt(2);
            }
            setIndex();
            resetMessage();
        }
    }
}
