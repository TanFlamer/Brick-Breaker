

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Public class CustomConsole is the container for the CustomPanel. Window listeners are added to this public class
 * to repaint HomeMenu when CustomConsole closes or centers the screen when CustomConsole opens.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class CustomConsole extends JDialog {

    /**
     * Title for the CustomConsole.
     */
    private static final String TITLE = "Customise Levels";

    /**
     * JFrame which is used to center the CustomConsole.
     */
    private JFrame owner;
    /**
     * The CustomPanel which is contained within CustomConsole.
     */
    private CustomPanel customPanel;
    /**
     * HomeMenu which is repainted when CustomConsole closes.
     */
    private HomeMenu homeMenu;

    /**
     * Double array of Integer to hold all player choices to send to GameBoard for level generation.
     */
    int[][] choice;

    /**
     * This constructor is used to load the CustomPanel into the CustomConsole and to add in window listeners.
     * @param owner JFrame which is used to center the CustomConsole.
     * @param homeMenu HomeMenu which is repainted when CustomConsole closes.
     */
    public CustomConsole(JFrame owner,HomeMenu homeMenu) {

        this.owner = owner;
        this.homeMenu = homeMenu;

        initialize();
        customPanel = new CustomPanel();
        this.choice = customPanel.getChoice();
        this.add(customPanel,BorderLayout.CENTER);
        this.setSize(300,300);
        this.pack();
    }

    /**
     * This method is used to set the CustomConsole title and to add in window listeners.
     */
    private void initialize() {

        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.addWindowListener(new WindowAdapter() {
            /**
             * This window listener is called when the CustomConsole is closing and repaints the HomeMenu.
             * @param windowEvent This parameter is used to track the CustomConsole window.
             */
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                homeMenu.repaint();
            }

            /**
             * This window listener is called when the CustomConsole is activated and centers the CustomConsole on
             * the screen. The choices are also set to the player choices and the choice message is reset.
             *
             * @param windowEvent This parameter is used to track the CustomConsole window.
             */
            @Override
            public void windowActivated(WindowEvent windowEvent) { //when debug console loaded
                setLocation(); //set debug console location
                customPanel.setIndex();
                customPanel.resetMessage();
            }
        });

        this.setFocusable(true);
    }

    /**
     * This method is used to center the CustomConsole by using the JFrame location.
     */
    private void setLocation(){ //set debug console location
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }

    /**
     * This method returns the player choices as a double array of integers.
     * @return The player choices as a double array of integers is returned.
     */
    public int[][] getChoice(){
        return choice;
    }
}

