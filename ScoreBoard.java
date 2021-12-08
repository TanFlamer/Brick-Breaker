package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Public class ScoreBoard is responsible for showing the list of highscores for that level at the end of every
 * level and also the highscores for the whole game at the end of the game. The player score, time and category
 * are listed and the player is prompted to enter their name to save their achievement. A new object is created
 * at the end of each level and at the end of the game.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class ScoreBoard extends JDialog implements ActionListener {

    /**
     * Linked list to load and hold the highscores for display.
     */
    LinkedList<Score> newScore = new LinkedList<>();

    /**
     * Title string.
     */
    private static final String TITLE = "Highscore Board";
    /**
     * Array of strings to hold the different categories for the whole game.
     */
    String[] finalType = {"Ordered (Mix)","Random (Mix)","Mixed"};
    /**
     * Array of strings to hold the different categories for the levels.
     */
    String[] levelType = {"Default","True Ordered","Ordered (Max)","Ordered (Mid)","Ordered (Min)","Random (Min)","Random (Mid)","Random (Max)","True Random"};

    /**
     * JFrame used to center the ScoreBoard.
     */
    private final JFrame owner;
    /**
     * JButton to be pressed to save player data.
     */
    private final JButton saveName;
    /**
     * JLabel to prompt player for username.
     */
    private final JLabel name;
    /**
     * JTextField for player to enter username.
     */
    private final JTextField getName;
    /**
     * Double array of integers to get player choice to generate level category.
     */
    private final int[][] choice;
    /**
     * Double array of integers to record player time and score for each level and the whole game.
     */
    private final int[][] scoreAndTime;
    /**
     * Integer to get current level number.
     */
    private final int level;

    /**
     * This constructor is used to load and display highscores for each level after the level is completed and
     * highscores for the whole game after the game is completed. It is called after every level and when the
     * game ends. The highscore list is loaded into the linked list, then is resorted, renumbered and duplicates
     * removed so that the rankings shown are accurate.
     *
     * @param owner JFrame used to center the ScoreBoard.
     * @param level Integer to get current level number.
     * @param scoreAndTime Double array of integers to record player time and score for each level and the whole game.
     * @param choice Double array of integers to get player choice to generate level category.
     * @throws FileNotFoundException This constructor throws FileNotFoundException when the highscore list for
     *                               that level does not exist.
     */
    public ScoreBoard(JFrame owner, int level, int[][] scoreAndTime, int[][] choice) throws FileNotFoundException {

        String[] column = {"Ranking","Name","Category","Time","Score"};

        JLabel jLabel;

        this.owner = owner;
        this.choice = choice;
        this.scoreAndTime = scoreAndTime;
        this.level = level;

        Load(newScore,level);
        newScore.sort(new ScoreComp());
        RemoveDuplicates(newScore);
        Renumber(newScore);

        String[][] data = new String[newScore.size()][5];
        int count = 0;

        for(Score print : newScore){
            data[count][0] = String.valueOf(print.ranking);
            data[count][1] = print.name;
            data[count][2] = print.levelType;
            data[count][3] = String.format("%02d:%02d",print.time/60,print.time%60);
            data[count][4] = String.valueOf(print.score);
            count++;
        }

        initialize();

        this.setLayout(new GridBagLayout()); //set layout
        GridBagConstraints constraints = new GridBagConstraints();

        if(level!=0) {
            jLabel = new JLabel("Level " + level + " Scores");
        }
        else {
            jLabel = new JLabel("Total highscores");
        }
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(jLabel,constraints);

        JTable scoreboard = new JTable(data,column);
        JScrollPane viewRanking = new JScrollPane(scoreboard);
        viewRanking.setPreferredSize(new Dimension(450,300));
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(viewRanking,constraints);

        if(level!=0) {
            jLabel = new JLabel("Level " + level + " score = " + scoreAndTime[level][0]);
        }
        else{
            jLabel = new JLabel("Total highscore = " + scoreAndTime[level][0]);
        }
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(jLabel,constraints);

        String timeString = String.format("%02d:%02d",scoreAndTime[level][1]/60,scoreAndTime[level][1]%60);

        if(level!=0) {
            jLabel = new JLabel("Level " + level + " time = " + timeString);
        }
        else{
            jLabel = new JLabel("Total time = " + timeString);
        }
        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(jLabel,constraints);

        jLabel = new JLabel("New ranking = " + getRanking(level));
        constraints.gridx = 0;
        constraints.gridy = 4;
        this.add(jLabel,constraints);

        if(level!=0) {
            jLabel = new JLabel("Category = " + levelType[choice[level-1][0]]);
        }
        else {
            if(choice[0][0]==choice[1][0]&&choice[1][0]==choice[2][0]&&choice[2][0]==choice[3][0]&&choice[3][0]==choice[4][0]){
                jLabel = new JLabel("Category = " + levelType[choice[0][0]]);
            }
            else if((choice[0][0]-1)/4==0&&(choice[1][0]-1)/4==0&&(choice[2][0]-1)/4==0&&(choice[3][0]-1)/4==0&&(choice[4][0]-1)/4==0&&choice[0][0]!=0&&choice[1][0]!=0&&choice[2][0]!=0&&choice[3][0]!=0&&choice[4][0]!=0){
                jLabel = new JLabel("Category = " + finalType[0]);
            }
            else if((choice[0][0]-1)/4==1&&(choice[1][0]-1)/4==1&&(choice[2][0]-1)/4==1&&(choice[3][0]-1)/4==1&&(choice[4][0]-1)/4==1){
                jLabel = new JLabel("Category = " + finalType[1]);
            }
            else {
                jLabel = new JLabel("Category = " + finalType[2]);
            }
        }
        constraints.gridx = 0;
        constraints.gridy = 5;
        this.add(jLabel,constraints);

        name = new JLabel("Please enter username. (Max 8 characters)");
        constraints.gridx = 0;
        constraints.gridy = 6;
        this.add(name,constraints);

        getName = new JTextField();
        getName.setPreferredSize(new Dimension(100,20));
        constraints.gridx = 0;
        constraints.gridy = 7;
        this.add(getName,constraints);

        saveName = new JButton("Save");
        saveName.addActionListener(this);
        constraints.gridx = 0;
        constraints.gridy = 8;
        this.add(saveName,constraints);

        this.setSize(450,300);
        this.pack();
        this.setVisible(true);
    }

    /**
     * This method is used to load the highscore lists into linked lists so that the top 10 highscores for each
     * level and the total highscore for the game can be shown.
     *
     * @param loadScore The linked list where the highscore list is loaded into.
     * @param level This is the level for which the highscore list is loaded.
     * @throws FileNotFoundException This IOException is thrown if the highscore list does not exist.
     */
    private void Load(LinkedList<Score> loadScore, int level) throws FileNotFoundException {

        String[] hold = new String[5]; //make array of strings with 8 elements

        File file=new File("highscores/highscore"+level+".txt"); //load location
        Scanner sc=new Scanner(file);

        while (sc.hasNextLine()) { //tokenize string using , and stop when list is empty
            StringTokenizer st = new StringTokenizer(sc.nextLine(), ",");

            while (st.hasMoreTokens()) { //temporarily save info of treatment in each loop
                for (int count = 0; count < 5; count++) {
                    hold[count] = st.nextToken();
                }
                Score input = new Score(Integer.parseInt(hold[0]), hold[1], hold[2], Integer.parseInt(hold[3]), Integer.parseInt(hold[4]));
                loadScore.add(input); //change each string to correct type and load in linked list
            }
        }
    }

    /**
     * This method is used to save the highscore from the linked lists into a text file to maintain a permanent
     * highscore list.
     *
     * @param saveScore The linked list where the highscore list to be saved is.
     * @param level This is the level for which the highscore list is to be saved.
     * @throws IOException This IOException is thrown if the highscore list does not exist.
     */
    private void Save(LinkedList<Score> saveScore, int level) throws IOException {

        PrintWriter writer = new PrintWriter("highscores/highscore"+level+".txt", StandardCharsets.UTF_8); //save location (can add code to change location)

        for(Score print : saveScore) { //save line by line with for loop
            writer.format("%d,%S,%S,%d,%d\n", print.ranking, print.name, print.levelType, print.time, print.score);
        }
        writer.close(); //close writer
    }

    /**
     * This method is called to renumber the linked list after resorting it. The new rankings will be in ascending
     * order in increments of 1 starting from 1.
     *
     * @param holdScore The linked list which is to be renumbered.
     */
    private void Renumber(LinkedList<Score> holdScore){

        int num = 1; //array of int to count number of treatment of each type

        for(Score renum : holdScore){ //count number of treatment of each type
            renum.ranking = num++;
        }
    }

    /**
     * This method is called to add a new entry into the ranking list. The new entry contains the player ranking,
     * name, level type, time and score. The new entry is added to the linked list and is then saved into the
     * permanent highscore list in the text file.
     *
     * @param holdScore The linked list where the new entry is to be loaded into.
     * @param ranking The ranking of the new entry.
     * @param name The name of the new entry.
     * @param levelType The level type of the new entry.
     * @param time The time of the new entry.
     * @param score The score of the new entry.
     */
    private void Add(LinkedList<Score> holdScore, int ranking, String name, String levelType, int time, int score) {

        Score newScore = new Score(ranking,name,levelType,time,score);
        holdScore.add(newScore); //add new entry into linked list
    }

    /**
     * This method is used to center the ScoreBoard by using the JFrame location.
     */
    private void setLocation(){ //set debug console location
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }

    /**
     * This method is used to set the ScoreBoard title and to add in window listeners.
     */
    private void initialize() {

        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.addWindowListener(new WindowAdapter() {

            /**
             * This window listener is called when the ScoreBoard is activated and centers the ScoreBoard on
             * the screen.
             *
             * @param windowEvent This parameter is used to track the ScoreBoard window.
             */
            @Override
            public void windowActivated(WindowEvent windowEvent) { //when debug console loaded
                setLocation(); //set debug console location
            }
        });

        this.setFocusable(true);
    }

    /**
     * This method gets the expected ranking of the player based on his score and time. The method looks for the
     * first entry where the player score is larger than the old entry score. Then, it checks the time to see if
     * the player time is less than the old entry time. If not, the method looks at the next entry and compares
     * again. This means that for equal scores, the old scores rank higher as the record is made earlier. For
     * equal scores and time, the old scores and time are ranked higher for the same reason.
     *
     * @param level This is the level of highscore list for which the new entry is compared to.
     * @return This method returns an integer as the expected ranking of the player.
     */
    private int getRanking(int level){
        int ranking = 0;

        for(Score find : newScore){
            if(scoreAndTime[level][0]>find.score){
                if(scoreAndTime[level][1]<find.time){
                    ranking = find.ranking;
                    break;
                }
            }
        }

        if(ranking==0){
            ranking = newScore.size() + 1;
        }
        return ranking;
    }

    /**
     * This method removes any duplicate entries in the rankings. The method checks for the same player name, score
     * and level type and leaves only a single entry. Since this method is called after the rankings are resorted,
     * only the best scores and best times for a person in each category is kept.
     *
     * @param holdScore The linked list where the duplicate entries are to be removed.
     */
    private void RemoveDuplicates(LinkedList<Score> holdScore){

        String nameComp,categoryComp;
        for(int i = 0; i < holdScore.size(); i++){

            if(holdScore.get(i)!=null) {
                nameComp = holdScore.get(i).name;
                categoryComp = holdScore.get(i).levelType;
            }
            else {
                break;
            }

            for(int j = 0; j < holdScore.size(); j++){

                if(holdScore.get(j)!=null) {
                    if ((nameComp.equalsIgnoreCase(holdScore.get(j).name) && categoryComp.equalsIgnoreCase(holdScore.get(j).levelType))&&(i!=j)){
                        holdScore.remove(j);
                        j--;
                    }
                }
                else {
                    break;
                }
            }
        }
    }

    /**
     * This action listener is called when the save button is pressed. If the name entered is invalid, the JLabel
     * will ask for a valid username. If entered name is valid, the JButton is disabled and the new entry is saved
     * into the linked list.
     * @param e This parameter is used to track the JButton save.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(getName.getText().length()==0) {
            name.setText("Please enter valid username.");
        }
        else {
            String temp = getName.getText();
            String username;
            if(temp.length()>8) {
                username = temp.substring(0,8);
            }
            else {
                username = temp;
            }
            name.setText("Name saved.");

            if(level!=0) {
                Add(newScore, getRanking(level), username, levelType[choice[level-1][0]],scoreAndTime[level][1], scoreAndTime[level][0]);
            }
            else{
                if(choice[0][0]==choice[1][0]&&choice[1][0]==choice[2][0]&&choice[2][0]==choice[3][0]&&choice[3][0]==choice[4][0]){
                    Add(newScore, getRanking(level), username, levelType[choice[0][0]],scoreAndTime[level][1], scoreAndTime[level][0]);
                }
                else if((choice[0][0]-1)/4==0&&(choice[1][0]-1)/4==0&&(choice[2][0]-1)/4==0&&(choice[3][0]-1)/4==0&&(choice[4][0]-1)/4==0&&choice[0][0]!=0&&choice[1][0]!=0&&choice[2][0]!=0&&choice[3][0]!=0&&choice[4][0]!=0){
                    Add(newScore, getRanking(level), username, finalType[0],scoreAndTime[level][1], scoreAndTime[level][0]);
                }
                else if((choice[0][0]-1)/4==1&&(choice[1][0]-1)/4==1&&(choice[2][0]-1)/4==1&&(choice[3][0]-1)/4==1&&(choice[4][0]-1)/4==1){
                    Add(newScore, getRanking(level), username, finalType[1],scoreAndTime[level][1], scoreAndTime[level][0]);
                }
                else {
                    Add(newScore, getRanking(level), username, finalType[2],scoreAndTime[level][1], scoreAndTime[level][0]);
                }
            }
            newScore.sort(new ScoreComp());
            RemoveDuplicates(newScore);
            Renumber(newScore);
            try {
                Save(newScore,level);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            saveName.setEnabled(false);
            getName.setEnabled(false);
        }
    }
}

