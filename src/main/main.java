package main;

import Farming.Bank;
import Farming.getPlanks;
import Farming.walkToGraveyard;
import Training.buyTrainingSupplies;
import Training.getTrainingSupplies;
import Training.killChickens;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.util.ArrayList;


@ScriptManifest(author = "Ryan", info = "Who doesn't love a cool beer?", name = "Plank Scrounger", version = 1, logo = "")


public class main extends Script {


    boolean hasTeleported;
    ArrayList<Position> graveyardPlanks = new ArrayList<>();
    ArrayList<Position> graveyardRoute = new ArrayList<>();

    public long startTime;
    long timeRan;
    long totalTripTime;
    long startTripTime;
    String currentAction = null;
    int totalPlanks = 0;
    boolean lockPlank;

    Position lastPlank;
    Position nextPlank;
    int supplyCost = 0;
    
    ArrayList<Node> trainingNodes;
    ArrayList<Node> farmingNodes;

    ArrayList<String> itemsToBuy = new ArrayList<>();

    @Override
    public void onStart() {
        graveyardPlanks.add(new Position(3148, 3671, 0));
        graveyardPlanks.add(new Position(3154, 3670, 0));
        graveyardPlanks.add(new Position(3154, 3659, 0));

        graveyardPlanks.add(new Position(3171, 3680, 0));
        graveyardPlanks.add(new Position(3182, 3669, 0));

        graveyardRoute.add(new Position(3148, 3671, 0));
        graveyardRoute.add(new Position(3154, 3670, 0));
        graveyardRoute.add(new Position(3154, 3659, 0));
        //centre
        graveyardRoute.add(new Position(3166, 3670, 0));
        //plank 4, 5
        graveyardRoute.add(new Position(3171, 3680, 0));
        graveyardRoute.add(new Position(3182, 3669, 0));
        //safespot
        graveyardRoute.add(new Position(3176,3654,0));
        graveyardRoute.add(new Position(3155,3654,0));
        graveyardRoute.add(new Position(3144,3664,0));





        startTime = System.currentTimeMillis();

        trainingNodes = new ArrayList<Node>();
        trainingNodes.add(new buyTrainingSupplies(this));
        trainingNodes.add(new getTrainingSupplies(this));
        trainingNodes.add(new killChickens(this));


        farmingNodes = new ArrayList<>();
        //farmingNodes.add(new buyRunSupplies(this));
        farmingNodes.add(new Bank(this));
        farmingNodes.add(new walkToGraveyard((this)));
        farmingNodes.add(new getPlanks(this));




    }


    @Override
    public int onLoop() throws InterruptedException {
    

	if(getSkills().getVirtualLevel(Skill.MAGIC) < 25) {
		
		for(Node n : trainingNodes) {
			if(n.validate()) {
				n.execute();
			}
		}
		
		
		
	}else {
		
  		for(Node n : farmingNodes) {
			if(n.validate()) {
				n.execute();
			}
		}
		
	}
	return 0;
    }
	
	//hello world
    

  

	public void onPaint(Graphics2D g) {

        timeRan = System.currentTimeMillis() - startTime;
        totalTripTime = System.currentTimeMillis() - startTripTime;
        long timeMins = Math.round(timeRan/1000/60);

        if (getSkills().getVirtualLevel(Skill.MAGIC) >= 25) {
            g.drawString("Time Ran: " + formatTime(timeRan), 15, 300);
            g.drawString("Planks Scrounged: " + totalPlanks, 15, 320);
            g.drawString("Total Profit: " + ((totalPlanks * 369) - supplyCost) + "gp   (" + (totalPlanks * 369) / timeMins * 60 + " GP/h)", 15, 340);
            g.drawString("Current Action: " + currentAction, 15, 280);
        } else {
            g.drawString("Time Ran: " + formatTime(timeRan), 15, 300);
            g.drawString("Current Action: " + currentAction, 15, 280);
        }
    }
    public String formatTime(long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return (String.format("%02d:%02d:%02d", h, m, s));
    }
    
    public void setCurrentAction(String s) {
    	
    	currentAction = s;
    }


//
//    Position getNextPlank(Position p){
//
//
//        lastPlank = p;
//        nextPlank = graveyardPlanks.get(graveyardPlanks.indexOf(p) + 1);
//        lockPlank = true;
//
//
//
//
//
//        return nextPlank;
//    }

    public ArrayList<Position> getGraveyardPlanks() {
        return graveyardPlanks;
    }

    public ArrayList<Position> getGraveyardRoute() {
        return graveyardRoute;
    }



    public ArrayList<String> getItemsToBuy() {

        return itemsToBuy;
    }

    public void addItemToBuy(String Item) {

        itemsToBuy.add(Item);
    }

    public void removeItemsToBuy(String Item) {

        itemsToBuy.remove(Item);
    }

    public int getCurrentSupplyCost() {

        return supplyCost;

    }

    public void setSupplyCost(int i) {

        supplyCost = i;
    }

    public int getTotalPlanks() {

        return totalPlanks;
    }

    public void setTotalPlanks(int i) {

        totalPlanks = i;
    }
}
