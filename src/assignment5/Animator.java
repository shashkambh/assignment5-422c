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
import javafx.scene.shape.*;

import java.util.HashSet;

public class Animator {

    // "x y"
    public static HashSet<String> changed = new HashSet<>();

    // Right now, everything has a 5x5 box
    public static void draw(Critter[][] critters, boolean firstDisplay) {
		int width = Main.SCREENWIDTH - 400;
		int height = Main.SCREENHEIGHT - 100 - Params.world_width * 2;
		
		int size = Math.min(height / Params.world_height, width / Params.world_width);

        for(int r = 0; r < critters.length; r++) {
            for(int c = 0; c < critters[r].length; c++) {
                if(firstDisplay || (!firstDisplay && (changed.contains(r + " " + c) || critters[r][c] != null))) {
                    Main.world.add(new Rectangle(size, size, Color.WHITE), r, c);
                    Main.world.add(getShape(Critter.CritterShape.DIAMOND, Color.WHITE, Color.WHITE, size - 1), r, c);
                    if(critters[r][c] == null) changed.remove(r + " " + c);
                }
                if(critters[r][c] != null) {
                    Main.world.add(getShape(critters[r][c].viewShape(), critters[r][c].viewFillColor(),
                            critters[r][c].viewOutlineColor(), size - 1), r, c);

                    changed.add(r + " " + c);
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
                size--;
                st.getPoints().addAll(new Double[] {
                        (double) size/2, 0.5,
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
