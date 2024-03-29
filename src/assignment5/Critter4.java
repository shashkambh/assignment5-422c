/* EE422C Project 5 submission by
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

import javafx.scene.paint.Color;

import java.util.TreeSet;

/**
 * Cows are peaceful grazing animals that like to follow a pattern.
 * They simply feed off of Algae and move in a nice box pattern.
 * Poor guys, they never fight back.
 */
public class Critter4 extends Critter {

    private static int numCows;

    private int patternSize;
    private int pathLength;
    private int pathCtr;
    private int direction;

    /**
     * Creates a new Cow
     */
    public Critter4(){
        patternSize = Critter.getRandomInt(6) + 3; // has to be at least three
        pathLength = (patternSize*patternSize) - ((patternSize - 2)*(patternSize - 2));
        direction = 0; // ->|<-| ...
        pathCtr = 1;
        numCows++;
    }

    /**
     * Returns a string representation of the Cow
     * @return a 1 character string representing the Cow
     */
    public String toString(){
        return "4";
    }

    /**
     * Checks if Cow will fight.  Cows only fight Algae
     * @param other The string representation of the other critter
     * @return true if the cat will fight, false otherwise
     */
    public boolean fight(String other){
        return other.equals("@");
    }

    /**
     * Performs one timestep for the Cow
     */
    public void doTimeStep(){
		if(look(2, false).equals("@")){
			walk(2);
		} else {
			walk(direction);
			pathCtr++;
			if(pathCtr > pathLength) pathCtr = 0;
			if((pathCtr / patternSize) == 0) {
				direction = 0;
			} else if((pathCtr / patternSize) == 1) {
				direction = 6;
			} else if((pathCtr / patternSize) == 2) {
				direction = 4;
			} else if((pathCtr / patternSize) == 3) {
            direction = 2;
			}
			System.out.println(direction);
		}
    }


    /**
     * Lists the stats of the Cows passed in from the general population.
     * @param cows A list of all cows in the population right now
     */
    public static void runStats(java.util.List<Critter> cows){
		String outText = "There are " + numCows + " Cows.\n"; 
        TreeSet<Integer> patterns = new TreeSet<>();
        for(Critter c : cows) {
            if(!patterns.contains(((Critter4) c).patternSize)) {
                patterns.add(((Critter4) c).patternSize);
            }
        }
        outText += ("There are " + patterns.size() + " different grazing patterns.\n");
        for(int p : patterns) {
			outText += p + " by " + p + "\n";
        }

		Main.setOutputText(outText);
    }

    public CritterShape viewShape() { return CritterShape.TRIANGLE; }
    public Color viewFillColor() { return Color.BLUE; }

}
