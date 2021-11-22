package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomConsole extends JDialog {

    private static final String TITLE = "Customise Levels";

    private JFrame owner;
    private CustomPanel customPanel;
    private HomeMenuNew homeMenuNew;

    public CustomConsole(JFrame owner,HomeMenuNew homeMenuNew){

        this.owner = owner;
        this.homeMenuNew = homeMenuNew;

        initialize();
        customPanel = new CustomPanel();
        this.add(customPanel,BorderLayout.CENTER);
        this.setSize(300,300);
    }

    private void initialize() {

        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                homeMenuNew.repaint();
            }

            @Override
            public void windowActivated(WindowEvent windowEvent) { //when debug console loaded
                setLocation(); //set debug console location
            }
        });

        this.setFocusable(true);
    }

    private void setLocation(){ //set debug console location
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }
}
