package Main.MVC;

import java.awt.*;

/**
 * Public interface Renderer is used to create an interface for renderers to be created. It is useful if more than one
 * game renderer is needed.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public interface Renderer {
    /**
     * This method is to be overridden in the subclass and is used to render the game graphics.
     * @param g This parameter is used to control the graphics of the new classes.
     */
    void render(Graphics g);
}
