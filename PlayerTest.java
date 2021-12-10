package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp() {
        player = new Player(new Point(100,100),150,10,new Dimension(600,450));
    }

    @AfterEach
    void tearDown() {
        player = null;
    }

    @Test
    void getMidPoint() {
        assertEquals(player.getMidPoint(),new Point(100,100));
    }

    @Test
    void getMoveAmount() {
        assertEquals(player.getMoveAmount(),0);
    }

    @Test
    void getMin() {
        assertEquals(player.getMin(),75);
    }

    @Test
    void getMax() {
        assertEquals(player.getMax(),525);
    }

    @Test
    void getPlayerFace() {
        assertEquals(player.getPlayerFace(),new Rectangle(new Point(25, 100),new Dimension(150,10)));
    }

    @Test
    void getBorder() {
        assertEquals(player.getBorder(),Color.GREEN.darker().darker());
    }

    @Test
    void getInner() {
        assertEquals(player.getInner(),Color.GREEN);
    }

    @Test
    void getTop() {
        assertEquals(player.getTop(),0);
    }

    @Test
    void getBottom() {
        assertEquals(player.getBottom(),440);
    }

    @Test
    void getVerticalMoveAmount() {
        assertEquals(player.getVerticalMoveAmount(),0);
    }

    @Test
    void setMidPoint() {
        player.setMidPoint(new Point(200,200));
        assertEquals(player.getMidPoint(),new Point(200,200));
    }

    @Test
    void setMoveAmount() {
        player.setMoveAmount(5);
        assertEquals(player.getMoveAmount(),5);
    }

    @Test
    void setPlayerFace() {
        player.setPlayerFace(new Rectangle(new Point(200, 200),new Dimension(100,20)));
        assertEquals(player.getPlayerFace(),new Rectangle(new Point(200, 200),new Dimension(100,20)));
    }

    @Test
    void setVerticalMoveAmount() {
        player.setVerticalMoveAmount(5);
        assertEquals(player.getVerticalMoveAmount(),5);
    }
}