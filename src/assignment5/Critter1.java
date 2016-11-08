/* EE422C Project 4 submission by
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

import java.util.*;

/**
 * This Cat is extremely capricious and decides almost everything based on the
 * random number generator. However, because Craig already uses the letter C, it
 * hates Craigs and will fight them at any opportunity. The more fights a Cat has
 * won, the more aggressive it is.
 * It will also reproduce somewhat frequently and run around every other turn.
 */
public class Critter1 extends Critter{

    private static int numCats = 0;
    private int catId;
    private int numFights;
    private int turnsAlive;
    private boolean running;

    /**
     * Sole constructor. Creates a new Cat
     */
    public Critter1(){
        catId = numCats;
        numCats++;
        numFights = 0;
        turnsAlive++;
        running = (Critter.getRandomInt(2) == 0);
    }
    
    /**
     * String representation of a Cat for output to user.
     * @return A single letter representation of a Cat
     */
    public String toString(){
        return "1";
    }

    /**
     * Checks if a Cat will fight with another Critter.
     * @param other The string representation of the other critter
     * @return true if the cat will fight, false otherwise
     */
    public boolean fight(String other){
        boolean willFight = false;

        if(other.equals("@") || other.equals("C")){
            willFight = true;
        } else {
            if(running || Critter.getRandomInt(2 + numFights / 2) != 0){
                willFight = true;
            } else {
                walk(Critter.getRandomInt(8));
            }
        }

		if(willFight) numFights++;
		
        return willFight;
    }

    /**
     * Performs one timestep for the Cat.
     */
    public void doTimeStep(){
        running = !running;

        if(running){
            run(Critter.getRandomInt(8));
        } else {
			if(getEnergy() >= Params.min_reproduce_energy){
				Critter1 child = new Critter1();
				reproduce(child, Critter.getRandomInt(8));
			}
        }

        turnsAlive++;
        
    }

    /**
     * Lists the stats of the Cats passed in from the general population.
     * @param cats A list of all cats in the population right now
     */
    public static void runStats(List<Critter> cats){
        int numNoFights = 0;

        int maxTurns = 0;
		ArrayList<Integer> elders = new ArrayList<>();

        int maxFights = 0;
        int bestFighter = 0;
        
        
        for(Critter e : cats){
            Critter1 cat = (Critter1) e;

            if(cat.numFights == 0){
                numNoFights++;
            } else if(cat.numFights >= maxFights){
                maxFights = cat.numFights;
                bestFighter = cat.catId;
            }

            if(cat.turnsAlive > maxTurns){
				elders.clear();
                maxTurns = cat.turnsAlive;
				elders.add(cat.catId);
            } else if(cat.turnsAlive == maxTurns){
				elders.add(cat.catId);
			}
        }

        String outText = ("There have been " + numCats + " cats in total." +"\n");
        outText += ("Of those, " + cats.size() + " cats are currently alive." + "\n");
        outText += (cats.size() - numNoFights + " have survived a fight." + "\n");
		if(maxFights != 0){
			outText += ("Cat number " + bestFighter + " is the best fighter." + "\n");
		}

		if(elders.size() == 1){
			outText += ("Cat number " + elders.get(0) + " rules the cats, having lived for " + maxTurns + " step(s).");
		} else if(elders.size() > 1){
			outText += ("Cats number ");

			for(int i = 0; i < elders.size() - 1; i++){
				outText += (elders.get(i) + " ");
			}

			outText += ("and " + elders.get(elders.size()) + " rule the cats, having lived for " + maxTurns + " step(s).");
		}

		Main.setOutputText(outText);

    }

    public CritterShape viewShape() { return CritterShape.STAR; }
    public Color viewFillColor() { return Color.BROWN; }

}
