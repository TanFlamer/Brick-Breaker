package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomConsole extends JDialog {

    private static final String TITLE = "Customise Levels";

    private JFrame owner;
    private CustomPanel customPanel;
    private HomeMenu homeMenu;

    public CustomConsole(JFrame owner,HomeMenu homeMenu) {

        this.owner = owner;
        this.homeMenu = homeMenu;

        initialize();
        customPanel = new CustomPanel();
        this.add(customPanel,BorderLayout.CENTER);
        this.setSize(300,300);
        this.pack();
    }

    private void initialize() {

        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                homeMenu.repaint();
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

