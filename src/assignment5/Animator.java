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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Animator {

    // Right now, everything has a 5x5 box
    public static void draw(Critter[][] critters) {
        for(int r = 0; r < critters.length; r++) {
            for(int c = 0; c < critters[r].length; c++) {
                if(critters[r][c] == null) {
                    Main.world.add(new Rectangle(11, 11, Color.WHITE), r, c);
                } else {
                    Main.world.add(new Rectangle(11, 11, Color.WHITE), r, c);
                    Main.world.add(getShape(critters[r][c].viewShape(), critters[r][c].viewFillColor(),
                            critters[r][c].viewOutlineColor(), 10), r, c);
                }
            }
        }
    }

    public static Shape getShape(Critter.CritterShape shape, Color c, Color o, int size) {
        Shape s;
        switch(shape) {
            case CIRCLE: s = new Circle(size/2);
                s.setFill(c); s.setStroke(o); return s;
            case SQUARE: s = new Rectangle(size, size, c);
                s.setStroke(o);
                return s;
            case DIAMOND: Polygon d = new Polygon();
                d.getPoints().addAll(new Double[] {
                        0.0, (double)size/2,
                        (double)size/2, 0.0,
                        (double)size, (double) size/2,
                        (double) size/2, (double)size
                });
                d.setFill(c);
                d.setStroke(o);
                return (Shape) d;
            case TRIANGLE: Polygon t = new Polygon();
                t.getPoints().addAll(new Double[] {
                        (double) size/2, 0.0,
                        0.0, (double)size,
                        (double) size, (double) size
                });
                t.setFill(c);
                t.setStroke(o);
                return (Shape) t;
            case STAR: Polygon st = new Polygon();
                st.getPoints().addAll(new Double[] {
                        (double) size/2, 0.0,
                        0.0, (double)size/1.4,
                        (double) size, (double) size/1.5
                });
                st.setFill(c);
                st.setStroke(o);
                Polygon st2 = new Polygon();
                st2.getPoints().addAll(new Double[] {
                        0.0, (double)size/3.0,
                        (double) size, (double) size/3.0,
                        (double) size/2, (double) size
                });
                st2.setFill(c);
                st2.setStroke(o);
                return Shape.union(st, st2);
            default: return null;
        }
    }
}
