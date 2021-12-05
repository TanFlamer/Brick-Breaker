import java.awt.*;
import java.io.IOException;

public class GraphicsMain {
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
