/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

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
    public static void main(String[] args){ //initialize game
        EventQueue.invokeLater(() -> {
            try {
                new GameFrame().initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
