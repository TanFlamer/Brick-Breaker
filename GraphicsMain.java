package Main;

import java.awt.*;
import java.io.IOException;

/**
 * Public class GraphicsMain is used to load the GameFrame which will load all the different screens of the game
 * and then show the HomeMenu.
 *
 * @author TanZhunXian, Filippo Ranza
 * @version 1.0
 * @since 28/11/2021
 */
public class GraphicsMain {
    /**
     * This method is used to load the GameFrame into the event queue.
     * @param args This is the main class argument.
     */
    public static void main(String[] args) { //initialize game
        EventQueue.invokeLater(() -> {
            try {
                new GameFrame().initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
