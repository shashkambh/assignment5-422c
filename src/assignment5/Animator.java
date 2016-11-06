/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Shashank Kambhampati
 * skk834
 * 16445
 * Pranav Harathi
 * sh44674
 * 16460
 * Slip days used: 0
 * Fall 2016
 */
package assignment5;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Animator {

    // Right now, everything has a 5x5 box
    public static void draw(Critter[][] critters) {
        for(int r = 0; r < critters.length; r++) {
            for(int c = 0; c < critters[r].length; c++) {
                if(critters[r][c] == null) {
                    Main.world.add(new Rectangle(10, 10, Color.WHITE), r, c);
                } else {
                    Main.world.add(getShape(critters[r][c].viewShape(), critters[r][c].viewFillColor(), 10), r, c);
                }
            }
        }
    }

    public static Shape getShape(Critter.CritterShape shape, Color c, int size) {
        Shape s;
        switch(shape) {
            case CIRCLE: s = new Circle(size/2);
                s.setFill(c); return s;
            case SQUARE: s = new Rectangle(size, size, c);
                return s;
            default: return null;
        }
    }
}
