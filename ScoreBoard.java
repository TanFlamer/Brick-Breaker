package test;

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

public class ScoreBoard extends JDialog implements ActionListener {

    private JFrame owner;
    private GameBoard gameboard;
    private static final String TITLE = "Highscore Board";
    private String username;
    private JButton saveName;
    private JLabel results,name,ranking,title,category,time;
    private JTextField getName;

    LinkedList<Score> newScore = new LinkedList<>();
    String[] column = {"Ranking","Name","Category","Time","Score"};
    String[] finalType = {"Ordered (Mix)","Random (Mix)","Mixed"};
    String[] levelType = {"Default","True Ordered","Ordered (Max)","Ordered (Mid)","Ordered (Min)","Random (Min)","Random (Mid)","Random (Max)","True Random"};
    private int[][] choice;
    private int[][] score;
    private int level;

    public ScoreBoard(JFrame owner, GameBoard gameboard, int level, int[][] score, int[][] choice) throws FileNotFoundException {

        this.owner = owner;
        this.gameboard = gameboard;
        this.choice = choice;
        this.score = score;
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
            title = new JLabel("Level " + level + " Scores");
        }
        else {
            title = new JLabel("Total highscores");
        }
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(title,constraints);

        JTable scoreboard = new JTable(data,column);
        JScrollPane viewRanking = new JScrollPane(scoreboard);
        viewRanking.setPreferredSize(new Dimension(450,300));
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(viewRanking,constraints);

        if(level!=0) {
            results = new JLabel("Level " + level + " score = " + score[level][0]);
        }
        else{
            results = new JLabel("Total highscore = " + score[0][0]);
        }
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(results,constraints);

        String timeString = String.format("%02d:%02d",score[level][1]/60,score[level][1]%60);

        if(level!=0) {
            time = new JLabel("Level " + level + " time = " + timeString);
        }
        else{
            time = new JLabel("Total time = " + timeString);
        }
        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(time,constraints);

        ranking=new JLabel("New ranking = " + getRanking(level));
        constraints.gridx = 0;
        constraints.gridy = 4;
        this.add(ranking,constraints);

        if(level!=0) {
            category = new JLabel("Category = " + levelType[choice[level-1][0]]);
        }
        else {
            if(choice[0][0]==choice[1][0]&&choice[1][0]==choice[2][0]&&choice[2][0]==choice[3][0]){
                category = new JLabel("Category = " + levelType[choice[0][0]]);
            }
            else if((choice[0][0]-1)/4==0&&(choice[1][0]-1)/4==0&&(choice[2][0]-1)/4==0&&(choice[3][0]-1)/4==0){
                category = new JLabel("Category = " + finalType[0]);
            }
            else if((choice[0][0]-1)/4==1&&(choice[1][0]-1)/4==1&&(choice[2][0]-1)/4==1&&(choice[3][0]-1)/4==1){
                category = new JLabel("Category = " + finalType[1]);
            }
            else {
                category = new JLabel("Category = " + finalType[2]);
            }
        }
        constraints.gridx = 0;
        constraints.gridy = 5;
        this.add(category,constraints);

        name=new JLabel("Please enter username. (Max 8 characters)");
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

    private void Save(LinkedList<Score> saveScore, int level) throws IOException {

        PrintWriter writer = new PrintWriter("highscores/highscore"+level+".txt", StandardCharsets.UTF_8); //save location (can add code to change location)

        for(Score print : saveScore) { //save line by line with for loop
            writer.format("%d,%S,%S,%d,%d\n", print.ranking, print.name, print.levelType, print.time, print.score);
        }
        writer.close(); //close writer
    }

    private void Renumber(LinkedList<Score> holdScore){

        int num = 1; //array of int to count number of treatment of each type

        for(Score renum : holdScore){ //count number of treatment of each type
            renum.ranking = num++;
        }
    }

    private void Add(LinkedList<Score> holdScore, int ranking, String name, String levelType, int time, int score) {

        Score newScore = new Score(ranking,name,levelType,time,score);
        holdScore.add(newScore); //add new entry into linked list
    }

    private void setLocation(){ //set debug console location
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }

    private void initialize() {

        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                gameboard.repaint();
            }

            @Override
            public void windowActivated(WindowEvent windowEvent) { //when debug console loaded
                setLocation(); //set debug console location
            }
        });

        this.setFocusable(true);
    }

    private int getRanking(int level){
        int ranking = 0;

        for(Score find : newScore){
            if(score[level][0]>find.score){
                ranking = find.ranking;
                break;
            }
        }

        if(ranking==0){
            ranking = newScore.size() + 1;
        }
        return ranking;
    }

    private void RemoveDuplicates(LinkedList<Score> holdScore){

        int scoreComp;
        String nameComp,categoryComp;
        for(int i = 0; i < holdScore.size(); i++){

            if(holdScore.get(i)!=null) {
                scoreComp = holdScore.get(i).score;
                nameComp = holdScore.get(i).name;
                categoryComp = holdScore.get(i).levelType;
            }
            else {
                break;
            }

            for(int j = 0; j < holdScore.size(); j++){

                if(holdScore.get(j)!=null) {
                    if ((scoreComp == holdScore.get(j).score && nameComp.equalsIgnoreCase(holdScore.get(j).name) && categoryComp.equalsIgnoreCase(holdScore.get(j).levelType))&&(i!=j)){
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if(getName.getText().length()==0) {
            name.setText("Please enter valid username.");
        }
        else {
            String temp = getName.getText();
            if(temp.length()>8) {
                this.username = temp.substring(0,8);
            }
            else {
                this.username = temp;
            }
            name.setText("Name saved.");

            if(level!=0) {
                Add(newScore, getRanking(level), username, levelType[choice[level-1][0]],score[level][1], score[level][0]);
            }
            else{
                if(choice[0][0]==choice[1][0]&&choice[1][0]==choice[2][0]&&choice[2][0]==choice[3][0]){
                    Add(newScore, getRanking(level), username, levelType[choice[0][0]],score[level][1], score[level][0]);
                }
                else if((choice[0][0]-1)/4==0&&(choice[1][0]-1)/4==0&&(choice[2][0]-1)/4==0&&(choice[3][0]-1)/4==0&&choice[0][0]!=0&&choice[1][0]!=0&&choice[2][0]!=0&&choice[3][0]!=0){
                    Add(newScore, getRanking(level), username, finalType[0],score[level][1], score[level][0]);
                }
                else if((choice[0][0]-1)/4==1&&(choice[1][0]-1)/4==1&&(choice[2][0]-1)/4==1&&(choice[3][0]-1)/4==1){
                    Add(newScore, getRanking(level), username, finalType[1],score[level][1], score[level][0]);
                }
                else {
                    Add(newScore, getRanking(level), username, finalType[2],score[level][1], score[level][0]);
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

